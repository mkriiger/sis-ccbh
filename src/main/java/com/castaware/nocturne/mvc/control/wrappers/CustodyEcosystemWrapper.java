package com.castaware.nocturne.mvc.control.wrappers;

import java.util.Set;
import java.util.TreeSet;

import com.castaware.nocturne.datatypes.Custody;

public class CustodyEcosystemWrapper extends CustodyWrapper
{
	Set<String> groups = new TreeSet<String>();
	
	public CustodyEcosystemWrapper(Set<Custody> assets) 
	{
		super(assets);	
		
		for (Custody custody : assets) 
			groups.add(custody.getEcosystem());
	}

	@Override
	public String getGroup(Custody asset) 
	{
		return asset.getEcosystem();
	}

	@Override
	public Set<String> getAllGroups() 
	{
		return groups;
	}		
}
