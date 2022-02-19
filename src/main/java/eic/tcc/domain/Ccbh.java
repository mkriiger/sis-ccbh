package eic.tcc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
	
	@Override
	public String toString() {
		return "ID: " + this.id + "\nSEQ NAME: " + this.seqName + "\nDESCRIPTION: " + this.description + "\nLENGTH: " + this.length;
	}
}