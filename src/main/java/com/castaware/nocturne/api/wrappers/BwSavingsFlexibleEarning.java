package com.castaware.nocturne.api.wrappers;

public class BwSavingsFlexibleEarning 
{
	public String asset;
	public String interest;
	public String lendingType;
	public String productName;
	public String time;
	
	@Override
	public String toString() {
		return "BwSavingsFlexibleEarning [asset=" + asset + ", interest=" + interest + ", lendingType=" + lendingType
				+ ", productName=" + productName + ", time=" + time + "]";
	}
}
