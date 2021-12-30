package com.itau.seguro.services.impl;

import com.itau.seguro.dtos.ClienteProdutoDto;
import com.itau.seguro.dtos.ProdutoDto;
import com.itau.seguro.exceptions.BusinessException;
import com.itau.seguro.exceptions.EntityNotFoundException;
import com.itau.seguro.models.Cliente;
import com.itau.seguro.models.Produto;
import com.itau.seguro.repositories.ClienteRepository;
import com.itau.seguro.repositories.ProdutoRepository;
import com.itau.seguro.services.ClienteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {


    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;


    @Transactional
    @Override
    public ClienteProdutoDto saveClienteProduto(ClienteProdutoDto clienteProdutoDto) {

        Cliente cliente = new Cliente();
        verificarClienteProdutoExiste(clienteProdutoDto);
        BeanUtils.copyProperties(clienteProdutoDto,cliente);
        cliente = clienteRepository.save(cliente);

        for (ProdutoDto produtoDto: clienteProdutoDto.getProdutos()) {
            Optional<Produto> produto = produtoRepository.findById(produtoDto.getProdutoId());
            if(produto.isPresent()){
                cliente.getProdutos().add(produto.get());
            }
            else{
                throw new EntityNotFoundException( "Produto "
                        + produtoDto.getNome() + " do Parceiro "
                        + produtoDto.getParceiro().getNome() + " não está cadastrado.");
            }

        }
        BeanUtils.copyProperties(cliente, clienteProdutoDto);
        return clienteProdutoDto;
    }


    private void verificarClienteProdutoExiste(ClienteProdutoDto clienteProdutoDto) {

        Optional<Cliente> cliente = clienteRepository.findByDocumento(clienteProdutoDto.getDocumento());

        if(cliente.isPresent()){
            for (ProdutoDto produto: clienteProdutoDto.getProdutos()) {
              List< Produto > produtos =  clienteRepository.findByClienteIdAndProdutos_ProdutoId(cliente.get().getClienteId(), produto.getProdutoId());

                if(!produtos.isEmpty()){
                   throw new BusinessException( "Cliente ja cadastrado com o produto: "
                           + produto.getNome() + " do Parceiro: "
                           + produto.getParceiro().getNome());
                }
            }
        }

    }



    public ClienteRepository getClienteRepository() {
        return clienteRepository;
    }

    public void setClienteRepository(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ProdutoRepository getProdutoRepository() {
        return produtoRepository;
    }

    public void setProdutoRepository(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

}
