package com.castaware.nocturne.datatypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.castaware.nocturne.util.NocDateFormat;
import com.castaware.nocturne.util.NocNumberFormat;
import com.vladmihalcea.hibernate.type.json.JsonType;

@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type",discriminatorType = DiscriminatorType.STRING)
public abstract class Pool extends Custody
{
	//
	// Attributes
	//	
	@Id    	
	protected String id;
	
	@Transient
	protected PoolType type;
	
	@Column
	@Enumerated(EnumType.STRING)
	protected PoolStrategy strategy = PoolStrategy.COMPOUND;
	
	@JoinColumn(name="wallet")
	@ManyToOne(optional=true)
	protected Wallet wallet;
	
	@JoinColumn(name = "pool_name")
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, orphanRemoval = true)
	protected List<Transaction> transactions = new ArrayList<Transaction>();
	
	@Transient
	protected List<Transaction> revTransactions = new ArrayList<Transaction>();
	
	@Type(type = "json")
    @Column(columnDefinition = "json")
	protected List<Claimable> claimables = new ArrayList<Claimable>();
	
	@Column
    @Type(type = "com.castaware.nocturne.datatypes.dao.mappings.BigDecimalStringType")
	protected BigDecimal depositedAmount = BigDecimal.ZERO;
	
	@Column
    protected double depositedQuote = 0d;
	
	@Column
    @Type(type = "com.castaware.nocturne.datatypes.dao.mappings.BigDecimalStringType")
	protected BigDecimal withdrawnAmount = BigDecimal.ZERO;	
	
	@Column
	protected double withdrawnQuote = 0d;
	
	//
	// Constructors
	//
	private Pool()
	{
		
	}
	
	public Pool(PoolType type)
	{
		this();
		this.type=type;
	}
	
	//
	// OPERATIONS
	//
	public void clear()
	{
		depositedAmount=withdrawnAmount=BigDecimal.ZERO;
		depositedQuote=withdrawnQuote=0d;
		childClear();
	}
	
	protected abstract void childClear();	
		
	//
	// JSF Accessors - Standard
	//
	public abstract void addEarn(BigDecimal earn);
	public abstract String getName(); 
	
	public long getElapsedDays()
	{
		LocalDateTime startDate = transactions.get(0).getTimestamp();
		long daysSince = NocDateFormat.daysSince(startDate);
		return daysSince;		
	}
	
	public double getAPR()
	{
		if (this.strategy==PoolStrategy.EARN || this.strategy==PoolStrategy.CLAIM)
			return getEarningsApr();
		
		return 0d;
	}
	
	public double getROI()
	{
		double total = getDepositedValue();
		double part  = getROIValue();
		double divide = part/total;
		return divide;
	}
	
	public double getROIValue()
	{
		double earnings  = getEarningsValue();
		double withdrawn = getWithdrawnValue();
		double invested  = getDepositedValue();
		double live      = getLiveValue();
		
		return earnings+withdrawn-invested+live;
	}	
	
	public double getDepositedValue()
	{
		return depositedAmount.doubleValue()*depositedQuote;
	}
	
	public double getWithdrawnValue()
	{
		return withdrawnAmount.doubleValue()*withdrawnQuote;
	}
	
	public double getEarningsApr()
	{
		long daysSince = getElapsedDays();
		double earnings = getEarningsValue();
		
		double earningsByDay = earnings/daysSince;
		double earningsInYear = earningsByDay*365;
		
		double perc = earningsInYear/getDepositedValue();
		
		return perc;
	}
	
	public double getEarningsValue()
	{
		double earned     = getEarnedValue();
		double claimable  = getClaimableValue();
		
		return earned+claimable;
	}
	
	public double getClaimableValue()
	{
		double claimableValue = 0d;
		
		for (Claimable claimable : claimables)
		{
			double quote = claimable.getQuote().doubleValue();
			double amount = claimable.getAmount().doubleValue();
			double value  = amount*quote;
			claimableValue += value;
		}
		
		return claimableValue;
	}
	
	public double getEarnedValue()
	{
		double earned = 0d;
		
		for (Transaction tx: transactions)
		{
			if (tx instanceof TransactionPoolSpotReward)
			{
				TransactionPoolSpotReward reward = (TransactionPoolSpotReward)tx;
				earned += reward.getValue().doubleValue(); 
			}
		}
		
		return earned; 
	}
	
	public String getLastEarn()
	{
		List<String> earns = getLastEarns(1);
		
		if (earns.isEmpty())
			return "";
		else
			return earns.get(0);
	}
	
	public List<String> getLastEarns(int count)
	{
		List<String> earns = new ArrayList<String>();
		
		int aux=0;
		
		for (Transaction tx : getRevTransactions())
		{
			if (tx instanceof TransactionPoolSpotReward)
			{
				TransactionPoolSpotReward reward = (TransactionPoolSpotReward)tx;
				BigDecimal quote = reward.getQuote(); 
				
				String s = String.format("%s - %s (%s)", NocDateFormat.format_ddMM(reward.getTimestamp()),
												         NocNumberFormat.format(reward.getAmount(),reward.getAsset()),
												         NocNumberFormat.formatUsd(reward.getAmount().multiply(quote)));
				earns.add(s);
				
				if (++aux == count)
					return earns;
			}			
		}
		
		while (earns.size()<count)
			earns.add("");
		
		return earns; 
	}
	
	public double getCompoundedROI()
	{
		double total = getDepositedAmount().doubleValue();
		double part  = getCompoundedAmount();
		double divide = part/total;
		return divide;
	}
	
	public double getCompoundedValue()
	{
		return getCompoundedAmount()*getLiveQuote();			
	}
	
	public double getCompoundedAmount()
	{
		double compoundedAmount = 0d;
		
		for (Transaction tx: transactions)
		{
			if (tx instanceof TransactionPoolCompound)
			{
				TransactionPoolCompound compound = (TransactionPoolCompound)tx;
				compoundedAmount += compound.getAmount().doubleValue(); 
			}
		}
		
		return compoundedAmount;				
	}
	
	public String getLastCompound()
	{
		return getLastCompounds(1).get(0);
	}
	
	public List<String> getLastCompounds(int count)
	{
		List<String> compounds = new ArrayList<String>();
		
		int aux=0;
		
		for (Transaction tx : getRevTransactions())
		{
			if (tx instanceof TransactionPoolCompound)
			{
				TransactionPoolCompound compound = (TransactionPoolCompound)tx;
				
				String s = String.format("%s - %s LPT", NocDateFormat.format_ddMM(compound.getTimestamp()),
												        NocNumberFormat.format(compound.getAmount()));
				compounds.add(s);
				
				if (++aux == count)
					return compounds;
			}
		}
		
		while (compounds.size()<count)
			compounds.add("");
		
		return compounds; 
	}
	
	//
	// META
	//	
	public abstract String decString();
	
	@Override
	public String toString() 
	{
		StringBuilder builder = new StringBuilder();
		builder.append("["+type.toString()+"_POOL] "+id+" - ");
		builder.append(decString());
		return builder.toString();
	}
	
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
		Pool other = (Pool) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	//
	// POJO ACCESS
	//
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PoolType getType() {
		return type;
	}

	public void setType(PoolType type) {
		this.type = type;
	}
	
	public Wallet getWallet() {
		return wallet;
	}
	
	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}
		
	public List<Claimable> getClaimables() {
		return claimables;
	}
	
	public void setClaimables(List<Claimable> claimables) {
		this.claimables = claimables;
	}
	
	public List<Transaction> getTransactions() {
		return transactions;
	}
	
	public List<Transaction> getRevTransactions() 
	{
		if(revTransactions==null || revTransactions.isEmpty())
		{
			revTransactions = new ArrayList<Transaction>(transactions);
			Collections.reverse(revTransactions);
		}
		
		return revTransactions;
	}
	
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	
	public BigDecimal getDepositedAmount() {
		return depositedAmount;
	}
	
	public void setDepositedAmount(BigDecimal depositedAmount) {
		this.depositedAmount = depositedAmount;
	}

	public double getDepositedQuote() {
		return depositedQuote;
	}

	public void setDepositedQuote(double depositedQuote) {
		this.depositedQuote = depositedQuote;
	}

	public BigDecimal getWithdrawnAmount() {
		return withdrawnAmount;
	}

	public void setWithdrawnAmount(BigDecimal withdrawnAmount) {
		this.withdrawnAmount = withdrawnAmount;
	}

	public double getWithdrawnQuote() {
		return withdrawnQuote;
	}

	public void setWithdrawnQuote(double withdrawnQuote) {
		this.withdrawnQuote = withdrawnQuote;
	}
	
	public PoolStrategy getStrategy() {
		return strategy;
	}
	
	public String getStrategyString() {
		return strategy.toString();
	}
	
	public void setStrategy(PoolStrategy strategy) {
		this.strategy = strategy;
	}
	
	public void setStrategyString(String strategy) {
		this.strategy = PoolStrategy.valueOf(strategy);
	}
}