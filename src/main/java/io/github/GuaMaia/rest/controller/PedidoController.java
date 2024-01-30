package io.github.GuaMaia.rest.controller;

import io.github.GuaMaia.domain.entity.ItemPedido;
import io.github.GuaMaia.domain.entity.Pedido;
import io.github.GuaMaia.domain.entity.Produto;
import io.github.GuaMaia.domain.enums.StatusPedido;
import io.github.GuaMaia.domain.repository.Produtos;
import io.github.GuaMaia.rest.dto.InformacoesItemPedidoDTO;
import io.github.GuaMaia.rest.dto.InformacoesPedidoDTO;
import io.github.GuaMaia.rest.dto.PedidoDTO;
import io.github.GuaMaia.service.PedidoService;
import io.swagger.annotations.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import io.github.GuaMaia.rest.dto.AtualizacaoStatusPedidoDTO;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
@Api("Api Pedidos")
public class PedidoController{

    // Repositorio
    public PedidoService service;

    // Contrutor
    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Salvar Pedido")
    @ApiResponses({
            @ApiResponse( code = 201 , message = "Pedido salvo com sucesso"),
            @ApiResponse( code = 400 , message = "Erro Validação.")})
    public Integer save (@RequestBody  PedidoDTO dto){
        Pedido pedido = service.salvar(dto);
        return pedido.getId();
    }
    @GetMapping("{id}")
    @ApiOperation("Obter detalhes de um pedido")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "Pedido encontrado"),
            @ApiResponse( code = 404 , message = "Pedido não encontrado para Id informado.")})
    public InformacoesPedidoDTO getById(@PathVariable @ApiParam("Id do Pedido") Integer id){
        return service.
                obterPedidoCompleto(id)
                .map(p -> converter(p))
                .orElseThrow( () ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"Pedido não encontrado"));
    }
    private InformacoesPedidoDTO converter(Pedido pedido) {
        return InformacoesPedidoDTO
                .builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .items(converter(pedido.getItens()))
                .build();
    }

    private List<InformacoesItemPedidoDTO> converter (List<ItemPedido> itens) {
        if (CollectionUtils.isEmpty(itens)){
            return Collections.emptyList();
        }

        return itens.stream().map(
                item -> InformacoesItemPedidoDTO
                        .builder().descricaoProduto(item.getProduto().getDescricao())
                        .precoUnitario(item.getProduto().getPreco())
                        .quantidade(item.getQuantidade())
                        .build()
        ).collect(Collectors.toList());
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Obter Status do pedido")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "Status do Pedido"),
            @ApiResponse( code = 404 , message = "Pedido não encontrado para Id informado.")})
    public void updateStatus(@PathVariable  @ApiParam("Id do Pedido") Integer id,
                             @RequestBody AtualizacaoStatusPedidoDTO dto){
        String novoStatus = dto.getNovoStatus();
        service.atualizarStatus(id, StatusPedido.valueOf(novoStatus));

    }
}
