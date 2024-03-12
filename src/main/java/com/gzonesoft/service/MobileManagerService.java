package com.gzonesoft.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("mobileManagerService")
public class MobileManagerService {
	
	@Autowired private ApiCallService apiCallService;

	/**
	 * 업데이트 체크
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> appCheck(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_LOGIN.APP_CHK", jsonParam) ;
	}


	/**
	 * 로그인요청
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> loginReq(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_LOGIN.MANAGER_LOGIN_REQ", jsonParam) ;
	}

	/**
	 * 코드조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> codeSel(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_COMM.CODE_SEL", jsonParam) ;
	}

	/**
	 * 최종작업상태조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> lastWorkSel(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_COMM.LASTWORK_SEL", jsonParam) ;
	}


	/**
	 * 위치기반 거래처 조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> custSearch(String type, LinkedHashMap<String, Object> jsonParam) {
		LinkedHashMap<String, Object> param = new LinkedHashMap<String, Object>();
		param.put("I_TYPE", type);
		Set<String> keys = jsonParam.keySet();
		for (String key : keys) {
			param.put(key, jsonParam.get(key));
		}
		return apiCallService.execute("MOP_LBS.CUST_POSITION", param) ;
	}


	/**
	 * 위치기반 담당거래처 조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> custUserSearch(String type, LinkedHashMap<String, Object> jsonParam) {
		LinkedHashMap<String, Object> param = new LinkedHashMap<String, Object>();
		param.put("I_TYPE", type);
		Set<String> keys = jsonParam.keySet();
		for (String key : keys) {
			param.put(key, jsonParam.get(key));
		}
		return apiCallService.execute("MOP_LBS.CUSTUSER_POSITION", param) ;
	}



	/**
	 * 위치기반 주문조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> orderSearch(String type, LinkedHashMap<String, Object> jsonParam) {
		LinkedHashMap<String, Object> param = new LinkedHashMap<String, Object>();
		param.put("I_TYPE", type);
		Set<String> keys = jsonParam.keySet();
		for (String key : keys) {
			param.put(key, jsonParam.get(key));
		}
		return apiCallService.execute("MOP_LBS.ORDER_POSITION", param) ;
	}



	/**
	 * 위치기반 차량조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> vehicleSearch(String type, LinkedHashMap<String, Object> jsonParam) {
		LinkedHashMap<String, Object> param = new LinkedHashMap<String, Object>();
		param.put("I_TYPE", type);
		Set<String> keys = jsonParam.keySet();
		for (String key : keys) {
			param.put(key, jsonParam.get(key));
		}
		return apiCallService.execute("MOP_LBS.VEHICLE_POSITION", param) ;
	}



	/**
	 * 위치기반 지정 차량조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> targetVehicleSearch(String type, LinkedHashMap<String, Object> jsonParam) {
		LinkedHashMap<String, Object> param = new LinkedHashMap<String, Object>();
		param.put("I_TYPE", type);
		Set<String> keys = jsonParam.keySet();
		for (String key : keys) {
			param.put(key, jsonParam.get(key));
		}
		return apiCallService.execute("MOP_LBS.TARGET_VEHICLE_POSITION", param) ;
	}



	/**
	 * 통합검색(메인조회)
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> mainSearch(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_LBS.MAIN_SEARCH", jsonParam) ;
	}



	/**
	 * 설정조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> userSettingSel(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_COMM.USER_SETTING_SEL", jsonParam) ;
	}



	/**
	 * 설정저장
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> userSettingSav(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_COMM.USER_SETTING_SAV", jsonParam) ;
	}

	/**
	 * 재고조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> sourceSearch(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_LBS.SOURCE_SEARCH", jsonParam) ;
	}

	/**
	 * 재고조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> targetSourceSearch(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_LBS.TARGET_SOURCE_SEARCH", jsonParam) ;
	}

	/**
	 * 경로조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> traceSearch(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_LBS.TRACE_SEARCH", jsonParam) ;
	}

	/**
	 * 출하지-재고조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> tankCapaSel(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_IF.ATG_TANK_CAPA_SEL", jsonParam) ;
	}

	/**
	 * 저유소-재고 상세 조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> sourceStockSearch(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_IF.SOURCE_STOCK_SEARCH", jsonParam) ;
	}

	/**
	 * 영업/배차 주문 조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> targetOrderSearch(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_LBS.TARGET_ORDER_POSITION", jsonParam) ;
	}

}
