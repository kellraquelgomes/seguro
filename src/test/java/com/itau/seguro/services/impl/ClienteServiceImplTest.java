package com.itau.seguro.services.impl;

import com.itau.seguro.dtos.ClienteDto;
import com.itau.seguro.dtos.ParceiroDto;
import com.itau.seguro.dtos.ProdutoDto;
import com.itau.seguro.enums.ParceiroEnum;
import com.itau.seguro.exceptions.BusinessException;
import com.itau.seguro.exceptions.EntityNotFoundException;
import com.itau.seguro.models.Cliente;
import com.itau.seguro.models.ClienteAcionamentoProduto;
import com.itau.seguro.models.Parceiro;
import com.itau.seguro.models.Produto;
import com.itau.seguro.repositories.ClienteAcionamentoProdutoRepository;
import com.itau.seguro.repositories.ClienteRepository;
import com.itau.seguro.repositories.ProdutoRepository;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.imposters.ByteBuddyClassImposteriser;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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
        final ClienteDto clienteDto = new ClienteDto();
        clienteDto.setClienteId(new Integer(1));
        clienteDto.setNome("Pedro Alves");
        clienteDto.setDocumento("28570368097");

        final ProdutoDto produtoSeguroVidaDto = new ProdutoDto();
        produtoSeguroVidaDto.setProdutoId(new Integer(1));
        produtoSeguroVidaDto.setNome("Seguro Vida");
        final ParceiroDto parceiroComVoceDto = new ParceiroDto();
        parceiroComVoceDto.setCodigo(ParceiroEnum.COM_vOCE.getCodigo());
        parceiroComVoceDto.setNome(ParceiroEnum.COM_vOCE.getDescricao());
        produtoSeguroVidaDto.setParceiro(parceiroComVoceDto);

        final ProdutoDto produtoSeguroAltoDto = new ProdutoDto();
        produtoSeguroAltoDto.setProdutoId(new Integer(6));
        produtoSeguroAltoDto.setNome("Seguro Auto");
        final ParceiroDto parceiroMaisVoceDto = new ParceiroDto();
        parceiroMaisVoceDto.setCodigo(ParceiroEnum.MAIS_VOCE.getCodigo());
        parceiroMaisVoceDto.setNome(ParceiroEnum.MAIS_VOCE.getDescricao());
        produtoSeguroAltoDto.setParceiro(parceiroMaisVoceDto);

        final List< ProdutoDto > produtosDto = new ArrayList<ProdutoDto>();
        produtosDto.add(produtoSeguroVidaDto);
        produtosDto.add(produtoSeguroAltoDto);
        clienteDto.setProdutos(produtosDto);

        // verificaCLienteProdutoExiste
        final  Optional<Cliente > clienteNãoExiste = Optional.ofNullable(null);

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

        final Optional<Produto> produtoSeguroVida =  Optional.of(new Produto());
        produtoSeguroVida.get().setProdutoId(new Integer(1));
        produtoSeguroVida.get().setNome("Seguro Vida");

        final Parceiro parceiroComVoce = new Parceiro();
        parceiroComVoce.setParceiroId(ParceiroEnum.COM_vOCE.getCodigo());
        parceiroComVoce.setNome(ParceiroEnum.COM_vOCE.getDescricao());
        produtoSeguroVida.get().setParceiro(parceiroComVoce);

        final Optional<Produto> produtoSeguroAlto = Optional.of(new Produto());
        produtoSeguroAlto.get().setProdutoId(new Integer(6));
        produtoSeguroAlto.get().setNome("Seguro Auto");

        final Parceiro parceiroMaisVoce = new Parceiro();
        parceiroMaisVoce.setParceiroId(ParceiroEnum.MAIS_VOCE.getCodigo());
        parceiroMaisVoce.setNome(ParceiroEnum.MAIS_VOCE.getDescricao());
        produtoSeguroAlto.get().setParceiro(parceiroMaisVoce);

        final Set< Produto > produtos = new HashSet<>();
        produtos.add(produtoSeguroVida.get());
        produtos.add(produtoSeguroAlto.get());

        clienteRetorno.setProdutos(produtos);

        context.checking(new Expectations() {
            {
                oneOf(repository).findByDocumento(with(clienteDto.getDocumento()));
                will(returnValue(clienteNãoExiste));
                oneOf(repository).save(with(cliente));
                will(returnValue(cliente));
                oneOf(repositoryProduto).findById(with(clienteDto.getProdutos().get(0).getProdutoId()));
                will(returnValue(produtoSeguroVida));
                oneOf(repositoryProduto).findById(with(clienteDto.getProdutos().get(1).getProdutoId()));
                will(returnValue(produtoSeguroAlto));
            }
        });
        service.setClienteRepository(repository);
        service.setProdutoRepository(repositoryProduto);
        final ClienteDto clienteDtoProdutoDtoRetorno = service.saveClienteProduto(clienteDto);
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

        final ProdutoDto produtoSeguroVidaDto = new ProdutoDto();
        produtoSeguroVidaDto.setProdutoId(new Integer(1));
        produtoSeguroVidaDto.setNome("Seguro Vida");

        final ParceiroDto parceiroComVoceDto = new ParceiroDto();
        parceiroComVoceDto.setCodigo(ParceiroEnum.COM_vOCE.getCodigo());
        parceiroComVoceDto.setNome(ParceiroEnum.COM_vOCE.getDescricao());
        produtoSeguroVidaDto.setParceiro(parceiroComVoceDto);

        final ProdutoDto produtoSeguroAltoDto = new ProdutoDto();
        produtoSeguroAltoDto.setProdutoId(new Integer(6));
        produtoSeguroAltoDto.setNome("Seguro Auto");

        final ParceiroDto parceiroMaisVoceDto = new ParceiroDto();
        parceiroMaisVoceDto.setCodigo(ParceiroEnum.MAIS_VOCE.getCodigo());
        parceiroMaisVoceDto.setNome(ParceiroEnum.MAIS_VOCE.getDescricao());
        produtoSeguroAltoDto.setParceiro(parceiroMaisVoceDto);

        final List< ProdutoDto > produtosDto = new ArrayList<ProdutoDto>();
        produtosDto.add(produtoSeguroVidaDto);
        produtosDto.add(produtoSeguroAltoDto);
        clienteDto.setProdutos(produtosDto);

        // verificaCLienteExiste
        final  Optional<Cliente > clienteExiste = Optional.ofNullable(new Cliente());
        clienteExiste.get().setClienteId(new Integer(1));
        clienteExiste.get().setNome("Pedro Alves");
        clienteExiste.get().setDocumento("28570368097");

        final Optional<Produto> produtoSeguroVida =  Optional.of(new Produto());
        produtoSeguroVida.get().setProdutoId(new Integer(1));
        produtoSeguroVida.get().setNome("Seguro Vida");

        final Parceiro parceiroComVoce = new Parceiro();
        parceiroComVoce.setParceiroId(ParceiroEnum.COM_vOCE.getCodigo());
        parceiroComVoce.setNome(ParceiroEnum.COM_vOCE.getDescricao());
        produtoSeguroVida.get().setParceiro(parceiroComVoce);

        final Optional<Produto> produtoSeguroAlto = Optional.of(new Produto());
        produtoSeguroAlto.get().setProdutoId(new Integer(6));
        produtoSeguroAlto.get().setNome("Seguro Auto");

        final Parceiro parceiroMaisVoce = new Parceiro();
        parceiroMaisVoce.setParceiroId(ParceiroEnum.MAIS_VOCE.getCodigo());
        parceiroMaisVoce.setNome(ParceiroEnum.MAIS_VOCE.getDescricao());
        produtoSeguroAlto.get().setParceiro(parceiroMaisVoce);

        final Set< Produto > produtos = new HashSet<>();
        produtos.add(produtoSeguroVida.get());
        produtos.add(produtoSeguroAlto.get());

        clienteExiste.get().setProdutos(produtos);

        final List<Produto> listaProduto = produtos.stream().collect(Collectors.toList());

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
        final ClienteDto clienteDto = new ClienteDto();
        clienteDto.setClienteId(new Integer(1));
        clienteDto.setNome("Pedro Alves");
        clienteDto.setDocumento("28570368097");

        final ProdutoDto produtoSeguroVidaDto = new ProdutoDto();
        produtoSeguroVidaDto.setProdutoId(new Integer(21));
        produtoSeguroVidaDto.setNome("Seguro Vida");

        final ParceiroDto parceiroComVoceDto = new ParceiroDto();
        parceiroComVoceDto.setCodigo(ParceiroEnum.COM_vOCE.getCodigo());
        parceiroComVoceDto.setNome(ParceiroEnum.COM_vOCE.getDescricao());
        produtoSeguroVidaDto.setParceiro(parceiroComVoceDto);

        final ProdutoDto produtoSeguroAltoDto = new ProdutoDto();
        produtoSeguroAltoDto.setProdutoId(new Integer(20));
        produtoSeguroAltoDto.setNome("Seguro Auto");

        final ParceiroDto parceiroMaisVoceDto = new ParceiroDto();
        parceiroMaisVoceDto.setCodigo(ParceiroEnum.MAIS_VOCE.getCodigo());
        parceiroMaisVoceDto.setNome(ParceiroEnum.MAIS_VOCE.getDescricao());
        produtoSeguroAltoDto.setParceiro(parceiroMaisVoceDto);

        final List< ProdutoDto > produtosDto = new ArrayList<ProdutoDto>();
        produtosDto.add(produtoSeguroVidaDto);
        produtosDto.add(produtoSeguroAltoDto);

        clienteDto.setProdutos(produtosDto);

        // verificaCLienteProdutoExiste
        final  Optional< Cliente > clienteNãoExiste = Optional.ofNullable(null);

        // retorno cliente sem produtos
        final Cliente cliente = new Cliente();
        cliente.setClienteId(new Integer(1));
        cliente.setNome("Pedro Alves");
        cliente.setDocumento("28570368097");
        cliente.setProdutos(new HashSet<>());

        // retorno cliente sem produtos cadastrado
        final Optional<Produto> produtoSeguroVida =   Optional.ofNullable(null);
        final Optional<Produto> produtoSeguroAlto = Optional.ofNullable(null);


        context.checking(new Expectations() {
            {
                oneOf(repository).findByDocumento(with(clienteDto.getDocumento()));
                will(returnValue(clienteNãoExiste));
                oneOf(repository).save(with(cliente));
                will(returnValue(cliente));
                oneOf(repositoryProduto).findById(with(clienteDto.getProdutos().get(0).getProdutoId()));
                will(returnValue(produtoSeguroVida));

            }
        });
        service.setClienteRepository(repository);
        service.setProdutoRepository(repositoryProduto);

        try {
            service.saveClienteProduto(clienteDto);
        } catch (EntityNotFoundException b) {
            context.assertIsSatisfied();
            assertEquals("Produto Seguro Vida do Parceiro com Você não está cadastrado.", b.getMessage());
            return;
        }
        fail("Nao lancou exception");

    }

    @Test
    public void testFindClienteProdutosAcionamentos() {

        final ClienteServiceImpl service = new ClienteServiceImpl();
        final ClienteRepository repository = context.mock(ClienteRepository.class);
        final ProdutoRepository repositoryProduto = context.mock(ProdutoRepository.class);
        final ClienteAcionamentoProdutoRepository clienteAcionamentoProdutoRepository =
                context.mock(ClienteAcionamentoProdutoRepository.class);

        // parametros de entrada
        final ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDocumento("28570368097");

        //retorno mok da Entidade de produtos e acionamentos
        final  Optional<Cliente > cliente = Optional.ofNullable(new Cliente());
        cliente.get().setClienteId(new Integer(1));
        cliente.get().setNome("Pedro Alves");
        cliente.get().setDocumento("28570368097");

        final Produto produtoSeguroVida = new Produto();
        produtoSeguroVida.setProdutoId(new Integer(1));
        produtoSeguroVida.setNome("Seguro Vida");
        produtoSeguroVida.setValor(new BigDecimal(200.00));
        produtoSeguroVida.setQuantidadeAcionamento(new Integer(1));
        final Parceiro parceiroComVoce = new Parceiro();
        parceiroComVoce.setParceiroId(ParceiroEnum.COM_vOCE.getCodigo());
        parceiroComVoce.setNome(ParceiroEnum.COM_vOCE.getDescricao());
        produtoSeguroVida.setParceiro(parceiroComVoce);

        final Produto produtoSeguroAlto = new Produto();
        produtoSeguroAlto.setProdutoId(new Integer(6));
        produtoSeguroAlto.setNome("Seguro Auto");
        produtoSeguroAlto.setValor(new BigDecimal(300.00));
        produtoSeguroAlto.setQuantidadeAcionamento(new Integer(3));
        final Parceiro parceiroMaisVoce = new Parceiro();
        parceiroMaisVoce.setParceiroId(ParceiroEnum.MAIS_VOCE.getCodigo());
        parceiroMaisVoce.setNome(ParceiroEnum.MAIS_VOCE.getDescricao());
        produtoSeguroAlto.setParceiro(parceiroMaisVoce);

        final HashSet< Produto > produtos = new HashSet<>();
        produtos.add(produtoSeguroVida);
        produtos.add(produtoSeguroAlto);
        cliente.get().setProdutos(produtos);

        final ClienteAcionamentoProduto clienteAcionamentoProdutoSeguroVida = new ClienteAcionamentoProduto();
        clienteAcionamentoProdutoSeguroVida.setDataAcionamento(converterStringParaDateTime("2022-01-12"));

        final ClienteAcionamentoProduto clienteAcionamentoProdutoSeguroVida_SegundoAcionamento = new ClienteAcionamentoProduto();
        clienteAcionamentoProdutoSeguroVida_SegundoAcionamento.setDataAcionamento(converterStringParaDateTime("2022-03-12"));

        final ClienteAcionamentoProduto clienteAcionamentoProdutoSeguroAlto = new ClienteAcionamentoProduto();
        clienteAcionamentoProdutoSeguroAlto.setDataAcionamento(converterStringParaDateTime("2022-03-12"));

        final HashSet< ClienteAcionamentoProduto > clienteAcionamentoProdutos = new HashSet<>();
        clienteAcionamentoProdutos.add(clienteAcionamentoProdutoSeguroVida);
        clienteAcionamentoProdutos.add(clienteAcionamentoProdutoSeguroVida_SegundoAcionamento);
        clienteAcionamentoProdutos.add(clienteAcionamentoProdutoSeguroAlto);

        cliente.get().setAcionamentos(clienteAcionamentoProdutos);

        context.checking(new Expectations() {
            {
                oneOf(repository).findByDocumento(with(clienteDto.getDocumento()));
                will(returnValue(cliente));


                oneOf(clienteAcionamentoProdutoRepository)
                        .findByClienteAndProdutoOrderByDataAcionamentoDesc(with(cliente.get()), with(produtoSeguroVida));
                will(returnValue(Arrays.asList(clienteAcionamentoProdutoSeguroVida,clienteAcionamentoProdutoSeguroVida_SegundoAcionamento)));

                oneOf(clienteAcionamentoProdutoRepository)
                        .findByClienteAndProdutoOrderByDataAcionamentoDesc(with(cliente.get()), with(produtoSeguroAlto));
                will(returnValue(Arrays.asList(clienteAcionamentoProdutoSeguroAlto)));

            }
        });

        service.setClienteRepository(repository);
        service.setProdutoRepository(repositoryProduto);
        service.setClienteAcionamentoProdutoRepository(clienteAcionamentoProdutoRepository);

        ClienteDto clienteDtoProdutoDtoRetorno = service.findClienteProdutosAcionamentos(clienteDto);

        Assert.assertEquals("Pedro Alves",clienteDtoProdutoDtoRetorno.getNome());
        Assert.assertEquals("28570368097",clienteDtoProdutoDtoRetorno.getDocumento());

        Assert.assertEquals("com Você",clienteDtoProdutoDtoRetorno.getProdutos().get(0).getParceiro().getNome());
        Assert.assertEquals("Seguro Vida",clienteDtoProdutoDtoRetorno.getProdutos().get(0).getNome());
        Assert.assertEquals(new BigDecimal(200.00),clienteDtoProdutoDtoRetorno.getProdutos().get(0).getValor());
        Assert.assertEquals(new Integer(1),clienteDtoProdutoDtoRetorno.getProdutos().get(0).getQuantidadeAcionamento());
        Assert.assertEquals(converterStringParaDateTime("2022-01-12"),clienteDtoProdutoDtoRetorno.getProdutos().get(0).getAcionamentos().get(0).getDataAcionamento());
        Assert.assertEquals(converterStringParaDateTime("2022-03-12"),clienteDtoProdutoDtoRetorno.getProdutos().get(0).getAcionamentos().get(1).getDataAcionamento());

        Assert.assertEquals("Mais Você",clienteDtoProdutoDtoRetorno.getProdutos().get(1).getParceiro().getNome());
        Assert.assertEquals("Seguro Auto",clienteDtoProdutoDtoRetorno.getProdutos().get(1).getNome());
        Assert.assertEquals(new BigDecimal(300.00),clienteDtoProdutoDtoRetorno.getProdutos().get(1).getValor());
        Assert.assertEquals(new Integer(3),clienteDtoProdutoDtoRetorno.getProdutos().get(1).getQuantidadeAcionamento());
        Assert.assertEquals(converterStringParaDateTime("2022-03-12"),clienteDtoProdutoDtoRetorno.getProdutos().get(1).getAcionamentos().get(0).getDataAcionamento());

        assertNotNull(clienteDtoProdutoDtoRetorno);
        context.assertIsSatisfied();
    }

    private DateTime converterStringParaDateTime(String data){

        return DateTime.parse(data, DateTimeFormat.forPattern("yyyy-MM-dd"));

    }


}
