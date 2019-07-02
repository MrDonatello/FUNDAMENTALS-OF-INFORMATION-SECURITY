package signature.daoImpl;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import signature.dao.ClientDao;
import signature.exceptions.ApiError;
import signature.exceptions.ErrorCode;
import signature.exceptions.ServiceException;
import signature.model.Client;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientDaoImpl extends DaoImplBase implements ClientDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    public Client insert(Client client) throws ServiceException {
        LOGGER.debug("DAO insert Client {}", client);
        try (SqlSession sqlSession = getSession()) {
            try {
                getClientMapper(sqlSession).insertUser(client);
                getClientMapper(sqlSession).insertClient(client);
            } catch (RuntimeException e) {
                LOGGER.info("Can't insert Client {}, {}", client, e);
                sqlSession.rollback();
                List<ApiError> errorList = new ArrayList<>();
                ApiError error = new ApiError(ErrorCode.ERROR_ADD_TO_DATABASE.name(), null, e.getMessage());
                errorList.add(error);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
        return client;
    }

    public Client editClientProfile(Client client) throws ServiceException {
        LOGGER.debug("DAO Edit Client {}", client);
        try (SqlSession sqlSession = getSession()) {
            try {
                getClientMapper(sqlSession).EditProfileClient(client);
            } catch (RuntimeException e) {
                LOGGER.info("Can't Edit Client {}, {}", client, e);
                sqlSession.rollback();
                List<ApiError> errorList = new ArrayList<>();
                ApiError error = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getMessage());
                errorList.add(error);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
        return client;
    }


    public Client getClientById(int id) throws ServiceException {
        LOGGER.debug("DAO get client");
        Client client;
        try (SqlSession sqlSession = getSession()) {
            try {
                client = getUserMapper(sqlSession).infoClient(id);
                if (client == null) {
                    sqlSession.rollback();
                    List<ApiError> errorList = new ArrayList<>();
                    ApiError error = new ApiError(ErrorCode.USER_IS_A_NOT_CLIENT.name(), "id", ErrorCode.USER_IS_A_NOT_CLIENT.getErrorString());
                    errorList.add(error);
                    throw new ServiceException(errorList);
                }
            } catch (RuntimeException e) {
                LOGGER.info("Can't get client {}", e);
                sqlSession.rollback();
                List<ApiError> errorList = new ArrayList<>();
                ApiError error = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getMessage());
                errorList.add(error);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
        return client;
    }
}
