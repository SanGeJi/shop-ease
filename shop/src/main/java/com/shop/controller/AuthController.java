package com.shop.controller;

import com.shop.common.AuthUtil;
import com.shop.common.Result;
import com.shop.common.UserContext;
import com.shop.entity.Session;
import com.shop.entity.User;
import com.shop.mapper.SessionMapper;
import com.shop.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired private UserMapper userMapper;
    @Autowired private SessionMapper sessionMapper;

    @PostMapping("/register")
    public Result register(@RequestBody Map<String,String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || username.trim().length() < 2) return Result.error("用户名至少2个字符");
        if (password == null || password.length() < 6) return Result.error("密码至少6位");
        if (userMapper.findByUsername(username) != null) return Result.error("用户名已存在");
        String salt = AuthUtil.genSalt();
        User u = new User();
        u.setUsername(username);
        u.setPasswordHash(AuthUtil.hashPassword(password, salt));
        u.setSalt(salt);
        u.setRole("customer");
        userMapper.insert(u);
        String token = createSession(u);
        return Result.ok(Map.of("token", token, "user", safeUser(u)));
    }

    @PostMapping("/login")
    public Result login(@RequestBody Map<String,String> body) {
        String username = body.get("username");
        String password = body.get("password");
        User u = userMapper.findByUsername(username);
        if (u == null || !AuthUtil.hashPassword(password, u.getSalt()).equals(u.getPasswordHash()))
            return Result.error("用户名或密码错误");
        String token = createSession(u);
        return Result.ok(Map.of("token", token, "user", safeUser(u)));
    }

    @PostMapping("/logout")
    public Result logout(@RequestHeader("Authorization") String auth) {
        String token = auth.startsWith("Bearer ") ? auth.substring(7) : "";
        sessionMapper.deleteByToken(token);
        return Result.ok();
    }

    @GetMapping("/me")
    public Result me(@RequestHeader("Authorization") String auth) {
        String token = (auth != null && auth.startsWith("Bearer ")) ? auth.substring(7) : null;
        Session s = (token == null) ? null : sessionMapper.findByToken(token);
        if (s == null) return Result.error(401, "未登录或登录已过期");
        User u = userMapper.findById(s.getUserId());
        return u == null ? Result.error(401, "未登录或登录已过期") : Result.ok(Map.of("user", safeUser(u)));
    }

    private String createSession(User u) {
        Session s = new Session();
        s.setToken(AuthUtil.genToken());
        s.setUserId(u.getId());
        s.setRole(u.getRole());
        s.setExpiresAt(LocalDateTime.now().plusDays(30));
        sessionMapper.insert(s);
        return s.getToken();
    }

    private Map<String,Object> safeUser(User u) {
        return Map.of("id", u.getId(), "username", u.getUsername(), "role", u.getRole());
    }
}
