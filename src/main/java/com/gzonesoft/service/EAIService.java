package com.gzonesoft.service;

import com.gzonesoft.utils.LogUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.EaiManager;
import core.Func;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service("eaiService")
public class EAIService {
	@Value("${eai.service.address}") private String eai_server_ip;

	/**
	 * 배차내역조회
	 * @param plantList
	 * @param vehicleNo
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public Map<String,Object> disDispatchDisplay(String plantList, String vehicleNo, String fromDate, String toDate) {

		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

		try {

			HashMap hsInput = new HashMap();	//
			HashMap hsTran = new HashMap();	// 실제 전송할 HashMap
			HashMap hsOutput = new HashMap();    // 전송 후 결과값을 전송받을 HashMap

			String RS_MSGTYP = "";
			String RS_MSGNUM = "";
			String RS_MSGTXT = "";

			hsInput.put("I_VEHICLE", vehicleNo);
			hsInput.put("I_ZFRDAT", fromDate);
			hsInput.put("I_ZTODAT", toDate);

			// 3341:1,3741:1,4086:2,4091:2,4800:2,4801:2

			if (plantList.equals("")) {
				HashMap[] hsTran1 = new HashMap[1];	// 실제 전송할 HashMap
				hsTran1[0] = new HashMap();
				// hsTran1[0].put("WERKS", "ALL");
				hsTran1[0].put("WERKS", "");
				hsInput.put("T_PLANT", "");
			} else {
				String arrPlant[] = plantList.split(",");
				HashMap[] hsTran2 = new HashMap[arrPlant.length];	// 실제 전송할 HashMap
				for(int i=0; i<arrPlant.length; i++) {
					String arrInfo[] = arrPlant[i].split(":");
					String werks = arrInfo[0];
					hsTran2[i] = new HashMap();
					hsTran2[i].put("WERKS", werks);
				}
				hsInput.put("T_PLANT", hsTran2);
			}
			hsTran.put("IF_IN", hsInput);

			LogUtil.infoLog(String.format("[EAI요청시작, DIS_Dispatch_Display]-----------------------------------------------------"));
			LogUtil.infoLog(String.format("plantList=%s,vehicleNo=%s,fromDate=%s,toDate=%s"
					,plantList, vehicleNo, fromDate, toDate));
			EaiManager cmManager = new EaiManager();
			hsOutput = (HashMap)cmManager.executeService(eai_server_ip, "DIS_Dispatch_Display.srv", "srcRoutine_Pub", hsTran);
			HashMap tmpHashMap = (HashMap)hsOutput.get("IF_OUT");
			LogUtil.infoLog(String.format("[EAI요청응답, DIS_Dispatch_Display]-----------------------------------------------------"));
//		Object objRv = (HashMap)hsOutput.get("IF_OUT");
//		tempHash000 = (HashMap) objRv;

			HashMap tmpHsMsg = (HashMap)tmpHashMap.get("E_RETURN");
			HashMap tmpHsData[] = (HashMap[])tmpHashMap.get("T_SHIPMENT");
			RS_MSGTYP = Func.getTrim((String)tmpHsMsg.get("MSGTYP"));
			RS_MSGNUM = Func.getTrim((String)tmpHsMsg.get("MSGNUM"));
			RS_MSGTXT = Func.getTrim((String)tmpHsMsg.get("MSGTXT"));


			JSONObject json =  new JSONObject(tmpHashMap);

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //매핑되지 않는 항목은 Skip
			Map<String, Object> rsMap = mapper.readValue(String.format("%s", json), new TypeReference<LinkedHashMap<String,Object>>(){});

			resultMap.put("resultType", RS_MSGTYP);
			resultMap.put("resultCode", RS_MSGNUM);
			resultMap.put("resultMsg", RS_MSGTXT);
			resultMap.put("resultData",  rsMap.get("T_SHIPMENT"));


		} catch(Exception ex) {
			LogUtil.errorLog(String.format("%s", ex));

			resultMap.put("resultType", "E");
			resultMap.put("resultCode", "01");
			resultMap.put("resultMsg", ex.toString());
			resultMap.put("resultData", null);
		}

		return resultMap;

	}

	/**
	 * 출하실적 조회
	 * @param plantList
	 * @param vehicleNo
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public Map<String,Object> disLoadingResultDisplay(String plantList, String vehicleNo, String fromDate, String toDate) {

		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

		try {

			HashMap hsInput = new HashMap();	//
			HashMap hsTran = new HashMap();	// 실제 전송할 HashMap
			HashMap hsOutput = new HashMap();    // 전송 후 결과값을 전송받을 HashMap

			String RS_MSGTYP = "";
			String RS_MSGNUM = "";
			String RS_MSGTXT = "";

			hsInput.put("I_VEHICLE", vehicleNo);
			hsInput.put("I_ZFRDAT", fromDate);
			hsInput.put("I_ZTODAT", toDate);
//			hsInput.put("I_ZIFCOD", "");


//			HashMap[] hsTran1 = null;
//			hsTran1 = new HashMap[1];	// 실제 전송할 HashMap
//			hsTran1[0] = new HashMap();
//			hsTran1[0].put("WERKS", plantCd);
//			hsTran1[0].put("ZACTGB", ZACTGB); // 1:TAS, 2:NON-TAS

			// 3341:1,3741:1,4086:2,4091:2,4800:2,4801:2
			String arrPlant[] = plantList.split(",");
			HashMap[] hsTran1 = new HashMap[arrPlant.length];	// 실제 전송할 HashMap
			for(int i=0; i<arrPlant.length; i++) {
				String arrInfo[] = arrPlant[i].split(":");
				String werks = arrInfo[0];
				String zactgb = arrInfo[1];
				hsTran1[i] = new HashMap();
				hsTran1[i].put("WERKS", werks);
				hsTran1[i].put("ZACTGB", zactgb); // 플랜트 수기입력여부 얻기..
			}
			hsInput.put("T_PLANT", hsTran1);
			hsTran.put("IF_IN", hsInput);

			LogUtil.infoLog(String.format("[EAI요청시작, DIS_Loading_Result_Display]-----------------------------------------------------"));
			LogUtil.infoLog(String.format("plantList=%s,vehicleNo=%s,fromDate=%s,toDate=%s",plantList, vehicleNo, fromDate, toDate));
			EaiManager cmManager = new EaiManager();
			hsOutput = (HashMap)cmManager.executeService(eai_server_ip, "DIS_Loading_Result_Display.srv", "srcRoutine_Pub", hsTran);
			HashMap tmpHashMap = (HashMap)hsOutput.get("IF_OUT");
			LogUtil.infoLog(String.format("[EAI요청응답, DIS_Loading_Result_Display]-----------------------------------------------------"));
//		Object objRv = (HashMap)hsOutput.get("IF_OUT");
//		tempHash000 = (HashMap) objRv;

			HashMap tmpHsMsg = (HashMap)tmpHashMap.get("E_RETURN");
			HashMap tmpHsData[] = (HashMap[])tmpHashMap.get("T_LOAD_INFO");
			RS_MSGTYP = Func.getTrim((String)tmpHsMsg.get("MSGTYP"));
			RS_MSGNUM = Func.getTrim((String)tmpHsMsg.get("MSGNUM"));
			RS_MSGTXT = Func.getTrim((String)tmpHsMsg.get("MSGTXT"));


			JSONObject json =  new JSONObject(tmpHashMap);

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //매핑되지 않는 항목은 Skip
			Map<String, Object> rsMap = mapper.readValue(String.format("%s", json), new TypeReference<LinkedHashMap<String,Object>>(){});

			resultMap.put("resultType", RS_MSGTYP);
			resultMap.put("resultCode", RS_MSGNUM);
			resultMap.put("resultMsg", RS_MSGTXT);
			resultMap.put("resultData",  rsMap.get("T_LOAD_INFO"));


		} catch(Exception ex) {
			LogUtil.errorLog(String.format("%s", ex));

			resultMap.put("resultType", "E");
			resultMap.put("resultCode", "01");
			resultMap.put("resultMsg", ex.toString());
			resultMap.put("resultData", null);
		}

		return resultMap;

	}

	/**
	 * 출하결과 입력
	 * @param vehicleNo
	 * @param fromDate
	 * @param toDate
	 * @param hashMaps
	 * @return
	 */
	public Map<String,Object> disLoadingTicketCreate(
			String vehicleNo, String fromDate, String toDate, String ZACTGB, String ZIFTIM, HashMap[] hashMaps
	) {

		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

		try {

			HashMap hsInput = new HashMap();	//
			HashMap hsTran = new HashMap();	// 실제 전송할 HashMap
			HashMap hsOutput = new HashMap();    // 전송 후 결과값을 전송받을 HashMap

			String RS_MSGTYP = "";
			String RS_MSGNUM = "";
			String RS_MSGTXT = "";

			hsInput.put("I_VEHICLE", vehicleNo);
			hsInput.put("I_ZFRDAT", fromDate);
			hsInput.put("I_ZTODAT", toDate);
			hsInput.put("I_ZACTGB", ZACTGB); // 1:전표출력, 2:출하결과입력(DIT_PLANT.RESULT_INPUT_YN = 'Y'인 플랜트)
			//
			hsInput.put("I_ZIFDAT", fromDate); // 인터페이스일자(?)
			hsInput.put("I_ZIFTIM", ZIFTIM); // 인터페이스시간(?)


//			HashMap[] loadingData = null;
//			loadingData = new HashMap[1];	// 실제 전송할 HashMap
//			loadingData[0] = new HashMap();
//			loadingData[0].put("SHNUMBER", hashMaps[0].get("SHNUMBER").toString()); // TD 선적 번호, PK
//			loadingData[0].put("VBELN", hashMaps[0].get("VBELN").toString()); // 납품문서번호, PK
//			loadingData[0].put("POSNN", hashMaps[0].get("POSNN").toString()); // 납품문서품목, PK
//			loadingData[0].put("WERKS", hashMaps[0].get("WERKS").toStrihashMapsng()); // 출하처(플랜트 Code)
//			loadingData[0].put("LOAD_EDDTA", hashMaps[0].get("LOAD_EDDTA").toString()); // 적재일자(전기일)	"적재 결과 입력된 경우 (12.03)"
//			loadingData[0].put("MATNR", hashMaps[0].get("MATNR").toString()); // 제품코드
//			loadingData[0].put("GRS_QTY", hashMaps[0].get("GRS_QTY").toString()); // 적재 Gross 수량("L" 통일)	"000000020000" 형식
//			loadingData[0].put("GRS_UOM", hashMaps[0].get("GRS_UOM").toString()); // 적재 Gross 수량단위	ISO 기준 UOM 사용
//			loadingData[0].put("NET_QTY", hashMaps[0].get("NET_QTY").toString()); // 적재 Net 수량("L15" 통일)	"000000020000" 형식
//			loadingData[0].put("NET_UOM", hashMaps[0].get("NET_UOM").toString()); // 적재 Net 수량단위	ISO 기준 UOM 사용
//			loadingData[0].put("TMP_VAL", hashMaps[0].get("TMP_VAL").toString()); // 적재 온도("CEL" 통일)	057.9+ or 057.9-
//			loadingData[0].put("TMP_UOM", hashMaps[0].get("TMP_UOM").toString()); // 적재 온도단위	ISO 기준 UOM 사용
//			loadingData[0].put("DEN_VAL", hashMaps[0].get("DEN_VAL").toString()); // 적재 밀도	0887.5


			hsInput.put("T_LOAD_INFO", hashMaps);

			hsTran.put("IF_IN", hsInput);

			LogUtil.infoLog(String.format("[EAI요청시작, DIS_Loading_Ticket_Create]-----------------------------------------------------"));
			EaiManager cmManager = new EaiManager();
			hsOutput = (HashMap)cmManager.executeService(eai_server_ip, "DIS_Loading_Ticket_Create.srv", "srcRoutine_Pub", hsTran);
			HashMap tmpHashMap = (HashMap)hsOutput.get("IF_OUT");
			LogUtil.infoLog(String.format("[EAI요청응답, DIS_Loading_Ticket_Create]-----------------------------------------------------"));
//		Object objRv = (HashMap)hsOutput.get("IF_OUT");
//		tempHash000 = (HashMap) objRv;

			HashMap tmpHsMsg = (HashMap)tmpHashMap.get("E_RETURN");
			HashMap tmpHsData[] = (HashMap[])tmpHashMap.get("T_SHIPMENT");
			RS_MSGTYP = Func.getTrim((String)tmpHsMsg.get("MSGTYP"));
			RS_MSGNUM = Func.getTrim((String)tmpHsMsg.get("MSGNUM"));
			RS_MSGTXT = Func.getTrim((String)tmpHsMsg.get("MSGTXT"));


			JSONObject json =  new JSONObject(tmpHashMap);

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //매핑되지 않는 항목은 Skip
			Map<String, Object> rsMap = mapper.readValue(String.format("%s", json), new TypeReference<LinkedHashMap<String,Object>>(){});

			resultMap.put("resultType", RS_MSGTYP);
			resultMap.put("resultCode", RS_MSGNUM);
			resultMap.put("resultMsg", RS_MSGTXT);
			resultMap.put("resultData",  rsMap.get("T_SHIPMENT"));


		} catch(Exception ex) {
			LogUtil.errorLog(String.format("%s", ex));

			resultMap.put("resultType", "E");
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", ex.toString());
			resultMap.put("resultData", null);
		}

		return resultMap;

	}

