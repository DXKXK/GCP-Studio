package gcp.studio.server.tools;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CaptchaEmail {

    private final JavaMailSenderImpl mailSender;
    // 发送验证码的邮箱的相关配置
    @Value("${spring.mail.username}")
    private String EMAIL_USERNAME;
    @Value("${spring.mail.password}")
    private String EMAIL_PASSWORD;
    @Value("${spring.mail.host}")
    private String EMAIL_HOST;

    public CaptchaEmail(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    // 生成验证码(1-9,a-z,A-Z)随机五位
    private String createCaptcha() {
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder captcha = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 5; i++) {
            captcha.append(characters.charAt(rnd.nextInt(characters.length())));
        }
        return captcha.toString();
    }

    /**
     * 发送邮件验证码
     * @param email 目标邮箱
     * @param type 邮箱类型，注册(1) / 重置密码(2)
     * @return 邮箱验证码
     */
    // 发送邮件验证码(注册)
    public String sendCaptcha(String email, String type) {
        try{
            String captcha = createCaptcha();
            String title = "";
            String contentType = "";

            if ("1".equals(type)){
                title = "欢迎注册匠心客工作室";
                contentType = "<h3>欢迎您注册成为匠心客工作室的访客</h3>";
            }else {
                title = "匠心客工作室密码重置";
                contentType = "<h3>您正在重置匠心客工作室访客账号密码</h3>";
            }

            // 邮件内容(采用HTML邮件)
            String content =
                    "<div style='font-family:Microsoft YaHei;color:#333;'>"
                        + contentType
                        + "<h3>您的验证码：</h3>"
                        + "<p style='font-size:24px;font-weight:bold;color:#1890ff;'>" + captcha + "</p>"
                        + "<p>有效期5分钟，请勿泄露给他人</p>"
                        + "<hr style='border:none;border-top:1px solid #eee;'/>"
                        + "<p style='color:#999;font-size:12px;'>系统自动发送，请勿回复</p>"
                    + "</div>";

            // 发送
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // 发送设置
            helper.setFrom(EMAIL_USERNAME);
            helper.setTo(email);
            helper.setSubject(title);
            helper.setText(content, true);

            mailSender.send(message);
            return captcha;
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }
}
