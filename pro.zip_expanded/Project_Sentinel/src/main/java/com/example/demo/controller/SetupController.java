package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.file.SetupEntity;
import com.example.demo.service.SetupService;

import lombok.RequiredArgsConstructor;

/**
 * 설정 컨트롤러 클래스
 */
@Controller
@RequiredArgsConstructor
public class SetupController {

    private final SetupService fileService;

    /**
     * 설정 화면을 보여주는 메서드
     *
     * @return 설정 템플릿 이름
     */
    @GetMapping("/admin/menu/setup")
    public String showSetupForm(Model model) {
    	SetupEntity currentSetup = fileService.getCurrentSetup();
        model.addAttribute("SNIFF_NIC", currentSetup.getSNIFF_NIC());
        model.addAttribute("DB_IP", currentSetup.getDB_IP());
        model.addAttribute("DB_PORT", currentSetup.getDB_PORT());
        return "setup";
    }

    /**
     * 설정을 업데이트하는 메서드
     *
     * @param confFileEntity SetupEntity 객체
     * @return 관리자 메뉴 페이지로의 리다이렉트
     */
    @PostMapping("/admin/menu/setup/changeSetting")
    public String updateFile(@ModelAttribute SetupEntity confFileEntity) {
        int updateStatus = fileService.makeConfFile(confFileEntity);
        
        if (updateStatus == 1) {
            return "redirect:/admin/menu/setup?changeSetting=true";
        } else if (updateStatus == 0) {
            return "redirect:/admin/menu/setup?changeSetting=same";
        } else {
            return "redirect:/admin/menu/setup?changeSetting=false";
        }
    }
}
