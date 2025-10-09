package com.recordroom.recordroom;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class HomeController {


    @GetMapping("/homepage")
    public String homepage() {
        return "layout/outsidelayout";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "layout/insidelayout";
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) throws ServletException {

        if (username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin")) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("HX-Redirect", "/record/dashboard"); // 👈 destination page
            return new ResponseEntity<>(headers, HttpStatus.OK);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.set("HX-Redirect", "/record/homepage"); // 👈 destination page
            return new ResponseEntity<>(headers, HttpStatus.OK);
        }
    }


    @GetMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpSession session,
            Model model) throws ServletException {
            HttpHeaders headers = new HttpHeaders();
            headers.set("HX-Redirect", "/record/homepage"); // 👈 destination page
            return new ResponseEntity<>(headers, HttpStatus.OK);

    }


    @GetMapping("/profile")
    public String profileFragment(Model model) {
        return "fragments/table :: content";
    }

}
