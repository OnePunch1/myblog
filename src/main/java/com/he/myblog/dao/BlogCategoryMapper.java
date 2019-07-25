package com.he.myblog.dao;

import com.he.myblog.entity.BlogCategory;
import com.he.myblog.util.PageQueryUtil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface BlogCategoryMapper {
    int getTotalCategories(PageQueryUtil pageQueryUtil);
    List<BlogCategory> findCategoryList(PageQueryUtil pageQueryUtil);
    BlogCategory getCategoryByName(String categoryName);
    BlogCategory getCategoryById(Integer categoryId);
    List<BlogCategory> getCategoryByIds(@Param("ids")List<Integer> ids);
    int insertSelective(BlogCategory blogCategory);
    int updateCategorySeletive(BlogCategory blogCategory);
    int deleteBatch(@Param("ids") Integer[] ids);
}