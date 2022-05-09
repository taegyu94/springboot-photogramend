package com.cos.photogramstart.web.dto.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

// NotNull  : Null 체크
// NotEmpty : 빈값이거나 null체크
// NotBlank : 빈값이거나 null체크 그리고 빈 공백(스페이스) 체크 까지

@Data
public class CommentDto {

	@NotNull		// 빈 값 이거나 null 체크 그리고 빈 공백까지 
	private int imageId;
	@NotBlank		// 빈 값 이거나 null 체크 
	private String content;
	
	// toEntity 가 필요없다. 
}
