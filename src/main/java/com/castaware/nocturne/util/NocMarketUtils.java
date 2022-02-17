package com.castaware.nocturne.util;

import org.apache.commons.math3.util.Pair;

public class NocMarketUtils 
{
	public static Pair<String,String> splitPair(String market)
	{
		String firstCoin;
		String secondCoin;
		
		if (market.endsWith("USDT"))
		{
			firstCoin = market.replaceAll("USDT","");
			secondCoin = "USDT";
		}
		else if (market.endsWith("BUSD"))
		{
			firstCoin = market.replaceAll("BUSD","");
			secondCoin = "BUSD";
		}
		else if (market.endsWith("BNB"))
		{
			firstCoin = market.replaceAll("BNB","");
			secondCoin = "BNB";
		}
		else if (market.endsWith("BTC"))
		{
			firstCoin = market.replaceAll("BTC","");
			secondCoin = "BTC";
		}
		else if (market.endsWith("BRL"))
		{
			firstCoin = market.replaceAll("BRL","");
			secondCoin = "BRL";
		}
		else if (market.endsWith("DAI"))
		{
			firstCoin = market.replaceAll("DAI","");
			secondCoin = "DAI";
		}
		else if (market.endsWith("ETH"))
		{
			firstCoin = market.replaceAll("ETH","");
			secondCoin = "ETH";
		}
		else
		{
			throw new IllegalArgumentException("Tried to parse invalid pair: "+market);
		}
		
		return new Pair<String,String>(firstCoin,secondCoin);
	}

}
