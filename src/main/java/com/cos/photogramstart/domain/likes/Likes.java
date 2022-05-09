package com.cos.photogramstart.domain.likes;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.subscribe.Subscribe;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(		// 한명의유저가 하나의 이미지를 여러번 좋아요 할 수 없다. 따라서 중복 유니크 키로 묶어서 관리. why? 1번유저가 1번 이미지를 좋아요했다는 것을 중복허용하지 않기 위해.
		uniqueConstraints = {	// 유니크 제약조건 
				@UniqueConstraint(
						name = "likes_uk",
						columnNames = {"imageId", "userId"}	//실제 컬럼명 
				)
		}
)
@Entity		// DB에 테이블을 생성 
public class Likes {	// 어떤 이미지를 누가 좋아했는가?

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// 번호 증가 전략이 DB를 따라가겠다.
	private int id;
	
	@JoinColumn(name = "imageId")
	@ManyToOne			// ManyToOne의 기본 fetch 전략 : EAGER , 반대로 OneToMany의 기본 fetch 전략 : LAZY
	private Image image;
	
	// 오류가 터지면 잡아보자.
	@JsonIgnoreProperties({"images"})
	@JoinColumn(name = "userId")
	@ManyToOne
	private User user;
	
	private LocalDateTime createDate;
	
	@PrePersist		//DB 에 insert 하기 전에 자동 실행.
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
	
	
}
