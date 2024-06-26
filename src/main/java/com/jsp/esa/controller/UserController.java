package com.jsp.esa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jsp.esa.enums.UserRole;
import com.jsp.esa.requestdto.OtpVerificationRequest;
import com.jsp.esa.requestdto.UserRequest;
import com.jsp.esa.responsedto.UserResponse;
import com.jsp.esa.service.UserService;
import com.jsp.esa.util.ResponseStructure;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v3")
@AllArgsConstructor
public class UserController {

	private UserService userService;
	
	@PostMapping("/sellers/register")
    public ResponseEntity<ResponseStructure<UserResponse>> addSeller(@Valid @RequestBody UserRequest userRequest) {
        return userService.addUser(userRequest, UserRole.SELLER);
    }
	
	@PostMapping("/customers/register")
    public ResponseEntity<ResponseStructure<UserResponse>> addCustomer(@Valid @RequestBody UserRequest userRequest) {
        return userService.addUser(userRequest, UserRole.CUSTOMER);
    }
	
	@PostMapping("/users/otp")
	public ResponseEntity<ResponseStructure<UserResponse>> verifyUserEmail(@RequestBody OtpVerificationRequest otpVerificationRequest) {
		return userService.verifyUserEmail(otpVerificationRequest);
	}
	
}
