package gcp.studio.server.request;

import gcp.studio.server.tools.CaptchaImage;
import gcp.studio.server.tools.JwtToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class User {

    private final JwtToken jwtToken;
    private final CaptchaImage captchaImage;

    public User(
            JwtToken jwtToken,
            CaptchaImage captchaImage
    ) {
        this.jwtToken = jwtToken;
        this.captchaImage = captchaImage;
    }

    // 连通性接口，用于前端辨别服务器是否在线
    @GetMapping("/api/serveronline")
    public Map<String, Object> serverOnline() {
        Map<String, Object> result = new HashMap<>();
        result.put("msg", "ServerOnline");
        result.put("code", 200);
        return result;
    }

    // 用户接口（测试用接口）
    @GetMapping("/api/usertest")
    public Map<String, Object> userTest(
            @RequestParam(required = false) String email
    ) {
        Map<String, Object> data =  new HashMap<>();
        String token = jwtToken.createToken(email, "admin");

        // 构建数据
        data.put("email", email);
        data.put("token", token);
        Map<String, Object> result = new HashMap<>();
        result.put("msg", "登录成功");
        result.put("code", 200);
        result.put("data", data);
        return result;
    }
}
