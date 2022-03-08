package eic.tcc.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tb_enzyme")
public class Enzyme {

	@Id
	@Column(name = "enzyme_code")
	private String code;

	@Column(name = "enzyme_name")
	private String name;

	public Enzyme(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public Enzyme() {
	}

	@Transient
	private List<Ccbh> listaCcbh;

	@Transient
	private List<CcbhBlast> listaCcbhBlast;

	@Transient
	private List<CcbhInter> listaCcbhInter;

	public String getCode() {
		return code.substring(3);
	}

	public String getName() {
		return name;
	}

	public void setListaCcbh(List<Ccbh> listaCcbh) {
		this.listaCcbh = listaCcbh;
	}

	public List<Ccbh> getListaCcbh() {
		return listaCcbh;
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
}