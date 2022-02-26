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

	@Transient
	private List<Ccbh> listaCcbh;

	public String getCode() {
		return code;
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
}