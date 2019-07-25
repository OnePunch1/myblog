package com.he.myblog.dao;

import com.he.myblog.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface AdminUserMapper {
    AdminUser login(@Param("userName") String userName,@Param("password") String password);
    AdminUser selectByPrimaryKey(Integer adminUserId);
    int updateName(AdminUser adminUser);
}
