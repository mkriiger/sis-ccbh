package com.castaware;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.castaware.nocturne.mvc.security.LoggedRole;
import com.castaware.nocturne.mvc.security.Permission;

public abstract class Module implements InitializingBean  
{
	protected Logger LOG = LoggerFactory.getLogger(getClass().getSuperclass());
	
	public Module()
	{
		this(true);
	}
	
	public Module(boolean redo)
	{
		if(!redo)
			return;
		
		if (!active())
			return;
		
		String msg = "== Inicializando Módulo "+getName()+" ==";
		String bar = StringUtils.repeat("=", msg.length());
		
		LOG.info(bar);
		LOG.info(msg);
		LOG.info(bar);
		
		for (String role : provideRoles())
		{
			LOG.info("Perfil Adicionado - "+role);
		}
		
		for (Permission permission : providePermissions())
		{
			LOG.info("Regra Adicionada - "+permission.getUrl()+" -> "+permission.getRoles());    			
		}			
	}
	
	@Override
	public void afterPropertiesSet() throws Exception 
	{
		init();
	}	
	
	public abstract String getName();
	public abstract boolean active();
	
	public void init()
	{	
	}
	
	public String[] provideRoles()
	{
		return new String[]{};
	}
	
	public Permission[] providePermissions()
	{
		return new Permission[]{};
	}
	
	public List<LoggedRole> doLogin(String username, String password)
	{
		return new ArrayList<LoggedRole>();
	}	
}
