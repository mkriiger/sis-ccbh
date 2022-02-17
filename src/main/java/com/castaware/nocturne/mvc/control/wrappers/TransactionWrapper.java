package com.castaware.nocturne.mvc.control.wrappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.castaware.nocturne.datatypes.TransactionMisc;
import com.castaware.nocturne.datatypes.TransactionPoolLPAdd;
import com.castaware.nocturne.datatypes.TransactionPoolLPRemove;
import com.castaware.nocturne.datatypes.TransactionPoolSPAdd;
import com.castaware.nocturne.datatypes.TransactionPoolSPRemove;
import com.castaware.nocturne.datatypes.TransactionSwap;
import com.castaware.nocturne.service.PriceService;
import com.castaware.nocturne.service.WalletService;
import com.castaware.nocturne.util.NocDateFormat;

public class TransactionWrapper
{
	public String timestamp;
	public String description;
	public String xAsset,xAmount,xQuote;
	public String yAsset,yAmount,yQuote;
	public String feeAsset,feeAmount;
	public String fromWallet,toWallet;
	public String fromHash,toHash;
	public String poolName,poolAmount;
	
	private PriceService priceService;
	private WalletService walletService;
	
	public TransactionWrapper(WalletService walletService, PriceService priceService)
	{
		this.priceService=priceService;
		this.walletService=walletService;
	}
	
	public TransactionMisc toTxMisc()
	{
		TransactionMisc tx = new TransactionMisc();
		tx.setFromHash(fromHash);
		tx.setFromWallet(walletService.getWallet(fromWallet));
		tx.setTimestamp(NocDateFormat.parse_yyyyMMddHHmmssHifen(timestamp));
		tx.setDescription(description);
		tx.setFeeAsset(feeAsset);
		tx.setFeeAmount(new BigDecimal(feeAmount));
		tx.setToAlloc(false);
		tx.setFromAlloc(false);
		tx.setSuccess(true);
		
		return tx;
	}
	
	public TransactionSwap toTxSwap()
	{
		TransactionSwap tx = new TransactionSwap();
		tx.setFromHash(fromHash);
		tx.setFromWallet(walletService.getWallet(fromWallet));
		tx.setTimestamp(NocDateFormat.parse_yyyyMMddHHmmssHifen(timestamp));
		tx.setFromAsset(xAsset);
		tx.setFromAmount(new BigDecimal(xAmount));
		tx.setToAsset(yAsset);
		tx.setToAmount(new BigDecimal(yAmount));
		tx.setFeeAsset(feeAsset);
		tx.setFeeAmount(new BigDecimal(feeAmount));
		tx.setToAlloc(false);
		tx.setFromAlloc(false);
		tx.setSuccess(true);
		
		return tx;
	}
	
	public TransactionPoolSPAdd toTxStakingAdd()
	{
		TransactionPoolSPAdd tx = new TransactionPoolSPAdd();
		tx.setFromHash(fromHash);
		tx.setFromWallet(walletService.getWallet(fromWallet));
		tx.setTimestamp(NocDateFormat.parse_yyyyMMddHHmmssHifen(timestamp));
		tx.setAsset(xAsset);
		tx.setAmount(new BigDecimal(xAmount));
		tx.setFeeAsset(feeAsset);
		tx.setFeeAmount(new BigDecimal(feeAmount));
		tx.setPoolName(poolName);
		tx.setToAlloc(false);
		tx.setFromAlloc(false);
		tx.setSuccess(true);
		
		return tx;
	}
	
	public TransactionPoolSPRemove toTxStakingRemove()
	{
		LocalDateTime txTimestamp = NocDateFormat.parse_yyyyMMddHHmmssHifen(timestamp);
		BigDecimal txQuoteX = new BigDecimal(xQuote);
		priceService.newPriceAtMinute(xAsset, txQuoteX, txTimestamp);
		
		TransactionPoolSPRemove tx = new TransactionPoolSPRemove();
		tx.setFromHash(fromHash);
		tx.setFromWallet(walletService.getWallet(fromWallet));		
		tx.setTimestamp(txTimestamp);
		tx.setAsset(xAsset);
		tx.setAmount(new BigDecimal(xAmount));
		tx.setFeeAsset(feeAsset);
		tx.setFeeAmount(new BigDecimal(feeAmount));
		tx.setPoolName(poolName);
		tx.setToAlloc(false);
		tx.setFromAlloc(false);
		tx.setSuccess(true);
		
		return tx;
	}
	
