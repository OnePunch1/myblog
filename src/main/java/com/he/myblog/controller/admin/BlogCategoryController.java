package com.he.myblog.controller.admin;

import com.he.myblog.service.BlogCategoryService;
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
public class BlogCategoryController {
    @Autowired
    BlogCategoryService blogCategoryService;

    @GetMapping("/categories")
    public String categoryPage(HttpServletRequest request){
        request.setAttribute("path","categories");
        return "admin/category";
    }
    @GetMapping("/categories/list")
    @ResponseBody
    public Result list(@RequestParam Map<String ,Object> param){
        if(StringUtils.isEmpty("limit")||StringUtils.isEmpty("page")){
            return ResultGenerator.genFailResult("参数错误!");
        }
        return ResultGenerator.genSuccessResult(blogCategoryService.findCategoryList(new PageQueryUtil(param)));
    }
    @PostMapping("/categories/save")
    @ResponseBody
    public Result save(@RequestParam("categoryName")String categoryName,
                       @RequestParam("categoryIcon")String categoryIcon){
        if(StringUtils.isEmpty(categoryName)){
            return ResultGenerator.genFailResult("请输入分类名称!");
        }
        if(StringUtils.isEmpty(categoryIcon)){
            return ResultGenerator.genFailResult("请选择分类图标!");
        }
        if(blogCategoryService.addCategory(categoryName,categoryIcon)){
            return ResultGenerator.genSuccessResult("添加成功!");
        }else{
            return ResultGenerator.genFailResult("添加失败,分类名称重复!");
        }
    }
    @PostMapping("/categories/update")
    @ResponseBody
    public Result update(@RequestParam("categoryId") Integer categoryId,
                         @RequestParam("categoryName") String categoryName,
                         @RequestParam("categoryIcon")String categoryIcon){
        if(StringUtils.isEmpty(categoryName)){
            return ResultGenerator.genFailResult("请输入分类名称!");
        }
        if(StringUtils.isEmpty(categoryIcon)){
            return ResultGenerator.genFailResult("请选择分类图标!");
        }
        if(blogCategoryService.updateCategory(categoryId,categoryName,categoryIcon)){
            return ResultGenerator.genSuccessResult("修改成功!");
        }else{
            return ResultGenerator.genFailResult("修改失败!");
        }
    }
    @PostMapping("/categories/delete")
    @ResponseBody
    public Result deleteBatch(@RequestBody Integer[] ids){
        if(ids.length<1){
            return ResultGenerator.genFailResult("参数错误!");
        }
        if(blogCategoryService.deleteBatch(ids)){
            return ResultGenerator.genSuccessResult("删除成功!");
        }else{
            return ResultGenerator.genFailResult("删除失败!");
        }
    }

}
