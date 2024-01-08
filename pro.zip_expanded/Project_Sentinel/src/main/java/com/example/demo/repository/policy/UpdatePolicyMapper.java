package com.example.demo.repository.policy;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.policy.UpdatePolicyEntity;

/**
 * 정책 업데이트를 위한 매퍼 인터페이스
 */
@Mapper
public interface UpdatePolicyMapper {

    /**
     * 특정 검출 번호에 해당하는 정책의 상세 정보를 조회하는 메서드
     *
     * @param detected_no 검출 번호
     * @return             검출 번호에 해당하는 정책의 상세 정보
     */
    UpdatePolicyEntity getPolicyDetailsById(int detected_no);

    /**
     * 특정 검출 번호에 해당하는 정책을 업데이트하는 메서드
     *
     * @param policy 업데이트할 정책 엔티티
     */
    void updatePolicy(UpdatePolicyEntity policy);

    /**
     * 활성 상태를 업데이트하는 메서드
     */
    void updateEnableStatus();
}
