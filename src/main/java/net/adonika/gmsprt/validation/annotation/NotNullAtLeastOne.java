package net.adonika.gmsprt.validation.annotation;

import net.adonika.gmsprt.validation.NotNullAtLeastOneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = NotNullAtLeastOneValidator.class)
@Repeatable(NotNullAtLeastOnes.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNullAtLeastOne {
    String message () default "{validation.not_null_at_least_one}";
    Class<?>[] groups () default {};
    Class<? extends Payload>[] payload () default {};

    String[] targets() default {};
}
