package signature.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import signature.daoImpl.SessionDaoImpl;
import signature.dto.response.SessionDtoResponse;
import signature.exceptions.ServiceException;
import signature.model.Session;

import javax.servlet.http.Cookie;

@Service
public class SessionService {

    private final SessionDaoImpl sessionDao;
    private final Session session;
    private SessionDtoResponse sessionDtoResponse;

    @Autowired
    public SessionService(SessionDaoImpl sessionDao, SessionDtoResponse sessionDtoResponse) {
        this.sessionDao = sessionDao;
        this.sessionDtoResponse = sessionDtoResponse;
        session = new Session();
    }

    public Cookie createNewSession(int id) throws ServiceException {
        return sessionDao.addSession(session.createSession(id)).getCookie();
    }

    public Cookie updateSession(int id) throws ServiceException {
        return sessionDao.updateSession(session.createSession(id)).getCookie();
    }

    public SessionDtoResponse deleteSession(String sessionId) throws ServiceException {
        sessionDao.deleteSession(sessionId);
        return sessionDtoResponse;
    }

    public Integer checkSessionId(String sessionId) throws ServiceException {
        return sessionDao.checkSessionId(sessionId);
    }
}
