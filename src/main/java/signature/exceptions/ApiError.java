package signature.exceptions;

import org.springframework.stereotype.Component;

@Component
public class ApiError {
    private String errorCode;
    private String field;
    private String message;


    public ApiError(String errorCode, String field, String message) {
        this.errorCode = errorCode;
        this.field = field;
        this.message = message;
    }

    public ApiError() {
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
