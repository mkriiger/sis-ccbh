package com.castaware.nocturne.datatypes;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.castaware.nocturne.util.NocNumberFormat;

@Entity
@DiscriminatorValue("ADD_LP")
public class TransactionPoolLPAdd extends TransactionPoolLP
{
	//
	// CONSTRUCTOR
	//
	public TransactionPoolLPAdd() 
	{
		super(TransactionType.ADD_LP);	
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
		return String.format("add %s | %s to %s for %s",
				 NocNumberFormat.format(amountX, assetX),
				 NocNumberFormat.format(amountY, assetY),
				 poolName,
				 NocNumberFormat.format(poolAmount, "LPT"));	
	}
}