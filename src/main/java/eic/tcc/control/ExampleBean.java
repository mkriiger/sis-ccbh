package eic.tcc.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import eic.tcc.dao.Dao;

@Controller(value = "exampleBean")
@Scope("session")
public class ExampleBean extends _Bean {
	@Autowired
	Dao dao;

	//
	// Attributes
	//
	private String hello = "HELLO WORLD!";

	private String id;

	public void query() {
		// DaoLike daoLike = new DaoLike("description", id, MatchMode.ANYWHERE);
		// List<Ccbh> listCcbh = dao.retrieveByManyLikes(Ccbh.class, daoLike);
		// List<CcbhBlast> listGos = dao.retrieveBySingleLike(CcbhBlast.class, "name",
		// id, MatchMode.ANYWHERE);
		// List<InterPro> interPros = dao.retrieveBySingleLike(InterPro.class, "name",
		// id, MatchMode.ANYWHERE);
		// Ccbh ccbh = dao.retrieveById(Ccbh.class, id);
		// Blast2Go blast2Go = dao.retrieveById(Blast2Go.class, id);

		// InterPro interPro = dao.retrieveById(InterPro.class, id);

		// EntityManagerFactory emfactory = Persistence.createEntityManagerFactory();
		// EntityManager entitymanager = emfactory.createEntityManager();
		// Query query = entitymanager.createNamedQuery("findByGoName");
		// query.setParameter("name", this.id);
		// List<CcbhBlast> list = query.getResultList();
		
		System.out.println(dao.queryHQL("SELECT e FROM CcbhBlast e WHERE e.blast.name LIKE '%" +  this.id + "%'"));
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