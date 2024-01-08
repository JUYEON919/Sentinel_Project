package com.example.demo.repository.policy;

import org.apache.ibatis.annotations.Mapper;

/**
 * 정책 삭제를 위한 매퍼 인터페이스
 */
@Mapper
public interface DeletePolicyMapper {

    /**
     * 특정 검출 번호에 해당하는 정책을 삭제하는 메서드
     *
     * @param detected_no 검출 번호
     */
    void deletePolicyById(int detected_no);

    /**
     * 특정 검출 번호에 해당하는 정책의 활성 상태를 조회하는 메서드
     *
     * @param detected_no 검출 번호
     * @return            활성 상태 (1: 활성, 0: 비활성)
     */
    int getPolicyEnableStatusById(int detected_no);
}
