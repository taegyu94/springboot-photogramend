package com.cos.photogramstart.web.dto.subscribe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubscribeDto {

	private int id;							// 어떤 유저를 구독취소할지 혹은 구독할지에대한 유저ID (목적유저)
	private String username;				// 프로필페이지 유저를 구독한 유저들의 username
	private String profileImageUrl;		// 프로필페이지 유저를 구독한 유저들의 profileImageUrl
	// Integer 로 선언한 이유 : mariaDB에서 true,false 로 표현된 1,0 을 int 로 선언하면 가져오지 못함.
	private Integer subscribeState;		// 로그인한 유저가 바라보는 프로필유저를 구독한 유저들의 구독상
	private Integer equalUserState;	// 구독유저들 중 로그인한 유저인지 아닌
}
