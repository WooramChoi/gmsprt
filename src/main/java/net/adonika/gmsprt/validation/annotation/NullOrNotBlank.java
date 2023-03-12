package net.adonika.gmsprt.validation.annotation;

import net.adonika.gmsprt.validation.NullOrNotBlankValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NullOrNotBlankValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NullOrNotBlank {
    String message () default "{validation.is_null_or_not_blank}";
    Class<?>[] groups () default {};
    Class<? extends Payload>[] payload () default {};
}
