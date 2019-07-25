package com.he.myblog.controller.common;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
@RequestMapping("/common")
public class CommonController {
    @Autowired
    private DefaultKaptcha kaptchaproducer;
    @GetMapping("/kaptcha")
    public void defaultkaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        byte[] captchaoutputstream=null;
        ByteArrayOutputStream imgoutputstream=new ByteArrayOutputStream();
        try {
            String verifyCode=kaptchaproducer.createText();
            request.getSession().setAttribute("verifyCode",verifyCode);
            BufferedImage bufferedImage=kaptchaproducer.createImage(verifyCode);
            ImageIO.write(bufferedImage,"jpg",imgoutputstream);
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        captchaoutputstream = imgoutputstream.toByteArray();
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(captchaoutputstream);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
