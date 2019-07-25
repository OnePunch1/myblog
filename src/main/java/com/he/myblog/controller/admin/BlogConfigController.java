package com.he.myblog.controller.admin;

import com.he.myblog.service.BlogConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("/admin")
@Controller
public class BlogConfigController {
    @Autowired
    BlogConfigService blogConfigService;

    @GetMapping("/configurations")
    public String config(HttpServletRequest request){
        request.setAttribute("path","configurations");
        request.setAttribute("configurations",blogConfigService.getAllConfig());
        return "admin/configuration";
    }
}