	/**
	 * 출하요청
	 * @param ifDate
	 * @param ifTime
	 * @param vehicleNo
	 * @param driverCode
	 * @param hashMaps
	 * @return
	 */
	public Map<String,Object> disShippingCreate(
			String ifDate, String ifTime, String vehicleNo, String driverCode, HashMap[] hashMaps
	) {

		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

		try {

			HashMap hsInput = new HashMap();	//
			HashMap hsTran = new HashMap();	// 실제 전송할 HashMap
			HashMap hsOutput = new HashMap();    // 전송 후 결과값을 전송받을 HashMap

			String RS_MSGTYP = "";
			String RS_MSGNUM = "";
			String RS_MSGTXT = "";

			hsInput.put("I_ZIFDAT", ifDate); // 인터페이스 일자
			hsInput.put("I_ZIFTIM", ifTime); // 인터페이스 시간
			hsInput.put("I_VEHICLE", vehicleNo); // 차량 Code
			hsInput.put("I_DRIVERCODE", driverCode); // 운전기사 Code


//			HashMap[] loadingData = null;
//			loadingData = new HashMap[1];	// 실제 전송할 HashMap
//			loadingData[0] = new HashMap();
//
//			loadingData[0].put("SHNUMBER", hashMaps[0].get("SHIPMENT_NO").toString()); // TD 선적 번호, PK
//			loadingData[0].put("VBELN", hashMaps[0].get("VBELN").toString()); // 납품문서번호, PK
//			loadingData[0].put("POSNN", hashMaps[0].get("POSNN").toString()); // 납품문서품목, PK
//			loadingData[0].put("WERKS", hashMaps[0].get("WERKS").toString()); // 출하처(플랜트 Code)
//			loadingData[0].put("MATNR", hashMaps[0].get("MATNR").toString()); // 제품코드
//			loadingData[0].put("SCH_QTY", hashMaps[0].get("SCH_QTY").toString()); // 배차수량
//			loadingData[0].put("SCH_UOM", hashMaps[0].get("SCH_UOM").toString()); // 배차단위
//			loadingData[0].put("CARDNO01", hashMaps[0].get("LOADING_KEY_NO1").toString()); // 출하 Card 번호
//			loadingData[0].put("CARDNO02", hashMaps[0].get("LOADING_KEY_NO2").toString()); // 출하 Card 번호
//			loadingData[0].put("ZSEALNO", hashMaps[0].get("SEALING_NO").toString()); // 봉인키번호

			hsInput.put("T_REQUEST", hashMaps);
			hsTran.put("IF_IN", hsInput);

			LogUtil.infoLog(String.format("[EAI요청시작, DIS_Shipping_Create]-----------------------------------------------------"));
			EaiManager cmManager = new EaiManager();
			hsOutput = (HashMap)cmManager.executeService(eai_server_ip, "DIS_Shipping_Create.srv", "srcRoutine_Pub", hsTran);
			HashMap tmpHashMap = (HashMap)hsOutput.get("IF_OUT");
			LogUtil.infoLog(String.format("[EAI요청응답, DIS_Shipping_Create]-----------------------------------------------------"));
//		Object objRv = (HashMap)hsOutput.get("IF_OUT");
//		tempHash000 = (HashMap) objRv;

			HashMap tmpHsMsg = (HashMap)tmpHashMap.get("E_RETURN");
			HashMap tmpHsData[] = (HashMap[])tmpHashMap.get("T_RESULT");
			RS_MSGTYP = Func.getTrim((String)tmpHsMsg.get("MSGTYP"));
			RS_MSGNUM = Func.getTrim((String)tmpHsMsg.get("MSGNUM"));
			RS_MSGTXT = Func.getTrim((String)tmpHsMsg.get("MSGTXT"));


			JSONObject json =  new JSONObject(tmpHashMap);

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //매핑되지 않는 항목은 Skip
			Map<String, Object> rsMap = mapper.readValue(String.format("%s", json), new TypeReference<LinkedHashMap<String,Object>>(){});

			resultMap.put("resultType", RS_MSGTYP);
			resultMap.put("resultCode", RS_MSGNUM);
			resultMap.put("resultMsg", RS_MSGTXT);
			resultMap.put("resultData",  rsMap.get("T_SHIPMENT"));


		} catch(Exception ex) {
			LogUtil.errorLog(String.format("%s", ex));

			resultMap.put("resultType", "E");
			resultMap.put("resultCode", "01");
			resultMap.put("resultMsg", ex.toString());
			resultMap.put("resultData", null);
		}

		return resultMap;

	}

