package com.castaware.nocturne.scripts;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.castaware.nocturne.datatypes.Wallet;
import com.castaware.nocturne.service.ConsolidationService;

import eic.tcc.dao.Dao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-servlet.xml"
})
public class ResetWallets 
{
	@Autowired
	Dao dao;
	
	@Autowired
	ConsolidationService consolidationService;
	
	@Test
	public void test() throws Exception
	{
		try
		{
			dao.executeSQL("update wallet set deposited=0,withdrawn=0");
			
			List<Wallet> wallets = dao.retrieveAll(Wallet.class);
			
			for (Wallet wallet : wallets) 
			{
				consolidationService.reset(wallet);
				consolidationService.logWallet(wallet);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}	
}
