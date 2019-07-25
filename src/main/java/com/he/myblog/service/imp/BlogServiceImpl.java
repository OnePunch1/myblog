package com.he.myblog.service.imp;

import com.he.myblog.controller.vo.BlogDetailVO;
import com.he.myblog.controller.vo.BlogListVO;
import com.he.myblog.dao.*;
import com.he.myblog.entity.Blog;
import com.he.myblog.entity.BlogCategory;
import com.he.myblog.entity.BlogTag;
import com.he.myblog.entity.BlogTagRelation;
import com.he.myblog.service.BlogService;
import com.he.myblog.util.MarkDownUtil;
import com.he.myblog.util.PageQueryUtil;
import com.he.myblog.util.PageResult;
import com.he.myblog.util.PatternUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service("blogService")
public class BlogServiceImpl implements BlogService {
    @Autowired
    BlogMapper blogMapper;
    @Autowired
    BlogCategoryMapper blogCategoryMapper;
    @Autowired
    BlogTagMapper blogTagMapper;
    @Autowired
    BlogTagRelationMapper blogTagRelationMapper;
    @Autowired
    BlogCommentMapper blogCommentMapper;
    @Override
    public int getTotalBlogs() {
        return blogMapper.getTotalBlogs(null);
    }

    @Override
    public PageResult list(PageQueryUtil pageQueryUtil) {
        List<Blog> list= blogMapper.findBlogsPage(pageQueryUtil);
        int total=blogMapper.getTotalBlogs(pageQueryUtil);
        PageResult pageResult=new PageResult(list,total,pageQueryUtil.getLimit(),pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    @Transactional
    public String saveBlog(Blog blog) {
        BlogCategory blogCategory=blogCategoryMapper.getCategoryById(blog.getBlogCategoryId());
        if(blogCategory==null){
            blog.setBlogCategoryId(0);
            blog.setBlogCategoryName("默认分类");
        }else{
            blog.setBlogCategoryName(blogCategory.getCategoryName());
            blogCategory.setCategoryRank(blogCategory.getCategoryRank()+1);
        }
        String[] tags=blog.getBlogTags().split(",");
        if(tags.length>6){
            return "标签数量不能超过6个!";
        }
        if(blogMapper.insertive(blog)>0){
            List<BlogTag> tagListForInsert=new ArrayList<>();
            List<BlogTag> allTagsList=new ArrayList<>();
            for(String tag:tags){
                BlogTag blogTag=blogTagMapper.selectTagByName(tag);
                if(blogTag==null){
                    BlogTag tmpTag=new BlogTag();
                    tmpTag.setTagName(tag);
                    tagListForInsert.add(tmpTag);
                }else{
                    allTagsList.add(blogTag);
                }
            }
            if(!CollectionUtils.isEmpty(tagListForInsert)){
                blogTagMapper.batchInsertBlogTag(tagListForInsert);
            }
            blogCategoryMapper.updateCategorySeletive(blogCategory);
            List<BlogTagRelation> tagRelationList=new ArrayList<>();
            allTagsList.addAll(tagListForInsert);
            for (BlogTag blogTag:allTagsList){
                BlogTagRelation blogTagRelation=new BlogTagRelation();
                blogTagRelation.setBlogId(blog.getBlogId());
                blogTagRelation.setTagId(blogTag.getTagId());
                tagRelationList.add(blogTagRelation);
            }
            if(blogTagRelationMapper.batchInsert(tagRelationList)>0){
                return "success";
            }
        }
        return "保存失败";
    }

    @Override
    public Blog info(Long id) {
        Blog blog=blogMapper.findByPrimaryKey(id);
        return blog;
    }

    @Override
    @Transactional
    public String update(Blog blog) {
        Blog blogForUpdate=blogMapper.findByPrimaryKey(blog.getBlogId());
        if(blogForUpdate==null){
            return "数据不存在!";
        }
        blogForUpdate.setBlogTitle(blog.getBlogTitle());
        blogForUpdate.setBlogSubUrl(blog.getBlogSubUrl());
        blogForUpdate.setBlogContent(blog.getBlogContent());
        blogForUpdate.setBlogCoverImage(blog.getBlogCoverImage());
        blogForUpdate.setBlogStatus(blog.getBlogStatus());
        blogForUpdate.setEnableComment(blog.getEnableComment());
        BlogCategory blogCategory=blogCategoryMapper.getCategoryById(blog.getBlogCategoryId());
        if(blogCategory==null){
            blogForUpdate.setBlogCategoryId(0);
            blogForUpdate.setBlogCategoryName("默认分类");
        }else{
            blogForUpdate.setBlogCategoryName(blogCategory.getCategoryName());
            blogForUpdate.setBlogCategoryId(blogCategory.getCategoryId());

            blogCategory.setCategoryRank(blogCategory.getCategoryRank()+1);
        }
        String[] tags=blog.getBlogTags().split(",");
        if(tags.length>6){
            return "标签数量不能超过6个!";
        }
        blogForUpdate.setBlogTags(blog.getBlogTags());
        List<BlogTag> tagListForInsert=new ArrayList<>();
        List<BlogTag> allTagsList=new ArrayList<>();
        for(String tag:tags){
            BlogTag blogTag=blogTagMapper.selectTagByName(tag);
            if(blogTag==null){
                BlogTag tmpTag=new BlogTag();
                tmpTag.setTagName(tag);
                tagListForInsert.add(tmpTag);
            }else{
                allTagsList.add(blogTag);
            }
        }
        if(!CollectionUtils.isEmpty(tagListForInsert)){
            blogTagMapper.batchInsertBlogTag(tagListForInsert);
        }
      /*  blogCategoryMapper.updateCategorySeletive(blogCategory);*/
        List<BlogTagRelation> tagRelationList=new ArrayList<>();
        allTagsList.addAll(tagListForInsert);
        for (BlogTag blogTag:allTagsList){
            BlogTagRelation blogTagRelation=new BlogTagRelation();
            blogTagRelation.setBlogId(blog.getBlogId());
            blogTagRelation.setTagId(blogTag.getTagId());
            tagRelationList.add(blogTagRelation);
        }
        blogCategoryMapper.updateCategorySeletive(blogCategory);
        blogTagRelationMapper.deleteByBlogId(blog.getBlogId());
        blogTagRelationMapper.batchInsert(tagRelationList);
        if(blogMapper.updateByPrimaryKeySelective(blogForUpdate)>0){
            return "success";
        }
        return "保存失败";
    }

    @Override
    public boolean deleteBatch(Long[] ids) {
        return blogMapper.deleteBatch(ids)>0;
    }

    @Override
    public PageResult getBlogsForIndexPage(int pageNum) {
        Map<String,Object> param=new HashMap<>();
        param.put("page",pageNum);
        param.put("limit",8);
        param.put("blogStatus",1);
        PageQueryUtil pageQueryUtil=new PageQueryUtil(param);
        List<Blog> blogs=blogMapper.findBlogsPage(pageQueryUtil);
        List<BlogListVO> blogListVOS=getBlogListVOsByBlog(blogs);
        int total=blogMapper.getTotalBlogs(pageQueryUtil);
        return new PageResult(blogListVOS,total,pageQueryUtil.getLimit(),pageQueryUtil.getPage());
    }

    @Override
    public List<BlogListVO> getBlogListForIndexPage(int type) {
        List<BlogListVO> blogListVOS=new ArrayList<>();
        List<Blog> blogs=blogMapper.findBlogListByType(type,9);
        if(!CollectionUtils.isEmpty(blogs)){
            for (Blog blog:
                 blogs) {
                BlogListVO blogListVO=new BlogListVO();
                BeanUtils.copyProperties(blog,blogListVO);
                blogListVOS.add(blogListVO);
            }
        }
        return blogListVOS;
    }

    @Override
    public BlogDetailVO getBlogDetailVO(Long id) {
        Blog blog=blogMapper.findByPrimaryKey(id);
        BlogDetailVO blogDetailVO=getBlogDetail(blog);
        if(blogDetailVO!=null){
            return blogDetailVO;
        }
        return null;
    }

    @Override
    public BlogDetailVO getBlogsPageBySubUrl(String subUrl) {
        Blog blog=blogMapper.findBlogListBySubUrl(subUrl);
        BlogDetailVO blogDetailVO=getBlogDetail(blog);
        if(blogDetailVO!=null){
            return blogDetailVO;
        }
        return null;
    }

    @Override
    public PageResult getBlogsPageByTag(String tagName, int page) {
        if(PatternUtil.validKeyword(tagName)){
            BlogTag tag=blogTagMapper.selectTagByName(tagName);
            if(tag!=null&&page>0){
                Map param=new HashMap();
                param.put("page",page);
                param.put("limit",9);
                param.put("tagId",tag.getTagId());
                PageQueryUtil pageQueryUtil=new PageQueryUtil(param);
                List<Blog> blogs=blogMapper.findBlogsPage(pageQueryUtil);
                List<BlogListVO> blogListVOS=getBlogListVOsByBlog(blogs);
                int total=blogMapper.getTotalBlogs(pageQueryUtil);
                PageResult pageResult=new PageResult(blogListVOS,total,pageQueryUtil.getLimit(),pageQueryUtil.getPage());
                return pageResult;
            }
        }
        return null;
    }

    @Override
    public PageResult getBlogsPageByCategory(String categoryName, int page) {
        if(PatternUtil.validKeyword(categoryName)){
            BlogCategory blogCategory=blogCategoryMapper.getCategoryByName(categoryName);
            if("默认分类".equals(categoryName)&&blogCategory==null){
                blogCategory=new BlogCategory();
                blogCategory.setCategoryId(0);
            }
            if(blogCategory!=null&&page>0){
                Map param=new HashMap();
                param.put("page",page);
                param.put("limit",9);
                param.put("blogCategoryId",blogCategory.getCategoryId());
                param.put("blogStatus",1);
                PageQueryUtil pageQueryUtil=new PageQueryUtil(param);
                List<Blog> blogs=blogMapper.findBlogsPage(pageQueryUtil);
                List<BlogListVO> blogListVOS=getBlogListVOsByBlog(blogs);
                int total=blogMapper.getTotalBlogs(pageQueryUtil);
                return new PageResult(blogListVOS,total,pageQueryUtil.getLimit(),pageQueryUtil.getPage());
            }
        }
        return null;
    }

    @Override
    public PageResult getBlogsPageBySearch(String keyword, int page) {
        if(page>0&&PatternUtil.validKeyword(keyword)){
            Map param=new HashMap();
            param.put("page",page);
            param.put("limit",9);
            param.put("keyword",keyword);
            param.put("blogStatus",1);
            PageQueryUtil pageQueryUtil=new PageQueryUtil(param);
            List<Blog> blogs=blogMapper.findBlogsPage(pageQueryUtil);
            int total=blogMapper.getTotalBlogs(pageQueryUtil);
            List<BlogListVO> blogListVOS=getBlogListVOsByBlog(blogs);
            return new PageResult(blogListVOS,total,pageQueryUtil.getLimit(),pageQueryUtil.getPage());
        }
        return null;
    }

    private BlogDetailVO getBlogDetail(Blog blog) {
        if(blog!=null&&blog.getBlogStatus()==1){
            blog.setBlogViews(blog.getBlogViews()+1);
            blogMapper.updateByPrimaryKeySelective(blog);
            BlogDetailVO blogDetailVO=new BlogDetailVO();
            BeanUtils.copyProperties(blog,blogDetailVO);
            blogDetailVO.setBlogContent(MarkDownUtil.mdToHtml(blogDetailVO.getBlogContent()));
            BlogCategory blogCategory=blogCategoryMapper.getCategoryById(blog.getBlogCategoryId());
            if(blogCategory==null){
                blogCategory=new BlogCategory();
                blogCategory.setCategoryId(0);
                blogCategory.setCategoryName("默认分类");
                blogCategory.setCategoryIcon("/admin/dist/img/category/00.png");
            }
            blogDetailVO.setBlogCategoryIcon(blogCategory.getCategoryIcon());
            if(!StringUtils.isEmpty(blog.getBlogTags())){
               List<String> tags= Arrays.asList(blog.getBlogTags().split(","));
               blogDetailVO.setBlogTags(tags);
            }
            Map param=new HashMap();
            param.put("blogId",blog.getBlogId());
            param.put("commentStatus",1);
            blogDetailVO.setCommentCount(blogCommentMapper.getTotalBlogComments(param));
            return blogDetailVO;
        }
        return null;
    }

    private List<BlogListVO> getBlogListVOsByBlog(List<Blog> blogs) {
        List<BlogListVO> listVOS=new ArrayList<>();
        if(!CollectionUtils.isEmpty(blogs)){
            List<Integer> categoryIds=blogs.stream().map(Blog::getBlogCategoryId).collect(Collectors.toList());
            Map<Integer,String> blogCategoryMap=new HashMap<>();
            if(!CollectionUtils.isEmpty(categoryIds)){
                List<BlogCategory> blogCategories=blogCategoryMapper.getCategoryByIds(categoryIds);
                if(!CollectionUtils.isEmpty(blogCategories)){
                    blogCategoryMap=blogCategories.stream().collect(Collectors.toMap(BlogCategory::getCategoryId,BlogCategory::getCategoryIcon,(key1,key2)->key2));
                }
            }
            for (Blog blog:
                 blogs) {
                BlogListVO blogListVO=new BlogListVO();
                BeanUtils.copyProperties(blog,blogListVO);
                if(blogCategoryMap.containsKey(blog.getBlogCategoryId())){
                    blogListVO.setBlogCategoryIcon(blogCategoryMap.get(blog.getBlogCategoryId()));
                }else{
                    blogListVO.setBlogCategoryId(0);
                    blogListVO.setBlogCategoryName("默认分类");
                    blogListVO.setBlogCategoryIcon("/admin/dist/img/category/00.png");
                }
                listVOS.add(blogListVO);
            }
        }
        return listVOS;
    }
}
