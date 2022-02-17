package com.castaware.nocturne.datatypes;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Type;

import com.castaware.nocturne.util.NocNumberFormat;

@Entity
@DiscriminatorValue("REMOVE_SP")
public class TransactionPoolSPRemove extends TransactionPool
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
	public TransactionPoolSPRemove() 
	{
		super(TransactionType.REMOVE_SP);	
	}
	
	//
	// JSF DECORATORS
	//
	public String decDesc(Wallet wallet) 
	{
		return String.format("%s",
				 NocNumberFormat.format(amount, asset));
	};
	
	//
	// META
	//
	@Override
	public String decString() 
	{
		return String.format("remove %s from %s",
							 NocNumberFormat.format(amount, asset),
							 poolName);
				 	
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