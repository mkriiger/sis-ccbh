package com.litesoftwares.coingecko;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.litesoftwares.coingecko.constant.GeckoCurrency;
import com.litesoftwares.coingecko.impl.CoinGeckoApiClientImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-servlet.xml"
})
public class CoinGeckoApiClientTest 
{
	@Test
	public void test()
	{
		CoinGeckoApiClientImpl apiClient = new CoinGeckoApiClientImpl();
		Object obj = apiClient.getPrice("apenft",GeckoCurrency.USD);
		System.out.println(obj);
	}
}
