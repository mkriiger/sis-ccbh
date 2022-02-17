package com.castaware.nocturne.datatypes;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Type;

import com.castaware.nocturne.util.NocNumberFormat;

@Entity
@DiscriminatorValue("MINT_NFT")
public class TransactionMintNFT extends Transaction
{
	//
	// ATTRIBUTES
	//
	@Column(name="assetX")
	protected String asset;
	
	@Column(name="amountX")
    @Type(type = "com.castaware.nocturne.datatypes.dao.mappings.BigDecimalStringType")
	protected BigDecimal amount;
	
	@Column(name="nft_name")
	protected String nftName;

	@Column(name="nft_id")
	protected String nftId;
	
	//
	// CONSTRUCTOR
	//
	public TransactionMintNFT() 
	{
		super(TransactionType.MINT_NFT);	
	}
	
	//
	// JSF DECORATORS
	//
	public String decDesc(Wallet wallet) 
	{
		return decDesc();
	};
	
	private String decDesc()
	{
		return String.format("%s for %s",
				 NocNumberFormat.format(amount, asset),
				 nftName);
	}
	
	//
	// META
	//
	@Override
	public String decString() 
	{
		return decDesc();
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

	public String getNftName() {
		return nftName;
	}
	
	public void setNftName(String nftName) {
		this.nftName = nftName;
	}
	
	public String getNftId() {
		return nftId;
	}
	
	public void setNftId(String nftId) {
		this.nftId = nftId;
	}
}





