package com.gzonesoft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service("mobileDriverService")
public class MobileDriverService {

	@Autowired private ApiCallService apiCallService;

	@Autowired private EAIService eaiService;


	/**
	 * 스케쥴러 구성 - 1분주기
	 * @throws InterruptedException
	 */
// 주석처리 - 2024.01.11
//	@Scheduled(fixedDelay =  1000 * 60)
//	public void scheduleJobEvery_1_min() throws InterruptedException {
//		LinkedHashMap<String, Object> jsonParam = new LinkedHashMap<String, Object>();
//		jsonParam.put("I_TYPE", "");
//		apiCallService.execute("MOP_JOB.EVERY_1_MIN", jsonParam) ;
//	}

	/**
	 * 스케쥴러 구성 - 10분주기
	 * @throws InterruptedException
	 */
// 주석처리 - 2024.01.11
//	@Scheduled(fixedDelay =  1000 * 60 * 10)
//	public void scheduleJobEvery_10_min() throws InterruptedException {
//		LinkedHashMap<String, Object> jsonParam = new LinkedHashMap<String, Object>();
//		jsonParam.put("I_TYPE", "");
//		apiCallService.execute("MOP_JOB.EVERY_10_MIN", jsonParam) ;
//	}

	/**
	 * 스케쥴러 구성 - 1시간(60분) 주기
	 * @throws InterruptedException
	 */
// 주석처리 - 2024.01.11
//	@Scheduled(fixedDelay =  1000 * 60 * 60)
//	public void scheduleJobEvery_1_hour() throws InterruptedException {
//		LinkedHashMap<String, Object> jsonParam = new LinkedHashMap<String, Object>();
//		jsonParam.put("I_TYPE", "");
//		apiCallService.execute("MOP_JOB.EVERY_1_HOUR", jsonParam) ;
//	}


	/**
	 * 업데이트 체크
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> appCheckDriver(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_LOGIN.APP_CHK", jsonParam) ;
	}

	/**
	 * 기사정보 조회(현장테스트용)
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> driverInfoSel(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_LOGIN.DRIVER_INFO_SEL", jsonParam) ;
	}

	/**
	 * 로그인요청
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> loginReq(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_LOGIN.DRIVER_LOGIN_REQ", jsonParam) ;
	}

	/**
	 * 차량선택정보 저장
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> selectVehicleSav(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_COMM.SELECT_VEHICLE_SAV", jsonParam) ;
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
	 * 출근하기(MOP_DRIVER_EVENT)
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> todayStart(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_DRIVER_EVENT.TODAY_START_SAV", jsonParam) ;
	}

	/**
	 * 퇴근하기(MOP_DRIVER_EVENT)
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> todayFinish(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_DRIVER_EVENT.TODAY_FINISH_SAV", jsonParam) ;
	}

	/**
	 * 배차목록 조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> dispatchListSel(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_DISPATCH.DISPATCH_LIST_SEL", jsonParam) ;
	}

	/**
	 * 배차선택
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> dispatchSelectSav(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_DISPATCH.DISPATCH_SELECT_SAV", jsonParam) ;
	}

	/**
	 * 출하요청
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> shippingCreateSav(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_DISPATCH.SHIPPING_CREATE_SAV", jsonParam) ;
	}

	/**
	 * 출발보고(MOP_DRIVER_EVENT)
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> departureReportSav(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_DRIVER_EVENT.DEPARTURE_REPORT_SAV", jsonParam) ;
	}

	/**
	 * 도착보고(MOP_DRIVER_EVENT)
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> arrivalReportSav(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_DRIVER_EVENT.ARRIVAL_REPORT_SAV", jsonParam) ;
	}

	/**
	 * 완료보고(MOP_DRIVER_EVENT)
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> deliFinishSav(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_DRIVER_EVENT.DELI_FINISH_SAV", jsonParam) ;
	}

	/**
	 * 현재위치정보 저장(MOP_GPS)
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> currentPositionSav(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_GPS.CURRENT_POSITION_SAV", jsonParam) ;
	}

	/**
	 * 사고목록 조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> accitentListSel(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_ACCIDENT.ACCIDENT_SEL", jsonParam) ;
	}

	/**
	 * 사고목록 저장
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> accitentInfoSav(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_ACCIDENT.ACCIDENT_SAV", jsonParam) ;
	}

	/**
	 * 전자, 출하전표 저장
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> invoiceSignSav(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_INVOICE.INVOICE_SIGN_SAV", jsonParam) ;
	}


	/**
	 * 파일 업로드 처리(출하전표 전용)
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> setFileInfoInvoice(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_INVOICE.FILE_INFO_SAV", jsonParam) ;
	}

	/**
	 * 전자, 출하전표 조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> invoiceListSel(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_INVOICE.INVOICE_LIST_SEL", jsonParam) ;
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
	 * 자차용,출하정보 조회
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> shippingInfoSel(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_DISPATCH.SHIPPING_INFO_SEL", jsonParam) ;
	}

	/**
	 * 기사정보 수정
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> driverUpdateSav(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_LOGIN.DRIVER_UPDATE_SAV", jsonParam) ;
	}

	/**
	 * 비밀번호 변경여부 체크
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> passwordChk(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_LOGIN.PASSWORD_CHK", jsonParam) ;
	}

	/**
	 * 사용자 인증 체크
	 * @param jsonParam
	 * @return
	 */
	public  Map<String,Object> authCheck(LinkedHashMap<String, Object> jsonParam) {
		return apiCallService.execute("MOP_LOGIN.AUTH_CHK", jsonParam) ;
	}
}
