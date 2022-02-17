package com.castaware.nocturne.api;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.castaware.nocturne.api.domain.BinanceCandlestick;
import com.castaware.nocturne.api.domain.BinanceInterval;
import com.castaware.nocturne.api.wrappers.BwCriptoBuy;
import com.castaware.nocturne.api.wrappers.BwCriptoDeposit;
import com.castaware.nocturne.api.wrappers.BwCriptoSell;
import com.castaware.nocturne.api.wrappers.BwCriptoTrade;
import com.castaware.nocturne.api.wrappers.BwCriptoWithdraw;
import com.castaware.nocturne.api.wrappers.BwDustTransfer;
import com.castaware.nocturne.api.wrappers.BwFiatDeposit;
import com.castaware.nocturne.api.wrappers.BwFiatWithdraw;
import com.castaware.nocturne.api.wrappers.BwPoolShare;
import com.castaware.nocturne.util.NocDateFormat;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Component
@SuppressWarnings("unused")
public class BinanceApi 
{
    /* API key and Secret Key */
	@Value("${API_KEY}")
    public String apiKey;
	
	@Value("${SECRET_KEY}")
    public String secretKey;

    /* API Base URL's */    
	private final String API_V1    = "https://api.binance.com/api/v1/";
    private final String API_V3    = "https://api.binance.com/api/v3/";
    private final String WAPI      = "https://api.binance.com/wapi/";
    private final String WAPI_V3   = "https://api.binance.com/wapi/v3/";    
	private final String SAPI_V1   = "https://api.binance.com/sapi/v1/";
    private final String WEBSOCKET = "wss://stream.binance.com:9443/ws/";

    private Gson gson = new Gson();

    //
    // CONSTRUCTORS
    //
    public BinanceApi()
    {
    	
    }
    
    public BinanceApi(String apiKey, String secretKey) 
    {
		super();
		this.apiKey = apiKey;
		this.secretKey = secretKey;
	}    
    
    @PostConstruct
    public void init() throws BinanceException 
    {
        if (Strings.isNullOrEmpty(this.apiKey))
            throw new BinanceException("BINANCE_API_KEY is mandatory");
        if (Strings.isNullOrEmpty(this.secretKey))
            throw new BinanceException("BINANCE_SECRET_KEY is mandatory");
    }

	// 
    // GENERAL ENDPOINTS
    // 

    /**
     * Checking connectivity,
     * @return empty object
     * @throws BinanceException in case of any error
     */
    public JsonObject ping() throws BinanceException 
    {
        return simpleRequestAsJson(API_V1,"ping");
    }

    /**
     * Checking server time,
     * @return JsonObject, expected { serverTime: 00000 }
     * @throws BinanceException in case of any error
     */
    public JsonObject time() throws BinanceException 
    {
    	return simpleRequestAsJson(API_V1,"time");
    }
    
    /**
     * Getting status of the system
     * @return Temporary returns JsonObject
     * @throws BinanceException in case of any error
     */
     public JsonObject status() throws BinanceException 
     {
    	 return simpleRequestAsJson(WAPI_V3,"systemStatus.html");         
     }
    
    //
    // WALLET ENDPOINTS
    //
    public List<BwCriptoDeposit> depositCriptoHistory(LocalDateTime start, LocalDateTime end) throws BinanceException 
    {
     	JsonArray jsonArray = authRequestAsJsonArray(SAPI_V1,"capital/deposit/hisrec",
     											             "startTime="+NocDateFormat.format_timestamp(start),
     											             "endTime="+NocDateFormat.format_timestamp(end));
     	
     	return jsonArrayToWrapperList(jsonArray,BwCriptoDeposit.class);
    }  
     
    public List<BwCriptoWithdraw> withdrawCriptoHistory(LocalDateTime start, LocalDateTime end) throws BinanceException 
    {
    	JsonArray jsonArray = authRequestAsJsonArray(SAPI_V1,"capital/withdraw/history",
													 "startTime="+NocDateFormat.format_timestamp(start),
													 "endTime="+NocDateFormat.format_timestamp(end));
    	
    	return jsonArrayToWrapperList(jsonArray,BwCriptoWithdraw.class);
    } 
        
    public List<BwFiatDeposit> depositFiatHistory(LocalDateTime start, LocalDateTime end) throws BinanceException 
    {
    	JsonObject json = authRequestAsJson(SAPI_V1,"fiat/orders","transactionType=0",
											"beginTime="+NocDateFormat.format_timestamp(start),
											"endTime="+NocDateFormat.format_timestamp(end));
    	
    	return jsonArrayToWrapperList(json.getAsJsonArray("data"),BwFiatDeposit.class);
    }
        
