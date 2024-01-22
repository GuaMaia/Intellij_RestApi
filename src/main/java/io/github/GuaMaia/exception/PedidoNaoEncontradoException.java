package io.github.GuaMaia.exception;
public class PedidoNaoEncontradoException extends RuntimeException {
    public PedidoNaoEncontradoException( ) {
        super("Pedido não encontrado.");
    }
}
