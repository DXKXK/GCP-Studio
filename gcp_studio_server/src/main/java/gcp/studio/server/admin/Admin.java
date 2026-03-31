package gcp.studio.server.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class Admin {
    // 管理员根路径定向到登录页
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/AdminLogin/index.html";
    }

}