    public List<BwFiatWithdraw> withdrawFiatHistory(LocalDateTime start, LocalDateTime end) throws BinanceException 
    {
    	JsonObject json = authRequestAsJson(SAPI_V1,"fiat/orders","transactionType=1",
											"beginTime="+NocDateFormat.format_timestamp(start),
											"endTime="+NocDateFormat.format_timestamp(end));
    	
    	return jsonArrayToWrapperList(json.getAsJsonArray("data"),BwFiatWithdraw.class);
    }
    
    //
    // TRADE ENDPOINTS
    //     
    public List<BwCriptoBuy> buyCriptoHistory(LocalDateTime start, LocalDateTime end) throws BinanceException 
    {
    	JsonObject json = authRequestAsJson(SAPI_V1,"fiat/payments","transactionType=0",
											"beginTime="+NocDateFormat.format_timestamp(start),
											"endTime="+NocDateFormat.format_timestamp(end));
    	
    	return jsonArrayToWrapperList(json.getAsJsonArray("data"), BwCriptoBuy.class);
    }
        
    public List<BwCriptoSell> sellCriptoHistory(LocalDateTime start, LocalDateTime end) throws BinanceException 
    {
    	JsonObject json = authRequestAsJson(SAPI_V1,"fiat/payments","transactionType=1",
											"beginTime="+NocDateFormat.format_timestamp(start),
											"endTime="+NocDateFormat.format_timestamp(end));
    	
    	return jsonArrayToWrapperList(json.getAsJsonArray("data"), BwCriptoSell.class);
    }
    
    public List<BwCriptoTrade> tradeHistory(String pair, LocalDateTime day) throws BinanceException 
    {
    	JsonArray jsonArray = authRequestAsJsonArray(API_V3,"myTrades","symbol="+pair,"limit=1000",
											"startTime="+NocDateFormat.format_timestamp(NocDateFormat.toStartOfDay(day)),
											"endTime="+NocDateFormat.format_timestamp(NocDateFormat.toEndOfDay(day)));
    	
    	return jsonArrayToWrapperList(jsonArray, BwCriptoTrade.class);
    }
    
    public List<BwDustTransfer> dustTransferHistory(LocalDateTime start, LocalDateTime end) throws BinanceException 
    {
    	List<BwDustTransfer> transfers = new ArrayList<BwDustTransfer>();
    	
    	JsonObject json = authRequestAsJson(SAPI_V1,"asset/dribblet",
						  		    			    "startTime="+NocDateFormat.format_timestamp(start),
													"endTime="+NocDateFormat.format_timestamp(end));
    	
    	JsonElement totalElement = json.get("total");
		int total = totalElement==null?0:totalElement.getAsInt();
    	
    	if (total>0)
    	{
    		JsonArray dribblets  = json.get("userAssetDribblets").getAsJsonArray();
    		
    		for (int i=0;i<dribblets.size();i++)
    		{
    			JsonArray details = dribblets.get(i).getAsJsonObject().get("userAssetDribbletDetails").getAsJsonArray();
    			
    			for (int j=0;j<details.size();j++)
    			{
    				JsonObject transfer = details.get(j).getAsJsonObject();
		
    				Long id   	   = transfer.get("transId").getAsLong()+j;
    				Long timestamp = transfer.get("operateTime").getAsLong();
		
    				String fromCoin = transfer.get("fromAsset").getAsString();
    				String toCoin   = "BNB";
    				String feeCoin  = "BNB";
				
    				BigDecimal fromAmount = transfer.get("amount").getAsBigDecimal();
    				BigDecimal toAmount   = transfer.get("transferedAmount").getAsBigDecimal();
    				BigDecimal feeCost    = transfer.get("serviceChargeAmount").getAsBigDecimal();
				
    				transfers.add(new BwDustTransfer(id, fromCoin, fromAmount, toCoin, toAmount, feeCoin, feeCost, timestamp));
    			}    				
			}
		}
    	
    	return transfers;
    }
    
    //
    // SWAP ENDPOINTS
    //
    public List<BwPoolShare> poolShares() throws BinanceException 
    {
    	JsonArray jsonArray = authRequestAsJsonArray(SAPI_V1,"bswap/liquidity");
    	List<BwPoolShare> shares = new ArrayList<BwPoolShare>();
    	
		for(int i=0;i<jsonArray.size();i++) 
		{
			JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
			
			double amount = jsonObject.get("share").getAsJsonObject().get("shareAmount").getAsDouble();
			
			if (amount>0)
			{				
				BwPoolShare wrapper = gson.fromJson(jsonObject.toString(), BwPoolShare.class);
				shares.add(wrapper);
			}
		}			
		
    	return shares;
    }
    
