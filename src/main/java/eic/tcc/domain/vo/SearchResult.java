package eic.tcc.domain.vo;

import java.util.ArrayList;
import java.util.List;

import eic.tcc.domain.Ccbh;
import eic.tcc.domain.CcbhBlast;
import eic.tcc.domain.CcbhEnzyme;
import eic.tcc.domain.CcbhInter;
import eic.tcc.domain.enums.Categoria;

public class SearchResult {
	
	private List<ResultRow> rowList = new ArrayList<>();

	public SearchResult(List<Ccbh> listAux) {
		for(Ccbh c : listAux) {
			ResultRow row = new ResultRow();
			row.setCcbh(c);
			
			//ADICIONANDO AS ENZIMAS
			for(CcbhEnzyme ce : c.getListaCcbhEnzyme()) {
				row.getEnzymes().add(ce.getEnzyme());
			}
			
			for(CcbhBlast cb : c.getListaCcbhBlast()) {
				
				//DEFININDO AS LETRAS DA CATEGORIA PARA O BADGE
				if(cb.getBlast().getId().substring(0, 1).equals("F")) {
					cb.getBlast().setCategoria(Categoria.F);
				} else if(cb.getBlast().getId().substring(0, 1).equals("P")) {
					cb.getBlast().setCategoria(Categoria.P);
				} else {
					cb.getBlast().setCategoria(Categoria.C);
				}
											
				if(!row.getBlastGos().contains(cb.getBlast())) {
					//REMOVENDO A LETRA DA CATEGORIA
					cb.getBlast().setId(cb.getBlast().getId().substring(2,12));
					cb.getBlast().setName(cb.getBlast().getName().substring(2));
					
					//SETANDO O HITS E EVALUE APENAS NA PRIMEIRA INSERÇÃO DE UM BLAST
					row.seteValue(cb.geteValue());
					row.setHits(cb.getHits());
					row.getBlastGos().add(cb.getBlast());
				} else {
					//REMOVENDO A LETRA DA CATEGORIA
					cb.getBlast().setId(cb.getBlast().getId().substring(2,12));
					cb.getBlast().setName(cb.getBlast().getName().substring(2));
					
					row.getBlastGos().add(cb.getBlast());
				}
				
			}
			for(CcbhInter ci : c.getListaCcbhInter()) {
				
				//DEFININDO AS LETRAS DA CATEGORIA PARA O BADGE
				if(ci.getInter().getId().substring(0, 1).equals("F")) {
					ci.getInter().setCategoria(Categoria.F);
				} else if(ci.getInter().getId().substring(0, 1).equals("P")) {
					ci.getInter().setCategoria(Categoria.P);
				} else {
					ci.getInter().setCategoria(Categoria.C);
				}
				//REMOVENDO A LETRA DA CATEGORIA
				ci.getInter().setId(ci.getInter().getId().substring(2,12));
				ci.getInter().setName(ci.getInter().getName().substring(2));
				row.getInterPros().add(ci.getInter());
			}
			rowList.add(row);
		}
	
	}
	
	public List<ResultRow> getRowList() {
		return rowList;
	}
	
	

}
