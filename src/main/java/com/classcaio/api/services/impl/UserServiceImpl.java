package com.classcaio.api.services.impl;

import com.classcaio.api.domain.User;
import com.classcaio.api.domain.dto.UserDTO;
import com.classcaio.api.repositories.UserRepository;
import com.classcaio.api.services.UserService;
import com.classcaio.api.services.exceptions.DataIntegratyViolationException;
import com.classcaio.api.services.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public User findById(Integer id) {
        Optional<User> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Object not found"));
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User create(UserDTO obj) {
        findByEmail(obj);
        return repository.save(mapper.map(obj, User.class));
    }

    private void findByEmail(UserDTO obj) {
        Optional<User> user = repository.findByEmail(obj.getEmail());
        if(user.isPresent())
            throw new DataIntegratyViolationException("e-mail already registered");
    }
}
