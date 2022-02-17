package com.castaware.nocturne.api.wrappers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import com.castaware.nocturne.util.NocDateFormat;
import com.castaware.nocturne.util.NocNumberFormat;

public class BwDustTransfer
{
	protected String id;
	protected LocalDateTime timestamp;
	protected String fromAsset;	
	protected String toAsset;
	protected BigDecimal fromAmount;
	protected BigDecimal toAmount;
	protected String feeAsset;	
	protected BigDecimal feeAmount;
	protected Boolean isBuyer;
	protected String wallet = "BINANCE";
	
	public BwDustTransfer()
	{
	
	}
	
	public BwDustTransfer(Long id,
                          String fromAsset, BigDecimal fromAmount,
						  String toAsset,   BigDecimal toAmount,
						  String feeAsset,  BigDecimal feeAmount,
						  Long timestamp)
	{
		this.id=id+"_dust";
		this.fromAsset = fromAsset;
		this.toAsset = toAsset;
		this.fromAmount = fromAmount;
		this.toAmount = toAmount;
		this.feeAsset = feeAsset;
		this.feeAmount = feeAmount;		
		this.isBuyer = true;		
		this.timestamp = NocDateFormat.parse_timestamp(timestamp);
	}
	
	@Override
	public String toString()
	{
		return String.format("%s - [%s] %s %s for %s at %s %s/%s - fee %s",
						     NocDateFormat.format_ddMMyyyyHHmm(timestamp),
						     "DUST_TRANSFER",
						     isBuyer?"BUY":"SELL",
						     NocNumberFormat.format(toAmount,toAsset),
						     NocNumberFormat.format(fromAmount,fromAsset),
						     NocNumberFormat.format(fromAmount.divide(toAmount,20,RoundingMode.HALF_EVEN)),		 
						     toAsset,
							 fromAsset,
							 NocNumberFormat.format(feeAmount,feeAsset));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getFromAsset() {
		return fromAsset;
	}

	public void setFromAsset(String fromAsset) {
		this.fromAsset = fromAsset;
	}

	public String getToAsset() {
		return toAsset;
	}

	public void setToAsset(String toAsset) {
		this.toAsset = toAsset;
	}

	public BigDecimal getFromAmount() {
		return fromAmount;
	}

	public void setFromAmount(BigDecimal fromAmount) {
		this.fromAmount = fromAmount;
	}

	public BigDecimal getToAmount() {
		return toAmount;
	}

	public void setToAmount(BigDecimal toAmount) {
		this.toAmount = toAmount;
	}

	public String getFeeAsset() {
		return feeAsset;
	}

	public void setFeeAsset(String feeAsset) {
		this.feeAsset = feeAsset;
	}

	public BigDecimal getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}

	public Boolean getIsBuyer() {
		return isBuyer;
	}

	public void setIsBuyer(Boolean isBuyer) {
		this.isBuyer = isBuyer;
	}

	public String getWallet() {
		return wallet;
	}

	public void setWallet(String wallet) {
		this.wallet = wallet;
	}	
}





