package com.castaware.nocturne.datatypes;

import java.math.BigDecimal;

public abstract class Custody implements Comparable<Custody>
{
	public abstract String getName();
	public abstract Wallet getWallet();
	
	public abstract BigDecimal getAmount();
	public abstract double getEntryValue();
	public abstract double getEntryQuote();
	public abstract double getLiveValue();
	public abstract double getLiveQuote();
	
	public abstract String getCategory();
	public abstract String getEcosystem();
		
	public abstract CustodyType getCustodyType();
	
	public boolean getSpot()
	{
		return getCustodyType()==CustodyType.ASSET;
	}
	
	public boolean getSP()
	{
		return getCustodyType()==CustodyType.SP;
	}
	
	public boolean getLP()
	{
		return getCustodyType()==CustodyType.LP;
	}
	
	public boolean getFiat()
	{
		return getCategory().equalsIgnoreCase("fiat");
	}
	
	public double getROI()
	{
		if(getEntryValue()>0)
			return getLiveValue()/getEntryValue()-1;
		else
			return 0d;
	}
	
	public double getROIValue()
	{
		if (this.getCategory().equals("fiat"))
			return 0;
		
		if(getLiveValue()>0)
			return getLiveValue()-getEntryValue();
		else
			return 0d;
	}
	
	@Override
	public int compareTo(Custody o) 
	{
		int compare = 0;
		
		if (this.getSpot() && this.getCategory().equals("fiat") && !o.getCategory().equals("fiat"))
		{
			return -1;
		}
		else if (this.getSpot() && !this.getCategory().equals("fiat") && o.getCategory().equals("fiat"))
		{
			return +1;
		}
		
		compare = new Integer(this.getCustodyType().ordinal()).compareTo(new Integer(o.getCustodyType().ordinal()));
		
		if (compare == 0)
		{
			compare = this.getName().compareTo(o.getName());
		}
		
		if (compare == 0)
		{
			compare = this.getWallet().getName().compareTo(o.getWallet().getName());
		}
		
		return compare;
	}		
}