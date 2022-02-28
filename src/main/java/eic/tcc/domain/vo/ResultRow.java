package eic.tcc.domain.vo;

import java.util.ArrayList;
import java.util.List;

import eic.tcc.domain.Blast2Go;
import eic.tcc.domain.Ccbh;
import eic.tcc.domain.Enzyme;
import eic.tcc.domain.InterPro;

public class ResultRow {

	private Ccbh ccbh;
	private String eValue;
	private String hits;
	private List<Enzyme> enzymes = new ArrayList<>();
	private List<Blast2Go> blastGos = new ArrayList<>();
	private List<InterPro> interPros = new ArrayList<>();
	
	
	public Ccbh getCcbh() {
		return ccbh;
	}
	public void setCcbh(Ccbh ccbh) {
		this.ccbh = ccbh;
	}
	public List<Enzyme> getEnzymes() {
		return enzymes;
	}
	public List<Blast2Go> getBlastGos() {
		return blastGos;
	}
	public List<InterPro> getInterPros() {
		return interPros;
	}

	public String geteValue() {
		return eValue;
	}

	public void seteValue(String eValue) {
		this.eValue = eValue;
	}

	public String getHits() {
		return hits;
	}

	public void setHits(String hits) {
		this.hits = hits;
	}
	
	
}
