package eic.tcc.control;

import java.util.List;

import javax.faces.event.ValueChangeEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import eic.tcc.dao.Dao;
import eic.tcc.domain.Ccbh;
import eic.tcc.domain.CcbhBlast;
import eic.tcc.domain.Enzyme;
import eic.tcc.domain.enums.Categoria;

@Controller(value = "exampleBean")
@Scope("session")
public class ExampleBean extends _Bean {

	@Autowired
	private Dao dao;

	//
	// Attributes
	//
	private String hello = "HELLO WORLD!";
	private String nomeGo;
	private String nomeEnzima;
	private String categoriaSelecionada;

	//
	// apagar toda esta query de testes
	//
	public void query() {

		this.verificarNulo();

		//
		// query teste busca por nome enzima
		//
		List<Enzyme> listaEnzimas = (List<Enzyme>) dao.queryHQL("SELECT e FROM Enzyme e WHERE e.name LIKE '%" + this.nomeEnzima + "%'");
		
		for (Enzyme e : listaEnzimas) {
		
			// OK
			//Enzyme e = dao.retrieveById(Enzyme.class, "EC:1.1.1.1");
			
			// OK
			e.setListaCcbh((List<Ccbh>) dao.queryHQL("SELECT ce.ccbh FROM CcbhEnzyme ce WHERE ce.enzyme.code like '" + e.getCode() + "'"));
		
			// OK
			for (Ccbh c : e.getListaCcbh()) {
				//c.setListaBlast((List<Blast2Go>) dao.queryHQL("SELECT cb.blast FROM CcbhBlast cb WHERE cb.ccbh.id like '" + c.getId() + "'"));
			
				c.setListaCcbhBlast((List<CcbhBlast>) dao.queryHQL("SELECT cb FROM CcbhBlast cb WHERE cb.ccbh.id like '" + c.getId() + "'"));
			
				System.out.println(c.getId());
				System.out.println(c.getListaCcbhBlast());
			}
		}
	}

	private List<?> buscarPorNomeGoBlast() {

		this.verificarNulo();
		return dao.queryHQL("SELECT e FROM CcbhBlast e WHERE e.blast.name LIKE '" + this.categoriaSelecionada + "%"
				+ this.nomeGo + "%'");
	}

	private List<?> buscarPorNomeGoInter() {

		this.verificarNulo();
		return dao.queryHQL("SELECT e FROM CcbhInter e WHERE e.inter.name LIKE '" + this.categoriaSelecionada + "%"
				+ this.nomeGo + "%'");
	}

	private List<?> buscarPorNomeEnzyme() {
		return dao.queryHQL("SELECT e FROM CcbhEnzyme e WHERE e.enzyme.name LIKE '%" + this.nomeEnzima + "%'");
	}

	private void verificarNulo() {
		if (categoriaSelecionada == null)
			categoriaSelecionada = "";
	}

	public void selecionarCategoria(ValueChangeEvent event) {
		categoriaSelecionada = (String) event.getNewValue();
	}

	public String getHello() {
		return hello;
	}

	public String getNomeGo() {
		return nomeGo;
	}

	public void setNomeGo(String nomeGo) {
		this.nomeGo = nomeGo;
	}

	public String getNomeEnzima() {
		return nomeEnzima;
	}

	public void setNomeEnzima(String nomeEnzima) {
		this.nomeEnzima = nomeEnzima;
	}

	public Categoria[] getCategorias() {
		return Categoria.values();
	}
}