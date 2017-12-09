package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Licao;
import com.example.demo.repository.LicaoDao;

@Service
public class LicaoService {

	@Autowired
	private LicaoDao licaoDao;
	
	public void salvar(Licao licao) {
		// Escrever regras de negócio...
		this.licaoDao.save(licao);
	}
	
	public void buscarPorCodigo(Long codigo) {
		licaoDao.findOne(codigo);
	}
	
	public void excluir(Licao licao) {
		licaoDao.delete(licao);;
	}
}
