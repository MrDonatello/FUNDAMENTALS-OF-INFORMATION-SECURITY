package signature.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import signature.daoImpl.AdminDaoImpl;
import signature.dto.request.AdminDto;
import signature.dto.response.UserDtoResponse;
import signature.exceptions.ApiError;
import signature.exceptions.ErrorCode;
import signature.exceptions.ServiceException;
import signature.model.Admin;
import signature.model.Client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class AdminService {

    private final AdminDaoImpl adminDao;
    private ObjectMapper objectMapper;

    @Autowired
    public AdminService(AdminDaoImpl adminDao, ObjectMapper objectMapper) {
        this.adminDao = adminDao;
        this.objectMapper = objectMapper;
    }

    public UserDtoResponse insert(AdminDto adminDto) throws ServiceException {
        if(adminDao.getAdminCode() != adminDto.getAdminCode()) {
            List<ApiError> errorList = new ArrayList<>();
            ApiError apiError = new ApiError(ErrorCode.NO_ACCESS_PERMISSIONS.name(), null, ErrorCode.NO_ACCESS_PERMISSIONS.getErrorString());
            errorList.add(apiError);
            throw new ServiceException(errorList);
        }
        Admin admin = objectMapper.convertValue(adminDto, Admin.class);
        return objectMapper.convertValue(adminDao.insert(admin), UserDtoResponse.class);
    }

    public List<UserDtoResponse> infoClient() throws ServiceException {
        List<Client> clientList = adminDao.getClientInfo();
        List<UserDtoResponse> userDtoResponses = new LinkedList<>();
        for (Client client : clientList) {
            userDtoResponses.add(objectMapper.convertValue(client, UserDtoResponse.class));
            userDtoResponses.get(userDtoResponses.size() - 1).setUserType(client.getRole().name());
        }
        return userDtoResponses;
    }
}
