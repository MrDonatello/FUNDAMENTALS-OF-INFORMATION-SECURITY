package signature.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ServiceConfig {

    private static ServiceConfig serviceConfig;
    private final ServerConfig serverConfig;

    @Autowired
    public ServiceConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    @PostConstruct
    public void setConfig() {
        serviceConfig = this;
    }

    public static ServerConfig getConfig() {
        return serviceConfig.serverConfig;
    }
}
