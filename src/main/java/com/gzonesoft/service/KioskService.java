package com.gzonesoft.service;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

@Service("kioskService")
public class KioskService {

	@Autowired private ApiCallService apiCallService;

	/**
	 * kioskApiTest
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> kioskApiTest(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("DIP_LOGIN.APP_CHK", jsonParam) ;
	}

	/**
	 * 앱체크(키오스크)
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> appCheckKiosk(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("DIP_LOGIN.APP_CHK", jsonParam) ;
	}

	/**
	 * 앱 업데이트 파일 목록
	 * @return
	 */
	public  Map<String,Object> getUpdateFiles(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("DIP_LOGIN.UPDATE_FILES", jsonParam) ;
	}

	/**
	 * 사용자 로그인
	 * @param jsonParam
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public  Map<String,Object> driverLogin(LinkedHashMap<String, Object> jsonParam) {

//		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
//
//		try {
//			// 암호화하여 다시넣는다..
//			String orgString = jsonParam.get("I_PASS").toString();
//			String encString = SysUtils.encryptSHA256(orgString);
//			jsonParam.put("I_PASS", encString);
//
//			resultMap = apiCallService.execute("DIP_LOGIN.LOGIN_REQ", jsonParam) ;
//
//		}catch(Exception ex) {
//			LogUtil.errorLog(String.format("%s", ex));
//
//			resultMap.put("resultCode", "01");
//			resultMap.put("resultMsg", ex.toString());
//			resultMap.put("resultData", null);
//		}
//
//		return resultMap;

		return apiCallService.execute("DIP_LOGIN.LOGIN_REQ", jsonParam) ;
	}

	/**
	 * 최종작업상태조회(공지사항포함)
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> lastWorkSel(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("DIP_COMM.LASTWORK_SEL", jsonParam) ;
	}

	/**
	 * 키오스크 상태(헬스)체크
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> kioskStatusSav(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("DIP_LOGIN.KIOSK_STATUS_SAV", jsonParam) ;
	}

	/**
	 * 차량조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> getDrivingCar(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("DIP_LOGIN.DRIVING_CAR_SEL", jsonParam) ;
	}

	/**
	 * 지문조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> getFingerPrint(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("DIP_LOGIN.FINGER_PRINT_SEL", jsonParam) ;
	}

	/**
	 * 주문조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> getOrder(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("DIP_LOGIN.ORDER_SEL", jsonParam) ;
	}



	/**
	 * 배차목록 조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> dispatchListSel(LinkedHashMap<String, Object> jsonParam) {
		jsonParam.put("I_REQ_TYPE", "10");
		return apiCallService.execute("DIP_SHIPPING.DISPATCH_LIST_SEL", jsonParam) ;
	}

	/**
	 * 배차선택(KIOSK)
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> dispatchSelectSav(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("DIP_SHIPPING.DISPATCH_SELECT_SAV", jsonParam) ;
	}


	/**
	 * 출하요청
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> shippingCreateSav(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("DIP_SHIPPING.SHIPPING_CREATE_SAV", jsonParam) ;
	}



	/**
	 * SAVE, DIT_IF_OPERATE_INFO
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> loadingRequestSav(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("DIP_IF.LOADING_REQUEST_SAV", jsonParam) ;
	}

	/**
	 * 출하전표발행, 출하내역 조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> shippingResultSel(LinkedHashMap<String, Object> jsonParam) {
		jsonParam.put("I_REQ_TYPE", "20");
		return apiCallService.execute("DIP_SHIPPING.DISPATCH_LIST_SEL", jsonParam) ;
	}

	/**
	 * 출하결과 입력
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> shippingResultSav(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("DIP_SHIPPING.SHIPPING_RESULT_SAV", jsonParam) ;
	}

	/**
	 * 출하전표발행 요청
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> ticketCreateSav(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("DIP_SHIPPING.TICKET_CREATE_SAV", jsonParam) ;
	}


	/**
	 * 파일정보 저장
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> setFileInfo(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("DIP_LOGIN.FILE_INFO_SAV", jsonParam) ;
	}


	/**
	 * 파일정보 저장(키오스크전용)
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> setFileInfoKiosk(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("DIP_COMM.FILE_INFO_SAV", jsonParam) ;
	}


	/**
	 * 랜덤키 얻기
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> getRandomKeySel(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("DIP_IF.RANDOM_KEY_SEL", jsonParam) ;
	}


	/**
	 * 출하지정보 얻기
	 * @param plantCode
	 * @return
	 */
	public  Map<String,Object> getPlantInfo(String plantCode) {
		LinkedHashMap<String, Object> jsonParam = new LinkedHashMap<String, Object>();
		jsonParam.put("I_PLANT_CODE", plantCode);

		return apiCallService.execute("DIP_COMM.PLANT_INFO_SEL", jsonParam) ;
	}

