package com.castaware.nocturne.datatypes;

import java.math.BigDecimal;

import com.castaware.nocturne.util.NocNumberFormat;

public class Game
{
	//
	// ATTRIBUTES
	//
	protected String 	 name;
	protected BigDecimal investedValue = BigDecimal.ZERO;		
	
	//
	// CONSTRUCTOR
	//
	public Game(String name) 
	{
		this.name=name;
	}
	
	//
	// OPERATIONS
	//
	public void clear()
	{
		this.investedValue=BigDecimal.ZERO;
	}
	
	public void addByQuote(BigDecimal addAmount, BigDecimal addUsdQuote)
	{
		addByValue(addAmount,addAmount.multiply(addUsdQuote));					
	}
	
	public void addByValue(BigDecimal addAmount, BigDecimal addUsdValue)
	{
		investedValue = investedValue.add(addUsdValue);	
	}
	
	//
	// JSF DECORATORS
	//
	public String getDecInvestedValue() 
	{
		return NocNumberFormat.formatUsd(investedValue);
	};	
			
	//
	// META
	//
	@Override
	public String toString() 
	{
		return String.format("[%s] %s invested",name,getDecInvestedValue());
	}

	//
	// POJO ACCESS
	//			
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getInvestedValue() {
		return investedValue;
	}

	public void setInvestedValue(BigDecimal investedValue) {
		this.investedValue = investedValue;
	}
}