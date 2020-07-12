package com.thinkgem.jeesite.modules.guard.web;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationHome;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.FileSizeHelper;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.entity.FileEntity;
import com.thinkgem.jeesite.modules.guard.service.FileEntityService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

@Controller
@RequestMapping(value = "${adminPath}/guard/file")
public class FileController extends BaseController {
	
	@Autowired
	private FileEntityService fileEntityService;
	
	@Value("${projectPath.filePath}")
	private String filePath;
	
	@ModelAttribute
	public FileEntity get(@RequestParam(required=false)String id) {
		FileEntity file = new FileEntity();
		if(StringUtils.isNotBlank(id)) {
			file = fileEntityService.get(id);
		}else {
			file = new FileEntity();
		}
		return file;
	}
	
	@RequiresPermissions("guard:file:view")
	@RequestMapping(value= {"/list",""})
	public String list(FileEntity fileEntity,HttpServletRequest request,HttpServletResponse response,Model model) {
		Page<FileEntity> page = fileEntityService.findPage(new Page<FileEntity>(request, response),
				fileEntity);
		model.addAttribute("page", page);
		return "modules/guard/fileList";
	}
	
	@RequiresPermissions("guard:file:view")
	@RequestMapping(value = "form")
	public String form(FileEntity fileEntity, Model model) {
		return backForm(fileEntity, model);
	}
	
	private String backForm(FileEntity fileEntity, Model model) {
		model.addAttribute("fileEntity", fileEntity);
		return "modules/guard/fileForm";
	}

	@RequiresPermissions("guard:file:edit")
	@RequestMapping(value = "/upload")
    public String upload(@RequestParam("file") MultipartFile file,FileEntity fileEntity,Model model) {
        try {
            if (StringUtils.isNotBlank(fileEntity.getId())) {
            	fileEntityService.update(fileEntity);
            }else {
            	//数据字典存储了最大可上传附件大小  xml也有  但无法动态控制 校验顺序 xml优先
            	/*double maxSize =  Double.valueOf(DictUtils.getDictValue("uploadSize", "sysconfig", "5"));
            	Double fileSize = FileSizeHelper.getFileSizeKB(file.getSize());
            	System.out.println(file.getSize());
            	if(fileSize>maxSize*1024) {
            		addMessage(model, "附件大小不能超过"+maxSize+"M");
            		return this.backForm(fileEntity, model);
            	}*/
            	
            	String fileType = DictUtils.getDictValue("uploadFileType", "sysconfig", "ALL");
            	String uploadFileType = FileUtils.getFileExtension(file.getOriginalFilename());
            	if(!fileType.toUpperCase().equals("ALL")) {
            		if(fileType.toLowerCase().indexOf(uploadFileType)==-1) {
            			addMessage(model, "不支持的上传附件类型！");
                		return this.backForm(fileEntity, model);
            		}
            	}
            	
            	String filePPath  = getJarPath()+filePath;
            	
            	FileUtils.upload(file, filePPath);
            	
            	fileEntity.setFileName(file.getOriginalFilename());
            	fileEntity.setFilePath(filePPath);
            	fileEntity.setFileType(FileUtils.getFileExtension(file.getOriginalFilename()));
            	fileEntityService.save(fileEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:" + Global.getAdminPath() + "/guard/file/?repage";
    }
	
	@RequestMapping("download")
    public String downLoad(FileEntity fileEntity,HttpServletRequest request,HttpServletResponse response,RedirectAttributes model){
        String filename=fileEntity.getFileName();
        String fileath = getJarPath()+filePath;
        File file = new File(fileath + "/" + filename);
       /* if(file.exists()){ //判断文件父目录是否存在
            response.setContentType("application/force-download");
            try {
				response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(filename, "utf-8"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;
            
            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file); 
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while(i != -1){
                    os.write(buffer);
                    i = bis.read(buffer);
                }
                
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("----------file download" + filename);
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else {
        	addMessage(model,"附件不存在！");
        	return "redirect:" + Global.getAdminPath() + "/guard/file/?repage";
        }*/
       String result =  FileUtils.downFile(file, request, response);
       if(StringUtils.isNotEmpty(result)) {
    	   addMessage(model,result);
       	return "redirect:" + Global.getAdminPath() + "/guard/file/?repage";
       }
        return null;
    }
	
	@RequiresPermissions("guard:file:edit")
	@RequestMapping("/delete")
	public String delete(FileEntity fileEntity,RedirectAttributes model) {
		File file = new File(fileEntity.getFilePath()+"/"+fileEntity.getFileName());
		if(file.exists()) {
			file.delete();
		}
		fileEntityService.delete(fileEntity);
		addMessage(model, "删除成功");
		return "redirect:" + Global.getAdminPath() + "/guard/file/?repage";
	}
	
	private String getJarPath() {
		ApplicationHome h = new ApplicationHome(getClass());
		File jarF = h.getSource();
		return jarF.getParentFile().toString();
	}
}
