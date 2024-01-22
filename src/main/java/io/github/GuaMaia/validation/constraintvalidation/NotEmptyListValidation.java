package io.github.GuaMaia.validation.constraintvalidation;

import io.github.GuaMaia.validation.NotEmptyList;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class NotEmptyListValidation implements
        ConstraintValidator <NotEmptyList,List>{
    @Override
    public boolean isValid(List list, ConstraintValidatorContext constraintValidatorContext){
        return list != null && !list.isEmpty();
    }

    @Override
    public void initialize(NotEmptyList constraintAnnotation) {

    }
}
