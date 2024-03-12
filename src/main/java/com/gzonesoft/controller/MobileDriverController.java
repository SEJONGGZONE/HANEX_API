package com.gzonesoft.controller;

import com.gzonesoft.domain.DispatchDisplayDto;
import com.gzonesoft.service.EAIService;
import com.gzonesoft.service.KioskService;
import com.gzonesoft.service.MobileDriverService;
import com.gzonesoft.utils.LogUtil;
import com.gzonesoft.utils.SysUtils;
import com.google.gson.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * http://localhost:9032/swagger-ui.html
 * @author ships
 *
 */
@RequestMapping("/api/driver")
@RestController
public class MobileDriverController {
	@Autowired private MobileDriverService mobileDriverService;
	@Autowired private EAIService eaiService;
	@Autowired private KioskService kioskService;

	/**
	 *
	 * @param api_key
	 * @param json_body
	 * @return
	 */
	@ApiOperation(value="업데이트체크\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "1Bb6wI6DnVL8S8LQAbvAO+pSnKXCf7HwQ7GEn+FnrJk=", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"I_APP_TYPE\":\"10\",\r\n" +
							"        	\"I_OS_TYPE\":\"10\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/appCheckDriver")
	public @ResponseBody Map<String, Object> appCheckDriver(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){
		return mobileDriverService.appCheckDriver(json_body);
	}


	/**
	 *
	 * @param api_key
	 * @param json_body
	 * @return
	 */
	@ApiOperation(value="기사정보 조회(현장테스트용)\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"I_VHCL_PCL_CD\":\"85-9850\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/driverInfoSel")
	public @ResponseBody Map<String, Object> driverInfoSel(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body) throws NoSuchAlgorithmException {

		// 암호화하여 다시넣는다..
		String orgString = json_body.get("I_PASS").toString();
		String encString = SysUtils.encryptSHA256(orgString);
		json_body.put("I_PASS", encString);

		return mobileDriverService.driverInfoSel(json_body);
	}

	/**
	 *
	 * @param api_key
	 * @param json_body
	 * @return
	 */
	@ApiOperation(value="로그인요청\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"I_ID\":\"8465\",\r\n" +
							"        	\"I_PASS\":\"8465\",\r\n" +
							"        	\"I_MIN_NO\":\"1075905736\",\r\n" +
							"        	\"I_APP_VERSION\":\"\",\r\n" +
							"        	\"I_OS_VERSION\":\"\",\r\n" +
							"        	\"I_UUID\":\"\",\r\n" +
							"        	\"I_DEVICE_MODEL\":\"\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/loginReq")
	public @ResponseBody Map<String, Object> loginReq(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body) throws NoSuchAlgorithmException {

		// 암호화하여 다시넣는다..
		String orgString = json_body.get("I_PASS").toString();
		if (orgString.length() < 64) { // 관리자의 로그인시도가 아니라면.. 암호화한다. 관리자의 로그인시도라면 그대로 보내준다.
			String encString = SysUtils.encryptSHA256(orgString);
			json_body.put("I_PASS", encString);
		}
		return mobileDriverService.loginReq(json_body);
	}

	/**
	 *
	 * @param api_key
	 * @param json_body
	 * @return
	 */
	@ApiOperation(value="차량선택정보 저장\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"I_DEVICE_CD\":\"0000000079\",\r\n" +
							"        	\"I_VEHICLE_CD\":\"80-1588\",\r\n" +
							"        	\"I_MIN_NO\":\"1088280995\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/selectVehicleSav")
	public @ResponseBody Map<String, Object> selectVehicleSav(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){
		return mobileDriverService.selectVehicleSav(json_body);
	}


	/**
	 *
	 * @param api_key
	 * @param json_body
	 * @return
	 */
	@ApiOperation(value="코드조회\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"I_CODE_TYPE\":\"\",\r\n" +
							"        	\"I_PARAM_01\":\"\",\r\n" +
							"        	\"I_PARAM_02\":\"\",\r\n" +
							"        	\"I_PARAM_03\":\"\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/codeSel")
	public @ResponseBody Map<String, Object> codeSel(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){
		return mobileDriverService.codeSel(json_body);
	}

	/**
	 *
	 * @param api_key
	 * @param json_body
	 * @return
	 */
	@ApiOperation(value="최종작업상태조회\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"I_DISPATCH_DATE\":\"20220303\",\r\n" +
							"        	\"I_DRV_CD\":\"8465\",\r\n" +
							"        	\"I_VEHICLE_CD\":\"80-1588\",\r\n" +
							"        	\"I_MIN_NO\":\"1075905736\",\r\n" +
							"        	\"I_LAT\":\"\",\r\n" +
							"        	\"I_LON\":\"\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/lastWorkSel")
	public @ResponseBody Map<String, Object> lastWorkSel(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){
		return mobileDriverService.lastWorkSel(json_body);
	}

	/**
	 *
	 * @param api_key
	 * @param json_body
	 * @return
	 */
	@ApiOperation(value="출근하기\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"I_DRV_CD\":\"8465\",\r\n" +
							"        	\"I_VEHICLE_CD\":\"80-1588\",\r\n" +
							"        	\"I_MIN_NO\":\"1075905736\",\r\n" +
							"        	\"I_LAT\":\"\",\r\n" +
							"        	\"I_LON\":\"\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/todayStart")
	public @ResponseBody Map<String, Object> todayStart(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){
		return mobileDriverService.todayStart(json_body);
	}

