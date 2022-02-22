package eic.tcc.control;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import eic.tcc.dao.Dao;

@Controller(value = "exampleBean")
@Scope("session")
public class ExampleBean extends _Bean {

	@Autowired
	private Dao dao;

	//
	// Attributes
	//
	private String hello = "HELLO WORLD!";

	private String id;
	private String nomeGo;

	public void query() {

	}

	private List<?> buscarPorNomeGoBlast() {
		return dao.queryHQL("SELECT e FROM CcbhBlast e WHERE e.blast.name LIKE '%" + this.nomeGo + "%'");
	}

	private List<?> buscarPorNomeGoInter() {
		return dao.queryHQL("SELECT e FROM CcbhInter e WHERE e.inter.name LIKE '%" + this.nomeGo + "%'");
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

	public String getNomeGo() {
		return nomeGo;
	}

	public void setNomeGo(String nomeGo) {
		this.nomeGo = nomeGo;
	}
}