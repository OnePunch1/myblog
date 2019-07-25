package com.he.myblog.dao;

import com.he.myblog.entity.BlogTagRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface BlogTagRelationMapper {
    List<Long> selectDistinctTagId(Integer[] ids);
    int batchInsert(@Param("relationList") List<BlogTagRelation> blogTagRelations);
    int deleteByBlogId(@Param("blogId") Long blogId);
}
