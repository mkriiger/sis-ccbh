package com.castaware.nocturne.datatypes;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Type;

import com.castaware.nocturne.util.NocNumberFormat;

@Entity
@DiscriminatorValue("POOL_COMPOUND")
public class TransactionPoolCompound extends TransactionPool
{
	//
	// ATTRIBUTES
	//
	@Column(name="assetX")
	protected String asset;
	
	@Column(name="amountX")
    @Type(type = "com.castaware.nocturne.datatypes.dao.mappings.BigDecimalStringType")
	protected BigDecimal amount;
	
	//
	// CONSTRUCTOR
	//
	public TransactionPoolCompound() 
	{
		super(TransactionType.POOL_COMPOUND);	
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
		return String.format("%s in %s",
				 NocNumberFormat.format(amount, asset),
				 poolName);
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
}





