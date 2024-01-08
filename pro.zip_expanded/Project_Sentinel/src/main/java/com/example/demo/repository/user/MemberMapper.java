package com.example.demo.repository.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.model.user.MemberEntity;

/**
 * 회원 정보를 다루기 위한 매퍼 인터페이스
 */
@Mapper
public interface MemberMapper {

    /**
     * 특정 아이디에 해당하는 회원 정보를 조회하는 메서드
     *
     * @param id 아이디
     * @return   특정 아이디에 해당하는 회원 정보
     */
    MemberEntity signin(@Param("id") String id);

    /**
     * 회원 비밀번호를 업데이트하는 메서드
     *
     * @param member 회원 엔티티
     */
    void updatePassword(MemberEntity member);
}
