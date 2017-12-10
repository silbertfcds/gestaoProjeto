package com.example.demo.model;

public enum Avaliacao {
	PERFEITO("Perfeito"),
	MUITOBOM("Muito bom"),
	BOM("Bom"),
	MEDIO("Médio"),
	RUIM("Ruim"),
	MUITORUIM("Muito ruim"),
	HORRIVEL("Horrível"),
	SEMAVALIACAO("Ainda não avaliado");
	
	private String value;
	
	Avaliacao(String val){
		this.value = val;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
