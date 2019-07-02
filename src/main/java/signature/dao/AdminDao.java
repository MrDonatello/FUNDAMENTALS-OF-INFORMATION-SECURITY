package signature.dao;


import org.springframework.stereotype.Service;
import signature.exceptions.ServiceException;
import signature.model.Admin;

@Service
public interface AdminDao {

    Admin insert(Admin admin) throws ServiceException;

}