	/**
	 * 출하요청(무인단말기)
	 * @param ifDate
	 * @param ifTime
	 * @param vehicleNo
	 * @param driverCode
	 * @param hashMaps
	 * @return
	 */
	public Map<String,Object> disShippingCreateRan(
			String ifDate, String ifTime, String vehicleNo, String driverCode, HashMap[] hashMaps
	) {

		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

		try {

			HashMap hsInput = new HashMap();	//
			HashMap hsTran = new HashMap();	// 실제 전송할 HashMap
			HashMap hsOutput = new HashMap();    // 전송 후 결과값을 전송받을 HashMap

			String RS_MSGTYP = "";
			String RS_MSGNUM = "";
			String RS_MSGTXT = "";

			hsInput.put("I_ZIFDAT", ifDate); // 인터페이스 일자
			hsInput.put("I_ZIFTIM", ifTime); // 인터페이스 시간
			hsInput.put("I_VEHICLE", vehicleNo); // 차량 Code
			hsInput.put("I_DRIVERCODE", driverCode); // 운전기사 Code


//			HashMap[] loadingData = null;
//			loadingData = new HashMap[1];	// 실제 전송할 HashMap
//			loadingData[0] = new HashMap();
//
//			loadingData[0].put("SHNUMBER", hashMaps[0].get("SHIPMENT_NO").toString()); // TD 선적 번호, PK
//			loadingData[0].put("VBELN", hashMaps[0].get("VBELN").toString()); // 납품문서번호, PK
//			loadingData[0].put("POSNN", hashMaps[0].get("POSNN").toString()); // 납품문서품목, PK
//			loadingData[0].put("WERKS", hashMaps[0].get("WERKS").toString()); // 출하처(플랜트 Code)
//			loadingData[0].put("MATNR", hashMaps[0].get("MATNR").toString()); // 제품코드
//			loadingData[0].put("SCH_QTY", hashMaps[0].get("SCH_QTY").toString()); // 배차수량
//			loadingData[0].put("SCH_UOM", hashMaps[0].get("SCH_UOM").toString()); // 배차단위
//			loadingData[0].put("CARDNO01", hashMaps[0].get("LOADING_KEY_NO1").toString()); // 출하 Card 번호
//			loadingData[0].put("CARDNO02", hashMaps[0].get("LOADING_KEY_NO2").toString()); // 출하 Card 번호
//			loadingData[0].put("ZSEALNO", hashMaps[0].get("SEALING_NO").toString()); // 봉인키번호

			hsInput.put("T_REQUEST", hashMaps);
			hsTran.put("IF_IN", hsInput);

			LogUtil.infoLog(String.format("[EAI요청시작, DIS_Shipping_Create_Ran]-----------------------------------------------------"));
			EaiManager cmManager = new EaiManager();
			hsOutput = (HashMap)cmManager.executeService(eai_server_ip, "DIS_Shipping_Create_Ran.srv", "srcRoutine_Pub", hsTran);
			HashMap tmpHashMap = (HashMap)hsOutput.get("IF_OUT");
			LogUtil.infoLog(String.format("[EAI요청응답, DIS_Shipping_Create_Ran]-----------------------------------------------------"));
//		Object objRv = (HashMap)hsOutput.get("IF_OUT");
//		tempHash000 = (HashMap) objRv;

			HashMap tmpHsMsg = (HashMap)tmpHashMap.get("E_RETURN");
			HashMap tmpHsData[] = (HashMap[])tmpHashMap.get("T_RESULT");
			RS_MSGTYP = Func.getTrim((String)tmpHsMsg.get("MSGTYP"));
			RS_MSGNUM = Func.getTrim((String)tmpHsMsg.get("MSGNUM"));
			RS_MSGTXT = Func.getTrim((String)tmpHsMsg.get("MSGTXT"));


			JSONObject json =  new JSONObject(tmpHashMap);

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //매핑되지 않는 항목은 Skip
			Map<String, Object> rsMap = mapper.readValue(String.format("%s", json), new TypeReference<LinkedHashMap<String,Object>>(){});

			resultMap.put("resultType", RS_MSGTYP);
			resultMap.put("resultCode", RS_MSGNUM);
			resultMap.put("resultMsg", RS_MSGTXT);
			resultMap.put("resultData",  rsMap.get("T_SHIPMENT"));

			LogUtil.infoLog(String.format("RS_MSGNUM => %s", RS_MSGNUM));
			LogUtil.infoLog(String.format("RS_MSGTXT => %s", RS_MSGTXT));

		} catch(Exception ex) {
			LogUtil.errorLog(String.format("%s", ex));

			resultMap.put("resultType", "E");
			resultMap.put("resultCode", "01");
			resultMap.put("resultMsg", ex.toString());
			resultMap.put("resultData", null);
		}

		return resultMap;

	}

