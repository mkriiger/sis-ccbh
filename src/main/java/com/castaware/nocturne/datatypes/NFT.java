package com.castaware.nocturne.datatypes;

public class NFT
{
	private String id;
	private String name;
	
	public NFT(String name)
	{
		if (name == null || name.isEmpty()) 
			throw new IllegalArgumentException("NFT name cannot be null or empty");
		
		this.name=name;
	}
	
	@Override
	public String toString() 
	{
		return String.format("[NFT] %s (%s)",name,id);		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
}