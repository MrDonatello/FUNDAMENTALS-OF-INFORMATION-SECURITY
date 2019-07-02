package signature.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import signature.dto.response.ResponseError;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseError methodArgumentNotValidException(MethodArgumentNotValidException ex) {

        List<ApiError> errors = new ArrayList<>();
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            for (Object o : fieldError.getArguments()) {
                if (o.getClass().equals(ErrorCode.class)) {
                    ErrorCode errorCode = (ErrorCode) o;
                    errors.add(new ApiError(errorCode.name(), fieldError.getField(), fieldError.getDefaultMessage()));
                }
            }
        }
        return new ResponseError(errors);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseError SQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {

        String[] field = ex.getMessage().split("'", 4);
        List<ApiError> apiErrors = new ArrayList<>();
        ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), field[3], ex.getMessage());
        apiErrors.add(apiError);
        return new ResponseError(apiErrors);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(ServiceException.class)
    public ResponseError Exception(ServiceException ex) {
        return new ResponseError(ex.getErrors());
    }
}
