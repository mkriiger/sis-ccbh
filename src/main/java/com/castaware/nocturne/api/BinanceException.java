package com.castaware.nocturne.api;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */
@SuppressWarnings("serial")
public class BinanceException extends RuntimeException 
{
	public BinanceException() 
	{
		super();
	}

	public BinanceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) 
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BinanceException(String message, Throwable cause) 
	{
		super(message, cause);
	}

	public BinanceException(String message) 
	{
		super(message);	
	}

	public BinanceException(Throwable cause) 
	{
		super(cause);	
	}  
}