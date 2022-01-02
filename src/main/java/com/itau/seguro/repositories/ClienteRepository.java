package com.itau.seguro.repositories;

import com.itau.seguro.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository< Cliente,Integer > {

    Optional< Cliente> findByDocumento(String documento);

    @Query(value = "select * from TB_CLIENTES_PRODUTOS where  cliente_id = :clienteId and produto_id = :produtoId", nativeQuery = true)
    Optional< Cliente> findByClienteIdAndProdutoId(Integer clienteId, Integer produtoId);

}
