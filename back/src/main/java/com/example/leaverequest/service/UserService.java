package com.example.leaverequest.service;

import com.example.leaverequest.dto.UserDTO;
import com.example.leaverequest.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface UserService
{
    @PreAuthorize("isAuthenticated()")
    User createUser(UserDTO dto);
    
    @PreAuthorize("isAuthenticated()")
    User getUserByLogin(String login);
    
    @PreAuthorize("isAuthenticated()")
    Page<User> getAllUsers(Pageable pageable);
    
    @PreAuthorize("isAuthenticated()")
    User updateUser(UserDTO dto);
    
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HR')")
    User addDaysUser(UserDTO dto);
}
