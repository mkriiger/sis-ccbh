package com.castaware.nocturne.api.wrappers;

public class BwSavingsFixedEarning 
{
	public String asset;
	public String interest;
	public String lendingType;
	public String productName;
	public String time;
	
	@Override
	public String toString() {
		return "BwSavingsFixedEarning [asset=" + asset + ", interest=" + interest + ", lendingType=" + lendingType
				+ ", productName=" + productName + ", time=" + time + "]";
	}
}
