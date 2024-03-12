package com.gzonesoft.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
	private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);
	
	public static void infoLog(String msg) {
		logger.info(msg);
	}
	
	public static void debugLog(String msg) {
		logger.debug(msg);
	}
	
	public static void errorLog(String msg) {
		logger.error(msg);
	}
	
}

