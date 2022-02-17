package com.castaware.nocturne.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.apache.commons.math3.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.castaware.nocturne.api.BinanceApi;
import com.castaware.nocturne.api.BinanceException;
import com.castaware.nocturne.api.domain.BinanceCandlestick;
import com.castaware.nocturne.api.domain.BinanceInterval;
import com.castaware.nocturne.datatypes.PriceAtMinute;
import com.castaware.nocturne.util.NocDateFormat;
import com.litesoftwares.coingecko.constant.GeckoCurrency;
import com.litesoftwares.coingecko.impl.CoinGeckoApiClientImpl;

import eic.tcc.dao.DaoLike;
import eic.tcc.dao.Dao;

@Component
public class PriceService 
{
	@Autowired
	private BinanceApi api;
	
	@Autowired
	private Dao dao;
	
	@Autowired
	private AssetService assetService;
	
	private long lastBinanceFetch;
	private Map<String,BigDecimal> binanceQuotes;
	private Map<String,Pair<Long,BigDecimal>> liveCache = new TreeMap<String,Pair<Long,BigDecimal>>();
	private CoinGeckoApiClientImpl geckoApi = new CoinGeckoApiClientImpl();
	
	@PostConstruct
	public void refreshQuotes()
	{
		lastBinanceFetch = System.currentTimeMillis();
		binanceQuotes = api.fetchQuotes();		
	} 
	
	public BigDecimal fetchBinanceQuote(String pair) 
	{
		if (System.currentTimeMillis()-lastBinanceFetch>60000)
			refreshQuotes();
			
		return binanceQuotes.get(pair);
	}
	
	public BigDecimal priceAtLive(String asset)
	{
		Pair<Long,BigDecimal> pricePair=null;
		
		try
		{
			pricePair = liveCache.get(asset);
		}
		catch(Exception e)
		{
			
		}
		
		if (pricePair==null)
			pricePair=new Pair<Long,BigDecimal>(0l,BigDecimal.ZERO);
		
		Long timestamp = pricePair.getFirst();
		BigDecimal price = pricePair.getSecond();
		
		if (System.currentTimeMillis()-timestamp > 300000)
		{
			price = priceAtLiveBinance(asset);
			
			if (price == null)
			{
				price = priceAtLiveGecko(asset);
			}
			
			if (price == null)
			{
				price = assetService.getStubPrice(asset);
			}
			
			if (price == null)
			{
				throw new IllegalArgumentException("Could not fetch price for asset "+asset);
			}
			else
			{
				pricePair=new Pair<Long,BigDecimal>(System.currentTimeMillis(),price);
				liveCache.put(asset,pricePair);
			}
		}
		
		return price;
	}

	public BigDecimal priceAtLiveGecko(String asset) 
	{
		asset = assetService.getCoinGecko(asset);
		
		if (asset==null)
			return null;
		
		Map<String, Map<String, Double>> map = geckoApi.getPrice(asset,GeckoCurrency.USD);
		
		if (map.isEmpty())
			return null;
		else
			return new BigDecimal(map.get(asset).get("usd"));		
	}
	
	private BigDecimal priceAtLiveBinance(String asset) 
	{
		switch(asset)
		{
			case "USDT":
			case "USDC":
			case "BUSD":
			case "DAI":
				return BigDecimal.ONE;
		}
		
		BigDecimal price = null; 
		
		// Attempt - COINUSDT
		if (price == null)
		{
			price = fetchBinanceQuote(asset+"USDT");
		}
		
		// Attempt - COINBUSD
		if (price == null)
		{
			price = fetchBinanceQuote(asset+"BUSD");
		}
		
		// Attempt - USDTCOIN
		if (price == null)
		{
			price = fetchBinanceQuote("USDT"+asset);
			
			if (price!=null)
			{
				price = BigDecimal.ONE.divide(price,20,RoundingMode.HALF_EVEN);
			}
		}				
		
		// Attempt - COINBTC
		if (price == null)
		{
			BigDecimal btcPrice = fetchBinanceQuote(asset+"BTC");
			
			if (btcPrice!=null)
			{
				BigDecimal btcUsdtQuote = fetchBinanceQuote("BTCUSDT");
				price = btcPrice.multiply(btcUsdtQuote);
			}
		}
		
		return price;
	}
	
	// TODO - Refazer com mais elegância
	public void newPriceAtMinute(String asset, BigDecimal price, LocalDateTime timestamp)
	{
		timestamp = LocalDateTime.of(timestamp.getYear(),
									 timestamp.getMonth(),
									 timestamp.getDayOfMonth(),
									 timestamp.getHour(),
									 timestamp.getMinute(),00);
		
		PriceAtMinute toPriceAM = new PriceAtMinute(asset, price, timestamp);
		dao.persist(toPriceAM);
	}
	