	/**
	 * 부피환산요청
	 * @param MATNR
	 * @param WERKS
	 * @param GRS_QTY
	 * @param GRS_UOM
	 * @param TMP_VAL
	 * @param TMP_UOM
	 * @param DEN_VAL
	 * @param NET_QTY
	 * @param NET_UOM
	 * @return
	 */
	public Map<String,Object> disVolumnExchangeCreate(
			String MATNR, String WERKS, String GRS_QTY, String GRS_UOM,
			String TMP_VAL, String TMP_UOM, String DEN_VAL, String NET_QTY, String NET_UOM
	) {


		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

		try {

			HashMap hsInput = new HashMap();	//
			HashMap hsTran = new HashMap();	// 실제 전송할 HashMap
			HashMap hsOutput = new HashMap();    // 전송 후 결과값을 전송받을 HashMap

			String RS_MSGTYP = "";
			String RS_MSGNUM = "";
			String RS_MSGTXT = "";

			hsInput.put("I_MATNR", MATNR);	// 자재번호
			hsInput.put("I_WERKS", WERKS);	// 출하처(플랜트)
			hsInput.put("I_GRS_QTY", GRS_QTY);	// Gross 수량("L" 통일)
			hsInput.put("I_GRS_UOM", GRS_UOM);	// Gross 수량단위
			hsInput.put("I_TMP_VAL", TMP_VAL);	// 온도("CEL" 통일)
			hsInput.put("I_TMP_UOM", TMP_UOM);	// 온도단위
			hsInput.put("I_DEN_VAL", DEN_VAL);	// 밀도
			hsInput.put("I_NET_QTY", NET_QTY);	// NET수량
			hsInput.put("I_NET_UOM", NET_UOM);	// NET수량단위

			LogUtil.infoLog(String.format("[EAI요청시작, DIS_Volumn_Exchange_Create]-----------------------------------------------------"));
			EaiManager cmManager = new EaiManager();
			hsOutput = (HashMap)cmManager.executeService(eai_server_ip, "DIS_Volumn_Exchange_Create.srv", "srcRoutine_Pub", hsTran);
			HashMap tmpHashMap = (HashMap)hsOutput.get("IF_OUT");
			LogUtil.infoLog(String.format("[EAI요청응답, DIS_Volumn_Exchange_Create]-----------------------------------------------------"));
//		Object objRv = (HashMap)hsOutput.get("IF_OUT");
//		tempHash000 = (HashMap) objRv;

			HashMap tmpHsMsg = (HashMap)tmpHashMap.get("E_RETURN");
			HashMap tmpHsData[] = (HashMap[])tmpHashMap.get("T_SHIPMENT");
			RS_MSGTYP = Func.getTrim((String)tmpHsMsg.get("MSGTYP"));
			RS_MSGNUM = Func.getTrim((String)tmpHsMsg.get("MSGNUM"));
			RS_MSGTXT = Func.getTrim((String)tmpHsMsg.get("MSGTXT"));


			JSONObject json =  new JSONObject(tmpHashMap);

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //매핑되지 않는 항목은 Skip
			Map<String, Object> rsMap = mapper.readValue(String.format("%s", json), new TypeReference<LinkedHashMap<String,Object>>(){});

			resultMap.put("resultCode", RS_MSGNUM);
			resultMap.put("resultMsg", RS_MSGTXT);
			resultMap.put("resultData",  rsMap.get("T_SHIPMENT"));


		} catch(Exception ex) {
			LogUtil.errorLog(String.format("%s", ex));

			resultMap.put("resultCode", "01");
			resultMap.put("resultMsg", ex.toString());
			resultMap.put("resultData", null);
		}

		return resultMap;

	}

