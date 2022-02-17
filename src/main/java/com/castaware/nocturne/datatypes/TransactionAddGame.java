package com.castaware.nocturne.datatypes;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Type;

import com.castaware.nocturne.util.NocNumberFormat;

@Entity
@DiscriminatorValue("ADD_GAME")
public class TransactionAddGame extends Transaction
{
	//
	// ATTRIBUTES
	//
	@Column(name="assetX")
	protected String asset;
	
	@Column(name="amountX")
    @Type(type = "com.castaware.nocturne.datatypes.dao.mappings.BigDecimalStringType")
	protected BigDecimal amount;
	
	@Column(name="pool_name")
	protected String gameName;
	
	//
	// CONSTRUCTOR
	//
	public TransactionAddGame() 
	{
		super(TransactionType.ADD_GAME);	
	}
	
	//
	// JSF DECORATORS
	//
	public String decDesc(Wallet wallet) 
	{
		return String.format("%s",
				 NocNumberFormat.format(amount, asset));
	};
	
	//
	// META
	//
	@Override
	public String decString() 
	{
		return String.format("add %s to %s",
							 NocNumberFormat.format(amount, asset),
							 gameName);
				 	
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

	public String getGameName() {
		return gameName;
	}

	public void setPoolName(String gameName) {
		this.gameName = gameName;
	}
}





