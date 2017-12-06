package com.example.demo.model;

public enum TipoLicao {

	POSITIVO("Positivo"), NEGATIVO("Negativo");

	private String descricao;

	TipoLicao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}

