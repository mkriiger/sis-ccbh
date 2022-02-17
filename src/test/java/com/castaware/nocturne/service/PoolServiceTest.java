package com.castaware.nocturne.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.castaware.nocturne.datatypes.Pool;
import com.castaware.nocturne.datatypes.PoolLiquidity;
import com.castaware.nocturne.datatypes.Wallet;

import eic.tcc.dao.Dao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-servlet.xml"
})
public class PoolServiceTest 
{
	@Autowired
	Dao dao;
	
	@Autowired
	PoolService poolService;
	
	@Test
	public void test() throws Exception
	{
		try
		{
			Wallet wallet = dao.retrieveBySingleLike(Wallet.class,"id","KEPLR_OSM").get(0);
			
			List<PoolLiquidity> pools = poolService.getLiquidityPools(wallet);
			
			for (Pool pool : pools) 
			{
				System.out.println(pool.getTransactions());
			}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
