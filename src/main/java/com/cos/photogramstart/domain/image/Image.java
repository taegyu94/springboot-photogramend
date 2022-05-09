package com.cos.photogramstart.domain.image;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.likes.Likes;
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
@Entity
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// 번호 증가 전략이 DB를 따라가겠다.
	private int id;
	private String caption;
	private String postImageUrl;
	
	@JsonIgnoreProperties({"images"})
	@JoinColumn(name="userId")
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;
	
	// 이미지 좋아요
	// OneToMany 의 기본 fetch 전략 : Lazy  getter 함수 호출 시 가져옴.
	@JsonIgnoreProperties({"image"})		// 무한참조방지 
	@OneToMany(mappedBy = "image")	//FK의 주인이 아니에요 컬럼을 만들지 않는다.
	private List<Likes> likes;
	
	// 좋아요 상태 
	@Transient	// DB에 컬럼 생성 하지 않는다.
	private boolean likeState;
	
	@Transient	
	private int likeCount;
	
	// 댓글  
	@OrderBy("id DESC")
	@JsonIgnoreProperties({"image"})
	@OneToMany(mappedBy = "image")
	private List<Comment> comments;
	
	
	private LocalDateTime createDate;
	
	@PrePersist		//DB 에 insert 하기 전에 자동 실행.
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
