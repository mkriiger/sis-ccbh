package com.castaware.nocturne;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.ComponentScan;

import com.castaware.Module;
import com.castaware.nocturne.mvc.security.LoggedRole;
import com.castaware.nocturne.mvc.security.Permission;

@ComponentScan("com.castaware.nocturne")
public class NocturneModule extends Module 
{
	public NocturneModule()
	{
		
	}
	
	public NocturneModule(boolean redo) 
	{
		super(redo);
	}
	
	@Override
	public boolean active() 
	{
		return true;
	}
	
	@Override
	public String getName() 
	{
		return "NOCTURNE";
	}
	
	@Override
	public String[] provideRoles() 
	{
		return new String[] {"ADMIN"};		
	}

	@Override
	public Permission[] providePermissions() 
	{
		return new Permission[] 
		{
//			new Permission("Data","Wallets","/wallet.jsf","ADMIN"),
//			new Permission("Events","Wallet Events","/event_wallet.jsf","ADMIN"),
//			new Permission("Events","Trade Events","/event_trade.jsf","ADMIN"),
//			new Permission("Events","Pool Events","/event_pool.jsf","ADMIN"),
//			new Permission("Trading","Boxes","/boxes.jsf","ADMIN")
		};
	}
	
	@Override
	public List<LoggedRole> doLogin(String username, String password) 
	{
		List<LoggedRole> loggedRoles = new ArrayList<LoggedRole>();
		
		if (username.equals("admin"))
		{			
			loggedRoles.add(new LoggedRole("ADMIN",null));
		}
			
		return loggedRoles;
	}	
}
