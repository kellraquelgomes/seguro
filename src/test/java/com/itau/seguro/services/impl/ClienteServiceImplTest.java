package com.itau.seguro.services.impl;

import com.itau.seguro.dtos.ClienteDto;
import com.itau.seguro.dtos.ParceiroDto;
import com.itau.seguro.dtos.ProdutoDto;
import com.itau.seguro.enums.ParceiroEnum;
import com.itau.seguro.exceptions.BusinessException;
import com.itau.seguro.exceptions.EntityNotFoundException;
import com.itau.seguro.models.Parceiro;
import com.itau.seguro.models.Produto;
import com.itau.seguro.repositories.ClienteRepository;
import com.itau.seguro.repositories.ProdutoRepository;
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
        ClienteDto clienteDtoProdutoDto = new ClienteDto();
        clienteDtoProdutoDto.setClienteId(new Integer(1));
        clienteDtoProdutoDto.setNome("Pedro Alves");
        clienteDtoProdutoDto.setDocumento("28570368097");

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
        clienteDtoProdutoDto.setProdutos(produtosDto);

        // verificaCLienteProdutoExiste
        final  Optional< com.itau.seguro.models.Cliente > clienteNãoExiste = Optional.ofNullable(null);

        // retorno cliente sem produtos
        final com.itau.seguro.models.Cliente cliente = new com.itau.seguro.models.Cliente();
        cliente.setClienteId(new Integer(1));
        cliente.setNome("Pedro Alves");
        cliente.setDocumento("28570368097");
        cliente.setProdutos(new HashSet<>());

        // retorno cliente com produtos
        final com.itau.seguro.models.Cliente clienteRetorno = new com.itau.seguro.models.Cliente();
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
                oneOf(repository).findByDocumento(with(clienteDtoProdutoDto.getDocumento()));
                will(returnValue(clienteNãoExiste));
                oneOf(repository).save(with(cliente));
                will(returnValue(cliente));
                oneOf(repositoryProduto).findById(with(clienteDtoProdutoDto.getProdutos().get(0).getProdutoId()));
                will(returnValue(produtoSeguroVida));
                oneOf(repositoryProduto).findById(with(clienteDtoProdutoDto.getProdutos().get(1).getProdutoId()));
                will(returnValue(produtoSeguroAlto));
            }
        });
        service.setClienteRepository(repository);
        service.setProdutoRepository(repositoryProduto);
        ClienteDto clienteDtoProdutoDtoRetorno = service.saveClienteProduto(clienteDtoProdutoDto);
        assertNotNull(clienteDtoProdutoDtoRetorno);
        context.assertIsSatisfied();
    }

    @Test
    public void testSaveClienteProdutoExiste(){
        final ClienteServiceImpl service = new ClienteServiceImpl();
        final ClienteRepository repository= context.mock(ClienteRepository.class);
        final ProdutoRepository repositoryProduto= context.mock(ProdutoRepository.class);
        // parametros de entrada
        final ClienteDto clienteDto = new ClienteDto();
        clienteDto.setClienteId(new Integer(1));
        clienteDto.setNome("Pedro Alves");
        clienteDto.setDocumento("28570368097");

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
        clienteDto.setProdutos(produtosDto);

        // verificaCLienteExiste
        final  Optional< com.itau.seguro.models.Cliente > clienteExiste = Optional.ofNullable(new com.itau.seguro.models.Cliente());

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
                oneOf(repository).findByDocumento(with(clienteDto.getDocumento()));
                will(returnValue(clienteExiste));
                oneOf(repository).findByClienteIdAndProdutos_ProdutoId(with(clienteExiste.get().getClienteId()),
                        with(clienteDto.getProdutos().get(0).getProdutoId()));
                will(returnValue(listaProduto));
                never(repository).save(with(any(com.itau.seguro.models.Cliente.class)));
                will(returnValue(null));
                never(repositoryProduto).findById(with(any(Integer.class)));
                will(returnValue(null));
            }
        });
        service.setClienteRepository(repository);
        service.setProdutoRepository(repositoryProduto);

        try {
            service.saveClienteProduto(clienteDto);
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
        final ClienteDto clienteDtoProdutoDto = new ClienteDto();
        clienteDtoProdutoDto.setClienteId(new Integer(1));
        clienteDtoProdutoDto.setNome("Pedro Alves");
        clienteDtoProdutoDto.setDocumento("28570368097");

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
        clienteDtoProdutoDto.setProdutos(produtosDto);

        // verificaCLienteProdutoExiste
        final  Optional< com.itau.seguro.models.Cliente > clienteNãoExiste = Optional.ofNullable(null);

        // retorno cliente sem produtos
        final com.itau.seguro.models.Cliente cliente = new com.itau.seguro.models.Cliente();
        cliente.setClienteId(new Integer(1));
        cliente.setNome("Pedro Alves");
        cliente.setDocumento("28570368097");
        cliente.setProdutos(new HashSet<>());


        // retorno cliente sem produtos cadastrado

        Optional<Produto> produtoSeguroVida =   Optional.ofNullable(null);

        Optional<Produto> produtoSeguroAlto = Optional.ofNullable(null);


        context.checking(new Expectations() {
            {
                oneOf(repository).findByDocumento(with(clienteDtoProdutoDto.getDocumento()));
                will(returnValue(clienteNãoExiste));
                oneOf(repository).save(with(cliente));
                will(returnValue(cliente));
                oneOf(repositoryProduto).findById(with(clienteDtoProdutoDto.getProdutos().get(0).getProdutoId()));
                will(returnValue(produtoSeguroVida));

            }
        });
        service.setClienteRepository(repository);
        service.setProdutoRepository(repositoryProduto);

        try {
            service.saveClienteProduto(clienteDtoProdutoDto);
        } catch (EntityNotFoundException b) {
            context.assertIsSatisfied();
            assertEquals("Produto Seguro Vida do Parceiro com Você não está cadastrado.", b.getMessage());
            return;
        }
        fail("Nao lancou exception");

    }


}
