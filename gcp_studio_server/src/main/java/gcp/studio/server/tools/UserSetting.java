package gcp.studio.server.tools;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserSetting {

    // 服务器运行目录下的头像文件夹
    @Value("${avatar.path}")
    private String AVATAR_PATH;


}
