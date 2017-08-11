package com.example.leaverequest.model;

import com.example.leaverequest.dto.UserDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER")
public class User
{
    @Id
    @Column(nullable = false, name = "LOGIN")
    private String login;
    
    @Column(nullable = false, name = "DAYS_LEFT")
    private Integer daysLeft;
    
    
    public User(){}
    
    public User(UserDTO dto)
    {
        this.login = dto.getLogin();
        this.daysLeft = dto.getDaysLeft();
    }
    
    public String getLogin()
    {
        return login;
    }
    
    public User setLogin(String login)
    {
        this.login = login;
        return this;
    }
    
    public Integer getDaysLeft()
    {
        return daysLeft;
    }
    
    public User setDaysLeft(Integer daysLeft)
    {
        this.daysLeft = daysLeft;
        return this;
    }
    
    public boolean valid()
    {
        return this.login.trim().length() > 0 && this.daysLeft >= 0;
    }
    
    @Override
    public String toString()
    {
        return "User{" +
                "login='" + login + '\'' +
                ", daysLeft=" + daysLeft +
                '}';
    }
}
