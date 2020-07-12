package com.thinkgem.jeesite.common.filter;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SessionFilter implements Filter {

	private String validateCodeUrl;

	public void setValidateCodeUrl(String validateCodeUrl) {
		this.validateCodeUrl = validateCodeUrl;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		// if (httpServletRequest.getSession().getAttribute("user") == null) {
//		if (!SecurityUtils.getSubject().isAuthenticated()) {
		if (UserUtils.getPrincipal() == null) {
			// 判断session里是否有用户信息
		if (httpServletRequest.getHeader("x-requested-with") != null
					&& httpServletRequest.getHeader("x-requested-with")
							.equalsIgnoreCase("XMLHttpRequest")) {

				// 如果校验验证码，直接放行
				String requestUrl = httpServletRequest.getServletPath();
				if (StringUtils.isNotBlank(validateCodeUrl) && validateCodeUrl.equals(requestUrl)) {
					chain.doFilter(request, response);
					return;
				}

				// 如果是ajax请求响应头会有，x-requested-with
				httpServletResponse.setHeader("rootPath", httpServletRequest.getContextPath()+Global.getAdminPath());// 在响应头设置session状态
				httpServletResponse.setHeader("sessionstatus", "timeout");// 在响应头设置session状态
				return;
			}
		}
		
		
		 
		 
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

	
}