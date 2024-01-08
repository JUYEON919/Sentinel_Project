package com.example.demo.repository.policy;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.policy.InsertPolicyEntity;

/**
 * 정책 추가를 위한 매퍼 인터페이스
 */
@Mapper
public interface InsertPolicyMapper {

    /**
     * 정책을 추가하는 메서드
     *
     * @param policy 추가할 정책 엔티티
     */
    void insertPolicy(InsertPolicyEntity policy);
}
