package com.castaware.nocturne.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eic.tcc.dao.Dao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-servlet.xml"
})
public class AssetServiceTest_getCategories 
{
	@Autowired
	Dao dao;
	
	@Autowired
	AssetService assetService;
	
	@Test
	public void test() throws Exception
	{
		try
		{
			List<String> categories = assetService.getAssetCategories();
			
			for (String category : categories) 
			{
				System.out.println(category);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