	/**
	 * 플랜트정보-결과입력여부 얻기(1:미입력, 2:입력)
	 * @param plantCode
	 * @return
	 */
	public String getResultInputGb(String plantCode) {
		String retString = "";
		try {
			// 플랜트정보 얻어오기...
			Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
			LinkedHashMap<String, Object> jsonParam = new LinkedHashMap<String, Object>();
			jsonParam.put("I_PLANT_CODE", plantCode);
			resultMap = apiCallService.execute("DIP_COMM.PLANT_INFO_SEL", jsonParam) ;

			// 결과데이터 처리..
			JSONObject jsonObject = new JSONObject(resultMap);
			String jsonData = jsonObject.toString();
			JsonElement jelement = new JsonParser().parse(jsonData);
			JsonObject jObject = jelement.getAsJsonObject();
			String resultCode = jObject.get("resultCode").getAsString();
			String resultMsg = jObject.get("resultMsg").getAsString();
			JsonArray jarrayRecordSet = jObject.get("resultData").getAsJsonArray();
			JsonObject jobjectStoreCheck = jarrayRecordSet.get(0).getAsJsonObject();

			// 데이타 추출..(결과입력여부)
			retString = jobjectStoreCheck.get("ZACTGB").getAsString();

		} catch (Exception ex) {
			ex.printStackTrace();
			retString = "";
		} finally {
			return retString;
		}
	}

	/**
	 * 출하제약 확인하기
	 * @param I_WERKS
	 * @param I_VEHICLE
	 * @param I_DRIVERCODE
	 * @param I_SHNUMBER
	 * @param I_VBELN
	 * @param I_POSNN
	 * @param I_VSBED
	 * @param I_MATNR
	 * @return
	 */
	public  Map<String, Object> checkShippingCheclSel(String I_WERKS, String I_VEHICLE, String I_DRIVERCODE, String I_SHNUMBER, String I_VBELN, String I_POSNN, String I_VSBED, String I_MATNR)
	{
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		try {
			LinkedHashMap<String, Object> jsonParam = new LinkedHashMap<String, Object>();
			jsonParam.put("I_WERKS", I_WERKS);
			jsonParam.put("I_VEHICLE", I_VEHICLE);
			jsonParam.put("I_DRIVERCODE", I_DRIVERCODE);
			jsonParam.put("I_SHNUMBER", I_SHNUMBER);
			jsonParam.put("I_VBELN", I_VBELN);
			jsonParam.put("I_POSNN", I_POSNN);
			jsonParam.put("I_VSBED", I_VSBED);
			jsonParam.put("I_MATNR", I_MATNR);

			resultMap = apiCallService.execute("DIP_SHIPPING.SHIPPING_CHECK_SEL", jsonParam);

//			// 결과데이터 처리..
//			JSONObject jsonObject = new JSONObject(resultMap);
//			String jsonData = jsonObject.toString();
//			JsonElement jelement = new JsonParser().parse(jsonData);
//			retObj = jelement.getAsJsonObject();

		} catch (Exception ex) {
			ex.printStackTrace();
			resultMap = null;
		}
		return resultMap;
	}


	/**
	 * 출하요청 조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> shippingRequestSel(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("DIP_SHIPPING.SHIPPING_REQUEST_SEL", jsonParam) ;
	}
}
