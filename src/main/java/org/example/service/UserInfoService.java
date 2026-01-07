package org.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.example.entity.UserInfo;
import org.example.mapper.UserInfoMapper;
import org.example.utils.SecurityUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final UserInfoMapper userInfoMapper;
    private final UserAccountService userAccountService;

    // 用户注册
    public void register(String userName, String userPassword){
        // 1. 重名校验
        boolean exists = userInfoMapper.selectCount(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUserName, userName)) > 0;
        if (exists) {
            throw new RuntimeException("用户名已存在");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userName);
        userInfo.setUserPassword(userPassword);
        userAccountService.createUser(userInfo);
    }

    // 用户登录:只比对用户名+密码
    public UserInfo login(String userAccount, String rawPassword) {
        UserInfo user = userInfoMapper.selectOne(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getUserAccount, userAccount));
        return user != null && SecurityUtils.matches(rawPassword, user.getUserPassword())
                ? user : null;
    }

    // 判断绑定账号是否存在
    public UserInfo whetherExists(String bindAccount){
        return userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getBindAccount,bindAccount));
    }
}
