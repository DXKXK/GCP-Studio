package gcp.studio.server.tools;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Component
public class CaptchaImage {

    // 服务器运行目录下的验证码存储路径
    @Value("${captcha.path}")
    private String CAPTCHA_PATH;

    /**
     * 生成验证码并保存到服务器运行目录
     * @param email 用户邮箱（会过滤非法字符）
     * @return 验证码文本（如 "A3b7E"）
     */
    // 生成验证码
    public Map<String , Object> createCaptcha(String email) {
        Map<String , Object> data = new HashMap<>();
        try{
            // 过滤邮箱中的非法字符
            String userEmail = email.replaceAll("[^a-zA-Z0-9@._-]", "_");
            // 创建验证码（5位字符）
            LineCaptcha captcha = CaptchaUtil.createLineCaptcha(200, 100, 5, 50);
            String code = captcha.getCode();
            // 保存路径：{运行目录}/captcha/User/{userEmail}/captcha.png
            File dir = new File(CAPTCHA_PATH, userEmail);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 保存图片到指定为止
            File imageFile = new File(dir, "captcha.png");
            captcha.write(imageFile);

            // 验证码位置：{运行目录}/captcha/User/{userEmail}/captcha.png
            String captchaPath = "/captcha/" + userEmail + "/captcha.png";

            // 构建返回数据
            data.put("path", captchaPath);
            data.put("code", code);
        } catch (Exception e){
            data.put("path", "验证码生成失败");
            data.put("code", "");
            return data;
        }
        return data;
    }

    // 清理验证码
    public boolean clearCaptcha(String email) {
        try{
            String userEmail = email.replaceAll("[^a-zA-Z0-9@._-]", "_");
            File dir = new File(CAPTCHA_PATH, userEmail);

            if (!dir.exists()) {
                // 无数据残留
                return true;
            }

            // 遍历目录下的所有文件并删除
            for (File file : dir.listFiles()) {
                if (file != null && file.exists()) {
                    file.delete();
                }
            }
            // 删除空目录
            return dir.delete();
        }catch (Exception e){
            return false;
        }
    }
}
