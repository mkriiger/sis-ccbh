package com.castaware.nocturne.api;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-servlet.xml"
})
public abstract class BinanceApiTest 
{
	@Autowired
	BinanceApi api = new BinanceApi();
	
	
}
