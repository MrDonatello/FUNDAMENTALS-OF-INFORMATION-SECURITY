package signature.daoImpl;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import signature.dao.AdminDao;
import signature.exceptions.ApiError;
import signature.exceptions.ErrorCode;
import signature.exceptions.ServiceException;
import signature.model.Admin;
import signature.model.Client;

import java.util.ArrayList;
import java.util.List;


@Repository
public class AdminDaoImpl extends DaoImplBase implements AdminDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(Admin.class);

    public Admin insert(Admin admin) throws ServiceException {
        LOGGER.debug("DAO insert Administrator {}", admin);
        List<ApiError> errorList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                getAdministratorMapper(sqlSession).insertUser(admin);
                getAdministratorMapper(sqlSession).insertAdministrator(admin);
            } catch (RuntimeException e) {
                LOGGER.info("Can't insert Administrator {}, {}", admin, e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.ERROR_ADD_TO_DATABASE.name(), null, e.getCause().getMessage());
                errorList.add(apiError);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
        return admin;
    }

    public int getAdminCode() throws ServiceException {
        LOGGER.debug("DAO check Code Administrator {}");
        List<ApiError> errorList = new ArrayList<>();
        int code;
        try (SqlSession sqlSession = getSession()) {
            try {
                code = getAdministratorMapper(sqlSession).getAdminCode();
            } catch (RuntimeException e) {
                LOGGER.info("Can't check Code Administrator {}, {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.ERROR_ADD_TO_DATABASE.name(), null, e.getCause().getMessage());
                errorList.add(apiError);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
        return code;
    }

    public void setAdminCode(int code) throws ServiceException {
        LOGGER.debug("Init Code Administrator {}");
        List<ApiError> errorList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                getAdministratorMapper(sqlSession).setAdminCode(code);
            } catch (RuntimeException e) {
                LOGGER.info("Can't init Code Administrator {}, {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.ERROR_ADD_TO_DATABASE.name(), null, e.getCause().getMessage());
                errorList.add(apiError);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
    }

    public Admin editAdminProfile(Admin admin) throws ServiceException {
        LOGGER.debug("DAO Edit Administrator {}", admin);
        List<ApiError> errorList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                getAdministratorMapper(sqlSession).editProfileAdmin(admin);
            } catch (RuntimeException e) {
                LOGGER.info("Can't Edit Administrator {}, {}", admin, e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getCause().getMessage());
                errorList.add(apiError);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
        return admin;
    }

    public List<Client> getClientInfo() throws ServiceException {
        LOGGER.debug("DAO get client Info");
        List<ApiError> errorList = new ArrayList<>();
        List<Client> clientList;
        try (SqlSession sqlSession = getSession()) {
            try {
                clientList = getAdministratorMapper(sqlSession).getClientInfo();
            } catch (RuntimeException e) {
                LOGGER.info("Can't get client Info", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getCause().getMessage());
                errorList.add(apiError);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
        return clientList;
    }
}