	/**
	 *
	 * @param api_key
	 * @param json_body
	 * @return
	 */
	@ApiOperation(value="퇴근하기\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"I_DRV_CD\":\"8465\",\r\n" +
							"        	\"I_VEHICLE_CD\":\"80-1588\",\r\n" +
							"        	\"I_MIN_NO\":\"1075905736\",\r\n" +
							"        	\"I_LAT\":\"\",\r\n" +
							"        	\"I_LON\":\"\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/todayFinish")
	public @ResponseBody Map<String, Object> todayFinish(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){
		return mobileDriverService.todayFinish(json_body);
	}

	/**
	 *
	 * @param api_key
	 * @param json_body
	 * @return
	 */
	@ApiOperation(value="배차목록 조회\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"I_DISPATCH_DT\":\"20220209\",\r\n" +
							"        	\"I_DRV_CD\":\"8465\",\r\n" +
							"        	\"I_VEHICLE_CD\":\"80-1588\",\r\n" +
							"        	\"I_MIN_NO\":\"1075905736\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/dispatchListSel")
	public @ResponseBody Map<String, Object> dispatchListSel(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){
		return mobileDriverService.dispatchListSel(json_body);
	}

	/**
	 *
	 * @param api_key
	 * @param json_body
	 * @return
	 */
	@ApiOperation(value="배차목록 조회(With.EAI)\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"SEARCH_TYPE\":\"10\",\r\n" +
							"        	\"VEHICLE\":\"85-9850\",\r\n" +
							"        	\"ZFRDAT\":\"20200213\",\r\n" +
							"        	\"ZTODAT\":\"20200213\",\r\n" +
							"        	\"DRV_CD\":\"11786\",\r\n" +
							"        	\"TAB_NO\":\"01075935736\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/dispatchListSelWithEai")
	public @ResponseBody Map<String, Object> dispatchListSelWithEai(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){

		// -----------------------------------------------------------------------------
		// 1.EAI-배차데이타 가져오기
		// -----------------------------------------------------------------------------
		String drvCd = json_body.get("DRV_CD").toString();
		String tabNo = json_body.get("TAB_NO").toString();

		String searchType = json_body.get("SEARCH_TYPE").toString();
		String vehicleNo = json_body.get("VEHICLE").toString();
		String fromDate = json_body.get("ZFRDAT").toString();
		String toDate = json_body.get("ZTODAT").toString();


		// -----------------------------------------------------------------------------
		// 2.TMS-배차데이타 가져오기
		// -----------------------------------------------------------------------------
		LinkedHashMap<String, Object> jsonBodyForTms = new LinkedHashMap<String, Object>();

		jsonBodyForTms.put("I_SEARCH_TYPE", searchType);
		jsonBodyForTms.put("I_DISPATCH_DT", fromDate);
		jsonBodyForTms.put("I_DRV_CD", drvCd);
		jsonBodyForTms.put("I_VEHICLE_CD", vehicleNo);
		jsonBodyForTms.put("I_TAB_NO", tabNo);

		Map<String, Object> tmsResult = new LinkedHashMap<String, Object>();
		tmsResult = mobileDriverService.dispatchListSel(jsonBodyForTms);

		// 데이타 추출..
		JSONObject jsonObject = new JSONObject(tmsResult);
		String jsonData = jsonObject.toString();
		JsonElement jelement = new JsonParser().parse(jsonData);
		JsonObject jObject = jelement.getAsJsonObject();
		String resultCode = jObject.get("resultCode").getAsString();
		String resultMsg = jObject.get("resultMsg").getAsString();
		// 배차내역 Loop
		List<String> sourceList = new ArrayList<String>();

		if (tmsResult.get("resultData") != null) {
			JsonArray jarrayRecordSetTms = jObject.get("resultData").getAsJsonArray();
			for (int idx = 0; idx < jarrayRecordSetTms.size(); ++idx) {
				JsonObject jobjectStoreTms = jarrayRecordSetTms.get(idx).getAsJsonObject();


//				DispatchDisplayDto dto = new DispatchDisplayDto();
//				Gson gson = new Gson();
//				dto = gson.fromJson(jobjectStoreTms, DispatchDisplayDto.class);

//				Map<String, Object> eaiResult = new LinkedHashMap<String, Object>();
				String sourceCd = jobjectStoreTms.get("SOURCE_CD").getAsString();
				// 중복출하지 제거..
				boolean bFind = false;
				for(String data:sourceList) {
					if (!bFind) {
						if (sourceCd.equals(data)) {
							bFind = true;
						}
					}
				}
				// 출하지정보 추출
				if (!bFind)
					sourceList.add(sourceCd);
			}
		}

		List<Map<String, Object>> eaiData = new ArrayList<Map<String, Object>>();
		List<DispatchDisplayDto> eaiDispatchData = new ArrayList<DispatchDisplayDto>();
		for (int idx=0; idx<sourceList.size(); idx++) {
			String sourceCd = sourceList.get(idx).toString();
			// EAI 호출..
			Map<String, Object> result = new LinkedHashMap<String, Object>();
			result =  eaiService.disDispatchDisplay(sourceCd, vehicleNo, fromDate, toDate);

			// 데이타 추출..
			JSONObject jsonObject1 = new JSONObject(result);
			String jsonData1 = jsonObject1.toString();
			JsonElement jelement1 = new JsonParser().parse(jsonData1);
			JsonObject jObject1 = jelement1.getAsJsonObject();
			String resultCode1 = jObject1.get("resultCode").getAsString();
			String resultMsg1 = jObject1.get("resultMsg").getAsString();
			if (result.get("resultData") != null) {
				JsonArray jarrayRecordSet1 = jObject1.get("resultData").getAsJsonArray();
				for (int idx1 = 0; idx1 < jarrayRecordSet1.size(); ++idx1) {
					JsonObject jobjectStore1 = jarrayRecordSet1.get(idx1).getAsJsonObject();
					Gson gson1 = new Gson();
					DispatchDisplayDto dto1 = new DispatchDisplayDto();
					dto1 = gson1.fromJson(jobjectStore1, DispatchDisplayDto.class);
					eaiDispatchData.add(dto1);
				}
			}
		}
//		// EAI 호출..
//		// 정상테스트시, 주석해제...
//		//eaiResult =  eaiService.disDispatchDisplay(plantCd, vehicleNo, fromDate, toDate);
//		// 통합테스트용 임시데이타..
//		eaiResult =  eaiService.disDispatchDisplay("2120", "85-9850", "20220817", "20220817");
//
//		// 데이타 추출..
//		JSONObject jsonObject = new JSONObject(eaiResult);
//		String jsonData = jsonObject.toString();
//		JsonElement jelement = new JsonParser().parse(jsonData);
//		JsonObject jObject = jelement.getAsJsonObject();
//		String resultCode = jObject.get("resultCode").getAsString();
//		String resultMsg = jObject.get("resultMsg").getAsString();
//
//		// EAI 배차데이타
//		List<DispatchDisplayDto> eaiDispatchData = new ArrayList<DispatchDisplayDto>();
//
//		if (eaiResult.get("resultData") != null) {
//
//			JsonArray jarrayRecordSet = jObject.get("resultData").getAsJsonArray();
//
//
//			// 배차내역 Loop
//			for (int idx = 0; idx < jarrayRecordSet.size(); ++idx) {
//				JsonObject jobjectStore = jarrayRecordSet.get(idx).getAsJsonObject();
//				System.out.println(String.format("[%d]%s", idx, jobjectStore.toString()));
//
//				DispatchDisplayDto dto = new DispatchDisplayDto();
//				Gson gson = new Gson();
//				dto = gson.fromJson(jobjectStore, DispatchDisplayDto.class);
//
//				eaiDispatchData.add(dto);
//			}
//		}
//
//
		// -----------------------------------------------------------------------------
		// 3.TMS-배차데이타에 EAI데이타 추가하기..
		// -----------------------------------------------------------------------------
		tmsResult.put("eaiResult", eaiDispatchData);

		// -----------------------------------------------------------------------------
		// 4.결과리턴(TMS+EAI)
		// -----------------------------------------------------------------------------
		return tmsResult;
	}



