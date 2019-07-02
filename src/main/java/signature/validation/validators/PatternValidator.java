package signature.validation.validators;

import signature.validation.PatternInterface;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PatternValidator implements ConstraintValidator<PatternInterface, String> {

    private String regexp;

    @Override
    public void initialize(PatternInterface constraintAnnotation) {
        this.regexp = constraintAnnotation.regexp();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Pattern.matches(regexp, s);
    }
}
