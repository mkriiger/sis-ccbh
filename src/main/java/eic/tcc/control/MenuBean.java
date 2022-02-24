package eic.tcc.control;

import java.util.Map;
import java.util.TreeMap;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("session")
public class MenuBean extends _Bean
{
	private MenuModel menuModel = new DefaultMenuModel(); 
	private Map<String,DefaultSubMenu> menus = new TreeMap<String,DefaultSubMenu>();
	
	public MenuBean()
	{
		addMenuItem("Menu","Início","index.jsf");
		addMenuItem("Menu","Sobre","index.jsf");
	}
	
	private void addMenuItem(String subMenuName, String menuItemName, String URL)
	{
		DefaultSubMenu subMenu = menus.get(subMenuName);
		
		if (subMenu==null)
		{
			subMenu = DefaultSubMenu.builder().label(subMenuName).build();
			menuModel.getElements().add(subMenu);
			menus.put(subMenuName, subMenu);
		}
		
		DefaultMenuItem menuItem = DefaultMenuItem.builder()
												  .value(menuItemName)
												  .url(URL+"?refresh=true").build();
				
		subMenu.getElements().add(menuItem);
	}
	
	
	
	public MenuModel getMenu() 
	{
		return menuModel;
	}
	
	public String getHello()
	{
		return "hello";
	}
}
