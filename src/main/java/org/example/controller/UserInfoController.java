package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.BusinessException;
import org.example.common.CommonReturn;
import org.example.entity.UserInfo;
import org.example.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.example.dto.RegisterDTO;
import org.example.dto.LoginDTO;
import org.example.vo.UserVO;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserInfoController {

    @Autowired
    private final UserInfoService userInfoService;

    /* 注册 POST /user/register  {username, password} */
    @PostMapping("/register")
    public CommonReturn<Void> register(@RequestBody RegisterDTO dto) {
        try {
            userInfoService.register(dto.getUsername(), dto.getPassword());
            return CommonReturn.ok(null);
        } catch (RuntimeException e) {
            return CommonReturn.fail(400,e.getMessage());
        }
    }

    /* 登录 POST /user/login  {username, password, bindAccount} */
    @PostMapping("/login")
    public CommonReturn<UserVO> login(@RequestBody LoginDTO dto) {
        UserInfo user = userInfoService.login(dto.getUsername(), dto.getPassword());
        if (user == null) {
            throw new BusinessException(400,"用户名或密码错误");
        }
        // 成功返回脱敏信息
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUserName());
        return CommonReturn.ok(vo);
    }
}