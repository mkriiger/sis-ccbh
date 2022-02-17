package com.castaware.nocturne.util;

import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NocLogger 
{
	private Logger logger;
	private Level  level;
	private StringBuilder builder;
	
	public NocLogger(Level level)
	{
		this.level=level;
		this.logger=LoggerFactory.getLogger(getClass());
		this.builder=new StringBuilder();
	}
	
	public void error(String message)
	{
		logger.error(message);
		builder.append(message+"\n");
	}
	
	public void warn(String message)
	{
		if (level.isGreaterOrEqual(Level.ERROR))
			return;
		
		logger.info(message);
		builder.append(message+"\n");
	}
	
	public void info(String message)
	{
		if (level.isGreaterOrEqual(Level.WARN))
			return;
		
		logger.info(message);
		builder.append(message+"\n");
	}
	
	public void debug(String message)
	{
		if (level.isGreaterOrEqual(Level.INFO))
			return;
		
		logger.info(message);
		builder.append(message+"\n");
	}
	
	public void trace(String message)
	{
		if (level.isGreaterOrEqual(Level.INFO))
			return;
		
		logger.info(message);
		builder.append(message+"\n");		
	}
	
	public String getLog() {
		return builder.toString();
	}

	public void clear() {
		builder = new StringBuilder();		
	}	
}
