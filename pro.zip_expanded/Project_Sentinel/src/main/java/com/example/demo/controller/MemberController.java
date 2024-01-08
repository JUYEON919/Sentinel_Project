package com.example.demo.controller;

import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.user.MemberEntity;
import com.example.demo.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * 회원 컨트롤러 클래스
 */
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    
    /**
     * 로그인 화면을 보여주는 메서드
     *
     * @return 로그인 템플릿 이름
     */
    @GetMapping("/admin")
    public String showLoginForm_2() {
        return "login";
    }
    @GetMapping("/")
    public String showLoginForm_1() {
        return "redirect:/admin";
    }
    @GetMapping("/admin/")
    public String showLoginForm_3() {
        return "redirect:/admin";
    }
    @GetMapping("/login")
    public String showLoginForm_4() {
        return "redirect:/admin";
    }
    @GetMapping("/login/")
    public String showLoginForm_5() {
        return "redirect:/admin";
    }

    /**
     * 로그인 처리를 위한 메서드
     *
     * @param memberEntity 로그인 시도한 회원 정보
     * @param model        Model 객체
     * @param session      HttpSession 객체
     * @return 관리자 메뉴 페이지로의 리다이렉트 또는 다시 로그인 화면으로의 리다이렉트
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException 예외
     */
    @PostMapping("/admin")
    public String login(@ModelAttribute MemberEntity memberEntity, Model model, HttpSession session) throws NoSuchAlgorithmException {
        if (memberService.login(memberEntity)) {
            session.setAttribute("id", memberEntity);
            session.setMaxInactiveInterval(30 * 60);
            return "redirect:/admin/menu";
        } else {
            return "redirect:/admin";
        }
    }

    /**
     * 비밀번호 변경을 위한 메서드
     *
     * @param currentPassword 현재 비밀번호
     * @param newPassword     새로운 비밀번호
     * @return 설정 페이지로의 리다이렉트 (비밀번호 변경 성공 또는 실패에 따라 다름)
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException 예외
     */
    @PostMapping("/admin/menu/changePassword")
    public String changePassword(@RequestParam String currentPassword, @RequestParam String newPassword) throws NoSuchAlgorithmException {
        boolean isChanged = memberService.changePassword(currentPassword, newPassword);
        if (isChanged) {
            return "redirect:/admin/menu/setup?changePassword=true";
        } else {
            return "redirect:/admin/menu/setup?changePassword=false";
        }
    }

    /**
     * 로그아웃 메서드
     *
     * @param session HttpSession 객체
     * @return 로그인 화면으로의 리다이렉트
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin";
    }

    /**
     * 관리자 메뉴 페이지를 보여주는 메서드
     *
     * @param memberEntity 로그인한 회원 정보
     * @param session      HttpSession 객체
     * @return 메뉴 템플릿 이름 또는 로그인 화면으로의 리다이렉트
     */
    @GetMapping("/admin/menu")
    public String showMenuForm(MemberEntity memberEntity, HttpSession session) {
        memberEntity = (MemberEntity) session.getAttribute("id");
        if (memberEntity != null) {
            return "menu";
        } else {
            return "redirect:/admin";
        }
    }

    /**
     * 홈 페이지를 보여주는 메서드
     *
     * @return 홈 템플릿 이름
     */
    @GetMapping("/admin/home")
    public String showHomeForm() {
        return "home";
    }
}
