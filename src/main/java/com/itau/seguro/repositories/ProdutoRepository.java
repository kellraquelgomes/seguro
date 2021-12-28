package com.itau.seguro.repositories;

import com.itau.seguro.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto,Integer> {

    Optional< Produto> findByNome(String nome);
}
