package com.he.myblog.controller.admin;

import com.he.myblog.service.BlogTagService;
import com.he.myblog.util.PageQueryUtil;
import com.he.myblog.util.Result;
import com.he.myblog.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class BlogTagController {
    @Autowired
    BlogTagService blogTagService;

    @GetMapping("/tags")
    public String tagPage(HttpServletRequest request){
        request.setAttribute("path","tags");
        return "admin/tag";
    }
    @PostMapping("/tags/save")
    @ResponseBody
    public Result saveTag(@RequestParam("tagName") String tagName){
        if(StringUtils.isEmpty(tagName)){
            return ResultGenerator.genFailResult("参数不能为空!");
        }
        if(blogTagService.saveTag(tagName)){
            return ResultGenerator.genSuccessResult("添加成功!");
        }else{
            return ResultGenerator.genFailResult("标签名不能重复!");
        }
    }
    @GetMapping("/tags/list")
    @ResponseBody
    public Result list(@RequestParam Map<String,Object> map){
        if(StringUtils.isEmpty(map.get("page"))||StringUtils.isEmpty(map.get("limit"))){
            return ResultGenerator.genFailResult("参数错误!");
        }
        return ResultGenerator.genSuccessResult(blogTagService.list(new PageQueryUtil(map)));
    }
    @PostMapping("/tags/delete")
    @ResponseBody
    public Result deleteBatch(@RequestBody Integer[] ids){
        if(ids.length<1){
            return ResultGenerator.genFailResult("参数错误!");
        }
        if(blogTagService.deleteBatch(ids)){
            return ResultGenerator.genSuccessResult("删除成功!");
        }else{
            return ResultGenerator.genFailResult("有关联数据请勿强行删除!");
        }
    }
}
