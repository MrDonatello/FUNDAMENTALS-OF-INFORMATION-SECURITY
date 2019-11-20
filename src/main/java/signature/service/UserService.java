package signature.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import signature.daoImpl.UserDaoImpl;
import signature.dto.request.LoginPasswordDto;
import signature.dto.response.UserDtoResponse;
import signature.dto.response.UserDtoResponseWithType;
import signature.exceptions.ApiError;
import signature.exceptions.ErrorCode;
import signature.exceptions.ServiceException;
import signature.model.Client;
import signature.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserDaoImpl userDao;
    private ObjectMapper objectMapper;
    private User user;
    private Client client;
    private UserDtoResponse userDtoResponse;
    private UserDtoResponseWithType userDtoResponseWithType;

    @Autowired
    public UserService(UserDaoImpl userDao, ObjectMapper objectMapper, User user, Client client, UserDtoResponse userDtoResponse, UserDtoResponseWithType userDtoResponseWithType) {
        this.userDao = userDao;
        this.objectMapper = objectMapper;
        this.user = user;
        this.client = client;
        this.userDtoResponse = userDtoResponse;
        this.userDtoResponseWithType = userDtoResponseWithType;
    }

    public UserDtoResponse login(LoginPasswordDto loginPasswordDto) throws ServiceException {
        user = userDao.login(loginPasswordDto.getLogin(), loginPasswordDto.getPassword());
        if (user.getRole().name().equals("ADMIN")) {
            userDtoResponse = objectMapper.convertValue(user, UserDtoResponse.class);
        }
        if (user.getRole().name().equals("CLIENT")) {
            userDtoResponseWithType = objectMapper.convertValue(client = objectMapper.convertValue(user, Client.class), UserDtoResponseWithType.class);
            userDtoResponse = objectMapper.convertValue(userDtoResponseWithType, UserDtoResponse.class);
        }
        userDtoResponse.setUserType(user.getRole().name());
        return userDtoResponse;
    }

    public UserDtoResponse infoAccounts(int userId) throws ServiceException {
        user = userDao.infoAccounts(userId);
        if (user.getRole().name().equals("ADMIN")) {
            userDtoResponse = objectMapper.convertValue(user, UserDtoResponse.class);
        }
        if (user.getRole().name().equals("CLIENT")) {
            userDtoResponse = objectMapper.convertValue(user, UserDtoResponse.class);
        }
        return userDtoResponse;
    }

    public void checkAdmin(int userId) throws ServiceException {
        if (!userDao.infoAccounts(userId).getRole().name().equals("ADMIN")) {
            List<ApiError> apiErrorList = new ArrayList<>();
            ApiError apiError = new ApiError();
            apiError.setErrorCode(ErrorCode.NO_ACCESS_PERMISSIONS.name());
            apiError.setMessage(ErrorCode.NO_ACCESS_PERMISSIONS.getErrorString());
            apiErrorList.add(apiError);
            throw new ServiceException(apiErrorList);
        }
    }

    public void checkClient(int userId) throws ServiceException {
        if (!userDao.infoAccounts(userId).getRole().name().equals("CLIENT")) {
            List<ApiError> apiErrorList = new ArrayList<>();
            ApiError apiError = new ApiError();
            apiError.setErrorCode(ErrorCode.USER_IS_A_NOT_CLIENT.name());
            apiError.setMessage(ErrorCode.USER_IS_A_NOT_CLIENT.getErrorString());
            apiErrorList.add(apiError);
            throw new ServiceException(apiErrorList);
        }
    }
}
