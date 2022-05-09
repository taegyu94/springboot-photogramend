package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final SubscribeRepository subscribeRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Value("${file.path}")	// .yml 파일에 있는 키값으로 .yml 파일의 데이터 가져오기. -> /Users/yoo/Documents/workspace/springboot/upload/
	private String uploadFolder;
	
	@Transactional(readOnly = true)
	public UserProfileDto 회원프로필(int pageUserId, int principalId) {
		//SELECT * FROM image WHERE userId=:userId;
		UserProfileDto dto = new UserProfileDto();
		
		User userEntity = userRepository.findById(pageUserId).orElseThrow(()->{
			throw new CustomException("해당 프로필 페이지는 없는 페이지입니다.");
		});
		
		dto.setUser(userEntity);
		dto.setImageCount(userEntity.getImages().size());
		dto.setPageOwnerState(principalId==pageUserId);
		
		int subscribeCount = subscribeRepository.mSubscribeCount(pageUserId);
		int subscribeState = subscribeRepository.mSubscribeState(principalId, pageUserId);
		
		dto.setSubscribeCount(subscribeCount);
		dto.setSubscribeState(subscribeState==1);
		
		userEntity.getImages().forEach((image)->{
			image.setLikeCount(image.getLikes().size());
		});

		return dto;
	}
	
	
	@Transactional	//Write (insert, update , delete) 
	public User 회원정보수정(int id, User user) {
	
		//update시 해야할 일
		// 1. 영속화 : DB에서 수정할 정보 가져오기. DB에서 값을 가져오는 순간 영속성컨텍스트에 저장하게된다.
		// 1. 무조건 찾았다. 걱정마 get(), 2. 못찾았어 Exception 발동할게 orElseThrow()
		User userEntity = userRepository.findById(id).orElseThrow(()-> {
				return new CustomValidationApiException("찾을 수 없는 id 입니다.");
		});
		
		// 2. 영속화된 오브젝트를 수정 - 수정이 발생하면 더티체킹이 일어나면서 update가 완료됨.
		userEntity.setName(user.getName());
		
		// 비밀번호 해시화
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		
		userEntity.setPassword(encPassword);
		userEntity.setWebsite(user.getWebsite());
		userEntity.setBio(user.getBio());
		userEntity.setPhone(user.getPhone());
		userEntity.setGender(user.getGender());
		
		return userEntity;
	}	// 더티체킹이 일어나면서 업데이트 완료.
	
	@Transactional
	public User 회원프로필변경(int principalId, MultipartFile profileImageFile) {
		UUID uuid = UUID.randomUUID(); 
		String imageFileName = uuid +"_"+ profileImageFile.getOriginalFilename();	// ex)uuidcode_1.jpg
		//System.out.println("이미지 파일 이름 : " + imageFileName);
		
		Path imageFilePath = Paths.get(uploadFolder+imageFileName); // -> /Users/yoo/Documents/workspace/springboot/upload/uuidcode_1.jpg
		
		// 통신, I/O -> 파일의 이름이 부정확하거나 파일이 없는경우 처럼 예외가 발생할 수 있다.
		try {
			Files.write(imageFilePath, profileImageFile.getBytes());
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
		User userEntity = userRepository.findById(principalId).orElseThrow(()->{
			throw new CustomApiException("찾을 수 없는 유저입니다.");
		});
		userEntity.setProfileImageUrl(imageFileName);
		
		return userEntity;
		// 더티체킹으로 자동으로 업데이트. 
	}
}
