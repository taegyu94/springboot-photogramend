package com.cos.photogramstart.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cos.photogramstart.config.auth.PrincipalDetalis;
import com.cos.photogramstart.service.UserService;
import com.cos.photogramstart.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {
	
	private final UserService userService;

	@GetMapping("/user/{pageUserId}")
	public String profile(@PathVariable int pageUserId, Model model,@AuthenticationPrincipal PrincipalDetalis principalDetalis) {
		UserProfileDto dto = userService.회원프로필(pageUserId,principalDetalis.getUser().getId());
		model.addAttribute("dto", dto);
		return "user/profile";
	}
	
	@GetMapping("/user/{id}/update") // 어떤 유저의 정보를 업데이트?
	public String update(@PathVariable int id, @AuthenticationPrincipal PrincipalDetalis principalDetalis) {
		// @AuthenticationPrincipal 어노테이션 활용 
		//System.out.println("세션정보 : "+principalDetalis.getUser());
		
		// SecurityContextHolder 이용해 세션정보 찾 권장 X 
		//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//PrincipalDetalis mPrincipalDetalis = (PrincipalDetalis)auth.getPrincipal();
		//System.out.println("직접 찾은 세션정보 : " + mPrincipalDetalis.getUser());
		
		return "user/update";
	}
}
