package com.castaware.nocturne.datatypes;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("MISC")
public class TransactionMisc extends Transaction
{
	//
	// CONSTRUCTOR
	//
	public TransactionMisc() 
	{
		super(TransactionType.MISC);	
	}
	
	//
	// JSF DECORATORS
	//
	
	//
	// META
	//
	@Override
	public String decTitle(Wallet wallet) {
		return description;
	}
	
	@Override
	public String decString() {
		return description;
	}
	
	//
	// POJO ACCESS
	//		
}





