package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Categoria;
import com.example.demo.repository.CategoriaDao;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaDao categoriaDao;
	
	public void salvar(Categoria categoria) {
		// Escrever regras de negócio...
		this.categoriaDao.save(categoria);
	}
	
}
