package eic.tcc.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tb_interpro")
public class InterPro {

	@Id
	@Column(name = "interpro_go_id")
	private String id;
	
	@Column(name = "interpro_go_name")
	private String name;
	
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "interPros")
	Set<Ccbh> ccbhs = new HashSet<>();

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "INTERPRO GO ID: " 
				+ this.id + 
				" INTERPRO GO NAME: " 
				+ this.name + "\n";
	}
	
	
}
