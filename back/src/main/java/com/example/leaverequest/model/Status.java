package com.example.leaverequest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status
{
    WAITINGAPPROVAL("Waiting for approval"),
    APPROVED("Approved"),
    REJECTED("Rejected");
    
    private String status;
    
    private Status(String status)
    {
        setStatus(status);
    }
    
    @JsonValue
    public String getStatus()
    {
        return this.status;
    }
    
    @JsonCreator
    public void setStatus(String status)
    {
        this.status = status;
    }
}
