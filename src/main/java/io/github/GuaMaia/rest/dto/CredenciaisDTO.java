package io.github.GuaMaia.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CredenciaisDTO { // Objeto
    // propriedades
    private String login;
    private String senha;
}
