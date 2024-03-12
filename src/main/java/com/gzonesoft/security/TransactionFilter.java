package com.gzonesoft.security;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.gzonesoft.domain.CommonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Order(1)
public class TransactionFilter implements Filter  {
	@Autowired private ApiKeyService apiKeyService;
	
	private final ObjectMapper mapper = new ObjectMapper();
	private CommonResponse commonResponse = new CommonResponse();
	
	@Override
	public void doFilter(ServletRequest request, javax.servlet.ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //LogUtil.debugLog("filter => API Token Filter");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

//        LogUtil.debugLog(req.getRequestURI());
//        LogUtil.debugLog(req.getContextPath());
//        LogUtil.debugLog(req.getServletPath());
        
        
        Pattern skipPattern = Pattern.compile("\\/api\\/file\\/download\\/");
        Matcher skipMatcher = skipPattern.matcher(req.getRequestURI());
        
        //API키 유효성 검사 Skip
        if(!skipMatcher.find()) {
            Pattern pattern = Pattern.compile("\\/api\\/");
            Matcher matcher = pattern.matcher(req.getRequestURI());
            
            //운영 시 주석 제거
            if(matcher.find()) {
    	        if ( req.getHeader("api_key") == null ) {
    				commonResponse.resultCode = "01";
    				commonResponse.resultMsg = "API 키 정보가 없습니다.";

    				res.setStatus(HttpStatus.UNAUTHORIZED.value());
    				res.setContentType(MediaType.APPLICATION_JSON_VALUE);

    				mapper.writeValue(res.getWriter(), commonResponse);
    	           return;
    	        }else {
    	        	if(!apiKeyService.validateApiKey(req.getHeader("api_key"))) {
    	
    	        		commonResponse.resultCode = "01";
    	        		commonResponse.resultMsg = "API Key is incorrect.";

    	                res.setStatus(HttpStatus.UNAUTHORIZED.value());
    	                res.setContentType(MediaType.APPLICATION_JSON_VALUE);

    	                mapper.writeValue(res.getWriter(), commonResponse);
    	        		return;
    	        	}
    	        }
            }
        }

        chain.doFilter(request, response);
		
	}
	
	
//	@Override
//	public void doFilter(ServletRequest request, javax.servlet.ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        //LogUtil.debugLog("filter => API Token Filter");
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse res = (HttpServletResponse) response;
//
////        LogUtil.debugLog(req.getRequestURI());
////        LogUtil.debugLog(req.getContextPath());
////        LogUtil.debugLog(req.getServletPath());
//        
//        Pattern pattern = Pattern.compile("\\/api\\/");
//        Matcher matcher = pattern.matcher(req.getRequestURI());
//        
//        //운영 시 주석 제거
//        if(matcher.find()) {
//	        //LogUtil.debugLog("req header => ");
//	        if ( req.getHeader("api_key") == null ) {
//	           res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "API 키 정보가 없습니다.");
//	           //res.sendRedirect("/error");
//	           return;
//	        }else {
//	        	if(!apiKeyService.validateApiKey(req.getHeader("api_key"))) {
//	        		res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "API 인증키가 잘 못 되었습니다.");
//	        		return;
//	        	}
//	        }
//        }
//        
//
//        
//        chain.doFilter(request, response);
//		
//	}	
}
