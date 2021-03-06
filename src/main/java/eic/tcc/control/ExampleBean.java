package eic.tcc.control;

import java.util.ArrayList;
import java.util.List;

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
	@SuppressWarnings("unused")
	private String quickGo;

	private String nomeGo;
	private String nomeEnzima;
	private String categoriaSelecionada;
	private String buscaProteinaInput;
	private String pesquisaSelecionada;
	private TipoBuscaProteina tipoBuscaProteina;
	private SearchResult result;
	private List<Ccbh> listAux = new ArrayList<>();
	private List<ResultRow> rows = new ArrayList<>();

	
	@SuppressWarnings("unchecked")
	public void buscarPorNomeEnzima() {

		this.clear();

		List<Enzyme> listaEnzimas = (List<Enzyme>) dao
				.queryHQL("SELECT e FROM Enzyme e WHERE e.name LIKE '%" + this.nomeEnzima + "%'");

		for (Enzyme e : listaEnzimas) {

			e.setListaCcbh((List<Ccbh>) dao
					.queryHQL("SELECT ce.ccbh FROM CcbhEnzyme ce WHERE ce.enzyme.code LIKE '%" + e.getCode() + "%'"));

			for (Ccbh c : e.getListaCcbh()) {
				if (!listAux.contains(c)) {
					c.setListaCcbhEnzyme(
							(List<CcbhEnzyme>) dao.queryHQL("SELECT cb FROM CcbhEnzyme cb WHERE cb.ccbh.id = '"
									+ c.getId() + "' AND cb.enzyme.name LIKE '%" + this.nomeEnzima + "%'"));
					c.setListaCcbhBlast((List<CcbhBlast>) dao
							.queryHQL("SELECT cb FROM CcbhBlast cb WHERE cb.ccbh.id = '" + c.getId() + "'"));
					c.setListaCcbhInter((List<CcbhInter>) dao
							.queryHQL("SELECT cb FROM CcbhInter cb WHERE cb.ccbh.id = '" + c.getId() + "'"));
					listAux.add(c);
				}
			}
		}
		result = new SearchResult(listAux);
		rows.addAll(result.getRowList());
		verificaResultado();
	}

	@SuppressWarnings("unchecked")
	public void buscarPorNomeGo() {

		this.clear();

		this.verificarNulo();

		List<Ccbh> allCcbhs = (List<Ccbh>) dao
				.queryHQL("SELECT DISTINCT e.ccbh FROM CcbhBlast e WHERE e.blast.name LIKE '"
						+ this.categoriaSelecionada + "%" + this.nomeGo + "%'");

		allCcbhs.addAll((List<Ccbh>) dao.queryHQL("SELECT DISTINCT e.ccbh FROM CcbhInter e WHERE e.inter.name LIKE '"
				+ this.categoriaSelecionada + "%" + this.nomeGo + "%'"));

		for (Ccbh c : allCcbhs) {

			if (!listAux.contains(c)) {

				c.setListaCcbhEnzyme((List<CcbhEnzyme>) dao
						.queryHQL("SELECT ce FROM CcbhEnzyme ce WHERE ce.ccbh.id = '" + c.getId() + "'"));

				c.setListaCcbhBlast(
						(List<CcbhBlast>) dao.queryHQL("SELECT cb FROM CcbhBlast cb WHERE cb.ccbh.id = '" + c.getId()
								+ "' AND cb.blast.name LIKE '" + this.categoriaSelecionada + "%" + this.nomeGo + "%'"));

				c.setListaCcbhInter(
						(List<CcbhInter>) dao.queryHQL("SELECT ci FROM CcbhInter ci WHERE ci.ccbh.id = '" + c.getId()
								+ "' AND ci.inter.name LIKE '" + this.categoriaSelecionada + "%" + this.nomeGo + "%'"));

				listAux.add(c);
			}
		}

		rows.addAll(new SearchResult(listAux).getRowList());

		verificaResultado();
	}

	@SuppressWarnings("unchecked")
	public void buscarProteina() {

		this.clear();

		List<Ccbh> allCcbhs = new ArrayList<>();

		if (getTipoBuscaProteina().equals(TipoBuscaProteina.DESCRICAO)) {
			allCcbhs.addAll((List<Ccbh>) dao
					.queryHQL("SELECT e FROM Ccbh e WHERE e.description LIKE '%" + this.buscaProteinaInput + "%'"));
		} else {
			allCcbhs.addAll((List<Ccbh>) dao
					.queryHQL("SELECT e FROM Ccbh e WHERE e.seqName LIKE '%" + this.buscaProteinaInput + "%'"));
		}

		for (Ccbh c : allCcbhs) {

			if (!listAux.contains(c)) {

				c.setListaCcbhEnzyme((List<CcbhEnzyme>) dao
						.queryHQL("SELECT ce FROM CcbhEnzyme ce WHERE ce.ccbh.id = '" + c.getId() + "'"));

				c.setListaCcbhBlast((List<CcbhBlast>) dao
						.queryHQL("SELECT cb FROM CcbhBlast cb WHERE cb.ccbh.id = '" + c.getId() + "'"));

				c.setListaCcbhInter((List<CcbhInter>) dao
						.queryHQL("SELECT ci FROM CcbhInter ci WHERE ci.ccbh.id = '" + c.getId() + "'"));

				listAux.add(c);
			}
		}

		rows.addAll(new SearchResult(listAux).getRowList());

		verificaResultado();
	}

	@SuppressWarnings("unchecked")
	public void buscarTudo() {

		this.clear();

		List<Ccbh> allCcbhs = (List<Ccbh>) dao.queryHQL("SELECT c FROM Ccbh c");

		for (Ccbh c : allCcbhs) {

			c.setListaCcbhEnzyme((List<CcbhEnzyme>) dao
					.queryHQL("SELECT ce FROM CcbhEnzyme ce WHERE ce.ccbh.id = '" + c.getId() + "'"));
			c.setListaCcbhBlast((List<CcbhBlast>) dao
					.queryHQL("SELECT cb FROM CcbhBlast cb WHERE cb.ccbh.id = '" + c.getId() + "'"));
			c.setListaCcbhInter((List<CcbhInter>) dao
					.queryHQL("SELECT ci FROM CcbhInter ci WHERE ci.ccbh.id = '" + c.getId() + "'"));
		}
		rows.addAll(new SearchResult(allCcbhs).getRowList());

		this.resetDataTableUI("formSearch:resultado");
	}

	public void selecionarCategoria(ValueChangeEvent event) {
		categoriaSelecionada = (String) event.getNewValue();
	}

	public void selecionarTipoBuscaProteina(ValueChangeEvent event) {
		if (event.getNewValue().toString().equals(TipoBuscaProteina.DESCRICAO.toString())) {
			setTipoBuscaProteina(TipoBuscaProteina.DESCRICAO);
		} else {
			setTipoBuscaProteina(TipoBuscaProteina.NOME);
		}
	}

	private void verificarNulo() {
		if (categoriaSelecionada == null)
			categoriaSelecionada = "";
	}
	
	private void verificaResultado() {
		if (this.rows.size() == 0) {
			this.popWarning("Nenhum registro encontrado");
		} else {
			this.resetDataTableUI("formSearch:resultado");
		}
	}

	private void clear() {
		rows.clear();
		listAux.clear();
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

	// REFERENCIA CRUZADA GO
	public String getQuickGo(String goId) {
		return "https://www.ebi.ac.uk/QuickGO/term/" + goId;
	}

	// REFERENCIA CRUZADA ENZIMA
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