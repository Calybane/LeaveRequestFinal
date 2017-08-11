package com.example.leaverequest.repository;

import com.example.leaverequest.model.HolidayDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface HolidayDateRepository extends JpaRepository<HolidayDate, Long>
{
    Page<HolidayDate> findAllByDateIsAfter(Date date, Pageable pageable);
    
    int countAllByDateBetween(@Param("start") Date start, @Param("end") Date end);
}
