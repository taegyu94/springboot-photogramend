package com.cos.photogramstart.web;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.AuthService;
import com.cos.photogramstart.web.dto.auth.SignupDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor		//DI
@Controller // 1. IoC , 2. view 를 리턴하는 컨트롤
public class AuthController {
	
	
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	
	//DI
	private final AuthService authService;
//	public AuthController(AuthService authService) {
//		this.authService = authService;
//	}


	@GetMapping("/auth/signin")
	public String signInForm() {
		return "auth/signin";
	}
	
	@GetMapping("/auth/signup")
	public String signUpForm() {
		return "auth/signup";
	}
	
	@PostMapping("/auth/signup")
	public  String signUp(@Valid SignupDto signupDto , BindingResult bindingResult) {		// form 태그로 데이터 전송 - x-www-form-urlencoded
			log.info(signupDto.toString());
			
			//SignupDto -> User
			User user = signupDto.toEntity();
			log.info(user.toString());
			
			// DI 한 AuthService 를 활용해 회원가입 메서드 호출
			User userEntity = authService.회원가입(user);
			System.out.println(userEntity);
			return "auth/signin";
	}
}
