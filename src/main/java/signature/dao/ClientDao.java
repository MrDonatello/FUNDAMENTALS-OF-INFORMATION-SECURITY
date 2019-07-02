package signature.dao;

import org.springframework.stereotype.Service;
import signature.exceptions.ServiceException;
import signature.model.Client;

@Service
public interface ClientDao {

    Client insert(Client client) throws ServiceException;

    Client getClientById(int id) throws ServiceException;

}
