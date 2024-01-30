package io.github.GuaMaia.rest.controller;

import io.github.GuaMaia.domain.entity.Usuario;
import io.github.GuaMaia.exception.SenhaInvalidaException;
import io.github.GuaMaia.rest.dto.CredenciaisDTO;
import io.github.GuaMaia.rest.dto.TokenDTO;
import io.github.GuaMaia.service.impl.UsuarioServiceImpl;
import io.github.GuaMaia.security.jwt.JwtService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Api("Api Usuários")
public class UsuarioController {

    // Construtor UsuarioServiceImpl
    private final UsuarioServiceImpl usuarioService;
    //Criptografar
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    //Salvar usuário
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar( @RequestBody @Valid Usuario usuario ){
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return usuarioService.salvar(usuario);
    }
    @PostMapping("/auth")
    public TokenDTO autenticar (@RequestBody CredenciaisDTO credenciais){
        try{
            Usuario usuario = Usuario.builder()
                    .login(credenciais.getLogin())
                    .senha(credenciais.getSenha()).build();

            UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);

            String token = jwtService.gerarToken(usuario);

            return new TokenDTO(usuario.getLogin(), token);

          } catch (UsernameNotFoundException | SenhaInvalidaException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
