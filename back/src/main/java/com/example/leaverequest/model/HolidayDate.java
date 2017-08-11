package com.example.leaverequest.model;

import com.example.leaverequest.dto.HolidayDateDTO;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "HOLIDAY")
public class HolidayDate
{
    @Id
    @Column(nullable = false, name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(nullable = false, name = "DATE")
    public Date date;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    public HolidayDate() {}
    
    public HolidayDate(HolidayDateDTO dto) {
        if (dto.getId() != null) this.id = dto.getId();
        this.date = dto.getDate();
        this.description = dto.getDescription();
    }
    
    
    public long getId()
    {
        return id;
    }
    
    public HolidayDate setId(long id)
    {
        this.id = id;
        return this;
    }
    
    public Date getDate()
    {
        return date;
    }
    
    public HolidayDate setDate(Date date)
    {
        this.date = date;
        return this;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public HolidayDate setDescription(String description)
    {
        this.description = description;
        return this;
    }
    
    @Override
    public String toString()
    {
        return "HolidayDate{" +
                "id=" + id +
                ", date=" + date +
                ", description='" + description + '\'' +
                '}';
    }
}
