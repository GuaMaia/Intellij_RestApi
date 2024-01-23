package io.github.GuaMaia.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO { // Objeto
    //propriedades
    private String login;
    private String token;
}
