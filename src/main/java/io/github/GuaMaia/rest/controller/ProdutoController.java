package io.github.GuaMaia.rest.controller;

import io.github.GuaMaia.domain.entity.Cliente;
import io.github.GuaMaia.domain.entity.Produto;
import io.github.GuaMaia.domain.repository.Produtos;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
// sãoos edPonit
@RestController
@RequestMapping("/api/produto")
public class ProdutoController {

    // Repositorio
    private Produtos produtos;

    public ProdutoController(Produtos produtos) {
        this.produtos = produtos;
    }

    // post
    // Use o método POST para atualizar ou inserir um recurso.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto save (@RequestBody  Produto produto) {
        return produtos.save(produto);
    }

    // delete
    //O método DELETE requer o ID exclusivo do recurso.
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void delete (@PathVariable Integer id) {
        produtos.findById(id)
                .map( produto -> {
                    produtos.delete(produto);
                    return produto;
                }).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Produto não encontrado "));
    }

    // put (atualizar)
    //Use o método PUT para atualizar ou inserir um recurso. Uma solicitação de atualização deve
    // fornecer o ID exclusivo do recurso.
    // Para atualizar um recurso da estrutura de objeto, o ID do objeto principal é necessário.
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update (@PathVariable Integer id,
                        @RequestBody @Valid Produto produto){
        produtos.findById(id)
                .map( produtoExiste -> {
                    produto.setId(produtoExiste.getId());
                    produtos.save(produto);
                    return produtoExiste;

                }).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Produto não encontrado "));
    }

    /// filtro
    @GetMapping
    public List<Produtos> find ( Produto filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(filtro, matcher);
        return produtos.findAll(example);
    }

    // Comando Get
    @GetMapping("{id}")
    public  Produto getProdutoById(@PathVariable Integer id) {
        return produtos.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Produto não encontrado "));
    }
}
