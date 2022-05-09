package com.cos.photogramstart.web.dto.user;

import com.cos.photogramstart.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserProfileDto {

	private boolean pageOwnerState;
	private int imageCount;
	private User user;
	private int subscribeCount;
	private boolean subscribeState;
	
}
