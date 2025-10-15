package com.recordroom.recordroom;


import com.recordroom.recordroom.closed.entity.RecordTransactionDetails;
import com.recordroom.recordroom.dashbord.DashboardService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/homepage")
    public String homepage(@RequestParam(value = "error", required = false) String error, Model model) {

        if ("invalid_credentials".equals(error)) {
            model.addAttribute("errorMessage", "Incorrect username or password.");
        }


        return "layout/outsidelayout";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("stats", dashboardService.getDashboardStats());
        return "layout/insidelayout";
    }
    //test

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
            headers.set("HX-Redirect", "/record/homepage?error=invalid_credentials"); // 👈 destination page
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


    @GetMapping("/summaryreport")
    public String summaryreport(Model model) {
        model.addAttribute("stats", dashboardService.getDashboardStats());
        return "fragments/summary_report :: tabler";
    }

    @GetMapping("/outWardReport")
    public String showReport(Model model) {

        return "fragments/closed/out_entry_report :: tabler"; // Thymeleaf template name
    }



}
