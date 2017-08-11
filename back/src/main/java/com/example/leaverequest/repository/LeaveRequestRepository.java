package com.example.leaverequest.repository;

import com.example.leaverequest.model.LeaveRequest;
import com.example.leaverequest.view.LeaveRequestView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long>
{
    Page<LeaveRequest> findAllByLogin(String login, Pageable pageable);
    
    Page<LeaveRequest> findAllByStatusLike(String status, Pageable pageable);
    
    List<LeaveRequest> findAllByLoginAndLeaveFromAfter(String login, Date date);
    
    List<LeaveRequest> findAllByStatusNotLike(String status);
    
    @Query(value = "SELECT LEAVE_REQUEST.ID as id, LEAVE_REQUEST.LOGIN, USER.DAYS_LEFT, LEAVE_REQUEST.REQUEST_DATE, " +
                    "LEAVE_REQUEST.APPROVAL_DATE, LEAVE_REQUEST.LEAVE_FROM, LEAVE_REQUEST.LEAVE_TO, " +
                    "LEAVE_REQUEST.DAYS_TAKEN, LEAVE_REQUEST.STATUS FROM LEAVE_REQUEST LEFT JOIN USER ON " +
                    "LEAVE_REQUEST.LOGIN = USER.LOGIN ORDER BY LEAVE_REQUEST.LOGIN, LEAVE_REQUEST.LEAVE_FROM",
                    nativeQuery = true)
    List<Object> findAllViews();
    
    @Transactional
    @Modifying
    @Query(value = "UPDATE LEAVE_REQUEST set DAYS_TAKEN = DAYS_TAKEN + 1 " +
                    "WHERE LEAVE_FROM <= :holiday and LEAVE_TO >= :holiday",
                    nativeQuery = true)
    void addOneDayTaken(@Param("holiday") Date date);
    
    @Transactional
    @Modifying
    @Query(value = "UPDATE LEAVE_REQUEST set DAYS_TAKEN = DAYS_TAKEN - 1 " +
                    "WHERE LEAVE_FROM <= :holiday and LEAVE_TO >= :holiday",
                    nativeQuery = true)
    void removeOneDayTaken(@Param("holiday") Date date);
}
