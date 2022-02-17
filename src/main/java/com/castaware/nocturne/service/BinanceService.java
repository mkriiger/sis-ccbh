package com.castaware.nocturne.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.castaware.nocturne.api.BinanceApi;
import com.castaware.nocturne.util.NocNumberFormat;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
public class BinanceService 
{
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private BinanceApi api;
	
//	public Map<String,BigDecimal> myBinanceAssets()
//	{
//		Map<String,BigDecimal> assets = new TreeMap<String,BigDecimal>();
//		myAssetsAdd(assets,myBinanceSpotAssets());
//		myAssetsAdd(assets,myBinanceFlexAssets());		
//		return assets;
//	}
	
	public Map<String,BigDecimal> myBinanceSpotAssets()
	{
		LOG.trace("> Retrieving Spot Assets");		
		Map<String,BigDecimal> assets = new TreeMap<String,BigDecimal>();
		
		JsonObject root = api.spotFlexAssets();
		JsonArray balances = root.get("balances").getAsJsonArray();
		
		for (int i=0;i<balances.size();i++)
		{
			JsonObject balance = balances.get(i).getAsJsonObject();
			
			String	   asset = balance.get("asset").getAsString();
			BigDecimal free = balance.get("free").getAsBigDecimal();
			BigDecimal locked = balance.get("locked").getAsBigDecimal();
			
			if (free.doubleValue()+locked.doubleValue()>0 && !asset.startsWith("LD"))
			{
				if (asset.equals("ERD"))
					asset="EGLD";
		
				LOG.trace(String.format(">> [SPOT] %s",NocNumberFormat.format(free,asset)));					
				assets.put(asset,free.add(locked));				
			}
		}				
		
		return assets;
	}	
	
	public Map<String,BigDecimal> myBinanceFlexAssets()
	{
		LOG.trace("> Retrieving Flex Assets");
		Map<String,BigDecimal> assets = new TreeMap<String,BigDecimal>();
		
		JsonObject root = api.spotFlexAssets();
		JsonArray balances = root.get("balances").getAsJsonArray();
		
		for (int i=0;i<balances.size();i++)
		{
			JsonObject balance = balances.get(i).getAsJsonObject();
			
			String	   asset = balance.get("asset").getAsString();
			BigDecimal free = balance.get("free").getAsBigDecimal();
			BigDecimal locked = balance.get("locked").getAsBigDecimal();
			
			if (free.doubleValue()+locked.doubleValue()>0 && asset.startsWith("LD"))
			{
				asset = asset.substring(2);					
				
				if (asset.equals("ERD"))
					asset="EGLD";
		
				LOG.trace(String.format(">> [EARN] %s",NocNumberFormat.format(free,asset)));					
				assets.put(asset,free.add(locked));				
			}
		}				
		
		return assets;
	}	
	
//	public Map<String,BigDecimal> myBinancePooledAssets()
//	{
//		LOG.trace("> Retrieving Pooled Assets");
//		List<BwPoolShare> poolShares = api.poolShares();
//		Map<String,BigDecimal> poolAssets = new TreeMap<String,BigDecimal>();
//		
//		for(BwPoolShare poolShare : poolShares) 
//		{
//			String     firstAsset = poolShare.getFirstAsset();
//			BigDecimal firstValue = poolShare.getFirstAmount();
//			BigDecimal firstAcc   = poolAssets.get(firstAsset);
//			
//			if (firstAcc==null)
//				firstAcc = BigDecimal.ZERO;
//			
//			poolAssets.put(firstAsset, firstAcc.add(firstValue));
//			
//			String     secondAsset = poolShare.getSecondAsset();
//			BigDecimal secondValue = poolShare.getSecondAmount();
//			BigDecimal secondAcc   = poolAssets.get(secondAsset);
//			
//			if (secondAcc==null)
//				secondAcc = BigDecimal.ZERO;
//			
//			poolAssets.put(secondAsset, secondAcc.add(secondValue));
//		}								
//		
//		for (Entry<String,BigDecimal> assetEntry : poolAssets.entrySet())
//		{
//			LOG.trace(String.format(">> [POOL-ASSET] %s",NocNumberFormat.format(assetEntry.getValue(),assetEntry.getKey())));
//		}
//		
//		return poolAssets;
//	}	
}