	/**
	 * @param api_key
	 * *
	 @param json_body
	  * @return
	 */
	@ApiOperation(value="자차,배차내역 조회\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"I_PLANT_LIST\":\"\",\r\n" +
							"        	\"I_SHIPMENT_DT\":\"20220911\",\r\n" +
							"        	\"I_DRV_CD\":\"10456\",\r\n" +
							"        	\"I_VEHICLE_CD\":\"81-8208\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/selfDispatchListSel")
	public @ResponseBody
	Map<String, Object> selfDispatchListSel(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){

		// -----------------------------------------------------------------------------
		// 1.EAI 호출(기본배차자료 조회)
		// -----------------------------------------------------------------------------
		String plantList = json_body.get("I_PLANT_LIST").toString();
		String vehicleNo = json_body.get("I_VEHICLE_CD").toString();
		String fromDate = json_body.get("I_SHIPMENT_DT").toString();
		String toDate = json_body.get("I_SHIPMENT_DT").toString();

		Map<String, Object> eaiResult = new LinkedHashMap<String, Object>();
		eaiResult = eaiService.disDispatchDisplay(plantList, vehicleNo, fromDate, toDate);

		// -----------------------------------------------------------------------------
		// 2.출하요청 데이타 가져오기
		// -----------------------------------------------------------------------------
		LinkedHashMap<String, Object> jsonBodyForTms = new LinkedHashMap<String, Object>();

		jsonBodyForTms.put("I_SHIPMENT_DT", fromDate);
		jsonBodyForTms.put("I_VEHICLE", vehicleNo);

		Map<String, Object> shipmentRequestResult = new LinkedHashMap<String, Object>();
		shipmentRequestResult = kioskService.shippingRequestSel(jsonBodyForTms);

		// -----------------------------------------------------------------------------
		// 3.EAI결과 셋에 TMS결과 추가하기
		// -----------------------------------------------------------------------------
		eaiResult.put("shipmentRequestResult", shipmentRequestResult);


		return eaiResult;
	}


	/**
	 *
	 * @param api_key
	 * @param json_body
	 * @return
	 */
	@ApiOperation(value="배차선택\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"I_DISPATCH_NO\":\"D000150760\",\r\n" +
							"        	\"I_DELI_NO\":\"\",\r\n" +
							"        	\"I_ROUTE_SEQ\":\"\",\r\n" +
							"        	\"I_STOP_SEQ\":\"\",\r\n" +
							"        	\"I_DRV_CD\":\"8465\",\r\n" +
							"        	\"I_VEHICLE_CD\":\"80-1588\",\r\n" +
							"        	\"I_MIN_NO\":\"1075905736\",\r\n" +
							"        	\"I_LAT\":\"\",\r\n" +
							"        	\"I_LON\":\"\",\r\n" +
							"        	\"I_REPORT_DATE\":\"\",\r\n" +
							"        	\"I_SEAL_START\":\"\",\r\n" +
							"        	\"I_SEAL_END\":\"\",\r\n" +
							"        	\"I_SHIP_KEY\":\"\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/dispatchSelectSav")
	public @ResponseBody Map<String, Object> dispatchSelectSav(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){
		return mobileDriverService.dispatchSelectSav(json_body);
	}

