package net.adonika.gmsprt.validation;

import net.adonika.gmsprt.validation.annotation.NullOrNotBlank;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, Object> {
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {

        boolean valid = (obj == null);

        if (!valid) {
            valid = !ObjectUtils.isEmpty(obj);
        }

        return valid;
    }
}
