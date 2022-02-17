package eic.tcc.domain;

//@Entity - EXEMPLO JPA
public class Example   
{
//	@Id    		
	protected String id;
	
//	@Column
    protected String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}





