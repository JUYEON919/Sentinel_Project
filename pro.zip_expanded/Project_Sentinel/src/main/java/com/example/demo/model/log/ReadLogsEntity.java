package com.example.demo.model.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 로그 정보를 담는 엔티티 클래스
 */
@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ReadLogsEntity {
    private Integer log_index;        // 로그 인덱스
    private Integer detected_no;      // 검출 번호
    private String detected_name;     // 검출 이름
    private LocalDateTime time;       // 로그 시간
    private Integer action;           // 동작
    private String detail;            // 세부 정보
    private String src_ip;            // 출발지 IP
    private String src_port;          // 출발지 포트
    private String dst_ip;            // 목적지 IP
    private String dst_port;          // 목적지 포트
    private byte[] packet_bin;        // 패킷 이진 데이터
    private Integer level;            // 레벨

    private String log_date;          // 로그 날짜

    /**
     * 로그 날짜를 시간 정보에서 설정하는 메서드
     */
    public void setLogdateFromTime() {
        this.setLog_date(this.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
