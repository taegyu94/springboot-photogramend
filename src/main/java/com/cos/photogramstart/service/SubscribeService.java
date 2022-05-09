package com.cos.photogramstart.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubscribeService {

	private final SubscribeRepository subscribeRepository;
	private final EntityManager em;	// Repository는 EntityManager를 구해서 만들어져 있는 구현체
	
	@Transactional(readOnly = true)
	public List<SubscribeDto> 구독리스트(int principalUserId, int pageUserId){
		
		// 쿼리를 String 으로 , 끝에 한칸을 반드시 띄어준다. append 할때 다음 String과 붙은 결과가 나오기 때문에 
		// 쿼리 준비. 
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT u.id, u.username , u.profileImageUrl, ");
		sb.append("if((SELECT 1 FROM subscribe WHERE fromUserId = ? AND toUserId = u.id),1,0) subscribeState, ");
		sb.append("if((?=u.id),1,0) equalUserState ");
		sb.append("FROM  user u INNER JOIN subscribe s ");
		sb.append("ON u.id = s.fromUserId ");
		sb.append("WHERE s.toUserId = ?");		// 세미콜론 첨부하면 안됨. 
		
		// 1. 물음표 : principalUserId
		// 2. 물음표 : principalUserId
		// 3. 물음표 : pageUserId
		
		//  javax.persistence.Query
		// 쿼리 완성 
		Query query = em.createNativeQuery(sb.toString())
				.setParameter(1, principalUserId)
				.setParameter(2, principalUserId)
				.setParameter(3, pageUserId);
		
		// 쿼리실행 (qlrm 라이브러리 필요 = DTO 에 DB 결과를 매핑하기 위해) qlrm : JpaResultMapper
		JpaResultMapper result = new JpaResultMapper();
		List<	SubscribeDto> subscribeDtos = result.list(query, SubscribeDto.class);
		
		return subscribeDtos;
	}
	
	
	@Transactional
	public void 구독하기(int fromUserId ,int toUserId) {
		try {
			subscribeRepository.mSubscribe(fromUserId, toUserId);
		} catch (Exception e) {
			throw new CustomApiException("이미 구독한 유저입니다.");
		}
		
	}
	
	@Transactional
	public void 구독취소하기(int fromUserId, int toUserId) {
		try {
			subscribeRepository.mUnSubscribe(fromUserId, toUserId);
		} catch (Exception e) {
			throw new CustomApiException("이미 취소한 유저입니다.");
		}
		
	}
}
