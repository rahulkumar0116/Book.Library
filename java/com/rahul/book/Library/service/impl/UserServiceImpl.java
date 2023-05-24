package com.rahul.book.Library.service.impl;

import com.rahul.book.Library.component.JwtToken;
import com.rahul.book.Library.entity.User;
import com.rahul.book.Library.repository.UserRepository;
import com.rahul.book.Library.service.UserOutput;
import com.rahul.book.Library.service.UserService;
import com.rahul.book.Library.service.exception.PasswordIncorrectException;
import com.rahul.book.Library.service.exception.UserExistsException;
import com.rahul.book.Library.service.exception.UserNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private JwtToken jwtToken;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserOutput registered(String username, String password, String name) {
        Optional<User> optionalUser = this.userRepository.findById(username);
        if (optionalUser.isEmpty()) {
            String encodedPassword = this.passwordEncoder.encode(password);
            User user = new User(username, name, encodedPassword);
            User saveduser = (User)this.userRepository.save(user);
            String token = this.jwtToken.generateNewToken(username);
            UserOutput userOutput = new UserOutput(username, name, token);
            return userOutput;
        } else {
            throw new UserExistsException();
        }
    }

    @Override
    public UserOutput login(String username, String password) {
        Optional<User> optionalUser = this.userRepository.findById(username);
        if (optionalUser.isPresent()) {
            this.passwordEncoder.encode(password);
            User user = (User)optionalUser.get();
            if (this.passwordEncoder.matches(password, user.getPassword())) {
                String token = this.jwtToken.generateNewToken(username);
                UserOutput userOutput = new UserOutput(username, user.getName(), token);
                return userOutput;
            } else {
                throw new PasswordIncorrectException();
            }
        } else {
            throw new UserNotExistException();
        }
    }

    @Override
    public UserOutput getUser(String token) {
        String userName = this.jwtToken.getUserFromToken(token);
        Optional<User> optionalUser = this.userRepository.findById(userName);
        User user = (User)optionalUser.get();
        UserOutput userOutput = new UserOutput(userName, user.getName(), token);
        return userOutput;
    }
}
