package com.example.demo.service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

import com.example.demo.model.user.MemberEntity;
import com.example.demo.repository.user.MemberMapper;

import lombok.RequiredArgsConstructor;

/**
 * 회원 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberMapper memberMapper;

	/**
	 * 회원 로그인 처리 메서드
	 *
	 * @param memberEntity 로그인 시도한 회원 엔티티
	 * @return 로그인 성공 여부 (true: 성공, false: 실패)
	 * @throws NoSuchAlgorithmException 암호화 알고리즘이 지원되지 않는 경우 발생
	 */
	public boolean login(MemberEntity memberEntity) throws NoSuchAlgorithmException {
		MemberEntity member = memberMapper.signin(memberEntity.getId());
		if (member != null) {
			String sha256hex = hashPassword(memberEntity.getPassword(), member.getSalt());
			if (member.getPassword().equals(sha256hex)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 비밀번호 변경 메서드
	 *
	 * @param currentPassword 현재 비밀번호
	 * @param newPassword     새로운 비밀번호
	 * @return 비밀번호 변경 성공 여부 (true: 성공, false: 실패)
	 * @throws NoSuchAlgorithmException 암호화 알고리즘이 지원되지 않는 경우 발생
	 */
	public boolean changePassword(String currentPassword, String newPassword) throws NoSuchAlgorithmException {
		final String id = "admin01";
		MemberEntity memberEntity = memberMapper.signin(id);
		if (memberEntity != null) {
			String hashedCurrentPassword = hashPassword(currentPassword, memberEntity.getSalt());
			if (memberEntity.getPassword().equals(hashedCurrentPassword)) {
				String hashedNewPassword = hashPassword(newPassword, memberEntity.getSalt());
				memberEntity.setPassword(hashedNewPassword);
				memberMapper.updatePassword(memberEntity);
				return true;
			}
		}
		return false;
	}

	/**
	 * 비밀번호를 해시하는 메서드
	 *
	 * @param password 사용자가 입력한 비밀번호
	 * @param salt     무작위값 (비밀번호에 추가되는 무작위 문자열)
	 * @return 해시된 비밀번호
	 * @throws NoSuchAlgorithmException 암호화 알고리즘이 지원되지 않는 경우 발생
	 */
	private String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest((password + salt).getBytes(StandardCharsets.UTF_8));
		return String.format("%064x", new BigInteger(1, hash));
	}
}
