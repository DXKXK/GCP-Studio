package gcp.studio.server.admin;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
public class AdminSession {

    // 管理员登录接口
    @PostMapping("/api/adminlogin")
    public Map<String, Object> AdminLogin(
            @RequestBody Map<String, String> request,
            //  设置会话
            HttpSession session
    ) {
        String password = request.get("password");
        Map<String, Object> result = new HashMap<>();

        // 临时密码：123456
        if ("123456".equals(password)) {

            // 设置会话，添加 adminAuth标识
            session.setAttribute("Allow", true);

            // 构建返回数据
            result.put("code", 200);
            result.put("msg", "ok");
            result.put("data", "/Admin/index.html");
        } else {
            result.put("code", 400);
            result.put("msg", "密码错误");
            result.put("data", "");
        }
        return result;
    }

    // 管理员退出登录接口
    @PostMapping("/api/adminlogout")
    public ResponseEntity<Void> AdminLogout(
            HttpSession session
    ) {
        session.invalidate(); // 销毁会话
        return ResponseEntity.ok().build();
    }

}
