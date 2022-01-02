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
    public void saveClienteAcionamentoProduto(ClienteAcionamentoProdutoDto clienteAcionamentoProdutoDto) {

        ClienteAcionamentoProduto clienteAcionamentoProduto = new ClienteAcionamentoProduto();

        Optional<Cliente> cliente = clienteRepository.findByDocumento(clienteAcionamentoProdutoDto.getCliente().getDocumento());

        if(!cliente.isPresent()){
            throw new EntityNotFoundException("Cliente não cadastrado.");
        }

        Optional<Produto> produto= produtoRepository.findById(clienteAcionamentoProdutoDto.getProduto().getProdutoId());

        if(!produto.isPresent()){
            throw new EntityNotFoundException("Produto não cadastrado.");
        }

        cliente.get().setClienteId(cliente.get().getClienteId());
        produto.get().setProdutoId(produto.get().getProdutoId());

        verificarProdutoLimiteAcionamento(clienteAcionamentoProdutoDto,cliente.get(),produto.get());

        clienteAcionamentoProduto.setCliente(cliente.get());
        clienteAcionamentoProduto.setProduto(produto.get());
        clienteAcionamentoProduto.setDataAcionamento(clienteAcionamentoProdutoDto.getDataAcionamento());

        clienteAcionamentoProdutoRepository.save(clienteAcionamentoProduto);
    }

    protected void verificarProdutoLimiteAcionamento(ClienteAcionamentoProdutoDto clienteAcionamentoProdutoDto, Cliente cliente, Produto produto) {


        Integer acionamentoProdutoCliente = clienteAcionamentoProdutoRepository.countByClienteAndProduto(
                cliente, produto);

        Integer quantidadeAcionamento = produto.getQuantidadeAcionamento();

        if(acionamentoProdutoCliente >= quantidadeAcionamento){

            throw new BusinessException("Não é possivel acionar o seguro,o número de acionamento está no limite.");
        }
        verificarPeriodoAcionamentoProdutoCliente(clienteAcionamentoProdutoDto, cliente, produto);
    }

    protected void verificarPeriodoAcionamentoProdutoCliente(ClienteAcionamentoProdutoDto clienteAcionamentoProdutoDto, Cliente cliente, Produto produto) {

        List< ClienteAcionamentoProduto > clienteAcionamentoProdutos = clienteAcionamentoProdutoRepository.findByClienteAndProdutoOrderByDataAcionamentoDesc(cliente,produto);

        if(!clienteAcionamentoProdutos.isEmpty()){
            int days = Days.daysBetween( DateUtils.converterDategParaDateTime(clienteAcionamentoProdutos.get(0).getDataAcionamento()),
                    DateUtils.converterDategParaDateTime(clienteAcionamentoProdutoDto.getDataAcionamento())).getDays();
            if(days <= 30){
                throw new BusinessException("O período entre os acionamentos do mesmo produtos, é de no mínimo " +
                        "30 dias, a partir da data do último acionamento.");
            }
        }
    }

}
