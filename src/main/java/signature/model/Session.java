package signature.model;

import javax.servlet.http.Cookie;
import java.util.UUID;

public class Session {

    private int userId;
    private Cookie cookie;


    public Session createSession(int userId) {
        UUID uuid = UUID.randomUUID();
        this.cookie = new Cookie("JAVASESSIONID", uuid.toString());
        this.cookie.setMaxAge(-1);
        this.userId = userId;
        return this;
    }

    public Session() {
    }

    public Cookie getCookie() {
        return cookie;
    }

    public int getUserId() {
        return userId;
    }
}
