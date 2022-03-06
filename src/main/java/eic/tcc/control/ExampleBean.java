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
import eic.tcc.domain.Ccbh;
import eic.tcc.domain.CcbhBlast;
import eic.tcc.domain.CcbhEnzyme;
import eic.tcc.domain.CcbhInter;
import eic.tcc.domain.Enzyme;
import eic.tcc.domain.enums.Categoria;
import eic.tcc.domain.enums.TipoBuscaProteina;
import eic.tcc.domain.vo.ResultRow;
import eic.tcc.domain.vo.SearchResult;

@Controller(value = "exampleBean")
@Scope("session")
public class ExampleBean extends _Bean {

	@Autowired
	private Dao dao;

	private String nomeGo;
	private String nomeEnzima;
	private String categoriaSelecionada;
	private String buscaProteinaInput;
	private String pesquisaSelecionada;
	private TipoBuscaProteina tipoBuscaProteina;
	
	@SuppressWarnings("unused")
	private String quickGo;
	
	private SearchResult result;
	private List<Ccbh> listAux = new ArrayList<>();
	private List<ResultRow> rows = new ArrayList<>();

	public void pesquisar() {
		rows.clear();
		listAux.clear();
		
		if(this.pesquisaSelecionada.equals("enzima")) {
			buscarPorNomeEnzima();			
		} else if(this.pesquisaSelecionada.equals("go")){
			buscarPorNomeGo();			
		} else if(this.pesquisaSelecionada.equals("proteina")){
			buscarProteina();
		} else if(this.pesquisaSelecionada.equals("tudo")){
			buscarTudo();
		} else {
			retornaErro();
		}
	}
	
