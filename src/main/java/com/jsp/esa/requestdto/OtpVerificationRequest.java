package com.jsp.esa.requestdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpVerificationRequest {

	private String email;
	private String otp;
	
}
