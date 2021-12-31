package com.itau.seguro.repositories;

import com.itau.seguro.models.Cliente;
import com.itau.seguro.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository< Cliente,Integer > {

    List<Produto> findByClienteIdAndProdutos_ProdutoId(Integer clienteId, Integer produtoId);

    Optional< Cliente> findByDocumento(String documento);


}
