package eic.tcc.control;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import eic.tcc.dao.Dao;
import eic.tcc.domain.Blast2Go;
import eic.tcc.domain.Ccbh;
import eic.tcc.domain.CcbhBlast;
import eic.tcc.domain.CcbhEnzyme;
import eic.tcc.domain.CcbhInter;
import eic.tcc.domain.Enzyme;
import eic.tcc.domain.InterPro;
import eic.tcc.domain.enums.Categoria;
import eic.tcc.domain.vo.ResultRow;
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
	
	@SuppressWarnings("unused")
	private String quickGo;
	
	private SearchResult result;
	private List<Ccbh> listAux = new ArrayList<>();
	private List<ResultRow> rows = new ArrayList<>();

	public void pesquisar() {
		rows.clear();
		listAux.clear();
		if((!this.nomeEnzima.equals("")) && (this.nomeGo.equals(""))) {
			buscarPorNomeEnzima();
			if(rows.size() == 0) {
				retornaAviso();
			}
		} else if((this.nomeEnzima.equals("")) && (!this.nomeGo.equals(""))){
			buscarPorNomeGo();
			if(rows.size() == 0) {
				retornaAviso();
			}
		} else {
			retornaErro();
		}
	}
	
	public void retornaErro() {
		FacesContext.getCurrentInstance().addMessage
		(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
				"Erro:", "Ao menos um critério de busca deve ser preenchido"));
	}
	
	public void retornaAviso() {
		FacesContext.getCurrentInstance().addMessage
		(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
				"Aviso:", "Nenhum registro encontrado"));
	}
	
	@SuppressWarnings("unchecked")
	public void buscarPorNomeEnzima() {		
		List<Enzyme> listaEnzimas = (List<Enzyme>) dao.queryHQL("SELECT e FROM Enzyme e WHERE e.name LIKE '%" + this.nomeEnzima + "%'");
		
		for (Enzyme e : listaEnzimas) {
		
			e.setListaCcbh((List<Ccbh>) dao.queryHQL("SELECT ce.ccbh FROM CcbhEnzyme ce WHERE ce.enzyme.code = '" + e.getCode() + "'"));
		
			for (Ccbh c : e.getListaCcbh()) {
				if(!listAux.contains(c)) {
					c.setListaCcbhEnzyme((List<CcbhEnzyme>) dao.queryHQL("SELECT cb FROM CcbhEnzyme cb WHERE cb.ccbh.id = '" + c.getId() +
							"' AND cb.enzyme.name LIKE '%" + this.nomeEnzima + "%'"));
					c.setListaCcbhBlast((List<CcbhBlast>) dao.queryHQL("SELECT cb FROM CcbhBlast cb WHERE cb.ccbh.id = '" + c.getId() + "'"));
					c.setListaCcbhInter((List<CcbhInter>) dao.queryHQL("SELECT cb FROM CcbhInter cb WHERE cb.ccbh.id = '" + c.getId() + "'"));
					listAux.add(c);
				}				
			}
		}
		result = new SearchResult(listAux);
		rows.addAll(result.getRowList());
	}
	
	/*
	 * @SuppressWarnings("unchecked") public void buscarPorNomeGo() {
	 * 
	 * List<Blast2Go> listaBlast = (List<Blast2Go>)
	 * dao.queryHQL("SELECT e FROM Blast2Go e WHERE e.name LIKE '%" + this.nomeGo +
	 * "%'"); List<InterPro> listaInter = (List<InterPro>)
	 * dao.queryHQL("SELECT e FROM InterPro e WHERE e.name LIKE '%" + this.nomeGo +
	 * "%'");
	 * 
	 * for(Blast2Go b : listaBlast) {
	 * 
	 * b.setListaCcbh((List<Ccbh>)
	 * dao.queryHQL("SELECT ce.ccbh FROM CcbhBlast ce WHERE ce.blast.id = '" +
	 * b.getId() + "'"));
	 * 
	 * for (Ccbh c : b.getListaCcbh()) {
	 * 
	 * c.setListaCcbhEnzyme((List<CcbhEnzyme>)
	 * dao.queryHQL("SELECT cb FROM CcbhEnzyme cb WHERE cb.ccbh.id = '" + c.getId()
	 * + "'")); } }
	 * 
	 * for(InterPro i : listaInter) {
	 * 
	 * i.setListaCcbh((List<Ccbh>)
	 * dao.queryHQL("SELECT ce.ccbh FROM CcbhInter ce WHERE ce.inter.id = '" +
	 * i.getId() + "'"));
	 * 
	 * for (Ccbh cc : i.getListaCcbh()) { cc.setListaCcbhEnzyme((List<CcbhEnzyme>)
	 * dao.queryHQL("SELECT cb FROM CcbhEnzyme cb WHERE cb.ccbh.id = '" + cc.getId()
	 * + "'")); } }
	 * 
	 * }
	 */
	
	
	
	private void buscarPorNomeGo() {
		
		this.verificarNulo();
		
		List<Ccbh> allCcbhs = new ArrayList<>();
		
		// teste OK
		allCcbhs.addAll((List<Ccbh>) dao.queryHQL("SELECT e.ccbh FROM CcbhBlast e WHERE e.blast.name LIKE '" + this.categoriaSelecionada + "%" + this.nomeGo + "%'"));
		
		// teste OK
		allCcbhs.addAll((List<Ccbh>) dao.queryHQL("SELECT e.ccbh FROM CcbhInter e WHERE e.inter.name LIKE '" + this.categoriaSelecionada + "%" + this.nomeGo + "%'"));
		
		for (Ccbh c : allCcbhs) {
			
			if(!listAux.contains(c)) {
				
				// teste OK
				c.setListaCcbhEnzyme((List<CcbhEnzyme>) dao.queryHQL("SELECT ce FROM CcbhEnzyme ce WHERE ce.ccbh.id = '" + c.getId() + "'"));
				
				// teste OK
				c.setListaCcbhBlast((List<CcbhBlast>) dao.queryHQL("SELECT cb FROM CcbhBlast cb WHERE cb.ccbh.id = '" + c.getId() + "' AND cb.blast.name LIKE '" + this.categoriaSelecionada + "%" + this.nomeGo + "%'"));
				

				// teste OK
				c.setListaCcbhInter((List<CcbhInter>) dao.queryHQL("SELECT ci FROM CcbhInter ci WHERE ci.ccbh.id = '" + c.getId() + "' AND ci.inter.name LIKE '" + this.categoriaSelecionada + "%" + this.nomeGo + "%'"));
				
				
				System.out.println(c.getId());
				System.out.println(c.getListaCcbhInter().size());
				listAux.add(c);
			}
		
		}
		
		rows.addAll(new SearchResult(listAux).getRowList());
	}

	

	public void selecionarCategoria(ValueChangeEvent event) {
		categoriaSelecionada = (String) event.getNewValue();
	}
	
	private void verificarNulo() {
		if (categoriaSelecionada == null)
			categoriaSelecionada = "";
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

	public SearchResult getResult() {
		return result;
	}

	public void setResult(SearchResult result) {
		this.result = result;
	}

	public List<Ccbh> getListAux() {
		return listAux;
	}

	public List<ResultRow> getRows() {
		return rows;
	}

	//REFERENCIA CRUZADA
	public String getQuickGo(String goId) {
		return "https://www.ebi.ac.uk/QuickGO/term/" + goId;
	}

	public void setQuickGo(String quickGo) {
		this.quickGo = quickGo;
	}


	
}