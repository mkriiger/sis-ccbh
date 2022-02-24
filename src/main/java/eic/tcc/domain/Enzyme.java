package eic.tcc.domain;

import java.util.Objects;

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
	
	@Override
	public int hashCode() {
		return Objects.hash(code);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Enzyme other = (Enzyme) obj;
		return Objects.equals(code, other.code);
	}
	
	//TODO remover após testes
	@Override
	public String toString() {
		return "Enzyme [code=" + code + ", name=" + name + "]";
	}
	
	
}
