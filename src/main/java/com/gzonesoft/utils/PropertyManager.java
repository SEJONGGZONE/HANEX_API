package com.gzonesoft.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyManager {
	private static PropertyManager rmsProperies;
	private Properties props = new Properties();

	public static PropertyManager getInstance(String filePath) {
		synchronized (PropertyManager.class) {
			// if (rmsProperies == null) {
			rmsProperies = new PropertyManager();
			rmsProperies.init(filePath);
			// }
		}
		return rmsProperies;
	}

	public void init(String filePath) {
		FileInputStream fis = null;
		try{
			// 프로퍼티 파일 스트림에 담기
            fis = new FileInputStream(filePath);

            // 프로퍼티 파일 로딩
            props.load(new java.io.BufferedInputStream(fis));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
            if(fis != null) try{fis.close();}catch(IOException e){}
        }
	}	
	
	public String getProperty(String key) {
		return (String) props.get(key);
	}
}
