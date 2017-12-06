package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Licao;

public interface LicaoDao extends JpaRepository<Licao, Long> {

}
