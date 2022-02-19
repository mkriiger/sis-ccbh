package eic.tcc.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

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
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	  name = "tb_ccbh_blast2go", 
	  joinColumns = @JoinColumn(name = "ccbh_id"), 
	  inverseJoinColumns = @JoinColumn(name = "blast2go_go_id"))
	Set<Blast2Go> blast2Gos = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	  name = "tb_ccbh_interpro", 
	  joinColumns = @JoinColumn(name = "ccbh_id"), 
	  inverseJoinColumns = @JoinColumn(name = "interpro_go_id"))
	Set<InterPro> interPros = new HashSet<>();

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

	public Set<Blast2Go> getBlast2Gos() {
		return blast2Gos;
	}

	public Set<InterPro> getInterPros() {
		return interPros;
	}

	@Override
	public String toString() {
		return "ID: " + this.id + "SEQ NAME: " + this.seqName + "DESCRIPTION: " + 
				this.description + "LENGTH: " + this.length + "BLAST2GOS: " + 
				this.blast2Gos + "INTERPROS: " + 
						this.interPros + "\n";
	}
	
	
}