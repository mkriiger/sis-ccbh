package com.castaware.nocturne.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

import org.apache.commons.lang3.StringUtils;

public class NocNumberFormat 
{
	private static final String PERC_FORMAT = "%2.0f";
	private static final BigDecimal HUNDRED = new BigDecimal(100);
	
	public static String formatUsd(BigDecimal value)
	{
		return "U$ "+format(value,2);
	}
	
	public static String formatUsd(double value)
	{
		return "U$ "+format(value,2);
	}
	
	public static String formatBrl(BigDecimal value) 
	{
		return "R$ "+format(value,2);
	}
	
	public static String formatBrl(double value) 
	{
		return "R$ "+format(value,2);
	}
	
	public static String format(BigDecimal value)
	{
		int decimalPlaces = determineDecimalPlaces(value);
		return format(value,decimalPlaces);
	}
	
	public static String format(double value)
	{
		int decimalPlaces = determineDecimalPlaces(value);
		return format(value,decimalPlaces);
	}
	
	public static String format(BigDecimal value, String asset)
	{
		return format(value)+" "+asset;
	}
		
	public static String format(double value, String asset)
	{
		return format(value)+" "+asset;
	}
	
	public static String format(BigDecimal value, int decimalPlaces)
	{
		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		
		numberFormat.setMinimumFractionDigits(decimalPlaces);
		numberFormat.setMaximumFractionDigits(decimalPlaces);
		
		return numberFormat.format(value);
	}
	
	public static String format(double value, int decimalPlaces)
	{
		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		
		numberFormat.setMinimumFractionDigits(decimalPlaces);
		numberFormat.setMaximumFractionDigits(decimalPlaces);
		
		return numberFormat.format(value);
	}
	
	public static String format(BigDecimal value, String asset, int decimalPlaces)
	{
		return format(value,decimalPlaces)+" "+asset;
	}
	
	public static String format(double value, String asset, int decimalPlaces)
	{
		return format(value,decimalPlaces)+" "+asset;
	}
	
	public static String formatShr(BigDecimal share, BigDecimal total) 
	{
		BigDecimal prc;
		
		if (share.doubleValue()>0)
			prc = share.divide(total,20,RoundingMode.HALF_EVEN);
		else
			prc = BigDecimal.ZERO;
		
		String prcS = String.format(PERC_FORMAT,prc.multiply(HUNDRED))+"%";
		
		if (prc.multiply(HUNDRED).doubleValue()<10)
			return "0"+prcS.trim();
		else
			return prcS;
	}
	
	public static String formatGainPrc(BigDecimal current, BigDecimal previous)
	{
		BigDecimal prc;
		
		if (previous.doubleValue()>0)
			prc = current.subtract(previous).divide(previous,20,RoundingMode.HALF_EVEN);
		else
			prc = BigDecimal.ZERO;
		
		String prcS = String.format(PERC_FORMAT,prc.multiply(HUNDRED))+"%";
		
		return prcS;
	}
	
	public static String formatAgg(BigDecimal value,int inthouses,int dechouses)
	{
		String format = "%"+(inthouses+dechouses+1)+"."+dechouses+"f";
		String string = String.format(format, value);
		int    length = string.length();
		
		if (length > 8+dechouses)
		{
			String[] split = string.split(",");
			String   ints  = split[0];
			string = ints.substring(0,ints.length()-9)+"M";			 							
		}
		else if (length > 5+dechouses)
		{
			String[] split = string.split(",");
			String   ints  = split[0];
			string = ints.substring(0,ints.length()-4)+"k";			 						
		}
		
		return string;
	}
	
	private static int determineDecimalPlaces(double value)
	{
		return determineDecimalPlaces(new BigDecimal(value));
	}
	
	private static int determineDecimalPlaces(BigDecimal value)
	{
		if(value.doubleValue()==0)
			return 0;
		
		String string = value.toPlainString();
		
		if (string.contains("."))
		{
			String[] split = string.split("\\.");
			String decimals = StringUtils.trim(split[1]);
			
			int totalDecimals = 0;
			boolean allZeroes = true;
			
			for (int i=0;i<decimals.length();i++)
			{
				totalDecimals++;
				
				if (decimals.charAt(i)!='0')
				{
					allZeroes = false;
					break;
				}
			}
			
			if (allZeroes)
				return 0;
			else
				return totalDecimals+1;
		}
		else
		{
			return 0;
		}		
	}
}
