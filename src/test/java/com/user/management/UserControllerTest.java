package com.user.management;



 


import org.junit.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.user.management.controller.UserController;
import com.user.management.dto.UserDto;
import com.user.management.service.UserService;

import static org.mockito.Mockito.*;

 

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

 

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserControllerTest {

              @InjectMocks

              UserController controller;

 
              @Mock

              UserService userService;

              @BeforeAll

              public void setup() {
            	  	
              }

 

              @Test
              public void getAllUsers() throws IOException {

                     List<UserDto> response = new ArrayList<UserDto>();
					 when(userService.getUsers()).thenReturn((List<UserDto>) response);
					 List<UserDto> response2 = new ArrayList<UserDto>();
					 Assert.assertEquals(response, response2);

              }

 

              @Test
              public void caseSave() throws IOException {

            	  UserDto user = new UserDto();
                  user.setFullName("User Name");
                  user.setMobilePhoneNumber("082277383839");
                  user.setEmail("muhib@gmail.com");
                  user.setStatus(1);
                  user.setUsername("muhib@gmail.com");
                  user.setType(1);

                  when(userService.addUser(user)).thenReturn(user);

                  Assert.assertEquals(user, user);

              }

}