package signature.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServerConfig {

    @Value("${max_name_length}")
    private int maxNameLength;

    @Value("${min_password_length}")
    private int minPasswordLength;

    @Value("${rest_http_port}")
    private int serverHttpPort;

    @Value("${admin_code}")
    private int adminCode;

    public ServerConfig() {
    }

    public int getMaxNameLength() {
        return maxNameLength;
    }

    public void setMaxNameLength(int maxNameLength) {
        this.maxNameLength = maxNameLength;
    }

    public int getMinPasswordLength() {
        return minPasswordLength;
    }

    public void setMinPasswordLength(int minPasswordLength) {
        this.minPasswordLength = minPasswordLength;
    }

    public int getServerHttpPort() {
        return serverHttpPort;
    }

    public void setServerHttpPort(int serverHttpPort) {
        this.serverHttpPort = serverHttpPort;
    }

    public int getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(int adminCode) {
        this.adminCode = adminCode;
    }
}
