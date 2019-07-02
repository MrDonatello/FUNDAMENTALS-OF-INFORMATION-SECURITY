package signature.validation;

import signature.exceptions.ErrorCode;
import signature.validation.validators.NotNullValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotNullValidator.class)
public @interface NotNullInterface {

    String message() default "Error null field";

    ErrorCode error();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
