
package signature.exceptions;

import java.util.List;

public class ServiceException extends Exception {

    private List<ApiError> errors;

    public ServiceException(List<ApiError> errors) {
        this.errors = errors;
    }


    public List<ApiError> getErrors() {
        return errors;
    }

}
