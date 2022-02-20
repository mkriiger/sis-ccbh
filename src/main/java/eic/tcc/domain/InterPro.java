package eic.tcc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_interpro")
public class InterPro {

	@Id
	@Column(name = "interpro_go_id")
	private String id;

	@Column(name = "interpro_go_name")
	private String name;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "INTERPRO GO ID: " + this.id + " INTERPRO GO NAME: " + this.name + "\n";
	}

}
