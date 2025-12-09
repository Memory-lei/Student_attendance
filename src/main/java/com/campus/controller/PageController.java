// com/campus/controller/PageController.java
package com.campus.controller;

import com.campus.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/students")
    public String studentList() {
        return "student_list";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/admin")
    public String adminPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "admin";
    }

    @GetMapping("/teacher")
    public String teacherPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"TEACHER".equals(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "teacher";
    }

    @GetMapping("/student")
    public String studentPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"STUDENT".equals(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "student";
    }
}