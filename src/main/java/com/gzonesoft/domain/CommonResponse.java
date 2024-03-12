package com.gzonesoft.domain;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonProperty;

@Service("commonResponse")
public class CommonResponse {
	@JsonProperty("resultCode")
	public String resultCode = "";
	
	@JsonProperty("resultMsg")
	public String resultMsg = "";
	
	@JsonProperty("resultData")
	public Object resultData = null;
}
