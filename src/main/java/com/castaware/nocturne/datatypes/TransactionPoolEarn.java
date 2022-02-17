package com.castaware.nocturne.datatypes;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("POOL_EARN")
public class TransactionPoolEarn extends TransactionPoolSpotReward
{
	//
	// CONSTRUCTOR
	//
	public TransactionPoolEarn() 
	{
		super(TransactionType.POOL_EARN);	
	}
}





