package com.castaware.nocturne.datatypes;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Type;

import com.castaware.nocturne.util.NocNumberFormat;

@Entity
@DiscriminatorValue("RAMP")
public class TransactionRamp extends Transaction
{
	//
	// ATTRIBUTES
	//
	@Column(name="assetX")
	protected String asset;
	
	@Column(name="amountX")
    @Type(type = "com.castaware.nocturne.datatypes.dao.mappings.BigDecimalStringType")
	protected BigDecimal amount;
	
	@Column(name="transfer_assetQuote")
    @Type(type = "com.castaware.nocturne.datatypes.dao.mappings.BigDecimalStringType")
	protected BigDecimal assetQuote;
	
	//
	// CONSTRUCTOR
	//
	public TransactionRamp() 
	{
		super(TransactionType.RAMP);	
	}
	
	//
	// JSF DECORATORS
	//
	public String decIcon(Wallet wallet)
	{
		return "download";
	}
	
	public String decColor(Wallet wallet)
	{
		return "chartreuse";
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
	
	public BigDecimal getAssetQuote() {
		return assetQuote;
	}
	
	public void setAssetQuote(BigDecimal assetQuote) {
		this.assetQuote = assetQuote;
	}
}





