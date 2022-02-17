package com.castaware.nocturne.datatypes;

import java.math.BigDecimal;

public class Claimable 
{
	private String 	   asset;
	private BigDecimal amount;
	private BigDecimal quote;
	
	public Claimable()
	{
		this.amount = BigDecimal.ZERO;
		this.quote = BigDecimal.ZERO;
	}
	
	public Claimable(String asset, BigDecimal amount) 
	{
		super();
		this.asset = asset;
		this.amount = amount;
		this.quote = BigDecimal.ZERO;
	}
	
	public Claimable(String asset, BigDecimal amount, BigDecimal quote) 
	{
		super();
		this.asset = asset;
		this.amount = amount;
		this.quote = quote;
	}
	
	public BigDecimal calcValue() {
		return amount.multiply(quote);
	}
	
	public String getAsset() {
		return asset;
	}
	public void setAsset(String name) {
		this.asset = name;
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
