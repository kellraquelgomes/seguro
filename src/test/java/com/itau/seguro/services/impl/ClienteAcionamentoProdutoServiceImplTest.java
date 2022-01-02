package com.itau.seguro.services.impl;

import com.itau.seguro.dtos.ClienteAcionamentoProdutoDto;
import com.itau.seguro.dtos.ClienteDto;
import com.itau.seguro.dtos.ProdutoDto;
import com.itau.seguro.exceptions.BusinessException;
import com.itau.seguro.models.Cliente;
import com.itau.seguro.models.ClienteAcionamentoProduto;
import com.itau.seguro.models.Produto;
import com.itau.seguro.repositories.ClienteAcionamentoProdutoRepository;
import com.itau.seguro.repositories.ClienteRepository;
import com.itau.seguro.repositories.ProdutoRepository;
import com.itau.seguro.utils.DateUtils;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.imposters.ByteBuddyClassImposteriser;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ClienteAcionamentoProdutoServiceImplTest {

    private Mockery context = new Mockery() {
        {
            setImposteriser(ByteBuddyClassImposteriser.INSTANCE);
        }
    };

    @Test
    public void saveClienteAcionamentoProduto(){

        final ClienteAcionamentoProdutoServiceImpl service = new ClienteAcionamentoProdutoServiceImpl();
        final ClienteAcionamentoProdutoRepository repository= context.mock(ClienteAcionamentoProdutoRepository.class);
        final ProdutoRepository repositoryProduto = context.mock(ProdutoRepository.class);
        final ClienteRepository repositoryCliente = context.mock(ClienteRepository.class);

        // parametros de entrada
        final ClienteAcionamentoProdutoDto clienteAcionamentoProdutoDto = new ClienteAcionamentoProdutoDto();
        final ClienteDto clienteDto = new ClienteDto();
        clienteDto.setClienteId(new Integer(1));
        clienteDto.setDocumento("28570368097");

        final ProdutoDto produtoDto = new ProdutoDto();
        produtoDto.setProdutoId(new Integer(1));

        clienteAcionamentoProdutoDto.setCliente(clienteDto);
        clienteAcionamentoProdutoDto.setProduto(produtoDto);
        clienteAcionamentoProdutoDto.setDataAcionamento(DateUtils.converterStringParaDateTime(("2022-01-05")).toDate());

        final Optional<Cliente> cliente =  Optional.ofNullable(new Cliente());
        cliente.get().setClienteId(new Integer(1));
        cliente.get().setDocumento(clienteAcionamentoProdutoDto.getCliente().getDocumento());

        final  Optional< Produto> produto = Optional.ofNullable(new Produto());
        produto.get().setProdutoId(new Integer(1));
        produto.get().setQuantidadeAcionamento(new Integer(2));

        //retorno mock
        final List< ClienteAcionamentoProduto > clienteAcionamentoProdutos = new ArrayList<ClienteAcionamentoProduto>();
        ClienteAcionamentoProduto clienteAcionamentoProduto = new ClienteAcionamentoProduto();
        clienteAcionamentoProduto.setCliente(cliente.get());
        clienteAcionamentoProduto.setProduto(produto.get());
        clienteAcionamentoProduto.setDataAcionamento(DateUtils.converterStringParaDateTime(("2021-12-05")).toDate());
        clienteAcionamentoProdutos.add(clienteAcionamentoProduto);

        final ClienteAcionamentoProduto clienteAcionamentoProdutoRetorno = new ClienteAcionamentoProduto();
        clienteAcionamentoProdutoRetorno.setCliente(cliente.get());
        clienteAcionamentoProdutoRetorno.setProduto(produto.get());
        clienteAcionamentoProdutoRetorno.setDataAcionamento(DateUtils.converterStringParaDateTime(("2022-01-05")).toDate());


        context.checking(new Expectations() {
            {
                oneOf(repositoryCliente).findByDocumento(with(clienteAcionamentoProdutoDto.getCliente().getDocumento()));
                will(returnValue(cliente));


                oneOf(repositoryProduto).findById(with(clienteAcionamentoProdutoDto.getProduto().getProdutoId()));
                will(returnValue(produto));

                oneOf(repository).countByClienteAndProduto(with(cliente.get()),with(produto.get()));
                will(returnValue(new Integer(1)));


                oneOf(repository).findByClienteAndProdutoOrderByDataAcionamentoDesc(with(cliente.get()), with(produto.get()));
                will(returnValue(clienteAcionamentoProdutos));

                oneOf(repository).save(with(clienteAcionamentoProdutoRetorno));
                will(returnValue(clienteAcionamentoProdutoRetorno));

            }
        });
        service.setClienteAcionamentoProdutoRepository(repository);
        service.setProdutoRepository(repositoryProduto);
        service.setClienteRepository(repositoryCliente);
        service.saveClienteAcionamentoProduto(clienteAcionamentoProdutoDto);
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
        clienteDto.setDocumento("28570368097");

        final ProdutoDto produtoDto = new ProdutoDto();
        produtoDto.setProdutoId(new Integer(1));

        clienteAcionamentoProdutoDto.setCliente(clienteDto);
        clienteAcionamentoProdutoDto.setProduto(produtoDto);
        clienteAcionamentoProdutoDto.setDataAcionamento(DateUtils.converterStringParaDateTime("2022-01-04").toDate());

        final Optional<Cliente> cliente =  Optional.ofNullable(new Cliente());
        cliente.get().setClienteId(new Integer(1));
        cliente.get().setDocumento(clienteDto.getDocumento());

        final  Optional< Produto> produto = Optional.ofNullable(new Produto());
        produto.get().setProdutoId(new Integer(1));
        produto.get().setQuantidadeAcionamento(new Integer(2));


        context.checking(new Expectations() {
            {
                oneOf(repository).countByClienteAndProduto(with(cliente.get()),with(produto.get()));
                will(returnValue(new Integer(2)));

                never(repository).findByClienteAndProdutoOrderByDataAcionamentoDesc(with(any(Cliente.class)),with(any(Produto.class)));
                will(returnValue(null));

            }
        });
        service.setClienteAcionamentoProdutoRepository(repository);
        service.setProdutoRepository(repositoryProduto);

        try {
            service.verificarProdutoLimiteAcionamento(clienteAcionamentoProdutoDto,cliente.get(),produto.get());
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
        clienteDto.setDocumento("28570368097");

        final ProdutoDto produtoDto = new ProdutoDto();
        produtoDto.setProdutoId(new Integer(1));

        clienteAcionamentoProdutoDto.setCliente(clienteDto);
        clienteAcionamentoProdutoDto.setProduto(produtoDto);
        clienteAcionamentoProdutoDto.setDataAcionamento(DateUtils.converterStringParaDateTime(("2022-01-04")).toDate());

        final Optional<Cliente> cliente =  Optional.ofNullable(new Cliente());
        cliente.get().setClienteId(new Integer(1));
        cliente.get().setDocumento(clienteDto.getDocumento());

        final  Optional< Produto> produto = Optional.ofNullable(new Produto());
        produto.get().setProdutoId(new Integer(1));
        produto.get().setQuantidadeAcionamento(new Integer(2));

        //Retorno mock
        final List< ClienteAcionamentoProduto > clienteAcionamentoProdutos = new ArrayList<ClienteAcionamentoProduto>();
        ClienteAcionamentoProduto clienteAcionamentoProduto = new ClienteAcionamentoProduto();
        clienteAcionamentoProduto.setCliente(cliente.get());
        clienteAcionamentoProduto.setProduto(produto.get());
        clienteAcionamentoProduto.setDataAcionamento(DateUtils.converterStringParaDateTime(("2021-12-05")).toDate());
        clienteAcionamentoProdutos.add(clienteAcionamentoProduto);

        context.checking(new Expectations() {
            {
                oneOf(repository).findByClienteAndProdutoOrderByDataAcionamentoDesc(with(cliente.get()), with(produto.get()));
                will(returnValue(clienteAcionamentoProdutos));

            }
        });
        service.setClienteAcionamentoProdutoRepository(repository);
        service.setProdutoRepository(repositoryProduto);

        try {
            service.verificarPeriodoAcionamentoProdutoCliente(clienteAcionamentoProdutoDto,cliente.get(),produto.get());
        } catch (BusinessException b) {
            context.assertIsSatisfied();
            assertEquals("O período entre os acionamentos do mesmo produtos, é de no mínimo " +
                    "30 dias, a partir da data do último acionamento.", b.getMessage());
            return;
        }
        fail("Nao lancou exception");
    }

//    @Test
//    public void testProdutoNaoCadastrado(){
//        final ClienteAcionamentoProdutoServiceImpl service = new ClienteAcionamentoProdutoServiceImpl();
//        final ClienteAcionamentoProdutoRepository repository= context.mock(ClienteAcionamentoProdutoRepository.class);
//        final ProdutoRepository repositoryProduto = context.mock(ProdutoRepository.class);
//        // parametros de entrada
//        final ClienteAcionamentoProdutoDto clienteAcionamentoProdutoDto = new ClienteAcionamentoProdutoDto();
//        final ClienteDto clienteDto = new ClienteDto();
//        final ProdutoDto produtoDto = new ProdutoDto();
//        clienteDto.setClienteId(new Integer(1));
//        produtoDto.setProdutoId(new Integer(1));
//        clienteAcionamentoProdutoDto.setCliente(clienteDto);
//        clienteAcionamentoProdutoDto.setProduto(produtoDto);
//        clienteAcionamentoProdutoDto.setDataAcionamento(DateUtils.converterStringParaDateTime("2022-01-04").toDate());
//
//        final Cliente cliente = new Cliente();
//        cliente.setClienteId(clienteAcionamentoProdutoDto.getCliente().getClienteId());
//        final Produto produto = new Produto();
//        produto.setProdutoId(clienteAcionamentoProdutoDto.getProduto().getProdutoId());
//
//        //Retorno mock
//        final  Optional< Produto> produtoOptional = Optional.ofNullable(new Produto());
//        produtoOptional.get().setProdutoId(new Integer(1));
//        produtoOptional.get().setQuantidadeAcionamento(new Integer(2));
//
//        context.checking(new Expectations() {
//            {
//                oneOf(repository).countByClienteAndProduto(with(cliente),with(produto));
//                will(returnValue(new Integer(2)));
//
//                oneOf(repositoryProduto).findById(with(clienteAcionamentoProdutoDto.getProduto().getProdutoId()));
//                will(returnValue(produtoOptional));
//
//                never(repository).findByClienteAndProdutoOrderByDataAcionamentoDesc(with(any(Cliente.class)),with(any(Produto.class)));
//                will(returnValue(null));
//
//            }
//        });
//        service.setClienteAcionamentoProdutoRepository(repository);
//        service.setProdutoRepository(repositoryProduto);
//
//        try {
//            service.verificarProdutoLimiteAcionamento(clienteAcionamentoProdutoDto,cliente,produto);
//        } catch (BusinessException b) {
//            context.assertIsSatisfied();
//            assertEquals("Não é possivel acionar o seguro,o número de acionamento está no limite.", b.getMessage());
//            return;
//        }
//        fail("Nao lancou exception");
//    }
//

}
