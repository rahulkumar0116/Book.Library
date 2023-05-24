package com.rahul.book.Library.service;

public interface UserService {
    UserOutput registered(String username, String password, String name);

    UserOutput login(String username, String password);

    UserOutput getUser(String token);
}
