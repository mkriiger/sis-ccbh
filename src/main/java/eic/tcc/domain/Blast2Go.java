package eic.tcc.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import eic.tcc.domain.enums.Categoria;

@Entity
@Table(name = "tb_blast2go")
public class Blast2Go {

	@Id
	@Column(name = "blast2go_go_id")
	private String id;

	@Column(name = "blast2go_go_name")
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
	
	
	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Ccbh> getListaCcbh() {
		return listaCcbh;
	}

	public void setListaCcbh(List<Ccbh> listaCcbh) {
		this.listaCcbh = listaCcbh;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
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
		Blast2Go other = (Blast2Go) obj;
		return Objects.equals(id, other.id);
	}

	

}