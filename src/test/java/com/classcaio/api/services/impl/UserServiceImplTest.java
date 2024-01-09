package com.classcaio.api.services.impl;

import com.classcaio.api.domain.User;
import com.classcaio.api.domain.dto.UserDTO;
import com.classcaio.api.repositories.UserRepository;
import com.classcaio.api.services.exceptions.DataIntegratyViolationException;
import com.classcaio.api.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    //constants
    public static final Integer ID = 1;
    public static final String NAME = "Caio";
    public static final String EMAIL = "caio@mail.com";
    public static final String PASSWORD = "123";
    public static final String OBJECT_NOT_FOUND = "Object not found";
    public static final String E_MAIL_ALREADY_REGISTERED = "e-mail already registered";

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserRepository repository;

    @Mock
    private ModelMapper mapper;

    private User user;
    private UserDTO userDTO;
    private Optional<User> optionalUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startUser();
    }

    @Test
    void whenFindByIdThenReturnAnUserInstance() {
        //mock to return an optional user when find by id
        when(repository.findById(anyInt())).thenReturn(optionalUser);

        User response = service.findById(ID);

        assertNotNull(response);

        assertEquals(User.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(EMAIL, response.getEmail());
    }

    @Test
    void whenFindByIdThenReturnAnObjectNotFoundException() {
        //mock of an exception when find by id
        when(repository.findById(anyInt())).thenThrow(new ObjectNotFoundException(OBJECT_NOT_FOUND));

        try {
            service.findById(ID);
        } catch (Exception ex) {
            assertEquals(ObjectNotFoundException.class, ex.getClass());
            assertEquals(OBJECT_NOT_FOUND, ex.getMessage());
        }
    }

    @Test
    void whenFindAllThenReturnAListOfUsers() {
        //mock to return a list of users when find all
        when(repository.findAll()).thenReturn(List.of(user));

        List<User> response = service.findAll();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(User.class, response.get(0).getClass());

        assertEquals(ID, response.get(0).getId());
        assertEquals(NAME, response.get(0).getName());
        assertEquals(EMAIL, response.get(0).getEmail());
        assertEquals(PASSWORD, response.get(0).getPassword());
    }

    @Test
    void whenCreateThenReturnSuccess() {
        //mock to return a user when create a user
        when(repository.save(any())).thenReturn(user);

        User response = service.create(userDTO);

        assertNotNull(response);

        assertEquals(User.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(PASSWORD, response.getPassword());
    }

    @Test
    void whenCreateThenReturnAnDataIntegrityViolationException() {
        //mock to return an optional user when find by email
        when(repository.findByEmail(anyString())).thenReturn(optionalUser);

        try {
            optionalUser.get().setId(2);
            service.create(userDTO);
        } catch (Exception ex) {
            assertEquals(DataIntegratyViolationException.class, ex.getClass());
            assertEquals(E_MAIL_ALREADY_REGISTERED, ex.getMessage());
        }
    }

    @Test
    void whenUpdateThenReturnSuccess() {
        //mock to return a user when update data
        when(repository.save(any())).thenReturn(user);

        User response = service.update(userDTO);

        assertNotNull(response);

        assertEquals(User.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(PASSWORD, response.getPassword());
    }

    @Test
    void whenUpdateThenReturnAnDataIntegrityViolationException() {
        //mock to return an optional user when find by email
        when(repository.findByEmail(anyString())).thenReturn(optionalUser);

        try {
            optionalUser.get().setId(2);
            service.update(userDTO);
        } catch (Exception ex) {
            assertEquals(DataIntegratyViolationException.class, ex.getClass());
            assertEquals(E_MAIL_ALREADY_REGISTERED, ex.getMessage());
        }
    }

    @Test
    void deleteWithSuccess() {
        //mock to return an optional user when find by id
        when(repository.findById(anyInt())).thenReturn(optionalUser);
        //mock to do nothing when the delete method is called
        Mockito.doNothing().when(repository).deleteById(anyInt());
        service.delete(ID);
        //verify if the method delete will be called 1 time
        Mockito.verify(repository, Mockito.times(1)).deleteById(anyInt());
    }

    @Test
    void deleteWithObjectNotFound() {
        //mock of an exception when find by id
        when(repository.findById(anyInt())).thenThrow(new ObjectNotFoundException(OBJECT_NOT_FOUND));

        try {
            service.delete(ID);
        } catch (Exception ex) {
            assertEquals(ObjectNotFoundException.class, ex.getClass());
            assertEquals(OBJECT_NOT_FOUND, ex.getMessage());
        }
    }

    private void startUser() {
        user = new User(ID, NAME, EMAIL, PASSWORD);
        userDTO = new UserDTO(ID, NAME, EMAIL, PASSWORD);
        optionalUser = Optional.of(new User(ID, NAME, EMAIL, PASSWORD));
    }
}