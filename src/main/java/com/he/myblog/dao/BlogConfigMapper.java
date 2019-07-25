package com.he.myblog.dao;

import com.he.myblog.entity.BlogConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface BlogConfigMapper {
    List<BlogConfig> getAllConfig();
}
