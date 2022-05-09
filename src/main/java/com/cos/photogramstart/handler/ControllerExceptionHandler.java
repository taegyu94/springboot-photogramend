package com.cos.photogramstart.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.util.Script;
import com.cos.photogramstart.web.dto.CMRespDto;

@RestController
@ControllerAdvice	//모든 예외가 발생하면 가로채준다.
public class ControllerExceptionHandler {

	@ExceptionHandler(CustomValidationException.class)
	public String validationException(CustomValidationException e) {
		//CMRespDto, Script 비교
		//1. 클라이언트에게 응답할때는 Script가 좋음. 
		//2. Ajax통신 (개발자에게 응답) - CMRespDto 가 좋음
		//3. 안드로이드 통신 (개발자에게 응답) - CMRespDto 가 좋
		
		// 방법1
		//return new CMRespDto<Map<String,String>>(-1,e.getMessage(),e.getErrorMap());
		
		//방법2
		//return Script.back(e.getErrorMap().toString());
		
		if(e.getErrorMap() == null) {
			return Script.back(e.getMessage());
		}
		else {
			return Script.back(e.getErrorMap().toString());
		}
	}
	
	@ExceptionHandler(CustomException.class)
	public String validationException(CustomException e) {
		
		return Script.back(e.getMessage());
		
	}
	
	@ExceptionHandler(CustomValidationApiException.class)
	public ResponseEntity<?> validationApiException(CustomValidationApiException e) {
		// 방법1
		return new ResponseEntity<>(new CMRespDto<>(-1,e.getMessage(),e.getErrorMap()), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CustomApiException.class)
	public ResponseEntity<?> validationApiException(CustomApiException e) {
		// 방법1
		return new ResponseEntity<>(new CMRespDto<>(-1,e.getMessage(),null), HttpStatus.BAD_REQUEST);
	}
}
