package com.castaware.nocturne.datatypes;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.castaware.nocturne.util.NocMathUtils;
import com.castaware.nocturne.util.NocNumberFormat;

@Entity
@DiscriminatorValue("STAKING")
public class PoolStaking extends Pool
{
	//
	// ATTRIBUTES
	//
	@Column(name="assetX")
	protected String asset;
	
	@ManyToOne()
	@JoinColumn(name="assetX",referencedColumnName = "asset", insertable=false, updatable=false)
	protected AssetMeta meta;
	
	@Column(name="amountX")
    @Type(type = "com.castaware.nocturne.datatypes.dao.mappings.BigDecimalStringType")
	protected BigDecimal amount = BigDecimal.ZERO;		
	
	@Column(name="entryQuoteX")
	protected double entryQuote = 0d;	
	
	@Transient
	protected double liveQuote = 0d;	

	//
	// CONSTRUCTOR
	//
	public PoolStaking() 
	{
		super(PoolType.STAKING);	
	}
	
	//
	// OPERATIONS
	//
	public void childClear()
	{
		amount=BigDecimal.ZERO;
		entryQuote=0d;
	}
	
	public void addByQuote(BigDecimal buyAmount, double buyUsdQuote)
	{
		addByValue(buyAmount,buyAmount.doubleValue()*buyUsdQuote);					
	}
	
	public void addByValue(BigDecimal buyAmount, double buyUsdValue)
	{
		double entryValue = (amount.doubleValue()*entryQuote)+buyUsdValue;
		
		amount = amount.add(buyAmount);
		depositedAmount = depositedAmount.add(buyAmount);
		entryQuote = entryValue/amount.doubleValue();
		depositedQuote = entryQuote;
	}
	
	public void addEarn(BigDecimal addAmount)
	{
		double entryValue = getEntryValue(); 
		amount = amount.add(addAmount);		
		entryQuote = entryValue/amount.doubleValue();	
	}
	
	public void sub(BigDecimal subAmount, double subValue) 
	{
		amount = NocMathUtils.sub(asset, amount, subAmount, entryQuote);
		
		// Atualiza o valor corrente
		if (amount.doubleValue()==0)
		{
			entryQuote=0d;
		}
		
		// Atualiza valor retirado
		double oldExitValue = getWithdrawnValue();
		double newExitValue = oldExitValue+subValue; 
		withdrawnAmount = withdrawnAmount.add(subAmount);
		withdrawnQuote  = newExitValue/withdrawnAmount.doubleValue();		
	}
	
	//
	// JSF Accessors - Standard
	//
	public String getName() 
	{
		return asset;
	}
	
	public CustodyType getCustodyType() 
	{
		return CustodyType.SP;
	}
	
	public String getDecTitle()
	{
		return this.id;		
	}
	
	@Override
	public String getCategory() 
	{
		return meta.getCategory();
	}

	@Override
	public String getEcosystem() 
	{
		return meta.getEcosystem().toString();
	}
	
	public double getEntryValue() 
	{
		return amount.doubleValue()*entryQuote;
	}
	
	public double getLiveValue() 
	{
		return amount.doubleValue()*liveQuote;
	}
		
	//
	// META
	//
	@Override
	public String decString() 
	{
		double value = amount.doubleValue()*entryQuote;
		
		return String.format("%s (%s)",
				 NocNumberFormat.format(amount, asset),
				 NocNumberFormat.formatUsd(value));
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
	
	public double getEntryQuote() {
		return entryQuote;
	}
	
	public void setEntryQuote(double entryQuote) {
		this.entryQuote = entryQuote;
	}	
	
	public double getLiveQuote() {
		return liveQuote;
	}
	
	public void setLiveQuote(double liveQuote) {
		this.liveQuote = liveQuote;
	}	
}