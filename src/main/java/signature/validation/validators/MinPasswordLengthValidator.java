package signature.validation.validators;

import signature.config.ServerConfig;
import signature.config.ServiceConfig;
import signature.validation.MinPasswordLength;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinPasswordLengthValidator implements ConstraintValidator<MinPasswordLength, String> {


    private ServerConfig serverConfig;

    @Override
    public void initialize(MinPasswordLength constraintAnnotation) {
        serverConfig = ServiceConfig.getConfig();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.length() > serverConfig.getMinPasswordLength();
    }
}
