package com.example.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.springframework.stereotype.Service;

import com.example.demo.model.log.EthernetHeaderEntity;
import com.example.demo.model.log.IpHeaderEntity;
import com.example.demo.model.log.ReadLogsEntity;
import com.example.demo.model.log.TcpHeaderEntity;

/**
 * 로그 관련 작업을 처리하는 서비스 클래스
 */
@Service
public class LogService {

    /**
     * 문자열 파라미터 검증 메서드
     *
     * @param param 검증할 문자열 파라미터
     * @return 유효한 경우 검증된 파라미터, 그렇지 않은 경우 null
     */
    public String validateParam(String param) {
        return ("".equalsIgnoreCase(param) || "any".equalsIgnoreCase(param) || param.isEmpty()) ? null : param;
    }

    /**
     * 정수 파라미터 검증 메서드
     *
     * @param param         검증할 정수 파라미터
     * @param defaultValue  기본값
     * @return 유효한 경우 검증된 파라미터, 그렇지 않은 경우 null
     */
    public Integer validateParam(Integer param, Integer defaultValue) {
        return (param == null || param.equals(defaultValue)) ? null : param;
    }
    
    /**
     * 문자열 파라미터 검증 및 변환 메서드
     *
     * @param param 검증 및 변환할 문자열 파라미터
     * @return 유효한 경우 변환된 파라미터, 그렇지 않은 경우 null
     * @throws PatternSyntaxException 입력된 문자열에서 패턴 매칭 중 에러가 발생했을 때
     */
    public String validateKorean(String param) throws PatternSyntaxException {
        if (param != null && !param.isEmpty()) {
            if (param.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
            	StringBuilder sb = new StringBuilder();
                for (char ch : param.toCharArray()) {
                    sb.append((int) ch);
                }
                param = sb.toString();
            }
        }
        return param;
    }