	public void retornaAviso() {
		FacesContext.getCurrentInstance().addMessage
		(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
				"Aviso:", "Nenhum registro encontrado"));
	}
	
	public void retornaErro() {
		FacesContext.getCurrentInstance().addMessage
		(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
				"Erro:", "Favor escolher um tipo de pesquisa"));
	}
	
	@SuppressWarnings("unchecked")
	private void buscarPorNomeEnzima() {		
		List<Enzyme> listaEnzimas = (List<Enzyme>) dao.queryHQL("SELECT e FROM Enzyme e WHERE e.name LIKE '%" + this.nomeEnzima + "%'");
		
		for (Enzyme e : listaEnzimas) {
		
			e.setListaCcbh((List<Ccbh>) dao.queryHQL("SELECT ce.ccbh FROM CcbhEnzyme ce WHERE ce.enzyme.code LIKE '%" + e.getCode() + "%'"));
		
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
		
		if(rows.size() == 0) {
			retornaAviso();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private void buscarPorNomeGo() {
		
		this.verificarNulo();
		
		List<Ccbh> allCcbhs = new ArrayList<>();
		
		allCcbhs.addAll((List<Ccbh>) dao.queryHQL("SELECT e.ccbh FROM CcbhBlast e WHERE e.blast.name LIKE '" + this.categoriaSelecionada + "%" + this.nomeGo + "%'"));
		
		allCcbhs.addAll((List<Ccbh>) dao.queryHQL("SELECT e.ccbh FROM CcbhInter e WHERE e.inter.name LIKE '" + this.categoriaSelecionada + "%" + this.nomeGo + "%'"));
		
		for (Ccbh c : allCcbhs) {
			
			if(!listAux.contains(c)) {
				
				c.setListaCcbhEnzyme((List<CcbhEnzyme>) dao.queryHQL("SELECT ce FROM CcbhEnzyme ce WHERE ce.ccbh.id = '" + c.getId() + "'"));
				
				c.setListaCcbhBlast((List<CcbhBlast>) dao.queryHQL("SELECT cb FROM CcbhBlast cb WHERE cb.ccbh.id = '" + c.getId() + "' AND cb.blast.name LIKE '" + this.categoriaSelecionada + "%" + this.nomeGo + "%'"));
				
				c.setListaCcbhInter((List<CcbhInter>) dao.queryHQL("SELECT ci FROM CcbhInter ci WHERE ci.ccbh.id = '" + c.getId() + "' AND ci.inter.name LIKE '" + this.categoriaSelecionada + "%" + this.nomeGo + "%'"));
									
				listAux.add(c);
			}
		
		}
		
		rows.addAll(new SearchResult(listAux).getRowList());
		
		if(rows.size() == 0) {
			retornaAviso();
		}
	}

	@SuppressWarnings("unchecked")
	public void buscarProteina() {
		
		List<Ccbh> allCcbhs = new ArrayList<>();
		
		if(getTipoBuscaProteina().equals(TipoBuscaProteina.DESCRICAO)) {
			allCcbhs.addAll((List<Ccbh>) dao.queryHQL("SELECT e FROM Ccbh e WHERE e.description LIKE '%" + this.buscaProteinaInput + "%'"));
		} else {
			allCcbhs.addAll((List<Ccbh>) dao.queryHQL("SELECT e FROM Ccbh e WHERE e.seqName LIKE '%" + this.buscaProteinaInput + "%'"));
		}
		
		for (Ccbh c : allCcbhs) {
			
			if(!listAux.contains(c)) {
				
				c.setListaCcbhEnzyme((List<CcbhEnzyme>) dao.queryHQL("SELECT ce FROM CcbhEnzyme ce WHERE ce.ccbh.id = '" + c.getId() + "'"));
				
				c.setListaCcbhBlast((List<CcbhBlast>) dao.queryHQL("SELECT cb FROM CcbhBlast cb WHERE cb.ccbh.id = '" + c.getId() + "'"));
				
				c.setListaCcbhInter((List<CcbhInter>) dao.queryHQL("SELECT ci FROM CcbhInter ci WHERE ci.ccbh.id = '" + c.getId() + "'"));
									
				listAux.add(c);
			}
		
		}
		
		rows.addAll(new SearchResult(listAux).getRowList());
		
		if(rows.size() == 0) {
			retornaAviso();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buscarTudo() {
		
		List<Ccbh> allCcbhs = (List<Ccbh>) dao.queryHQL("SELECT c FROM Ccbh c");
		
		for (Ccbh c : allCcbhs) {
			
			c.setListaCcbhEnzyme((List<CcbhEnzyme>) dao.queryHQL("SELECT ce FROM CcbhEnzyme ce WHERE ce.ccbh.id = '" + c.getId() + "'"));
			c.setListaCcbhBlast((List<CcbhBlast>) dao.queryHQL("SELECT cb FROM CcbhBlast cb WHERE cb.ccbh.id = '" + c.getId() + "'"));
			c.setListaCcbhInter((List<CcbhInter>) dao.queryHQL("SELECT ci FROM CcbhInter ci WHERE ci.ccbh.id = '" + c.getId() + "'"));
		}
		rows.addAll(new SearchResult(allCcbhs).getRowList());
	}
	

	public void selecionarCategoria(ValueChangeEvent event) {
		categoriaSelecionada = (String) event.getNewValue();
	}
	
	public void selecionarTipoBuscaProteina(ValueChangeEvent event) {
		if(event.getNewValue().toString().equals(TipoBuscaProteina.DESCRICAO.toString())) {
			setTipoBuscaProteina(TipoBuscaProteina.DESCRICAO);
		} else {
			setTipoBuscaProteina(TipoBuscaProteina.NOME);
		}
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
	
	public TipoBuscaProteina[] getBuscasProteina() {
		return TipoBuscaProteina.values();
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

	//REFERENCIA CRUZADA GO
	public String getQuickGo(String goId) {
		return "https://www.ebi.ac.uk/QuickGO/term/" + goId;
	}

	//REFERENCIA CRUZADA ENZIMA
	public String getBrenda(String enzymeCode) {
		return "https://www.brenda-enzymes.org/enzyme.php?ecno=" + enzymeCode;
	}

	public String getBuscaProteinaInput() {
		return buscaProteinaInput;
	}

	public void setBuscaProteinaInput(String buscaProteinaInput) {
		this.buscaProteinaInput = buscaProteinaInput;
	}

	public TipoBuscaProteina getTipoBuscaProteina() {
		return tipoBuscaProteina;
	}

	public void setTipoBuscaProteina(TipoBuscaProteina tipoBuscaProteina) {
		this.tipoBuscaProteina = tipoBuscaProteina;
	}

	public String getPesquisaSelecionada() {
		return pesquisaSelecionada;
	}

	public void setPesquisaSelecionada(String pesquisaSelecionada) {
		this.pesquisaSelecionada = pesquisaSelecionada;
	}

	
}