package com.example.demo.model.user;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 정보를 담은 엔티티 클래스
 */
@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MemberEntity {
    private Integer personal_no;     // 개인 번호
    private String id;               // 아이디
    private String password;         // 비밀번호
    private String salt;             // 솔트
}