	/**
	 * 출하요청(EAI이전)
	 * @param api_key
	 * @param json_body
	 * @return
	 */
	@ApiOperation(value="출하요청\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"I_DISPATCH_NO\":\"D000150760\",\r\n" +
							"        	\"I_DELI_NO\":\"\",\r\n" +
							"        	\"I_ROUTE_SEQ\":\"\",\r\n" +
							"        	\"I_STOP_SEQ\":\"\",\r\n" +
							"        	\"I_DRV_CD\":\"8465\",\r\n" +
							"        	\"I_VEHICLE_CD\":\"80-1588\",\r\n" +
							"        	\"I_MIN_NO\":\"1075905736\",\r\n" +
							"        	\"I_LAT\":\"\",\r\n" +
							"        	\"I_LON\":\"\",\r\n" +
							"        	\"I_REPORT_DATE\":\"\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/shippingCreateSav")
	public @ResponseBody Map<String, Object> shippingCreateSav(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){
		return mobileDriverService.shippingCreateSav(json_body);
	}

	/**
	 *
	 * {
	 *   "ZIFDAT": "20220817",
	 *   "ZIFTIM": "153448",
	 *   "VEHICLE": "85-9850",
	 *   "DRIVERCODE": "11786",
	 *   "SHNUMBER": "8805102",
	 *   "VBELN": "305531347",
	 *   "POSNN": "10",
	 *   "WERKS": "2120",
	 *   "MATNR": "10270",
	 *   "SCH_QTY": "8000",
	 *   "SCH_UOM": "L",
	 *   "CARDNO01": "",
	 *   "CARDNO02": "",
	 *   "ZSEALNO": "",
	 *   "I_DISPATCH_NO": "D000453803",
	 *   "I_DELI_NO": "A08170000006109R050000009",
	 *   "I_ROUTE_SEQ": "1",
	 *   "I_STOP_SEQ": "1",
	 *   "I_DRV_CD": "11786",
	 *   "I_VEHICLE_CD": "85-9850",
	 *   "I_MIN_NO": "1075935736",
	 *   "I_LAT": "37.5571885",
	 *   "I_LON": "126.9737383",
	 *   "I_REPORT_DATE": ""
	 * }
	 *
	 * @param api_key
	 * @param json_body
	 * @return
	 */
	@ApiOperation(value="출하요청(With,EAI)\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"ZIFDAT\":\"20220817\",\r\n" +
							"        	\"ZIFTIM\":\"153448\",\r\n" +
							"        	\"VEHICLE\":\"85-9850\",\r\n" +
							"        	\"DRIVERCODE\":\"11786\",\r\n" +
							"        	\"SHNUMBER\":\"8805102\",\r\n" +
							"        	\"VBELN\":\"305531347\",\r\n" +
							"        	\"POSNN\":\"10\",\r\n" +
							"        	\"WERKS\":\"2120\",\r\n" +
							"        	\"MATNR\":\"10270\",\r\n" +
							"        	\"SCH_QTY\":\"8000\",\r\n" +
							"        	\"SCH_UOM\":\"L\",\r\n" +
							"        	\"CARDNO01\":\"\",\r\n" +
							"        	\"CARDNO02\":\"\",\r\n" +
							"        	\"ZSEALNO\":\"\",\r\n" +
							"        	\"VSBED\":\"\",\r\n" +
							"        	\"SHIPPING_CREATE_INFO\":\"8804912\",\r\n" +
							"        	\"I_DISPATCH_NO\":\"D000150760\",\r\n" +
							"        	\"I_DELI_NO\":\"\",\r\n" +
							"        	\"I_ROUTE_SEQ\":\"\",\r\n" +
							"        	\"I_STOP_SEQ\":\"\",\r\n" +
							"        	\"I_DRV_CD\":\"6505\",\r\n" +
							"        	\"I_VEHICLE_CD\":\"85-9999\",\r\n" +
							"        	\"I_MIN_NO\":\"1076790974\",\r\n" +
							"        	\"I_LAT\":\"\",\r\n" +
							"        	\"I_LON\":\"\",\r\n" +
							"        	\"I_REPORT_DATE\":\"\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/shippingCreateSavWithEai")
	public @ResponseBody Map<String, Object> shippingCreateSavWithEai(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){

		String RANDOM_KEY_YN = json_body.get("RANDOM_KEY_YN").toString();
		String ZIFDAT = json_body.get("ZIFDAT").toString();
		String ZIFTIM = json_body.get("ZIFTIM").toString();

		String SHNUMBER = json_body.get("SHNUMBER").toString();
		String VBELN = json_body.get("VBELN").toString();
		String POSNN = json_body.get("POSNN").toString();
		String WERKS = json_body.get("WERKS").toString();
		String MATNR = json_body.get("MATNR").toString();
		String SCH_QTY = json_body.get("SCH_QTY").toString();
		String SCH_UOM = json_body.get("SCH_UOM").toString();
		String CARDNO01 = json_body.get("CARDNO01").toString();
		String CARDNO02 = json_body.get("CARDNO02").toString();
		String ZSEALNO = json_body.get("ZSEALNO").toString();
		String VSBED = json_body.get("VSBED").toString();


		String VEHICLE = json_body.get("VEHICLE").toString();
		String DRIVERCODE = json_body.get("DRIVERCODE").toString();

		HashMap[] hashMaps = new HashMap[1];
		hashMaps[0] = new HashMap();
		hashMaps[0].put("SHNUMBER", SHNUMBER);
		hashMaps[0].put("VBELN", VBELN);
		hashMaps[0].put("POSNN", POSNN);
		hashMaps[0].put("WERKS", WERKS);
		hashMaps[0].put("MATNR", MATNR);
		hashMaps[0].put("SCH_QTY", SCH_QTY);
		hashMaps[0].put("SCH_UOM", SCH_UOM);
		hashMaps[0].put("CARDNO01", CARDNO01);
		hashMaps[0].put("CARDNO02", CARDNO02);
		hashMaps[0].put("ZSEALNO", ZSEALNO);

		Map<String, Object> checkResult = new LinkedHashMap<String, Object>();

		// ----------------------------------------------------------------------------------------------------
		// 1.출하제약조건 확인...
		// ----------------------------------------------------------------------------------------------------
		checkResult = mobileDriverService.checkShippingCheclSel(
				WERKS,
				VEHICLE,
				DRIVERCODE,
				SHNUMBER,
				VBELN,
				POSNN,
				VSBED,
				MATNR
		);
		// 결과데이터 처리..
		JSONObject jsonObjectCheck = new JSONObject(checkResult);
		String jsonDataCheck = jsonObjectCheck.toString();
		JsonElement jelementCheck = new JsonParser().parse(jsonDataCheck);
		JsonObject retObjCheck = jelementCheck.getAsJsonObject();

		String checkResultCode = retObjCheck.get("resultCode").getAsString();
		String checkResultMsg = retObjCheck.get("resultMsg").getAsString();

		if (checkResultCode.equals("-1")) {
			// 출하제약조건 위배
			return checkResult;
		}

		// ----------------------------------------------------------------------------------------------------
		// 2.EAI 서비스 호출(출하요청)
		// ----------------------------------------------------------------------------------------------------
		Map<String, Object> eaiResult = new LinkedHashMap<String, Object>();

		if (RANDOM_KEY_YN.equals("Y")) {
			eaiResult = eaiService.disShippingCreateRan(ZIFDAT, ZIFTIM, VEHICLE, DRIVERCODE, hashMaps);
		} else {
			eaiResult = eaiService.disShippingCreate(ZIFDAT, ZIFTIM, VEHICLE, DRIVERCODE, hashMaps);
		}

		// 데이타 추출..
		JSONObject jsonObject = new JSONObject(eaiResult);
		String jsonData = jsonObject.toString();
		JsonElement jelement = new JsonParser().parse(jsonData);
		JsonObject jObject = jelement.getAsJsonObject();
		String resultCode = jObject.get("resultCode").getAsString();
		String resultMsg = jObject.get("resultMsg").getAsString();

		if (resultCode.equals("100")) {
			json_body.put("MSGTYP", "S");
		} else {
			json_body.put("MSGTYP", "E");
		}
		json_body.put("MSGNUM", resultCode);
		json_body.put("MSGTXT", resultMsg);

		// EAI 주석처리시, 필요..
//		json_body.put("MSGTYP", "s");
//		json_body.put("MSGNUM", "999");
//		json_body.put("MSGTXT", "MOBILE 테스트");

		// ----------------------------------------------------------------------------------------------------
		// 3.TMS 출하요청정보저장
		// ----------------------------------------------------------------------------------------------------
		Map<String, Object> tmsResult = new LinkedHashMap<String, Object>();
		tmsResult = mobileDriverService.shippingCreateSav(json_body);
		return tmsResult;
	}

