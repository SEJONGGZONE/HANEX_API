package com.gzonesoft.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("apiKeyService")
public class ApiKeyServiceImpl implements ApiKeyService{
	@Value("${api.server.apikey}") private String serverApiKey;
	
	@Override
	public boolean validateApiKey(String apiKey) {
		if(!serverApiKey.equals(apiKey)) {
			return false;
		}
		return true;
	}

}