    /**
     * 16진수 패킷 추출 메서드
     *
     * @param log 패킷을 추출할 로그 엔터티
     * @return 추출된 16진수 패킷
     */
    public String getPacket(ReadLogsEntity log) {
        byte[] packet_bin = log.getPacket_bin();
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : packet_bin) {
            stringBuilder.append(String.format("%02X", b));
        }
        String binaryData = stringBuilder.toString().toUpperCase();
        return parseBinaryDataForPacket(binaryData);
    }

    /**
     * 이진 데이터를 가공 후 패킷 데이터를 생성하는 메서드
     *
     * @param binaryData 가공할 바이너리 데이터
     * @return 생성된 패킷 데이터
     */
    private String parseBinaryDataForPacket(String binaryData) {
        int index = 0;
        String packetData = "";
        while (index < binaryData.length()) {
            String packet = binaryData.substring(index, Math.min(index + 2, binaryData.length()));
            packetData += packet + " ";
            index += 2;
        }
        return packetData.trim();
    }
    
    /**
     * 이진 데이터를 ASCII 문자열로 변환하는 메서드
     *
     * @param binaryData 이진 데이터
     * @return ASCII 문자열
     */
    public String toASCII(String binaryData) {
        StringBuilder payload = new StringBuilder();
        String binaryDataTrim = binaryData.replace(" ", "");
        for (int i = 0; i < binaryDataTrim.length(); i += 2) {
            String str = binaryDataTrim.substring(i, i + 2);
            int intValue = Integer.parseInt(str, 16);
            if (intValue >= 32 && intValue <= 126) {
                payload.append((char) intValue);
            } else {
                payload.append(".");
            }
        }
        return payload.toString();
    }
    
    /**
     * 헤더 추출 메서드
     *
     * @param log 헤더를 추출할 로그 엔터티
     * @return 추출된 헤더 목록
     */
    public List<Object> getHeader(ReadLogsEntity log) {
        byte[] packet_bin = log.getPacket_bin();
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : packet_bin) {
            stringBuilder.append(String.format("%02x", b));
        }
        String binaryData = stringBuilder.toString().toUpperCase();
        return parseBinaryDataForHeader(binaryData);
    }
	
    /**
     * 주어진 이진 데이터에서 헤더를 추출하고 해당 헤더를 목록으로 반환하는 메서드
     *
     * @param binaryData 추출할 헤더를 포함하는 이진 데이터
     * @return 헤더를 나타내는 맵의 목록
     */
    private List<Object> parseBinaryDataForHeader(String binaryData) {
        int index = 0;
        String packetData = "";

        while (index < binaryData.length()) {
            String packet = binaryData.substring(index, Math.min(index + 2, binaryData.length()));
            packetData += packet + " ";
            index += 2;
        }
        
        return parseHeader(packetData.trim());
    }

    /**
     * 주어진 16진수 문자열 배열을 바이트 배열로 변환하여
     * Ethernet, IP 및 TCP 헤더의 값을 추출하고 이를 객체에 저장하는 메서드
     *
     * @param packetData 가공된 16진수 문자열을 포함하는 문자열
     * @return Ethernet, IP 및 TCP 헤더 값을 나타내는 객체의 목록
     */
    private List<Object> parseHeader(String packetData) {
        // 공백을 제거하고 각 2자리 16진수 문자열을 바이트로 변환
        String[] hexBytes = packetData.split(" ");
        byte[] bytes = new byte[hexBytes.length];
        for (int i = 0; i < hexBytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hexBytes[i], 16);
        }

        // Ethernet 헤더값 추출 후 EthernetHeader 객체에 주입
        EthernetHeaderEntity ethernetHeader = parseEthernetHeader(bytes);

        // IP 헤더 시작 위치 (Ethernet 헤더는 14바이트)
        int ipStart = 14;

        // IP 헤더값 추출 후 IpHeader 객체에 주입
        IpHeaderEntity ipHeader = parseIpHeader(bytes, ipStart);

        // TCP 또는 UDP 헤더 시작 위치 계산 (IP 헤더 길이는 4바이트 단위)
        int headerLength = ipHeader.getHeaderLength();
        int tcpOrUdpStart = ipStart + headerLength * 4;

        // TCP 헤더값 추출 후 TCPHeader 객체에 주입
        TcpHeaderEntity tcpHeader = null;
        if (ipHeader.getDatagramProtocol() == 6) { // TCP
            tcpHeader = parseTcpHeader(bytes, tcpOrUdpStart);
        }

        // 추출된 헤더들을 목록에 추가
        List<Object> headers = new ArrayList<>();
        headers.add(ethernetHeader);
        headers.add(ipHeader);
        if (tcpHeader != null) {
            headers.add(tcpHeader);
        }

        return headers;
    }
    
    /**
     * 주어진 바이트 배열에서 Ethernet 헤더 값을 추출하여 EthernetHeader 객체에 저장하는 메서드
     *
     * @param bytes Ethernet 헤더를 나타내는 바이트 배열
     * @return 추출된 Ethernet 헤더 값을 나타내는 EthernetHeader 객체
     */
    private EthernetHeaderEntity parseEthernetHeader(byte[] bytes) {
        // 바이트 배열의 일부를 추출하여 MAC 주소 및 Ethernet Type을 얻음
        String destinationMac = bytesToHexForMac(Arrays.copyOfRange(bytes, 0, 6));
        String sourceMac = bytesToHexForMac(Arrays.copyOfRange(bytes, 6, 12));
        String ethernetType = bytesToHexForEtherType(Arrays.copyOfRange(bytes, 12, 14));

        // 추출된 값을 EthernetHeader 객체에 저장
        EthernetHeaderEntity ethernetHeader = new EthernetHeaderEntity();
        ethernetHeader.setDestinationMacAddr(destinationMac);
        ethernetHeader.setSourceMacAddr(sourceMac);
        ethernetHeader.setEthernetType("0x" + ethernetType);

        return ethernetHeader;
    }

    /**
     * 주어진 바이트 배열을 16진수 형식의 MAC 주소 문자열로 변환하여 반환하는 메서드
     *
     * @param bytes MAC 주소를 나타내는 바이트 배열
     * @return 16진수 형식의 MAC 주소 문자열
     */
    private String bytesToHexForMac(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X:", b));
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 주어진 바이트 배열을 16진수 형식의 Ethernet Type 문자열로 변환하여 반환하는 메서드
     *
     * @param bytes Ethernet Type을 나타내는 바이트 배열
     * @return 16진수 형식의 Ethernet Type 문자열
     */
    private String bytesToHexForEtherType(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    /**
     * 주어진 바이트 배열에서 IP 헤더 값을 추출하여 IpHeaderEntity 객체에 저장하는 메서드
     *
     * @param bytes   IP 헤더를 나타내는 바이트 배열
     * @param ipStart IP 헤더의 시작 위치
     * @return 추출된 IP 헤더 값을 나타내는 IpHeaderEntity 객체
     */
    private IpHeaderEntity parseIpHeader(byte[] bytes, int ipStart) {
        // IP 헤더의 첫 바이트에서 버전과 헤더 길이를 추출
        byte versionAndHeaderLength = bytes[ipStart];
        int version = (versionAndHeaderLength >> 4) & 0xF;
        int headerLength = versionAndHeaderLength & 0xF;

        // IP 헤더 분석
        byte typeOfService = bytes[ipStart + 1];
        int datagramLength = ((bytes[ipStart + 2] & 0xFF) << 8) | (bytes[ipStart + 3] & 0xFF);
        int identification = ((bytes[ipStart + 4] & 0xFF) << 8) | (bytes[ipStart + 5] & 0xFF);
        int flagsAndFragmentOffset = ((bytes[ipStart + 6] & 0xFF) << 8) | (bytes[ipStart + 7] & 0xFF);
        int flags = (flagsAndFragmentOffset >> 13) & 0x7;
        int fragmentOffset = flagsAndFragmentOffset & 0x1FFF;
        int ttl = bytes[ipStart + 8] & 0xFF;
        byte datagramProtocol = bytes[ipStart + 9];
        int checksum = ((bytes[ipStart + 10] & 0xFF) << 8) | (bytes[ipStart + 11] & 0xFF);
        String sourceIp = (bytes[ipStart + 12] & 0xFF) + "." + (bytes[ipStart + 13] & 0xFF) + "." + (bytes[ipStart + 14] & 0xFF) + "." + (bytes[ipStart + 15] & 0xFF);
        String destinationIp = (bytes[ipStart + 16] & 0xFF) + "." + (bytes[ipStart + 17] & 0xFF) + "." + (bytes[ipStart + 18] & 0xFF) + "." + (bytes[ipStart + 19] & 0xFF);

        // 추출된 값을 IpHeaderEntity 객체에 저장
        IpHeaderEntity ipHeader = new IpHeaderEntity();
        ipHeader.setVersion(version);
        ipHeader.setHeaderLength(headerLength);
        ipHeader.setTypeOfService(typeOfService);
        ipHeader.setDatagramLength(datagramLength);
        ipHeader.setIdentification(identification);
        ipHeader.setFlags(flags);
        ipHeader.setFragmentOffset(fragmentOffset);
        ipHeader.setTtl(ttl);
        ipHeader.setDatagramProtocol(datagramProtocol);
        ipHeader.setChecksum(checksum);
        ipHeader.setSourceIp(sourceIp);
        ipHeader.setDestinationIp(destinationIp);

        return ipHeader;
    }

    /**
     * 주어진 바이트 배열에서 TCP 헤더 값을 추출하여 TCPHeader 객체에 저장하는 메서드
     *
     * @param bytes    TCP 헤더를 나타내는 바이트 배열
     * @param tcpStart TCP 헤더의 시작 위치
     * @return 추출된 TCP 헤더 값을 나타내는 TCPHeader 객체
     */
    private TcpHeaderEntity parseTcpHeader(byte[] bytes, int tcpStart) {
        // TCP 헤더 분석
        int sourcePort = ((bytes[tcpStart] & 0xFF) << 8) | (bytes[tcpStart + 1] & 0xFF);
        int destinationPort = ((bytes[tcpStart + 2] & 0xFF) << 8) | (bytes[tcpStart + 3] & 0xFF);
        long sequenceNumber = ((bytes[tcpStart + 4] & 0xFFL) << 24) | ((bytes[tcpStart + 5] & 0xFFL) << 16) | ((bytes[tcpStart + 6] & 0xFFL) << 8) | (bytes[tcpStart + 7] & 0xFFL);
        long acknowledgmentNumber = ((bytes[tcpStart + 8] & 0xFFL) << 24) | ((bytes[tcpStart + 9] & 0xFFL) << 16) | ((bytes[tcpStart + 10] & 0xFFL) << 8) | (bytes[tcpStart + 11] & 0xFFL);
        int dataOffset = (bytes[tcpStart + 12] >> 4) & 0xF;
        int reserved = (bytes[tcpStart + 12] >> 1) & 0x7;
        int flags = bytes[tcpStart + 13] & 0xFF;
        String flagsString = String.format("0x%03X", flags & 0xFF);
        int windowSize = ((bytes[tcpStart + 14] & 0xFF) << 8) | (bytes[tcpStart + 15] & 0xFF);
        int checksum = ((bytes[tcpStart + 16] & 0xFF) << 8) | (bytes[tcpStart + 17] & 0xFF);
        int urgentPointer = ((bytes[tcpStart + 18] & 0xFF) << 8) | (bytes[tcpStart + 19] & 0xFF);

        // 추출된 값을 TCPHeader 객체에 저장
        TcpHeaderEntity tcpHeader = new TcpHeaderEntity();
        tcpHeader.setSourcePort(sourcePort);
        tcpHeader.setDestinationPort(destinationPort);
        tcpHeader.setSequenceNumber(sequenceNumber);
        tcpHeader.setAcknowledgmentNumber(acknowledgmentNumber);
        tcpHeader.setDataOffset(dataOffset);
        tcpHeader.setReserved(reserved);
        tcpHeader.setFlags(flagsString);
        tcpHeader.setWindowSize(windowSize);
        tcpHeader.setChecksum(checksum);
        tcpHeader.setUrgentPointer(urgentPointer);

        return tcpHeader;
    }
}
