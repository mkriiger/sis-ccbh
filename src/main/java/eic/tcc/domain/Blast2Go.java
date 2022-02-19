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
@Table(name = "tb_blast2go")
public class Blast2Go {

	@Id
	@Column(name = "blast2go_go_id")
	private String id;
	
	@Column(name = "blast2go_go_name")
	private String name;
	
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "blast2Gos")
	Set<Ccbh> ccbhs = new HashSet<>();

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "BLAST2GO GO ID: " + 
				this.id + " BLAST2GO GO NAME: " + 
				this.name + "\n";
	}
	
	
}
