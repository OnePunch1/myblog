package com.he.myblog.service;

import com.he.myblog.entity.BlogComment;
import com.he.myblog.util.PageQueryUtil;
import com.he.myblog.util.PageResult;

public interface BlogCommentService {
    int getTotalBlogComments();
    Boolean addComment(BlogComment blogComment);
    PageResult listPage(PageQueryUtil pageQueryUtil);
    boolean checkDone(Integer[] ids);
    boolean deleteBatch(Integer[] ids);
    boolean reply(Integer commentId,String replyBody);
    PageResult getCommentPageByBlogIdAndPageNum(Long blogId,int page);
}
