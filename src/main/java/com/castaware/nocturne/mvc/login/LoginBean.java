package com.castaware.nocturne.mvc.login;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.castaware.Module;
import com.castaware.nocturne.mvc.security.LoggedRole;
import com.castaware.nocturne.mvc.security.Permission;
import com.castaware.nocturne.mvc.security.SecurityError;

import eic.tcc.control._Bean;
import eic.tcc.control.menu.Menu;

/**
 * @author Rafael Castaneda (rafael.ribeiro@cefet-rj.br)
 * <p>
 * ManagedBean que controla a página 'login.jsf'.
 * <p>
 * Executa as tentativas de autenticação e exibe mensagens 
 * referentes a erros de segurança e credenciais. 
 */
@Controller
@Scope("request")
public class LoginBean extends _Bean
{     
	//
	// Dependências
	//
	@Autowired
	private List<Module> modules;
	
	@Autowired
	private SessionBean sessionBean;
		
	//
	// Campos do Bean
	//
	private String username;    
    private String password;
        
    //
    // Operações do Bean
    //
    public String login() 
    {
    	try
    	{
    		boolean authenticated = false;
    		
    		// Tenta autenticar o usuário em todos os módulos
    		Set<LoggedRole> allRoles = new TreeSet<LoggedRole>();
    		for (Module module : modules) 
    		{
    			List<LoggedRole> moduleRoles = module.doLogin("admin","admin");
    			
    			if (!moduleRoles.isEmpty())
    			{
    				authenticated=true;
    				allRoles.addAll(moduleRoles);    				
    			}    				
			}
    		
    		if (authenticated)
    		{
    			// (1) Configura o nome do usuário no SessionBean
    			sessionBean.setAlias(username);
        		sessionBean.setUsername(username);
        		
        		// (2) Guarda os perfis autenticados no SessionBean
        		for (LoggedRole loggedRole : allRoles) 
        		{
        			sessionBean.addLoggedRole(loggedRole);
        		}
        			
        		// (3) Monta o menu do usuário com base nas permissões dos seus perfis
        		Menu menu = new Menu();
        		
        		for (LoggedRole loggedRole : allRoles)
        		{
        			String roleName = loggedRole.getRole();
        			
	        		for (Module module : modules) 
	        		{
	        			for (Permission permission : module.providePermissions())
	        			{
	        				if (permission.supports(roleName))
	        				{
	        					menu.addMenuItem(permission.getCategory(), permission.getResource(), permission.getUrl());
	        				}	        					        				    		
	        			}	
	        		}
        		}       
        		
        		// (4) Guarda o menu do usuário no SessionBean
        		sessionBean.setMenu(menu);
    		
        		// (5) Redireciona o usuário para a página principal 
            	return "/index.xhtml";
    		}
    		else
    		{
    			popError(SecurityError.INVALID_CREDENTIALS.getDescription());            	
            	return "";
    		}    		
    	}
    	catch(Exception e)
    	{
    		popError("Falha de Login: "+e.getMessage());
    		LOG.error("Falha de Login",e);    		
    		return "";
    	}        
    }
    
    public String logout()
    {
    	getHttpSession().removeAttribute("sessionBean");    	
    	return "/login.xhtml";
    }
    		
    
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
}  