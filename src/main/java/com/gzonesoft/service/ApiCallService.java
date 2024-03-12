package com.gzonesoft.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzonesoft.database.ICommonDbService;
import com.gzonesoft.utils.LogUtil;
import com.gzonesoft.utils.SysUtils;

@Service("apiCallService")
public class ApiCallService {
	@Autowired private ICommonDbService commonDbService;

	/**
	 * DB 직접호툴
	 * @param procName
	 * @param jsonParam
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> execute(String procName, LinkedHashMap<String,Object>  jsonParam) {
		
		LogUtil.infoLog(String.format("[%s] 요청 수신 => %s", SysUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss"), jsonParam));
		
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		
		try {
			Map<String, Object> map = (Map<String, Object>) commonDbService.runProcedure(procName, jsonParam);

			resultMap.put("resultCode", map.get("outResultCd").toString());
			resultMap.put("resultMsg", map.get("outResultMsg").toString());
			resultMap.put("resultData", (List<Map<String, Object>>)map.get("outCursor"));			
			
		}catch(Exception ex) {
			LogUtil.errorLog(String.format("%s", ex));

			resultMap.put("resultCode", "01");
			resultMap.put("resultMsg", ex.toString());
			resultMap.put("resultData", null);
		}
		
		return resultMap;
	}
}
