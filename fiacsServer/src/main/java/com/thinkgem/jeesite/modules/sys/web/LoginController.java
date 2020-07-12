/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.security.shiro.session.SessionDAO;
import com.thinkgem.jeesite.common.servlet.ValidateCodeServlet;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.CookieUtils;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.entity.SysConfig;
import com.thinkgem.jeesite.modules.guard.service.SysConfigService;
import com.thinkgem.jeesite.modules.sys.entity.Index;
import com.thinkgem.jeesite.modules.sys.security.FormAuthenticationFilter;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.SuperDogUtil;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 登录Controller
 * 
 * @author ThinkGem
 * @version 2013-5-31
 */
@Controller
public class LoginController extends BaseController {

	private static Logger log = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	private SessionDAO sessionDAO;
	@Autowired
	private SysConfigService sysConfigService;
	// 用户和Session绑定关系
	public static final Map<String, HttpSession> USR_SESSION = new HashMap<String, HttpSession>();
	// SessionId和用户的绑定关系
	public static final Map<String, String> SESSIONID_USR = new HashMap<String, String>();

	/**
	 * 管理登录
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {

		SysConfig sysConfig = new SysConfig();
		List<SysConfig> sysConfigList = sysConfigService.findList(sysConfig);
		Index index = new Index();
		for (int i = 0; i < sysConfigList.size(); i++) {
			String attribute = sysConfigList.get(i).getAttribute();
			if ("system_name".equals(attribute)) {
				index.setSysName(sysConfigList.get(i).getValue());
			}
			if ("version_number".equals(attribute)) {
				index.setVersionNumber(sysConfigList.get(i).getValue());
			}
			if ("corporation_name".equals(attribute)) {
				index.setCorporationName(sysConfigList.get(i).getValue());
			}
		}
		
		String staticPath = request.getSession().getServletContext().getRealPath("static");
		String logoUrl = DictUtils.getDictValue("logoImage","sysconfig", "1");
		if(!new File(staticPath+logoUrl).exists()) {
			logoUrl="/images/logo.png";
		}
		index.setLogoUrl(logoUrl);
		String icoUrl = DictUtils.getDictValue("titleIco", "sysconfig",  "1");
		if(!new File(staticPath+icoUrl).exists()) {
			icoUrl="/favicon.ico";
		}
		index.setIcoUrl(icoUrl);
		String mainColor = DictUtils.getDictValue("mainColor", "sysconfig",  "#54b4cb");
		index.setMainColor(mainColor);
		model.addAttribute("index", index);
		Principal principal = UserUtils.getPrincipal();

		// // 默认页签模式
		// String tabmode = CookieUtils.getCookie(request, "tabmode");
		// if (tabmode == null){
		// CookieUtils.setCookie(response, "tabmode", "1");
		// }

		if (logger.isDebugEnabled()) {
			logger.debug("login, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}
		
		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))) {
			CookieUtils.setCookie(response, "LOGINED", "false");
		}

		// 如果已经登录，则跳转到管理首页
		if (principal != null && !principal.isMobileLogin()) {
			return "redirect:" + adminPath;
		}

	/*	// 当前登录的用户
		String userName = request.getParameter("userName");
		// 当前sessionId
		String sessionId = request.getSession().getId();
		// 删除当前sessionId绑定的用户，用户--HttpSession
		USR_SESSION.remove(SESSIONID_USR.remove(sessionId));
		// 删除当前登录用户绑定的HttpSession
		HttpSession session = USR_SESSION.remove(userName);
		if (session != null) {
			SESSIONID_USR.remove(session.getId());
			session.setAttribute("msg", "您的账号已经在另一处登录了,您被迫下线!");
		}
*/
		// String view;
		// view = "/WEB-INF/views/modules/sys/sysLogin.jsp";
		// view = "classpath:";
		// view +=
		// "jar:file:/D:/GitHub/jeesite/src/main/webapp/WEB-INF/lib/jeesite.jar!";
		// view += "/"+getClass().getName().replaceAll("\\.",
		// "/").replace(getClass().getSimpleName(), "")+"view/sysLogin";
		// view += ".jsp";
		return "modules/sys/sysLogin";
	}

	/**
	 * 登录失败，真正登录的POST请求由Filter完成
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.POST)
	public String loginFail(HttpServletRequest request, HttpServletResponse response, Model model) {
		Principal principal = UserUtils.getPrincipal();

		// 如果已经登录，则跳转到管理首页
		if (principal != null) {
			return "redirect:" + adminPath;
		}

		String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
		boolean rememberMe = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
		boolean mobile = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
		String exception = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		String message = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);

		if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")) {
			message = "用户或密码错误, 请重试.";
		}

		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);

		if (logger.isDebugEnabled()) {
			logger.debug("login fail, active session size: {}, message: {}, exception: {}",
					sessionDAO.getActiveSessions(false).size(), message, exception);
		}

		// 非授权异常，登录失败，验证码加1。
		if (!UnauthorizedException.class.getName().equals(exception)) {
			model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, true, false));
		}

		// 验证失败清空验证码
		String validateCode = IdGen.uuid();
		request.getSession().setAttribute(ValidateCodeServlet.VALIDATE_CODE, validateCode);

		// 如果是手机登录，则返回JSON字符串
		if (mobile) {
			return renderString(response, model);
		}

		return "modules/sys/sysLogin";
	}

	/**
	 * 登录成功，进入管理首页
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "${adminPath}")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		SysConfig sysConfig = new SysConfig();
		List<SysConfig> sysConfigList = sysConfigService.findList(sysConfig);
		Index index = new Index();
		for (int i = 0; i < sysConfigList.size(); i++) {
			String attribute = sysConfigList.get(i).getAttribute();
			if ("system_name".equals(attribute)) {
				index.setSysName(sysConfigList.get(i).getValue());
			}
			if ("version_number".equals(attribute)) {
				index.setVersionNumber(sysConfigList.get(i).getValue());
			}
			if ("corporation_name".equals(attribute)) {
				index.setCorporationName(sysConfigList.get(i).getValue());
			}
		}
		String staticPath = request.getSession().getServletContext().getRealPath("static");
		String logoUrl = DictUtils.getDictValue("logoImage","sysconfig", "1");
		if(!new File(staticPath+logoUrl).exists()) {
			logoUrl="/images/logo.png";
		}
		index.setLogoUrl(logoUrl);
		String icoUrl = DictUtils.getDictValue("titleIco", "sysconfig",  "1");
		if(!new File(staticPath+icoUrl).exists()) {
			icoUrl="/favicon.ico";
		}
		index.setIcoUrl(icoUrl);
		String mainColor = DictUtils.getDictValue("mainColor", "sysconfig",  "#54b4cb");
		index.setMainColor(mainColor);
		model.addAttribute("index", index);
		Principal principal = UserUtils.getPrincipal();
		// 登录成功后，验证码计算器清零
		isValidateCodeLogin(principal.getLoginName(), false, true);

		if (logger.isDebugEnabled()) {
			logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}

		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))) {
			String logined = CookieUtils.getCookie(request, "LOGINED");
			if (StringUtils.isBlank(logined) || "false".equals(logined)) {
				CookieUtils.setCookie(response, "LOGINED", "true");
			} else if (StringUtils.equals(logined, "true")) {
				UserUtils.getSubject().logout();
				return "redirect:" + adminPath + "/login";
			}
		}

		// 如果是手机登录，则返回JSON字符串
		if (principal.isMobileLogin()) {
			if (request.getParameter("login") != null) {
				return renderString(response, principal);
			}
			if (request.getParameter("index") != null) {
				return "modules/sys/sysIndex";
			}
			return "redirect:" + adminPath + "/login";
		}
		// 登录成功获取加密狗数据进行缓存
		SuperDogUtil.cacheConfineNumber();
		log.error("functionUserCache:"+UserUtils.getCache(UserUtils.CACHE_FUNCTION_USER));
		log.error("functionEquCache:"+UserUtils.getCache(UserUtils.CACHE_FUNCTION_EQU));
		log.error("functionStaffCache:"+UserUtils.getCache(UserUtils.CACHE_FUNCTION_STAFF));
		// // 登录成功后，获取上次登录的当前站点ID
		// UserUtils.putCache("siteId",
		// StringUtils.toLong(CookieUtils.getCookie(request, "siteId")));

		// System.out.println("==========================a");
		// try {
		// byte[] bytes =
		// com.thinkgem.jeesite.common.utils.FileUtils.readFileToByteArray(
		// com.thinkgem.jeesite.common.utils.FileUtils.getFile("c:\\sxt.dmp"));
		// UserUtils.getSession().setAttribute("kkk", bytes);
		// UserUtils.getSession().setAttribute("kkk2", bytes);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//// for (int i=0; i<1000000; i++){
		//// //UserUtils.getSession().setAttribute("a", "a");
		//// request.getSession().setAttribute("aaa", "aa");
		//// }
		// System.out.println("==========================b");
		return "modules/sys/sysIndex";
	}

	/* 
	@RequestMapping(value = "${adminPath}/sys/login/checkUserOnline")
	@ResponseBody
	public void checkUserOnline(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		out.print(session.getAttribute("msg"));
	}
*/
	/**
	 * 获取主题方案
	 */
	@RequestMapping(value = "/theme/{theme}")
	public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request,
			HttpServletResponse response) {
		if (StringUtils.isNotBlank(theme)) {
			CookieUtils.setCookie(response, "theme", theme);
		} else {
			theme = CookieUtils.getCookie(request, "theme");
		}
		return "redirect:" + request.getParameter("url");
	}

	/**
	 * 是否是验证码登录
	 * 
	 * @param useruame
	 *            用户名
	 * @param isFail
	 *            计数加1
	 * @param clean
	 *            计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean) {
		Map<String, Integer> loginFailMap = (Map<String, Integer>) CacheUtils.get("loginFailMap");
		if (loginFailMap == null) {
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum == null) {
			loginFailNum = 0;
		}
		if (isFail) {
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean) {
			loginFailMap.remove(useruame);
		}
		return false;
	}


}
