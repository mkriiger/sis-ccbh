package com.castaware.nocturne.mvc.control;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.castaware.nocturne.datatypes.Asset;
import com.castaware.nocturne.datatypes.Claimable;
import com.castaware.nocturne.datatypes.Pool;
import com.castaware.nocturne.datatypes.PoolLiquidity;
import com.castaware.nocturne.datatypes.PoolStaking;
import com.castaware.nocturne.datatypes.PriceAtMinute;
import com.castaware.nocturne.datatypes.TransactionPoolCompound;
import com.castaware.nocturne.datatypes.TransactionPoolEarn;
import com.castaware.nocturne.datatypes.Wallet;
import com.castaware.nocturne.service.AssetService;
import com.castaware.nocturne.service.ConsolidationService;
import com.castaware.nocturne.service.PoolService;
import com.castaware.nocturne.service.PriceService;
import com.castaware.nocturne.util.NocDateFormat;

import eic.tcc.control._Bean;
import eic.tcc.dao.Dao;
 
@Controller(value="poolBean")
@Scope("session")
public class PoolBean extends _Bean
{
	@Autowired
	Dao dao;

	@Autowired
	AssetService assetService;
	
	@Autowired
	PriceService priceService;
	
	@Autowired
	PoolService poolService;
	
	@Autowired
	ConsolidationService consolidationService;
	
	//
	// Attributes
	//
	private double brlQuote = 0d;
	private List<PoolStaking>   stkPools = new ArrayList<PoolStaking>();
	private List<PoolStaking>   endStkPools = new ArrayList<PoolStaking>();
	private List<PoolStaking>   allStkPools = new ArrayList<PoolStaking>();
	private List<PoolLiquidity> liqPools = new ArrayList<PoolLiquidity>();
	private List<PoolLiquidity> endLiqPools = new ArrayList<PoolLiquidity>();
	private List<PoolLiquidity> allLiqPools = new ArrayList<PoolLiquidity>();
	
	//
	// Init
	//
	@PostConstruct
	public void init() 
	{	
		brlQuote = priceService.fetchBinanceQuote("USDTBRL").doubleValue();				
		loadPools();		
	}

	private void loadPools() 
	{
		loadPoolsSP();
		loadPoolsLP();
	}
	
	private void loadPoolsSP() 
	{
		stkPools.clear();
		endStkPools.clear();
		allStkPools.clear();
		
		List<PoolStaking> all = poolService.getStakingPools();
		
		for (PoolStaking sp : all) 
		{
			allStkPools.add(sp);
			
			if (sp.getAmount().doubleValue()>0)
				stkPools.add(sp);								
			else
				endStkPools.add(sp);
			
			BigDecimal liveQuote = priceService.priceAtLive(sp.getAsset());
			sp.setLiveQuote(liveQuote.doubleValue());
			
			for (Claimable claimable : sp.getClaimables())
			{
				String asset = claimable.getAsset();
				BigDecimal quote = priceService.priceAtLive(asset);
				claimable.setQuote(quote);
			}
		}
		
		Collections.sort(stkPools,new Comparator<Pool>() 
		{
			public int compare(Pool o1, Pool o2) 
			{
				int compare = o1.getWallet().getNetwork().compareTo(o2.getWallet().getNetwork());
				
				if (compare==0)
					compare = o1.getId().compareTo(o2.getId());
				
				return compare;
			}
		});
	}

	private void loadPoolsLP() 
	{
		liqPools.clear();
		endLiqPools.clear();
		allLiqPools.clear();
		
		List<PoolLiquidity> all = poolService.getLiquidityPools();
		
		for (PoolLiquidity lp : all) 
		{
			allLiqPools.add(lp);
			
			if(lp.getAmount().doubleValue()>0)
				liqPools.add(lp);
			else
				endLiqPools.add(lp);
			
			for (Claimable claimable : lp.getClaimables())
			{
				String asset = claimable.getAsset();
				BigDecimal quote = priceService.priceAtLive(asset);
				claimable.setQuote(quote);
			}
		}
		
		Collections.sort(liqPools,new Comparator<Pool>() 
		{
			public int compare(Pool o1, Pool o2) 
			{
				int compare = o1.getWallet().getNetwork().compareTo(o2.getWallet().getNetwork());
				
				if (compare==0)
					compare = o1.getId().compareTo(o2.getId());
				
				return compare;
			}
		});
	}

