package com.itau.seguro.serviceImpl;

import com.itau.seguro.dtos.ClienteProdutoDto;
import com.itau.seguro.dtos.ParceiroDto;
import com.itau.seguro.dtos.ProdutoDto;
import com.itau.seguro.enums.ParceiroEnum;
import com.itau.seguro.exceptions.BusinessException;
import com.itau.seguro.exceptions.EntityNotFoundException;
import com.itau.seguro.models.Cliente;
import com.itau.seguro.models.Parceiro;
import com.itau.seguro.models.Produto;
import com.itau.seguro.repositories.ClienteRepository;
import com.itau.seguro.repositories.ProdutoRepository;
import com.itau.seguro.services.impl.ClienteServiceImpl;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.imposters.ByteBuddyClassImposteriser;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ClienteServiceImplTest {

    private Mockery context = new Mockery() {
        {
            setImposteriser(ByteBuddyClassImposteriser.INSTANCE);
        }
    };

    @Test
    public void testSaveClienteProdutos(){
        final ClienteServiceImpl service = new ClienteServiceImpl();
        final ClienteRepository repository= context.mock(ClienteRepository.class);
        final ProdutoRepository repositoryProduto= context.mock(ProdutoRepository.class);
        // parametros de entrada
        final ClienteProdutoDto clienteProdutoDto = new ClienteProdutoDto();
        clienteProdutoDto.setClienteId(new Integer(1));
        clienteProdutoDto.setNome("Pedro Alves");
        clienteProdutoDto.setDocumento("28570368097");

        ProdutoDto produtoSeguroVidaDto = new ProdutoDto();
        produtoSeguroVidaDto.setProdutoId(new Integer(1));
        produtoSeguroVidaDto.setNome("Seguro Vida");
        ParceiroDto parceiroComVoceDto = new ParceiroDto();
        parceiroComVoceDto.setCodigo(ParceiroEnum.COM_vOCE.getCodigo());
        parceiroComVoceDto.setNome(ParceiroEnum.COM_vOCE.getDescricao());
        produtoSeguroVidaDto.setParceiro(parceiroComVoceDto);

        ProdutoDto produtoSeguroAltoDto = new ProdutoDto();
        produtoSeguroAltoDto.setProdutoId(new Integer(6));
        produtoSeguroAltoDto.setNome("Seguro Auto");
        ParceiroDto parceiroMaisVoceDto = new ParceiroDto();
        parceiroMaisVoceDto.setCodigo(ParceiroEnum.MAIS_VOCE.getCodigo());
        parceiroMaisVoceDto.setNome(ParceiroEnum.MAIS_VOCE.getDescricao());
        produtoSeguroAltoDto.setParceiro(parceiroMaisVoceDto);

        List< ProdutoDto > produtosDto = new ArrayList<ProdutoDto>();
        produtosDto.add(produtoSeguroVidaDto);
        produtosDto.add(produtoSeguroAltoDto);
        clienteProdutoDto.setProdutos(produtosDto);

        // verificaCLienteProdutoExiste
        final  Optional< Cliente> clienteNãoExiste = Optional.ofNullable(null);

        // retorno cliente sem produtos
        final Cliente cliente = new Cliente();
        cliente.setClienteId(new Integer(1));
        cliente.setNome("Pedro Alves");
        cliente.setDocumento("28570368097");
        cliente.setProdutos(new HashSet<>());

        // retorno cliente com produtos
        final Cliente clienteRetorno = new Cliente();
        clienteRetorno.setClienteId(new Integer(1));
        clienteRetorno.setNome("Pedro Alves");
        clienteRetorno.setDocumento("28570368097");

        Optional<Produto> produtoSeguroVida =  Optional.of(new Produto());
        produtoSeguroVida.get().setProdutoId(new Integer(1));
        produtoSeguroVida.get().setNome("Seguro Vida");
        Parceiro parceiroComVoce = new Parceiro();
        parceiroComVoce.setParceiroId(ParceiroEnum.COM_vOCE.getCodigo());
        parceiroComVoce.setNome(ParceiroEnum.COM_vOCE.getDescricao());
        produtoSeguroVida.get().setParceiro(parceiroComVoce);

        Optional<Produto> produtoSeguroAlto = Optional.of(new Produto());
        produtoSeguroAlto.get().setProdutoId(new Integer(6));
        produtoSeguroAlto.get().setNome("Seguro Auto");
        Parceiro parceiroMaisVoce = new Parceiro();
        parceiroMaisVoce.setParceiroId(ParceiroEnum.MAIS_VOCE.getCodigo());
        parceiroMaisVoce.setNome(ParceiroEnum.MAIS_VOCE.getDescricao());
        produtoSeguroAlto.get().setParceiro(parceiroMaisVoce);

        Set< Produto > produtos = new HashSet<>();
        produtos.add(produtoSeguroVida.get());
        produtos.add(produtoSeguroAlto.get());
        clienteRetorno.setProdutos(produtos);

        context.checking(new Expectations() {
            {
                oneOf(repository).findByDocumento(with(clienteProdutoDto.getDocumento()));
                will(returnValue(clienteNãoExiste));
                oneOf(repository).save(with(cliente));
                will(returnValue(cliente));
                oneOf(repositoryProduto).findById(with(clienteProdutoDto.getProdutos().get(0).getProdutoId()));
                will(returnValue(produtoSeguroVida));
                oneOf(repositoryProduto).findById(with(clienteProdutoDto.getProdutos().get(1).getProdutoId()));
                will(returnValue(produtoSeguroAlto));
            }
        });
        service.setClienteRepository(repository);
        service.setProdutoRepository(repositoryProduto);
        final ClienteProdutoDto clienteProdutoDtoRetorno = service.saveClienteProduto(clienteProdutoDto);
        assertNotNull(clienteProdutoDtoRetorno);
        context.assertIsSatisfied();
    }

    @Test
    public void testSaveClienteProdutoExiste(){
        final ClienteServiceImpl service = new ClienteServiceImpl();
        final ClienteRepository repository= context.mock(ClienteRepository.class);
        final ProdutoRepository repositoryProduto= context.mock(ProdutoRepository.class);
        // parametros de entrada
        final ClienteProdutoDto clienteProdutoDto = new ClienteProdutoDto();
        clienteProdutoDto.setClienteId(new Integer(1));
        clienteProdutoDto.setNome("Pedro Alves");
        clienteProdutoDto.setDocumento("28570368097");

        ProdutoDto produtoSeguroVidaDto = new ProdutoDto();
        produtoSeguroVidaDto.setProdutoId(new Integer(1));
        produtoSeguroVidaDto.setNome("Seguro Vida");
        ParceiroDto parceiroComVoceDto = new ParceiroDto();
        parceiroComVoceDto.setCodigo(ParceiroEnum.COM_vOCE.getCodigo());
        parceiroComVoceDto.setNome(ParceiroEnum.COM_vOCE.getDescricao());
        produtoSeguroVidaDto.setParceiro(parceiroComVoceDto);

        ProdutoDto produtoSeguroAltoDto = new ProdutoDto();
        produtoSeguroAltoDto.setProdutoId(new Integer(6));
        produtoSeguroAltoDto.setNome("Seguro Auto");
        ParceiroDto parceiroMaisVoceDto = new ParceiroDto();
        parceiroMaisVoceDto.setCodigo(ParceiroEnum.MAIS_VOCE.getCodigo());
        parceiroMaisVoceDto.setNome(ParceiroEnum.MAIS_VOCE.getDescricao());
        produtoSeguroAltoDto.setParceiro(parceiroMaisVoceDto);

        List< ProdutoDto > produtosDto = new ArrayList<ProdutoDto>();
        produtosDto.add(produtoSeguroVidaDto);
        produtosDto.add(produtoSeguroAltoDto);
        clienteProdutoDto.setProdutos(produtosDto);

        // verificaCLienteExiste
        final  Optional< Cliente> clienteExiste = Optional.ofNullable(new Cliente());

        clienteExiste.get().setClienteId(new Integer(1));
        clienteExiste.get().setNome("Pedro Alves");
        clienteExiste.get().setDocumento("28570368097");
        Optional<Produto> produtoSeguroVida =  Optional.of(new Produto());
        produtoSeguroVida.get().setProdutoId(new Integer(1));
        produtoSeguroVida.get().setNome("Seguro Vida");
        Parceiro parceiroComVoce = new Parceiro();
        parceiroComVoce.setParceiroId(ParceiroEnum.COM_vOCE.getCodigo());
        parceiroComVoce.setNome(ParceiroEnum.COM_vOCE.getDescricao());
        produtoSeguroVida.get().setParceiro(parceiroComVoce);

        Optional<Produto> produtoSeguroAlto = Optional.of(new Produto());
        produtoSeguroAlto.get().setProdutoId(new Integer(6));
        produtoSeguroAlto.get().setNome("Seguro Auto");
        Parceiro parceiroMaisVoce = new Parceiro();
        parceiroMaisVoce.setParceiroId(ParceiroEnum.MAIS_VOCE.getCodigo());
        parceiroMaisVoce.setNome(ParceiroEnum.MAIS_VOCE.getDescricao());
        produtoSeguroAlto.get().setParceiro(parceiroMaisVoce);

        Set< Produto > produtos = new HashSet<>();
        produtos.add(produtoSeguroVida.get());
        produtos.add(produtoSeguroAlto.get());
        clienteExiste.get().setProdutos(produtos);
        List<Produto> listaProduto = produtos.stream().collect(Collectors.toList());

        context.checking(new Expectations() {
            {
                oneOf(repository).findByDocumento(with(clienteProdutoDto.getDocumento()));
                will(returnValue(clienteExiste));
                oneOf(repository).findByClienteIdAndProdutos_ProdutoId(with(clienteExiste.get().getClienteId()),with(clienteProdutoDto.getProdutos().get(0).getProdutoId()));
                will(returnValue(listaProduto));
                never(repository).save(with(any(Cliente.class)));
                will(returnValue(null));
                never(repositoryProduto).findById(with(any(Integer.class)));
                will(returnValue(null));
            }
        });
        service.setClienteRepository(repository);
        service.setProdutoRepository(repositoryProduto);

        try {
            service.saveClienteProduto(clienteProdutoDto);
        } catch (BusinessException b) {
            context.assertIsSatisfied();
            assertEquals("Cliente ja cadastrado com o produto: Seguro Vida do Parceiro: com Você", b.getMessage());
            return;
        }
        fail("Nao lancou exception");

    }

    @Test
    public void testSaveClienteProdutoNaoCadastrado(){
        final ClienteServiceImpl service = new ClienteServiceImpl();
        final ClienteRepository repository= context.mock(ClienteRepository.class);
        final ProdutoRepository repositoryProduto= context.mock(ProdutoRepository.class);
        // parametros de entrada
        final ClienteProdutoDto clienteProdutoDto = new ClienteProdutoDto();
        clienteProdutoDto.setClienteId(new Integer(1));
        clienteProdutoDto.setNome("Pedro Alves");
        clienteProdutoDto.setDocumento("28570368097");

        ProdutoDto produtoSeguroVidaDto = new ProdutoDto();
        produtoSeguroVidaDto.setProdutoId(new Integer(21));
        produtoSeguroVidaDto.setNome("Seguro Vida");
        ParceiroDto parceiroComVoceDto = new ParceiroDto();
        parceiroComVoceDto.setCodigo(ParceiroEnum.COM_vOCE.getCodigo());
        parceiroComVoceDto.setNome(ParceiroEnum.COM_vOCE.getDescricao());
        produtoSeguroVidaDto.setParceiro(parceiroComVoceDto);

        ProdutoDto produtoSeguroAltoDto = new ProdutoDto();
        produtoSeguroAltoDto.setProdutoId(new Integer(20));
        produtoSeguroAltoDto.setNome("Seguro Auto");
        ParceiroDto parceiroMaisVoceDto = new ParceiroDto();
        parceiroMaisVoceDto.setCodigo(ParceiroEnum.MAIS_VOCE.getCodigo());
        parceiroMaisVoceDto.setNome(ParceiroEnum.MAIS_VOCE.getDescricao());
        produtoSeguroAltoDto.setParceiro(parceiroMaisVoceDto);

        List< ProdutoDto > produtosDto = new ArrayList<ProdutoDto>();
        produtosDto.add(produtoSeguroVidaDto);
        produtosDto.add(produtoSeguroAltoDto);
        clienteProdutoDto.setProdutos(produtosDto);

        // verificaCLienteProdutoExiste
        final  Optional< Cliente> clienteNãoExiste = Optional.ofNullable(null);

        // retorno cliente sem produtos
        final Cliente cliente = new Cliente();
        cliente.setClienteId(new Integer(1));
        cliente.setNome("Pedro Alves");
        cliente.setDocumento("28570368097");
        cliente.setProdutos(new HashSet<>());


        // retorno cliente sem produtos cadastrado

        Optional<Produto> produtoSeguroVida =   Optional.ofNullable(null);

        Optional<Produto> produtoSeguroAlto = Optional.ofNullable(null);


        context.checking(new Expectations() {
            {
                oneOf(repository).findByDocumento(with(clienteProdutoDto.getDocumento()));
                will(returnValue(clienteNãoExiste));
                oneOf(repository).save(with(cliente));
                will(returnValue(cliente));
                oneOf(repositoryProduto).findById(with(clienteProdutoDto.getProdutos().get(0).getProdutoId()));
                will(returnValue(produtoSeguroVida));

            }
        });
        service.setClienteRepository(repository);
        service.setProdutoRepository(repositoryProduto);

        try {
            service.saveClienteProduto(clienteProdutoDto);
        } catch (EntityNotFoundException b) {
            context.assertIsSatisfied();
            assertEquals("Produto Seguro Vida do Parceiro com Você não está cadastrado.", b.getMessage());
            return;
        }
        fail("Nao lancou exception");

    }


}