	/**
	 * 전자출하전표 조회(신규)
	 * @param vehicleNo
	 * @param fromDate
	 * @param toDate
	 * @param shnnumber
	 * @return
	 */
	public Map<String,Object> disLoadingTicketDisplay(
			String vehicleNo, String fromDate, String toDate, String shnnumber
	) {

		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

		try {

			HashMap hsInput = new HashMap();	//
			HashMap hsTran = new HashMap();	// 실제 전송할 HashMap
			HashMap hsOutput = new HashMap();    // 전송 후 결과값을 전송받을 HashMap

			String RS_MSGTYP = "";
			String RS_MSGNUM = "";
			String RS_MSGTXT = "";

			hsInput.put("I_VEHICLE", vehicleNo);
			hsInput.put("I_ZFRDAT", fromDate);
			hsInput.put("I_ZTODAT", toDate);
			hsInput.put("I_SHNNUMBER", shnnumber);

			LogUtil.infoLog(String.format("[EAI요청시작, DIS_Loading_Ticket_Display]-----------------------------------------------------"));
			EaiManager cmManager = new EaiManager();
			hsOutput = (HashMap)cmManager.executeService(eai_server_ip, "DIS_Loading_Ticket_Display.srv", "srcRoutine_Pub", hsTran);
			HashMap tmpHashMap = (HashMap)hsOutput.get("IF_OUT");
			LogUtil.infoLog(String.format("[EAI요청응답, DIS_Loading_Ticket_Display]-----------------------------------------------------"));
//		Object objRv = (HashMap)hsOutput.get("IF_OUT");
//		tempHash000 = (HashMap) objRv;

			HashMap tmpHsMsg = (HashMap)tmpHashMap.get("E_RETURN");
			HashMap tmpHsData[] = (HashMap[])tmpHashMap.get("T_SHIPMENT");
			RS_MSGTYP = Func.getTrim((String)tmpHsMsg.get("MSGTYP"));
			RS_MSGNUM = Func.getTrim((String)tmpHsMsg.get("MSGNUM"));
			RS_MSGTXT = Func.getTrim((String)tmpHsMsg.get("MSGTXT"));


			JSONObject json =  new JSONObject(tmpHashMap);

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //매핑되지 않는 항목은 Skip
			Map<String, Object> rsMap = mapper.readValue(String.format("%s", json), new TypeReference<LinkedHashMap<String,Object>>(){});

			resultMap.put("resultType", RS_MSGTYP);
			resultMap.put("resultCode", RS_MSGNUM);
			resultMap.put("resultMsg", RS_MSGTXT);
			resultMap.put("resultData",  rsMap.get("T_SHIPMENT"));


		} catch(Exception ex) {
			LogUtil.errorLog(String.format("%s", ex));

			resultMap.put("resultType", "E");
			resultMap.put("resultCode", "01");
			resultMap.put("resultMsg", ex.toString());
			resultMap.put("resultData", null);
		}

		return resultMap;

	}


