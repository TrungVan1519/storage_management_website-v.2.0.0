package com.trungvan.security;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;

import com.trungvan.entity.Auth;
import com.trungvan.entity.User;
import com.trungvan.entity.UserRole;
import com.trungvan.utils.Constant;

public class FilterSystem implements HandlerInterceptor {

	private final Logger log = Logger.getLogger(this.getClass());

	/**
	 * Khi request endpoint thi chay ham nay truoc roi moi chay den Controller xu ly endpoint do
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		log.info("<<==>> URI List");
		log.info(">> Request URI: " + request.getRequestURI()); 
		 
		// Kiem tra user da dang nhap thanh cong vao web chua
		User user = (User) request.getSession().getAttribute(Constant.USER_IN_SESSION);
		if(user == null) {
			
			response.sendRedirect(request.getContextPath() + "/login");
			return false;
		} 
		
		String url = request.getServletPath();
		if(!hasPermission(url, user)) {
			
			response.sendRedirect(request.getContextPath() + "/exception-hanlder/access-denied");
			return false;
		}
		return true;
	}
	
	private boolean hasPermission(String url, User user) {
		
		// Vi 2 endpoint nay khi tao DB ta khong nghi~ toi de ma them vao DB nen khi kiem tra
		//		ta se cho no thoa man luon, vi du gi user cung phai dang nhap tu man hinh Login
		//		truoc roi moi vao man hinh Home ma
		if(url.contains("/home") 
				|| url.contains("/logout")
				|| url.contains("/exception-hanlder/access-denied")) {
			
			return true;
		}
		
		UserRole userRole = user.getUserRoles().iterator().next();
		Set<Auth> auths = userRole.getRole().getAuths();
		
		for(Auth auth: auths) {
			
			if(url.contains(auth.getMenu().getUrl())) {
				
				// Kiem tra user co tham quyen de dang nhap vao URL khong
				return auth.getPermission() == 1;
			}
		}
		
		return false;
	}
}
