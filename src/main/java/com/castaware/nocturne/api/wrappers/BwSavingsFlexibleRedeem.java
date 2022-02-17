package com.castaware.nocturne.api.wrappers;

public class BwSavingsFlexibleRedeem 
{
	public String amount;
	public String asset;
	public String createTime;
	public String principal;
	public String projectId;	
	public String projectName;
	public String status;
	public String type;
	
	@Override
	public String toString() {
		return "BwSavingsFlexibleRedeem [amount=" + amount + ", asset=" + asset + ", createTime=" + createTime
				+ ", principal=" + principal + ", projectId=" + projectId + ", projectName=" + projectName + ", status="
				+ status + ", type=" + type + "]";
	}
}
