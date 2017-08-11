package com.example.leaverequest.model;

import com.example.leaverequest.dto.LeaveRequestDTO;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "LEAVE_REQUEST")
public class LeaveRequest
{
    @Id
    @Column(nullable = false, name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(nullable = false, name = "LOGIN")
    private String login;
    
    @Column(nullable = false, name = "TYPE_ABSENCE")
    private String typeAbsence;
    
    @Column(nullable = false, name = "LEAVE_FROM")
    private Date leaveFrom;
    
    @Column(nullable = false, name = "LEAVE_TO")
    private Date leaveTo;
    
    @Column(nullable = false, name = "DAYS_TAKEN")
    private int daysTaken;
    
    @Column(nullable = false, name = "REQUEST_DATE")
    private Date requestDate;
    
    @Column(name = "APPROVAL_DATE")
    private Date approvalDate;
    
    @Column(nullable = false, name = "STATUS")
    private String status;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    
    public LeaveRequest()
    {
    }
    
    public LeaveRequest(LeaveRequestDTO dto)
    {
        if (dto.getId() != null) this.id = dto.getId();
        this.login = dto.getLogin();
        this.typeAbsence = dto.getTypeAbsence();
        this.leaveFrom = dto.getLeaveFrom();
        this.leaveTo = dto.getLeaveTo();
        this.daysTaken = dto.getDaysTaken();
        this.requestDate = dto.getRequestDate();
        this.approvalDate = dto.getApprovalDate();
        this.status = dto.getStatus();
        this.description = dto.getDescription();
    }
    
    
    public long getId()
    {
        return id;
    }
    
    public LeaveRequest setId(long id)
    {
        this.id = id;
        return this;
    }
    
    public String getLogin()
    {
        return login;
    }
    
    public LeaveRequest setLogin(String login)
    {
        this.login = login;
        return this;
    }
    
    public String getTypeAbsence()
    {
        return typeAbsence;
    }
    
    public LeaveRequest setTypeAbsence(String typeAbsence)
    {
        this.typeAbsence = typeAbsence;
        return this;
    }
    
    public Date getLeaveFrom()
    {
        return leaveFrom;
    }
    
    public LeaveRequest setLeaveFrom(Date leaveFrom)
    {
        this.leaveFrom = leaveFrom;
        return this;
    }
    
    public Date getLeaveTo()
    {
        return leaveTo;
    }
    
    public LeaveRequest setLeaveTo(Date leaveTo)
    {
        this.leaveTo = leaveTo;
        return this;
    }
    
    public int getDaysTaken()
    {
        return daysTaken;
    }
    
    public LeaveRequest setDaysTaken(int daysTaken)
    {
        this.daysTaken = daysTaken;
        return this;
    }
    
    public Date getRequestDate()
    {
        return requestDate;
    }
    
    public LeaveRequest setRequestDate(Date requestDate)
    {
        this.requestDate = requestDate;
        return this;
    }
    
    public Date getApprovalDate()
    {
        return approvalDate;
    }
    
    public LeaveRequest setApprovalDate(Date approvalDate)
    {
        this.approvalDate = approvalDate;
        return this;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public LeaveRequest setStatus(String status)
    {
        this.status = status;
        return this;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public LeaveRequest setDescription(String description)
    {
        this.description = description;
        return this;
    }
    
    @Override
    public String toString()
    {
        return "LeaveRequest{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", typeAbsence='" + typeAbsence + '\'' +
                ", leaveFrom=" + leaveFrom +
                ", leaveTo=" + leaveTo +
                ", daysTaken=" + daysTaken +
                ", requestDate=" + requestDate +
                ", approvalDate=" + approvalDate +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
    
    public boolean valid(List<Date> dates)
    {
        if(this.login.trim().length() == 0) return false;
        if(this.leaveFrom.after(this.leaveTo)) return false;
        if(this.daysTaken <= 0) return false;
        if(!this.status.equals(Status.WAITINGAPPROVAL.getStatus())) return false;
        String[] typesAbsence = TypesAbsence.typesAbsence();
        if(Arrays.stream(typesAbsence).noneMatch(type -> type.equalsIgnoreCase(this.typeAbsence))) return false;
        if(!dates.isEmpty() && dates.contains(this.leaveFrom) || dates.contains(this.leaveTo)) return false;
        
        return true;
    }
}
