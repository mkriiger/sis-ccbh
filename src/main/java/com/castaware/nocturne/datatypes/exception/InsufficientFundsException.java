package com.castaware.nocturne.datatypes.exception;

import java.math.BigDecimal;

@SuppressWarnings("serial")
public class InsufficientFundsException extends RuntimeException 
{
	public String     asset;
	public BigDecimal lackAmount;
	
	public InsufficientFundsException() {
		// TODO Auto-generated constructor stub
	}

	public InsufficientFundsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public InsufficientFundsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public InsufficientFundsException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InsufficientFundsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
