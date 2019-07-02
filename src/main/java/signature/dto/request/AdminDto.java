package signature.dto.request;

import signature.exceptions.ErrorCode;
import signature.model.Role;
import signature.validation.MaxNameLength;
import signature.validation.MinPasswordLength;
import signature.validation.NotNullInterface;
import signature.validation.PatternInterface;

public class AdminDto {


    @PatternInterface(regexp = "^[А-Яа-я- ]+$", message = "the first name can contain only Russian letters, spaces and the minus sign", error = ErrorCode.INVALID_FIRST_NAME)
    @MaxNameLength
    private String firstName;

    @PatternInterface(regexp = "^[А-Яа-я- ]+$", message = "the last name can contain only Russian letters, spaces and the minus sign", error = ErrorCode.INVALID_LAST_NAME)
    @MaxNameLength
    private String lastName;

    @PatternInterface(regexp = "(^$)|(^[А-Яа-я- ]+$)", message = "the patronymic can contain only Russian letters, spaces and the minus sign", error = ErrorCode.INVALID_PATRONYMIC)
    @MaxNameLength
    private String patronymic;

    @PatternInterface(regexp = "^[A-Za-z0-9А-Яа-я]+$", message = "login contains invalid characters", error = ErrorCode.INVALID_LOGIN)
    @MaxNameLength
    @NotNullInterface(error = ErrorCode.INVALID_LOGIN, message = "login cannot be null")
    private String login;

    @MinPasswordLength
    @MaxNameLength
    @NotNullInterface(error = ErrorCode.INVALID_PASSWORD, message = "Password cannot be null")
    private String password;

    private Role role;

    @MaxNameLength()
    private String position;

    private int adminCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public AdminDto(String firstName, String lastName, String patronymic, String login, String password, String position, int adminCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.login = login;
        this.password = password;
        this.position = position;
        this.adminCode = adminCode;
        this.role = Role.ADMIN;
    }

    public AdminDto() {
        this.role = Role.ADMIN;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(int adminCode) {
        this.adminCode = adminCode;
    }
}
