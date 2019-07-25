package com.he.myblog.dao;

import com.he.myblog.entity.BlogTag;
import com.he.myblog.entity.BlogTagCount;
import com.he.myblog.util.PageQueryUtil;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface BlogTagMapper {
    int getTotalTags(PageQueryUtil pageQueryUtil);
    int insertTagSelective(BlogTag blogTag);
    int deleteBatch(Integer[] ids);
    BlogTag selectTagByName(String tagName);
    List<BlogTag> findTagList(PageQueryUtil pageQueryUtil);
    int batchInsertBlogTag(List<BlogTag> blogTags);
    List<BlogTagCount> getTagCount();
}
