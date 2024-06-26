package com.jsp.esa.mapper;

import org.springframework.stereotype.Component;

import com.jsp.esa.entity.User;
import com.jsp.esa.enums.UserRole;
import com.jsp.esa.requestdto.UserRequest;
import com.jsp.esa.responsedto.UserResponse;

@Component
public class UserMapper {

	public User mapToUser(UserRequest userRequest, User user) {
		user.setUsername(userRequest.getUsername());
		user.setEmail(userRequest.getEmail());
		user.setPassword((userRequest.getPassword()));
		
		return user;


	}

	public UserResponse mapToUserResponse(User user) {
		return UserResponse.builder()
				.userId(user.getUserId())
				.username(user.getUsername())
				.email(user.getEmail())
				.userRole(user.getUserRole())
				.isEmailVerified(user.isEmailVerified())
				
				.isDeleted(user.isDeleted())
				.build();

	}

	public User mapToUser(UserRequest userRequest, User user, UserRole userRole) {
        user = mapToUser(userRequest, user); // reuse existing mapping
        user.setUserRole(userRole); // set the role
        
        return user;
    }

}
