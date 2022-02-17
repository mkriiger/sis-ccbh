package com.castaware.nocturne.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.castaware.nocturne.datatypes.Asset;
import com.castaware.nocturne.datatypes.Wallet;

import eic.tcc.dao.Dao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-servlet.xml"
})
public class AssetServiceTest_getSpotAssets 
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
			Wallet wallet = dao.retrieveBySingleLike(Wallet.class,"id","KEPLR_OSM").get(0);
			
			List<Asset> assets = assetService.getSpotAssets(wallet);
			
			for (Asset asset : assets) 
			{
				System.out.println(asset);
			}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
