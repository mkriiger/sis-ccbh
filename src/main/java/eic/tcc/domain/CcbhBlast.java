package eic.tcc.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_ccbh_blast2go")
public class CcbhBlast implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	@JoinColumn(name = "ccbh_id")
	private Ccbh ccbh;

	@Id
	@ManyToOne
	@JoinColumn(name = "blast2go_go_id")
	private Blast2Go blast;

	@Column(name = "blast2go_e_value")
	private String eValue;

	@Column(name = "blast2go_hits")
	private String hits;

	public Ccbh getCcbh() {
		return ccbh;
	}

	public Blast2Go getBlast() {
		return blast;
	}

	public String geteValue() {
		return eValue;
	}

	public String getHits() {
		return hits;
	}
	
	
	
	
	
	@Override
	public String toString() {
		return "\nNOME BLAST: " + this.blast.getName() + "\nEVALUE: " + this.eValue + "\nHITS: " + this.hits;
	}
	
	
	
}