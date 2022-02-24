package eic.tcc.control;

import java.util.ArrayList;
import java.util.List;
import javax.faces.event.ValueChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import eic.tcc.dao.Dao;
import eic.tcc.domain.CcbhEnzyme;
import eic.tcc.domain.enums.Categoria;
import eic.tcc.domain.vo.SearchResult;

@Controller(value = "exampleBean")
@Scope("session")
public class ExampleBean extends _Bean {

	@Autowired
	private Dao dao;

	//
	// Attributes
	//
	private String nomeGo;
	private String nomeEnzima;
	private String categoriaSelecionada;

	//
	// apagar toda esta query de testes
	//
	public void query() {

//		this.verificarNulo();
//
//		//
//		// teste busca com filtro de categoria
//		//
//		System.out.println(dao.queryHQL("SELECT e FROM CcbhInter e WHERE e.inter.name LIKE '"
//				+ this.categoriaSelecionada + "%" + this.nomeGo + "%'").size());
		List<CcbhEnzyme> enzymeList = buscarPorNomeEnzyme();
		SearchResult result = new SearchResult(enzymeList);
		System.out.println(result);
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

	private List<CcbhEnzyme> buscarPorNomeEnzyme() {
		return (List<CcbhEnzyme>) dao.queryHQL("SELECT e FROM CcbhEnzyme e WHERE e.enzyme.name LIKE '%" + this.nomeEnzima + "%'");
	}

	private void verificarNulo() {
		if (categoriaSelecionada == null)
			categoriaSelecionada = "";
	}

	public void selecionarCategoria(ValueChangeEvent event) {
		categoriaSelecionada = (String) event.getNewValue();
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