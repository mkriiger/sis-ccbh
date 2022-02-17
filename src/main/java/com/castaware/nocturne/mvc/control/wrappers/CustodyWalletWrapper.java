package com.castaware.nocturne.mvc.control.wrappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.castaware.nocturne.datatypes.Custody;
import com.castaware.nocturne.datatypes.Wallet;

public class CustodyWalletWrapper extends CustodyWrapper
{
	Set<String> groups = new TreeSet<String>();
	Map<String,Wallet> wallets = new TreeMap<String,Wallet>();
	
	public CustodyWalletWrapper(Set<Custody> assets) 
	{
		super(assets);		
		
		for (Custody custody : assets) 
		{
			groups.add(custody.getWallet().getName());
			wallets.put(custody.getWallet().getName(),custody.getWallet());
		}
	}
	
	public Wallet getWallet(String walletName) 
	{
		return wallets.get(walletName);
	}

	public Collection<Wallet> getWallets() 
	{
		List<Wallet> sortedWallets = new ArrayList<Wallet>();
		
		for (String walletName : getGroups())
		{
			sortedWallets.add(wallets.get(walletName));
		}
		
		return sortedWallets;
	}
	
	@Override
	public String getGroup(Custody asset) 
	{
		return asset.getWallet().getName();
	}
		
	@Override
	public Set<String> getAllGroups() 
	{
		return groups;
	}	
}
