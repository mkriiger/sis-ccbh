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

	public String getId() {
		return id;
	}

	public String getSeqName() {
		return seqName;
	}

	public String getDescription() {
		return description;
	}

	public Integer getLength() {
		return length;
	}
}