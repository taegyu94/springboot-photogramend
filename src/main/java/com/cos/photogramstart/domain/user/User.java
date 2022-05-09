package com.cos.photogramstart.domain.user;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.cos.photogramstart.domain.image.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity		// DB에 테이블을 생성 
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// 번호 증가 전략이 DB를 따라가겠다.
	private int id;
	
	@Column(length= 100, unique = true)
	private String username;
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String name;
	private String website;			//사이
	private String bio;					//자기소개  
	@Column(nullable = false)
	private String email;
	private String phone;
	private String gender;
	
	private String profileImageUrl;		//프로필 이미지 
	private String role;							// 권한  
	
	// 나는 연관관계의 주인이 아니다. 그러므로 테이블에 컬럼을 만들지마.
	// User를 select 할때, 해당 User id로 등록된 Image 들을 다 가져와.
	// Lazy = User 를 Select할 때 해당 User id로 등록된 Image들을 가져오지마. - 대신, getImages() 함수의 image 들이 호출되면 가져와!
	// Eager = User 를 Select할 때 해당 User id로 등록된 Image들을 전부 join해서 가져와! (무조건)
	@OneToMany(mappedBy = "user" , fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"user"})
	private List<Image> images;		// 양방향 매핑 
	
	private LocalDateTime createDate;
	
	@PrePersist		//DB 에 insert 하기 전에 자동 실행.
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", website="
				+ website + ", bio=" + bio + ", email=" + email + ", phone=" + phone + ", gender=" + gender
				+ ", profileImageUrl=" + profileImageUrl + ", role=" + role + ", createDate="
				+ createDate + "]";
	}
	
	
}
