package org.example.service;

import org.example.entity.UserInfo;

public interface  UserAccountService {
    /**
     * 生成业务账号并插入用户记录（事务边界）
     */
    void createUser(UserInfo user);
}