package com.castaware.nocturne.datatypes;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Type;

import com.castaware.nocturne.util.NocNumberFormat;

@Entity
@DiscriminatorValue("BURN")
public class TransactionBurn extends Transaction
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
	public TransactionBurn() 
	{
		super(TransactionType.BURN);	
	}
	
	//
	// JSF DECORATORS
	//
	public String decIcon(Wallet wallet)
	{
		return "trash";
	}
	
	public String decColor(Wallet wallet)
	{
		return "tomato";
	}
	
	public String decTitle(Wallet wallet)
	{
		return type.name();
	}
	
	public String decDesc(Wallet wallet) 
	{
		return NocNumberFormat.format(amount, asset);		
	};
	
	//
	// META
	//
	@Override
	public String decString() 
	{
		return NocNumberFormat.format(amount,asset);
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





