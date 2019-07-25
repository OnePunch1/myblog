package com.he.myblog.controller.admin;

import com.he.myblog.config.Constants;
import com.he.myblog.entity.Blog;
import com.he.myblog.service.BlogCategoryService;
import com.he.myblog.service.BlogService;
import com.he.myblog.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/admin")
public class BlogController {
    @Autowired
    BlogCategoryService blogCategoryService;
    @Autowired
    BlogService blogService;
    @GetMapping("/blogs")
    public String list(HttpServletRequest request){
        request.setAttribute("path","blogs");
        return "admin/blog";
    }
    @GetMapping("/blogs/edit")
    public String edit(HttpServletRequest request){
        request.setAttribute("path","edit");
        request.setAttribute("categories",blogCategoryService.findCategoryList());
        return "admin/edit";
    }
    @GetMapping("/blogs/list")
    @ResponseBody
    public Result list(@RequestParam Map<String,Object> param){
        if(StringUtils.isEmpty(param.get("page"))||StringUtils.isEmpty(param.get("limit"))){
            return ResultGenerator.genFailResult("参数错误!");
        }
        PageResult pageResult=blogService.list(new PageQueryUtil(param));
        return ResultGenerator.genSuccessResult(pageResult);
    }

    @PostMapping("/blogs/save")
    @ResponseBody
    public Result save(@RequestParam("blogTitle")String blogTitle,
                       @RequestParam(name = "blogSubUrl",required = true)String blogSubUrl,
                       @RequestParam("blogCategoryId")Integer blogCategoryId,
                       @RequestParam("blogTags")String blogTags,
                       @RequestParam("blogContent")String blogContent,
                       @RequestParam("blogCoverImage")String blogCoverImage ,
                       @RequestParam("blogStatus")Byte blogStatus,
                       @RequestParam("enableComment")Byte enableComment){
        if(StringUtils.isEmpty(blogTitle)){
            return ResultGenerator.genFailResult("请输入文章标题!");
        }
        else if(blogTitle.trim().length()>150){
            return ResultGenerator.genFailResult("文章标题太长!");
        }
        if(StringUtils.isEmpty(blogTags)){
            return ResultGenerator.genFailResult("请输入文章标签!");
        }else if(blogTags.trim().length()>150){
            return ResultGenerator.genFailResult("文章标签过长!");
        }
        if(blogSubUrl.trim().length()>150){
            return ResultGenerator.genFailResult("路径过长!");
        }
        if(StringUtils.isEmpty(blogContent)){
            return ResultGenerator.genFailResult("内容不能为空!");
        }else if(blogContent.trim().length()>100000){
            return ResultGenerator.genFailResult("内容不能过长!");
        }
        if(StringUtils.isEmpty(blogCoverImage)){
            return ResultGenerator.genFailResult("封面图不能为空!");
        }
        Blog blog=new Blog();
        blog.setBlogTitle(blogTitle);
        blog.setBlogTags(blogTags);
        blog.setBlogCategoryId(blogCategoryId);
        blog.setBlogContent(blogContent);
        blog.setBlogSubUrl(blogSubUrl);
        blog.setBlogCoverImage(blogCoverImage);
        blog.setBlogStatus(blogStatus);
        blog.setEnableComment(enableComment);
        String saveBlog=blogService.saveBlog(blog);
        if("success".equals(saveBlog)){
            return ResultGenerator.genSuccessResult("添加成功!");
        }
        return ResultGenerator.genFailResult(saveBlog);
    }
    @PostMapping("/blogs/update")
    @ResponseBody
    public Result update(@RequestParam("blogId")Long blogId,
                         @RequestParam("blogTitle")String blogTitle,
                         @RequestParam(name = "blogSubUrl",required = true)String blogSubUrl,
                         @RequestParam("blogCategoryId")Integer blogCategoryId,
                         @RequestParam("blogTags")String blogTags,
                         @RequestParam("blogContent")String blogContent,
                         @RequestParam("blogCoverImage")String blogCoverImage ,
                         @RequestParam("blogStatus")Byte blogStatus,
                         @RequestParam("enableComment")Byte enableComment){
        if(StringUtils.isEmpty(blogTitle)){
            return ResultGenerator.genFailResult("请输入文章标题!");
        }
        else if(blogTitle.trim().length()>150){
            return ResultGenerator.genFailResult("文章标题太长!");
        }
        if(StringUtils.isEmpty(blogTags)){
            return ResultGenerator.genFailResult("请输入文章标签!");
        }else if(blogTags.trim().length()>150){
            return ResultGenerator.genFailResult("文章标签过长!");
        }
        if(blogSubUrl.trim().length()>150){
            return ResultGenerator.genFailResult("路径过长!");
        }
        if(StringUtils.isEmpty(blogContent)){
            return ResultGenerator.genFailResult("内容不能为空!");
        }else if(blogContent.trim().length()>100000){
            return ResultGenerator.genFailResult("内容不能过长!");
        }
        if(StringUtils.isEmpty(blogCoverImage)){
            return ResultGenerator.genFailResult("封面图不能为空!");
        }
        Blog blog = new Blog();
        blog.setBlogId(blogId);
        blog.setBlogTitle(blogTitle);
        blog.setBlogSubUrl(blogSubUrl);
        blog.setBlogCategoryId(blogCategoryId);
        blog.setBlogTags(blogTags);
        blog.setBlogContent(blogContent);
        blog.setBlogCoverImage(blogCoverImage);
        blog.setBlogStatus(blogStatus);
        blog.setEnableComment(enableComment);
        String blogUpdate=blogService.update(blog);
        if("success".equals(blogUpdate)){
            return ResultGenerator.genSuccessResult("修改成功!");
        }else{
            return ResultGenerator.genFailResult(blogUpdate);
        }
    }

