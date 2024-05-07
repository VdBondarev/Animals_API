package api.annotation;

import api.annotation.validator.StartsWithCapitalValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @StartsWithCapital annotation is used for validation and can be put over a String object
 * It checks whether the field is null or its first character isUpperCase()
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartsWithCapitalValidator.class)
@Documented
public @interface StartsWithCapital {
    String message() default "should start with a capital letter";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
