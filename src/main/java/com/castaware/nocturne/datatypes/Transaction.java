package com.castaware.nocturne.datatypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.castaware.nocturne.util.NocDateFormat;
import com.castaware.nocturne.util.NocNumberFormat;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type",discriminatorType = DiscriminatorType.STRING)
public abstract class Transaction implements Comparable<Transaction>
{
	//
	// Attributes
	//	
	@Id    	
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Integer id;
	
	@Column
	protected LocalDateTime timestamp;
	
	@Transient
	protected TransactionType type;
	
	@JoinColumn(name="fromWallet")
	@ManyToOne(optional=true)
	protected Wallet fromWallet;
	
	@JoinColumn(name="toWallet")
	@ManyToOne(optional=true)
	protected Wallet toWallet;
	
	@Column    
	protected String feeAsset;
	
	@Column
    @Type(type = "com.castaware.nocturne.datatypes.dao.mappings.BigDecimalStringType")
	protected BigDecimal feeAmount;
		
	@Column
	protected String toHash;
	
	@Column
	protected String fromHash;
	
	@Column
	protected Boolean success = true;
		
	@Column
	protected Boolean toAlloc = false;
	
	@Column
	protected Boolean fromAlloc = false;
	
	@Column(name="misc_desc")
	protected String description;
	
	//
	// Constructors
	//
	private Transaction()
	{
		
	}
	
	public Transaction(TransactionType type)
	{
		this();
		this.type=type;
	}
	
	//
	// JSF DECORATORS
	//
	public String getIcon(Wallet wallet)
	{
		if (!success)
			return "times";
		
		return decIcon(wallet);
	}
	
	public String getColor(Wallet wallet)
	{
		if (!success)
			return "tomato";
		
		return decColor(wallet);
	}
	
	public String getTitle(Wallet wallet)
	{
		String title = decTitle(wallet);
		
		if (!success)
			title+=" (fail)";
		
		return title;
	}
	
	public String getDesc(Wallet wallet)
	{
		if (!success)
		{
			if (feeAsset!=null)
				return NocNumberFormat.format(feeAmount, feeAsset);
			else
				return "no costs";
		}
		
		return decDesc(wallet);
	}
	
	public String getDatetime() 
	{
		return NocDateFormat.format_ddMMyyyyHHmm(timestamp);
	};
	
	public String decIcon(Wallet wallet)
	{
		return "bookmark";
	}
	
	public String decColor(Wallet wallet)
	{
		return "aqua";
	}
	
	public String decTitle(Wallet wallet)
	{
		return type.name();
	}
	
	public String decDesc(Wallet wallet) 
	{
		if (feeAsset!=null)
			return NocNumberFormat.format(feeAmount, feeAsset);
		else
			return "no costs";
	};
	
	//
	// META
	//	
	@Override
	public String toString() 
	{
		StringBuilder builder = new StringBuilder();
		builder.append(NocDateFormat.format_ddMMyyyyHHmm(timestamp)+" ["+type.toString()+"] ");
		
		if (!success)
		{
			builder.append("(FAIL)");
		}
		else
		{
			builder.append(decString());
		}
		
		if (feeAsset!=null)
		{
			builder.append(" | Fee "+NocNumberFormat.format(feeAmount,feeAsset,6));
		}
						
		return builder.toString();
	}
	
	public abstract String decString();
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Transaction other)
	{
		int compare = timestamp.compareTo(other.timestamp);
		
		if (compare==0)
			compare = id.compareTo(other.id);
		
		return compare;		
	}
	
	//
	// POJO ACCESS
	//
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public Wallet getFromWallet() {
		return fromWallet;
	}

	public void setFromWallet(Wallet fromWallet) {
		this.fromWallet = fromWallet;
	}

	public Wallet getToWallet() {
		return toWallet;
	}

	public void setToWallet(Wallet toWallet) {
		this.toWallet = toWallet;
	}

	public String getFeeAsset() {
		return feeAsset;
	}

	public void setFeeAsset(String feeAsset) {
		this.feeAsset = feeAsset;
	}

	public BigDecimal getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}

	public String getToHash() {
		return toHash;
	}

	public void setToHash(String toHash) {
		this.toHash = toHash;
	}

	public String getFromHash() {
		return fromHash;
	}

	public void setFromHash(String fromHash) {
		this.fromHash = fromHash;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Boolean getToAlloc() {
		return toAlloc;
	}

	public void setToAlloc(Boolean toAlloc) {
		this.toAlloc = toAlloc;
	}

	public Boolean getFromAlloc() {
		return fromAlloc;
	}

	public void setFromAlloc(Boolean fromAlloc) {
		this.fromAlloc = fromAlloc;
	};
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}