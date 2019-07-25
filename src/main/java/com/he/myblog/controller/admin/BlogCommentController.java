package com.he.myblog.controller.admin;

import com.he.myblog.service.BlogCommentService;
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
public class BlogCommentController {
    @Autowired
    BlogCommentService blogCommentService;
    @GetMapping("/comments")
    public String commentPage(HttpServletRequest request){
        request.setAttribute("path","comments");
        return "admin/comment";
    }

    @GetMapping("/comments/list")
    @ResponseBody
    public Result list(@RequestParam Map<String,Object> param){
        if(StringUtils.isEmpty(param.get("limit"))||StringUtils.isEmpty(param.get("page"))){
            return ResultGenerator.genFailResult("参数错误!");
        }
        return ResultGenerator.genSuccessResult(blogCommentService.listPage(new PageQueryUtil(param)));
    }
    @PostMapping("/comments/checkDone")
    @ResponseBody
    public Result checkDone(@RequestBody Integer[] ids){
        if(ids.length<1){
            return ResultGenerator.genFailResult("参数错误!");
        }
        if(blogCommentService.checkDone(ids)){
            return ResultGenerator.genSuccessResult("审核成功!");
        }else{
            return ResultGenerator.genFailResult("审核失败!");
        }
    }
    @PostMapping("/comments/delete")
    @ResponseBody
    public Result deleteBatch(@RequestBody Integer[] ids){
        if(ids.length<1){
            return ResultGenerator.genFailResult("参数错误!");
        }
        if(blogCommentService.deleteBatch(ids)){
            return ResultGenerator.genSuccessResult("删除成功!");
        }else{
            return ResultGenerator.genFailResult("删除失败!");
        }
    }

    @PostMapping("/comments/reply")
    @ResponseBody
    public Result reply(@RequestParam("commentId")Integer commentId,@RequestParam("replyBody")String replyBody){
        if(commentId==null||commentId<1||StringUtils.isEmpty(replyBody)){
            return ResultGenerator.genFailResult("参数错误!");
        }
        if(blogCommentService.reply(commentId,replyBody)){
            return ResultGenerator.genSuccessResult("回复成功!");
        }else{
            return ResultGenerator.genFailResult("回复失败!");
        }
    }
}
