package com.castaware.nocturne.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

public class NocFormat 
{
	public static DecimalFormat btc = new DecimalFormat("#,000.0000000000");
	public static DecimalFormat brl = new DecimalFormat("0,000.00");
	public static DecimalFormat prc = new DecimalFormat("###.00%");
	public static DateTimeFormatter ddMMyyyyHHmmBar = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	public static DateTimeFormatter yyyyMMddHHmmssHifen = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public static LocalDateTime parse_timestamp(long timestamp)
	{
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp),TimeZone.getDefault().toZoneId());
	}
	
	public static String format_ddMMyyyyHHmm(LocalDateTime value)
	{
		return ddMMyyyyHHmmBar.format(value);		
	}
	
	public static LocalDateTime parse_yyyyyMMddHHmmssHifen(String value)
	{
		return LocalDateTime.parse(value, yyyyMMddHHmmssHifen);
	}
	
	public static String format_yyyyyMMddHHmmssHifen(LocalDateTime value)
	{
		return yyyyMMddHHmmssHifen.format(value);
	}
	
	public static String formatBtcOld(BigDecimal value)
	{
		return btc.format(value);
	}
	
	public static String formatBrlOld(BigDecimal value)
	{
		return brl.format(value);
	}
	
	public static String formatSymbol(String symbol)
	{
		return symbol+(symbol.length()==5?"":(symbol.length()==4?" ":"  "));
	}
	
	public static String formatToken(BigDecimal value, String coin)
	{
		return format(value, coin, false, false);
	}
	
	public static String formatDouble(BigDecimal value, String coin, boolean pretty)
	{
		return format(value, coin, pretty, false);
	}
	
	public static String format(BigDecimal value, String coin, boolean pretty, boolean spaced)
	{
		return btc.format(value)+(!spaced?" "+coin:(" "+coin+(coin.length()==5?"":(coin.length()==4?" ":"  "))));
	}
	
	public static String formatBalance(BigDecimal value, String coin, boolean pretty, boolean spaced)
	{
		if (coin.equals("BRL"))
		{
			if (value.signum()==-1)
				return "-R$"+brl.format(value.abs());
			else
				return "+R$"+brl.format(value);
				
		}
		else 
		{
			if (!spaced)
				if (value.signum()==-1)
					return "-"+btc.format(value.abs())+" "+coin;
				else
					return "+"+btc.format(value)+" "+coin;
				
			else
				if (value.signum()==-1)					
					return "-"+btc.format(value.abs())+(!spaced?" "+coin:(" "+coin+(coin.length()==5?"":(coin.length()==4?" ":"  "))));
				else
					return "+"+btc.format(value)+(!spaced?" "+coin:(" "+coin+(coin.length()==5?"":(coin.length()==4?" ":"  "))));
		}
	}
	
	//
	// ORGANIZED
	//
	private static final String PRC_FORMAT = "%4.0f";
	private static final String BTC_FORMAT = "%8.8f";
	private static final String BRL_FORMAT_BIG = "%9s";
	private static final String BRL_FORMAT_SML = "%8s";
	private static final DecimalFormat CURR_FORMAT = new DecimalFormat("#,##0.00");
	
	private static final BigDecimal HUNDRED = new BigDecimal(100);
	
	public static String formatValue(BigDecimal value)
	{
		return formatValue(value,4,5);
	}
	
	public static String formatValue(BigDecimal value,int inthouses,int dechouses)
	{
		String format = "%"+(inthouses+dechouses+1)+"."+dechouses+"f";
		String string = String.format(format, value);
		return string;
	}
	
	public static String formatCoin(BigDecimal value,String symbol,
								    int inthouses,int dechouses,
								    boolean spaced)
	{
		String format = "%"+(inthouses+dechouses+1)+"."+dechouses+"f";
		String string = String.format(format, value);
		int    length = string.length();
		
		if (length > 8+dechouses)
		{
			String[] split = string.split(",");
			String   ints  = split[0];
			string = ints.substring(0,ints.length()-6)+"M";
			string = StringUtils.repeat(" ",length-string.length()-7)+string; 							
		}
		else if (length > 5+dechouses)
		{
			String[] split = string.split(",");
			String   ints  = split[0];
			string = ints.substring(0,ints.length()-4)+"k";
			string = StringUtils.repeat(" ",length-string.length()-2)+string; 							
		}
		
		if (spaced)
		{
			string += " "+symbol;
			for (int i=0;i<5-symbol.length();i++)
			{
				string+=" ";
			}
		}
		else
		{
			string += " "+(symbol.length()>4?symbol.substring(0, 4):symbol);
			
			if(symbol.length()==2)
				string += "  ";
			else if(symbol.length()==3)
				string += " ";
		}
		
		return string;
	}
	
	
	public static String formatCurrency(BigDecimal value)
	{
		return formatBrl(value,true);
	}
	
	public static String formatBalanceBrl(BigDecimal current,BigDecimal previous)
	{
		return formatCurrency(current.subtract(previous));
	}
	
	public static String formatBrl(BigDecimal value,boolean symbol)
	{
		return formatBrl(value,symbol,false,true);
	}
	
	public static String formatBrl(BigDecimal value,boolean symbol,boolean post,boolean big)
	{
		StringBuilder string = new StringBuilder();
		string.append(symbol&&!post?"R$ ":"");
		string.append(String.format(big?BRL_FORMAT_BIG:BRL_FORMAT_SML, CURR_FORMAT.format(value)));
		string.append(symbol&&post?" R$":"");
		return string.toString();
	}	
	
	public static String formatBalanceBrl(BigDecimal current,BigDecimal previous,boolean coin,boolean post)
	{
		return formatBalanceBrl(current.subtract(previous),coin,post);
	}
	
	public static String formatBalanceBrl(BigDecimal brl,boolean coin,boolean post)
	{
		if (brl.signum()>-1)
			return "+ "+formatBrl(brl,coin,post,false);
		else
			return "- "+formatBrl(brl.abs(),coin,post,false);
	}
	
	public static String formatBtc(BigDecimal btc)
	{
		return formatBtc(btc,true,false);
	}
	
	public static String formatBtc(BigDecimal btc,boolean coin)
	{
		return formatBtc(btc,coin,false);
	}
	
	public static String formatBtc(BigDecimal btc,boolean coin,boolean spaced)
	{
		StringBuilder string = new StringBuilder();
		string.append(String.format(BTC_FORMAT, btc));
		string.append(coin?" BTC":"");
		string.append(spaced?"  ":"");
		return string.toString();
	}
	
	public static String formatBalanceBtc(BigDecimal current, BigDecimal previous,boolean coin,boolean spaced)
	{
		return formatBalanceBtc(current.subtract(previous),coin,spaced);
	}
	
	public static String formatBalanceBtc(BigDecimal btc,boolean coin,boolean spaced)
	{
		if (btc.signum()>-1)
			return "+ "+formatBtc(btc,coin,spaced);
		else
			return "- "+formatBtc(btc.abs(),coin,spaced);
	}
	
	public static String formatPrc(BigDecimal prc)
	{
		return String.format(PRC_FORMAT,prc.multiply(HUNDRED))+"%";
	}
	
	public static String formatBalancePrc(BigDecimal prc)
	{
		if (prc.signum()>-1)
			return "+"+formatPrc(prc);
		else
			return "-"+formatPrc(prc.abs());
	}
	
	public static String formatBalancePrc(BigDecimal current, BigDecimal previous)
	{
		BigDecimal prc;
		
		if (previous.doubleValue()>0)
			prc = current.subtract(previous).divide(previous,20,RoundingMode.HALF_EVEN);
		else
			prc = BigDecimal.ZERO;
		
		return formatBalancePrc(prc);
	}
}
