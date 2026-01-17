package org.example.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import org.example.common.BusinessException;
import org.example.common.CommonReturn;
import org.example.dto.UpdateUserDTO;
import org.example.dto.UploadAvatarDTO;
import org.example.entity.UserInfo;
import org.example.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.dto.RegisterDTO;
import org.example.dto.LoginDTO;
import org.example.vo.UserVO;
import org.example.utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.transform.Result;
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
        UserInfo user = userInfoService.login(dto.getUserAccount(), dto.getPassword());
        if (user == null) {
            throw new BusinessException(400,"账号或密码错误");
        }
        return getMapCommonReturn(user);
    }

    /* 根据userID获取用户头像 */
    @GetMapping("/getUserInfo")
    public CommonReturn<UserInfo> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId"); // 拦截器已放好
        UserInfo userInfo = userInfoService.getUserInfoById(userId);  // 允许 null

        return CommonReturn.ok(userInfo);
    }

    /* 根据userId更新用户头像 */
    @PostMapping("/uploadAvatar")
    public CommonReturn<Boolean> uploadAvatar(@RequestBody UploadAvatarDTO dto) {
        try {
            // 验证参数
            if (dto.getId() == null || StringUtils.isBlank(dto.getUrl())) {
                return CommonReturn.fail(400, "参数不能为空");
            }

            // 验证Base64格式
            String base64Avatar = dto.getUrl();
            if (!base64Avatar.startsWith("data:image/")) {
                return CommonReturn.fail(400, "头像格式不正确");
            }

            // 更新用户头像（Base64直接存储到数据库）
            Boolean isSuccess = userInfoService.uploadAvatarById(dto.getId(), base64Avatar);

            if (isSuccess) {
                return CommonReturn.ok(true);
            } else {
                return CommonReturn.fail(500, "头像更新失败");
            }
        } catch (Exception e) {
            return CommonReturn.fail(500, "上传失败");
        }
    }

    /* 根据userId更新除密码和头像外的所有用户信息 */
    @PostMapping("/updateUser")
    public CommonReturn<UpdateUserDTO> updateUser(@RequestBody @Valid UpdateUserDTO updateUserDTO) {
        // 手动处理 bindAccount 的空值
        if (updateUserDTO.getBindAccount() != null && updateUserDTO.getBindAccount().trim().isEmpty()) {
            updateUserDTO.setBindAccount(null);
        }

        UpdateUserDTO updateUserInfo = userInfoService.updateUserById(updateUserDTO);
        if (updateUserInfo == null) {
            throw new BusinessException(400, "个人信息更新失败");
        }
        return CommonReturn.ok(updateUserInfo);
    }

    /* 根据userId解绑绑定账号 */
    @PostMapping("/unbindAccount")
    public Boolean unbindAccount(@RequestParam Long id) {
        return userInfoService.unbindAccount(id);
    }

    @PostMapping("/bindAccount")
    public CommonReturn<Boolean> bindAccount(@RequestParam Long id, @RequestParam String bindAccount) {
        return userInfoService.bindAccount(id, bindAccount);
    }
}