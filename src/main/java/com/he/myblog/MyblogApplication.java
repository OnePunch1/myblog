package com.he.myblog;

import com.he.myblog.dao.BlogMapper;
import com.he.myblog.entity.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
public class MyblogApplication {
  /*  @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation("/static/upload");
        return factory.createMultipartConfig();
    }*/
    public static void main(String[] args) {
        SpringApplication.run(MyblogApplication.class, args);
    }

}
