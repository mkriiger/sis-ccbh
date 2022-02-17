package com.castaware.nocturne.datatypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Claim extends Claimable
{
	private LocalDateTime timestamp;
	
	public Claim()
	{
		
	}
	
	public Claim(String asset, BigDecimal amount, BigDecimal quote, LocalDateTime timestamp) 
	{
		super(asset,amount,quote);		
		this.timestamp=timestamp;
	}
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}
