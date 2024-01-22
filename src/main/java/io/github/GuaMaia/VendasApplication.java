package io.github.GuaMaia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class VendasApplication {
    /*@Bean
    public CommandLineRunner commandLineRunner(@Autowired Clientes clientes){
        return args -> {
            Cliente c = new Cliente(null,"Gustavo");
            clientes.save(c);
        };
    }*/

    public static void main(String[] args) {

        SpringApplication.run(VendasApplication.class, args);
    }
}
