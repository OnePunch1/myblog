package com.he.myblog.controller.admin;

import com.he.myblog.entity.AdminUser;
import com.he.myblog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminUserService adminUserService;
    @Autowired
    BlogTagService blogTagService;
    @Autowired
    BlogLinkService blogLinkService;
    @Autowired
    BlogService blogService;
    @Autowired
    BlogCategoryService blogCategoryService;
    @Autowired
    BlogCommentService blogCommentService;

    @GetMapping({"","/","/index.html","/index"})
    public String index(HttpServletRequest request) {
        request.setAttribute("path","index");
        request.setAttribute("blogCount",blogService.getTotalBlogs());
        request.setAttribute("commentCount",blogCommentService.getTotalBlogComments());
        request.setAttribute("categoryCount",blogCategoryService.getTotalCategories());
        request.setAttribute("tagCount",blogTagService.getTotalTags());
        request.setAttribute("linkCount",blogLinkService.getTotalLinks());
        request.setAttribute("path","index");
        return "admin/index";
    }

    @GetMapping("/login")
    public String login(){
        return "admin/login";
    }
    @PostMapping(value = "/login")
    public String login(@RequestParam("userName")String userName,
                        @RequestParam("password")String password,
                        @RequestParam("verifyCode")String verifyCode,
                        HttpSession session){
        if(StringUtils.isEmpty(verifyCode)){
            session.setAttribute("errorMsg","验证码不能为空!");
            return "admin/login";
        }
        if (StringUtils.isEmpty(userName)||StringUtils.isEmpty(password)){
            session.setAttribute("errorMsg","用户名或密码不能为空!");
            return "admin/login";
        }
        String captchaCode=session.getAttribute("verifyCode")+"";
        if(StringUtils.isEmpty(captchaCode)||!verifyCode.equals(captchaCode)){
            session.setAttribute("errorMsg","验证码错误!");
            return "admin/login";
        }
        AdminUser adminUser=adminUserService.login(userName,password);
        if(adminUser!=null){
            session.setAttribute("loginUser",adminUser.getNickName());
            session.setAttribute("loginUserId",adminUser.getAdminUserId());
            session.setMaxInactiveInterval(2*60*60);
            return "redirect:/admin/index";
        }else {
            session.setAttribute("errorMsg","用户名或密码错误!");
            return "admin/login";
        }
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest request){
        Integer loginUserId=(int)request.getSession().getAttribute("loginUserId");
        AdminUser adminUser=adminUserService.selectAdminUserById(loginUserId);
        if(adminUser==null){
            return "admin/login";
        }
        request.setAttribute("path","profile");
        request.setAttribute("loginUserName",adminUser.getLoginUserName());
        request.setAttribute("nickName",adminUser.getNickName());
        return "admin/profile";
    }
    @PostMapping("/profile/name")
    @ResponseBody
    public String profileName(HttpServletRequest request,
                              @RequestParam("loginUserName") String loginUserName,
                              @RequestParam("nickName")String nickName){
        if(StringUtils.isEmpty(loginUserName)||StringUtils.isEmpty(nickName)){
            return "参数不能为空!";
        }
        Integer loginUserId=(int)request.getSession().getAttribute("loginUserId");
        if(adminUserService.updateName(loginUserId,loginUserName,nickName)){
            request.setAttribute("loginUserName",loginUserName);
            request.setAttribute("nickName",nickName);
            request.getSession().setAttribute("loginUser",nickName);
            request.getSession().setAttribute("loginUserId",loginUserId);
            return "success";
        }else{
            return "修改失败!";
        }
    }
    @PostMapping("/profile/password")
    @ResponseBody
    public String profilePassword(HttpServletRequest request,
                                  @RequestParam("originalPassword")String originalPassword,
                                  @RequestParam("newPassword")String newPassword){
        if(StringUtils.isEmpty(originalPassword)||StringUtils.isEmpty(newPassword)){
            return "参数不能为空!";
        }
        Integer loginUserId=(int)request.getSession().getAttribute("loginUserId");
        if(adminUserService.updatePassword(loginUserId,originalPassword,newPassword)){
            return "success";
        }else {
            return "修改失败!";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute("loginUserId");
        request.getSession().removeAttribute("loginUser");
        request.getSession().removeAttribute("errorMsg");
        return "admin/login";
    }
}