	public TransactionPoolLPAdd toTxLiquidityAdd()
	{
		TransactionPoolLPAdd tx = new TransactionPoolLPAdd();
		tx.setFromHash(fromHash);
		tx.setFromWallet(walletService.getWallet(fromWallet));
		tx.setTimestamp(NocDateFormat.parse_yyyyMMddHHmmssHifen(timestamp));
		tx.setAssetX(xAsset);
		tx.setAmountX(new BigDecimal(xAmount));
		tx.setAssetY(yAsset);
		tx.setAmountY(new BigDecimal(yAmount));
		tx.setFeeAsset(feeAsset);
		tx.setFeeAmount(new BigDecimal(feeAmount));
		tx.setPoolName(poolName);
		tx.setPoolAmount(new BigDecimal(poolAmount));
		tx.setToAlloc(false);
		tx.setFromAlloc(false);
		tx.setSuccess(true);
		
		return tx;
	}
	
	public TransactionPoolLPRemove toTxLiquidityRemove()
	{
		LocalDateTime txTimestamp = NocDateFormat.parse_yyyyMMddHHmmssHifen(timestamp);
		BigDecimal txQuoteX = new BigDecimal(xQuote);
		BigDecimal txQuoteY = new BigDecimal(yQuote);
		priceService.newPriceAtMinute(xAsset, txQuoteX, txTimestamp);
		priceService.newPriceAtMinute(yAsset, txQuoteY, txTimestamp);
		
		TransactionPoolLPRemove tx = new TransactionPoolLPRemove();
		tx.setFromHash(fromHash);
		tx.setFromWallet(walletService.getWallet(fromWallet));		
		tx.setTimestamp(txTimestamp);
		tx.setAssetX(xAsset);
		tx.setAmountX(new BigDecimal(xAmount));
		tx.setAssetY(yAsset);
		tx.setAmountY(new BigDecimal(yAmount));
		tx.setFeeAsset(feeAsset);
		tx.setFeeAmount(new BigDecimal(feeAmount));
		tx.setPoolName(poolName);
		tx.setPoolAmount(new BigDecimal(poolAmount));
		tx.setToAlloc(false);
		tx.setFromAlloc(false);
		tx.setSuccess(true);
		
		return tx;
	}
	
	public void clear()
	{
		timestamp="";
		description="";
		xAsset=xAmount=xQuote="";
		yAsset=yAmount=yQuote="";
		feeAsset=feeAmount="";
		fromHash=toHash="";
		poolName=poolAmount="";
		fromWallet=toWallet="";
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public String getFromWallet() {
		return fromWallet;
	}
	
	public void setFromWallet(String fromWallet) {
		this.fromWallet = fromWallet;
	}
	
	public String getToWallet() {
		return toWallet;
	}

	public void setToWallet(String toWallet) {
		this.toWallet = toWallet;
	}

	public String getFromHash() {
		return fromHash;
	}

	public void setFromHash(String fromHash) {
		this.fromHash = fromHash;
	}

	public String getToHash() {
		return toHash;
	}

	public void setToHash(String toHash) {
		this.toHash = toHash;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getxAsset() {
		return xAsset;
	}
	
	public void setxAsset(String xAsset) {
		this.xAsset = xAsset;
	}
	
	public String getxAmount() {
		return xAmount;
	}
	
	public void setxAmount(String xAmount) {
		this.xAmount = xAmount;
	}
	
	public String getyAsset() {
		return yAsset;
	}
	
	public void setyAsset(String yAsset) {
		this.yAsset = yAsset;
	}
	
	public String getyAmount() {
		return yAmount;
	}
	
	public void setyAmount(String yAmount) {
		this.yAmount = yAmount;
	}
	
	public String getFeeAsset() {
		return feeAsset;
	}
	
	public void setFeeAsset(String feeAsset) {
		this.feeAsset = feeAsset;
	}
	
	public String getFeeAmount() {
		return feeAmount;
	}
	
	public void setFeeAmount(String feeAmount) {
		this.feeAmount = feeAmount;
	}
	
	public String getPoolName() {
		return poolName;
	}
	
	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}
	
	public String getPoolAmount() {
		return poolAmount;
	}
	
	public void setPoolAmount(String poolAmount) {
		this.poolAmount = poolAmount;
	}		
	
	public String getxQuote() {
		return xQuote;
	}
	
	public void setxQuote(String xQuote) {
		this.xQuote = xQuote;
	}
	
	public String getyQuote() {
		return yQuote;
	}
	
	public void setyQuote(String yQuote) {
		this.yQuote = yQuote;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
