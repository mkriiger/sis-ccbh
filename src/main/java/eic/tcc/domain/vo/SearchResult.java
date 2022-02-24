package eic.tcc.domain.vo;

import java.util.ArrayList;
import java.util.List;

import eic.tcc.domain.CcbhEnzyme;

public class SearchResult {
	
	private List<CcbhVO> ccbhVOs = new ArrayList<>();
	
	public SearchResult(List<CcbhEnzyme> enzymeList) {		
		for(CcbhEnzyme ccbhEnzyme : enzymeList) {
			CcbhVO x = new CcbhVO();
			x.setCcbh(ccbhEnzyme.getCcbh());
			if(ccbhVOs.contains(x)) {
				int i = ccbhVOs.indexOf(x);
				ccbhVOs.get(i).getEnzymes().add(ccbhEnzyme.getEnzyme());
			} else {
				x.getEnzymes().add(ccbhEnzyme.getEnzyme());
				ccbhVOs.add(x);
			}			
		}
	}

	public List<CcbhVO> getCcbhVOs() {
		return ccbhVOs;
	}

	//TODO remover após testes
	@Override
	public String toString() {
		return "SearchResult: ccbhVOs= " + ccbhVOs + "\n";
	}
	
	

}
