package com.user.management.service;

import java.util.List;

import com.user.management.dto.UserDto;

public interface UserService {
    UserDto addUser(UserDto userDto);
    UserDto getUserByID(Long userID);
    UserDto getUserByUsername(String username);
    List<UserDto> getUsers();
    UserDto deleteUserByID(Long userID);
    UserDto editUserByID(Long userID, UserDto inputUser);
    UserDto editPasswordByUserID(Long userID, UserDto inputUser);
    byte[] getKtpFile(String urlPath);
    byte[] getProfileFile(String urlPath);
}
