package com.castaware.nocturne.mvc.control;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.primefaces.event.TabChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.castaware.nocturne.datatypes.Asset;
import com.castaware.nocturne.datatypes.PoolLiquidity;
import com.castaware.nocturne.datatypes.PoolStaking;
import com.castaware.nocturne.datatypes.Transaction;
import com.castaware.nocturne.datatypes.TransactionPoolEarn;
import com.castaware.nocturne.datatypes.Wallet;
import com.castaware.nocturne.datatypes.exception.InsufficientFundsException;
import com.castaware.nocturne.service.AssetService;
import com.castaware.nocturne.service.PoolService;
import com.castaware.nocturne.service.PriceService;
import com.castaware.nocturne.service.WalletService;
import com.castaware.nocturne.service.ConsolidationService;
import com.castaware.nocturne.util.NocDateFormat;

import eic.tcc.control._Bean;
import eic.tcc.dao.Dao;
 
@Controller(value="consolidationBean")
@Scope("session")
public class ConsolidationBean extends _Bean
{
	@Autowired
	Dao dao;
	
	@Autowired
	ConsolidationService consolidationService;
	
	@Autowired
	WalletService walletService;
	
	@Autowired
	AssetService assetService;
	
	@Autowired
	PoolService poolService;
	
	@Autowired
	PriceService priceService;
	
	//
	// Attributes
	//
	private List<Asset> 		spotAssets;
	private List<PoolStaking>   stakingPools;
	private List<PoolLiquidity> liquidityPools;
	
	private List<Wallet>   	  wallets  = new ArrayList<Wallet>();
	private List<Wallet>   	  allWallets  = new ArrayList<Wallet>();
	
	private List<Transaction> txAlloc  = new ArrayList<Transaction>();
	private List<Transaction> txAvail  = new ArrayList<Transaction>();
	
	private Wallet   editWallet       = null;
	private Integer  editWalletIndex  = null;
		
	// TEMP
	private String earnAsset;
	private String earnAmount;	
	private String earnTimestamp;
	
	//
	// Init
	//
	@PostConstruct
	public void init() 
	{
		fillWallets();
		fillWallet();
	}

	public void fillWallets() 
	{
		editWallet = null;		
		allWallets = dao.retrieveBySingleLike(Wallet.class,"visible",true);
		
		for (Wallet wallet : allWallets)
		{
			if (wallet.getVisible())
				wallets.add(wallet);
		}
				
		Collections.sort(wallets,new Comparator<Wallet>() 
		                         {
									@Override
									public int compare(Wallet o1, Wallet o2) 
									{
										return o1.getOrder().compareTo(o2.getOrder());
									}			
		                         });
		
		// 1 - Procura pela primeira carteira selecionada
		for (int i=0;i<wallets.size();i++) 
		{
			Wallet wallet = wallets.get(i);
			
			if (wallet.getSelected())
			{
				if (wallet.getVisible())
				{
					if (editWallet==null)
					{
						editWallet=wallet;
						editWalletIndex=i;
					}
					else
					{
						wallet.setSelected(false);
						dao.persist(wallet);
					}				
				}
			}			
		}

		// 2 - Caso não haja carteira selecionada, seleciona a primeira carteira visivel
		if (editWallet==null)
		{
			for (int i=0;i<wallets.size();i++) 
			{
				Wallet wallet = wallets.get(i);
				
				if (wallet.getVisible())
				{
					editWalletIndex=i;
					editWallet=wallet;
					editWallet.setSelected(true);
					dao.persist(editWallet);
					break;
				}
			}
		}
		
		// 3 - Caso em que não há carteiras visiveis
		if (editWallet==null)
		{
			throw new IllegalArgumentException("At least one visible wallet is required");
		}
	}	
	
