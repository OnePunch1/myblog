package com.he.myblog.service.imp;

import com.he.myblog.dao.BlogCategoryMapper;
import com.he.myblog.dao.BlogMapper;
import com.he.myblog.entity.BlogCategory;
import com.he.myblog.service.BlogCategoryService;
import com.he.myblog.util.PageQueryUtil;
import com.he.myblog.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;

@Service("blogCategoryService")
public class BlogCategoryServiceImpl implements BlogCategoryService {

    @Autowired
    BlogCategoryMapper blogCategoryMapper;
    @Autowired
    BlogMapper blogMapper;

    @Override
    public int getTotalCategories() {
        return blogCategoryMapper.getTotalCategories(null);
    }

    @Override
    public List<BlogCategory> findCategoryList() {
        return blogCategoryMapper.findCategoryList(null);
    }

    @Override
    public PageResult findCategoryList(PageQueryUtil pageQueryUtil) {
        List<BlogCategory> list=blogCategoryMapper.findCategoryList(pageQueryUtil);
        int total=blogCategoryMapper.getTotalCategories(pageQueryUtil);
        PageResult pageResult=new PageResult(list,total,pageQueryUtil.getLimit(),pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public boolean addCategory(String categoryName, String categoryIcon) {
        BlogCategory blogCategory=blogCategoryMapper.getCategoryByName(categoryName);
        if(blogCategory==null){
            blogCategory=new BlogCategory();
            blogCategory.setCategoryName(categoryName);
            blogCategory.setCategoryIcon(categoryIcon);
            return blogCategoryMapper.insertSelective(blogCategory)>0;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean updateCategory(Integer categoryId, String categoryName, String categoryIcon) {
        BlogCategory blogCategory=blogCategoryMapper.getCategoryById(categoryId);
        if(blogCategory!=null){
            blogCategory.setCategoryName(categoryName);
            blogCategory.setCategoryIcon(categoryIcon);
            blogMapper.updateBlogCategorys(categoryId,blogCategory.getCategoryName(),new Integer[]{categoryId});
            return blogCategoryMapper.updateCategorySeletive(blogCategory)>0;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean deleteBatch(Integer[] ids) {
        if(ids.length<1){
            return false;
        }
        blogMapper.updateBlogCategorys(0,"默认分类",ids);
        return blogCategoryMapper.deleteBatch(ids)>0;
    }

    @Override
    public List<BlogCategory> getAllCategory() {
        return blogCategoryMapper.findCategoryList(null);
    }


}
