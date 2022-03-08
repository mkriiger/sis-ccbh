package eic.tcc.domain.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import eic.tcc.domain.Ccbh;
import eic.tcc.domain.Enzyme;

public class CcbhVO {

	private Ccbh ccbh;
	private List<Enzyme> enzymes = new ArrayList<>();

	public Ccbh getCcbh() {
		return ccbh;
	}

	public void setCcbh(Ccbh ccbh) {
		this.ccbh = ccbh;
	}

	public List<Enzyme> getEnzymes() {
		return enzymes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ccbh);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CcbhVO other = (CcbhVO) obj;
		return Objects.equals(ccbh, other.ccbh);
	}
}