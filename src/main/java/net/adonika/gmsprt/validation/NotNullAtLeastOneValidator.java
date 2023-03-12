package net.adonika.gmsprt.validation;

import net.adonika.gmsprt.validation.annotation.NotNullAtLeastOne;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class NotNullAtLeastOneValidator implements ConstraintValidator<NotNullAtLeastOne, Object> {

    private String[] targets;

    @Override
    public void initialize(NotNullAtLeastOne constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);

        this.targets = constraintAnnotation.targets();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {

        boolean valid = false;

        for (String target: targets) {
            if (!valid) {
                try {
                    Field field = obj.getClass().getDeclaredField(target);

                    field.setAccessible(true);
                    if (field.get(obj) != null) {
                        valid = true;
                    }
                    field.setAccessible(false);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return valid;
    }
}
