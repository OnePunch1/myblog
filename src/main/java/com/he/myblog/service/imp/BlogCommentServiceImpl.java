package com.he.myblog.service.imp;

import com.he.myblog.dao.BlogCommentMapper;
import com.he.myblog.entity.BlogComment;
import com.he.myblog.service.BlogCommentService;
import com.he.myblog.util.PageQueryUtil;
import com.he.myblog.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("blogCommentService")
public class BlogCommentServiceImpl implements BlogCommentService {
    @Autowired
    BlogCommentMapper blogCommentMapper;
    @Override
    public int getTotalBlogComments() {
        return blogCommentMapper.getTotalBlogComments(null);
    }

    @Override
    public Boolean addComment(BlogComment blogComment) {
        return blogCommentMapper.insertSeletive(blogComment)>0;
    }

    @Override
    public PageResult listPage(PageQueryUtil pageQueryUtil) {
        List<BlogComment> list=blogCommentMapper.findBlogCommentList(pageQueryUtil);
        int total=blogCommentMapper.getTotalBlogComments(pageQueryUtil);
        PageResult pageResult=new PageResult(list,total,pageQueryUtil.getLimit(),pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public boolean checkDone(Integer[] ids) {
        if(ids.length<1){
            return false;
        }
        return blogCommentMapper.updateCheckDone(ids)>0;
    }

    @Override
    public boolean deleteBatch(Integer[] ids) {
        if(ids.length<1){
            return false;
        }
        return blogCommentMapper.deleteBatch(ids)>0;
    }

    @Override
    public boolean reply(Integer commentId, String replyBody) {
        BlogComment blogComment=blogCommentMapper.getCommentById(commentId);
        if(blogComment!=null&&blogComment.getCommentStatus().intValue()==1){
            blogComment.setReplyBody(replyBody);
            blogComment.setReplyCreateTime(new Date());
            return blogCommentMapper.updateReply(blogComment)>0;
        }
        return false;
    }

    @Override
    public PageResult getCommentPageByBlogIdAndPageNum(Long blogId, int page) {
        Map param=new HashMap();
        param.put("page",page);
        param.put("limit",8);
        param.put("blogId",blogId);
        param.put("commentStatus",1);
        PageQueryUtil pageQueryUtil=new PageQueryUtil(param);
        List<BlogComment> blogComments=blogCommentMapper.findBlogCommentList(pageQueryUtil);
        if(!CollectionUtils.isEmpty(blogComments)){
            int total=blogCommentMapper.getTotalBlogComments(param);
            PageResult pageResult=new PageResult(blogComments,total,pageQueryUtil.getLimit(),pageQueryUtil.getPage());
            return pageResult;
        }
        return null;
    }
}
