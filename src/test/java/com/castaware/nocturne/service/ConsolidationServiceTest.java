package com.castaware.nocturne.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.castaware.nocturne.datatypes.Wallet;

import eic.tcc.dao.Dao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-servlet.xml"
})
public class ConsolidationServiceTest 
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
			Wallet wallet = dao.retrieveBySingleLike(Wallet.class,"id","META_RAFA_FTM").get(0);
			consolidationService.reset(wallet);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
