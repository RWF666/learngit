package com.weige.admincontroller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;








import com.fasterxml.jackson.databind.ObjectMapper;
import com.weige.pojo.PicUploadResult;
import com.weige.properties.MyProperties;

/**
 * 图片上传
 * @author RWF
 *
 */
@Controller
@RequestMapping("pic")
public class PicUploadController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PicUploadController.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    // 允许上传的格�?
    private static final String[] IMAGE_TYPE = new String[] { ".bmp", ".jpg", ".jpeg", ".gif", ".png" };
    
    /*@Autowired
    private PropertieService propertieService;
*/
    @Autowired
	private MyProperties myProperties;
    /**
     * 
     * produces: 指定响应类型
     * 
     * @param uploadFile
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String upload(@RequestParam("uploadFile") MultipartFile uploadFile, HttpServletResponse response)
            throws Exception {
        // 校验图片格式
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(), type)) {
                isLegal = true;
                break;
            }
        }

        // 封装Result对象
        PicUploadResult fileUploadResult = new PicUploadResult();

        // 状�?,0-上传成功�?-上传失败
        fileUploadResult.setError(isLegal ? 0 : 1);

        // 文件新路�?
        String filePath = getFilePath(uploadFile.getOriginalFilename());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Pic file upload .[{}] to [{}] .", uploadFile.getOriginalFilename(), filePath);
        }

		//http://image.taotao.com/images/2015/09/17/1112321321321.jpg
        // 生成图片的绝对引用地�?
        String picUrl = StringUtils.replace(filePath,
                "\\", "/");
        fileUploadResult.setUrl(myProperties.getImageBseUrl() + picUrl);
        String repositoryPath =myProperties.getRepositoryPath()+picUrl;
        String repositoryDir = StringUtils.substring(repositoryPath, 0, repositoryPath.lastIndexOf("/"));
        File dir = new File(repositoryDir);
        if(!dir.exists()){
        	dir.mkdir();
        }
        File newFile = new File(repositoryPath);
        
        // 写文件到磁盘
        uploadFile.transferTo(newFile);
        // 校验图片是否合法
        isLegal = false;
        try {
            BufferedImage image = ImageIO.read(newFile);
            if (image != null) {
                fileUploadResult.setWidth(image.getWidth() + "");
                fileUploadResult.setHeight(image.getHeight() + "");
                isLegal = true;
            }
        } catch (IOException e) {
        }

        // 状�?
        fileUploadResult.setError(isLegal ? 0 : 1);
        
        if (!isLegal) {
            // 不合法，将磁盘上的文件删�?
            newFile.delete();
        }
        //response.setContentType(MediaType.TEXT_HTML_VALUE);
        //将java对象序列化成json数据
        return mapper.writeValueAsString(fileUploadResult);
    }

    //返回：E:\\0914\\taotao-upload\\images\\2015\\09\\17\\1112321321321.jpg
    private String getFilePath(String sourceFileName) {
        String baseFolder = /*myProperties.getRepositoryPath()+ */File.separator + "images";
        Date nowDate = new Date();
        // yyyy/MM/dd
        String fileFolder = baseFolder + File.separator + new DateTime(nowDate).toString("yyyy")
                + File.separator + new DateTime(nowDate).toString("MM") + File.separator
                + new DateTime(nowDate).toString("dd");
        File file = new File(fileFolder);
        if (!file.isDirectory()) {
            // 如果目录不存在，则创建目�?
            file.mkdirs();
        }
        // 生成新的文件�?
        String fileName = new DateTime(nowDate).toString("yyyyMMddhhmmssSSSS")
                + (new Random().nextInt(100)+100)+ "." + StringUtils.substringAfterLast(sourceFileName, ".");
        return fileFolder + File.separator + fileName;
    }
}
