package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Projeto;

public interface ProjetoDao extends JpaRepository<Projeto, Long>{

}
