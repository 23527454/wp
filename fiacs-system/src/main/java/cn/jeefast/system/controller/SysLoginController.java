package cn.jeefast.system.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fiacs.common.util.ByteUtil;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

import cn.jeefast.common.utils.R;
import cn.jeefast.common.utils.ShiroUtils;
import cn.jeefast.system.entity.SysUser;
import cn.jeefast.system.service.SysUserService;
import cn.jeefast.system.service.SysUserTokenService;

/**
 * 登录相关
 * 
 * @author theodo
 * @email 36780272@qq.com
 * @date 2016年11月10日 下午1:15:31
 */
@RestController
public class SysLoginController {
	@Autowired
	private Producer producer;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;
	@Value("${netty.webSocketPort}")
	private String webSocketPort;

	@Value("${logo.show}")
	private boolean logoShow;

	@RequestMapping("captcha.jpg")
	public void captcha(HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		// 生成文字验证码
		String text = producer.createText();
		// 生成图片验证码
		BufferedImage image = producer.createImage(text);
		// 保存到shiro session
		ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		IOUtils.closeQuietly(out);
	}

	/**
	 * 登录
	 */
	@RequestMapping(value = "/sys/login", method = RequestMethod.POST)
	public Map<String, Object> login(String username, String password, String captcha) throws IOException {
		/*
		 * String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
		 * if(!captcha.equalsIgnoreCase(kaptcha)){ return R.error("验证码不正确"); }
		 */

		// 用户信息
		SysUser user = sysUserService.queryByUserName(username);

		// 账号不存在、密码错误
		if (user == null || !user.getPassword().substring(16).equals(
				new Sha1Hash(password, ByteUtil.hexStringToBytes(user.getPassword().substring(0, 16)), 1024).toHex())) {
			return R.error("账号或密码不正确");
		}

		// 账号锁定
		if (user.getStatus() == 0) {
			return R.error("账号已被锁定,请联系管理员");
		}

		// 生成token，并保存到数据库
		R r = sysUserTokenService.createToken(user.getUserId());
		String host = InetAddress.getLocalHost().getHostAddress();
		System.out.println(host);
		return r.put("webSocketIp", host);
	}

	@RequestMapping(value = "/sys/logo", method = RequestMethod.POST)
	public Map<String, Object> logo() {
		return R.ok().put("logoShow", logoShow);
	}

	@CrossOrigin(origins = { "http://localhost:8182", "null" })
	@PostMapping("/upload")
	@ResponseBody
	public String upload(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return "上传失败，请选择文件";
		}

		String fileName = file.getOriginalFilename();
		String filePath = "/Users/itinypocket/workspace/temp/";
		File dest = new File("E:/temp/" + fileName);
		try {
			file.transferTo(dest);
			return "上传成功";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "上传失败！";
	}
	
	@RequestMapping("/vaildServer")
	public String vaildServer() {
		return "success";
	}
}
