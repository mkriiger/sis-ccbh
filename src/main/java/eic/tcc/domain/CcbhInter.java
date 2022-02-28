package eic.tcc.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_ccbh_interpro")
public class CcbhInter implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	@JoinColumn(name = "ccbh_id")
	private Ccbh ccbh;

	@Id
	@ManyToOne
	@JoinColumn(name = "interpro_go_id")
	private InterPro inter;

	public Ccbh getCcbh() {
		return ccbh;
	}

	public InterPro getInter() {
		return inter;
	}
	
}