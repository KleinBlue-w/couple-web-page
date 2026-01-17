package org.example.service;

import org.example.common.CommonReturn;
import org.example.dto.UpdateUserDTO;
import org.example.entity.UserInfo;

import javax.xml.transform.Result;

public interface UserInfoService {
    // 用户注册
    UserInfo register(String userName, String userPassword);

    // 用户登录:只比对用户名+密码
    UserInfo login(String userAccount, String rawPassword);

    // 根据UserId查询头像
    UserInfo getUserInfoById(Long id);

    // 根据UserId更新头像
    Boolean uploadAvatarById(Long id, String url);

    // 根据UserId更新用户除密码和头像以外的所有信息
    UpdateUserDTO updateUserById(UpdateUserDTO updateUserDTO);

    // 根据userId解绑绑定账号
    Boolean unbindAccount(Long id);

    // 根据userId绑定账号
    CommonReturn<Boolean> bindAccount(Long id, String userAccount);

    // 判断绑定账号是否存在
    UserInfo whetherExists(String bindAccount);
}
