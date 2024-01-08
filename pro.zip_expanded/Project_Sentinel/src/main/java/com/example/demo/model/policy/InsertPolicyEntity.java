package com.example.demo.model.policy;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 정책 추가를 위한 엔티티 클래스
 */
@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class InsertPolicyEntity {
    private Integer detected_no;        // 검출 번호
    private String detected_name;       // 검출 이름
    private Integer enable;             // 활성/비활성 상태
    private Integer action;             // 동작
    private String src_ip;              // 출발지 IP
    private String src_port;            // 출발지 포트
    private String content1;            // 내용1
    private String content2;            // 내용2
    private String content3;            // 내용3
    private Integer base_time;          // 기준 시간
    private Integer base_limit;         // 기준 제한
    private Integer level;              // 레벨
    private String detail;              // 세부 정보
    private String to_sip;              // 도착지 IP
    private String to_sp;               // 도착지 포트
    private String dst_ip;              // 목적지 IP
    private String dst_port;            // 목적지 Port
    private String base_size;           // 기준 크기
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end_time;        // 종료 시간
}
