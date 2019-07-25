package com.he.myblog.service;

import com.he.myblog.controller.vo.BlogDetailVO;
import com.he.myblog.controller.vo.BlogListVO;
import com.he.myblog.entity.Blog;
import com.he.myblog.entity.BlogCategory;
import com.he.myblog.util.PageQueryUtil;
import com.he.myblog.util.PageResult;

import java.util.List;
import java.util.Map;

public interface BlogService {
    int getTotalBlogs();
    PageResult list(PageQueryUtil pageQueryUtil);
    String saveBlog(Blog blog);
    Blog info(Long id);
    String update(Blog blog);
    boolean deleteBatch(Long[] ids);
    PageResult getBlogsForIndexPage(int pageNum);
    List<BlogListVO> getBlogListForIndexPage(int type);
    BlogDetailVO getBlogDetailVO(Long id);
    BlogDetailVO getBlogsPageBySubUrl(String subUrl);
    PageResult getBlogsPageByTag(String tagName,int page);
    PageResult getBlogsPageByCategory(String categoryName,int page);
    PageResult getBlogsPageBySearch(String keyword,int page);

}
