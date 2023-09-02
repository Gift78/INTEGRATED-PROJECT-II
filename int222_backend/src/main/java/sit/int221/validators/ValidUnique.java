package sit.int221.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueValidator.class)
public @interface ValidUnique {
    String message() default "must be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
