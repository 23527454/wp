/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 语音配置信息Entity
 * @author Jumbo
 * @version 2017-07-13
 */
public class TtsSetting extends DataEntity<TtsSetting> {
	
	private static final long serialVersionUID = 1L;
	private String voiceType;		// 语音类型
	private String voiceConfig;		// 语音配置
	private String scope;		// 作用范围
	
	public TtsSetting() {
		super();
	}

	public TtsSetting(String id){
		super(id);
	}

	@Length(min=1, max=20, message="语音类型长度必须介于 1 和 20 之间")
	public String getVoiceType() {
		return voiceType;
	}

	public void setVoiceType(String voiceType) {
		this.voiceType = voiceType;
	}
	
	@Length(min=1, max=500, message="语音配置长度必须介于 1 和 500 之间")
	public String getVoiceConfig() {
		return voiceConfig;
	}

	public void setVoiceConfig(String voiceConfig) {
		this.voiceConfig = voiceConfig;
	}
	
	@Length(min=1, max=20, message="作用范围长度必须介于 1 和 20 之间")
	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
	
}