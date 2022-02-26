package eic.tcc.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_ccbh_enzyme")
public class CcbhEnzyme implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	@JoinColumn(name = "ccbh_id")
	private Ccbh ccbh;

	@Id
	@ManyToOne
	@JoinColumn(name = "enzyme_code")
	private Enzyme enzyme;

	public Ccbh getCcbh() {
		return ccbh;
	}

	public Enzyme getEnzyme() {
		return enzyme;
	}
}