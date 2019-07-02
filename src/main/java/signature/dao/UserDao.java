package signature.dao;


import org.springframework.stereotype.Service;
import signature.exceptions.ServiceException;
import signature.model.User;

@Service
public interface UserDao {

    User login(String login, String password) throws ServiceException;

    User infoAccounts(int userId) throws ServiceException;

}
