package ru.myorder.services;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.myorder.exceptions.UserExistsException;
import ru.myorder.exceptions.UserNotExistsException;
import ru.myorder.models.User;
import ru.myorder.repositories.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;



    public User updateAccount(String currentUsername, String updateUsername, String password) throws UserExistsException {
        if(userRepository.existsByUsername(updateUsername)){
            new UserExistsException("пользователь с таким именем уже существует");
        }
        User user = userRepository.findByUsername(currentUsername).get();
        user.setUsername(updateUsername);
        user.setPasswordHash(password);
        return userRepository.save(user);
    }

    public User getUserById(Long id){
        LOGGER.info("GET ACCOUNT BY ID");
        return userRepository.findById(id).orElseThrow(() -> new UserNotExistsException(String.format("аккаунта с id %s не существует", id)));
    }

    public List<User> getUsers(){
        LOGGER.info("GET ACCOUNTS");
        List<User> users = userRepository.findUsers();
        LOGGER.info("GETTING ACCOUNTS");
        return users;
    }

    public void updateUserById(long accountId, String username, String password, boolean isAdmin){
        User user = userRepository.findById(accountId).get();
        user.setUsername(username);
        user.setPasswordHash(password);
        user.setIsAdmin(isAdmin);
        userRepository.save(user);
    }

}
