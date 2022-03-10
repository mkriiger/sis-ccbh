package eic.tcc.control;

import java.io.IOException;
import java.util.Locale;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rafael Castaneda (rafael.ribeiro@cefet-rj.br)
 *  
 * Classe abstrata que fornece métodos utilitários 
 * para todos os ManagedBeans do sistema.
 */
public abstract class _Bean 
{
	/**
	 * Define um Logger para ser herdado por cada Bean específico 
	 */
	protected Logger LOG = LoggerFactory.getLogger(getClass());
			
	/**
	 * Constrói o Bean forçando o Locale para Português (Brasil)
	 */
	public _Bean() 
	{
		Locale.setDefault(new Locale("pt","BR"));
	}
	
	/**
	 * Detecta operações de Refresh na página. Requer a seguinte tag na página:
	 * <p>
	 * <f:event listener="#{sessionBean.preRender()}" type="preRenderView" />
	 * <p>
	 * Possibilita ainda às páginas apresentarem mensagens de pop-up no momento
     * de sua renderização após um redirecionamento advindo de Servlets ou 
     * Filtros. Para que a mensagem seja exibida, o Filtro ou Servlet deve
     * executar o seguinte código: 
     * <p>
     * request.getSession().setAttribute("preRenderMessage","MESSAGE");
	 * response.sendRedirect(request.getContextPath()+"/[DESTINO].jsf");
	 * <p>
	 */
	String previousPage;
	public void preRender() 
	{
		boolean ajaxRequest = FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest();
		
		if(ajaxRequest)
			return;
		
	    UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
	    String id = viewRoot.getViewId();
	    
	    if (previousPage==null)
	    {
	    	onPageLoad();		
	    }
	    else if (previousPage.equals(id)) 
	    {	    	
			onPageRefresh();			
	    }
	 
	    previousPage = id;
	    
	    Object preRenderMessage = getHttpSession().getAttribute("preRenderMessage");
    	
    	if (preRenderMessage != null)
    	{
    		popWarning((String)preRenderMessage);    		
    		getHttpSession().removeAttribute("preRenderMessage");
    	}
	}
	
	/**
	 * Métodos para serem sobrecarregados a fim de realizar operações no carregamento da página
	 */
	public void onPageLoad() {};
	public void onPageRefresh() {};	
	
	/**
	 * Força a atualização de um formulário pelo back-end 
	 */
	public void updateForm(String formId)
	{
		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add(formId);
	}
	
	/**
	 * Força a atualização de um componente pelo back-end 
	 */
	public void updateComponent(String componentId)
	{
		PrimeFaces.current().ajax().update(componentId);
	}
	
	/**
	 * Recupera o context path da aplicação
	 * <p>
	 * @return objeto HttpServletRequest
	 */
	public String getContextPath()
	{
		return FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath();
	}
	
	/**
	 * Recupera o objeto da requisição HTTP
	 * <p>
	 * @return objeto HttpServletRequest
	 */
	public HttpServletRequest getHttpServletRequest()
	{
		return (HttpServletRequest)(FacesContext.getCurrentInstance().getExternalContext().getRequest());
	}
	
	/**
	 * Recupera o objeto de resposta HTTP
	 * <p>
	 * @return objeto HttpServletResponse
	 */
	public HttpServletResponse getHttpServletResponse() 
	{
		return (HttpServletResponse)(FacesContext.getCurrentInstance().getExternalContext().getResponse());
	}
	
	/**
	 * Recupera o objeto de sessão HTTP
	 * <p>
	 * @return objeto HttpSession
	 */
	public HttpSession getHttpSession()
	{
		return getHttpServletRequest().getSession();
	}
	
	/**
	 * Recupera qualquer outro bean que esteja disponível no contexto da aplicação
	 * <p>
	 * @return objeto genérico do bean
	 */
	public<TYPE> TYPE getBean(String beanName,Class<TYPE> beanClass)
	{
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		TYPE bean = (TYPE)application.evaluateExpressionGet(context, "#{"+beanName+"}", beanClass);		
		return bean;
	}
			
	/**
	 * Método utilitário para apresentar mensagens Pop-Up de nível informativo
	 * <p>
	 * @param message Mensagem informativa
	 */
	public void popInfo(String message)
	{
		FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, message, "");
		FacesContext.getCurrentInstance().addMessage(null, fm);
	}
	
	/**
	 * Método utilitário para apresentar mensagens Pop-Up de nível de alerta
	 * <p>
	 * @param message Mensagem de alerta
	 */
	public void popWarning(String message)
	{
		FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_WARN, message, "");
		FacesContext.getCurrentInstance().addMessage(null, fm);
	}
	
	/**
	 * Método utilitário para apresentar mensagens Pop-Up de nível de erro
	 * <p>
	 * @param message Mensagem de erro
	 */
	public void popError(String message)
	{
		FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "");
		FacesContext.getCurrentInstance().addMessage(null, fm);
	}
	
	/**
	 * Busca um componente da tela JSF que invocou o Bean pelo seu ID
	 * <p>
	 * @param componentID Mensagem de erro
	 */
	public UIComponent findUIComponent(String componentID)
	{
		return FacesContext.getCurrentInstance().getViewRoot().findComponent(componentID);
	}
	
	/**
	 * Método utilitário que reseta uma DataTable JSF para suas configurações 
	 * padrões pelo ID.
	 * <p>
	 * @param componentId ID da DataTable na página JSF.
	 */
	public void resetDataTableUI(String componentId)
	{
		DataTable dataTable = (DataTable)findUIComponent(componentId);
//		dataTable.setValueExpression("sortBy", dataTable.getDefaultSortByVE());
//		dataTable.setSortOrder(dataTable.getDefaultSortOrder());
		dataTable.setFirst(0);
	}
	
	/**
	 * Cria programaticamente uma ValueExpression do tipo #{EXPRESSION} 
	 * <p>
	 * @param expression O conteúdo da expressão
	 * @param expectedType O tipo esperado pela resolução da expressão 
	 * @return O objeto ValueExpression construído
	 */
	public ValueExpression valueExpression(String expression, Class<?> expectedType) 
	{
	    FacesContext context = FacesContext.getCurrentInstance();
	    return context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), expression, expectedType);
	}
	
	/**
	 * Realiza o tratamento mínimo para uma exceção que chega até a camada 
	 * de visão. Que consiste em adicionar uma entrada ao LOG do sistema e
	 * apresentar uma mensagem de popUp 
	 * <p>
	 * @param exception A exceção capturada
	 */
	public void handleError(Exception exception)
	{
		popError("ERRO: "+exception.getMessage());
		LOG.error("ERRO: "+exception,exception);
	}
	
	/**
	 * Apresenta um diálogo de pop-up 
	 * <p>
	 * @param dialog O nome do diálogo de acordo com o atributo "widgetVar"
	 */
	public void showDialog(String dialog)
	{
		PrimeFaces.current().executeScript("PF('"+dialog+"').show()");
	}
	
	/**
	 * Esconde um diálogo de pop-up 
	 * <p>
	 * @param dialog O nome do diálogo de acordo com o atributo "widgetVar"
	 */
	public void hideDialog(String dialog)
	{
		PrimeFaces.current().executeScript("PF('"+dialog+"').hide()");
	}
	
	/**
	 * Redireciona a requisição para uma nova página
	 * <p>
	 * @return objeto HttpServletResponse
	 */
	public void redirect(String url) 
	{
		try 
		{
			FacesContext.getCurrentInstance().getExternalContext().redirect(url);
		}
		catch (IOException e) 
		{
			handleError(e);
		}
	}
}