	public boolean fillWallet()
	{
		try
		{
			txAvail = walletService.getAvailableTx(editWallet);
			txAlloc = walletService.getLastAllocatedTx(editWallet);
			spotAssets = assetService.getSpotAssets(editWallet);
			stakingPools = poolService.getActiveStakingPools(editWallet);
			liquidityPools = poolService.getActiveLiquidityPools(editWallet);
			return true;
		}
		catch(Exception e)
		{
			if (e.getCause()!=null)
			{
				Throwable ex = e.getCause().getCause();
				
				if (ex instanceof InsufficientFundsException)
				{
					InsufficientFundsException ifex = (InsufficientFundsException)ex;
					
					this.earnAsset=ifex.asset;
					this.earnAmount=ifex.lackAmount.toPlainString();
					
					handleError(ifex);
				}
				else
				{
					handleError(e);
				}				
			}
			else
			{
				handleError(e);
			}
			
			return false;
		}		
	}
			
	//
	// Operations
	//
	public void proccessTx(boolean allocate)
	{
		Transaction tx;
		
		if (allocate)
		{
			tx = txAvail.get(0);
			
			consolidationService.proccess(editWallet, tx);			
			dao.persist(tx);
			fillWallet();
		}
		else
		{
			tx = txAlloc.get(0);
			
			if (editWallet.equals(tx.getToWallet()))
				tx.setToAlloc(!allocate);		
			if (editWallet.equals(tx.getFromWallet()))
				tx.setFromAlloc(!allocate);
			
			dao.persist(tx);			
			fillWallet();
			consolidationService.reset(editWallet);
		}
	}
	
	public void addTx()
	{
		try
		{
			showDialog("addTxDialog");			
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}
	
	public void addTx(String asset,BigDecimal amount,String timestampString)
	{
		try
		{
			if (!editWallet.getId().equals("BINANCE"))
				throw new IllegalArgumentException();
			
			LocalDateTime timestamp = NocDateFormat.parse_yyyyMMddHHmmssHifen(timestampString);		
			
			TransactionPoolEarn tx = new TransactionPoolEarn();
			
			tx.setAmount(amount);
			tx.setAsset(asset);
			tx.setTimestamp(timestamp);
			tx.setToAlloc(false);
			tx.setFromAlloc(true);
			tx.setSuccess(true);
			tx.setFromWallet(editWallet);
			tx.setPoolName("Binance Earn ("+asset+")");
			
			dao.persist(tx);	
			
			fillWallet();
			
			hideDialog("addTxDialog");
			popInfo("Tx Saved");
		}
		catch(Exception e)
		{
			handleError(e);
		}	
	}
	
	//
	// Events
	//
	public void onWalletChange(TabChangeEvent<?> event) 
	{
		String currentWalletName = event.getTab().getTitle();
		
		for (int i=0;i<wallets.size();i++) 
		{
			Wallet wallet = wallets.get(i);
			
			if (wallet.getName().equals(currentWalletName))
			{
				editWalletIndex=i;
				editWallet=wallet;
				editWallet.setSelected(true);
				dao.persist(editWallet);
			}
			else
			{
				wallet.setSelected(false);
				dao.persist(wallet);
			}
		}		        
		
		fillWallet();
    }
	
	//
	// Retrievers
	//
	public List<Wallet> getWallets() 
	{
		return wallets;
	}
	
	public List<Asset> getSpotAssets()
	{
		return spotAssets;
	}
	
	public List<PoolStaking> getStakingPools()
	{
		return stakingPools;
	}
	
	public List<PoolLiquidity> getLiquidityPools()
	{
		return liquidityPools;
	}
		
	//
	// Accessors
	//
	public Wallet getWallet() {
		return editWallet;
	}
		
	public List<Transaction> getTxAlloc() {
		return txAlloc;
	}
	
	public List<Transaction> getTxAvail() {
		return txAvail;
	}
	
	public Integer getEditWalletIndex() {
		return editWalletIndex;
	}
	
	public void setEditWalletIndex(Integer editWalletIndex) {
		this.editWalletIndex = editWalletIndex;
	}

	public String getEarnAsset() {
		return earnAsset;
	}

	public void setEarnAsset(String earnAsset) {
		this.earnAsset = earnAsset;
	}

	public String getEarnAmount() {
		return earnAmount;
	}

	public void setEarnAmount(String earnAmount) {
		this.earnAmount = earnAmount;
	}

	public String getEarnTimestamp() {
		return earnTimestamp;
	}

	public void setEarnTimestamp(String earnTimestamp) {
		this.earnTimestamp = earnTimestamp;
	}
}