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
public class PedidoController{

    // Repositorio
    public PedidoService service;

    // Contrutor
    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer save (@RequestBody  PedidoDTO dto){
        Pedido pedido = service.salvar(dto);
        return pedido.getId();
    }
    @GetMapping("{id}")
    public InformacoesPedidoDTO getById(@PathVariable Integer id){
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
    public void updateStatus(@PathVariable Integer id,
                             @RequestBody AtualizacaoStatusPedidoDTO dto){
        String novoStatus = dto.getNovoStatus();
        service.atualizarStatus(id, StatusPedido.valueOf(novoStatus));

    }
}
