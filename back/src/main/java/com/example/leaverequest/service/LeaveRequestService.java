package com.example.leaverequest.service;

import com.example.leaverequest.dto.LeaveRequestDTO;
import com.example.leaverequest.model.LeaveRequest;
import com.example.leaverequest.view.LeaveRequestView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Date;
import java.util.List;

public interface LeaveRequestService
{
    @PreAuthorize("isAuthenticated()")
    List<LeaveRequest> getAllLeaveRequests();
    
    @PreAuthorize("isAuthenticated()")
    List<LeaveRequest> getAllLeaveRequestsNotRejected();
    
    @PreAuthorize("isAuthenticated()")
    List<LeaveRequestView> getAllLeaveRequestsView();
    
    @PreAuthorize("isAuthenticated()")
    Page<LeaveRequest> getAllLeaveRequestsByLogin(String login, Pageable pageable);
    
    @PreAuthorize("isAuthenticated()")
    List<Date> getAllDisabledDatesByLogin(String login);
    
    @PreAuthorize("isAuthenticated()")
    Page<LeaveRequest> getAllLeaveRequestsByStatus(String status, Pageable pageable);
    
    @PreAuthorize("isAuthenticated()")
    LeaveRequest getLeaveRequestById(long id);
    
    @PreAuthorize("isAuthenticated()")
    LeaveRequest createLeaveRequest(LeaveRequestDTO dto);
    
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HR')")
    LeaveRequest updateLeaveRequestApproved(long id);
    
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HR')")
    LeaveRequest updateLeaveRequestRejected(long id);
    
    @PreAuthorize("isAuthenticated()")
    boolean deleteLeaveRequest(long id);
    
    @PreAuthorize("isAuthenticated()")
    String[] getAllTypesAbsence();
}
