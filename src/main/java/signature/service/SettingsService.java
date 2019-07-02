package signature.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import signature.config.ServiceConfig;
import signature.daoImpl.UserDaoImpl;
import signature.dto.response.SettingsDtoResponse;
import signature.exceptions.ServiceException;
import signature.model.User;

@Service
public class SettingsService {

    private final SessionService sessionService;
    private final UserDaoImpl userDao;

    @Autowired
    public SettingsService(SessionService sessionService, UserDaoImpl userDao) {
        this.sessionService = sessionService;
        this.userDao = userDao;
    }

    public SettingsDtoResponse getSettings(String sessionId) {
        SettingsDtoResponse settings = new SettingsDtoResponse();
        User user;
        if (sessionId != null) {
            try {
                user = userDao.infoAccounts(sessionService.checkSessionId(sessionId));
                if (user.getRole().name().equals("ADMIN")) {
                    settings.setMaxNameLength(ServiceConfig.getConfig().getMaxNameLength());
                    settings.setMinPasswordLength(ServiceConfig.getConfig().getMinPasswordLength());
                    return settings;
                }
                if (user.getRole().name().equals("CLIENT")) {
                    settings.setMaxNameLength(ServiceConfig.getConfig().getMaxNameLength());
                    settings.setMinPasswordLength(ServiceConfig.getConfig().getMinPasswordLength());
                    return settings;
                }
            } catch (ServiceException e) {
                settings.setMaxNameLength(ServiceConfig.getConfig().getMaxNameLength());
                settings.setMinPasswordLength(ServiceConfig.getConfig().getMinPasswordLength());
                return settings;
            }
        }
        settings.setMaxNameLength(ServiceConfig.getConfig().getMaxNameLength());
        settings.setMinPasswordLength(ServiceConfig.getConfig().getMinPasswordLength());
        return settings;
    }
}
