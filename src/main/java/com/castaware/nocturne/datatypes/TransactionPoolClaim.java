package com.castaware.nocturne.datatypes;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("POOL_CLAIM")
public class TransactionPoolClaim extends TransactionPoolSpotReward
{
	//
	// CONSTRUCTOR
	//
	public TransactionPoolClaim() 
	{
		super(TransactionType.POOL_CLAIM);	
	}
}





