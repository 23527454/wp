package com.thinkgem.jeesite.modules.guard.web;

import com.alibaba.fastjson.JSON;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.boot.ApplicationHome;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 摄像头牌照
 */
@WebServlet(name = "webCamServlet", urlPatterns = "/webCamServlet")
public class WebCamController  extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String basePath = "fiacsServer/upload/";
        String filePath = getJarPath()+"/" + basePath;
        String fileName = (new Date()).getTime()+".jpg";
        String imgStr = request.getParameter("image");
        if (null != imgStr) {
            imgStr = imgStr.substring(imgStr.indexOf(",") + 1);
        }
        Boolean flag = GenerateImage(imgStr, filePath, fileName);
        String result = "";
        if (flag) {
            result = basePath + fileName;
        }
        System.out.println(filePath);
//		    this.writeJson(result, resp);
        response.getWriter().print(JSON.toJSON(result));
    }

    public boolean GenerateImage(String imgStr, String filePath, String fileName) {
        try {
            if (imgStr == null) {
                return false;
            }
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(imgStr);
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(b));
            BufferedImage newBufferedImage = new BufferedImage(
                    bufferedImage.getWidth(), bufferedImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            // TYPE_INT_RGB:创建一个RBG图像，24位深度，成功将32位图转化成24位
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
                    Color.WHITE, null);
            // write to jpeg file
            ImageIO.write(newBufferedImage, "jpg", new File(filePath+fileName));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String getJarPath() {
        ApplicationHome h = new ApplicationHome(getClass());
        File jarF = h.getSource();
        return jarF.getParentFile().toString();
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(request);
            for (FileItem item : items) {
                if (item.isFormField()) {
                    // item.getFieldName();// 参数名
                    // item.getString();// 参数值
                    params.put(item.getFieldName(), item.getString());
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        return params;
    }

    /**
     * 检查参数是否完整
     *
     * @param params
     * @return
     */
    @SuppressWarnings("rawtypes")
    private Boolean checkParam(Map params) {
        String[] paramNames = {"d", "u", "c", "h", "e", "t", "f"};
        for (String paramName : paramNames) {
            if (params.containsKey(paramName)) {
                if (params.get(paramName) == null)
                    return false;
            } else {
                return false;
            }
        }
        return true;
    }
}
