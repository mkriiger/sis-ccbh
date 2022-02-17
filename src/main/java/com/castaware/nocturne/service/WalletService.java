package com.castaware.nocturne.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.castaware.nocturne.datatypes.Transaction;
import com.castaware.nocturne.datatypes.Wallet;

import eic.tcc.dao.Dao;

@Component
public class WalletService 
{
	@Autowired
	private Dao dao;
		
	public List<Wallet> getWallets()
	{
		return dao.retrieveAll(Wallet.class);
	}
	
	@SuppressWarnings("unchecked")
	public Wallet getWallet(String id) 
	{
		String hql = "FROM Wallet "
				+ "WHERE id=?0 ";
	
		List<Wallet> result = (List<Wallet>)dao.queryHQL(hql, id);
		
		if (result.size()>1)
			throw new IllegalStateException("Duplicate entry for "+id);
		else if (result.size()==1)
			return result.get(0);
		else
			return null;
	}
	
	public List<Transaction> getAvailableTx(Wallet wallet)
	{
		return getTx(wallet,false,false);
	}
	
	public List<Transaction> getAvailableTx(Wallet wallet,boolean desc)
	{
		return getTx(wallet,false,desc);
	}

	public List<Transaction> getAllocatedTx(Wallet wallet)
	{
		return getTx(wallet,true,false);
	}
	
	public List<Transaction> getAllocatedTx(Wallet wallet,boolean desc)
	{
		return getTx(wallet,true,desc);
	}
	
	@SuppressWarnings("unchecked")
	public List<Transaction> getLastAllocatedTx(Wallet wallet)
	{
		String hql = "FROM Transaction T "
				+ "WHERE (toWallet=?0 and toAlloc=?1) "
				+ "OR (fromWallet=?0 and fromAlloc=?1) "
				+ "ORDER BY T.timestamp DESC";
	
		List<Transaction> result = (List<Transaction>)dao.queryLimitHQL(hql, 3, wallet, true);
	
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	private List<Transaction> getTx(Wallet wallet,boolean allocated,boolean desc)
	{
		String hql = "FROM Transaction T "
					+ "WHERE (toWallet=?0 and toAlloc=?1) "
					+ "OR (fromWallet=?0 and fromAlloc=?1) "
					+ "ORDER BY T.timestamp ";
		
		if (desc)
			hql+="DESC";
		
		List<Transaction> result = (List<Transaction>)dao.queryHQL(hql, wallet, allocated);
		
		return result;
	}
}
