package com.classcaio.api.services;

import com.classcaio.api.domain.User;

import java.util.List;

public interface UserService {

    User findById(Integer id);

    List<User> findAll();
}
