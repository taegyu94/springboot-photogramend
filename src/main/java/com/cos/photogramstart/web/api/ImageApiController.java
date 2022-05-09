package com.cos.photogramstart.web.api;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.config.auth.PrincipalDetalis;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.service.ImageService;
import com.cos.photogramstart.service.LikesService;
import com.cos.photogramstart.web.dto.CMRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ImageApiController {
	
	private final ImageService imageService;
	private final LikesService likesService;
	
	@GetMapping("/api/image")
	public ResponseEntity<?> imageStory(@AuthenticationPrincipal PrincipalDetalis principalDetalis,
			@PageableDefault(size = 3) Pageable pageable){
		Page<Image> images = imageService.이미지스토리(principalDetalis.getUser().getId(),pageable);
		return new ResponseEntity<>(new CMRespDto<>(1,"성공",images),HttpStatus.OK);	// 파라미터 1 : body데이터 , 2 : http 상태코드
	}
	
	@PostMapping("/api/image/{imageId}/likes")
	public ResponseEntity<?> likes(@PathVariable int imageId, @AuthenticationPrincipal PrincipalDetalis principalDetalis){
		likesService.좋아요(imageId,principalDetalis.getUser().getId());
		return new ResponseEntity<>(new CMRespDto<>(1, "좋아요 성공", null),HttpStatus.CREATED);
	}
	
	@DeleteMapping("/api/image/{imageId}/likes")
	public ResponseEntity<?>unLikes(@PathVariable int imageId, @AuthenticationPrincipal PrincipalDetalis principalDetalis){
		likesService.좋아요취소(imageId,principalDetalis.getUser().getId());
		return new ResponseEntity<>(new CMRespDto<>(1, "좋아요취소 성공", null),HttpStatus.OK);
	}
}
