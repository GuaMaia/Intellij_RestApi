package io.github.GuaMaia.rest.controller;

import io.github.GuaMaia.domain.entity.Cliente;
import io.github.GuaMaia.domain.entity.Produto;
import io.github.GuaMaia.domain.repository.Produtos;
import io.swagger.annotations.*;
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
@Api("Api Produto")
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
    @ApiOperation("Salvando um novo produto")
    @ApiResponses({
            @ApiResponse( code = 201 , message = "Produto salvo com sucesso"),
            @ApiResponse( code = 400 , message = "Erro Validação.")})
    public Produto save (@RequestBody @ApiParam("Id do Produto") Produto produto) {
        return produtos.save(produto);
    }

    // delete
    //O método DELETE requer o ID exclusivo do recurso.
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiOperation("Deletar o por Id Produto")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "Produto deletado com sucesso"),
            @ApiResponse( code = 404 , message = "Produto não encontrado para Id informado.")})
    public void delete (@PathVariable @ApiParam("Id do Produto") Integer id) {
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
    @ApiOperation("Editar Produto")
    @ApiResponses({
            @ApiResponse( code = 201 , message = "Produto salvo com sucesso"),
            @ApiResponse( code = 400 , message = "Erro Validação.")})
    public void update (@PathVariable @ApiParam("Id do Produto") Integer id,
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
    @ApiOperation("Obter detalhes de um produto")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "Produto encontrado"),
            @ApiResponse( code = 404 , message = "Produto não encontrado para Id informado.")})
    public  Produto getProdutoById(@PathVariable   @ApiParam("Id do Produto") Integer id) {
        return produtos.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Produto não encontrado "));
    }
}
