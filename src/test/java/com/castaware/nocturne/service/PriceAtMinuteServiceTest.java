package com.castaware.nocturne.service;

import java.time.LocalDateTime;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.castaware.nocturne.util.NocDateFormat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-servlet.xml"
})
public class PriceAtMinuteServiceTest 
{
	@Autowired
	PriceService service;
	
	@Test
	public void test() throws Exception
	{
		LogManager.getLogger("com.castaware.nocturne").setLevel(Level.TRACE);
		
		try
		{
			LocalDateTime timestamp = NocDateFormat.parse_yyyyMMddHHmmssHifen("2021-10-11 13:45:00");
			System.out.println(service.priceAtMinute("SOL",timestamp));
		}
		catch(Throwable e)
		{
			e.printStackTrace();
			try {Thread.sleep(500);} catch (InterruptedException e1) {}
			System.out.println();
			System.out.println(e.getMessage());
		}		
	}
}
