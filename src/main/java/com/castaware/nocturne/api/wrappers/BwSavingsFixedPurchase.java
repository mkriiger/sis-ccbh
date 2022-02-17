package com.castaware.nocturne.api.wrappers;

public class BwSavingsFixedPurchase 
{
	public String amount;
	public String asset;
	public String createTime;
	public int	  purchaseId;
	public int	  lot;
	public String lendingType;
	public String productName;
	public String status;
	
	@Override
	public String toString() {
		return "BwFixedSavingsPurchase [amount=" + amount + ", asset=" + asset + ", createTime=" + createTime
				+ ", purchaseId=" + purchaseId + ", lendingType=" + lendingType + ", productName=" + productName
				+ ", status=" + status + "]";
	}	
}
