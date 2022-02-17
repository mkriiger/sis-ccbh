package com.castaware.nocturne.scripts;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.castaware.nocturne.datatypes.TransactionPoolSpotReward;
import com.castaware.nocturne.service.PriceService;

import eic.tcc.dao.Dao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-servlet.xml"
})
public class SyncTemp 
{	
	@Autowired
	Dao dao;
	
	@Autowired
	PriceService priceService;
	
	@Test
	public void sync()
	{
		LogManager.getLogger("com.castaware.nocturne").setLevel(Level.TRACE);
		
		try
		{
			List<TransactionPoolSpotReward> rewards = dao.retrieveAll(TransactionPoolSpotReward.class);	
			
			for(TransactionPoolSpotReward tx : rewards)
			{
				BigDecimal price = priceService.priceAtMinute(tx.getAsset(), tx.getTimestamp());
				tx.setQuote(price);				
				dao.persist(tx);
			}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}