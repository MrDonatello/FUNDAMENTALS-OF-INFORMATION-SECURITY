package signature.dto.request;

import signature.exceptions.ErrorCode;
import signature.model.Role;
import signature.validation.*;


public class ClientDto {

    @PatternInterface(regexp = "^[А-Яа-я- ]+$", message = "the first name can contain only Russian letters, spaces and the minus sign", error = ErrorCode.INVALID_FIRST_NAME)
    @MaxNameLength
    private String firstName;

    @PatternInterface(regexp = "^[А-Яа-я- ]+$", message = "the last name can contain only Russian letters, spaces and the minus sign", error = ErrorCode.INVALID_LAST_NAME)
    @MaxNameLength
    private String lastName;

    @PatternInterface(regexp = "(^$)|(^[А-Яа-я- ]+$)", message = "the patronymic can contain only Russian letters, spaces and the minus sign", error = ErrorCode.INVALID_PATRONYMIC)
    @MaxNameLength
    private String patronymic;

    @EmailInterface(message = "Email should be valid", error = ErrorCode.INVALID_EMAIL)
    private String email;

    @MaxNameLength
    @NotNullInterface(message = "Address cannot be null", error = ErrorCode.INVALID_ADDRESS)
    private String address;

    @PatternInterface(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{10,15}$", message = "invalid phone", error = ErrorCode.INVALID_PHONE)
    private String phone;

    @PatternInterface(regexp = "^[A-Za-z0-9А-Яа-я]+$", message = "login contains invalid characters", error = ErrorCode.INVALID_LOGIN)
    @MaxNameLength
    @NotNullInterface(message = "Login cannot be null", error = ErrorCode.INVALID_LOGIN)
    private String login;

    @MinPasswordLength
    @MaxNameLength
    @NotNullInterface(message = "Password cannot be null", error = ErrorCode.INVALID_PASSWORD)
    private String password;

    private Role role;
    private int id;

    public ClientDto(String firstName, String lastName, String patronymic, String email, String address, String phone, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.login = login;
        this.password = password;
        this.role = Role.CLIENT;
    }

    public ClientDto() {
        this.role = Role.CLIENT;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
