package com.example.demo.repository.log;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.model.log.ReadLogsEntity;

/**
 * 로그 조회를 위한 매퍼 인터페이스
 */
@Mapper
public interface ReadLogsMapper {

    /**
     * 필터링된 로그를 조회하는 메서드
     *
     * @param tableName    테이블 이름
     * @param src_ip       출발지 IP
     * @param src_port     출발지 포트
     * @param dst_ip       목적지 IP
     * @param detected_name 검출 이름
     * @param level        레벨
     * @param action       동작
     * @param startDate    시작 날짜
     * @param endDate      종료 날짜
     * @return             필터링된 로그 리스트
     */
    List<ReadLogsEntity> findFilteredLogs(
            @Param("tableName") String tableName,
            @Param("src_ip") String src_ip,
            @Param("src_port") String src_port,
            @Param("dst_ip") String dst_ip,
            @Param("dst_port") String dst_port,
            @Param("detected_name") String detected_name,
            @Param("level") Integer level,
            @Param("action") Integer action,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * 이진 데이터를 조회하는 메서드
     *
     * @param tableName 테이블 이름
     * @param log_index 로그 인덱스
     * @return          이진 데이터를 포함한 로그 엔티티
     */
    ReadLogsEntity getBinaryData(
            @Param("tableName") String tableName,
            @Param("log_index") int log_index);

    /**
     * 검출 이름을 통해 로그를 조회하는 메서드
     *
     * @param tableName 테이블 이름
     * @param log_index 로그 인덱스
     * @return          검출 이름에 해당하는 로그 엔티티
     */
    ReadLogsEntity findByDetectedNo(
            @Param("tableName") String tableName,
            @Param("log_index") int log_index);
    
    /**
     * 특정 기간 동안의 로그를 조회하는 메서드
     *
     * @param tableName 테이블 이름
     * @param startDate 시작 날짜
     * @param endDate   종료 날짜
     * @return          조회된 로그 리스트
     */
    List<ReadLogsEntity> getLastLogs(
            @Param("tableName") String tableName,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * 특정 기간 동안의 주간 로그를 조회하는 메서드
     *
     * @param tableName 테이블 이름
     * @param startDate 시작 날짜
     * @param endDate   종료 날짜
     * @return          조회된 주간 로그 리스트
     */
    List<ReadLogsEntity> getWeekLogs(
            @Param("tableName") String tableName,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
