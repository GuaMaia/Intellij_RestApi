package io.github.GuaMaia.rest.controller;

import io.github.GuaMaia.domain.entity.Cliente;
import io.github.GuaMaia.domain.repository.Clientes;
import io.swagger.annotations.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


//@Controller
@RestController
@RequestMapping("/api/clientes")
@Api("Api Clientes")
/*@RequestMapping("/api/clientes")*/

public class ClienteController {
   /* @RequestMapping(value = "/hello/{nome}", method = RequestMethod.GET)
   *  public String helloCliente(@PathVariable("nome") String nomeCliente,
                                @RequestBody Cliente cliente){
        return String.format(" Hello %s ", nomeCliente);
    }
   * */
    private Clientes clientes;

    public ClienteController(Clientes clientes) {
        this.clientes = clientes;
    }

   /* @GetMapping("/api/clientes/{id}")
    @ResponseBody
    public ResponseEntity getClienteByid(@PathVariable Integer id){
        Optional<Cliente> cliente = clientes.findById(id);

        if (cliente.isPresent()){
            return ResponseEntity.ok( cliente.get());
        }

        return ResponseEntity.notFound().build();
    }*/

    @GetMapping("{id}")
    @ApiOperation("Obter detalhes de um cliente")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "Cliente encontrado"),
            @ApiResponse( code = 404 , message = "Cliente não encontrado para Id informado.")})
    public Cliente getClienteByid(@PathVariable @ApiParam("Id do Cliente") Integer id){
       return  clientes
               .findById(id)
               .orElseThrow(() ->
                       new ResponseStatusException(HttpStatus.NOT_FOUND,
                               "Cliente não encontrado "));

    }

    // post
    // Use o método POST para atualizar ou inserir um recurso.
   /* @PostMapping("/api/clientes")
    @ResponseBody

    public ResponseEntity save ( @RequestBody Cliente cliente){
        Cliente clienteSalvo = clientes.save(cliente);
        return ResponseEntity.ok(clienteSalvo);
    }*/

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Salva um novo cliente")
    @ApiResponses({
            @ApiResponse( code = 201 , message = "Cliente salvo com sucesso"),
            @ApiResponse( code = 400 , message = "Erro Validação.")})
    public Cliente save (@RequestBody @Valid Cliente cliente){
        return clientes.save(cliente);
    }

    // delete
    //O método DELETE requer o ID exclusivo do recurso.
   /* @DeleteMapping("/api/clientes/{id}")
    @ResponseBody
    public ResponseEntity delete (@PathVariable Integer id){
        Optional<Cliente> cliente = clientes.findById(id);

        if (cliente.isPresent()){
            clientes.delete( cliente.get());
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }*/
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiOperation("Deletar Cliente")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "Cliente excluído com sucesso"),
            @ApiResponse( code = 404 , message = "Id de cliente não encontrado.")})
    public void delete (@PathVariable @ApiParam("Id do Cliente") Integer id) {
        clientes.findById(id)
                .map(cliente ->{ clientes.delete(cliente);
                    return cliente;
                })
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Cliente não encontrado "));
    }


    // put (atualizar)
    //Use o método PUT para atualizar ou inserir um recurso. Uma solicitação de atualização deve
    // fornecer o ID exclusivo do recurso.
    // Para atualizar um recurso da estrutura de objeto, o ID do objeto principal é necessário.
   /* @PutMapping("/api/clientes/{id}")
    @ResponseBody
    public ResponseEntity update (@PathVariable Integer id,
                                  @RequestBody Cliente cliente) {
        return clientes
                .findById(id)
                .map( clienteExistente -> {
                    cliente.setId(clienteExistente.getId());
                    clientes.save(cliente);
                     return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }*/

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Alterar Cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente salvo com sucesso"),
            @ApiResponse(code = 404, message = "Id de cliente não encontrado."),
            @ApiResponse(code = 204, message = "Id de cliente não encontrado.")
    })
    public void update ( @PathVariable @ApiParam("Id do Cliente") Integer id,
                         @RequestBody  Cliente cliente ){
        clientes
                .findById(id)
                .map( clienteExistente ->{
                    cliente.setId(clienteExistente.getId());
                    clientes.save(cliente);
                    return clienteExistente;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente não encontrado") );
    }

    /// filtro
    /*@GetMapping("/api/clientes")
    public ResponseEntity find (Cliente filtro){
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(filtro, matcher);
        List<Cliente> lista = clientes.findAll(example);
        return ResponseEntity.ok(lista);

    }*/
    @GetMapping
    public  List<Cliente> find( Cliente filtro ) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(filtro, matcher);
        return clientes.findAll(example);
    }
}

