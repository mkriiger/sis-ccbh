package com.castaware.nocturne.datatypes;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.castaware.nocturne.util.NocNumberFormat;

@Entity
@DiscriminatorValue("REMOVE_LP")
public class TransactionPoolLPRemove extends TransactionPoolLP
{
	//
	// CONSTRUCTOR
	//
	public TransactionPoolLPRemove() 
	{
		super(TransactionType.REMOVE_LP);	
	}
	
	//
	// JSF DECORATORS
	//
	public String decDesc(Wallet wallet) 
	{
		return String.format("%s | %s",
				 NocNumberFormat.format(amountX, assetX),
				 NocNumberFormat.format(amountY, assetY));
	};
	
	//
	// META
	//
	@Override
	public String decString() 
	{
		return String.format("remove %s | %s from %s with %s",
				 NocNumberFormat.format(amountX, assetX),
				 NocNumberFormat.format(amountY, assetY),
				 poolName,
				 NocNumberFormat.format(poolAmount, "LPT"));	
	}
}