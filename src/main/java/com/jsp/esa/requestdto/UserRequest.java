package com.jsp.esa.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

	@NotBlank(message = "Admin name should not be blank")
	@NotNull(message = "Admin name should not be null")
	@Pattern(regexp = "^[a-zA-Z]+$", message = "Name should only contain alphabetic characters")
	private String username;
	
	@Email(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "Email should end with @gmail.com")
	private String email;
	
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must be alpha-numeric, should contain at least 1 uppercase letter,1 lowercase letter,1 special character, and 1 numeric character. It must be at least 8 characters in length")
	private String password;
}
