package eic.tcc.domain.enums;

public enum Categoria {

	F("F","Função Molecular", "success"), P("P", "Processo Biológico", "warning"), 
	C("C", "Componente Celular", "info");

	private String category;
	private String descricao;
	private String cor;

	private Categoria(String category, String descricao, String cor) {
		this.category = category;
		this.descricao = descricao;
		this.cor = cor;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getCor() {
		return cor;
	}

	public String getCategory() {
		return category;
	}
}