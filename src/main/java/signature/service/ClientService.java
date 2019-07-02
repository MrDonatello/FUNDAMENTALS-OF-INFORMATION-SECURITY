package signature.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import signature.config.StorageProperties;
import signature.daoImpl.ClientDaoImpl;
import signature.dto.request.ClientDto;
import signature.dto.response.UserDtoResponse;
import signature.exceptions.ApiError;
import signature.exceptions.ErrorCode;
import signature.exceptions.ServiceException;
import signature.model.Client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {

    private final ClientDaoImpl clientDao;
    private ObjectMapper objectMapper;
    private StorageProperties storageProperties;

    @Autowired
    public ClientService(ClientDaoImpl clientDao, ObjectMapper objectMapper, StorageProperties storageProperties) {
        this.clientDao = clientDao;
        this.objectMapper = objectMapper;
        this.storageProperties = storageProperties;
    }

    public UserDtoResponse insert(ClientDto clientDto) throws ServiceException {
        Client client = objectMapper.convertValue(clientDto, Client.class);
        UserDtoResponse userDtoResponse = objectMapper.convertValue(clientDao.insert(client), UserDtoResponse.class);
        boolean createNotSignet = new File(storageProperties.getLocationNotSigned() + "/" + userDtoResponse.getId()).mkdir();
        boolean createSignet = new File(storageProperties.getLocationSigned() + "/" + userDtoResponse.getId()).mkdir();
        if(!(createNotSignet && createSignet)){
            List<ApiError> errorList = new ArrayList<>();
            ApiError apiError = new ApiError(ErrorCode.INVALID_CREATE_DIRECTORY.name(), null, ErrorCode.INVALID_CREATE_DIRECTORY.getErrorString());
            errorList.add(apiError);
            throw new ServiceException(errorList);
        }
        return userDtoResponse;
    }
}
