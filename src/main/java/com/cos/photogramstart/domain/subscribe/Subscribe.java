package com.cos.photogramstart.domain.subscribe;

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

import com.cos.photogramstart.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(
		uniqueConstraints = {
				@UniqueConstraint(
						name = "subscribe_uk",
						columnNames = {"fromUserId", "toUserId"}	//실제 컬럼명 
				)
		}
)
@Entity		// DB에 테이블을 생성 
public class Subscribe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// 번호 증가 전략이 DB를 따라가겠다.
	private int id;
	
	@JoinColumn(name="fromUserId")	// ORM에 의해 생성된 테이블의 컬럼의 이름을 설정.
	@ManyToOne
	private User fromUser;
	
	@JoinColumn(name="toUserId")
	@ManyToOne
	private User toUser;
	
	private LocalDateTime createDate;
	
	@PrePersist		//DB 에 insert 하기 전에 자동 실행.
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
