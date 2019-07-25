package com.he.myblog.dao;

import com.he.myblog.entity.Blog;
import com.he.myblog.entity.BlogTagCount;
import com.he.myblog.util.PageQueryUtil;
import com.he.myblog.util.PageResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface BlogMapper {
    int getTotalBlogs(PageQueryUtil pageQueryUtil);
    int updateBlogCategorys(@Param("categoryId") Integer categoryId,@Param("categoryName") String categoryName,@Param("ids")Integer[] ids);
    List<Blog> findBlogsPage(PageQueryUtil pageQueryUtil);
    int insertive(Blog blog);
    Blog findByPrimaryKey(@Param("blogId")Long blogId);
    int updateByPrimaryKeySelective(Blog blog);
    int deleteBatch(@Param("ids")Long[] ids);
    List<Blog> findBlogListByType(@Param("type") int type,@Param("limit") int limit);
    Blog findBlogListBySubUrl(@Param("subUrl") String subUrl );
}
