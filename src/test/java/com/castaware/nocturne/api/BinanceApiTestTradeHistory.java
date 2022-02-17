package com.castaware.nocturne.api;

import org.junit.Test;

import com.castaware.nocturne.api.wrappers.BwCriptoTrade;
import com.castaware.nocturne.util.NocDateFormat;

public class BinanceApiTestTradeHistory extends BinanceApiTest 
{
	//
	// General Endpoints Test
	//
	@Test
	public void test() 
	{
		for(BwCriptoTrade o : api.tradeHistory("LITBTC",
				NocDateFormat.parse_yyyyMMddHHmmssHifen("2021-02-14 00:00:00")))
		{
			System.out.println(o);
			System.out.println(NocDateFormat.format_ddMMyyyyHHmm(NocDateFormat.parse_timestamp(new Long(o.time))));
		}
		
		for(BwCriptoTrade o : api.tradeHistory("STRAXBTC",
				NocDateFormat.parse_yyyyMMddHHmmssHifen("2021-02-16 00:00:00")))
		{
			System.out.println(o);
			System.out.println(NocDateFormat.format_ddMMyyyyHHmm(NocDateFormat.parse_timestamp(new Long(o.time))));
		}
		
		for(BwCriptoTrade o : api.tradeHistory("GLMBTC",
				NocDateFormat.parse_yyyyMMddHHmmssHifen("2021-02-16 00:00:00")))
		{
			System.out.println(o);
			System.out.println(NocDateFormat.format_ddMMyyyyHHmm(NocDateFormat.parse_timestamp(new Long(o.time))));
		}
		
		
	}
}
