package signature.dto.response;

import signature.exceptions.ApiError;

import java.util.List;

public class ResponseError {

    private List<ApiError> errors;

    public ResponseError(List<ApiError> errors) {
        this.errors = errors;
    }

    public List<ApiError> getErrors() {
        return errors;
    }
}
