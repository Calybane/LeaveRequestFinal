package com.example.leaverequest.controller;

import com.example.leaverequest.dto.LeaveRequestDTO;
import com.example.leaverequest.model.LeaveRequest;
import com.example.leaverequest.model.Status;
import com.example.leaverequest.service.LeaveRequestService;
import com.example.leaverequest.view.LeaveRequestView;
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
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/api/leaverequest")
public class LeaveRequestController
{
    @Autowired
    LeaveRequestService leaveRequestService;
    
    
    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<LeaveRequest> getAllLeaveRequests()
    {
        return new ResponseEntity(leaveRequestService.getAllLeaveRequests(), HttpStatus.OK);
    }
    
    @RequestMapping(method = GET, path = "/schedule",  produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<LeaveRequest> getAllLeaveRequestsSchedule()
    {
        return new ResponseEntity(leaveRequestService.getAllLeaveRequestsNotRejected(), HttpStatus.OK);
    }
    
    @RequestMapping(method = GET, path = "/views", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LeaveRequestView>> getAllLeaveRequestsViews()
    {
        return new ResponseEntity(leaveRequestService.getAllLeaveRequestsView(), HttpStatus.OK);
    }
    
    @RequestMapping(method = GET, path = "/person/{login}", produces = APPLICATION_JSON_VALUE)
    public Page<LeaveRequest> getAllLeaveRequestsByLogin(@PathVariable(value = "login") final String login, final
    Pageable pageable)
    {
        return leaveRequestService.getAllLeaveRequestsByLogin(login, pageable);
    }
    
    @RequestMapping(method = GET, path = "/person/{login}/disableddates", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Date>> getAllDisabledDatesByLogin(@PathVariable(value = "login") final String login)
    {
        return new ResponseEntity(leaveRequestService.getAllDisabledDatesByLogin(login), HttpStatus.OK);
    }
    
    @RequestMapping(method = GET, path = "/status/{status}", produces = APPLICATION_JSON_VALUE)
    public Page<LeaveRequest> getAllLeaveRequestsByStatus(@PathVariable(value = "status") final String status, final Pageable pageable)
    {
        if (status.toLowerCase().equals(Status.APPROVED.getStatus().toLowerCase())){
            return leaveRequestService.getAllLeaveRequestsByStatus(Status.APPROVED.getStatus(), pageable);
        } if (status.toLowerCase().equals(Status.REJECTED.getStatus().toLowerCase())){
            return leaveRequestService.getAllLeaveRequestsByStatus(Status.REJECTED.getStatus(), pageable);
        } else {
            return leaveRequestService.getAllLeaveRequestsByStatus(Status.WAITINGAPPROVAL.getStatus(), pageable);
        }
    }
    
    @RequestMapping(method = GET, path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<LeaveRequest> getLeaveRequestById(@PathVariable(value = "id") final long id)
    {
        return new ResponseEntity(leaveRequestService.getLeaveRequestById(id), HttpStatus.OK);
    }
    
    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<LeaveRequest> createLeaveRequest(@Valid @RequestBody final LeaveRequestDTO dto)
    {
        return new ResponseEntity(leaveRequestService.createLeaveRequest(dto), HttpStatus.CREATED);
    }
    
    @RequestMapping(method = PUT, path = "/{id}/changestatus/approved", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<LeaveRequest> updateLeaveRequestStatusApproved(@PathVariable(value = "id") final long id)
    {
        return new ResponseEntity(leaveRequestService.updateLeaveRequestApproved(id), HttpStatus.OK);
    }
    
    @RequestMapping(method = PUT, path = "/{id}/changestatus/rejected", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<LeaveRequest> updateLeaveRequestStatusRejected(@PathVariable(value = "id") final long id)
    {
        return new ResponseEntity(leaveRequestService.updateLeaveRequestRejected(id), HttpStatus.OK);
    }
    
    @RequestMapping(method = DELETE, path = "/{id}")
    public ResponseEntity<Boolean> deleteLeaveRequest(@PathVariable(value = "id") final long id)
    {
        return new ResponseEntity(leaveRequestService.deleteLeaveRequest(id), HttpStatus.OK);
    }
    
    @RequestMapping(method = GET, path = "/typesabsence", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String[]> getAllTypesAbsence()
    {
        return new ResponseEntity(leaveRequestService.getAllTypesAbsence(), HttpStatus.OK);
    }
}
