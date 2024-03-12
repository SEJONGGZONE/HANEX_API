package com.gzonesoft.exception;

public class CustomException {

	public static String getMessage(Exception ex) {
		if(ex.getCause() != null) {
			return ex.getCause().getMessage();
		}else {
			return ex.getMessage();
		}
	}
}

