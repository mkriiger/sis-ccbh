package eic.tcc.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tb_blast2go")
public class Blast2Go {

	@Id
	@Column(name = "blast2go_go_id")
	private String id;

	@Column(name = "blast2go_go_name")
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
	public String toString() {
		return "\nBLAST ID: " + this.id + " BLAST NAME: " + this.name + " LISTA CCBH: " + this.listaCcbh;
	}
}