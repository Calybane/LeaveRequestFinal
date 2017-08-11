package com.example.leaverequest.controller;

import com.example.leaverequest.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/roles")
public class RolesController
{
    @Autowired
    LeaveRequestService leaveRequestService;
    
    
    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllRoles()
    {
        List<String> authorities = new ArrayList<>();
        String str = "";
        try {
            for (GrantedAuthority auth : SecurityContextHolder.getContext().getAuthentication().getAuthorities())
            {
                authorities.add(auth.getAuthority());
            }
            str = "{\"username\":\"" + SecurityContextHolder.getContext().getAuthentication().getName() + "\", " +
                    "\"role\":\"" + String.join(", ", authorities) + "\"}";
        } catch (Exception ex) {
            System.out.println("Error authorities : " + ex.getMessage());
        }
        return new ResponseEntity(str, HttpStatus.OK);
    }
}
