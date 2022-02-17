package com.castaware.nocturne.api.wrappers;

public class BwSavingsFixedRedeem 
{
	public String amount;
	public String asset;
	public String createTime;
	public String interest;
	public String principal;
	public String projectId;
	public String projectName;
	public String starTime;
	public String status;
	
	@Override
	public String toString() {
		return "BwSavingsFixedRedeem [amount=" + amount + ", asset=" + asset + ", createTime=" + createTime
				+ ", interest=" + interest + ", principal=" + principal + ", projectId=" + projectId + ", projectName="
				+ projectName + ", starTime=" + starTime + ", status=" + status + "]";
	}	
}