	// TODO - Refazer com mais elegância
	public void updatePriceAtMinute(String fromAsset, BigDecimal fromAmount, String toAsset, BigDecimal toAmount, LocalDateTime timestamp)
	{
		timestamp = LocalDateTime.of(timestamp.getYear(),
									 timestamp.getMonth(),
									 timestamp.getDayOfMonth(),
									 timestamp.getHour(),
									 timestamp.getMinute(),00);
		
		BigDecimal price = null;
		
		try // to fetch fromAsset
		{
			price = priceAtMinute(fromAsset, timestamp);
			
			if (price!=null)
			{
				try
				{
					priceAtMinute(toAsset, timestamp);
				}
				catch(Exception e)
				{
					BigDecimal totalValue = fromAmount.multiply(price);
					BigDecimal toPrice = totalValue.divide(toAmount,20,RoundingMode.HALF_EVEN);
						
					PriceAtMinute toPriceAM = new PriceAtMinute(toAsset, toPrice, timestamp);
					dao.persist(toPriceAM);
				}
			}
			
			return;
		}
		catch(Exception e){}
		
		try // to fetch fromAsset
		{
			price = priceAtMinute(toAsset, timestamp);
			
			if (price!=null)
			{
				try
				{
					priceAtMinute(fromAsset, timestamp);
				}
				catch(Exception e)
				{
					BigDecimal totalValue = toAmount.multiply(price);
					BigDecimal fromPrice = totalValue.divide(fromAmount,20,RoundingMode.HALF_EVEN);
					
					PriceAtMinute fromPriceAM = new PriceAtMinute(fromAsset, fromPrice, timestamp);
					dao.persist(fromPriceAM);
				}
			}
			
			return;
		}
		catch(Exception e){}
		
		throw new IllegalStateException("Could not fetch price for "+fromAsset+" or "+toAsset+" in "+NocDateFormat.format_ddMMyyyyHHmm(timestamp));		
	}
		
	public BigDecimal priceAtMinute(String symbol, int year, int month, int day, int hour, int minute)
	{
		return priceAtMinute(symbol,
							 LocalDateTime.of(year,month,day,hour,minute,00),
							 LocalDateTime.of(year,month,day,hour,minute,59));
	}
	
	public BigDecimal priceAtMinute(String symbol, LocalDateTime minute)
	{
		return priceAtMinute(symbol,
							 LocalDateTime.of(minute.getYear(),minute.getMonth(),minute.getDayOfMonth(),minute.getHour(),minute.getMinute(),00),
							 LocalDateTime.of(minute.getYear(),minute.getMonth(),minute.getDayOfMonth(),minute.getHour(),minute.getMinute(),59));
	}

	private BigDecimal priceAtMinute(String asset, LocalDateTime start, LocalDateTime end)
	{
		if (asset.equals("WMATIC"))
			asset="MATIC";
		
		switch(asset)
		{
			case "USDT":
			case "USDC":
			case "BUSD":
			case "DAI":
				return BigDecimal.ONE;
		}
		
		BigDecimal price = null; 
		
		List<PriceAtMinute> result = dao.retrieveByManyLikes(PriceAtMinute.class,
															 new DaoLike("coin",asset),
															 new DaoLike("datetime",start));		
		if (result.size()>=1)
		{
			return result.get(0).getPrice();
		}
		else
		{
			long s = NocDateFormat.format_timestamp(start.minusHours(3));
			long e = NocDateFormat.format_timestamp(end.minusHours(3));
			
			// Attempt 0 - LIVE
			if (NocDateFormat.minutesSince(start)<5)
			{
				price = priceAtLive(asset);
			}
			
			// Attempt 1 - COINUSDT
			if (price == null)
			{
				price = fetchPriceBinance(asset+"USDT", s, e);
			}
			
			// Attempt 2 - USDTCOIN
			if (price == null)
			{
				price = fetchPriceBinance("USDT"+asset, s, e);
				
				if (price!=null)
				{
					price = BigDecimal.ONE.divide(price,20,RoundingMode.HALF_EVEN);
				}
			}
			
			// Attempt 3 - COINBTC
			if (price == null)
			{
				BigDecimal btcPrice = fetchPriceBinance(asset+"BTC", s, e);
				
				if (btcPrice!=null)
				{
					BigDecimal btcUsdtQuote = fetchPriceBinance("BTCUSDT", s, e);
					price = btcPrice.multiply(btcUsdtQuote);
				}
			}
			
			// Attempt 3 - COINBUSD
			if (price == null)
			{
				BigDecimal busdPrice = fetchPriceBinance(asset+"BUSD", s, e);
				
				if (busdPrice!=null)
				{
					BigDecimal busdUsdtQuote = fetchPriceBinance("BUSDUSDT", s, e);
					price = busdPrice.multiply(busdUsdtQuote);
				}
			}
			
			// No more attempts
			if (price == null)
			{
				throw new IllegalStateException("Could not fetch price for "+asset+" between "+NocDateFormat.format_yyyyMMddHHmmssHifen(start)+" and "+NocDateFormat.format_ddMMyyyyHHmm(end));
			}							
		}					
		
		PriceAtMinute priceAtMinute = new PriceAtMinute(asset, price, start);
		dao.persist(priceAtMinute);
		
		return price;
	}

	private BigDecimal fetchPriceBinance(String coin, long s, long e) 
	{
		try
		{
			List<BinanceCandlestick> candles = api.fetchCandles(coin,BinanceInterval.ONE_MIN,1,s,e);
			
			if(candles.size()==0)
			{
				return null;
			}
			else
			{
				BigDecimal price = candles.get(0).getOpen();
				return price;
			}
		}
		catch (BinanceException bae)
		{
			if (bae.getMessage().contains("Invalid symbol"))
				return null;
			else
				throw new IllegalArgumentException(bae);
		}
	}
}
