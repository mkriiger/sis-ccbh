package com.castaware.nocturne.api.wrappers;

public class BwSavingsFlexiblePurchase 
{
	public String amount;
	public String asset;
	public String createTime;
	public int	  purchaseId;
	public String lendingType;
	public String productName;
	public String status;
	
	@Override
	public String toString() {
		return "BwFlexibleSavingsPurchase [amount=" + amount + ", asset=" + asset + ", createTime=" + createTime
				+ ", purchaseId=" + purchaseId + ", lendingType=" + lendingType + ", productName=" + productName
				+ ", status=" + status + "]";
	}	
}
