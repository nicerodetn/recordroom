package com.recordroom.recordroom;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {


    @GetMapping("/homepage")
    public String homepage()
    {
        return "layout/outsidelayout";
    }

    @GetMapping("/dashboard")
    public String dashboard()
    {
        return "layout/insidelayout";
    }


    @GetMapping("/profile")
    public String profileFragment(Model model) {
        return "fragments/table :: content";
    }

}
