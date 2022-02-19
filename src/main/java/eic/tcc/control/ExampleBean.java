package eic.tcc.control;

import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import eic.tcc.dao.Dao;
import eic.tcc.dao.DaoLike;
import eic.tcc.domain.Blast2Go;
import eic.tcc.domain.Ccbh;
import eic.tcc.domain.InterPro;
 
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
		//DaoLike daoLike = new DaoLike("description", id, MatchMode.ANYWHERE);
		//List<Ccbh> listCcbh = dao.retrieveByManyLikes(Ccbh.class, daoLike);
		List<Blast2Go> listGos = dao.retrieveBySingleLike(Blast2Go.class, "name", 
				id, MatchMode.ANYWHERE);
		List<InterPro> interPros = dao.retrieveBySingleLike(InterPro.class, "name", 
				id, MatchMode.ANYWHERE);
		//Ccbh ccbh = dao.retrieveById(Ccbh.class, id);
		//Blast2Go blast2Go = dao.retrieveById(Blast2Go.class, id);
		
		//InterPro interPro = dao.retrieveById(InterPro.class, id);
		
		System.out.println(listGos);
		System.out.println(interPros);
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