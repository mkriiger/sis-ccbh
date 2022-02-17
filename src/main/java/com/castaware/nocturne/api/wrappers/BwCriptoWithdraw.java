package com.castaware.nocturne.api.wrappers;

public class BwCriptoWithdraw 
{
	public String address;
	public String amount;
	public String applyTime;
	public String coin;
	public String id;
	public String withdrawOrderId;
	public String network;
	public String transferType;
	public int	  status;
	public String transactionFee;
	public String txId;
	
	@Override
	public String toString() {
		return "BwCriptoWithdraw [address=" + address + ", amount=" + amount + ", applyTime=" + applyTime + ", coin="
				+ coin + ", id=" + id + ", withdrawOrderId=" + withdrawOrderId + ", network=" + network
				+ ", transferType=" + transferType + ", status=" + status + ", transactionFee=" + transactionFee
				+ ", txId=" + txId + "]";
	}
}
