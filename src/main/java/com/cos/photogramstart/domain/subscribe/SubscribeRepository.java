package com.cos.photogramstart.domain.subscribe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer>{

	@Modifying		// INSERT, DELETE, UPDATE 를 네이티브쿼리로 작성할때 필요한 어노테이션 
	@Query(value = "INSERT INTO subscribe(fromUserId,toUserId,createDate) VALUES(:fromUserId,:toUserId,now())", nativeQuery = true)
	void mSubscribe(int fromUserId, int toUserId);
	
	@Modifying
	@Query(value = "DELETE FROM subscribe WHERE fromUserId = :fromUserId AND toUserID=:toUserId", nativeQuery = true)
	void mUnSubscribe(int fromUserId, int toUserId);
	
	@Query(value = "SELECT COUNT(*) FROM subscribe WHERE toUserId = :pageUserId", nativeQuery = true)
	int mSubscribeCount(int pageUserId);
	
	@Query(value = "SELECT COUNT(*) FROM subscribe WHERE fromUserId = :principalUserId AND toUserId = :pageUserId", nativeQuery = true)
	int mSubscribeState(int principalUserId, int pageUserId);
}
