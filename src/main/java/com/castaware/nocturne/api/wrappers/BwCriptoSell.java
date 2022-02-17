package com.castaware.nocturne.api.wrappers;

public class BwCriptoSell 
{
	public String orderNo;
	public String sourceAmount;
	public String fiatCurrency;
	public String obtainAmount;
	public String cryptoCurrency;
	public String totalFee;
	public String price;
	public String status;
	public String createTime;
	public String updateTime;
	
	@Override
	public String toString() {
		return "BwCriptoBuy [orderNo=" + orderNo + ", sourceAmount=" + sourceAmount + ", fiatCurrency=" + fiatCurrency
				+ ", obtainAmount=" + obtainAmount + ", cryptoCurrency=" + cryptoCurrency + ", totalFee=" + totalFee
				+ ", price=" + price + ", status=" + status + ", createTime=" + createTime + ", updateTime="
				+ updateTime + "]";
	}
}
