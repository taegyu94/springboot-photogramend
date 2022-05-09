package com.cos.photogramstart.web.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.config.auth.PrincipalDetalis;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.SubscribeService;
import com.cos.photogramstart.service.UserService;
import com.cos.photogramstart.web.dto.CMRespDto;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;
import com.cos.photogramstart.web.dto.user.UserUpdateDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserApiController {
	
	private final UserService userService;
	private final SubscribeService subscribeService;
	
	@GetMapping("/api/user/{pageUserId}/subscribe")
	public ResponseEntity<?> subscribeList(@PathVariable int pageUserId, @AuthenticationPrincipal PrincipalDetalis principalDetalis ){
		
		List<SubscribeDto> subscribeDto = subscribeService.구독리스트(principalDetalis.getUser().getId(),pageUserId);
		
		return new ResponseEntity<>(new CMRespDto<>(1,"구독리스트 불러오기 성공",subscribeDto), HttpStatus.OK);
	}
	
	
	
	@PutMapping("/api/user/{id}")
	public CMRespDto<?> update(
			@PathVariable int id , 
			@Valid UserUpdateDto userUdateDto,
			BindingResult bindingResult, // 꼭 @Valid 가 적혀있는 파라미터 다음에 적어야 적용됨!! 
			@AuthenticationPrincipal PrincipalDetalis principalDetalis) {
		
			User userEntity = userService.회원정보수정(id, userUdateDto.toEntity());
			principalDetalis.setUser(userEntity);	//세션 정보 수정.
			
			return new CMRespDto<>(1,"회원수정완료",userEntity);	// 응답시에 userEntity의 모든 getter 함수가 호출되고 JSON으로 파싱하여 응답한다.
		
	}
	
	@PutMapping("/api/user/{principalId}/profileImageUrl")
	public ResponseEntity<?> profileImageUrl(@PathVariable int principalId, MultipartFile profileImageFile
			,@AuthenticationPrincipal PrincipalDetalis principalDetalis){
		
		User userEntity = userService.회원프로필변경(principalId,profileImageFile);
		principalDetalis.setUser(userEntity); // 세션 변경. 
		return new ResponseEntity<>(new CMRespDto<>(1, "프로필 사진변경 성공", null),HttpStatus.OK);	
	}
}
