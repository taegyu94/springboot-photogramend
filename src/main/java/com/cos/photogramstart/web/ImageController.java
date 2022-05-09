package com.cos.photogramstart.web;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.photogramstart.config.auth.PrincipalDetalis;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.ImageService;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller	//view 
public class ImageController {
	
	private final ImageService imageService;

	@GetMapping({"/", "/image/story"})
	public String story() {
		return "image/story";
	}
	
	@GetMapping("/image/popular")
	public String popular(Model model) {
		
		List<Image> images = imageService.인기사진();
		model.addAttribute("images", images);
		return "image/popular";
	}
	
	@GetMapping("/image/upload")
	public String upload() {
		return "image/upload";
	}
	
	@PostMapping("/image")
	public String imageUpload(ImageUploadDto imageUploadDto, @AuthenticationPrincipal PrincipalDetalis principalDetalis) {
		
		if(imageUploadDto.getFile().isEmpty()) {
			throw new CustomValidationException("이미지가 첨부되지 않았습니다.", null);	//페이지를 리턴해줄것이기 때문에 CustomValidationException
		}
		else {
			// 서비스 호출.
			imageService.사진업로드(imageUploadDto, principalDetalis);
			
			return "redirect:/user/"+principalDetalis.getUser().getId(); // 이미지 업로드가 완료되면 "/user/{id}" 페이지로 돌아간다.
		}
	}
}
