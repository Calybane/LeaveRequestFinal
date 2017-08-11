package com.example.leaverequest.service;

import com.example.leaverequest.dto.HolidayDateDTO;
import com.example.leaverequest.model.HolidayDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Date;
import java.util.List;

public interface HolidayDateService
{
    @PreAuthorize("isAuthenticated()")
    List<HolidayDate> getAllHolidayDates();
    
    @PreAuthorize("isAuthenticated()")
    Page<HolidayDate> getAllHolidayDatesAfter(Date date, Pageable pageable);
    
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HR')")
    HolidayDate createHolidayDate(HolidayDateDTO dto);
    
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HR')")
    boolean deleteHolidayDate(long id);
}
