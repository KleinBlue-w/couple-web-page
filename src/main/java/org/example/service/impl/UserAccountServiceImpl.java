package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.UserInfo;
import org.example.mapper.UserInfoMapper;
import org.example.service.UserAccountService;
import org.example.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final UserInfoMapper userInfoMapper;
    private static final DateTimeFormatter nowTime = DateTimeFormatter.ofPattern("yyMMdd");

    /**
     * 生成业务账号并插入用户记录（事务边界）
     */
    @Transactional
    public void createUser(UserInfo user) {
        // 1. 加密密码
        user.setUserPassword(SecurityUtils.encrypt(user.getUserPassword()));

        // 2. 生成唯一账号
        String account = generateAccount();
        user.setUserAccount(account);

        // 3. 落库
        int rows = userInfoMapper.insert(user);
        if (rows != 1) {
            throw new RuntimeException("用户创建失败");
        }
    }

    /* 生成 U + yyMMdd + 三位序号 */
    private String generateAccount() {
        String prefix = "U" + LocalDate.now().format(nowTime);
        String max = userInfoMapper.selectMaxAccountLike(prefix);
        int seq = max == null ? 1 : Integer.parseInt(max.substring(max.length() - 3)) + 1;
        return String.format("%s%03d", prefix, seq);
    }
}