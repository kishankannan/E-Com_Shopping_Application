package com.jsp.esa.serviceimpl;

import java.util.Date;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.jsp.esa.entity.Customer;
import com.jsp.esa.entity.Seller;
import com.jsp.esa.entity.User;
import com.jsp.esa.enums.UserRole;
import com.jsp.esa.exception.OtpExpiredException;
import com.jsp.esa.exception.RegistrationSessionExpiredException;
import com.jsp.esa.exception.UserAlreadyExistException;
import com.jsp.esa.mail.MailService;
import com.jsp.esa.mapper.UserMapper;
import com.jsp.esa.repository.CustomerRepository;
import com.jsp.esa.repository.SellerRepository;
import com.jsp.esa.repository.UserRepository;
import com.jsp.esa.requestdto.OtpVerificationRequest;
import com.jsp.esa.requestdto.UserRequest;
import com.jsp.esa.responsedto.UserResponse;
import com.jsp.esa.service.UserService;
import com.jsp.esa.util.MessageData;
import com.jsp.esa.util.ResponseStructure;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

	private UserRepository userRepository;

	private CustomerRepository customerRepository;

	private SellerRepository sellerRepository;

	private UserMapper userMapper;

	private final Cache<String, User> userCache;

	private final Cache<String, String> otpCache;

	private final Random random;

	private final MailService mailService;

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> addUser(UserRequest userRequest, UserRole userRole) {
		User user = null;
		MessageData messageData = new MessageData();

		switch (userRole) {

		case SELLER -> user = new Seller();
		case CUSTOMER -> user = new Customer();
		}

		if(user != null) {
			user = userMapper.mapToUser(userRequest, user, userRole);
			user.setEmailVerified(false);
	        
			user.setDeleted(false);
		}

		int num = random.nextInt(100000, 999999);
		String otpValue = String.valueOf(num);
		userCache.put(user.getEmail(), user);
		otpCache.put(user.getEmail(), otpValue);

		messageData.setTo(user.getEmail());
		messageData.setSubject("Verify your email using otp");
		messageData.setSentDate(new Date());
		messageData.setText("Your otp "+otpValue);

		try {
			mailService.sendMail(messageData);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED)
				.body(new ResponseStructure<UserResponse>().setStatus(HttpStatus.ACCEPTED.value())
						.setMessage("User created").setData(userMapper.mapToUserResponse(user)));

	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> verifyUserEmail(OtpVerificationRequest otpVerificationRequest) {

		User user = userCache.getIfPresent(otpVerificationRequest.getEmail());
		String otp = otpCache.getIfPresent(otpVerificationRequest.getEmail());

		if(otp == null) throw new OtpExpiredException("Otp expired");
		if(user == null) throw new 	RegistrationSessionExpiredException("Session Expired");

		if (!otp.equals(otpVerificationRequest.getOtp())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ResponseStructure<UserResponse>().setStatus(HttpStatus.UNAUTHORIZED.value())
							.setMessage("Invalid OTP"));
		}	
		
		user.setEmailVerified(true);

		if (user instanceof Seller) {
	        ((Seller) user).setUserRole(UserRole.SELLER);
	        sellerRepository.save((Seller) user);
	    } else if (user instanceof Customer) {
	        ((Customer) user).setUserRole(UserRole.CUSTOMER);
	        customerRepository.save((Customer) user);
	    }

		//To remove user and otp details from cache
		userCache.invalidate(otpVerificationRequest.getEmail());
		otpCache.invalidate(otpVerificationRequest.getEmail());

		UserResponse userResponse = userMapper.mapToUserResponse(user);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseStructure<UserResponse>().setStatus(HttpStatus.OK.value())
						.setMessage("Email verified successfully").setData(userResponse));


	}

}
