package com.itau.seguro.services.impl;

import com.itau.seguro.dtos.ClienteAcionamentoProdutoDto;
import com.itau.seguro.exceptions.BusinessException;
import com.itau.seguro.exceptions.EntityNotFoundException;
import com.itau.seguro.models.Cliente;
import com.itau.seguro.models.ClienteAcionamentoProduto;
import com.itau.seguro.models.Produto;
import com.itau.seguro.repositories.ClienteAcionamentoProdutoRepository;
import com.itau.seguro.repositories.ClienteRepository;
import com.itau.seguro.repositories.ProdutoRepository;
import com.itau.seguro.services.ClienteAcionamentoProdutoService;
import com.itau.seguro.utils.DateUtils;
import lombok.AccessLevel;
import lombok.Setter;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteAcionamentoProdutoServiceImpl implements ClienteAcionamentoProdutoService {

    @Setter(AccessLevel.PROTECTED)
    @Autowired
    private ClienteAcionamentoProdutoRepository clienteAcionamentoProdutoRepository;

    @Setter(AccessLevel.PROTECTED)
    @Autowired
    private ProdutoRepository produtoRepository;

    @Setter(AccessLevel.PROTECTED)
    @Autowired
    private ClienteRepository clienteRepository;


    @Override
    @Transactional
    public void incluirClienteAcionamentoProduto(ClienteAcionamentoProdutoDto clienteAcionamentoProdutoDto) {

        ClienteAcionamentoProduto clienteAcionamentoProduto = new ClienteAcionamentoProduto();

        Optional< Cliente > cliente = verificarClienteCadastrado(clienteAcionamentoProdutoDto);

        Optional< Produto > produto = verificarProdutoCadastrado(clienteAcionamentoProdutoDto);

        cliente.get().setClienteId(cliente.get().getClienteId());
        produto.get().setProdutoId(produto.get().getProdutoId());

        verificarClienteSeguroContratado(cliente.get(), produto.get());

        verificarProdutoLimiteAcionamento(clienteAcionamentoProdutoDto,cliente.get(),produto.get());

        clienteAcionamentoProduto.setCliente(cliente.get());
        clienteAcionamentoProduto.setProduto(produto.get());
        clienteAcionamentoProduto.setDataAcionamento(clienteAcionamentoProdutoDto.getDataAcionamento());

        clienteAcionamentoProdutoRepository.save(clienteAcionamentoProduto);
    }

    protected void verificarClienteSeguroContratado(Cliente cliente, Produto produto) {

        Optional<Cliente> clienteAndProduto = clienteRepository.findByClienteIdAndProdutoId(
                cliente.getClienteId(), produto.getProdutoId());

        if(!clienteAndProduto.isPresent()){
            throw new BusinessException("O cliente não contratou este seguro.");
        }

    }

    protected Optional< Produto > verificarProdutoCadastrado(ClienteAcionamentoProdutoDto clienteAcionamentoProdutoDto) {

        Optional<Produto> produto= produtoRepository.findById(clienteAcionamentoProdutoDto.getProduto().getProdutoId());

        if(!produto.isPresent()){
            throw new EntityNotFoundException("Produto não cadastrado.");
        }
        return produto;
    }

    protected Optional< Cliente > verificarClienteCadastrado(ClienteAcionamentoProdutoDto clienteAcionamentoProdutoDto) {

        Optional<Cliente> cliente = clienteRepository.findByDocumento(clienteAcionamentoProdutoDto.getCliente().getDocumento());

        if(!cliente.isPresent()){
            throw new EntityNotFoundException("Cliente não cadastrado.");
        }
        return cliente;
    }

    protected void verificarProdutoLimiteAcionamento(ClienteAcionamentoProdutoDto clienteAcionamentoProdutoDto, Cliente cliente, Produto produto) {

        Integer acionamentoProdutoCliente = clienteAcionamentoProdutoRepository.countByClienteAndProduto(
                cliente, produto);

        Integer quantidadeAcionamento = produto.getQuantidadeAcionamento();

        if(acionamentoProdutoCliente >= quantidadeAcionamento){

            throw new BusinessException("Não é possivel acionar o seguro,o número de acionamento está no limite.");
        }
        verificarPeriodoAcionamentoProduto(clienteAcionamentoProdutoDto, cliente, produto);
    }

    protected void verificarPeriodoAcionamentoProduto(ClienteAcionamentoProdutoDto clienteAcionamentoProdutoDto, Cliente cliente, Produto produto) {

        List< ClienteAcionamentoProduto > clienteAcionamentoProdutos = clienteAcionamentoProdutoRepository.
                findByClienteAndProdutoOrderByDataAcionamentoDesc(cliente,produto);

        if(!clienteAcionamentoProdutos.isEmpty()){
            int days = Days.daysBetween(new DateTime(clienteAcionamentoProdutos.get(0).getDataAcionamento()),
                    new DateTime(clienteAcionamentoProdutoDto.getDataAcionamento())).getDays();
            if(days <= 30){
                throw new BusinessException("O período entre os acionamentos do mesmo produtos, é de no mínimo " +
                        "30 dias, a partir da data do último acionamento.");
            }
        }
    }

}
