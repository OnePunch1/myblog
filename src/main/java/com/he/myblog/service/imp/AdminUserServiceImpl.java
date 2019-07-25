package com.he.myblog.service.imp;

import com.he.myblog.dao.AdminUserMapper;
import com.he.myblog.entity.AdminUser;
import com.he.myblog.service.AdminUserService;
import com.he.myblog.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("adminUserService")
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    AdminUserMapper adminUserMapper;

    @Override
    public AdminUser login(String userName, String password) {
        return adminUserMapper.login(userName, MD5Util.MD5Encode(password,"utf-8"));
    }

    @Override
    public AdminUser selectAdminUserById(Integer loginUserId) {
        return adminUserMapper.selectByPrimaryKey(loginUserId);
    }

    @Override
    public boolean updateName(Integer loginUserId,String loginUserName,String nickName) {
        AdminUser adminUser=adminUserMapper.selectByPrimaryKey(loginUserId);
        if(adminUser!=null){
            adminUser.setLoginUserName(loginUserName);
            adminUser.setNickName(nickName);
            if(adminUserMapper.updateName(adminUser)>0){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword) {
        AdminUser adminUser=adminUserMapper.selectByPrimaryKey(loginUserId);
        if(adminUser!=null){
           if(adminUser.getLoginPassword().equals(MD5Util.MD5Encode(originalPassword,"utf-8"))){
               adminUser.setLoginPassword(MD5Util.MD5Encode(newPassword,"utf-8"));
               if(adminUserMapper.updateName(adminUser)>0){
                   return true;
               }
           }
        }
        return false;
    }
}