	/**
	 *
	 * @param api_key
	 * @param json_body
	 * @return
	 */
	@ApiOperation(value="출발보고\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"I_DISPATCH_NO\":\"D000150760\",\r\n" +
							"        	\"I_DEST_CD\":\"\",\r\n" +
							"        	\"I_DRV_CD\":\"8465\",\r\n" +
							"        	\"I_VEHICLE_CD\":\"80-1588\",\r\n" +
							"        	\"I_MIN_NO\":\"1075905736\",\r\n" +
							"        	\"I_LAT\":\"\",\r\n" +
							"        	\"I_LON\":\"\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/departureReportSav")
	public @ResponseBody Map<String, Object> departureReportSav(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){
		return mobileDriverService.departureReportSav(json_body);
	}

	/**
	 *
	 * @param api_key
	 * @param json_body
	 * @return
	 */
	@ApiOperation(value="도착보고\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"I_DISPATCH_NO\":\"D000150760\",\r\n" +
							"        	\"I_DELI_NO\":\"A02090000006120R050000004\",\r\n" +
							"        	\"I_ROUTE_SEQ\":\"1\",\r\n" +
							"        	\"I_STOP_SEQ\":\"1\",\r\n" +
							"        	\"I_DRV_CD\":\"8465\",\r\n" +
							"        	\"I_VEHICLE_CD\":\"80-1588\",\r\n" +
							"        	\"I_MIN_NO\":\"1075905736\",\r\n" +
							"        	\"I_LAT\":\"\",\r\n" +
							"        	\"I_LON\":\"\",\r\n" +
							"        	\"I_DEST_CD\":\"\",\r\n" +
							"        	\"I_INVOICE_RESULT_URL\":\"\",\r\n" +
							"        	\"I_REASON_TYPE\":\"\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/arrivalReportSav")
	public @ResponseBody Map<String, Object> arrivalReportSav(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){
		return mobileDriverService.arrivalReportSav(json_body);
	}

