package eic.tcc.control.menu;

import java.util.ArrayList;
import java.util.List;

public class MenuCategory 
{
	private String name;
	private List<MenuItem> items;
	
	public MenuCategory(String name)
	{
		this.name = name;
		this.items = new ArrayList<MenuItem>();
	}
	
	//
	// Operações
	//
	public void addItem(String itemName, String url)
	{
		for (MenuItem sessionMenuItem : items) 
		{
			if (sessionMenuItem.getName().equals(itemName))
			{
				if (!sessionMenuItem.getUrl().equals(url))
				{
					throw new IllegalArgumentException("Duplicate name for SessionMenuItem "+itemName+" with two differents URL's: (1) "+url+" and (2) "+sessionMenuItem.getUrl());
				}					
			}				
		}
		
		this.items.add(new MenuItem(itemName, url));
	}
	
	//
	// Métodos de Acesso
	//
	public String getName() 
	{
		return name;
	}
	
	public List<MenuItem> getItems() {
		return items;
	}
	
}
