package com.castaware.nocturne.datatypes;

public enum TransactionType 
{
	RAMP,
	BURN,
	TRANSFER,
	BRIDGE,
	MISC,
	BUY,
	SELL,
	SWAP,
	DUST,
	ADD_LP,
	REMOVE_LP,
	ADD_SP,
	REMOVE_SP,
	POOL_CLAIM,
	POOL_EARN,
	POOL_COMPOUND,
	BUY_NFT,
	MINT_NFT,
	SELL_NFT,
	ADD_GAME,
	REMOVE_GAME, 
	AIRDROP;
	
	/*
	public String getIcon(Transaction event,Wallet wallet) 
	{
		if (!event.getSuccess())
			return "times";
		
		switch (this)
		{
			case RAMP:      return "download";
			case BURN:      return "trash";
			case BRIDGE:
			case TRANSFER:
			{
				if (wallet.getId().equals(event.fromWallet.getId()))
					return  "upload";
				else
					return  "download";
			}
			default:
			{
				return "bookmark";
			}
		}
	}
	
	
	public String getColor(Transaction event,Wallet wallet) 
	{
		if (!event.getSuccess())
			return "tomato";
		
		switch (this)
		{
			case RAMP:   return "chartreuse";
			case BURN:   return "tomato";
			case BRIDGE:
			case TRANSFER:
			{
				if (wallet.getId().equals(event.fromWallet.getId()))
					return  "tomato";
				else
					return  "chartreuse";
			}
			default:
			{
				return "aqua";
			}
		}
	}
	
	public String getTitle(Transaction event,Wallet wallet) 
	{
		StringBuilder builder = new StringBuilder(); 
		
		switch (this)
		{
			case BRIDGE:
			case TRANSFER:
			{
				if (wallet.getId().equals(event.fromWallet.getId()))
					builder.append("WITHDRAW "+this.name());
				else
					builder.append("DEPOSIT "+this.name());
				
				break;
			}
			default:
			{
				builder.append(this.name());				
			}
		}
		
		if(!event.getSuccess())
			builder.append(" (fail)");
		
		return builder.toString();
	}		
	
	public String getDesc(Transaction event,Wallet wallet)
	{
		if (!event.getSuccess())
		{
			if (event.getFeeAsset()!=null)
				return NocNumberFormat.format(event.feeAmount, event.feeAsset);
			else
				return "no costs";
		}
		
		switch (this)
		{
			case SWAP:
			{
				return NocNumberFormat.format(event.fromAmount, event.fromAsset)+" to "+NocNumberFormat.format(event.toAmount, event.toAsset);
			}
			case BRIDGE:
			{
				if (wallet.getId().equals(event.fromWallet.getId()))
					return  NocNumberFormat.format(event.fromAmount, event.fromAsset)+" to "+event.toWallet.network;
				else
					return  NocNumberFormat.format(event.fromAmount, event.fromAsset)+" from "+event.fromWallet.network;
			}
			default:
			{
				if (event.getFromAsset()!=null)
					return NocNumberFormat.format(event.fromAmount, event.fromAsset);
				else if (event.getFromFeeAsset()!=null)
					return NocNumberFormat.format(event.fromFeeAmount, event.fromFeeAsset);
				else
					return "no costs";
			}
		}
	}
	
	public String getTimestamp(Transaction event)
	{
		return NocDateFormat.format_ddMMyyyyHHmm(event.timestamp);		
	}
	*/
}
