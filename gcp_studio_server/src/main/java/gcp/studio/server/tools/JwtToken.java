package gcp.studio.server.tools;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtToken {

    // Token生成密钥
    @Value("${jwt.secret}")
    private String jwtSecret;

    // Token时限
    @Value("${jwt.exp}")
    private int jwtExp;

    // 生成密钥
    private SecretKey getSigningKey() {
        // 将jwtSecret字符串转换为 SecretKey 对象。
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     *
     * @param email 用户名
     * @param role 用户类型
     * @return Token
     */
    // 生成Token
    public String createToken(String email, String role) {
        Date now = new Date();
        Date exp= new Date(now.getTime() + jwtExp * 60 * 60 * 1000L);

        // Token荷载: email: 用户名, role: 用户权限, iat: 签发时间, exp: 过期时间
        return Jwts.builder()
                // 添加用户信息
                .claim("email", email)
                // 添加权限信息
                .claim("role", role)
                // 添加签发时间
                .setIssuedAt(now)
                // 添加过期时间
                .setExpiration(exp)
                // 添加签名
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                // 生成Token
                .compact();
    }

    /**
     * @param token 用户令牌
     * @return 令牌信息 含用户名、权限、签发时间、过期时间
     */
    // 解码Token
    private Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new IllegalArgumentException("无效的 Token", e);
        }
    }

    /**
     * @param token 用户令牌
     * @return 令牌是否过期
     */
    // 校验Token时效性
    public boolean checkExp(String token) {
        // 判断Token中的过期时间是否在当前时间之前，返回值：true为未过期，false为过期
        try {
            Claims claims = parseToken(token);
            Date now = new Date();
            return now.before(claims.getExpiration());
        } catch (Exception e) {
            // Token 不合法或解析失败，视为已过期或无效
            return false;
        }
    }

    /**
     * @param token 用户令牌
     * @param role 用户身份
     * @return 令牌身份是否匹配
     */
    // 校验Token身份
    public boolean checkRole(String token, String role) {
        try {
            Claims claims = parseToken(token);
            Object claimRole = claims.get("role");
            return role.equals(claimRole);
        } catch (Exception e) {
            // Token 不合法或角色字段不存在，视为身份不匹配
            return false;
        }
    }

    // 获取Token中的用户名
    public String getEmail(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.get("email").toString();
        } catch (Exception e) {
            // Token 不合法或用户名字段不存在，返回 null
            return null;
        }
    }
}
