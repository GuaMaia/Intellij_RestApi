package io.github.GuaMaia.service;

import io.github.GuaMaia.domain.entity.Pedido;
import io.github.GuaMaia.domain.enums.StatusPedido;
import io.github.GuaMaia.rest.dto.PedidoDTO;

import java.util.List;
import java.util.Optional;

public interface PedidoService {
    /// médito de salvar
    Pedido salvar (PedidoDTO dto);

    // Busca o pedido completo
    Optional<Pedido> obterPedidoCompleto (Integer id);

    //Busca somente o status do pedido
    void atualizarStatus(Integer id, StatusPedido statusPedido);
}
