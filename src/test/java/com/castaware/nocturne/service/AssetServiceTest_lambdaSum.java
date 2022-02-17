package com.castaware.nocturne.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.castaware.nocturne.datatypes.Asset;

import eic.tcc.dao.Dao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-servlet.xml"
})
public class AssetServiceTest_lambdaSum 
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
			List<Asset> assets = assetService.getAssets();
			
			double sumA = normalSum(assets);
			double sumB = lambdaSum(assets);
			
			System.out.println(sumA);
			System.out.println(sumB);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private double lambdaSum(List<Asset> assets)
	{
		return assets.stream().mapToDouble(a -> a.getEntryValue()).sum();
	}
	
	private double normalSum(List<Asset> assets)
	{
		double sum = 0;
		
		for (Asset asset : assets) 
		{
			sum += asset.getEntryValue();
		}
		
		return sum;
	}
}