	//
	// Totals
	//
	public double getTotalInvestedSP()
	{
		double total = 0;
		
		for (PoolStaking pool : stkPools) 
			total += pool.getDepositedValue();
		
		return total;
	}
	
	public double getTotalCurrentSP()
	{
		double total = 0;
		
		for (PoolStaking pool : stkPools) 
			total += pool.getEntryValue();
		
		return total;
	}
	
	public double getTotalProfitSP()
	{
		double total = 0;
		
		for (PoolStaking pool : stkPools) 
			total += pool.getROIValue();
		
		return total;
	}
	
	public double getTotalROISP()
	{
		double totalInvested = getTotalInvestedSP();
		double totalProfit = getTotalProfitSP();
		double divide = totalProfit/totalInvested;
		return divide;		
	}
	
	public double getTotalInvestedLP()
	{
		double total = 0;
		
		for (PoolLiquidity pool : liqPools) 
			total += pool.getDepositedValue();
		
		return total;
	}
	
	public double getTotalCurrentLP()
	{
		double total = 0;
		
		for (PoolLiquidity pool : liqPools) 
			total += pool.getEntryValue();
		
		return total;
	}
	
	public double getTotalProfitLP()
	{
		double total = 0;
		
		for (PoolLiquidity pool : liqPools) 
			total += pool.getROIValue();
		
		return total;
	}
	
	public double getTotalROILP()
	{
		double totalInvested = getTotalInvestedLP();
		double totalProfit = getTotalProfitLP();
		double divide = totalProfit/totalInvested;
		return divide;		
	}
	
	//
	// Operations
	//	
	public void setStrategy(Pool pool)
	{
		dao.persist(pool);
	}
	
	public void addClaimable(Pool pool)
	{
		pool.getClaimables().add(new Claimable("TOKEN",BigDecimal.ZERO));
		dao.persist(pool);
	}
	
