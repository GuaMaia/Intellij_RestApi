package io.github.GuaMaia.security.jwt;

import io.github.GuaMaia.domain.entity.Usuario;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {
    // Criando variaveis
    @Value("${security.jwt.expiracao}")
    private String expiracao;
    @Value("${security.jwt.chave-assinatura}")
    private String chaveAssinatura;

    // gerar o Token
    public String gerarToken(Usuario usuario){
        // defini a hora
        long expString = Long.valueOf(expiracao);
        /// data hora      + (Somando)  minutos
        LocalDateTime dataHoraExepiracao = LocalDateTime.now().plusMinutes(expString);
        // converter objeto LocalDateTime  para date time
        Instant instant = dataHoraExepiracao.atZone(ZoneId.systemDefault()).toInstant();
        Date data = Date.from(instant);
        return Jwts
                .builder()
                .setSubject(usuario.getLogin())
                .setExpiration(data)
                .signWith(SignatureAlgorithm.HS512,chaveAssinatura)
                .compact(); // Retorna em String
    }

    // Coleta as informação do token atraves do Claims
    private Claims obterClaims( String token) throws ExpiredJwtException {
        return Jwts
                .parser()  // decodificar o token
                .setSigningKey(chaveAssinatura)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean tokenValido( String token) {
        try {
            Claims claims = obterClaims(token);
            Date dataExpiracao = claims.getExpiration();
            LocalDateTime data = dataExpiracao.toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();

            return !LocalDateTime.now().isAfter(data);
        } catch (Exception e) {
            return false;
        }
    }

    public String obterLoginUsuario(String token) throws ExpiredJwtException{
        return (String) obterClaims(token).getSubject();
    }
}
