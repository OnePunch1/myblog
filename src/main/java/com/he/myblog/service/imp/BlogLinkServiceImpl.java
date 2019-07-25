package com.he.myblog.service.imp;

import com.he.myblog.dao.BlogLinkMapper;
import com.he.myblog.entity.BlogLink;
import com.he.myblog.service.BlogLinkService;
import com.he.myblog.util.PageQueryUtil;
import com.he.myblog.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("blogLinkService")
public class BlogLinkServiceImpl implements BlogLinkService {
    @Autowired
    BlogLinkMapper blogLinkMapper;
    @Override
    public int getTotalLinks() {
        return blogLinkMapper.getTotalLinks(null);
    }

    @Override
    public PageResult findLinksPage(PageQueryUtil pageQueryUtil) {
        List<BlogLink> list=blogLinkMapper.findLinksPage(pageQueryUtil);
        int total=blogLinkMapper.getTotalLinks(pageQueryUtil);
        PageResult pageResult=new PageResult(list,total,pageQueryUtil.getLimit(),pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public boolean saveLink(BlogLink blogLink) {
        return blogLinkMapper.insertLink(blogLink)>0;
    }

    @Override
    public BlogLink infoLink(Integer linkId) {
        return blogLinkMapper.findLinksById(linkId);
    }

    @Override
    public boolean updateLink(BlogLink blogLink) {
        return blogLinkMapper.updateLinkById(blogLink) > 0;
    }

    @Override
    public boolean deleteBatch(Integer[] ids) {
        if(ids.length<1){
            return false;
        }
        return blogLinkMapper.deleteBatchLinks(ids)>0;
    }

    @Override
    public Map<Byte, List<BlogLink>> getLinksForLinkPage() {
        List<BlogLink> blogLinks=blogLinkMapper.findLinksPage(null);
        if(!CollectionUtils.isEmpty(blogLinks)){
            Map<Byte, List<BlogLink>> linksMap=blogLinks.stream().collect(Collectors.groupingBy(BlogLink::getLinkType));
            return linksMap;
        }
        return null;
    }
}
