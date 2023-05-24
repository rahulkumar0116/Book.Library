package com.rahul.book.Library.controller;

import com.rahul.book.Library.service.*;
import com.rahul.book.Library.service.exception.UserExistsException;
import com.rahul.book.Library.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping({"/register"})
    public ResponseEntity<Object> registerUser(@RequestBody UserInput user) {
        try {
            UserOutput userOutput = this.userService.registered(user.getUsername(), user.getPassword(), user.getName());
            return ResponseEntity.ok(userOutput);
        } catch (UserExistsException var3) {
            return ResponseEntity.status(403).body("User already exist");
        }
    }

    @GetMapping({"/login"})
    public ResponseEntity<Object> login(@RequestBody UserInputLogin user) {
        try {
            UserOutput userOutput = this.userService.login(user.getUsername(), user.getPassword());
            return ResponseEntity.ok(userOutput);
        } catch (Exception var3) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("username or password incorrect");
        }
    }

    @GetMapping({"/get-user"})
    public ResponseEntity<Object> getUser(@RequestBody UserToken user) {
        try {
            UserOutput userOutput = this.userService.getUser(user.getToken());
            return ResponseEntity.ok(userOutput);
        } catch (Exception var3) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found");
        }
    }
}
