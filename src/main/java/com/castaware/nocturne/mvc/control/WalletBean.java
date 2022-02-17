package com.castaware.nocturne.mvc.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.castaware.nocturne.datatypes.Wallet;
import com.castaware.nocturne.datatypes.Network;
import com.castaware.nocturne.mvc.control.templates._CrudBean;

import eic.tcc.dao.Dao;
 
@Controller(value="walletBean")
@Scope("session")
public class WalletBean extends _CrudBean<Wallet>
{
	@Autowired
	Dao dao;
	
	public WalletBean() 
	{
		super(Wallet.class);
	}	
	
	@Override
	public String[] filterFields() 
	{
		return new String[]{"name","provider","network","creation","address"};
	}	
	
	//
	// Custom Behavior
	//
	@Override
	public void persist()
	{
		Wallet wallet = (Wallet)object;
		
		if (wallet.getNetwork() != Network.BINANCE)
		{
			wallet.setApiKey("");
			wallet.setSecretKey("");
		}
		
		super.persist();
	}	
		
	public void onVisibilityChange(Wallet wallet)
	{
		dao.persist(wallet);		
	}
	
	//
	// JSF Access
	//
	public Network[] getAllNetworks()
	{
		return Network.values();
	}	
}
