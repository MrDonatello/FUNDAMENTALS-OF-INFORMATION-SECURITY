
package signature.exceptions;


public enum ErrorCode  {

    INVALID_FIRST_NAME("first name field invalid"),
    INVALID_PATRONYMIC("patronymic field invalid"),
    INVALID_ADDRESS("address field is invalid"),
    INVALID_LAST_NAME("last name field is invalid"),
    INVALID_CREATE_DIRECTORY("invalid create directory"),
    INVALID_EMAIL(""),
    INVALID_LOGIN("invalid login "),
    INVALID_PHONE(""),
    INVALID_MAX_LENGTH(""),
    INVALID_MIN_LENGTH(""),
    ERROR_ADD_TO_DATABASE(""),
    DATABASE_ACCESS_ERROR("database access error"),
    USER_IS_A_NOT_CLIENT("user is not a client"),
    NO_ACCESS_PERMISSIONS("no access permissions"),
    INVALID_PUBLIC_KEY("public key fild is invalid"),
    INVALID_PASSWORD("invalid password");

    private String error;

    ErrorCode(String errorString) {

        this.error = errorString;
    }

    public String getErrorString() {

        return error;
    }
}
