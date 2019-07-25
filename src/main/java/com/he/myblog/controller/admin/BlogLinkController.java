package com.he.myblog.controller.admin;

import com.he.myblog.entity.BlogLink;
import com.he.myblog.service.BlogLinkService;
import com.he.myblog.util.PageQueryUtil;
import com.he.myblog.util.PageResult;
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
public class BlogLinkController {
    @Autowired
    BlogLinkService blogLinkService;

    @GetMapping("/links")
    public String linkPage(HttpServletRequest request){
        request.setAttribute("path","links");
        return "admin/link";
    }
    @GetMapping("/links/list")
    @ResponseBody
    public Result list(@RequestParam Map<String,Object> param){
        if(StringUtils.isEmpty(param.get("page"))||StringUtils.isEmpty("limit")){
            return ResultGenerator.genFailResult("参数错误!");
        }
        PageResult pageResult=blogLinkService.findLinksPage(new PageQueryUtil(param));
        return ResultGenerator.genSuccessResult(pageResult);
    }
    @PostMapping("/links/save")
    @ResponseBody
    public Result save(@RequestParam("linkType")Integer linkType,
                       @RequestParam("linkName")String linkName,
                       @RequestParam("linkUrl")String linkUrl,
                       @RequestParam("linkDescription")String linkDescription,
                       @RequestParam("linkRank")Integer linkRank){
        if(linkType==null||linkType<0||linkRank==null||linkRank<0||StringUtils.isEmpty(linkName)||StringUtils.isEmpty(linkUrl)||StringUtils.isEmpty(linkDescription)){
            return ResultGenerator.genFailResult("参数错误!");
        }
        BlogLink blogLink=new BlogLink();
        blogLink.setLinkType(linkType.byteValue());
        blogLink.setLinkName(linkName);
        blogLink.setLinkUrl(linkUrl);
        blogLink.setLinkDescription(linkDescription);
        blogLink.setLinkRank(linkRank);
        return ResultGenerator.genSuccessResult(blogLinkService.saveLink(blogLink));
    }
    @GetMapping("/links/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id")Integer id){
        BlogLink blogLink=blogLinkService.infoLink(id);
        if(blogLink!=null){
            return ResultGenerator.genSuccessResult(blogLink);
        }else{
            return ResultGenerator.genFailResult("查找失败!");
        }
    }
    @PostMapping("/links/update")
    @ResponseBody
    public Result update(
                         @RequestParam("linkId") Integer linkId,
                         @RequestParam("linkType")Integer linkType,
                         @RequestParam("linkName")String linkName,
                         @RequestParam("linkUrl")String linkUrl,
                         @RequestParam("linkDescription")String linkDescription,
                         @RequestParam("linkRank")Integer linkRank){
        BlogLink blogLink=blogLinkService.infoLink(linkId);
        if(blogLink==null){
            return ResultGenerator.genFailResult("无数据!");

        }
        if(linkType==null||linkType<0||linkRank==null||linkRank<0||StringUtils.isEmpty(linkName)||StringUtils.isEmpty(linkUrl)||StringUtils.isEmpty(linkDescription)){
            return ResultGenerator.genFailResult("参数错误!");
        }
        blogLink.setLinkType(linkType.byteValue());
        blogLink.setLinkName(linkName);
        blogLink.setLinkUrl(linkUrl);
        blogLink.setLinkDescription(linkDescription);
        blogLink.setLinkRank(linkRank);
        boolean flag=blogLinkService.updateLink(blogLink);
        return ResultGenerator.genSuccessResult(flag);
    }
    @PostMapping("/links/delete")
    @ResponseBody
    public Result deleteBatch(@RequestBody Integer[] ids){
        if(ids.length<1){
            return ResultGenerator.genFailResult("参数错误!");
        }
        if(blogLinkService.deleteBatch(ids)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("删除失败!");
        }
    }
}
