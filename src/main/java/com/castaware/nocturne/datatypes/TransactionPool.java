package com.castaware.nocturne.datatypes;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class TransactionPool extends Transaction
{
	//
	// ATTRIBUTES
	//
	@Column(name="pool_name")
	protected String poolName;	
	
	//
	// CONSTRUCTOR
	//
	public TransactionPool(TransactionType type) 
	{
		super(type);	
	}
	
	public String getPoolName() {
		return poolName;
	}
	
	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}
}





