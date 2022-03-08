package eic.tcc.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tb_ccbh")
public class Ccbh {

	@Id
	@Column(name = "ccbh_id")
	private String id;

	@Column(name = "seq_name")
	private String seqName;

	@Column
	private String description;

	@Column
	private Integer length;

	@Transient
	private List<CcbhBlast> listaCcbhBlast;

	@Transient
	private List<CcbhInter> listaCcbhInter;

	@Transient
	private List<CcbhEnzyme> listaCcbhEnzyme;

	public String getId() {
		return id;
	}

	public String getSeqName() {
		return seqName.substring(14).replaceAll("[|]", "");
	}

	public String getDescription() {
		return description;
	}

	public Integer getLength() {
		return length;
	}

	public List<CcbhBlast> getListaCcbhBlast() {
		return listaCcbhBlast;
	}

	public void setListaCcbhBlast(List<CcbhBlast> listaCcbhBlast) {
		this.listaCcbhBlast = listaCcbhBlast;
	}

	public List<CcbhInter> getListaCcbhInter() {
		return listaCcbhInter;
	}

	public void setListaCcbhInter(List<CcbhInter> listaCcbhInter) {
		this.listaCcbhInter = listaCcbhInter;
	}

	public List<CcbhEnzyme> getListaCcbhEnzyme() {
		return listaCcbhEnzyme;
	}

	public void setListaCcbhEnzyme(List<CcbhEnzyme> listaCcbhEnzyme) {
		this.listaCcbhEnzyme = listaCcbhEnzyme;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ccbh other = (Ccbh) obj;
		return Objects.equals(id, other.id);
	}
}