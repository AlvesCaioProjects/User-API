package com.classcaio.api.controllers;

import com.classcaio.api.domain.User;
import com.classcaio.api.domain.dto.UserDTO;
import com.classcaio.api.services.exceptions.ObjectNotFoundException;
import com.classcaio.api.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserControllerTest {

    public static final Integer ID = 1;
    public static final String NAME = "Caio";
    public static final String EMAIL = "caio@mail.com";
    public static final String PASSWORD = "123";
    public static final String OBJECT_NOT_FOUND = "Object not found";

    @InjectMocks
    private UserController controller;

    @Mock
    private UserServiceImpl service;

    @Mock
    private ModelMapper mapper;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startUser();
    }

    @Test
    void whenFindByIdThenReturnSuccess() {
        //mock for return a user when find by id
        when(service.findById(anyInt())).thenReturn(user);
        //mock for return a userDTO when try to convert an instance
        when(mapper.map(any(), any())).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = controller.findById(ID);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(UserDTO.class, response.getBody().getClass());

        assertEquals(ID, response.getBody().getId());
        assertEquals(NAME, response.getBody().getName());
        assertEquals(EMAIL, response.getBody().getEmail());
        assertEquals(PASSWORD, response.getBody().getPassword());
    }

    @Test
    void whenFindByIdThenReturnAnObjectNotFoundException() {
        //mock of an exception when find by id
        when(service.findById(anyInt())).thenThrow(new ObjectNotFoundException(OBJECT_NOT_FOUND));

        try {
            service.findById(ID);
        } catch (Exception ex) {
            assertEquals(ObjectNotFoundException.class, ex.getClass());
            assertEquals(OBJECT_NOT_FOUND, ex.getMessage());
        }
    }

    @Test
    void whenFindAllThenReturnAListOfUserDTO() {
        //mock for return a list of users
        when(service.findAll()).thenReturn(List.of(user));
        //mock for return a userDTO when try to convert an instance
        when(mapper.map(any(), any())).thenReturn(userDTO);

        ResponseEntity<List<UserDTO>> response = controller.findAll();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ArrayList.class, response.getBody().getClass());
        assertEquals(UserDTO.class, response.getBody().get(0).getClass());

        assertEquals(ID, response.getBody().get(0).getId());
        assertEquals(NAME, response.getBody().get(0).getName());
        assertEquals(EMAIL, response.getBody().get(0).getEmail());
        assertEquals(PASSWORD, response.getBody().get(0).getPassword());
    }

    @Test
    void whenCreateThenReturnSuccess() {
        //mock for return a user
        when(service.create(any())).thenReturn(user);

        ResponseEntity<UserDTO> response = controller.create(userDTO);

        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().get("Location"));
    }

    @Test
    void whenUpdateThenReturnSuccess() {
        //mock to return a user when update data
        when(service.update(userDTO)).thenReturn(user);
        //mock for return a userDTO when try to convert an instance
        when(mapper.map(any(), any())).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = controller.update(ID, userDTO);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(UserDTO.class, response.getBody().getClass());

        assertEquals(ID, response.getBody().getId());
        assertEquals(NAME, response.getBody().getName());
        assertEquals(EMAIL, response.getBody().getEmail());
    }

    @Test
    void whenDeleteThenReturnSuccess() {
        //mock to do nothing when the delete method is called
        doNothing().when(service).delete(any());

        ResponseEntity<UserDTO> response = controller.delete(ID);

        assertNotNull(response);
        assertEquals(ResponseEntity.class, response.getClass());
        //verify if the method delete will be called 1 time
        verify(service, times(1)).delete(any());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    private void startUser() {
        user = new User(ID, NAME, EMAIL, PASSWORD);
        userDTO = new UserDTO(ID, NAME, EMAIL, PASSWORD);
    }
}