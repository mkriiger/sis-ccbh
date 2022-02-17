package com.castaware.nocturne.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

public class NocDateFormat 
{
	public static DateTimeFormatter ddMM_Bar = DateTimeFormatter.ofPattern("dd/MM");
	public static DateTimeFormatter ddMMyy_Bar = DateTimeFormatter.ofPattern("dd/MM/yy");
	public static DateTimeFormatter ddMMyyyy_Bar = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	public static DateTimeFormatter ddMMyyyyHHmm_Bar = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	public static DateTimeFormatter yyyyMMddHHmmss_Hifen = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public static long daysSince(LocalDateTime since)
	{
		LocalDateTime now = LocalDateTime.now();
		long days = ChronoUnit.DAYS.between(since, now);
		return days;
	}
	
	public static long minutesSince(LocalDateTime since)
	{
		LocalDateTime now = LocalDateTime.now();
		long days = ChronoUnit.MINUTES.between(since, now);
		return days;
	}
	
	public static LocalDateTime toStartOfDay(LocalDateTime from)
	{
		from = from.minus(from.getHour(), ChronoUnit.HOURS);
		from = from.minus(from.getSecond(), ChronoUnit.SECONDS);
		from = from.minus(from.getMinute(), ChronoUnit.MINUTES);
		return from;
	}
	
	public static LocalDateTime toStartOfMonth(LocalDateTime from)
	{
		from = from.minus(from.getDayOfMonth(), ChronoUnit.DAYS);
		from = from.minus(from.getHour(), ChronoUnit.HOURS);
		from = from.minus(from.getSecond(), ChronoUnit.SECONDS);
		from = from.minus(from.getMinute(), ChronoUnit.MINUTES);
		return from;
	}
	
	public static LocalDateTime toEndOfMonth(LocalDateTime from) 
	{
		YearMonth 		yearMonth  = YearMonth.of(from.getYear(),from.getMonth());
		LocalDate 		endOfMonth = yearMonth.atEndOfMonth();
		LocalDateTime	result	   = endOfMonth.atStartOfDay().plus(1,ChronoUnit.DAYS).minus(1,ChronoUnit.SECONDS);
		return result;
	}
	
	public static LocalDateTime toEndOfDay(LocalDateTime from)
	{
		from = from.plus(59-from.getSecond(), ChronoUnit.SECONDS);
		from = from.plus(59-from.getMinute(), ChronoUnit.MINUTES);
		from = from.plus(23-from.getHour(), ChronoUnit.HOURS);
		return from;	
	}
	
	public static LocalDateTime fetchDay(LocalDateTime from)
	{
		return LocalDateTime.of(from.getYear(), from.getMonth(),from.getDayOfMonth(),0,0);
	}
	
	public static LocalDateTime fetchMonth(LocalDateTime from)
	{
		return LocalDateTime.of(from.getYear(), from.getMonth(),0,0,0);
	}
	
	public static LocalDateTime parse_timestamp(long timestamp)
	{
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp),TimeZone.getTimeZone("GMT").toZoneId());
	}
	
	public static LocalDateTime parse_ddMMyyyy(String value)
	{
		return LocalDateTime.parse(value, ddMMyyyy_Bar);
	}
	
	public static LocalDateTime parse_ddMMyyyyHHmm(String value)
	{
		return LocalDateTime.parse(value, ddMMyyyyHHmm_Bar);
	}
	
	public static LocalDateTime parse_yyyyMMddHifen(String value)
	{
		return LocalDateTime.parse(value+" 00:00:00", yyyyMMddHHmmss_Hifen);
	}	
	
	public static LocalDateTime parse_yyyyMMddHHmmssHifen(String value)
	{
		return LocalDateTime.parse(value, yyyyMMddHHmmss_Hifen);
	}	
		
	public static long format_timestamp(LocalDateTime value)
	{
		return value.atZone(TimeZone.getTimeZone("GMT").toZoneId()).toInstant().toEpochMilli();
	}
	
	public static String format_ddMM(LocalDate value)
	{
		return ddMM_Bar.format(value);		
	}
	
	public static String format_ddMM(LocalDateTime value)
	{
		return ddMM_Bar.format(value);		
	}
	
	public static String format_ddMMyy(LocalDate value)
	{
		return ddMMyy_Bar.format(value);		
	}
	
	public static String format_ddMMyy(LocalDateTime value)
	{
		return ddMMyy_Bar.format(value);		
	}
	
	public static String format_ddMMyyyy(LocalDate value)
	{
		return ddMMyyyy_Bar.format(value);		
	}
	
	public static String format_ddMMyyyy(LocalDateTime value)
	{
		return ddMMyyyy_Bar.format(value);		
	}
	
	public static String format_ddMMyyyyHHmm(LocalDateTime value)
	{
		return ddMMyyyyHHmm_Bar.format(value);		
	}
	
	public static String format_yyyyMMddHifen(LocalDateTime value)
	{
		return yyyyMMddHHmmss_Hifen.format(value).split(" ")[0];
	}
	
	public static String format_yyyyMMddHHmmssHifen(LocalDateTime value)
	{
		return yyyyMMddHHmmss_Hifen.format(value);
	}
}
