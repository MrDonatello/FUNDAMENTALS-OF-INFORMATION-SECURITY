package signature.daoImpl;


import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import signature.dao.SessionDao;
import signature.exceptions.ApiError;
import signature.exceptions.ErrorCode;
import signature.exceptions.ServiceException;
import signature.model.Session;

import java.util.ArrayList;
import java.util.List;

@Service
public class SessionDaoImpl extends DaoImplBase implements SessionDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(Session.class);


    public Session addSession(Session session) throws ServiceException {
        LOGGER.debug("DAO addSession start");
        List<ApiError> apiErrorsList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                getSessionMapper(sqlSession).addSession(session);
            } catch (RuntimeException e) {
                LOGGER.info("Can't add session {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.ERROR_ADD_TO_DATABASE.name(), null, e.getCause().getMessage());
                apiErrorsList.add(apiError);
                throw new ServiceException(apiErrorsList);
            }
            sqlSession.commit();
        }
        return session;
    }

    public Session updateSession(Session session) throws ServiceException {
        LOGGER.debug("DAO updateSession start");
        List<ApiError> apiErrorsList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                getSessionMapper(sqlSession).updateSession(session);
            } catch (RuntimeException e) {
                LOGGER.info("Can't updateSession {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getCause().getMessage());
                apiErrorsList.add(apiError);
                throw new ServiceException(apiErrorsList);
            }
            sqlSession.commit();
        }
        return session;
    }

    public void deleteSession(String sessionId) throws ServiceException {
        LOGGER.debug("DAO deleteSession start");
        List<ApiError> apiErrorsList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                getSessionMapper(sqlSession).delete(sessionId);
            } catch (RuntimeException e) {
                LOGGER.info("Can't deleteSession {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getCause().getMessage());
                apiErrorsList.add(apiError);
                throw new ServiceException(apiErrorsList);
            }
            sqlSession.commit();
        }
    }

    public Integer checkSessionId(String sessionId) throws ServiceException {
        LOGGER.debug("DAO checkSession start");
        List<ApiError> errors = new ArrayList<>();
        Integer id;
        List<ApiError> apiErrorsList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                id = getSessionMapper(sqlSession).checkSessionId(sessionId);
                if (id == null) {
                    ApiError apiError = new ApiError(ErrorCode.NO_ACCESS_PERMISSIONS.name(), "id", ErrorCode.NO_ACCESS_PERMISSIONS.getErrorString());
                    errors.add(apiError);
                    throw new ServiceException(errors);
                }
            } catch (RuntimeException e) {
                LOGGER.info("Can't checkSession {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getCause().getMessage());
                apiErrorsList.add(apiError);
                throw new ServiceException(apiErrorsList);
            }
            sqlSession.commit();
        }
        return id;
    }
}
