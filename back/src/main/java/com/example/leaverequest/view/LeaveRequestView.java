package com.example.leaverequest.view;

import com.example.leaverequest.model.LeaveRequest;
import com.example.leaverequest.model.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class LeaveRequestView
{
    private Long id;
    
    private String login;
    
    private int daysLeft;
    
    private Date leaveFrom;
    
    private Date leaveTo;
    
    private Integer daysTaken;
    
    private Date requestDate;
    
    private Date approvalDate;
    
    private String status;
    
    public LeaveRequestView()
    {}
    
    public LeaveRequestView(LeaveRequest request, User user)
    {
        this.id = request.getId();
        this.login = request.getLogin();
        this.daysLeft = user.getDaysLeft();
        this.leaveFrom = request.getLeaveFrom();
        this.leaveTo = request.getLeaveTo();
        this.daysTaken = request.getDaysTaken();
        this.requestDate = request.getRequestDate();
        this.approvalDate = request.getApprovalDate();
        this.status = request.getStatus();
    }
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getLogin()
    {
        return login;
    }
    
    public void setLogin(String login)
    {
        this.login = login;
    }
    
    public int getDaysLeft()
    {
        return daysLeft;
    }
    
    public void setDaysLeft(int daysLeft)
    {
        this.daysLeft = daysLeft;
    }
    
    public Date getLeaveFrom()
    {
        return leaveFrom;
    }
    
    public void setLeaveFrom(Date leaveFrom)
    {
        this.leaveFrom = leaveFrom;
    }
    
    public Date getLeaveTo()
    {
        return leaveTo;
    }
    
    public void setLeaveTo(Date leaveTo)
    {
        this.leaveTo = leaveTo;
    }
    
    public Integer getDaysTaken()
    {
        return daysTaken;
    }
    
    public void setDaysTaken(Integer daysTaken)
    {
        this.daysTaken = daysTaken;
    }
    
    public Date getRequestDate()
    {
        return requestDate;
    }
    
    public void setRequestDate(Date requestDate)
    {
        this.requestDate = requestDate;
    }
    
    public Date getApprovalDate()
    {
        return approvalDate;
    }
    
    public void setApprovalDate(Date approvalDate)
    {
        this.approvalDate = approvalDate;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
}
