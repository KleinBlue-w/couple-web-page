package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.example.entity.UserInfo;
import org.example.mapper.UserInfoMapper;
import org.example.service.UserAccountService;
import org.example.service.UserInfoService;
import org.example.utils.SecurityUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoMapper userInfoMapper;
    private final UserAccountService userAccountService;

    @Override
    public void register(String userName, String userPassword) {
        boolean exists = userInfoMapper.selectCount(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getUserName, userName)) > 0;
        if (exists) {
            throw new RuntimeException("用户名已存在");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userName);
        userInfo.setUserPassword(userPassword);
        userAccountService.createUser(userInfo);
    }

    @Override
    public UserInfo login(String userAccount, String rawPassword) {
        UserInfo user = userInfoMapper.selectOne(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getUserAccount, userAccount));
        return user != null && SecurityUtils.matches(rawPassword, user.getUserPassword())
                ? user : null;
    }

    @Override
    public UserInfo whetherExists(String bindAccount) {
        return userInfoMapper.selectOne(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getBindAccount, bindAccount));
    }
}
