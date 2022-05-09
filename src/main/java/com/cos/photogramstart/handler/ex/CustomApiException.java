package com.cos.photogramstart.handler.ex;


public class CustomApiException extends RuntimeException{

	// 객체를 구분할때 사용!! JVM한테 중요한
	private static final long serialVersionUID = 1L;
	
	public CustomApiException(String message) {
		super(message); // RuntimeException 에게 message를 보내게 되면 RuntimeException도 부모에게전달하다가 getter 함수를 호출
	}
	

}
