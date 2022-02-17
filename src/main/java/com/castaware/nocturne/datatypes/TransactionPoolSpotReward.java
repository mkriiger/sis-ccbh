package com.castaware.nocturne.datatypes;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

import com.castaware.nocturne.util.NocNumberFormat;

@MappedSuperclass
public abstract class TransactionPoolSpotReward extends TransactionPool
{
	//
	// ATTRIBUTES
	//
	@Column(name="assetX")
	protected String asset;
	
	@Column(name="amountX")
    @Type(type = "com.castaware.nocturne.datatypes.dao.mappings.BigDecimalStringType")
	protected BigDecimal amount;
	
	@Column(name="transfer_assetQuote")
    @Type(type = "com.castaware.nocturne.datatypes.dao.mappings.BigDecimalStringType")
	protected BigDecimal quote;
	
	//
	// CONSTRUCTOR
	//
	public TransactionPoolSpotReward(TransactionType type) 
	{
		super(type);	
	}
	
	//
	// JSF DECORATORS
	//
	public String decIcon(Wallet wallet) 
	{
		return "dollar";
	}

	@Override
	public String decColor(Wallet wallet) 
	{
		return "chartreuse";
	}
	
	
	public String decDesc(Wallet wallet) 
	{
		return decDesc();		
	};
	
	private String decDesc()
	{
		return String.format("%s from %s",
				 NocNumberFormat.format(amount, asset),
				 poolName);
	}
	
	public BigDecimal getValue() {
		return amount.multiply(quote);
	}
	
	//
	// META
	//
	@Override
	public String decString() 
	{
		return decDesc();
	}
	
	//
	// POJO ACCESS
	//
	public String getAsset() {
		return asset;
	}
	
	public void setAsset(String asset) {
		this.asset = asset;
	}	
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public BigDecimal getQuote() {
		return quote;
	}
	
	public void setQuote(BigDecimal quote) {
		this.quote = quote;
	}
}





