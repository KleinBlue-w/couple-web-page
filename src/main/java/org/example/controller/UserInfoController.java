package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.BusinessException;
import org.example.common.CommonReturn;
import org.example.entity.UserInfo;
import org.example.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.dto.RegisterDTO;
import org.example.dto.LoginDTO;
import org.example.vo.UserVO;
import org.example.utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserInfoController {

    @Autowired
    private final UserInfoService userInfoService;

    private final JwtUtil jwtUtil;

    /**
     * 获取登录后token
     * @param user
     * @return
     */
    private CommonReturn<Map<String, Object>> getMapCommonReturn(UserInfo user) {
        String token = jwtUtil.createToken(user.getId());

        Map<String,Object> res = new HashMap<>();
        res.put("token", token);
        res.put("user", new UserVO(user.getId(), user.getUserName()));
        return CommonReturn.ok(res);
    }

    /* 注册 POST /user/register  {username, password} */
    @PostMapping("/register")
    public CommonReturn<Map<String,Object>> register(@RequestBody RegisterDTO dto) {
        // 1. 注册
        UserInfo user = userInfoService.register(dto.getUsername(), dto.getPassword());
        // 2. 立即签发 token
        return getMapCommonReturn(user);
    }

    /* 登录 POST /user/login  {username, password, bindAccount} */
    @PostMapping("/login")
    public CommonReturn<Map<String,Object>> login(@RequestBody LoginDTO dto) {
        UserInfo user = userInfoService.login(dto.getUsername(), dto.getPassword());
        if (user == null) {
            throw new BusinessException(400,"用户名或密码错误");
        }
        return getMapCommonReturn(user);
    }

    /* 根据userID获取用户头像 */
    @GetMapping("/avatar")
    public CommonReturn<Map<String, String>> avatar(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId"); // 拦截器已放好
        String url = userInfoService.getAvatarById(userId);  // 允许 null
        Map<String, String> map = new HashMap<>();
        map.put("avatar", url == null ? "" : url);
        return CommonReturn.ok(map);
    }
}