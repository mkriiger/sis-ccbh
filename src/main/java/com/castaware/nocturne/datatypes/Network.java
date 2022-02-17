package com.castaware.nocturne.datatypes;

public enum Network 
{
	BINANCE  ("",null,null),
	BSC		 ("BNB","https://bscscan.com/tx/","https://bscscan.com/address/"),
	COSMOS	 ("ATOM","https://www.mintscan.io/cosmos/txs/","https://www.mintscan.io/cosmos/account/"),
	OSMOSIS  ("OSMO","https://www.mintscan.io/osmosis/txs/","https://www.mintscan.io/osmosis/account/"),
	SIFCHAIN ("ROWAN","https://www.mintscan.io/sifchain/txs/","https://www.mintscan.io/sifchain/account/"),
	JUNO	 ("JUNO","https://www.mintscan.io/juno/txs/","https://www.mintscan.io/juno/account/"),
	STARGAZE ("STARS","https://www.mintscan.io/stargaze/txs/","https://www.mintscan.io/stargaze/account/"),
	SECRET	 ("SCRT","https://www.mintscan.io/secret/txs/","https://www.mintscan.io/secret/account/"),
	COMDEX	 ("CMDX","https://www.mintscan.io/comdex/txs/","https://www.mintscan.io/comdex/account/"),
	LUM		 ("LUM","https://www.mintscan.io/lum/txs/","https://www.mintscan.io/lum/account/"),
	SOLANA	 ("SOL","https://explorer.solana.com/tx/","https://explorer.solana.com/address/"),
	ARWEAVE	 ("AR","https://viewblock.io/arweave/tx/","https://viewblock.io/arweave/address/"),
	HARMONY	 ("ONE","https://explorer.harmony.one/tx/","https://explorer.harmony.one/address/"),
	POLYGON	 ("MATIC","https://polygonscan/tx/","https://polygonscan/address/"),
	ETHEREUM ("ETH","https://etherscan.io/tx/","https://etherscan.io/address/"),
	TERRA	 ("LUNA","https://finder.terra.money/mainnet/tx/","https://finder.terra.money/mainnet/address/"),
	FANTOM	 ("FTM","https://explorer.fantom.network/tx/","https://explorer.fantom.network/address/"),
	AVALANCHE("AVAX","https://snowtrace.io/tx/","https://https://snowtrace.io/address/");

	private String  asset;
	private String  txUrl;
	private String  accUrl;
	
	private Network(String asset,String txUrl, String accUrl)
	{
		this.asset=asset;
		this.txUrl=txUrl;
		this.accUrl=accUrl;
	}
		
	public String getTxUrl() {
		return txUrl;
	}
	
	public String getAccUrl() {
		return accUrl;
	}
	
	public String getNativeAsset() {
		return asset;
	}
}
