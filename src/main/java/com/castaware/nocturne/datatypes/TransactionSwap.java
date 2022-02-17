package com.castaware.nocturne.datatypes;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Type;

import com.castaware.nocturne.util.NocNumberFormat;

@Entity
@DiscriminatorValue("SWAP")
public class TransactionSwap extends Transaction
{
	//
	// ATTRIBUTES
	//
	@Column(name="assetX")
	protected String fromAsset;
	
	@Column(name="amountX")
    @Type(type = "com.castaware.nocturne.datatypes.dao.mappings.BigDecimalStringType")
	protected BigDecimal fromAmount;
	
	@Column(name="assetY")
	protected String toAsset;
	
	@Column(name="amountY")
    @Type(type = "com.castaware.nocturne.datatypes.dao.mappings.BigDecimalStringType")
	protected BigDecimal toAmount;
	
	//
	// CONSTRUCTOR
	//
	public TransactionSwap() 
	{
		super(TransactionType.SWAP);	
	}
	
	//
	// JSF DECORATORS
	//
	public String decDesc(Wallet wallet) 
	{
		return NocNumberFormat.format(fromAmount, fromAsset)+" to "+NocNumberFormat.format(toAmount, toAsset);
	};
	
	//
	// META
	//
	@Override
	public String decString() 
	{
		return NocNumberFormat.format(fromAmount, fromAsset)+" to "+NocNumberFormat.format(toAmount, toAsset);
	}
	
	//
	// POJO ACCESS
	//
	public String getFromAsset() {
		return fromAsset;
	}

	public void setFromAsset(String fromAsset) {
		this.fromAsset = fromAsset;
	}

	public BigDecimal getFromAmount() {
		return fromAmount;
	}

	public void setFromAmount(BigDecimal fromAmount) {
		this.fromAmount = fromAmount;
	}

	public String getToAsset() {
		return toAsset;
	}

	public void setToAsset(String toAsset) {
		this.toAsset = toAsset;
	}

	public BigDecimal getToAmount() {
		return toAmount;
	}

	public void setToAmount(BigDecimal toAmount) {
		this.toAmount = toAmount;
	}
}





