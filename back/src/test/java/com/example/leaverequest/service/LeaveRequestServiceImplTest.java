package com.example.leaverequest.service;

import com.example.leaverequest.dto.LeaveRequestDTO;
import com.example.leaverequest.exception.EntityNotFoundException;
import com.example.leaverequest.model.LeaveRequest;
import com.example.leaverequest.model.Status;
import com.example.leaverequest.repository.LeaveRequestRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
@WebAppConfiguration
@TestExecutionListeners(listeners={ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class LeaveRequestServiceImplTest
{
    @Mock
    LeaveRequestRepository leaveRequestRepository;
    
    @InjectMocks
    LeaveRequestServiceImpl classUnderTest;
    
    @Test
    public void getAllLeaveRequestsViews()
    {
        //Given
        LeaveRequest leaveRequest = Mockito.mock(LeaveRequest.class);
    
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        leaveRequests.add(leaveRequest);
        Mockito.when(leaveRequestRepository.findAllByStatusNotLike(Status.REJECTED.getStatus()))
                .thenReturn(leaveRequests);
    
        //When
        List<LeaveRequest> allLeaveRequests = classUnderTest.getAllLeaveRequestsNotRejected();
    
        //Then
        assertNotEquals(allLeaveRequests.get(0).getStatus(), Status.REJECTED.getStatus());
    }
    
    @Test
    public void getAllLeaveRequestsByPersonId()
    {
        //Given
        LeaveRequest leaveRequest = Mockito.mock(LeaveRequest.class);
        
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        leaveRequests.add(leaveRequest);
        Mockito.when(leaveRequestRepository.findAllByLogin("user", new PageRequest(0, 10)))
                .thenReturn(new PageImpl<>(leaveRequests, new PageRequest(0,10), 100));
        
        //When
        Page<LeaveRequest> allLeaveRequests = classUnderTest.getAllLeaveRequestsByLogin("user", new PageRequest(0, 10));
        
        //Then
        assertEquals(allLeaveRequests.getTotalElements(), 100);
        assertEquals(allLeaveRequests.getSize(), 10);
        assertEquals(allLeaveRequests.getContent().get(0), leaveRequest);
    }
    
    @Test
    public void getAllDisabledDatesByPersonId()
    {
        //Given
        LeaveRequest leaveRequest = Mockito.mock(LeaveRequest.class);
        
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        leaveRequests.add(leaveRequest);
        
        Mockito.when(leaveRequestRepository.findAllByLoginAndLeaveFromAfter("user", new Date())).thenReturn
                (leaveRequests);
        
        //When
        List<Date> dates = classUnderTest.getAllDisabledDatesByLogin("user");
        
        //Then
        assertTrue(dates.contains(leaveRequest.getLeaveFrom()) && dates.contains(leaveRequest.getLeaveTo()));
    }
    
    @Test
    public void getAllLeaveRequestsBystatus()
    {
        //Given
        LeaveRequest leaveRequest = Mockito.mock(LeaveRequest.class);
        
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        leaveRequests.add(leaveRequest);
        Mockito.when(leaveRequestRepository.findAllByStatusLike(Status.WAITINGAPPROVAL.getStatus(), new PageRequest(0, 10)))
                .thenReturn(new PageImpl<>(leaveRequests, new PageRequest(0,10), 100));
        
        //When
        Page<LeaveRequest> allLeaveRequests = classUnderTest.getAllLeaveRequestsByStatus(Status.WAITINGAPPROVAL.getStatus(), new
                PageRequest(0, 10));
        
        //Then
        assertEquals(allLeaveRequests.getTotalElements(), 100);
        assertEquals(allLeaveRequests.getSize(), 10);
        assertEquals(allLeaveRequests.getContent().get(0), leaveRequest);
    }
    
    @Test
    public void getAllLeaveRequestsByStatusApproved()
    {
        //Given
        LeaveRequest leaveRequest = Mockito.mock(LeaveRequest.class);
        
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        leaveRequests.add(leaveRequest);
        Mockito.when(leaveRequestRepository.findAllByStatusLike(Status.APPROVED.getStatus(), new PageRequest(0, 10)))
                .thenReturn(new PageImpl<>(leaveRequests, new PageRequest(0,10), 100));
        
        //When
        Page<LeaveRequest> allLeaveRequests = classUnderTest.getAllLeaveRequestsByStatus(Status.APPROVED.getStatus(), new
                PageRequest(0, 10));
        
        //Then
        assertEquals(allLeaveRequests.getTotalElements(), 100);
        assertEquals(allLeaveRequests.getSize(), 10);
        assertEquals(allLeaveRequests.getContent().get(0), leaveRequest);
    }
    
    @Test
    public void getLeaveRequestById()
    {
        //Given
        LeaveRequest returnedLeaveRequest = Mockito.mock(LeaveRequest.class);
        Mockito.when(leaveRequestRepository.findOne(1L)).thenReturn(returnedLeaveRequest);
        
        //When
        LeaveRequest leaveRequest = classUnderTest.getLeaveRequestById(1L);
        
        //Then
        assertEquals(leaveRequest, returnedLeaveRequest);
    }
    
    @Test
    public void createLeaveRequest()
    {
        //Given
        LeaveRequest returnedLeaveRequest = Mockito.mock(LeaveRequest.class);
        LeaveRequestDTO dto = new LeaveRequestDTO(1L, "user", "Annual leave", new Date(), new Date(), 1,
                null, null, Status.WAITINGAPPROVAL.getStatus(), "vacation");
        Mockito.when(leaveRequestRepository.save(any(LeaveRequest.class))).thenReturn(returnedLeaveRequest);
        
        //When
        LeaveRequest leaveRequest = classUnderTest.createLeaveRequest(dto);
        
        //Then
        assertEquals(leaveRequest, returnedLeaveRequest);
    }
    
    @Test(expected = EntityNotFoundException.class)
    public void approveInexistingLeaveRequestByManager()
    {
        Mockito.when(leaveRequestRepository.findOne(456L)).thenReturn(null);
        classUnderTest.updateLeaveRequestApproved(456L);
    }
    
    @Test(expected = EntityNotFoundException.class)
    public void approveInexistingLeaveRequestByHR()
    {
        Mockito.when(leaveRequestRepository.findOne(456L)).thenReturn(null);
        classUnderTest.updateLeaveRequestApproved(456L);
    }
    
    @Test
    public void approveExistingLeaveRequest()
    {
        //Given
        LeaveRequest mockedLeaveRequest = Mockito.mock(LeaveRequest.class);
        Mockito.when(leaveRequestRepository.findOne(2L)).thenReturn(mockedLeaveRequest);
    
        //When
        classUnderTest.updateLeaveRequestApproved(2L);
        
        //Then
        Mockito.verify(mockedLeaveRequest).setStatus(Status.APPROVED.getStatus());
        Mockito.verify(leaveRequestRepository).save(mockedLeaveRequest);
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    public void approveSecurityTest()
    {
        //Given
        LeaveRequest mockedLeaveRequest = Mockito.mock(LeaveRequest.class);
        Mockito.when(leaveRequestRepository.findOne(1L)).thenReturn(mockedLeaveRequest);
        
        //When
        classUnderTest.updateLeaveRequestApproved(1L);
        
        //Then
        Mockito.verify(mockedLeaveRequest).setStatus(Status.APPROVED.getStatus());
        Mockito.verify(leaveRequestRepository).save(mockedLeaveRequest);
    }
    
    @Test
    public void rejectExistingLeaveRequest()
    {
        //Given
        LeaveRequest mockedLeaveRequest = Mockito.mock(LeaveRequest.class);
        LeaveRequest savedLeaveRequest = Mockito.mock(LeaveRequest.class);
        Mockito.when(leaveRequestRepository.findOne(2L)).thenReturn(mockedLeaveRequest);
        Mockito.when(leaveRequestRepository.save(mockedLeaveRequest)).thenReturn(savedLeaveRequest);
        
        //When
        LeaveRequest leaveRequest = classUnderTest.updateLeaveRequestRejected(2L);
    
        //Then
        Mockito.verify(mockedLeaveRequest).setStatus(Status.REJECTED.getStatus());
        assertEquals(savedLeaveRequest, leaveRequest);
    }
}