package com.he.myblog.service;

import com.he.myblog.entity.BlogTagCount;
import com.he.myblog.util.PageQueryUtil;
import com.he.myblog.util.PageResult;

import java.util.List;

public interface BlogTagService {
    int getTotalTags();
    boolean saveTag(String tagName);
    PageResult list(PageQueryUtil pageQueryUtil);
    boolean deleteBatch(Integer[] ids);
    List<BlogTagCount> getBlogTagCountForIndex();
}