    //
    // MARKET ENDPOINTS
    //
    public Map<String, BigDecimal> fetchQuotes() throws BinanceException
    {
    	JsonArray jsonArray = new BinanceRequest(API_V1 + "ticker/allPrices").read().asJsonArray();
        
    	Map<String, BigDecimal> map = new ConcurrentHashMap<>();
    	
    	for (JsonElement elem : jsonArray) 
        {
            JsonObject obj = elem.getAsJsonObject();
            map.put(obj.get("symbol").getAsString(), obj.get("price").getAsBigDecimal());
        }
    	
        return map;
    }
    
    public List<BinanceCandlestick> fetchCandles(String symbol, BinanceInterval interval, int limit, long startTime, long endTime) throws BinanceException
    {
    	JsonArray candles = simpleRequestAsJsonArray(API_V3,"klines?symbol=" + symbol 
									                       + "&interval=" + interval.toString() 
									                       + "&limit=" + limit
									                       + "&startTime=" + startTime
									                       + "&endTime=" + endTime
									                       );
    	
        List<BinanceCandlestick> list = new LinkedList<>();
        
        for (JsonElement candle : candles)
        {
        	BinanceCandlestick c = new BinanceCandlestick(candle.getAsJsonArray());
        	list.add(c);
        }
        
        return list;
    }
    
    //
    // ASSETS ENDPOINTS 
    //
    public JsonObject spotFlexAssets() throws BinanceException
    {
    	JsonObject json = authRequestAsJson(API_V3,"account");
    	return json;
    }
    
    // 
    // REQUESTS 
    // 
    private JsonObject simpleRequestAsJson(String baseUrl,String url)
    {
    	String requestUrl = baseUrl + url;
		BinanceRequest binanceRequest = new BinanceRequest(requestUrl).read();
		return binanceRequest.asJsonObject();
    }
    
    private JsonArray simpleRequestAsJsonArray(String baseUrl, String url)
    {
    	String requestUrl = baseUrl + url;
		BinanceRequest binanceRequest = new BinanceRequest(requestUrl).read();
		return binanceRequest.asJsonArray();
    }
    
    private JsonObject authRequestAsJson(String baseUrl,String url, String... parameters)
    {
    	BinanceRequest binanceRequest = makeAuthRequest(baseUrl, url, parameters);		
		return binanceRequest.asJsonObject();
    }	   
     
    private JsonArray authRequestAsJsonArray(String baseUrl,String url, String... parameters)
    {
    	BinanceRequest binanceRequest = makeAuthRequest(baseUrl, url, parameters);		
		return binanceRequest.asJsonArray();
    }
    
    private BinanceRequest makeAuthRequest(String baseUrl, String url, String... parameters) 
    {
    	StringBuilder urlBuilder = new StringBuilder();
    	
    	urlBuilder.append(baseUrl);
    	urlBuilder.append(url);
    	urlBuilder.append("?timestamp="+(System.currentTimeMillis()-1000));
    	
    	for (String parameter : parameters) 
    	{
    		String[] split = parameter.split("=");
    		
			if (!parameter.contains("=") || split[0]==null || split[0].equals("") || split[1]==null || split[1].equals(""))
				throw new IllegalArgumentException("Invalid parameter "+parameter+" in request "+baseUrl+url);
    		
    		urlBuilder.append("&"+parameter);
		}
    	
		String requestUrl = urlBuilder.toString();
		BinanceRequest binanceRequest = new BinanceRequest(requestUrl).sign(apiKey, secretKey).read();
		return binanceRequest;
	}
    
    //
    // UTILITY
    //
    private <WRAPPER,OBJECT> List<OBJECT> jsonArrayToObjectList(JsonArray jsonArray,Class<WRAPPER> wrapperClass,Class<OBJECT> objectClass)
    {
		List<OBJECT>  objects  = new ArrayList<OBJECT>();
		List<WRAPPER> wrappers = jsonArrayToWrapperList(jsonArray, wrapperClass);
		
		for (WRAPPER wrapper : wrappers) 
		{
			try
			{
				Constructor<OBJECT> objectConstructor = objectClass.getConstructor(wrapperClass);
				OBJECT       	    object            = objectConstructor.newInstance(wrapper);
			
				objects.add(object);
			}
			catch(Exception e)
			{
				throw new IllegalArgumentException("Error while unwrapping "+wrapperClass.getSimpleName()+" to "+objectClass.getSimpleName(),e);
			}
		}
    	
    	return objects;
    }
	
    private <WRAPPER> List<WRAPPER> jsonArrayToWrapperList(JsonArray jsonArray,Class<WRAPPER> objectClass)
    {
    	List<WRAPPER> wrappers = new ArrayList<WRAPPER>();
    	
    	if(jsonArray==null)
			return wrappers;
    	
    	for(int i=0;i<jsonArray.size();i++)
    	{
    		JsonObject json    = jsonArray.get(i).getAsJsonObject();
    		WRAPPER    wrapper = gson.fromJson(json.toString(), objectClass);
    		wrappers.add(wrapper);
    	}
    	
    	return wrappers;
    }    
}


