package com.he.myblog.dao;

import com.he.myblog.entity.BlogLink;
import com.he.myblog.util.PageQueryUtil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface BlogLinkMapper {
    int getTotalLinks(PageQueryUtil pageQueryUtil);
    List<BlogLink> findLinksPage(PageQueryUtil pageQueryUtil);
    int insertLink(BlogLink blogLink);
    BlogLink findLinksById(@Param("linkId") Integer linkId);
    int updateLinkById(BlogLink blogLink);
    int deleteBatchLinks(@Param("ids") Integer[] ids);
}
