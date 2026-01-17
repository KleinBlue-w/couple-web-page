package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import org.example.common.BusinessException;
import org.example.common.CommonReturn;
import org.example.dto.UpdateUserDTO;
import org.example.entity.UserInfo;
import org.example.mapper.UserInfoMapper;
import org.example.service.UserAccountService;
import org.example.service.UserInfoService;
import org.example.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.xml.transform.Result;
import java.time.LocalDateTime;

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
            throw  new BusinessException(400,"用户名已存在");
        }


        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userName);
        userInfo.setUserPassword(SecurityUtils.encrypt(userPassword));
        userAccountService.createUser(userInfo);// 存储加密后的密码
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
    public UserInfo getUserInfoById(Long id) {
        return userInfoMapper.selectById(id);
    }

    /**
     * 根据id更新用户头像url
     * @param id
     * @param url
     * @return
     */
    @Override
    public Boolean uploadAvatarById(Long id, String url) {
        // 参数校验
        if (id == null || StringUtils.isBlank(url)) {
            return false;
        }

        // 使用LambdaUpdateWrapper构建更新条件
        LambdaUpdateWrapper<UserInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserInfo::getId, id)
                .set(UserInfo::getAvatar, url)
                .set(UserInfo::getModifyTime, LocalDateTime.now()); // 可选

        int rows = userInfoMapper.update(null, wrapper);
        return rows > 0;
    }

    /**
     * 根据UserId更新用户除密码和头像以外的所有信息
     * @param updateUserDTO
     * @return
     */
    @Override
    public UpdateUserDTO updateUserById(UpdateUserDTO updateUserDTO) {
        // 参数校验
        if (updateUserDTO.getId() == null) {
            throw new BusinessException(400,"id不允许为空");
        } else if (StringUtils.isBlank(updateUserDTO.getUserName())) {
            throw new BusinessException(400,"用户名不允许为空");
        } else if (StringUtils.isBlank(updateUserDTO.getGender())) {
            throw new BusinessException(400,"性别不允许为空");
        } else if (updateUserDTO.getBirthday() == null || updateUserDTO.getBirthday().isEmpty()) {
            throw new BusinessException(400,"出生日期不允许为空");
        }

        // 使用LambdaUpdateWrapper构建更新条件
        LambdaUpdateWrapper<UserInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserInfo::getId, updateUserDTO.getId())
                .set(UserInfo::getUserName, updateUserDTO.getUserName())
                .set(UserInfo::getGender, updateUserDTO.getGender())
                .set(UserInfo::getBirthday, updateUserDTO.getBirthday())
                .set(updateUserDTO.getBindAccount() != null && !updateUserDTO.getBindAccount().trim().isEmpty(),
                        UserInfo::getBindAccount, updateUserDTO.getBindAccount())
                .set(UserInfo::getModifyTime, LocalDateTime.now()); // 可选

        int rows = userInfoMapper.update(null, wrapper);
        if(rows > 0){
            return updateUserDTO;
        }else {
            return null;
        }
    }

    /**
     * 根据userId解绑绑定账号
     * @param id
     * @return
     */
    @Override
    public Boolean unbindAccount(Long id) {
        // 参数校验
        if (id == null || StringUtils.isBlank(id.toString())) {
            return false;
        }

        LambdaUpdateWrapper<UserInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserInfo::getId, id)
                .set(UserInfo::getModifyTime, LocalDateTime.now())
                .set(UserInfo::getBindAccount, null);

        int rows = userInfoMapper.update(null, wrapper);
        return rows > 0;
    }

    /**
     * 根据userId绑定账号
     * @param id
     * @return
     */
    @Override
    public CommonReturn<Boolean> bindAccount(Long id, String userAccount) {
        // 先查询是否存在该账号
        Long count = userInfoMapper.selectCount(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getUserAccount, userAccount));
        if(count > 0){
            // 绑定账号 -- 根据id更新绑定账号
            LambdaUpdateWrapper<UserInfo> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(UserInfo::getId, id)
                    .set(UserInfo::getModifyTime, LocalDateTime.now())
                    .set(UserInfo::getBindAccount, userAccount);
            int rows = userInfoMapper.update(null, wrapper);
            return rows > 0 ? CommonReturn.ok(true) : CommonReturn.fail(400,"绑定失败");
        }else{
            return CommonReturn.fail(400,"该账号不存在");
        }
    }

    @Override
    public UserInfo whetherExists(String bindAccount) {
        return userInfoMapper.selectOne(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getBindAccount, bindAccount));
    }
}
