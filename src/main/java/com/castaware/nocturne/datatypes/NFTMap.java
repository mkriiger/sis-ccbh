package com.castaware.nocturne.datatypes;

import java.util.TreeMap;

@SuppressWarnings("serial")
public class NFTMap extends TreeMap<String,NFT>
{
	public NFT getNFT(String name)
	{
		NFT nft = this.get(name);
		
		if (nft==null)
		{
			nft = new NFT(name);
			put(name,nft);
		}
		
		return nft;			
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("###### NFT's ######");
		for(NFT nft : values())
		{
			builder.append("\n# "+nft);
		}
		builder.append("\n#");
		
		return builder.toString();
	}
}
