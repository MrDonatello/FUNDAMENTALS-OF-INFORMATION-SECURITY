package signature.model;

import org.springframework.stereotype.Component;

@Component
public class Client extends User {

    private String email;
    private String address;
    private String phone;
    private String publicKey;

    public Client() {
    }

    public Client(String firstName, String lastName, String patronymic, String login, String password, int id, Role role, String email, String address, String phone, String publicKey) {
        super(firstName, lastName, patronymic, login, password, id, role);
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.publicKey = publicKey;
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

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
