package com.castaware.nocturne.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.castaware.nocturne.datatypes.Pool;
import com.castaware.nocturne.datatypes.PoolLiquidity;
import com.castaware.nocturne.datatypes.PoolStaking;
import com.castaware.nocturne.datatypes.Wallet;

import eic.tcc.dao.Dao;

@Component
public class PoolService 
{
	@Autowired
	private Dao dao;
	
	@SuppressWarnings("unchecked")
	public Pool getPool(String poolName) 
	{
		String hql = "FROM Pool "
				   + "WHERE id=?0 ";
	
		List<Pool> result = (List<Pool>)dao.queryHQL(hql, poolName);
		
		if (result.size()>1)
			throw new IllegalStateException("Duplicate entry for "+poolName);
		else if (result.size()==1)
			return result.get(0);
		else
			return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Pool> getPools(Wallet wallet) 
	{
		String hql = "FROM Pool "
				   + "WHERE wallet=?0 "
				   + "ORDER BY id ";
	
		List<Pool> result = (List<Pool>)dao.queryHQL(hql, wallet);
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<PoolStaking> getStakingPools() 
	{
		String hql = "FROM PoolStaking "
				   + "ORDER BY id ";
	
		List<PoolStaking> result = (List<PoolStaking>)dao.queryHQL(hql);
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<PoolStaking> getStakingPools(Wallet wallet) 
	{
		String hql = "FROM PoolStaking "
				   + "WHERE wallet=?0 "
				   + "ORDER BY id ";
	
		List<PoolStaking> result = (List<PoolStaking>)dao.queryHQL(hql, wallet);
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<PoolStaking> getActiveStakingPools(Wallet wallet) 
	{
		String hql = "FROM PoolStaking "
				   + "WHERE wallet=?0 "
				   + "ORDER BY id ";
	
		List<PoolStaking> result = (List<PoolStaking>)dao.queryHQL(hql, wallet);
		
		Iterator<PoolStaking> iter = result.iterator();
		
		while(iter.hasNext())
		{
			PoolStaking pool = iter.next();
			
			if(pool.getAmount().doubleValue()==0)
				iter.remove();
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<PoolLiquidity> getLiquidityPools() 
	{
		String hql = "FROM PoolLiquidity "
				   + "ORDER BY id ";
	
		List<PoolLiquidity> result = (List<PoolLiquidity>)dao.queryHQL(hql);
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<PoolLiquidity> getLiquidityPools(Wallet wallet) 
	{
		String hql = "FROM PoolLiquidity "
				+ "WHERE wallet=?0 "
				+ "ORDER BY id ";
	
		List<PoolLiquidity> result = (List<PoolLiquidity>)dao.queryHQL(hql, wallet);
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<PoolLiquidity> getActiveLiquidityPools(Wallet wallet) 
	{
		String hql = "FROM PoolLiquidity "
				+ "WHERE wallet=?0 "
				+ "ORDER BY id ";
	
		List<PoolLiquidity> result = (List<PoolLiquidity>)dao.queryHQL(hql, wallet);
		
		Iterator<PoolLiquidity> iter = result.iterator();
		
		while(iter.hasNext())
		{
			PoolLiquidity pool = iter.next();
			
			if(pool.getAmount().doubleValue()==0)
				iter.remove();
		}
		
		return result;
	}
}
