package org.example.service;

import org.example.entity.UserInfo;

public interface UserInfoService {
    // 用户注册
    UserInfo register(String userName, String userPassword);

    // 用户登录:只比对用户名+密码
    UserInfo login(String userAccount, String rawPassword);

    // 根据UserId查询头像
    String getAvatarById(Long id);

    // 判断绑定账号是否存在
    UserInfo whetherExists(String bindAccount);
}
