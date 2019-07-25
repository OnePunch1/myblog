package com.he.myblog.controller.blog;

import com.he.myblog.controller.vo.BlogDetailVO;
import com.he.myblog.entity.BlogComment;
import com.he.myblog.entity.BlogConfig;
import com.he.myblog.entity.BlogLink;
import com.he.myblog.service.*;
import com.he.myblog.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class MyBlogController {
    public static String theme = "amaze";
    @Autowired
    BlogService blogService;
    @Autowired
    BlogTagService blogTagService;
    @Autowired
    BlogConfigService blogConfigService;
    @Autowired
    BlogCategoryService blogCategoryService;
    @Autowired
    BlogCommentService blogCommentService;
    @Autowired
    BlogLinkService blogLinkService;

    @GetMapping({"/","/index","/index.html"})
    public String index(HttpServletRequest request){
        return this.page(request,1);
    }

    @GetMapping("/page/{pageNum}")
    public String page(HttpServletRequest request, @PathVariable("pageNum")int pageNum){
        PageResult pageResult=blogService.getBlogsForIndexPage(pageNum);
        if(pageResult==null){
            return "error/error_404";
        }
        request.setAttribute("blogPageResult",pageResult);
        request.setAttribute("newBlogs",blogService.getBlogListForIndexPage(1));
        request.setAttribute("hotBlogs",blogService.getBlogListForIndexPage(0));
        request.setAttribute("hotTags",blogTagService.getBlogTagCountForIndex());
        request.setAttribute("pageName","首页");
        request.setAttribute("configurations",blogConfigService.getAllConfig());
        return "blog/"+theme+"/index";
    }

    @GetMapping("/categories")
    public String categories(HttpServletRequest request){
        request.setAttribute("hotTags",blogTagService.getBlogTagCountForIndex());
        request.setAttribute("categories",blogCategoryService.getAllCategory());
        request.setAttribute("pageName","分类页面");
        request.setAttribute("configurations",blogConfigService.getAllConfig());
        return "blog/"+theme+"/category";
    }
    @GetMapping({"/blog/{blogId}","/article/{blogId}"})
    public String detail(HttpServletRequest request, @PathVariable("blogId") Long blogId, @RequestParam(value = "commentPage",required = false,defaultValue = "1")Integer commentPage){
        BlogDetailVO blogDetailVO=blogService.getBlogDetailVO(blogId);
        if(blogDetailVO!=null){
           request.setAttribute("blogDetailVO",blogDetailVO);
           request.setAttribute("commentPageResult",blogCommentService.getCommentPageByBlogIdAndPageNum(blogId,commentPage));
        }
        request.setAttribute("pageName","详情");
        request.setAttribute("configurations",blogConfigService.getAllConfig());
        return "blog/"+theme+"/detail";
    }
    @GetMapping("/tag/{tagName}")
    public String tag(HttpServletRequest request,@PathVariable("tagName")String tagName){
        return this.tag(request,tagName,1);
    }
    @GetMapping("/tag/{tagName}/{page}")
    private String tag(HttpServletRequest request, @PathVariable("tagName") String tagName,@PathVariable("page") int page) {
        PageResult blogPageResult=blogService.getBlogsPageByTag(tagName, page);
        request.setAttribute("blogPageResult",blogPageResult);
        request.setAttribute("pageName","标签");
        request.setAttribute("pageUrl","tag");
        request.setAttribute("keyword",tagName);
        request.setAttribute("newBlogs",blogService.getBlogListForIndexPage(1));
        request.setAttribute("hotBlogs",blogService.getBlogListForIndexPage(0));
        request.setAttribute("hotTags",blogTagService.getBlogTagCountForIndex());
        request.setAttribute("configurations",blogConfigService.getAllConfig());
        return "blog/"+theme+"/list";
    }
    @GetMapping({"/category/{categoryName}"})
    public String category(HttpServletRequest request,@PathVariable("categoryName")String categoryName){
        return this.category(request,categoryName,1);
    }

    @GetMapping("/category/{categoryName}/{page}")
    public String category(HttpServletRequest request, @PathVariable("categoryName") String categoryName, @PathVariable("page") int page) {
        PageResult blogPageResult=blogService.getBlogsPageByCategory(categoryName,page);
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("pageName", "分类");
        request.setAttribute("pageUrl", "category");
        request.setAttribute("keyword", categoryName);
        request.setAttribute("newBlogs", blogService.getBlogListForIndexPage(1));
        request.setAttribute("hotBlogs", blogService.getBlogListForIndexPage(0));
        request.setAttribute("hotTags", blogTagService.getBlogTagCountForIndex());
        request.setAttribute("configurations", blogConfigService.getAllConfig());
        return "blog/" + theme + "/list";
    }
    @GetMapping({"/search/{keyword}"})
    public String search(HttpServletRequest request,@PathVariable("keyword")String keyword){
        return this.search(request,keyword,1);
    }
    @GetMapping({"/search/{keyword}/{page}"})
    public String search(HttpServletRequest request, String keyword, int page) {
        PageResult blogPageResult=blogService.getBlogsPageBySearch(keyword,page);
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("pageName", "搜索");
        request.setAttribute("pageUrl", "search");
        request.setAttribute("keyword", keyword);
        request.setAttribute("newBlogs", blogService.getBlogListForIndexPage(1));
        request.setAttribute("hotBlogs", blogService.getBlogListForIndexPage(0));
        request.setAttribute("hotTags", blogTagService.getBlogTagCountForIndex());
        request.setAttribute("configurations", blogConfigService.getAllConfig());
        return "blog/" + theme + "/list";
    }
    @GetMapping({"/link"})
    public String link(HttpServletRequest request){
        request.setAttribute("pageName","友情链接");
        Map<Byte, List<BlogLink>> linksMap=blogLinkService.getLinksForLinkPage();
        if(!CollectionUtils.isEmpty(linksMap)){
            if(linksMap.containsKey((byte)0)){
                request.setAttribute("favoriteLinks",linksMap.get((byte)0));
            }
            if(linksMap.containsKey((byte)1)){
                request.setAttribute("recommendLinks",linksMap.get((byte)1));
            }
            if(linksMap.containsKey((byte)2)){
                request.setAttribute("recommendLinks",linksMap.get((byte)2));
            }
        }
        request.setAttribute("configurations",blogConfigService.getAllConfig());
        return "blog/" + theme + "/link";
    }
    @PostMapping(value = "/blog/comment")
    @ResponseBody
    public Result comment(HttpServletRequest request, HttpSession session,
                          @RequestParam Long blogId, @RequestParam String verifyCode,
                          @RequestParam String commentator, @RequestParam String email,
                          @RequestParam String websiteUrl, @RequestParam String commentBody){
        if(StringUtils.isEmpty(verifyCode)){
            return ResultGenerator.genFailResult("验证码不能为空!");
        }
        String kaptchaCode=session.getAttribute("verifyCode")+"";
        if(StringUtils.isEmpty(kaptchaCode)){
            return ResultGenerator.genFailResult("非法请求");
        }
        if(!verifyCode.equals(kaptchaCode)){
            return ResultGenerator.genFailResult("验证码错误!");
        }
        String ref = request.getHeader("Referer");
        if (StringUtils.isEmpty(ref)) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if(null==blogId||blogId<0){
            return ResultGenerator.genFailResult("非法请求!");
        }
        if(StringUtils.isEmpty(commentator)){
            return ResultGenerator.genFailResult("请输入称呼!");
        }
        if(StringUtils.isEmpty(email)){
            return ResultGenerator.genFailResult("请输入邮箱!");
        }
        if(!PatternUtil.isEmail(email)){
            return ResultGenerator.genFailResult("请输入正确邮箱地址!");
        }
        if(StringUtils.isEmpty(commentBody)){
            return ResultGenerator.genFailResult("请输入评论!");
        }
        if(commentBody.trim().length()>200){
            return ResultGenerator.genFailResult("评论内容过长!");
        }
        BlogComment blogComment=new BlogComment();
        blogComment.setBlogId(blogId);
        blogComment.setCommentator(commentator);
        blogComment.setEmail(email);
        blogComment.setWebsiteUrl(websiteUrl);
        if(!PatternUtil.isURL(websiteUrl)){
            blogComment.setWebsiteUrl(websiteUrl);
        }
        blogComment.setCommentBody(MyBlogUtils.cleanString(commentBody));
        return ResultGenerator.genSuccessResult(blogCommentService.addComment(blogComment));
    }
    @GetMapping({"/{subUrl}"})
    public String subUrl(HttpServletRequest request,@PathVariable("subUrl")String subUrl){
        BlogDetailVO blogDetailVO=blogService.getBlogsPageBySubUrl(subUrl);
        if(blogDetailVO!=null){
            request.setAttribute("blogDetailVO",blogDetailVO);
            request.setAttribute("pageName",subUrl);
            request.setAttribute("configurations", blogConfigService.getAllConfig());
            return "blog/" + theme + "/detail";
        }
        return "error/error_400";
    }
}
