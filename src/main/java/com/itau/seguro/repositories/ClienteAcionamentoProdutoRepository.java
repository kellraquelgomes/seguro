package com.itau.seguro.repositories;

import com.itau.seguro.models.Cliente;
import com.itau.seguro.models.ClienteAcionamentoProduto;
import com.itau.seguro.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteAcionamentoProdutoRepository extends JpaRepository< ClienteAcionamentoProduto,Integer > {

    Integer countByClienteAndProduto(Cliente cliente, Produto produto);

    List<ClienteAcionamentoProduto> findByClienteAndProdutoOrderByDataAcionamentoDesc(Cliente cliente, Produto produto);

}
