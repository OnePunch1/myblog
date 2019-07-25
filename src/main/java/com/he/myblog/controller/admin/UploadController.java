package com.he.myblog.controller.admin;

import com.he.myblog.config.Constants;
import com.he.myblog.util.MyBlogUtils;
import com.he.myblog.util.Result;
import com.he.myblog.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@RequestMapping("/admin")
@Controller
public class UploadController {
    @PostMapping("/upload/file")
    @ResponseBody
    public Result upload(@RequestParam("file")MultipartFile multipartFile, HttpServletRequest request) throws  URISyntaxException {
        String fileName=multipartFile.getOriginalFilename();
        String suffixName=fileName.substring(fileName.lastIndexOf("."));
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r=new Random();
        StringBuilder tempName=new StringBuilder();
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
        String newFile=tempName.toString();
        File fileDirectory=new File(Constants.FILE_UPLOAD_DIC);

        File destFile=new File(Constants.FILE_UPLOAD_DIC+newFile);
        try {
            if(!fileDirectory.exists()){
                if(!fileDirectory.mkdir()){
                    throw  new IOException("文件夹创建失败:"+fileDirectory);
                }
            }
           multipartFile.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Result result= ResultGenerator.genSuccessResult();
        result.setData(MyBlogUtils.getHost(new URI(request.getRequestURL() + "")) + "/upload/" + newFile);
        return result;
    }
}
