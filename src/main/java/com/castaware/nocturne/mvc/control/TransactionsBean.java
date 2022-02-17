package com.castaware.nocturne.mvc.control;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.castaware.nocturne.datatypes.Asset;
import com.castaware.nocturne.datatypes.PoolLiquidity;
import com.castaware.nocturne.datatypes.PoolStaking;
import com.castaware.nocturne.datatypes.Transaction;
import com.castaware.nocturne.datatypes.Wallet;
import com.castaware.nocturne.mvc.control.wrappers.TransactionWrapper;
import com.castaware.nocturne.service.ConsolidationService;
import com.castaware.nocturne.service.PriceService;
import com.castaware.nocturne.service.WalletService;
import com.castaware.nocturne.util.NocDateFormat;

import eic.tcc.control._Bean;
import eic.tcc.dao.Dao;
 
@Controller(value="transactionsBean")
@Scope("session")
public class TransactionsBean extends _Bean
{
	@Autowired
	Dao dao;
	
	@Autowired
	PriceService priceService;
	
	@Autowired
	WalletService walletService;
	
	@Autowired
	ConsolidationService consolidationService;
	
	TransactionWrapper wrapper;
	
	@PostConstruct
	public void init()
	{
		wrapper = new TransactionWrapper(walletService,priceService);
	}
	
	//
	// Operations
	//
	public void newMisc(Wallet wallet)
	{
		try
		{
			wrapper.clear();
			
			wrapper.setFromWallet(wallet.getId());
			wrapper.setTimestamp(makeTimestamp());
			wrapper.setFeeAsset(wallet.getNetwork().getNativeAsset());
			
			newTx("txMisc");
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}

	public void newSwap(Asset asset)
	{
		try
		{
			wrapper.clear();
			wrapper.setFromWallet(asset.getWallet().getId());
			wrapper.setTimestamp(makeTimestamp());
			wrapper.setxAsset(asset.getName());
			wrapper.setxAmount(asset.getAmount().toString());
			wrapper.setFeeAsset(asset.getWallet().getNetwork().getNativeAsset());
			
			newTx("txSwap");
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}
	
	public void newStakingAdd(Wallet wallet)
	{
		try
		{
			wrapper.clear();
			wrapper.setFromWallet(wallet.getId());
			wrapper.setTimestamp(makeTimestamp());
			wrapper.setFeeAsset(wallet.getNetwork().getNativeAsset());
			
			newTx("txStakingAdd");
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}
	
	public void newStakingAdd(PoolStaking pool, Wallet wallet)
	{
		try
		{
			newStakingAdd(wallet);
			wrapper.setPoolName(pool.getId());
			wrapper.setxAsset(pool.getAsset());
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}
	
	public void newStakingRemove(PoolStaking pool, Wallet wallet)
	{
		try
		{
			wrapper.clear();
			
			String asset = pool.getAsset();
			BigDecimal quote = priceService.priceAtLive(asset);
			
			wrapper.setxQuote(quote.toString());
			wrapper.setPoolName(pool.getId());
			wrapper.setxAsset(asset);
			wrapper.setxAmount(pool.getAmount().toString());
			wrapper.setFromWallet(wallet.getId());
			wrapper.setTimestamp(makeTimestamp());
			wrapper.setFeeAsset(wallet.getNetwork().getNativeAsset());
			
			newTx("txStakingRemove");
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}
	
	public void newLiquidityAdd(Wallet wallet)
	{
		try
		{
			wrapper.clear();
			wrapper.setFromWallet(wallet.getId());
			wrapper.setTimestamp(makeTimestamp());
			wrapper.setFeeAsset(wallet.getNetwork().getNativeAsset());
			
			newTx("txLiquidityAdd");
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}
	
	public void newLiquidityAdd(PoolLiquidity pool, Wallet wallet)
	{
		try
		{
			newLiquidityAdd(wallet);
			wrapper.setPoolName(pool.getName());
			wrapper.setxAsset(pool.getAssetX());
			wrapper.setyAsset(pool.getAssetY());									
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}
	
	public void newLiquidityRemove(PoolLiquidity pool, Wallet wallet)
	{
		try
		{
			wrapper.clear();
			
			String assetX = pool.getAssetX();
			String assetY = pool.getAssetY();
			
			BigDecimal quoteX = priceService.priceAtLive(assetX);
			BigDecimal quoteY = priceService.priceAtLive(assetY);
			
			wrapper.setxQuote(quoteX.toString());
			wrapper.setyQuote(quoteY.toString());
			
			wrapper.setPoolName(pool.getName());
			wrapper.setxAsset(assetX);
			wrapper.setyAsset(assetY);
			wrapper.setPoolAmount(pool.getAmount().toString());
			wrapper.setFromWallet(wallet.getId());
			wrapper.setTimestamp(makeTimestamp());
			wrapper.setFeeAsset(wallet.getNetwork().getNativeAsset());
			
			newTx("txLiquidityRemove");
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}
	
	public void newTx(String dialog)
	{
		showDialog(dialog);					
	}
	
	@Transactional
	public void saveTx(String dialog, ConsolidationBean consolidationBean)
	{
		saveTx(dialog,"",consolidationBean);
	}
	
	@Transactional
	public void saveTx(String dialog, String suffix, ConsolidationBean consolidationBean)
	{
		try
		{
			String command = "to"+StringUtils.capitalize(dialog)+suffix;
			Method saveMethod = wrapper.getClass().getMethod(command);
			Transaction tx = (Transaction)saveMethod.invoke(wrapper);
			
			consolidationService.proccess(tx);
			consolidationBean.fillWallet();
			
			wrapper.clear();
			hideDialog(dialog);
			PrimeFaces.current().ajax().update("form");
			popInfo("Tx Saved");
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}
	
	//
	// Utils
	//
	private String makeTimestamp() 
	{
		return NocDateFormat.format_yyyyMMddHHmmssHifen(LocalDateTime.now().plus(3,ChronoUnit.HOURS));
	}
	
	//
	// JSF Access
	//
	public TransactionWrapper getTx() {
		return wrapper;
	}
	
	public void setTx(TransactionWrapper wrapper) {
		this.wrapper = wrapper;
	}
}