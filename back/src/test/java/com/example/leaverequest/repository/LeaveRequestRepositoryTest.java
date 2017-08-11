package com.example.leaverequest.repository;

import com.example.leaverequest.LeaverequestprojectApplication;
import com.example.leaverequest.model.LeaveRequest;
import com.example.leaverequest.model.Status;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@TestExecutionListeners({DbUnitTestExecutionListener.class})
@SpringBootTest(classes = LeaverequestprojectApplication.class)
@DirtiesContext
@ActiveProfiles("test")
@DatabaseSetup("classpath:datasets/leaveRequestData.xml")
@DatabaseTearDown(type = DatabaseOperation.CLEAN_INSERT)
public class LeaveRequestRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests
{
    @Autowired
    LeaveRequestRepository leaveRequestRepository;
    
    @Test
    public void findAll() throws Exception
    {
        Page<LeaveRequest> all = leaveRequestRepository.findAll(new PageRequest(0, 10, Sort.Direction.ASC, "leaveFrom"));
        Assert.assertEquals(all.getTotalElements(), 5);
        Assert.assertTrue("First date (" + all.getContent().get(0).getLeaveFrom() + ") should be lower or equals than second ("
                + all.getContent().get(1).getLeaveFrom() + ")", all.getContent().get(0).getLeaveFrom().compareTo(all.getContent().get(1).getLeaveFrom()) <= 0);
        Assert.assertTrue("First date (" + all.getContent().get(0).getLeaveFrom() + ") should be lower or equals than second ("
                + all.getContent().get(1).getLeaveFrom() + ")", all.getContent().get(1).getLeaveFrom().compareTo(all.getContent().get(2).getLeaveFrom()) <= 0);
    }
    
    @Test
    public void findAllByPersonId() throws Exception
    {
        Page<LeaveRequest> all = leaveRequestRepository.findAllByLogin("user", new PageRequest(0, 10, Sort.Direction.ASC, "leaveFrom"));
        Assert.assertEquals(all.getTotalElements(), 1);
        Assert.assertEquals(all.getContent().get(0).getLogin(), "user");
    }
    
    @Test
    public void findAllByStatusLike() throws Exception
    {
        Page<LeaveRequest> all = leaveRequestRepository.findAllByStatusLike(Status.WAITINGAPPROVAL.getStatus(), new PageRequest(0, 10));
        Assert.assertEquals(all.getTotalElements(), 3);
        Assert.assertEquals(all.getContent().get(0).getStatus(), Status.WAITINGAPPROVAL.getStatus());
        Assert.assertEquals(all.getContent().get(1).getStatus(), Status.WAITINGAPPROVAL.getStatus());
        Assert.assertEquals(all.getContent().get(2).getStatus(), Status.WAITINGAPPROVAL.getStatus());
    }
    
}