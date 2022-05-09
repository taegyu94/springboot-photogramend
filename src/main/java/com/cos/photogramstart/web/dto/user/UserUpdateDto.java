package com.cos.photogramstart.web.dto.user;

import javax.validation.constraints.NotBlank;

import com.cos.photogramstart.domain.user.User;

import lombok.Data;

@Data
public class UserUpdateDto {

	@NotBlank
	private String name;	//필수 
	@NotBlank
	private String password;	//필수
	private String website;
	private String bio;
	private String phone;
	private String gender;
	
	//website 아래의 필드 값들을 필수 값들이 아니다.
	// 필수값들이 아닌 값들도 엔티티로 만들게되면조금 위험하다. 수정 필요.
	public User toEntity() {
		return User.builder()
				.name(name)	//이름을 기재안했으면 db에 비어있는 name이 update 되기때문에 문제!! ->Validation 체크	
 				.password(password)		// 패스워드 기재안하면 문제!! -> Validation 체
				.website(website)
				.bio(bio)
				.phone(phone)
				.gender(gender)
				.build();
	}
}
