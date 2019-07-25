package com.he.myblog.dao;

import com.he.myblog.entity.Blog;
import com.he.myblog.entity.BlogComment;
import com.he.myblog.util.PageQueryUtil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface BlogCommentMapper {
    int getTotalBlogComments(Map map);
    List<BlogComment> findBlogCommentList(PageQueryUtil pageQueryUtil);
    int updateCheckDone(@Param("ids")Integer[] ids);
    int deleteBatch(@Param("ids") Integer[] ids);
    BlogComment getCommentById(@Param("commentId") Integer commentId);
    int updateReply(BlogComment blogComment);
    int insertSeletive(BlogComment blogComment);
}
