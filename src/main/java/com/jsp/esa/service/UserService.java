package com.jsp.esa.service;

import org.springframework.http.ResponseEntity;

import com.jsp.esa.enums.UserRole;
import com.jsp.esa.requestdto.OtpVerificationRequest;
import com.jsp.esa.requestdto.UserRequest;
import com.jsp.esa.responsedto.UserResponse;
import com.jsp.esa.util.ResponseStructure;

import jakarta.validation.Valid;

public interface UserService {

	public ResponseEntity<ResponseStructure<UserResponse>> addUser(UserRequest userRequest, UserRole seller);

	public ResponseEntity<ResponseStructure<UserResponse>> verifyUserEmail(OtpVerificationRequest otpVerificationRequest);

	
}
