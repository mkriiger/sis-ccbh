package eic.tcc.control.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Menu 
{
	private Map<String,MenuCategory> categoriesMap = new TreeMap<String,MenuCategory>();
	
	public void addMenuItem(String categoryName,String itemName,String url)
	{
		MenuCategory category = categoriesMap.get(categoryName);
		
		if (category == null)
		{
			category = new MenuCategory(categoryName);
			categoriesMap.put(categoryName, category);
		}
		
		category.addItem(itemName, url);			
	}
	
	public List<MenuCategory> getCategories()
	{
		return new ArrayList<MenuCategory>(categoriesMap.values());
	}

}
