package com.castaware.nocturne.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.castaware.nocturne.datatypes.Asset;
import com.castaware.nocturne.datatypes.Wallet;

import eic.tcc.dao.Dao;

@Component
public class AssetService 
{
	@Autowired
	private Dao dao;
		
	@SuppressWarnings("unchecked")
	public boolean isValid(String assetName) 
	{
		String hql = "FROM AssetMeta "
				   + "WHERE asset=% ";
	
		List<Asset> result = (List<Asset>)dao.queryHQL(hql, assetName);
		
		if (result.size()>1)
			throw new IllegalStateException("Duplicate entry for "+assetName+" in AssetMeta");
		else if (result.size()==1)
			return true;
		else
			return false;				
	}
	
	public List<Asset> getAssets()
	{
		return dao.retrieveAll(Asset.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Asset> getSpotAssets(Wallet wallet) 
	{
		String hql = "FROM Asset "
				+ "WHERE wallet=?0 "
				+ "AND name NOT LIKE 'LPT %' "
				+ "ORDER BY name ";
	
		List<Asset> result = (List<Asset>)dao.queryHQL(hql, wallet);
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Asset getAsset(Wallet wallet,String assetName) 
	{
		String hql = "FROM Asset "
				+ "WHERE wallet=?0 "
				+ "AND name LIKE ?1";
				
		List<Asset> result = (List<Asset>)dao.queryHQL(hql, wallet, assetName);
		
		if (result.size()>1)
			throw new IllegalStateException("Duplicate entry for "+assetName+" in "+wallet.getId());
		else if (result.size()==1)
			return result.get(0);
		else
			return null;
	}
	
	public BigDecimal getStubPrice(String assetName)
	{
		Map<String,Object> price = dao.querySingleRowSQL("select stubquote from assetmeta where asset='"+assetName+"'");
		
		if (price.isEmpty())
			return null;
		else
			return new BigDecimal(price.get("stubquote").toString());
	}
	
	public String getCoinGecko(String assetName)
	{
		Map<String,Object> results = dao.querySingleRowSQL("select coingecko from assetmeta where asset='"+assetName+"'");
		
		if (results.isEmpty())
			return null;
		else
		{
			String result = (String)results.get("coingecko");
			
			if (result==null || result.isEmpty())
				return null;
			else
				return result;
		}
	}
	
	public List<String> getAssetWallets()
	{
		List<String> results = dao.queryMultiRowSQL("select distinct name from wallet",
									new RowMapper<String>()
									{
										@Override
										public String mapRow(ResultSet rs, int rowNum) throws SQLException 
										{
											return rs.getString("name");
										}
									});
		
		return results;
	}
	
	public List<String> getAssetCategories()
	{
		List<String> results = dao.queryMultiRowSQL("select distinct category from assetmeta",
									new RowMapper<String>()
									{
										@Override
										public String mapRow(ResultSet rs, int rowNum) throws SQLException 
										{
											return rs.getString("category");
										}
									});
		
		return results;
	}
	
	public List<String> getAssetEcosystems()
	{
		List<String> results = dao.queryMultiRowSQL("select distinct ecosystem from assetmeta",
									new RowMapper<String>()
									{
										@Override
										public String mapRow(ResultSet rs, int rowNum) throws SQLException 
										{
											return rs.getString("ecosystem");
										}
									});
		
		return results;
	}
}
