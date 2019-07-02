package signature.validation;

import signature.exceptions.ErrorCode;
import signature.validation.validators.MinValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface MinInterface {

    String message();

    ErrorCode error();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
