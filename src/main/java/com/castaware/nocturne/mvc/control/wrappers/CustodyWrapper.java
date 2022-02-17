package com.castaware.nocturne.mvc.control.wrappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.castaware.nocturne.datatypes.Custody;

public abstract class CustodyWrapper
{
	private Map<String,Set<Custody>> assetsByGroup = new TreeMap<String,Set<Custody>>();
	
	//
	// Abstract 
	//
	public abstract String getGroup(Custody asset);
	public abstract Set<String> getAllGroups();
	
	//
	// Constructor
	//
	public Collection<String> getGroups()
	{
		Map<Double,String> groupsByValue = new TreeMap<Double,String>();
		
		for(String group : getAllGroups())
		{
			double liveValue = 0;
			
			Set<Custody> set = assetsByGroup.get(group);
			
			if (set!=null)
			{				
				for (Custody custody : set)
				{
					liveValue += custody.getLiveValue();
				}
				
				groupsByValue.put(liveValue, group);
			}
		}		
		
		List<String> groups = new ArrayList<String>(groupsByValue.values());
		Collections.reverse(groups);
		return groups;
	}
	
	public CustodyWrapper(Set<Custody> assets) 
	{
		for (Custody custody : assets) 
		{
			String group = getGroup(custody);
			
			Set<Custody> assetsOfGroup = assetsByGroup.get(group);
			
			if (assetsOfGroup==null)
			{
				assetsOfGroup = new TreeSet<Custody>();
				assetsByGroup.put(group, assetsOfGroup);
			}
			
			assetsOfGroup.add(custody);
		}		
	}
	
	//
	// JSF Accessors
	//
	public Set<Custody> getAssets(String group)
	{
		return assetsByGroup.get(group);				
	}
	
	public double getEntry(String group)
	{
		double result = 0d;
		
		for (Custody custody : assetsByGroup.get(group))
		{
			result += custody.getEntryValue();			
		}
		
		return result;
	}
	
	public double getLive(String group)
	{
		double result = 0d;
		
		for (Custody asset : assetsByGroup.get(group))
		{
			result += asset.getLiveValue();
		}
		
		return result;
	}
	
	public double getRoiPerc(String group)
	{
		double totalROI = getRoi(group);
		double totalEntry = getEntry(group);
		double divide = totalROI/totalEntry;
		double subtract = divide;
		return subtract;
	}
	
	public double getRoi(String group)
	{
		return assetsByGroup.get(group).stream().mapToDouble(a -> a.getROIValue()).sum();
	}	
}
