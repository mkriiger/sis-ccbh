package com.castaware.nocturne.datatypes;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

@MappedSuperclass
public abstract class TransactionPoolLP extends TransactionPool
{
	//
	// ATTRIBUTES
	//
	@Column(name="assetX")
	protected String assetX;
	
	@Column(name="amountX")
    @Type(type = "com.castaware.nocturne.datatypes.dao.mappings.BigDecimalStringType")
	protected BigDecimal amountX;
	
	@Column(name="assetY")
	protected String assetY;
	
	@Column(name="amountY")
    @Type(type = "com.castaware.nocturne.datatypes.dao.mappings.BigDecimalStringType")
	protected BigDecimal amountY;
	
	@Column(name="pool_amount")
	protected BigDecimal poolAmount;
	
	//
	// Constructor
	//
	public TransactionPoolLP(TransactionType type) 
	{
		super(type);
	}
	
	//
	// POJO ACCESS
	//		
	public String getAssetX() {
		return assetX;
	}

	public void setAssetX(String assetX) {
		this.assetX = assetX;
	}

	public BigDecimal getAmountX() {
		return amountX;
	}

	public void setAmountX(BigDecimal amountX) {
		this.amountX = amountX;
	}

	public String getAssetY() {
		return assetY;
	}

	public void setAssetY(String assetY) {
		this.assetY = assetY;
	}

	public BigDecimal getAmountY() {
		return amountY;
	}

	public void setAmountY(BigDecimal amountY) {
		this.amountY = amountY;
	}

	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	public BigDecimal getPoolAmount() {
		return poolAmount;
	}

	public void setPoolAmount(BigDecimal poolAmount) {
		this.poolAmount = poolAmount;
	}
}





