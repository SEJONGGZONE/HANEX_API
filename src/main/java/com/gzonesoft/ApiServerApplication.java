package com.gzonesoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

import com.gzonesoft.utils.LogUtil;
import com.gzonesoft.utils.SysUtils;
import org.springframework.scheduling.annotation.EnableScheduling;

@ImportResource("file:config/spring-main.xml")
@SpringBootApplication
@EnableScheduling // 스켖쥴러 추가(2022.11.07)
@EnableAutoConfiguration(exclude = { FreeMarkerAutoConfiguration.class }) //FreeMarker 디폴트 설정 사용안함
public class ApiServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiServerApplication.class, args);
	}
	
	public ApiServerApplication(){
		init();
	}
	
	public void init(){ 
		try{
			LogUtil.debugLog(String.format("[%s] ================[ K I O S K     A P I     S E R V E R     S T A R T ]================", SysUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss")));
			
		}catch(Exception ex){
			LogUtil.errorLog(String.format("[%s] %s", SysUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss"), ex.toString())); 
		}
	}
}
