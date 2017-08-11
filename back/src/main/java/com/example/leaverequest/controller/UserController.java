package com.example.leaverequest.controller;

import com.example.leaverequest.dto.UserDTO;
import com.example.leaverequest.model.User;
import com.example.leaverequest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/api/user")
public class UserController
{
    @Autowired
    UserService userService;
    
    
    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@Valid @RequestBody final UserDTO dto)
    {
        return new ResponseEntity(userService.createUser(dto), HttpStatus.CREATED);
    }
    
    @RequestMapping(method = GET, path = "/connected", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserConnected()
    {
        User user = null;
        try {
            String login = SecurityContextHolder.getContext().getAuthentication().getName();
            user = userService.getUserByLogin(login);
            if (user == null) // if user with the login passed not exist => created it
            {
                user = userService.createUser(new UserDTO(login, 0));
            }
        } catch (Exception ex) {
            System.out.println("User not connected");
        }
        return new ResponseEntity(user, HttpStatus.OK);
    }
    
    @RequestMapping(method = GET, path = "/username/{login}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserByLogin(@PathVariable(value = "login") final String login)
    {
        return new ResponseEntity(userService.getUserByLogin(login), HttpStatus.OK);
    }
    
    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    public Page<User> getAllUsers(final Pageable pageable)
    {
        return userService.getAllUsers(pageable);
    }
    
    @RequestMapping(method = PUT, path = "/update", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@Valid @RequestBody final UserDTO dto)
    {
        return new ResponseEntity(userService.updateUser(dto), HttpStatus.OK);
    }
    
    @RequestMapping(method = PUT, path = "/adddays", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addDaysUser(@Valid @RequestBody final UserDTO dto)
    {
        return new ResponseEntity(userService.addDaysUser(dto), HttpStatus.OK);
    }
}
