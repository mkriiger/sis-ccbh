package eic.tcc.domain.enums;

public enum Categoria {

	F("Função Molecular"), P("Processo Biológico"), C("Componente Celular");

	private String descricao;

	Categoria(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}