package eic.tcc.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import eic.tcc.domain.enums.Categoria;

@Entity
@Table(name = "tb_interpro")
public class InterPro {

	@Id
	@Column(name = "interpro_go_id")
	private String id;

	@Column(name = "interpro_go_name")
	private String name;

	@Transient
	private Categoria categoria;

	@Transient
	private List<Ccbh> listaCcbh;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Ccbh> getListaCcbh() {
		return listaCcbh;
	}

	public void setListaCcbh(List<Ccbh> listaCcbh) {
		this.listaCcbh = listaCcbh;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
}