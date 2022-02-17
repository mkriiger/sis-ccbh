package com.castaware.nocturne.mvc.converters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.springframework.stereotype.Component;

@Component
@FacesConverter("localDateTime")
public class LocalDateTimeConverter implements Converter 
{
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String stringValue) 
	{
		if (null == stringValue || stringValue.isEmpty()) 
		{
			return null;
		}

		LocalDateTime localDateTime;

		try 
		{
			localDateTime = LocalDateTime.parse(stringValue,DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
		}
		catch (DateTimeParseException e) 
		{
			throw new ConverterException("Could not convert "+stringValue+" check for format dd/MM/yyyy HH:mm:ss");
		}

		return localDateTime;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object localDateTimeValue) 
	{
		if (null == localDateTimeValue) 
		{
			return "";
		}

		return ((LocalDateTime) localDateTimeValue).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
	}
}