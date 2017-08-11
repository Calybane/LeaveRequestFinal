package com.example.leaverequest.service;

import com.example.leaverequest.Utils;
import com.example.leaverequest.dto.HolidayDateDTO;
import com.example.leaverequest.exception.EntityBadInformationsException;
import com.example.leaverequest.exception.EntityNotFoundException;
import com.example.leaverequest.model.HolidayDate;
import com.example.leaverequest.repository.HolidayDateRepository;
import com.example.leaverequest.repository.LeaveRequestRepository;
import com.example.leaverequest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class HolidayDateServiceImpl implements HolidayDateService
{
    @Autowired
    HolidayDateRepository holidayDateRepository;
    
    @Autowired
    LeaveRequestRepository leaveRequestRepository;
    
    @Autowired
    UserRepository userRepository;
    
    
    @Override
    @PreAuthorize("isAuthenticated()")
    public List<HolidayDate> getAllHolidayDates()
    {
        return holidayDateRepository.findAll();
    }
    
    @Override
    @PreAuthorize("isAuthenticated()")
    public Page<HolidayDate> getAllHolidayDatesAfter(Date date, Pageable pageable)
    {
        return holidayDateRepository.findAllByDateIsAfter(date, pageable);
    }
    
    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HR')")
    public HolidayDate createHolidayDate(HolidayDateDTO dto)
    {
        HolidayDate holiday = new HolidayDate(dto).setDate(Utils.getZeroTimeDate(dto.getDate()));
        
        List<HolidayDate> holidayDates = holidayDateRepository.findAll();
        if (holidayDates.stream().anyMatch(date -> Utils.getZeroTimeDate(date.getDate()).compareTo(Utils.getZeroTimeDate(dto.getDate())) == 0)) {
            throw new EntityBadInformationsException("The holiday already exist");
        } else {
            System.out.println("Creating holiday date: " + holiday.toString());
            holiday = holidayDateRepository.save(holiday);
            userRepository.addOneDay(holiday.getDate());
            leaveRequestRepository.removeOneDayTaken(holiday.getDate());
            return holiday;
        }
    }
    
    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HR')")
    public boolean deleteHolidayDate(long id)
    {
        HolidayDate holiday = holidayDateRepository.findOne(id);
        if (holiday == null) {
            throw new EntityNotFoundException("Holiday with id " + id + " not found");
        } else {
            System.out.println("Deleting holiday : " + holiday.toString());
            holidayDateRepository.delete(id);
            userRepository.removeOneDay(holiday.getDate());
            leaveRequestRepository.addOneDayTaken(holiday.getDate());
            return holidayDateRepository.findOne(id) == null;
        }
    }
}