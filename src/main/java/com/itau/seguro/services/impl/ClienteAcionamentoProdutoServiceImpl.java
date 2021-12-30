package com.itau.seguro.services.impl;

import com.itau.seguro.dtos.ClienteAcionamentoProdutoDto;
import com.itau.seguro.exceptions.BusinessException;
import com.itau.seguro.models.Cliente;
import com.itau.seguro.models.ClienteAcionamentoProduto;
import com.itau.seguro.models.Produto;
import com.itau.seguro.repositories.ClienteAcionamentoProdutoRepository;
import com.itau.seguro.repositories.ProdutoRepository;
import com.itau.seguro.services.ClienteAcionamentoProdutoService;
import org.joda.time.Days;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteAcionamentoProdutoServiceImpl implements ClienteAcionamentoProdutoService {


    @Autowired
    private ClienteAcionamentoProdutoRepository clienteAcionamentoProdutoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;


    @Override
    @Transactional
    public ClienteAcionamentoProdutoDto saveClienteAcionamento(ClienteAcionamentoProdutoDto clienteAcionamentoProdutoDto) {
        ClienteAcionamentoProduto clienteAcionamentoProduto = new ClienteAcionamentoProduto();
        verificarProdutoLimiteAcionamento(clienteAcionamentoProdutoDto);
        BeanUtils.copyProperties(clienteAcionamentoProdutoDto,clienteAcionamentoProduto);
        clienteAcionamentoProduto = clienteAcionamentoProdutoRepository.save(clienteAcionamentoProduto);
        BeanUtils.copyProperties(clienteAcionamentoProduto, clienteAcionamentoProdutoDto);
        return clienteAcionamentoProdutoDto;
    }


    private void verificarProdutoLimiteAcionamento(ClienteAcionamentoProdutoDto clienteAcionamentoProdutoDto) {
        Cliente cliente = new Cliente();
        cliente.setClienteId(clienteAcionamentoProdutoDto.getClienteId());
        Produto produto = new Produto();
        produto.setProdutoId(clienteAcionamentoProdutoDto.getProdutoId());

        Integer acionamentoProdutoCliente = clienteAcionamentoProdutoRepository.countByClienteAndProduto(
                cliente, produto);

        Optional<Produto> produtoQuantidadeAcionamento = produtoRepository.findById(clienteAcionamentoProdutoDto.getProdutoId());
        Integer quantidadeAcionamento = produtoQuantidadeAcionamento.get().getQuantidadeAcionamento();

        if(acionamentoProdutoCliente >= quantidadeAcionamento){

            throw new BusinessException("Não é possivel acionar o seguro,o número de acionamento está no limite.");
        }
        List<ClienteAcionamentoProduto> clienteAcionamentoProdutos = clienteAcionamentoProdutoRepository.findByClienteAndProdutoOrderByDataAcionamentoDesc(cliente,produto);

        if(!clienteAcionamentoProdutos.isEmpty()){
            int days = Days.daysBetween(clienteAcionamentoProdutoDto.getDataAcionamento(), clienteAcionamentoProdutos.get(0).getDataAcionamento()).getDays();

            if(days <= 30){
                throw new BusinessException("O período entre os acionamentos do mesmo produtos, é de no mínimo " +
                        "30 dias, a partir da data do último acionamento na data " +
                        clienteAcionamentoProdutos.get(0).getDataAcionamento());
            }

        }
    }

    public ClienteAcionamentoProdutoRepository getClienteAcionamentoProdutoRepository() {
        return clienteAcionamentoProdutoRepository;
    }

    public void setClienteAcionamentoProdutoRepository(ClienteAcionamentoProdutoRepository clienteAcionamentoProdutoRepository) {
        this.clienteAcionamentoProdutoRepository = clienteAcionamentoProdutoRepository;
    }

    public ProdutoRepository getProdutoRepository() {
        return produtoRepository;
    }

    public void setProdutoRepository(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }


}
