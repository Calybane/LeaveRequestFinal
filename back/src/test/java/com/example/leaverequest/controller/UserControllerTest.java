package com.example.leaverequest.controller;

import com.example.leaverequest.dto.UserDTO;
import com.example.leaverequest.model.User;
import com.example.leaverequest.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableSpringDataWebSupport
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners(listeners={ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class UserControllerTest extends ControllerTest
{
    private MockMvc mockMvc;
    
    @Mock
    UserService userService;
    
    @InjectMocks
    UserController classUnderTest;
    
    @InjectMocks
    PageableHandlerMethodArgumentResolver pageableArgumentResolver;
    
    @Captor
    ArgumentCaptor<UserDTO> dtoCaptor;
    
    @Before
    public void setUpControllerTestSupport()
    {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(classUnderTest)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .build();
    }
    
    @Test
    public void createUser() throws Exception
    {
        UserDTO dto = new UserDTO("user", 5);
    
        mockMvc.perform(post("/api/user")
                .accept(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    
        Mockito.verify(userService).createUser(dtoCaptor.capture());
        Mockito.verifyNoMoreInteractions(userService);
    
        UserDTO value = dtoCaptor.getValue();
        assertEquals(value.getLogin(), "user");
    }
    
    @Test
    public void getUserConnected() throws Exception
    {
        // TODO
    }
    
    @Test
    public void getUserByLogin() throws Exception
    {
        //Given
        User user = new User().setLogin("user").setDaysLeft(5);
        Mockito.when(userService.getUserByLogin("user")).thenReturn(user);
    
        mockMvc.perform(get("/api/user/username/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login", is("user")));
    }
    
    @Test
    public void getAllUsers() throws Exception
    {
        //Given
        List<User> users = new ArrayList<>();
        users.add(new User().setLogin("user").setDaysLeft(5));
        Mockito.when(userService.getAllUsers(new PageRequest(0,10)))
                .thenReturn(new PageImpl<User>(users, new PageRequest(0,10), 100));
    
        mockMvc.perform(get("/api/user").param("page","0").param("size","10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content.size()", is(1)))
                .andExpect(jsonPath("size", is(10)))
                .andExpect(jsonPath("content[0].daysLeft", is(5)))
                .andExpect(jsonPath("content[0].login", is("user")));
    }
    
    @Test
    public void updateUser() throws Exception
    {
        // TODO
    }
    
}