package com.he.myblog.service;

import com.he.myblog.entity.BlogCategory;
import com.he.myblog.util.PageQueryUtil;
import com.he.myblog.util.PageResult;
import com.he.myblog.util.Result;

import java.util.List;

public interface BlogCategoryService {
    int getTotalCategories();
    List<BlogCategory> findCategoryList();
    PageResult findCategoryList(PageQueryUtil pageQueryUtil);
    boolean addCategory(String categoryName,String categoryIcon);
    boolean updateCategory(Integer categoryId,String categoryName,String categoryIcon);
    boolean deleteBatch(Integer[] ids);
    List<BlogCategory> getAllCategory();
}
