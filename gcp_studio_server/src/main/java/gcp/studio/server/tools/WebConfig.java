package gcp.studio.server.tools;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Paths;


// 配置静态资源映射
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 图片验证码映射：/captcha/** -> {运行目录}/User/captcha/
        String captchaPath = Paths.get(System.getProperty("user.dir"), "User", "captcha")
                .toAbsolutePath().toUri().toString() + "/";
        registry.addResourceHandler("/captcha/**")
                .addResourceLocations(captchaPath);

        // 用户头像映射：/avatar/** -> {运行目录}/User/avatar/
        String avatarPath = Paths.get(System.getProperty("user.dir"), "User", "avatar")
                .toAbsolutePath().toUri().toString() + "/";
        registry.addResourceHandler("/avatar/**")
                .addResourceLocations(avatarPath);
    }
    
}