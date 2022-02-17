package com.castaware.nocturne.datatypes;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Type;

import com.castaware.nocturne.util.NocNumberFormat;

@Entity
@DiscriminatorValue("TRANSFER")
public class TransactionTransfer extends Transaction
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
	public TransactionTransfer() 
	{
		super(TransactionType.TRANSFER);	
	}
	
	//
	// JSF DECORATORS
	//
	public String decIcon(Wallet wallet)
	{
		if (wallet.equals(fromWallet))
			return  "upload";
		else
			return  "download";
	}
	
	public String decColor(Wallet wallet)
	{
		if (wallet.equals(fromWallet))
			return  "tomato";
		else
			return  "chartreuse";
	}
	
	public String decTitle(Wallet wallet)
	{
		if (wallet.equals(fromWallet))
			return "WITHDRAW "+type.name();
		else
			return "DEPOSIT "+type.name();
	}
	
	public String decDesc(Wallet wallet) 
	{
		if (wallet.equals(fromWallet))
			return NocNumberFormat.format(amount, asset)+" to "+toWallet.name;
		else
			return NocNumberFormat.format(amount, asset)+" from "+fromWallet.name;
	};
	
	//
	// META
	//
	@Override
	public String decString() 
	{
		return String.format("%s from %s to %s",
							 NocNumberFormat.format(amount, asset),
							 fromWallet.name,
							 toWallet.name);
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





