package com.cos.photogramstart.handler.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;

@Component	//RestController이던, Service 던  모든 것들이 Component를 상속해서 만들어져 있다.
@Aspect
public class ValidationAdvice {

	@Around("execution(* com.cos.photogramstart.web.api.*Controller.*(..))")
	public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		// ex. CommentApiController의 commentSave 메서드가 실행되려고 할때 실행되기 전에 실행.
		//proceedingJoinPoint => commentSave 함수의 모든 곳에 접근 할 수 있는 변수 
		
		Object[] args = proceedingJoinPoint.getArgs();
		for (Object arg : args) {
			if(arg instanceof BindingResult) {
				System.out.println("유효성 검사 실행.");
				BindingResult bindingResult = (BindingResult)arg;
				
				if(bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();
					
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
					}
					throw new CustomValidationApiException("유효성 검사 실패 ", errorMap);
				}
				
			}
		}
		
		
		return proceedingJoinPoint.proceed();	// 종료 후에 다시 commentSave 로 돌아가 실행.
	}
	
	@Around("execution(* com.cos.photogramstart.web.*Controller.*(..))")
	public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		Object[] args = proceedingJoinPoint.getArgs();
		for (Object arg : args) {
			if(arg instanceof BindingResult) {
				System.out.println("유효성 검사 실행.");
				BindingResult bindingResult = (BindingResult)arg;
				
				if(bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();
					
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
					}
					throw new CustomValidationException("유효성 검사 실패 ", errorMap);
				}
			}
		}
		
		return proceedingJoinPoint.proceed();
	}
}
