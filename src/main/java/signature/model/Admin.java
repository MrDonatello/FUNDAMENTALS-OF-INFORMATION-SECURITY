package signature.model;

import org.springframework.stereotype.Component;

@Component
public class Admin extends User {

    private String position;

    public Admin() {

    }

    public Admin(String firstName, String lastName, String patronymic, String login, String password, int id, Role role, String position, String adminCode) {
        super(firstName, lastName, patronymic, login, password, id, role);
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
