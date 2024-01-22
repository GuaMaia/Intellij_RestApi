package io.github.GuaMaia.validation;

import io.github.GuaMaia.validation.constraintvalidation.NotEmptyListValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // Vereficar em tempo de execução
@Target(ElementType.FIELD) // A ponta onde pode colocar  essa anoteicion ( Esse em algum campo)
@Constraint(validatedBy = NotEmptyListValidation.class) // vai dizer qual a classe sera utilizada
public @interface NotEmptyList {
    String message() default " A Lista não pode ser vazia";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
