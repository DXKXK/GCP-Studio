package gcp.studio.server.request;

import gcp.studio.server.tools.CaptchaEmail;
import gcp.studio.server.tools.CaptchaImage;
import gcp.studio.server.tools.JwtToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController

public class RequestTest {

    private final JwtToken jwtToken;
    private final CaptchaImage captchaImage;
    private final CaptchaEmail captchaEmail;

    public RequestTest(
            JwtToken jwtToken,
            CaptchaImage captchaImage,
            CaptchaEmail captchaEmail
    ) {
        this.jwtToken = jwtToken;
        this.captchaImage = captchaImage;
        this.captchaEmail = captchaEmail;
    }

    // 请求方式为Get，返回的数据不需要经过安全认证（测试用接口）
    // 模拟发送获取邮件验证码
    @GetMapping("/api/sendemail")
    public Map<String, Object> sendEmail(
            @RequestParam(required = false) String email
    ){
        Map<String, Object> result = new HashMap<>();
        String captcha = captchaEmail.sendCaptcha(email, "1");

        if (captcha.equals("error")){
            result.put("msg", "发送失败");
            result.put("code", 500);
            result.put("data", null);
        }else{
            result.put("msg", "发送成功");
            result.put("code", 200);
            result.put("data", captcha);
        }
        return result;
    }

    // 请求方式为Get，返回的数据不需要经过安全认证（测试用接口）
    // 模拟生成验证码
    @GetMapping("/api/test")
    public Map<String, Object> test(
            @RequestParam(required = false) String email
    ) {
        Map<String, Object> result = new HashMap<>();
        Map<String , Object> data = new HashMap<>();
        Map<String , Object> Captch = captchaImage.createCaptcha(email);

        // 构建数据
        data.put("code", Captch.get("code"));
        data.put("path", Captch.get("path"));

        result.put("msg", "成功");
        result.put("code", 200);
        result.put("data", data);
        return result;
    }

    // 请求方式为Post，返回的数据不需要经过安全认证（测试用接口）
    // 模拟返回Token
    @PostMapping("/api/settype1")
    public Map<String, Object> postType1(
            @RequestBody Map<String, String> body
    ) {
        Map<String, Object> result = new HashMap<>();
        Map<String , Object> data = new HashMap<>();
        String email = body.get("email");
        String token = jwtToken.createToken(email, "admin");

        // 构建数据
        data.put("token", token);
        data.put("email", email);
        data.put("isadmin", jwtToken.checkRole(token, "admin"));
        data.put("istourist", jwtToken.checkRole(token, "tourist"));
        data.put("isstudio", jwtToken.checkRole(token, "studio"));
        data.put("isexp", jwtToken.checkExp(token));

        result.put("msg", "ok");
        result.put("code", 200);
        result.put("data", data);
        return result;
    }

    // 请求方式为Post，返回的数据需要经过安全认证（测试用接口）
    @PostMapping("/api/settype2")
    public Map<String, Object> postType2(
            // 接收的数据
            @RequestBody Map<String, String> body,
            // 安全验证
            @RequestHeader("Authorization") String Token
    ) {
        Map<String, Object> result = new HashMap<>();
        if (Token.equals("123456")) {
            result.put("msg", "验证成功");
            result.put("code", 200);
            result.put("data", body);
        }else{
            result.put("msg", "验证失败");
            result.put("code", 401);
            result.put("data", null);
        }
        return result;
    }

    // 请求方式为Get，返回的数据不需要经过安全认证（测试用接口）
    @GetMapping("/api/get")
    public Map<String, Object> getTest() {
        // 构建需要返回的数据（测试用接口）
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> user1 = new HashMap<>();
        user1.put("id", 1);
        user1.put("name", "张三");
        user1.put("status", "普通用户");
        dataList.add(user1);
        Map<String, Object> user2 = new HashMap<>();
        user2.put("id", 2);
        user2.put("name", "李四");
        user2.put("status", "管理员");
        dataList.add(user2);

        Map<String, Object> result = new HashMap<>();
        result.put("msg", "OK");
        result.put("code", 200);
        result.put("data", dataList);
        return result;
    }
}