    @PostMapping("/blogs/delete")
    @ResponseBody
    public Result deleteBatch(@RequestBody()Long[] ids){
        if(ids.length<1){
            return ResultGenerator.genFailResult("参数错误!");
        }
        if(blogService.deleteBatch(ids)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("删除失败!");
        }
    }

    @GetMapping("/blogs/edit/{blogId}")
    public String info(HttpServletRequest request,@PathVariable("blogId")Long blogId){
        request.setAttribute("path","edit");
        Blog blog=blogService.info(blogId);
        if(blog==null){
            return "error/error_400";
        }
        request.setAttribute("blog",blog);
        request.setAttribute("categories",blogCategoryService.findCategoryList());
        return "admin/edit";
    }
    @PostMapping("/blogs/md/uploadfile")
    public void uploadFileByEditormd(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam(name="editormd-image-file",required = true)
                                                 MultipartFile file) throws URISyntaxException, IOException {
        String fileName=file.getOriginalFilename();
        String suffixName=fileName.substring(fileName.lastIndexOf("."));
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r=new Random();
        StringBuilder tempName=new StringBuilder();
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
        String newFile=tempName.toString();
        File destFile=new File(Constants.FILE_UPLOAD_DIC+newFile);
        String fileUrl= MyBlogUtils.getHost(new URI(request.getRequestURL()+""))+"/upload/"+newFile;
        File fileDirctory=new File(Constants.FILE_UPLOAD_DIC);

        try {
            if(!fileDirctory.exists()){
                if(!fileDirctory.mkdir()){
                    throw  new IOException("文件夹创建失败:"+fileDirctory);
                }
            }
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            request.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type","text/html");
            response.getWriter().write("{\"success\":1,\"message\":\"success\",\"url\":\""+fileUrl+"\"}");
        } catch (UnsupportedEncodingException e) {
            response.getWriter().write("{\"success\":0}");
        } catch (IOException e) {
            response.getWriter().write("{\"success\":0}");
        }
    }
}
