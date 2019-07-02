package signature.validation.validators;

import signature.config.ServerConfig;
import signature.config.ServiceConfig;
import signature.validation.MaxNameLength;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class MaxNameLengthValidator implements ConstraintValidator<MaxNameLength, String> {

    private ServerConfig serverConfig;

    @Override
    public void initialize(MaxNameLength maxNameLength) {
        serverConfig = ServiceConfig.getConfig();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return   s.length() < serverConfig.getMaxNameLength();
    }
}
