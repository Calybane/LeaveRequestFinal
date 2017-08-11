package com.example.leaverequest.service;

import com.example.leaverequest.dto.UserDTO;
import com.example.leaverequest.exception.EntityBadInformationsException;
import com.example.leaverequest.exception.EntityNotFoundException;
import com.example.leaverequest.model.User;
import com.example.leaverequest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    UserRepository userRepository;
    
    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public User createUser(UserDTO dto)
    {
        User user = new User(dto);
        if (user.valid()) {
            System.out.println("Creating user : " + user.toString());
            return userRepository.save(user);
        } else {
            throw new EntityBadInformationsException("User informations are incorrect");
        }
    }
    
    @Override
    @PreAuthorize("isAuthenticated()")
    public User getUserByLogin(String login)
    {
        return userRepository.findByLogin(login);
    }
    
    @Override
    @PreAuthorize("isAuthenticated()")
    public Page<User> getAllUsers(Pageable pageable)
    {
        return userRepository.findAll(pageable);
    }
    
    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public User updateUser(UserDTO dto)
    {
        User user = new User(dto);
        if(user.getDaysLeft() < 0 || user.getDaysLeft() > 999) {
            throw new EntityBadInformationsException("User's days left are incorrect : " + user.getDaysLeft() + " < 0 or > 999");
        } else if(userRepository.findByLogin(user.getLogin()) == null) {
            throw new EntityNotFoundException("User with login '" + user.getLogin() + "' not found.");
        } else {
            System.out.println("Updating user : " + user.toString());
            return userRepository.save(user);
        }
    }
    
    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HR')")
    public User addDaysUser(UserDTO dto)
    {
        if (dto.getLogin().trim().length() > 0){
            userRepository.addDays(dto.getDaysLeft(), dto.getLogin());
            return userRepository.findByLogin(dto.getLogin());
        }
        return null;
    }
}
