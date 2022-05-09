package com.cos.photogramstart.web.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.config.auth.PrincipalDetalis;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.service.SubscribeService;
import com.cos.photogramstart.web.dto.CMRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class SubscribeApiController {
	
	private final SubscribeService subscribeService;

	@PostMapping("/api/subscribe/{toUserId}")	// 로그인한 사람(principalDetalis)이 toUserId를 구독
	public ResponseEntity<?> subscribe(@AuthenticationPrincipal PrincipalDetalis principalDetalis , @PathVariable int toUserId){
		if (principalDetalis.getUser().getId() == toUserId) {
			throw new CustomApiException("자신을 구독할 수 없습니다.");
		}
		else {
			subscribeService.구독하기(principalDetalis.getUser().getId(), toUserId);
			return new ResponseEntity<>(new CMRespDto<>(1,"구독하기 성공",null), HttpStatus.OK);
		}
	}
		

	@DeleteMapping("/api/subscribe/{toUserId}")		// 로그인한 사람(principalDetalis)이 toUserId를 구독취소 
	public ResponseEntity<?> unSubscribe(@AuthenticationPrincipal PrincipalDetalis principalDetalis , @PathVariable int toUserId){
		if (principalDetalis.getUser().getId() == toUserId) {
			throw new CustomApiException("자신을 구독취소 할 수 없습니다.");
		}
		else {
			subscribeService.구독취소하기(principalDetalis.getUser().getId(), toUserId);
			return new ResponseEntity<>(new CMRespDto<>(1,"구독취소 성공",null), HttpStatus.OK);
		}

	}
}
