package com.castaware.nocturne.api.wrappers;

/*
{"symbol":"BNBBTC",
 "id":109611667,
 "orderId":747760648,
 "orderListId":-1,
 "price":"0.00566780",
 "qty":"0.38000000",
 "quoteQty":"0.00215376",
 "commission":"0.00038000",
 "commissionAsset":"BNB",
 "time":1613745063598,
 "isBuyer":true,
 "isMaker":false,
 "isBestMatch":true}
*/
public class BwCriptoTrade 
{
	public String id;
	public String symbol;
	public String orderId;
	public String orderListId;
	public String price;
	public String qty;
	public String quoteQty;
	public String commission;
	public String commissionAsset;
	public String time;
	public String isBuyer;
	public String isMaker;
	public String isBestMatch;
	
	@Override
	public String toString() {
		return "BwCriptoTrade [id=" + id + ", symbol=" + symbol + ", orderId=" + orderId + ", orderListId="
				+ orderListId + ", price=" + price + ", qty=" + qty + ", quoteQty=" + quoteQty + ", commission="
				+ commission + ", commissionAsset=" + commissionAsset + ", time=" + time + ", isBuyer=" + isBuyer
				+ ", isMaker=" + isMaker + ", isBestMatch=" + isBestMatch + "]";
	}
}
