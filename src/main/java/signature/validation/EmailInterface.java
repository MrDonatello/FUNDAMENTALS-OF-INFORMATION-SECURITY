package signature.validation;


import signature.exceptions.ErrorCode;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailInterface {

    String regexp() default ".*";

    String message() default "Invalid email";

    ErrorCode error();
}
