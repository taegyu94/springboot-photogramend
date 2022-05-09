package com.cos.photogramstart.handler.ex;

import java.util.Map;

public class CustomValidationException extends RuntimeException{

	// 객체를 구분할때 사용!! JVM한테 중요한
	private static final long serialVersionUID = 1L;
	
	private Map<String, String> errorMap;
	
	public CustomValidationException(String message, Map<String, String> errorMap) {
		super(message); // RuntimeException 에게 message를 보내게 되면 RuntimeException도 부모에게전달하다가 getter 함수를 호출
		this.errorMap = errorMap;
	}
	
	public Map<String, String> getErrorMap(){
		return errorMap;
	}

}
