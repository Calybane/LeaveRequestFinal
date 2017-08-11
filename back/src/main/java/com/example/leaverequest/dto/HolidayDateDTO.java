package com.example.leaverequest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HolidayDateDTO
{
    private Long id;
    
    @NotNull
    private final Date date;
    
    private final String description;
    
    @JsonCreator
    public HolidayDateDTO(@JsonProperty("id") Long id,
                          @JsonProperty("date") Date date,
                          @JsonProperty("description") String description)
    {
        if(id != null) this.id = id;
        this.date = date;
        this.description = description;
    }
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public Date getDate()
    {
        return date;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    @Override
    public String toString()
    {
        return "HolidayDateDTO{" +
                "id=" + id +
                ", date=" + date +
                ", description='" + description + '\'' +
                '}';
    }
}
