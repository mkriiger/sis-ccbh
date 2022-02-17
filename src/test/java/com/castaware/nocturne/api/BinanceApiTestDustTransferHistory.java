package com.castaware.nocturne.api;

import org.junit.Test;

import com.castaware.nocturne.util.NocDateFormat;

public class BinanceApiTestDustTransferHistory extends BinanceApiTest 
{
	//
	// General Endpoints Test
	//
	@Test
	public void test() 
	{
		System.out.println(api.dustTransferHistory(
		NocDateFormat.parse_yyyyMMddHHmmssHifen("2021-02-01 00:00:00"),
		NocDateFormat.parse_yyyyMMddHHmmssHifen("2021-03-31 23:59:59")));
	}
}
