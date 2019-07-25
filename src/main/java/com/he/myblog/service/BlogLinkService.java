package com.he.myblog.service;

import com.he.myblog.entity.Blog;
import com.he.myblog.entity.BlogLink;
import com.he.myblog.util.PageQueryUtil;
import com.he.myblog.util.PageResult;

import java.util.List;
import java.util.Map;

public interface BlogLinkService {
    int getTotalLinks();
    PageResult findLinksPage(PageQueryUtil pageQueryUtil);
    boolean saveLink(BlogLink blogLink);
    BlogLink infoLink(Integer linkId);
    boolean updateLink(BlogLink blogLink);
    boolean deleteBatch(Integer[] ids);
    Map<Byte, List<BlogLink>> getLinksForLinkPage();

}
