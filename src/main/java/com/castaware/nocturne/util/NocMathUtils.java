package com.castaware.nocturne.util;

import java.math.BigDecimal;

import com.castaware.nocturne.datatypes.exception.InsufficientFundsException;

public class NocMathUtils 
{
	public static BigDecimal sub(String asset, BigDecimal amount,BigDecimal subAmount)
	{
		BigDecimal balance = amount.subtract(subAmount);
		
		if (balance.doubleValue()<0)
		{
			throw new IllegalStateException("Insufficient funds in "+asset+" has "+amount+" for "+subAmount+". Lacks "+subAmount.subtract(amount));
		}
		
		return balance;
	}
	
	public static BigDecimal sub(String asset, BigDecimal amount,BigDecimal subAmount,double entryQuote)
	{
		return sub(asset,amount,subAmount,new BigDecimal(entryQuote));
	}
	
	public static BigDecimal sub(String asset, BigDecimal amount,BigDecimal subAmount,BigDecimal entryQuote)
	{
		if (amount.doubleValue()==0)
		{
			InsufficientFundsException ex = new InsufficientFundsException("Insufficient funds for "+asset+" has "+amount+" for "+subAmount+". Lacks "+subAmount.subtract(amount));
			ex.asset=asset;
			ex.lackAmount=subAmount;
			throw ex;
		}
			
		BigDecimal balance = amount.subtract(subAmount);
		BigDecimal balanceValue = balance.multiply(entryQuote);
		String 	   balanceValueString = balanceValue.toString();
		double     balanceValueDouble = balanceValue.doubleValue();
		
		// Corrige valores irrisórios
		if (balanceValue.doubleValue() < 0.00001)
		{
			balance = BigDecimal.ZERO;
		}		
		else if (balanceValueString.contains("E-"))
		{
			String[] balanceSplit  = balanceValueString.split("-");
			String   factorString  = balanceValueDouble<0?balanceSplit[2]:balanceSplit[1];
			int      factor		   = Integer.parseInt(factorString);
				
			if (factor>=8)
				balance = BigDecimal.ZERO;
			else						
			{
				InsufficientFundsException ex = new InsufficientFundsException("Insufficient funds for "+asset+" has "+amount+" for "+subAmount+". Lacks "+subAmount.subtract(amount));
				ex.asset=asset;
				ex.lackAmount=balance;
				throw ex;
			}
		}
		else if (balanceValue.doubleValue()<0)
		{
			InsufficientFundsException ex = new InsufficientFundsException("Insufficient funds for "+asset+" has "+amount+" for "+subAmount+". Lacks "+subAmount.subtract(amount));
			ex.asset=asset;
			ex.lackAmount=balance;
			throw ex;
		}
		
		return balance;
	}

}
