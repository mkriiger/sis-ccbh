package com.castaware.nocturne.mvc.control;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.castaware.nocturne.datatypes.Asset;
import com.castaware.nocturne.datatypes.Custody;
import com.castaware.nocturne.datatypes.CustodyType;
import com.castaware.nocturne.datatypes.Pool;
import com.castaware.nocturne.datatypes.PoolStaking;
import com.castaware.nocturne.datatypes.Wallet;
import com.castaware.nocturne.mvc.control.wrappers.CustodyCategoryWrapper;
import com.castaware.nocturne.mvc.control.wrappers.CustodyEcosystemWrapper;
import com.castaware.nocturne.mvc.control.wrappers.CustodyWalletWrapper;
import com.castaware.nocturne.mvc.control.wrappers.CustodyWrapper;
import com.castaware.nocturne.service.AssetService;
import com.castaware.nocturne.service.ConsolidationService;
import com.castaware.nocturne.service.PriceService;
import com.castaware.nocturne.service.WalletService;

import eic.tcc.control._Bean;
import eic.tcc.dao.Dao;
 
@Controller(value="assetBean")
@Scope("session")
public class AssetBean extends _Bean
{
	@Autowired
	Dao dao;

	@Autowired
	PriceService priceService;
	
	@Autowired
	AssetService assetService;
	
	@Autowired
	WalletService walletService;
	
	@Autowired
	ConsolidationService consolidationService;
	
	//
	// Attributes
	//
	private double brlQuote = 0d;
	
	private Set<Custody> custodies;
	private Map<String,CustodyWrapper> wrappers;
	
	private CustodyWalletWrapper walletWrapper; 	     
	private CustodyEcosystemWrapper ecosystemWrapper; 
	private CustodyCategoryWrapper categoryWrapper;   
	
	//
	// Init
	//
	@PostConstruct
	public void init() 
	{	
		brlQuote = priceService.fetchBinanceQuote("USDTBRL").doubleValue();
		fillAssets();		
	}

	private void fillAssets()
	{
		custodies = new TreeSet<Custody>();
		custodies.addAll(dao.retrieveAll(Asset.class));
		
		for (Pool pool : dao.retrieveAll(Pool.class))
		{
			if (pool.getAmount().doubleValue()>0)
				custodies.add(pool);
		}
		
		walletWrapper 	 = new CustodyWalletWrapper(custodies);
		ecosystemWrapper = new CustodyEcosystemWrapper(custodies);
		categoryWrapper  = new CustodyCategoryWrapper(custodies);
		
		wrappers = new TreeMap<String,CustodyWrapper>();
		wrappers.put("1. Wallet",walletWrapper);
		wrappers.put("2. Ecosystem",ecosystemWrapper);
		wrappers.put("3. Category",categoryWrapper);
		
		updateLiveValues();
	}
	
	//
	// Operations
	// 
	public void updateLiveValues()
	{
		for (Custody custody : custodies) 
		{
			if (custody.getCustodyType()==CustodyType.ASSET)
			{
				BigDecimal quote = priceService.priceAtLive(custody.getName());
				((Asset)custody).setLiveQuote(quote.doubleValue());
			}
			
			if (custody.getCustodyType()==CustodyType.SP)
			{
				BigDecimal quote = priceService.priceAtLive(((PoolStaking)custody).getAsset());
				((PoolStaking)custody).setLiveQuote(quote.doubleValue());
			}
		}
	}
		
	//
	// TOTALS
	//
	public double getBrlQuote() 
	{
		return brlQuote;
	}
	
	public double getTotalFiat()
	{
		return custodies.stream().mapToDouble(a -> a.getFiat()?a.getLiveValue():0d).sum()*brlQuote;
	}
	
	public double getTotalInvested() 
	{
		return 60000d;
	}
	
	public double getTotalLive()
	{
		return custodies.stream().mapToDouble(a -> a.getLiveValue()).sum()*brlQuote;
	}
	
	public double getTotalRoi()
	{
		return getTotalLive()-getTotalInvested();
	}
	
	public double getTotalRoiPerc()
	{
		double totalROI = getTotalRoi();
		double totalEntry = getTotalInvested();
		double divide = totalROI/totalEntry;
		double subtract = divide;
		return subtract;
	}
		
	//
	// WALLET
	//
	public Collection<Wallet> getWallets()
	{
		return walletWrapper.getWallets();
	}
	
	public Set<Custody> getWalletAssets(Wallet wallet)
	{
		return walletWrapper.getAssets(wallet.getName());
	}
	
	public double getWalletLive(Wallet wallet)
	{
		return walletWrapper.getLive(wallet.getName());
	}
	
	public double getWalletEntry(Wallet wallet)
	{
		return walletWrapper.getEntry(wallet.getName());
	}
	
	public double getWalletRoi(Wallet wallet)
	{
		return wallet.getWithdrawn()-wallet.getDeposited()+getWalletLive(wallet);
	}
	
	public double getWalletRoiPerc(Wallet wallet)
	{
		double totalROI = getWalletRoi(wallet);
		double totalEntry = wallet.getDeposited();
		double divide = totalROI/totalEntry;
		double subtract = divide;
		return subtract;
	}
	
	//
	// GENERIC
	//
	public Set<String> getWrappers()
	{
		return wrappers.keySet();
	}
	
	public Collection<String> getGroups(String wrapper)
	{
		return wrappers.get(wrapper).getGroups();
	}
	
	public Set<Custody> getAssets(String wrapper, String group)
	{
		return wrappers.get(wrapper).getAssets(group);
	}
	
	public double getEntry(String wrapper, String group)
	{
		return wrappers.get(wrapper).getEntry(group);
	}
	
	public double getLive(String wrapper, String group)
	{
		return wrappers.get(wrapper).getLive(group);
	}
	
	public double getRoi(String wrapper, String group)
	{
		return wrappers.get(wrapper).getRoi(group);
	}
	
	public double getRoiPerc(String wrapper, String group)
	{
		return wrappers.get(wrapper).getRoiPerc(group);
	}					
}