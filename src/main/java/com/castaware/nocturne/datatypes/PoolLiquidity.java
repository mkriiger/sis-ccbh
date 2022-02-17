package com.castaware.nocturne.datatypes;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import com.castaware.nocturne.util.NocMathUtils;
import com.castaware.nocturne.util.NocNumberFormat;

@Entity
@DiscriminatorValue("LIQUIDITY")
public class PoolLiquidity extends Pool
{
	//
	// ATTRIBUTES
	//
	@Column
	protected String assetX;
	
	@ManyToOne()
	@JoinColumn(name="assetX",referencedColumnName = "asset", insertable=false, updatable=false)
	protected AssetMeta metaX;
	
	@Column
    @Type(type = "com.castaware.nocturne.datatypes.dao.mappings.BigDecimalStringType")
	protected BigDecimal amountX = BigDecimal.ZERO;
	
	@Column
	protected double entryQuoteX = 0d;
	
	@Transient
	protected double liveQuoteX = 0d;
	
	@Column(name="assetY")
	protected String assetY;
	
	@ManyToOne()
	@JoinColumn(name="assetY",referencedColumnName = "asset", insertable=false, updatable=false)
	protected AssetMeta metaY;
	
	@Column(name="amountY")
    @Type(type = "com.castaware.nocturne.datatypes.dao.mappings.BigDecimalStringType")
	protected BigDecimal amountY = BigDecimal.ZERO;
		
	@Column
	protected double entryQuoteY = 0d;
	
	@Transient
	protected double liveQuoteY = 0d;
	
	@Column(name="liquidity_amountLP")
    @Type(type = "com.castaware.nocturne.datatypes.dao.mappings.BigDecimalStringType")
	protected BigDecimal amount = BigDecimal.ZERO;
	
	@Column(name="liquidity_quoteLP")
	protected double entryQuote = 0d;
	
	@Column(name="liquidity_liveQuoteLP") 
	protected double liveQuote = 0d;
			
	//
	// CONSTRUCTOR
	//
	public PoolLiquidity() 
	{
		super(PoolType.LIQUIDITY);	
	}
	
	//
	// OPERATIONS
	//
	public void childClear()
	{
		amountX=amountY=amount=BigDecimal.ZERO;
		entryQuoteX=entryQuoteY=entryQuote=0d;
	}
	
	public void addEarn(BigDecimal addAmount)
	{
		double entryValue = getEntryValue(); 
		amount = amount.add(addAmount);		
		entryQuote = entryValue/amount.doubleValue();	
	}
	
	public void sub(BigDecimal subAmount, double subValue)
	{
		amount = NocMathUtils.sub(id, amount, subAmount);
		
		// atualiza valor corrente
		if (amount.doubleValue()==0)
		{
			amountX=amountY=BigDecimal.ZERO;
			entryQuoteX=entryQuoteY=0d;
		}
		
		// Atualiza valor retirado
		double oldExitValue = getWithdrawnValue();
		double newExitValue = oldExitValue+subValue; 
		withdrawnAmount = withdrawnAmount.add(subAmount);
		withdrawnQuote  = newExitValue/withdrawnAmount.doubleValue();			
	}
	
	public void add(BigDecimal addAmount, double addQuote)
	{
		double currentValue = this.getEntryValue();
		double addValue = addAmount.doubleValue()*addQuote;
		double totalValue = currentValue+addValue;
		
		amount = amount.add(addAmount);
		entryQuote = totalValue/amount.doubleValue();
	}
	
	public void add(BigDecimal addAmountX,double addEntryQuoteX,
			        BigDecimal addAmountY,double addEntryQuoteY,
			        BigDecimal addAmount)
	{
		double currentValueX = amountX.doubleValue()*entryQuoteX;
		double addedValueX = addAmountX.doubleValue()*addEntryQuoteX;
		double valueX = 0d;
		amountX = amountX.add(addAmountX);
		entryQuoteX = (currentValueX+addedValueX)/amountX.doubleValue();
		valueX = amountX.doubleValue()*entryQuoteX;
		
		double currentValueY = amountY.doubleValue()*entryQuoteY;
		double addedValueY = addAmountY.doubleValue()*addEntryQuoteY;
		double valueY = 0d;
		
		if(addedValueY>0)
		{
			amountY = amountY.add(addAmountY);
			entryQuoteY = (currentValueY+addedValueY)/amountY.doubleValue();
			valueY = amountY.doubleValue()*entryQuoteY;
		}
		else
		{
			entryQuoteY = 0d;
		}
		
		double totalValue = valueX+valueY;
		amount = amount.add(addAmount);
		depositedAmount = depositedAmount.add(addAmount);
		entryQuote = totalValue/amount.doubleValue();
		depositedQuote = entryQuote;
	}		
	
	//
	// JSF Accessors - Standard
	//
	@Override
	public String getName() 
	{
		return id;
	}
	
	public CustodyType getCustodyType() 
	{
		return CustodyType.LP;
	}
	
	@Override
	public String getCategory() 
	{
		return metaY.getCategory();
	}

	@Override
	public String getEcosystem() 
	{
		return metaY.getEcosystem().toString();
	}
	
	public String getDecTitle()
	{
		return StringUtils.split(id,"(")[0].trim();		
	}
	
	public double getEntryValue() 
	{
		return amount.doubleValue()*entryQuote;		
	}
	
	public double getEntryValueX() 
	{
		return amountX.doubleValue()*entryQuoteX;		
	}
	
	public double getEntryValueY() 
	{
		return amountY.doubleValue()*entryQuoteY;		
	}
	
	public double getLiveQuote() 
	{
		return liveQuote;
	}
	
	public void setLiveQuote(double liveQuote) 
	{
		this.liveQuote = liveQuote;		
	}
	
	public double getLiveValue() 
	{
		if(liveQuote>0)
			return amount.doubleValue()*liveQuote;
		else
			return 0d;
	}
	
	public void setLiveValue(double liveValue) 
	{
		this.liveQuote = liveValue/amount.doubleValue();
	}
	
	//
	// META
	//
	@Override
	public String decString() 
	{
		double xValue = amountX.doubleValue()*entryQuoteX;
		double yValue = amountY.doubleValue()*entryQuoteY;
		
		return String.format("%s (%s) | %s (%s) - TOTAL (%s) - FOR (%s)",
				 NocNumberFormat.format(amountX, assetX),
				 NocNumberFormat.formatUsd(xValue),
				 NocNumberFormat.format(amountY, assetY),
				 NocNumberFormat.formatUsd(yValue),
				 NocNumberFormat.formatUsd(xValue+yValue),
				 NocNumberFormat.format(amount,id));
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
	
	public double getEntryQuoteX() {
		return entryQuoteX;
	}
	
	public void setEntryQuoteX(double entryQuoteX) {
		this.entryQuoteX = entryQuoteX;
	}
	
	public double getEntryQuoteY() {
		return entryQuoteY;
	}
	
	public void setEntryQuoteY(double entryQuoteY) {
		this.entryQuoteY = entryQuoteY;
	}
	
	public double getEntryQuote() {
		return entryQuote;
	}
	
	public void setEntryQuote(double entryQuote) {
		this.entryQuote = entryQuote;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
		
	public double getLiveQuoteX() {
		return liveQuoteX;
	}
	
	public void setLiveQuoteX(double liveQuoteX) {
		this.liveQuoteX = liveQuoteX;
	}
	
	public double getLiveQuoteY() {
		return liveQuoteY;
	}
	
	public void setLiveQuoteY(double liveQuoteY) {
		this.liveQuoteY = liveQuoteY;
	}
}
