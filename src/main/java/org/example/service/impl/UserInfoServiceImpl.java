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
    public UserInfo register(String userName, String userPassword) {
        boolean exists = userInfoMapper.selectCount(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getUserName, userName)) > 0;
        if (exists) {
            throw new RuntimeException("用户名已存在");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userName);
        userInfo.setUserPassword(userPassword);   // 记得后面做密码加密
        userInfoMapper.insert(userInfo);          // 主键已回填
        return userInfo;                          // 带 id 返回
    }

    @Override
    public UserInfo login(String userAccount, String rawPassword) {
        UserInfo user = userInfoMapper.selectOne(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getUserAccount, userAccount));
        return user != null && SecurityUtils.matches(rawPassword, user.getUserPassword())
                ? user : null;
    }

    /**
     * 根据id查询用户头像URL
     * @param id
     * @return
     */
    @Override
    public String getAvatarById(Long id) {
        if (id == null) return "";                    // 兜底
        UserInfo user = userInfoMapper.selectById(id); // MyBatis-Plus
        return user == null ? "" : user.getAvatar();
    }

    @Override
    public UserInfo whetherExists(String bindAccount) {
        return userInfoMapper.selectOne(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getBindAccount, bindAccount));
    }
}
