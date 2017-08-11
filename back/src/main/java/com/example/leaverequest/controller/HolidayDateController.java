package com.example.leaverequest.controller;

import com.example.leaverequest.dto.HolidayDateDTO;
import com.example.leaverequest.model.HolidayDate;
import com.example.leaverequest.service.HolidayDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Calendar;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/api/holiday")
public class HolidayDateController
{
    @Autowired
    HolidayDateService holidayDateService;
    
    
    @RequestMapping(method = GET, path = "/dates", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<HolidayDate> getAllHolidayDates()
    {
        return new ResponseEntity(holidayDateService.getAllHolidayDates(), HttpStatus.OK);
    }
    
    @RequestMapping(method = GET, path = "/after", produces = APPLICATION_JSON_VALUE)
    public Page<HolidayDate> getAllHolidayDates(final Pageable pageable)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, Calendar.JANUARY, 01, 0, 0, 0);
        return holidayDateService.getAllHolidayDatesAfter(calendar.getTime(), pageable);
    }
    
    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<HolidayDate> createHolidayDate(@Valid @RequestBody final HolidayDateDTO dto)
    {
        return new ResponseEntity(holidayDateService.createHolidayDate(dto), HttpStatus.CREATED);
    }
    
    @RequestMapping(method = DELETE, path = "/{id}")
    public ResponseEntity<Boolean> deleteHolidayDate(@PathVariable(value = "id") final long id)
    {
        return new ResponseEntity(holidayDateService.deleteHolidayDate(id), HttpStatus.OK);
    }
}
