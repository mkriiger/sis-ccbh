package com.litesoftwares.coingecko;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinGeckoApiError 
{
    private int code;
    private String message;
    
    public int getCode() {
		return code;
	}
    
    public void setCode(int code) {
		this.code = code;
	}
    
    public String getMessage() {
		return message;
	}
    
    public void setMessage(String message) {
		this.message = message;
	}
}
