package com.user.management.service;

import com.user.management.dto.JwtResponse;
import com.user.management.dto.UserDto;
import com.user.management.model.Token;

public interface TokenService {
    JwtResponse save(String token, UserDto userDto, String deviceID) throws Exception;
}
