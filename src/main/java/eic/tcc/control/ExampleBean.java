package eic.tcc.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import eic.tcc.dao.Dao;
import eic.tcc.domain.Ccbh;
 
@Controller(value="exampleBean")
@Scope("session")
public class ExampleBean extends _Bean
{
	@Autowired
	Dao dao;

	//
	// Attributes
	//
	private String hello = "HELLO WORLD!";
	
	private String id;
	
	public void query() {
		Ccbh ccbh = dao.retrieveById(Ccbh.class, id);
		
		System.out.println(ccbh);
	}
	
	public String getHello() {
		return hello;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}