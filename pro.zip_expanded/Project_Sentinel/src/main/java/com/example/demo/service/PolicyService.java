package com.example.demo.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.repository.policy.UpdatePolicyMapper;

/**
 * 정책 관리 서비스 클래스
 */
@Service
@EnableScheduling
public class PolicyService {
	
    @Autowired
    private UpdatePolicyMapper updatePolicyMapper;
    private volatile boolean isPolicyUpdated = false;

    /**
     * 주어진 내용을 URL 인코딩하는 메서드
     *
     * @param content 인코딩할 내용
     * @return URL 인코딩된 문자열
     * @throws UnsupportedEncodingException 인코딩이 지원되지 않는 경우 발생
     */
    public String encodingContent(String content) throws UnsupportedEncodingException {
        return URLEncoder.encode(content, StandardCharsets.UTF_8.toString());
    }

    /**
     * 주어진 내용을 URL 디코딩하는 메서드
     *
     * @param content 디코딩할 내용
     * @return URL 디코딩된 문자열
     * @throws UnsupportedEncodingException 디코딩이 지원되지 않는 경우 발생
     */
    public String decodingContent(String content) throws UnsupportedEncodingException {
        return URLDecoder.decode(content, StandardCharsets.UTF_8.toString());
    }
    
    /**
     * 주어진 포트 범위를 "-"를 기준으로 분리하는 메서드
     *
     * @param portRange 포트 범위를 나타내는 문자열
     * @return 포트 범위를 나타내는 문자열 배열 [시작포트, 종료포트]
     */
    public String[] isSplit(String portRange) {
        return portRange.split("-");
    }

    /**
     * 주기적으로 실행되는 메서드로, 정책 업데이트를 수행하여 활성화 상태를 업데이트합니다.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void updateEnableStatus() {
        updatePolicyMapper.updateEnableStatus();
        triggerPolicyUpdate();
    }
    
    /**
     * 정책 업데이트를 트리거하여 정책이 업데이트되었음을 표시하는 메서드
     */
    public void triggerPolicyUpdate() {
        this.isPolicyUpdated = true;
    }

    /**
     * 주기적으로 실행되며 정책의 유효기간이 지나 비활성화되었을 때 UDP 신호를 전송하는 메서드
     * 업데이트 후 플래그를 다시 false로 설정하여 중복 전송을 방지합니다.
     *
     * @throws IOException UDP 신호 전송 중 발생할 수 있는 입출력 예외입니다.
     */
    @Scheduled(fixedDelay = 10000) // 10초마다 실행
    public void processPolicyUpdate() throws IOException {
        if (isPolicyUpdated) {
            sendUDP();
            isPolicyUpdated = false; // 플래그를 다시 false로 설정
        }
    }
    
    /**
     * UDP 프로토콜을 사용하여 메시지를 특정 호스트와 포트로 전송하는 메서드
     * 
     * @throws NoSuchAlgorithmException
     */
    public void sendUDP() {
        final String RECEIVER_HOST = "192.168.1.14";
        final int RECEIVER_PORT = 21113;
        final String MESSAGE = "updatePolicy";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(MESSAGE.getBytes());
            byte[] digest = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            final String MY_HASH = sb.toString().toUpperCase();
            
            final InetAddress RECEIVER_ADDRESS = InetAddress.getByName(RECEIVER_HOST);
            try (DatagramSocket socket = new DatagramSocket()) {
                byte[] buffer = MY_HASH.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, RECEIVER_ADDRESS, RECEIVER_PORT);
                socket.send(packet);
            }
        } catch (UnknownHostException e) {
            System.err.println("호스트를 찾을 수 없습니다: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O 에러가 발생했습니다: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("SHA-256 알고리즘이 존재하지 않습니다: " + e.getMessage());
        }
    }
}
