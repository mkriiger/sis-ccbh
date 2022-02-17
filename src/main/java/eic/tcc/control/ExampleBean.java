package eic.tcc.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import eic.tcc.dao.Dao;
 
@Controller(value="exampleBean")
@Scope("session")
public class ExampleBean extends _Bean
{
	@Autowired
	Dao dao;

	//
	// Attributes
	//
	private String hello = "EXAMPLE HELLO";
	
	public String getHello() {
		return hello;
	}					
}