package signature.dao;


import org.springframework.stereotype.Service;
import signature.exceptions.ServiceException;
import signature.model.Session;

@Service
public interface SessionDao {

    Session addSession(Session session) throws ServiceException;

    Session updateSession(Session session) throws ServiceException;

    void deleteSession(String sessionId) throws ServiceException;

    Integer checkSessionId(String sessionId) throws ServiceException;
}
