package com.example.demo.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.example.demo.model.file.SetupEntity;

/**
 * 설정 파일을 생성하고 관리하는 서비스 클래스
 */
@Service
public class SetupService {
    
    // 설정 파일 경로
    final String PATH = "/home/rocky/manager/dbms_ips_manager.conf";
    final File FILE = new File(PATH);

    /**
     * 현재 설정 정보를 읽어오는 메서드
     *
     * @return 현재 설정 정보를 담고 있는 SetupEntity 객체
     */
    public SetupEntity getCurrentSetup() {
        SetupEntity currentSetup = new SetupEntity();
        // 기존 파일에서 설정을 읽어옴
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("SNIFF_NIC")) {
                    currentSetup.setSNIFF_NIC(line.split("=")[1].trim());
                } else if (line.startsWith("DB_IP")) {
                    currentSetup.setDB_IP(line.split("=")[1].trim());
                } else if (line.startsWith("DB_PORT")) {
                    currentSetup.setDB_PORT(line.split("=")[1].trim());
                }
            }
        } catch (IOException e) {
        	if (e.getCause() instanceof FileNotFoundException) {
        		System.out.println("========== 설정 파일을 찾을 수 없습니다. 지정된 경로를 다시 확인하세요. ==========");
            } else if (e instanceof IOException) {
            	System.out.println("========== 설정 파일을 읽어오는 중 오류가 발생했습니다. 지정된 경로를 다시 확인하세요. ==========");
            } else {
            	System.out.println("========== 설정 파일을 처리 중 오류가 발생했습니다. ==========");
            }
        }
        return currentSetup;
    }
    
    /**
     * 설정 파일을 생성하거나 변경하는 메서드
     *
     * @param setupEntity 설정 정보를 담고 있는 객체
     * @return int -1, 0, 1
     *              -1: 파일 생성이나 변경 중 에러가 발생했음을 의미
     *              0: 입력된 정보와 기존 정보가 동일하여 파일 변경이 일어나지 않았음을 의미 
     *              1: 파일 생성이나 변경이 성공적으로 이루어졌음을 의미
     */
    public int makeConfFile(SetupEntity setupEntity) {
        final File BACKUP_FILE = new File(PATH + "_backup");

        // 입력된 정보와 기존 정보가 동일하다면 백업 파일을 생성하지 않음
        SetupEntity currentSetup = getCurrentSetup();
        if (currentSetup.equals(setupEntity)) {
            return 0;
        }

        // 기존 파일이 존재하는 경우 백업 파일로 변경
        if (FILE.exists()) {
            FILE.renameTo(BACKUP_FILE);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH))) {
            writer.write("SNIFF_NIC = " + setupEntity.getSNIFF_NIC() + "\n\n");
            writer.write("DB_IP = " + setupEntity.getDB_IP() + "\n");
            writer.write("DB_PORT = " + setupEntity.getDB_PORT() + "");

            // 백업 파일에서 설정 파일로 이동할 때 다른 설정을 유지
            try (BufferedReader reader = new BufferedReader(new FileReader(BACKUP_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("SNIFF_NIC") ||
                            line.startsWith("DB_IP") ||
                            line.startsWith("DB_PORT")) {
                        continue;
                    }
                    writer.write(line + "\n");
                }
            } catch (IOException e) {
                if (e.getCause() instanceof FileNotFoundException) {
            		System.out.println("========== 백업 파일 읽어오는 중 오류가 발생했습니다. ==========");
            		return -1;
                }
            }
        } catch (IOException e) {
        	if (e.getCause() instanceof FileNotFoundException) {
        		System.out.println("========== 백업 파일 생성 중 오류가 발생했습니다. ==========");
        		return -1;
            }
        }
        return 1;
    }
}
