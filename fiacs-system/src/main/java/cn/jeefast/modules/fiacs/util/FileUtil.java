package cn.jeefast.modules.fiacs.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.util.ResourceUtils;

public class FileUtil {	
	/**
	 * 下载项目根目录下doc下的文件
	 * @param response response
	 * @param fileName 文件名
	 * @return 返回结果 成功或者文件不存在
	 */
	public static String downloadFileWindows(HttpServletResponse response, String fileName,String filePath) {
		File path = null;
		response.setHeader("content-type", "application/octet-stream");
		response.setContentType("application/octet-stream");
		try {
			response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		byte[] buff = new byte[1024];
		BufferedInputStream bis = null;
		OutputStream os = null;		
		try {
			//path = new File(ResourceUtils.getURL("classpath:").getPath());
			//System.out.println("path:"+System.getProperty("user.dir"));
			os = response.getOutputStream();
			
			path = new File(filePath + fileName);
			
			bis = new BufferedInputStream(new FileInputStream(path));
			int i = bis.read(buff);
			while (i != -1) {
				os.write(buff, 0, buff.length);
				os.flush();
				i = bis.read(buff);
			}
		} catch (FileNotFoundException e1) {
			//e1.getMessage()+"系统找不到指定的文件";
			//return "系统找不到指定的文件";
			return downloadFileNnix(response,fileName);
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "success";

	}
	
	
	/**
	 * 下载项目根目录下doc下的文件
	 * @param response response
	 * @param fileName 文件名
	 * @return 返回结果 成功或者文件不存在
	 */
	public static String downloadFileNnix(HttpServletResponse response, String fileName) {
		InputStream stream = FileUtil.class.getClassLoader().getResourceAsStream("logs/" + fileName);
		response.setHeader("content-type", "application/octet-stream");
		response.setContentType("application/octet-stream");
		try {
			String name = java.net.URLEncoder.encode(fileName, "UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLDecoder.decode(name, "ISO-8859-1") );
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		byte[] buff = new byte[1024];
		BufferedInputStream bis = null;
		OutputStream os = null;		
		try {
			os = response.getOutputStream();			
			bis = new BufferedInputStream(stream);
			int i = bis.read(buff);
			while (i != -1) {
				os.write(buff, 0, buff.length);
				os.flush();
				i = bis.read(buff);
			}
		} catch (FileNotFoundException e1) {
			//e1.getMessage()+"系统找不到指定的文件";
			return "系统找不到指定的文件";
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "success";
	}
}
