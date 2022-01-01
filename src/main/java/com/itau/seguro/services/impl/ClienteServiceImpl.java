package com.itau.seguro.services.impl;

import com.itau.seguro.dtos.ClienteAcionamentoProdutoDto;
import com.itau.seguro.dtos.ClienteDto;
import com.itau.seguro.dtos.ParceiroDto;
import com.itau.seguro.dtos.ProdutoDto;
import com.itau.seguro.exceptions.BusinessException;
import com.itau.seguro.exceptions.EntityNotFoundException;
import com.itau.seguro.models.Cliente;
import com.itau.seguro.models.ClienteAcionamentoProduto;
import com.itau.seguro.models.Produto;
import com.itau.seguro.repositories.ClienteAcionamentoProdutoRepository;
import com.itau.seguro.repositories.ClienteRepository;
import com.itau.seguro.repositories.ProdutoRepository;
import com.itau.seguro.services.ClienteService;
import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    @Setter(AccessLevel.PROTECTED)
    private ClienteRepository clienteRepository;

    @Autowired
    @Setter(AccessLevel.PROTECTED)
    private ProdutoRepository produtoRepository;

    @Autowired
    @Setter(AccessLevel.PROTECTED)
    private ClienteAcionamentoProdutoRepository clienteAcionamentoProdutoRepository;


    @Transactional
    @Override
    public void saveClienteProduto(ClienteDto clienteDto) {

        Cliente cliente = new Cliente();

        verificarClienteProdutoExiste(clienteDto);

        BeanUtils.copyProperties(clienteDto,cliente);

        cliente = clienteRepository.save(cliente);

        for (ProdutoDto produtoDto: clienteDto.getProdutos()) {

            Optional<Produto> produto = produtoRepository.findById(produtoDto.getProdutoId());

            if (produto.isPresent()){
                cliente.getProdutos().add(produto.get());

            }
            else {
                throw new EntityNotFoundException( "Produto "
                        + produtoDto.getNome() + " não está cadastrado.");
            }

        }
    }


    @Override
    public ClienteDto findClienteProdutosAcionamentos(ClienteDto clienteDto) {

        Optional< Cliente > cliente = findByDocumento(clienteDto);

        if (cliente.isPresent()) {

            clienteDto.setDocumento(cliente.get().getDocumento());
            clienteDto.setClienteId(cliente.get().getClienteId());
            clienteDto.setNome(cliente.get().getNome());

            List<ProdutoDto> produtoDtoList = new ArrayList<ProdutoDto>();

            cliente.get().getProdutos().forEach(produto -> {

                ProdutoDto produtoDto = new ProdutoDto();
                produtoDto.setProdutoId(produto.getProdutoId());
                produtoDto.setNome(produto.getNome());
                produtoDto.setValor(produto.getValor());
                produtoDto.setQuantidadeAcionamento(produto.getQuantidadeAcionamento());
                ParceiroDto parceiroDto = new ParceiroDto();
                parceiroDto.setNome(produto.getParceiro().getNome());
                parceiroDto.setCodigo(produto.getParceiro().getParceiroId());
                produtoDto.setParceiro(parceiroDto);
                produtoDtoList.add(produtoDto);

                List<ClienteAcionamentoProduto> acionamentos =
                        clienteAcionamentoProdutoRepository
                                .findByClienteAndProdutoOrderByDataAcionamentoDesc(cliente.get(), produto);

                List<ClienteAcionamentoProdutoDto> acionamentosDtos = new ArrayList<>();

                acionamentos.forEach(clienteAcionamentoProduto -> {

                    ClienteAcionamentoProdutoDto clienteAcionamentoProdutoDto = new ClienteAcionamentoProdutoDto();
                    clienteAcionamentoProdutoDto.setDataAcionamento(clienteAcionamentoProduto.getDataAcionamento());
                    acionamentosDtos.add(clienteAcionamentoProdutoDto);

                });


                produtoDto.setAcionamentos(acionamentosDtos);

            });

            clienteDto.setProdutos(produtoDtoList);

        } else {
            throw new EntityNotFoundException("Cliente não cadastrado.");
        }

        return clienteDto;
    }


    protected void verificarClienteProdutoExiste(ClienteDto clienteDto) {

        Optional< Cliente > cliente = findByDocumento(clienteDto);

        if (cliente.isPresent()){
            for (ProdutoDto produto: clienteDto.getProdutos()) {

              List< Produto > produtos =  produtoRepository.findByClientesAndProdutoId(cliente.get(), produto.getProdutoId());

                if (!produtos.isEmpty()){
                   throw new BusinessException( "Cliente ja cadastrado com o produtoID: " +  produto.getProdutoId());
                }
            }
        }

    }

    protected Optional< Cliente > findByDocumento(ClienteDto clienteDto) {

        return clienteRepository.findByDocumento(clienteDto.getDocumento());
    }

}