	/**
	 * 전자, 출하전표 조회
	 * @param plantCd
	 * @param vehicleNo
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public Map<String,Object> loadingPrintCreate(String shnumber, String vbeln, String posnr) {

		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

		try {

			HashMap hsInput = new HashMap();	//
			HashMap hsTran = new HashMap();	// 실제 전송할 HashMap
			HashMap hsOutput = new HashMap();    // 전송 후 결과값을 전송받을 HashMap

			String RS_MSGTYP = "";
			String RS_MSGNUM = "";
			String RS_MSGTXT = "";

			hsInput.put("SHNUMBER", shnumber);
			hsInput.put("VBELN", vbeln);
			hsInput.put("POSNR", posnr);


//			HashMap[] hsTran1 = null;
//			hsTran1 = new HashMap[1];	// 실제 전송할 HashMap
//			hsTran1[0] = new HashMap();
//			hsTran1[0].put("WERKS", plantCd);
//
//
//			hsInput.put("T_PLANT", hsTran1);
			hsTran.put("IF_IN", hsInput);

			LogUtil.infoLog(String.format("[EAI요청시작, DIS_Dispatch_Display]-----------------------------------------------------"));
			EaiManager cmManager = new EaiManager();
			hsOutput = (HashMap)cmManager.executeService(eai_server_ip, "TMS_TO_SAP_Loading_Print_Create.srv", "srcRoutine_Pub", hsTran);
			HashMap tmpHashMap = (HashMap)hsOutput.get("IF_OUT");
			LogUtil.infoLog(String.format("[EAI요청응답, DIS_Dispatch_Display]-----------------------------------------------------"));
//		Object objRv = (HashMap)hsOutput.get("IF_OUT");
//		tempHash000 = (HashMap) objRv;

			HashMap tmpHsMsg = (HashMap)tmpHashMap.get("E_RETURN");
			HashMap tmpHsData[] = (HashMap[])tmpHashMap.get("T_DATA");
//			RS_MSGTYP = Func.getTrim((String)tmpHsMsg.get("MSGTYP"));
//			RS_MSGNUM = Func.getTrim((String)tmpHsMsg.get("MSGNUM"));
//			RS_MSGTXT = Func.getTrim((String)tmpHsMsg.get("MSGTXT"));


			JSONObject json =  new JSONObject(tmpHsData[0]);

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //매핑되지 않는 항목은 Skip
			Map<String, Object> rsMap = mapper.readValue(String.format("%s", json), new TypeReference<LinkedHashMap<String,Object>>(){});

			resultMap.put("resultType", RS_MSGTYP);
			resultMap.put("resultCode", RS_MSGNUM);
			resultMap.put("resultMsg", RS_MSGTXT);
			resultMap.put("resultData",  rsMap);


		} catch(Exception ex) {
			LogUtil.errorLog(String.format("%s", ex));

			resultMap.put("resultType", "E");
			resultMap.put("resultCode", "01");
			resultMap.put("resultMsg", ex.toString());
			resultMap.put("resultData", null);
		}

		return resultMap;

	}
}