	public void removeClaimable(Pool pool,Claimable claimable)
	{
		pool.getClaimables().remove(claimable);
		dao.persist(pool);
	}
	
	
	public void savePool(Pool pool,Object object)
	{
		try
		{
			dao.persist(pool);
			hideDialog("editPoolDialog");			
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}
	
	public void savePool(Pool pool)
	{
		try
		{
			dao.persist(pool);
			hideDialog("editPoolDialog");			
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}
	
	//
	// Add Claim 
	//
	private String claimAsset = "";
	private String claimOldAmount = "";
	private String claimNewAmount = "";
	private String claimAddAmount = "";
	private String claimTimestamp = NocDateFormat.format_yyyyMMddHHmmssHifen(NocDateFormat.toStartOfDay(LocalDateTime.now()));;
	
	private static String claimAssetBackup = "";
	private static String claimOldAmountBackup = "";
	private static String claimNewAmountBackup = "";
	private static String claimAddAmountBackup = "";	
			
	public void setClaimAsset(Pool pool)
	{
		if (claimAsset.equals("LPT"))
		{
			if (pool instanceof PoolLiquidity)
			{
				PoolLiquidity lp = (PoolLiquidity)pool;
				claimOldAmount = lp.getAmount().toPlainString();
			}
			
			if (pool instanceof PoolStaking)
			{
				claimOldAmount = pool.getAmount().toPlainString();
			}
		}
		else
		{
			Wallet wallet = pool.getWallet();
			Asset asset = assetService.getAsset(wallet, claimAsset);
			
			if (asset!=null)
				claimOldAmount = asset.getAmount().toPlainString();
			else
				claimOldAmount = "0.00";
		}		
		
		claimAssetBackup = claimAsset;
		claimOldAmountBackup = claimOldAmount;
	}
	
	public void setClaimAmount(Pool pool)
	{
		claimAsset 	   = claimAssetBackup;
		claimOldAmount = claimOldAmountBackup;
		
		try
		{
			if (claimNewAmount!=null && !claimNewAmount.isEmpty())
			{
				BigDecimal newAmount = new BigDecimal(claimNewAmount);
				BigDecimal oldAmount = new BigDecimal(claimOldAmount);
				BigDecimal addAmount = newAmount.subtract(oldAmount);
				claimAddAmount = addAmount.toPlainString();
			}
			
			if (claimAddAmount!=null && !claimAddAmount.isEmpty())
			{
				BigDecimal addAmount = new BigDecimal(claimAddAmount);
				BigDecimal oldAmount = new BigDecimal(claimOldAmount);
				BigDecimal newAmount = oldAmount.add(addAmount);
				claimNewAmount = newAmount.toPlainString();
			}
		}
		catch(Exception e) 
		{
			handleError(e);
		}
		
		claimNewAmountBackup = claimNewAmount;
		claimOldAmountBackup = claimOldAmount;
		claimAddAmountBackup = claimAddAmount;
	}
		
	public void addClaim(Pool pool)
	{
		claimAsset 	   = claimAssetBackup;
		claimOldAmount = claimOldAmountBackup;
		claimNewAmount = claimNewAmountBackup;
		claimAddAmount = claimAddAmountBackup;
		
		try
		{
			if (claimAsset.equals("LPT"))
			{
				BigDecimal quote = priceService.priceAtLive(claimAssetBackup);
				LocalDateTime timestamp = NocDateFormat.parse_yyyyMMddHHmmssHifen(claimTimestamp);
				PriceAtMinute pam = new PriceAtMinute(claimAssetBackup, quote, timestamp);
				dao.persist(pam);
				
				TransactionPoolCompound tx = new TransactionPoolCompound();
				tx.setAsset(claimAsset);
				tx.setAmount(new BigDecimal(claimAddAmount.toString()));
				tx.setTimestamp(timestamp);
				tx.setPoolName(pool.getId());
				tx.setFromWallet(pool.getWallet());
				
				consolidationService.proccess(pool.getWallet(), tx);
				loadPools();
			}
			else
			{
				BigDecimal quote = priceService.priceAtLive(claimAssetBackup);
				LocalDateTime timestamp = NocDateFormat.parse_yyyyMMddHHmmssHifen(claimTimestamp);
				PriceAtMinute pam = new PriceAtMinute(claimAssetBackup, quote, timestamp);
				dao.persist(pam);
				
				TransactionPoolEarn tx = new TransactionPoolEarn();
				tx.setAsset(claimAsset);
				tx.setAmount(new BigDecimal(claimAddAmount.toString()));
				tx.setQuote(quote);
				tx.setTimestamp(timestamp);
				tx.setPoolName(pool.getId());
				tx.setFromWallet(pool.getWallet());
				
				consolidationService.proccess(pool.getWallet(), tx);
				loadPools();
			}
			
			claimAsset = "";
			claimOldAmount = "";
			claimNewAmount = "";
			claimAddAmount = "";
			claimTimestamp = NocDateFormat.format_yyyyMMddHHmmssHifen(NocDateFormat.toStartOfDay(LocalDateTime.now()));;
			
			popInfo("Claim sucessfully added");
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}
		
	//
	// JSF Accessors
	//
	public double getBrlQuote() {
		return brlQuote;
	}
		
	public List<PoolLiquidity> getLiquidityPools() {
		return liqPools;
	}
	
	public List<PoolLiquidity> getAllLiquidityPools() {
		return allLiqPools;
	}
	
	public List<PoolLiquidity> getEndLiquidityLiqPools() {
		return endLiqPools;
	}
	
	public List<PoolStaking> getStakingPools() {
		return stkPools;
	}
	
	public List<PoolStaking> getAllStakingPools() {
		return allStkPools;
	}
	
	public List<PoolStaking> getEndStakingPools() {
		return endStkPools;
	}

	public String getClaimAsset() {
		return claimAsset;
	}

	public void setClaimAsset(String claimAsset) {
		this.claimAsset = claimAsset;
	}

	public String getClaimOldAmount() {
		return claimOldAmount;
	}

	public void setClaimOldAmount(String claimOldAmount) {
		this.claimOldAmount = claimOldAmount;
	}

	public String getClaimNewAmount() {
		return claimNewAmount;
	}

	public void setClaimNewAmount(String claimNewAmount) {
		this.claimNewAmount = claimNewAmount;
	}

	public String getClaimAddAmount() {
		return claimAddAmount;
	}

	public void setClaimAddAmount(String claimAddAmount) {
		this.claimAddAmount = claimAddAmount;
	}

	public String getClaimTimestamp() {
		return claimTimestamp;
	}

	public void setClaimTimestamp(String claimTimestamp) {
		this.claimTimestamp = claimTimestamp;
	}
}