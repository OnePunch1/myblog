package com.he.myblog.service;


import com.he.myblog.entity.AdminUser;

public interface AdminUserService {
    AdminUser login(String userName,String password);
    AdminUser selectAdminUserById(Integer loginUserId);
    boolean updateName(Integer loginUserId,String loginUserName,String nickName);
    boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword);
}