	/**
	 *
	 * @param api_key
	 * @param json_body
	 * @return
	 */
	@ApiOperation(value="완료보고\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"I_DISPATCH_NO\":\"D000150760\",\r\n" +
							"        	\"I_DELI_NO\":\"A02090000006120R050000004\",\r\n" +
							"        	\"I_ROUTE_SEQ\":\"1\",\r\n" +
							"        	\"I_STOP_SEQ\":\"1\",\r\n" +
							"        	\"I_DRV_CD\":\"8465\",\r\n" +
							"        	\"I_VEHICLE_CD\":\"80-1588\",\r\n" +
							"        	\"I_MIN_NO\":\"1075905736\",\r\n" +
							"        	\"I_LAT\":\"\",\r\n" +
							"        	\"I_LON\":\"\",\r\n" +
							"        	\"I_DEST_CD\":\"\",\r\n" +
							"        	\"I_INVOICE_RESULT_URL\":\"\",\r\n" +
							"        	\"I_REASON_TYPE\":\"\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/deliFinishSav")
	public @ResponseBody Map<String, Object> deliFinishSav(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){
		return mobileDriverService.deliFinishSav(json_body);
	}

	/**
	 * 현재위치정보 저장
	 * @param api_key
	 * @param json_body
	 * @return
	 */
	@ApiOperation(value="현재위치정보 저장\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"          \"I_DEVICE_ID\" : \"\",\r\n" +
							"          \"I_TERMINAL_NO\" : \"\",\r\n" +
							"          \"I_POS_X\" : \"\",\r\n" +
							"          \"I_POS_Y\" : \"\",\r\n" +
							"          \"I_GPS_DTM\" : \"\",\r\n" +
							"          \"I_SPEED\" : \"\",\r\n" +
							"          \"I_ANGEL\" : \"\",\r\n" +
							"          \"I_BATTERY_LEVEL\" : \"\",\r\n" +
							"          \"I_ADDRESS_INFO\" : \"\",\r\n" +
							"          \"I_ETC_DATA\" : \"\",\r\n" +
							"          \"I_REPORT_DTM\" : \"\",\r\n" +
							"          \"I_DISTANCE_TO\" : \"\",\r\n" +
							"          \"I_SEQ\" : \"\",\r\n" +
							"          \"I_CHECK_DISTANCE\" : \"\",\r\n" +
							"          \"I_CHECK_TIME\" : \"\",\r\n" +
							"          \"I_CHECK_SPEED\" : \"\",\r\n" +
							"          \"I_MIN_NO\" : \"\",\r\n" +
							"          \"I_VHCL_PCL_CD\" : \"\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/currentPositionSav")
	public @ResponseBody Map<String, Object> currentPositionSav(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){
		return mobileDriverService.currentPositionSav(json_body);
	}

	/**
	 *
	 * @param api_key
	 * @param json_body
	 * @return
	 */
	@ApiOperation(value="사고목록 조회\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"          \"I_DRV_CD\":\"8465\",\r\n" +
							"          \"I_VEHICLE_CD\":\"80-1588\",\r\n" +
							"          \"I_MIN_NO\":\"1075905736\",\r\n" +
							"          \"I_ACT_DATE_FROM\":\"20220101\",\r\n" +
							"          \"I_ACT_DATE_TO\":\"20221231\",\r\n" +
							"          \"I_ACT_TYPE\":\"\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/accitentListSel")
	public @ResponseBody Map<String, Object> accitentListSel(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){
		return mobileDriverService.accitentListSel(json_body);
	}

	/**
	 * 사고목록 저장
	 * @param api_key
	 * @param json_body
	 * @return
	 */
	@ApiOperation(value="사고목록 저장\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"          \"I_DRV_CD\":\"8465\",\r\n" +
							"          \"I_VEHICLE_CD\":\"80-1588\",\r\n" +
							"          \"I_MIN_NO\":\"1075905736\",\r\n" +
							"          \"I_LAT\":\"\",\r\n" +
							"          \"I_LON\":\"\",\r\n" +
							"          \"I_ACT_TYPE\" : \"\",\r\n" +
							"          \"I_MEMO\" : \"\",\r\n" +
							"          \"I_FILE_CD\" : \"\",\r\n" +
							"          \"I_DISPATCH_NO\" : \"\",\r\n" +
							"          \"I_DELI_NO\" : \"\",\r\n" +
							"          \"I_ROUTE_SEQ\" : \"\",\r\n" +
							"          \"I_STOP_SEQ\" : \"\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/accitentInfoSav")
	public @ResponseBody Map<String, Object> accitentInfoSav(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){
		return mobileDriverService.accitentInfoSav(json_body);
	}

