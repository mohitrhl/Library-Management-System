package com.OpenLeaf.service;

import com.OpenLeaf.exception.UserException;
import com.OpenLeaf.payload.dto.UserDTO;
import com.OpenLeaf.payload.response.AuthResponse;



public interface AuthService {
    AuthResponse login(String username, String password) throws UserException;
    AuthResponse signup(UserDTO req) throws UserException;

    void createPasswordResetToken(String email) throws UserException;
    void resetPassword(String token, String newPassword);
}
