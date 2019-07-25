package com.he.myblog.service.imp;

import com.he.myblog.dao.BlogTagMapper;
import com.he.myblog.dao.BlogTagRelationMapper;
import com.he.myblog.entity.BlogTag;
import com.he.myblog.entity.BlogTagCount;
import com.he.myblog.service.BlogTagService;
import com.he.myblog.util.PageQueryUtil;
import com.he.myblog.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service("blogTagService")
public class BlogTagServiceImpl implements BlogTagService {
    @Autowired
    BlogTagMapper blogTagMapper;
    @Autowired
    BlogTagRelationMapper blogTagRelationMapper;

    @Override
    public int getTotalTags() {
        return blogTagMapper.getTotalTags(null);
    }

    @Override
    public boolean saveTag(String tagName) {
        BlogTag blogTag=blogTagMapper.selectTagByName(tagName);
        if(blogTag==null){
            blogTag=new BlogTag();
            blogTag.setTagName(tagName);
            return blogTagMapper.insertTagSelective(blogTag)>0;
        }
        return false;
    }

    @Override
    public PageResult list(PageQueryUtil map) {
        List<BlogTag> list=blogTagMapper.findTagList(map);
        int total=blogTagMapper.getTotalTags(map);
        PageResult pageResult=new PageResult(list,total,map.getLimit(),map.getPage());
        return pageResult;
    }

    @Override
    public boolean deleteBatch(Integer[] ids) {
        List<Long> tagIds= blogTagRelationMapper.selectDistinctTagId(ids);
        if(!CollectionUtils.isEmpty(tagIds)){
            return false;
        }
        return blogTagMapper.deleteBatch(ids)>0;
    }

    @Override
    public List<BlogTagCount> getBlogTagCountForIndex() {
        return blogTagMapper.getTagCount();
    }
}