	/**
	 * 전자, 출하전표 저장
	 * @param api_key
	 * @param json_body
	 * @return
	 */
	@ApiOperation(value="전자, 출하전표 저장\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"          \"I_DRV_CD\":\"8465\",\r\n" +
							"          \"I_VEHICLE_CD\":\"80-1588\",\r\n" +
							"          \"I_MIN_NO\":\"1075905736\",\r\n" +
							"          \"I_LAT\":\"\",\r\n" +
							"          \"I_LON\":\"\",\r\n" +
							"          \"I_DISPATCH_NO\" : \"\",\r\n" +
							"          \"I_ROUTE_SEQ\" : \"\",\r\n" +
							"          \"I_STOP_SEQ\" : \"\",\r\n" +
							"          \"I_SIGN_TYPE\" : \"\",\r\n" +
							"          \"I_FILE_CD\" : \"\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/invoiceSignSav")
	public @ResponseBody Map<String, Object> invoiceSignSav(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){

		Map<String, Object> saveResult = new LinkedHashMap<String, Object>();

		saveResult = mobileDriverService.invoiceSignSav(json_body);

		// 서명이 고객사 서명일경우 전표를 생성해준다..
//	paraMap.put("SHNUMBER", shNumber);
//	paraMap.put("VBELN", vbeln);
//	paraMap.put("POSNR", posnr);
//	paraMap.put("DRV_SIGN_URL", mCurDispatchInfo.getINVOICE_DRV_IMG());
//	paraMap.put("CUST_SIGN_URL", mCurDispatchInfo.getINVOICE_CUST_IMG());
//	paraMap.put("ADD_SIGN_URL", mCurDispatchInfo.getINVOICE_ADD_IMG());

		String signType = json_body.get("I_SIGN_TYPE").toString();

		// 고객사 서명시점 전표생성 요청..
		if (signType.equals("20")) {
			// 데이타 추출..
			JSONObject jsonObject = new JSONObject(saveResult);
			String jsonData = jsonObject.toString();
			JsonElement jelement = new JsonParser().parse(jsonData);
			JsonObject jObject = jelement.getAsJsonObject();
			String resultCode = jObject.get("resultCode").getAsString();
			String resultMsg = jObject.get("resultMsg").getAsString();
			// 배차내역 Loop
			List<String> sourceList = new ArrayList<String>();

			if (saveResult.get("resultData") != null) {
				JsonArray jarrayRecordSetTms = jObject.get("resultData").getAsJsonArray();
				for (int idx = 0; idx < jarrayRecordSetTms.size(); ++idx) {
					JsonObject jobjectStoreTms = jarrayRecordSetTms.get(idx).getAsJsonObject();
					try {

						String DRV_SIGN_URL = jobjectStoreTms.get("INVOICE_DRV_IMG").toString();
						String CUST_SIGN_URL = jobjectStoreTms.get("INVOICE_CUST_IMG").toString();
						String ADD_SIGN_URL = jobjectStoreTms.get("INVOICE_ADD_IMG").toString();
						String INVOICE_KEY = jobjectStoreTms.get("INVOICE_KEY").getAsString(); // SHNUMBER+VBELN+POSNR

						LogUtil.infoLog(String.format("[기사서명] => %s ", DRV_SIGN_URL));
						LogUtil.infoLog(String.format("[고객사서명] => %s ", CUST_SIGN_URL));
						LogUtil.infoLog(String.format("[추가서명] => %s ", ADD_SIGN_URL));
						LogUtil.infoLog(String.format("[키값] => %s ", INVOICE_KEY));

						String arrDrvData[] = DRV_SIGN_URL.split("/");
						String arrCustData[] = CUST_SIGN_URL.split("/");
						String arrAddData[] = ADD_SIGN_URL.split("/");
						String arrKeyData[] = INVOICE_KEY.split("/");
						String drvFileName = "", custFileName = "", addFileName = "";
						if (arrDrvData.length>7) drvFileName = arrDrvData[7];
						if (arrCustData.length>7) custFileName = arrCustData[7];
						if (arrAddData.length>7) addFileName = arrAddData[7];

						String SHNUMBER = "", VBELN = "", POSNR = "";
						if (arrKeyData.length>2) SHNUMBER = arrKeyData[0];
						if (arrKeyData.length>2) VBELN = arrKeyData[1];
						if (arrKeyData.length>2) POSNR = arrKeyData[2];  // <= 무조건 10이라고 전달받음..

						Map<String, Object> eaiResult = new LinkedHashMap<String, Object>();

						// EAI 호출..
						eaiResult =  eaiService.loadingPrintCreate(SHNUMBER, VBELN, POSNR);

						FileController fileController = new FileController();
						// 전자전표(PDF)생성
						String retPath = fileController.invoiceImageCreate(
								eaiResult,
								drvFileName, custFileName, addFileName
						);
						LogUtil.infoLog(String.format("[전자전표이미지] => %s ", retPath));

						saveResult.put("invoiceImage", retPath);

					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
			}
		}
		return saveResult;
	}






	/**
	 * 전자, 출하전표 조회
	 * @param api_key
	 * @param json_body
	 * @return
	 */
	@ApiOperation(value="전자, 출하전표 조회(With.EAI)\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"SHNUMBER\":\"8734384\",\r\n" +
							"        	\"VBELN\":\"305421247\",\r\n" +
							"        	\"POSNR\":\"10\",\r\n" +
							"        	\"DRV_SIGN_URL\":\"\",\r\n" +
							"        	\"CUST_SIGN_URL\":\"\",\r\n" +
							"        	\"ADD_SIGN_URL\":\"\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/loadingPrintCreate")
	public @ResponseBody Map<String, Object> loadingPrintCreate(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){


		String SHNUMBER = json_body.get("SHNUMBER").toString();
		String VBELN = json_body.get("VBELN").toString();
		String POSNR = json_body.get("POSNR").toString();  // <= 무조건 10이라고 전달받음..

		String DRV_SIGN_URL = json_body.get("DRV_SIGN_URL").toString();
		String CUST_SIGN_URL = json_body.get("CUST_SIGN_URL").toString();
		String ADD_SIGN_URL = json_body.get("ADD_SIGN_URL").toString();

		String arrDrvData[] = DRV_SIGN_URL.split("/");
		String arrCustData[] = CUST_SIGN_URL.split("/");
		String arrAddData[] = ADD_SIGN_URL.split("/");
		String drvFileName = "", custFileName = "", addFileName = "";
		if (arrDrvData.length>7) drvFileName = arrDrvData[7];
		if (arrCustData.length>7) custFileName = arrCustData[7];
		if (arrAddData.length>7) addFileName = arrAddData[7];

		Map<String, Object> eaiResult = new LinkedHashMap<String, Object>();

		// EAI 호출..
		eaiResult =  eaiService.loadingPrintCreate(SHNUMBER, VBELN, POSNR);

		FileController fileController = new FileController();
		//String retPath = fileController.invoiceImageCreate(eaiResult, "D:\\HDO_2022\\project\\MOB\\ApiServer\\upload\\invoice\\202207\\20220726\\D000225812_1_1_20_054604_6088220664.png");
		String retPath = fileController.invoiceImageCreate(
				eaiResult,
				drvFileName, custFileName, addFileName
		);



		LogUtil.infoLog(String.format("[전자전표이미지] => %s ", retPath));

		eaiResult.put("invoiceImage", retPath);

		return eaiResult;
	}

	/**
	 *
	 * @param api_key
	 * @param json_body
	 * @return
	 */
	@ApiOperation(value="전표 서명정보 조회\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "1Bb6wI6DnVL8S8LQAbvAO+pSnKXCf7HwQ7GEn+FnrJk=", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"I_INVOICE_CD\":\"\",\r\n" +
							"        	\"I_DISPATCH_NO\":\"D000225812\",\r\n" +
							"        	\"I_ROUTE_SEQ\":\"1\",\r\n" +
							"        	\"I_STOP_SEQ\":\"1\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/invoiceSignInfoSel")
	public @ResponseBody Map<String, Object> invoiceSignInfoSel(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){
		return mobileDriverService.invoiceListSel(json_body);
	}



	/**
	 * @param api_key
	 * *
	 @param json_body
	  * @return
	 */
	@ApiOperation(value="자차용,출하정보 조회\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"I_SHNUMBER\":\"10353323\",\r\n" +
							"        	\"I_SHIPMENT_DT\":\"20220911\",\r\n" +
							"        	\"I_SOURCE_CD\":\"1000\",\r\n" +
							"        	\"I_DRV_CD\":\"5556\",\r\n" +
							"        	\"I_VEHICLE_CD\":\"81-8208\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/shippingInfoSel")
	public @ResponseBody
	Map<String, Object> shippingInfoSel(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){


		Map<String, Object> tmsResult = new LinkedHashMap<String, Object>();
		tmsResult = mobileDriverService.shippingInfoSel(json_body);

		return tmsResult;
	}

	/**
	 * @param api_key
	 * *
	 @param json_body
	  * @return
	 */
	@ApiOperation(value="기사정보 수정\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"I_ID\":\"9999\",\r\n" +
							"        	\"I_PASS\":\"1111\",\r\n" +
							"        	\"I_TAB_NO\":\"01050700131\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/driverUpdateSav")
	public @ResponseBody
	Map<String, Object> driverUpdateSav(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body){

		Map<String, Object> tmsResult = new LinkedHashMap<String, Object>();
		try {
			// 암호화하여 다시넣는다..
			String orgString = json_body.get("I_PASS").toString();
			String encString = SysUtils.encryptSHA256(orgString);
			json_body.put("I_PASS", encString);

			tmsResult = mobileDriverService.driverUpdateSav(json_body);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return tmsResult;
	}

	/**
	 * @param api_key
	 * *
	 @param json_body
	  * @return
	 */
	@ApiOperation(value="비밀번호 변경체크\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"I_ID\":\"9999\",\r\n" +
							"        	\"I_TAB_NO\":\"01050700131\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/passwordChk")
	public @ResponseBody
	Map<String, Object> passwordChk(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body)
	{

		Map<String, Object> tmsResult = new LinkedHashMap<String, Object>();
		tmsResult = mobileDriverService.passwordChk(json_body);
		return tmsResult;
	}




	/**
	 * @param api_key
	 * *
	 @param json_body
	  * @return
	 */
	@ApiOperation(value="사용자인증 체크\n")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "json_body",
					value =  "        {\r\n" +
							"        	\"I_ID\":\"\",\r\n" +
							"        	\"I_PASS\":\"\",\r\n" +
							"        	\"I_TAB_NO\":\"\"\r\n" +
							"        }",
					required = true, dataType = "object", paramType = "body")
	})
	@CrossOrigin("*")
	@PostMapping(value="/authCheck")
	public @ResponseBody
	Map<String, Object> authCheck(@RequestHeader String api_key, @RequestBody LinkedHashMap<String, Object> json_body)
	{

		Map<String, Object> tmsResult = new LinkedHashMap<String, Object>();
		tmsResult = mobileDriverService.authCheck(json_body);
		return tmsResult;
	}
}