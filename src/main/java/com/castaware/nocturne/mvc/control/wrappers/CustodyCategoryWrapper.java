package com.castaware.nocturne.mvc.control.wrappers;

import java.util.Set;
import java.util.TreeSet;

import com.castaware.nocturne.datatypes.Custody;

public class CustodyCategoryWrapper extends CustodyWrapper
{
	Set<String> groups = new TreeSet<String>();
	
	public CustodyCategoryWrapper(Set<Custody> assets) 
	{
		super(assets);	
		
		for (Custody custody : assets) 
			groups.add(custody.getCategory());
	}

	@Override
	public String getGroup(Custody asset) 
	{
		return asset.getCategory();
	}
		
	@Override
	public Set<String> getAllGroups() 
	{
		return groups;
	}
}
