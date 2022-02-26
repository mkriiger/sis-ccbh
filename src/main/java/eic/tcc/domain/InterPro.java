package eic.tcc.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tb_interpro")
public class InterPro {

	@Id
	@Column(name = "interpro_go_id")
	private String id;

	@Column(name = "interpro_go_name")
	private String name;

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
		InterPro other = (InterPro) obj;
		return Objects.equals(id, other.id);
	}
	
	@Override
	public String toString() {
		return "\nINTER ID: " + this.id + " INTER NAME: " + this.name + " LISTA CCBH: " + this.listaCcbh;
	}
}