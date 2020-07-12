package com.thinkgem.jeesite.modules.sys.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantUtil {
	
	public static String databaseUsername;
	
	public static String databasePassword;
	
	@Value("${jdbc.password}") 
	public  void setDatabasePassword(String password) {
		databasePassword = password;
	}
	@Value("${jdbc.username}") 
	public  void setDatabaseUsername(String username) {
		databaseUsername = username;
	}
}
