package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.entity.UserInfo;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    @Select("SELECT MAX(user_account) FROM user_info WHERE user_account LIKE CONCAT(#{prefix}, '%')")
    String selectMaxAccountLike(@Param("prefix") String prefix);
}
