package eic.tcc.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_ccbh_blast2go")
public class CcbhBlast implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	@JoinColumn(name = "ccbh_id")
	private Ccbh ccbh;

	@Id
	@ManyToOne
	@JoinColumn(name = "blast2go_go_id")
	private Blast2Go blast;
	
	public Ccbh getCcbh() {
		return ccbh;
	}

	public Blast2Go getBlast() {
		return blast;
	}
	
	@Override
	public String toString() {
		return "CCBH ID: " + this.ccbh.getId() + "\nCCBH SEQ NAME: " + this.ccbh.getSeqName() +
				"\nBLAST2GO ID: " + this.blast.getId() + "\nBLAST2GO NAME: " + this.blast.getName();
	}
}