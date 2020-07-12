package com.xk.netty.util;

import io.netty.util.AttributeKey;

public class AttributeUtil {
	
	public static AttributeKey<String> get(String key){
		return AttributeKey.valueOf(key);
	}
}
