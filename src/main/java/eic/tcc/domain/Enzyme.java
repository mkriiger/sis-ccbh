package eic.tcc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_enzyme")
public class Enzyme {

	@Id
	@Column(name = "enzyme_code")
	private String code;
	
	@Column(name = "enzyme_name")
	private String name;
	
	
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	
	
}
