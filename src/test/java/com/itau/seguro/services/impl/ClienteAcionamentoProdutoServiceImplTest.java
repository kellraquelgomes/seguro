package com.itau.seguro.services.impl;

import com.itau.seguro.dtos.ClienteAcionamentoProdutoDto;
import com.itau.seguro.dtos.ClienteDto;
import com.itau.seguro.dtos.ProdutoDto;
import com.itau.seguro.exceptions.BusinessException;
import com.itau.seguro.models.Cliente;
import com.itau.seguro.models.ClienteAcionamentoProduto;
import com.itau.seguro.models.Produto;
import com.itau.seguro.repositories.ClienteAcionamentoProdutoRepository;
import com.itau.seguro.repositories.ProdutoRepository;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.imposters.ByteBuddyClassImposteriser;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ClienteAcionamentoProdutoServiceImplTest {

    private Mockery context = new Mockery() {
        {
            setImposteriser(ByteBuddyClassImposteriser.INSTANCE);
        }
    };

    @Test
    public void saveClienteAcionamento(){
        final ClienteAcionamentoProdutoServiceImpl service = new ClienteAcionamentoProdutoServiceImpl();
        final ClienteAcionamentoProdutoRepository repository= context.mock(ClienteAcionamentoProdutoRepository.class);
        final ProdutoRepository repositoryProduto = context.mock(ProdutoRepository.class);
        // parametros de entrada
        final ClienteAcionamentoProdutoDto clienteAcionamentoProdutoDto = new ClienteAcionamentoProdutoDto();
        final ClienteDto clienteDto = new ClienteDto();
        final ProdutoDto produtoDto = new ProdutoDto();
        clienteDto.setClienteId(new Integer(1));
        produtoDto.setProdutoId(new Integer(1));
        clienteAcionamentoProdutoDto.setCliente(clienteDto);
        clienteAcionamentoProdutoDto.setProduto(produtoDto);
        clienteAcionamentoProdutoDto.setDataAcionamento(new DateTime("2022-01-05"));

        final Cliente cliente = new Cliente();
        cliente.setClienteId(clienteAcionamentoProdutoDto.getCliente().getClienteId());
        final Produto produto = new Produto();
        produto.setProdutoId(clienteAcionamentoProdutoDto.getProduto().getProdutoId());

        //Retorno mock
        final  Optional< Produto> produtoOptional = Optional.ofNullable(new Produto());
        produtoOptional.get().setProdutoId(new Integer(1));
        produtoOptional.get().setQuantidadeAcionamento(new Integer(2));

        final List< ClienteAcionamentoProduto > clienteAcionamentoProdutos = new ArrayList<ClienteAcionamentoProduto>();
        ClienteAcionamentoProduto clienteAcionamentoProduto = new ClienteAcionamentoProduto();
        clienteAcionamentoProduto.setCliente(cliente);
        clienteAcionamentoProduto.setProduto(produto);
        clienteAcionamentoProduto.setDataAcionamento(new DateTime("2021-12-05"));
        clienteAcionamentoProdutos.add(clienteAcionamentoProduto);

        //retorno mock save

        final ClienteAcionamentoProduto clienteAcionamentoProdutoRetorno = new ClienteAcionamentoProduto();
        final Cliente clienteRetorno = new Cliente();
        clienteRetorno.setClienteId(new Integer(1));
        final Produto produtoRetorno = new Produto();
        produtoRetorno.setProdutoId(new Integer(1));

        clienteAcionamentoProdutoRetorno.setCliente(clienteRetorno);
        clienteAcionamentoProdutoRetorno.setProduto(produtoRetorno);
        clienteAcionamentoProdutoRetorno.setDataAcionamento(new DateTime("2022-01-05"));


        context.checking(new Expectations() {
            {
                oneOf(repository).countByClienteAndProduto(with(cliente),with(produto));
                will(returnValue(new Integer(1)));
                oneOf(repositoryProduto).findById(with(clienteAcionamentoProdutoDto.getProduto().getProdutoId()));
                will(returnValue(produtoOptional));
                oneOf(repository).findByClienteAndProdutoOrderByDataAcionamentoDesc(with(cliente), with(produto));
                will(returnValue(clienteAcionamentoProdutos));
                oneOf(repository).save(with(clienteAcionamentoProdutoRetorno));
                will(returnValue(clienteAcionamentoProdutoRetorno));

            }
        });
        service.setClienteAcionamentoProdutoRepository(repository);
        service.setProdutoRepository(repositoryProduto);
        final ClienteAcionamentoProdutoDto dto  = service.saveClienteAcionamento(clienteAcionamentoProdutoDto);
        assertNotNull(dto);
        context.assertIsSatisfied();

    }

    @Test
    public void testVerificarProdutoLimiteAcionamento(){
        final ClienteAcionamentoProdutoServiceImpl service = new ClienteAcionamentoProdutoServiceImpl();
        final ClienteAcionamentoProdutoRepository repository= context.mock(ClienteAcionamentoProdutoRepository.class);
        final ProdutoRepository repositoryProduto = context.mock(ProdutoRepository.class);
        // parametros de entrada
        final ClienteAcionamentoProdutoDto clienteAcionamentoProdutoDto = new ClienteAcionamentoProdutoDto();
        final ClienteDto clienteDto = new ClienteDto();
        final ProdutoDto produtoDto = new ProdutoDto();
        clienteDto.setClienteId(new Integer(1));
        produtoDto.setProdutoId(new Integer(1));
        clienteAcionamentoProdutoDto.setCliente(clienteDto);
        clienteAcionamentoProdutoDto.setProduto(produtoDto);
        clienteAcionamentoProdutoDto.setDataAcionamento(new DateTime("2022-01-04"));

        final Cliente cliente = new Cliente();
        cliente.setClienteId(clienteAcionamentoProdutoDto.getCliente().getClienteId());
        final Produto produto = new Produto();
        produto.setProdutoId(clienteAcionamentoProdutoDto.getProduto().getProdutoId());

        //Retorno mock
        final  Optional< Produto> produtoOptional = Optional.ofNullable(new Produto());
        produtoOptional.get().setProdutoId(new Integer(1));
        produtoOptional.get().setQuantidadeAcionamento(new Integer(2));

        context.checking(new Expectations() {
            {
                oneOf(repository).countByClienteAndProduto(with(cliente),with(produto));
                will(returnValue(new Integer(2)));
                oneOf(repositoryProduto).findById(with(clienteAcionamentoProdutoDto.getProduto().getProdutoId()));
                will(returnValue(produtoOptional));
                never(repository).findByClienteAndProdutoOrderByDataAcionamentoDesc(with(any(Cliente.class)),with(any(Produto.class)));
                will(returnValue(null));

            }
        });
        service.setClienteAcionamentoProdutoRepository(repository);
        service.setProdutoRepository(repositoryProduto);

        try {
            service.verificarProdutoLimiteAcionamento(clienteAcionamentoProdutoDto,cliente,produto);
        } catch (BusinessException b) {
            context.assertIsSatisfied();
            assertEquals("Não é possivel acionar o seguro,o número de acionamento está no limite.", b.getMessage());
            return;
        }
        fail("Nao lancou exception");
    }

    @Test
    public void testVerificarPeriodoAcionamentoProdutoCliente(){
        final ClienteAcionamentoProdutoServiceImpl service = new ClienteAcionamentoProdutoServiceImpl();
        final ClienteAcionamentoProdutoRepository repository= context.mock(ClienteAcionamentoProdutoRepository.class);
        final ProdutoRepository repositoryProduto = context.mock(ProdutoRepository.class);
        // parametros de entrada
        final ClienteAcionamentoProdutoDto clienteAcionamentoProdutoDto = new ClienteAcionamentoProdutoDto();
        final ClienteDto clienteDto = new ClienteDto();
        final ProdutoDto produtoDto = new ProdutoDto();
        clienteDto.setClienteId(new Integer(1));
        produtoDto.setProdutoId(new Integer(1));
        clienteAcionamentoProdutoDto.setCliente(clienteDto);
        clienteAcionamentoProdutoDto.setProduto(produtoDto);
        clienteAcionamentoProdutoDto.setDataAcionamento(new DateTime("2022-01-04"));

        //Retorno mock
        final  Optional< Produto> produtoOptional = Optional.ofNullable(new Produto());
        produtoOptional.get().setProdutoId(new Integer(1));
        produtoOptional.get().setQuantidadeAcionamento(new Integer(2));

        final List< ClienteAcionamentoProduto > clienteAcionamentoProdutos = new ArrayList<ClienteAcionamentoProduto>();
        ClienteAcionamentoProduto clienteAcionamentoProduto = new ClienteAcionamentoProduto();

        final Cliente cliente = new Cliente();
        cliente.setClienteId(clienteAcionamentoProdutoDto.getCliente().getClienteId());
        final Produto produto = new Produto();
        produto.setProdutoId(clienteAcionamentoProdutoDto.getProduto().getProdutoId());
        clienteAcionamentoProduto.setCliente(cliente);
        clienteAcionamentoProduto.setProduto(produto);
        clienteAcionamentoProduto.setDataAcionamento(new DateTime("2021-12-05"));
        clienteAcionamentoProdutos.add(clienteAcionamentoProduto);

        context.checking(new Expectations() {
            {
                oneOf(repository).findByClienteAndProdutoOrderByDataAcionamentoDesc(with(cliente), with(produto));
                will(returnValue(clienteAcionamentoProdutos));

            }
        });
        service.setClienteAcionamentoProdutoRepository(repository);
        service.setProdutoRepository(repositoryProduto);

        try {
            service.verificarPeriodoAcionamentoProdutoCliente(clienteAcionamentoProdutoDto,cliente,produto);
        } catch (BusinessException b) {
            context.assertIsSatisfied();
            assertEquals("O período entre os acionamentos do mesmo produtos, é de no mínimo " +
                    "30 dias, a partir da data do último acionamento.", b.getMessage());
            return;
        }
        fail("Nao lancou exception");
    }


}
