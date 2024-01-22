package io.github.GuaMaia.service.impl;

import io.github.GuaMaia.domain.entity.Cliente;
import io.github.GuaMaia.domain.entity.ItemPedido;
import io.github.GuaMaia.domain.entity.Pedido;
import io.github.GuaMaia.domain.entity.Produto;
import io.github.GuaMaia.domain.enums.StatusPedido;
import io.github.GuaMaia.domain.repository.Clientes;
import io.github.GuaMaia.domain.repository.ItemsPedido;
import io.github.GuaMaia.domain.repository.Pedidos;
import io.github.GuaMaia.domain.repository.Produtos;
import io.github.GuaMaia.exception.PedidoNaoEncontradoException;
import io.github.GuaMaia.exception.RegraNegocioException;
import io.github.GuaMaia.rest.dto.PedidoDTO;
import io.github.GuaMaia.rest.dto.ItemPedidoDTO;
import io.github.GuaMaia.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final Pedidos repository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final ItemsPedido itemsPedidoRepository;

    // Construtor
    /* public PedidoServiceImpl(Pedidos pedidos, Clientes clientes, Produtos produtos) {
        this.pedidos = pedidos;
        this.clientes = clientes;
        this.produtos = produtos;
    }*/

    // Salva o Pedido
    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientesRepository.findById(idCliente)
                .orElseThrow(() ->
                        new RuntimeException("Código de Cliente Inválido."));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItems());
        repository.save(pedido);
        itemsPedidoRepository.saveAll(itemsPedido);
        pedido.setItens(itemsPedido);

        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return repository.findByFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizarStatus(Integer id, StatusPedido statusPedido) {
        repository
                .findById(id)
                .map( pedido -> {
                    pedido.setStatus(statusPedido);
                    return repository.save(pedido);
                }).orElseThrow( () -> new PedidoNaoEncontradoException());
    }

    // Salva o item do Pedido

    private List<ItemPedido> converterItems ( Pedido pedido, List<ItemPedidoDTO> items){
        if (items.isEmpty()){
            throw new RegraNegocioException("Não é possivel realizar um pedido sem items.");
        }

        return items
                .stream()
                .map( dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtosRepository
                            .findById(idProduto)
                            .orElseThrow(
                                    () -> new RegraNegocioException(
                                            "Código de produto inválido: "+ idProduto
                                    ));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                }).collect(Collectors.toList());
        }
}
