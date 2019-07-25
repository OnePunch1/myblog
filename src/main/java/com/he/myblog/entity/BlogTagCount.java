package com.he.myblog.entity;

import lombok.Data;

@Data
public class BlogTagCount {
    private Integer tagId;

    private String tagName;

    private Integer tagCount;
}
