package com.example.leaverequest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class UserDTO
{
    @NotBlank
    @Size(max = 255)
    private final String login;
    
    @Min(0)
    private final Integer daysLeft;
    
    @JsonCreator
    public UserDTO(
            @JsonProperty("login") String login,
            @JsonProperty("daysLeft") Integer daysLeft) {
        this.login = login;
        this.daysLeft = daysLeft;
    }
    
    public String getLogin()
    {
        return login;
    }
    
    public Integer getDaysLeft()
    {
        return daysLeft;
    }
    
    @Override
    public String toString()
    {
        return "UserDTO{" +
                "login='" + login + '\'' +
                ", daysLeft=" + daysLeft +
                '}';
    }
}
