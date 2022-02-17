package com.castaware.nocturne.api.wrappers;

public class BwCriptoDeposit 
{
	public String amount;
	public String coin;
	public String network;
	public int	  status;
	public String address;
	public String addressTag;
	public String txId;
	public String insertTime;
	public String confirmTimes;
	
	@Override
	public String toString() {
		return "BwCriptoDeposit [amount=" + amount + ", coin=" + coin + ", network=" + network + ", status=" + status
				+ ", address=" + address + ", addressTag=" + addressTag + ", txId=" + txId + ", insertTime="
				+ insertTime + ", confirmTimes=" + confirmTimes + "]";
	}
}
