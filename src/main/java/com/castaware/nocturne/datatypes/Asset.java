package com.castaware.nocturne.datatypes;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

import com.castaware.nocturne.util.NocMathUtils;

@Entity
public class Asset extends Custody
{
	//
	// Attributes
	//
	@Id
	private String id;
	
	@Column
	private String name;
	
	@ManyToOne()
	@JoinColumn(name="wallet")
	protected Wallet wallet;
	
	@ManyToOne()
	@JoinColumn(name="name",referencedColumnName = "asset", insertable=false, updatable=false)
	protected AssetMeta meta;
	
	@Column
    @Type(type = "com.castaware.nocturne.datatypes.dao.mappings.BigDecimalStringType")
	private BigDecimal amount = BigDecimal.ZERO;
	
	@Column
	private double entryQuote = 0d;
	
	@Column
	private double liveQuote  = 0d;
	
	//
	// Virtual Attributes
	//
	public CustodyType getCustodyType() 
	{
		return CustodyType.ASSET;
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
	// Constructor
	//
	protected Asset()
	{
		
	}
	
	public Asset(String name,Wallet wallet)
	{
		if (name == null || name.isEmpty()) 
			throw new IllegalArgumentException("Asset name cannot be null or empty");
		if (wallet == null) 
			throw new IllegalArgumentException("Wallet cannot be null");
		
		this.id=wallet.getId()+" "+name;
		this.name=name;
		this.wallet=wallet;
	}
		
	//
	// Operations
	//
	public void addEarn(BigDecimal addAmount)
	{
		double entryValue = getEntryValue();
		amount = amount.add(addAmount);
		entryQuote = entryValue/amount.doubleValue();
	}
	
	public void addByQuote(BigDecimal buyAmount, double buyUsdQuote)
	{
		addByValue(buyAmount,buyAmount.doubleValue()*buyUsdQuote);					
	}
	
	public void addByValue(BigDecimal buyAmount, double buyUsdValue)
	{
		double entryValue = (amount.doubleValue()*entryQuote)+buyUsdValue;
		
		amount = amount.add(buyAmount);
		entryQuote = entryValue/amount.doubleValue();		
	}
	
	public void sub(BigDecimal subAmount)
	{
		amount = NocMathUtils.sub(name, amount, subAmount, entryQuote);
		
		// Atualiza o valor em dolares
		if (amount.doubleValue()==0)
			entryQuote=0d;			
	}
	
	//
	// META
	//
	@Override
	public String toString() 
	{
		return String.format("[%s %s] (%,.2f) at %,.2f",
				 	   	     name,
				 	   	     amount,
				 	   	     getEntryValue(),
				 	   	     entryQuote);				
	}	
	
	//
	// POJO Access
	//	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Wallet getWallet() {
		return wallet;
	}
	
	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
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
	
	public AssetMeta getMeta() {
		return meta;
	}
	
	public void setMeta(AssetMeta meta) {
		this.meta = meta;
	}		
}