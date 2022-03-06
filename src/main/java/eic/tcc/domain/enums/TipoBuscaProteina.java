package eic.tcc.domain.enums;

public enum TipoBuscaProteina {
	
	NOME("Buscar por nome", "Nome"), DESCRICAO("Buscar por descrição", "Descrição");
		
	private String descricao;
	private String tipo;
	
	private TipoBuscaProteina(String descricao, String valor) {
		this.descricao = descricao;
		this.tipo = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getTopo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	

}
