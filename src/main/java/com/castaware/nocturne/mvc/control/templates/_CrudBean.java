package com.castaware.nocturne.mvc.control.templates;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.castaware.nocturne.util.NocDateFormat;

import eic.tcc.control._Bean;
import eic.tcc.dao.Dao;

public abstract class _CrudBean<DOMAIN_TYPE> extends _Bean 
{	
	//
	// Dependências
	//
	@Autowired
	protected Dao dao;
	
	//
	// Atributos da Página Crud
	//
	enum Operation{NEW,EDIT,VIEW}
	
	protected DOMAIN_TYPE object;
	protected boolean readonly;
	protected boolean rendered;
	protected Operation operation;
	protected String filter;
	protected String dialogName;
	protected Class<DOMAIN_TYPE> classType;
	
	protected List<DOMAIN_TYPE> allObjects;
	protected List<DOMAIN_TYPE> filteredObjects;
	
	protected Comparator<DOMAIN_TYPE> comparator;
	
	//
	// Construtor
	//
	public _CrudBean(Class<DOMAIN_TYPE> classType,Comparator<DOMAIN_TYPE> comparator)
	{
		this.classType = classType;
		this.dialogName = "newEditDialog";
		this.allObjects = new ArrayList<DOMAIN_TYPE>();
		this.filteredObjects = new ArrayList<DOMAIN_TYPE>();
		this.comparator = comparator;
		this.operation = Operation.NEW;
	}
	
	public _CrudBean(Class<DOMAIN_TYPE> classType)
	{
		this.classType = classType;
		this.dialogName = "newEditDialog";
		this.allObjects = new ArrayList<DOMAIN_TYPE>();
		this.filteredObjects = new ArrayList<DOMAIN_TYPE>();
		this.operation = Operation.NEW;
	}
	
	//
	// Operações da Página
	//
	public abstract String[] filterFields();	
		
	@Override	
	public void onPageLoad() 
	{
		onPageRefresh();	
	}

	@Override
	public void onPageRefresh() 
	{
		try
		{
			object=newObject();
			
			filter="";
			allObjects.clear();
			filteredObjects.clear();
			loadObjects(allObjects);
			filteredObjects=allObjects;
		}
		catch(Exception e)
		{
			handleError(e);
		}		
	}	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onFilter()
	{
		if (filter==null||filter.isEmpty())
		{
			filteredObjects=allObjects;
			return;
		}
				
		try
		{
			filteredObjects = new ArrayList<DOMAIN_TYPE>();
			
			for(DOMAIN_TYPE obj : allObjects)
			{
				for(String fieldName : filterFields())
				{
					String[] subFieldsNames = fieldName.split("\\.");
					
					String finalFieldValue = null;
										
					Object auxFieldObject  = obj;
					Class  auxFieldClass   = obj.getClass();
					
					for (int i=0;i<subFieldsNames.length;i++) 
					{
						String subFieldName = subFieldsNames[i];
						
						Method getMethod = auxFieldClass.getMethod("get"+StringUtils.capitalize(subFieldName));
						auxFieldObject = getMethod.invoke(auxFieldObject);
						
						if (auxFieldObject==null)
							break;
						
						auxFieldClass = auxFieldObject.getClass();
						
						if (i==subFieldsNames.length-1)
						{
							if (auxFieldClass.isEnum())
							{
								Method toString = auxFieldClass.getMethod("toString");
								finalFieldValue = (String)toString.invoke(auxFieldObject);
							}
							else if (auxFieldClass == LocalDate.class)
							{
								LocalDate localDate = (LocalDate)auxFieldObject;
								finalFieldValue = NocDateFormat.format_ddMMyyyy(localDate);
							}
							else if (auxFieldClass == LocalDateTime.class)
							{
								LocalDateTime localDate = (LocalDateTime)auxFieldObject;
								finalFieldValue = NocDateFormat.format_ddMMyyyy(localDate);
							}
							else
							{
								Method toString = auxFieldClass.getMethod("toString");
								finalFieldValue = (String)toString.invoke(auxFieldObject);
							}
						}
					}
					
					if (finalFieldValue!=null && finalFieldValue.startsWith(filter))
					{					
						filteredObjects.add(obj);
						break;
					}
				}
			}
		}
		catch(Exception e)
		{
			LOG.error("Error while filtering objects");
			LOG.error(e.getMessage());
		}
	}
	
	public DOMAIN_TYPE newObject() throws Exception
	{
		return classType.newInstance();
	}
	
	public void loadObjects(List<DOMAIN_TYPE> objects)
	{
		objects.addAll(dao.retrieveAll(classType));
		
		if (comparator!=null)
			objects.sort(comparator);		
	}
	
	public void create()
	{
		try
		{
			this.object = classType.newInstance();
			this.operation = Operation.NEW;
			this.readonly = false;
			this.rendered = true;
			showDialog(this.dialogName);
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}
	
	public void update(DOMAIN_TYPE t) 
	{
		try
		{
			this.operation = Operation.EDIT;
			this.readonly = false;
			this.rendered = true;
			this.object = t; 
			showDialog(this.dialogName);
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}
	
	public void select(DOMAIN_TYPE t)
	{
		try
		{
			this.operation = Operation.VIEW;
			this.readonly = true;
			this.rendered = false;
			this.object = t;
			showDialog(this.dialogName);
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}
	
	public void persist()
	{
		try
		{
			dao.persist(object);
			hideDialog(this.dialogName);
			popInfo("Saved successfully");
			onPageLoad();
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}
	
	public void delete(DOMAIN_TYPE t)
	{
		try
		{
			dao.delete(t);
			popInfo("Removed successfully");
			onPageLoad();
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}
			
	//
	// Métodos de Acesso 
	//		
	public DOMAIN_TYPE getObject() {
		return object;
	}

	public void setObject(DOMAIN_TYPE object) {
		this.object = object;
	}

	public List<DOMAIN_TYPE> getAllObjects() {
		return allObjects;
	}

	public void setAllObjects(List<DOMAIN_TYPE> allObjects) {
		this.allObjects = allObjects;
	}
	
	public List<DOMAIN_TYPE> getFilteredObjects() {
		return filteredObjects;
	}

	public void setFilteredObjects(List<DOMAIN_TYPE> filteredObjects) {
		this.filteredObjects = filteredObjects;
	}
	
	public String getDialogName() {
		return dialogName;
	}
	
	public String getFilter() {
		return filter;
	}
	
	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	public boolean isReadonly() 
	{
		return this.readonly;
	}
	
	public boolean isRendered() 
	{
		return this.rendered;
	}
	
	public String getOperation()
	{
		return this.operation.toString();
	}	
}
