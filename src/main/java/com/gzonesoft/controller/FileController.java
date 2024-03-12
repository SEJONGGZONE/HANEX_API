package com.gzonesoft.controller;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gzonesoft.domain.SignType;
import com.gzonesoft.service.MobileDriverService;
import com.gzonesoft.utils.LogUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.gzonesoft.domain.FileDto;
import com.gzonesoft.domain.FileInfoResponse;
import com.gzonesoft.service.KioskService;
import com.gzonesoft.utils.EncryptUtil;
import com.gzonesoft.utils.SysUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

// 이미지파일 처리를위한..
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * http://localhost:9032/swagger-ui.html
 * @author ships
 *
 */
@RequestMapping("/api/file")
@RestController
public class FileController {
	@Autowired private KioskService kioskService;
	@Autowired private MobileDriverService mobileDriverService;


	/**
	 * 
	 * @param request
	 * @param api_key
	 * @param files
	 * @param fileKind "regcard"
	 * @param fileFormat "jpg"
	 * @param isEncrypt "true"
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	@ApiOperation(value="파일 업로드")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header")
      })    	
	@CrossOrigin("*")        
	@PostMapping("/upload/{fileKind}/{fileFormat}/{isEncrypt}")
	public @ResponseBody Map<String,Object> upload(HttpServletRequest request, @RequestHeader String api_key, @RequestParam("files") List<MultipartFile> files, 
	//public @ResponseBody Map<String,Object> upload(HttpServletRequest request, @RequestParam("files") List<MultipartFile> files,
			@PathVariable("fileKind") String fileKind, 
			@PathVariable("fileFormat") String fileFormat, 
			@PathVariable("isEncrypt") String isEncrypt
			) throws NoSuchAlgorithmException {

		Map<String,Object> returnMap = new HashMap<String,Object>();
		
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //매핑되지 않는 항목은 Skip
        
		try {
            //암호화 키 생성
			String encKey = "";
			String curTimeYYYYMM = SysUtils.getCurrentTime("yyyyMM");
			String curTimeYYYYMMDD = SysUtils.getCurrentTime("yyyyMMdd");
			String saveDirPath = System.getProperty("user.dir")+File.separatorChar+"upload"+File.separatorChar+fileKind + File.separatorChar + curTimeYYYYMM + File.separatorChar + curTimeYYYYMMDD;
			File dirPath = new File(saveDirPath);
			
			String saveFileName = "";
			File filePath = null;
			
			if(!dirPath.exists()) {
				dirPath.mkdirs();
			}

			List<FileDto> upFiles = new ArrayList<FileDto>();
			
			for (MultipartFile file : files) {

				byte[] fileBytes;
				if(isEncrypt.equals("true")) {
					encKey = SysUtils.getAlphaUniqueNumber(16);
					
					saveFileName = String.format("%s_%s%s", SysUtils.getCurrentTime("yyyyMMddhhmmss"), SysUtils.getNumericUniqueNumber(10),".enc");
					//파일 바이트 암호화
					fileBytes = EncryptUtil.encryptBytes(encKey, file.getBytes());
				}else {
					encKey = "";
					
					saveFileName = String.format("%s_%s%s", SysUtils.getCurrentTime("yyyyMMddhhmmss"), SysUtils.getNumericUniqueNumber(10),"."+fileFormat);
					fileBytes = file.getBytes();
				}
				
				filePath = new File(saveDirPath + File.separatorChar + saveFileName);

				Files.write(filePath.toPath(), fileBytes);
				
				FileDto fileDto = new FileDto();
				fileDto.fileName = saveFileName;
				fileDto.fileFormat = fileFormat;
				fileDto.fileSize = Long.toString(filePath.length());
				fileDto.fileKind = fileKind;
				fileDto.filePath1 = curTimeYYYYMM;
				fileDto.filePath2 = curTimeYYYYMMDD;
				fileDto.encryptKey = encKey;
				fileDto.fileCreatedTime = SysUtils.getCurrentTime("yyyyMMddhhmmss");

				LinkedHashMap<String, Object> param = new LinkedHashMap<String, Object>();
				param.put("fileName", fileDto.fileName);
				//param.put("filePath", String.format("%s/%s/%s/%s/%s/%s", fileKind, curTimeYYYYMM, curTimeYYYYMMDD, saveFileName, fileFormat, encKey));
				//param.put("filePath", String.format("%s/%s/%s/%s", fileKind, curTimeYYYYMM, curTimeYYYYMMDD, saveFileName));
				param.put("filePath", String.format("/api/file/download/%s/%s/%s/%s/%s/0", fileKind, curTimeYYYYMM, curTimeYYYYMMDD, saveFileName, fileFormat));
				param.put("fileSize", fileDto.fileSize);

				String strResponse = mapper.writeValueAsString(kioskService.setFileInfo(param));

				FileInfoResponse fileInfoResponse = mapper.readValue(strResponse, new TypeReference<FileInfoResponse>() {});

				if (fileInfoResponse.resultCode.equals("00")) {
					fileDto.fileNo = fileInfoResponse.fileDetails.get(0).fileNo;

					upFiles.add(fileDto);
				} else {
					throw new Exception();
				}
			}
			
			returnMap.put("resultCode", "00");
			returnMap.put("resultMsg", "SUCCESS");
			returnMap.put("resultData", upFiles);
			
		}catch(Exception ex) {
			returnMap.put("resultCode", "01");
			returnMap.put("resultMsg", "FAIL");
			returnMap.put("resultData", ex.toString());	
		}
		
		return returnMap;
	}

	/**
	 * 파일 업로드 처리(키오스크전용)
	 * @param request
	 * @param api_key
	 * @param files
	 * @param terminalCode
	 * @param kioskId
	 * @param fileKind
	 * @param fileFormat
	 * @param isEncrypt
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@ApiOperation(value="파일 업로드(키오스크 전용)")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header")
	})
	@CrossOrigin("*")
	@PostMapping("/uploadKiosk/{terminalCode}/{kioskId}/{fileKind}/{fileFormat}/{isEncrypt}")
	public @ResponseBody Map<String,Object> uploadKiosk(HttpServletRequest request, @RequestHeader String api_key, @RequestParam("files") List<MultipartFile> files,
														@PathVariable("terminalCode") String terminalCode,
														@PathVariable("kioskId") String kioskId,
														@PathVariable("fileKind") String fileKind,
														@PathVariable("fileFormat") String fileFormat,
														@PathVariable("isEncrypt") String isEncrypt
	) throws NoSuchAlgorithmException {

		Map<String,Object> returnMap = new HashMap<String,Object>();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //매핑되지 않는 항목은 Skip

		try {
			//암호화 키 생성
			String encKey = "";
			String curTimeYYYYMM = SysUtils.getCurrentTime("yyyyMM");
			String curTimeYYYYMMDD = SysUtils.getCurrentTime("yyyyMMdd");
			String saveDirPath = System.getProperty("user.dir")+File.separatorChar+"upload"+File.separatorChar+fileKind + File.separatorChar + curTimeYYYYMM + File.separatorChar + curTimeYYYYMMDD;
			File dirPath = new File(saveDirPath);

			String saveFileName = "";
			File filePath = null;

			if(!dirPath.exists()) {
				dirPath.mkdirs();
			}

			List<FileDto> upFiles = new ArrayList<FileDto>();

			for (MultipartFile file : files) {

				byte[] fileBytes;
				if(isEncrypt.equals("true")) {
					encKey = SysUtils.getAlphaUniqueNumber(16);

					saveFileName = String.format("%s_%s%s", SysUtils.getCurrentTime("yyyyMMddhhmmss"), SysUtils.getNumericUniqueNumber(10),".enc");
					//파일 바이트 암호화
					fileBytes = EncryptUtil.encryptBytes(encKey, file.getBytes());
				}else {
					encKey = "";

					saveFileName = String.format("%s_%s_%s_%s%s", terminalCode, kioskId, SysUtils.getCurrentTime("hhmmss"), SysUtils.getNumericUniqueNumber(10),"."+fileFormat);
					fileBytes = file.getBytes();
				}

				filePath = new File(saveDirPath + File.separatorChar + saveFileName);

				Files.write(filePath.toPath(), fileBytes);

				FileDto fileDto = new FileDto();
				fileDto.fileName = saveFileName;
				fileDto.fileFormat = fileFormat;
				fileDto.fileSize = Long.toString(filePath.length());
				fileDto.fileKind = fileKind;
				fileDto.filePath1 = curTimeYYYYMM;
				fileDto.filePath2 = curTimeYYYYMMDD;
				fileDto.encryptKey = encKey;
				fileDto.fileCreatedTime = SysUtils.getCurrentTime("yyyyMMddhhmmss");

				LinkedHashMap<String, Object> param = new LinkedHashMap<String, Object>();
				param.put("terminalCode", terminalCode);
				param.put("kioskId", kioskId);
				param.put("fileName", fileDto.fileName);
				param.put("filePath", String.format("%s/%s/%s/%s", fileKind, curTimeYYYYMM, curTimeYYYYMMDD, saveFileName));
				param.put("fileSize", fileDto.fileSize);

				String strResponse = mapper.writeValueAsString(kioskService.setFileInfoKiosk(param));

				FileInfoResponse fileInfoResponse = mapper.readValue(strResponse, new TypeReference<FileInfoResponse>() {});

				if (fileInfoResponse.resultCode.equals("00")) {
					fileDto.fileNo = fileInfoResponse.fileDetails.get(0).fileNo;

					upFiles.add(fileDto);
				} else {
					throw new Exception();
				}
			}

			returnMap.put("resultCode", "00");
			returnMap.put("resultMsg", "SUCCESS");
			returnMap.put("resultData", upFiles);

		}catch(Exception ex) {
			returnMap.put("resultCode", "01");
			returnMap.put("resultMsg", "FAIL");
			returnMap.put("resultData", ex.toString());
		}

		return returnMap;
	}

	/**
	 * 파일 업로드 처리(출하전표 전용)
	 * @param request
	 * @param api_key
	 * @param files
	 * @param dispatchNo
	 * @param routeSeq
	 * @param stopSeq
	 * @param signType
	 * @param fileKind
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@ApiOperation(value="파일 업로드(출하전표 전용)")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "api_key", value = "API 키", required = true, dataType = "String", paramType = "header")
	})
	@CrossOrigin("*")
	@PostMapping("/uploadInvoice/{dispatchNo}/{routeSeq}/{stopSeq}/{signType}/{fileKind}")
	public @ResponseBody Map<String,Object> uploadInvoice(HttpServletRequest request, @RequestHeader String api_key, @RequestParam("files") List<MultipartFile> files,
														@PathVariable("dispatchNo") String dispatchNo,
													    @PathVariable("routeSeq") String routeSeq,
													    @PathVariable("stopSeq") String stopSeq,
													    @PathVariable("signType") String signType,
													    @PathVariable("fileKind") String fileKind
	) throws NoSuchAlgorithmException {

		Map<String,Object> returnMap = new HashMap<String,Object>();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //매핑되지 않는 항목은 Skip

		try {
			//암호화 키 생성
			//String encKey = "";
			String curTimeYYYYMM = SysUtils.getCurrentTime("yyyyMM");
			String curTimeYYYYMMDD = SysUtils.getCurrentTime("yyyyMMdd");
			String saveDirPath = System.getProperty("user.dir")+File.separatorChar+"upload"+File.separatorChar+fileKind + File.separatorChar + curTimeYYYYMM + File.separatorChar + curTimeYYYYMMDD;
			File dirPath = new File(saveDirPath);

			String saveInvoiceFileName = "", saveSignFileName = "";
			File resultFile = null;

			if(!dirPath.exists()) {
				dirPath.mkdirs();
			}

			List<FileDto> uploadResults = new ArrayList<FileDto>();

			String strResponse = "";

			for (MultipartFile file : files) {

				byte[] fileData;
				fileData = file.getBytes(); // 파일받기..

				/*
				 * 1.출하전표용 PDF 생성(서명 이미지 포함)
				 */
				if (signType.equals(SignType._기사서명)) {
					saveInvoiceFileName = String.format("%s_%s_%s_%s_%s_%s%s",
							dispatchNo, routeSeq, stopSeq, signType,
							SysUtils.getCurrentTime("hhmmss"),
							SysUtils.getNumericUniqueNumber(10),
							".pdf"
					);

					resultFile = new File(saveDirPath + File.separatorChar + saveInvoiceFileName);

				} else if (signType.equals(SignType._고객사서명)) {
					saveInvoiceFileName = String.format("%s_%s_%s_%s_%s_%s%s",
							dispatchNo, routeSeq, stopSeq, signType,
							SysUtils.getCurrentTime("hhmmss"),
							SysUtils.getNumericUniqueNumber(10),
							".pdf"
					);

//					Map<String, Object> templateParam = new HashMap<String, Object>();
//					templateParam.put("dispatchNo", dispatchNo);
//					templateParam.put("signImage", Base64Utils.encodeToString(fileData)); //서명이미지 바이트 배열을 Base64로 변환하여 HTML에 삽입
//
//					resultFile = new File(saveDirPath + File.separatorChar + saveInvoiceFileName);
//
//					Files.write(resultFile.toPath(), fileData);
//
//					// 템플릿 파일 지정(세팅)..
//					String htmlTempletPath = System.getProperty("user.dir")+"/config/templates/html";
//					Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);
//					cfg.setDirectoryForTemplateLoading(new File(htmlTempletPath));
//					Template template = cfg.getTemplate("invoice.html"); //인보이스 템플릿 파일
//					// 템플릿+파라미터(데이타) 매핑..
//					StringWriter sw = new StringWriter();
//					template.process(templateParam, sw);
//					String requestBody = sw.toString();
//
//					resultFile = new File(saveDirPath + File.separatorChar + saveInvoiceFileName);
//
//					//HTML -> PDF 파일 생성
//					PdfUtil.htmlToPdf(requestBody, htmlTempletPath, resultFile);
				}

				/**
				 * 2.서명이미지 저장(파일쓰기)
				 */
				saveSignFileName = String.format("%s_%s_%s_%s_%s_%s%s",
						dispatchNo, routeSeq, stopSeq, signType,
						SysUtils.getCurrentTime("hhmmss"),
						SysUtils.getNumericUniqueNumber(10),
						".png"
				);
				resultFile = new File(saveDirPath + File.separatorChar + saveSignFileName);
				Files.write(resultFile.toPath(), fileData);

				/**
				 * 3.파일정보 저장..
				 */
				// 파라미터 생성
				LinkedHashMap<String, Object> param = new LinkedHashMap<String, Object>();

//				if (signType.equals(SignType._기사서명)) {
					param.put("dispatchNo", dispatchNo);
					param.put("routeSeq", routeSeq);
					param.put("stopSeq", stopSeq);
					param.put("signType", signType);
					param.put("fileName", saveSignFileName);
					param.put("fileSignPath", String.format("%s/%s/%s/%s/png/0", fileKind, curTimeYYYYMM, curTimeYYYYMMDD, saveSignFileName));
					param.put("fileInvoicePath", ""); // 전표 미생성..
					param.put("fileSize", Long.toString(resultFile.length()));
//				} else if (signType.equals(SignType._고객사서명)) {
//					param.put("dispatchNo", dispatchNo);
//					param.put("routeSeq", routeSeq);
//					param.put("stopSeq", stopSeq);
//					param.put("signType", signType);
//					param.put("fileName", saveInvoiceFileName);
//					param.put("fileSignPath", String.format("%s/%s/%s/%s/png/0", fileKind, curTimeYYYYMM, curTimeYYYYMMDD, saveSignFileName));
//					//param.put("fileInvoicePath", String.format("%s/%s/%s/%s/pdf/0", fileKind, curTimeYYYYMM, curTimeYYYYMMDD, saveInvoiceFileName)); // 전표 생성.. => 보류..(2022.07.21)
//					param.put("fileInvoicePath", ""); // 전표 미생성..
//					param.put("fileSize", Long.toString(resultFile.length()));
//				}

				//파일정보 저장용 API 호출
				strResponse = mapper.writeValueAsString(
						mobileDriverService.setFileInfoInvoice(param)
				);

				FileInfoResponse fileInfoResponse = mapper.readValue(strResponse, new TypeReference<FileInfoResponse>() {});

				if (fileInfoResponse.resultCode.equals("00")) {
					FileDto fileDto = new FileDto();
					fileDto.fileName = saveInvoiceFileName;
					fileDto.fileFormat = "pdf";
					fileDto.fileSize = Long.toString(resultFile.length());
					fileDto.fileKind = fileKind;
					fileDto.filePath1 = curTimeYYYYMM;
					fileDto.filePath2 = curTimeYYYYMMDD;
					fileDto.encryptKey = "";
					fileDto.fileCreatedTime = SysUtils.getCurrentTime("yyyyMMddhhmmss");

					fileDto.fileNo = fileInfoResponse.fileDetails.get(0).fileNo;
					// 출하전표 관련 추가..
					fileDto.signFileCd = fileInfoResponse.fileDetails.get(0).signFileCd;
					fileDto.signFilePath = fileInfoResponse.fileDetails.get(0).signFilePath;
					fileDto.invoiceFileCd = ""; // fileInfoResponse.fileDetails.get(0).invoiceFileCd;
					fileDto.invoiceFilePath = ""; // fileInfoResponse.fileDetails.get(0).invoiceFilePath;

					uploadResults.add(fileDto);
				} else {
					throw new Exception();
				}
			}

			returnMap.put("resultCode", "00");
			returnMap.put("resultMsg", "SUCCESS");
			returnMap.put("resultData", uploadResults);

		}catch(Exception ex) {
			returnMap.put("resultCode", "01");
			returnMap.put("resultMsg", "FAIL");
			returnMap.put("resultData", ex.toString());

			LogUtil.infoLog(String.format("오류발생 => %s ", ex.toString()));
		}

		return returnMap;
	}

	/**
	 * 파일다운로드 서비스
	 * http://localhost:9032/api/file/download/regcard/202204/20220404/20220404054631_9904562741.enc/jpg/1234567890123456
	 * @param request
	 * @param response
	 * @param fileKind
	 * @param filePath1
	 * @param filePath2
	 * @param fileName
	 * @param fileFormat
	 * @param encryptKey
	 * @throws Exception
	 */
	@GetMapping("/download/{fileKind}/{filePath1}/{filePath2}/{fileName}/{fileFormat}/{encryptKey}")
	public void download(HttpServletRequest request, HttpServletResponse response, 
	//public void download(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("fileKind") String fileKind, 
			@PathVariable("filePath1") String filePath1, 
			@PathVariable("filePath2") String filePath2, 
			@PathVariable("fileName") String fileName, 
			@PathVariable("fileFormat") String fileFormat, 
			@PathVariable("encryptKey") String encryptKey
			) throws Exception {
		String fileSavePath = String.format("%s/%s/%s/%s/%s/%s", System.getProperty("user.dir"), "upload", fileKind, filePath1, filePath2, fileName);
		String newFileName = String.format("%s_%s%s%s", SysUtils.getCurrentTime("yyyyMMddhhmmss"), SysUtils.getNumericUniqueNumber(10),".", fileFormat);
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
		
		Path filePath = Paths.get(fileSavePath);

		if (Files.exists(filePath)) {

			byte[] fileBytes;
			
			if(fileExt.equals("enc")) {
				//암호화된 파일의 경우 복호화 시킨다.
				fileBytes = EncryptUtil.decryptBytes(encryptKey, Files.readAllBytes(filePath));
			}else {
				fileBytes = Files.readAllBytes(filePath);
			}

			//response.setContentType("application/vnd.android.package-archive");
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition", "attachment; filename=" + newFileName);
			try {
				
				ByteArrayInputStream in = new ByteArrayInputStream(fileBytes);
				IOUtils.copy(in, response.getOutputStream());
				//Files.copy(filePath, response.getOutputStream());
				response.getOutputStream().flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	// invoiceImageCreate(eaiResult, "D:\\HDO_2022\\project\\MOB\\ApiServer\\upload\\invoice\\202207\\20220726\\D000225812_1_1_20_054604_6088220664.png"
	public  String invoiceImageCreate(Map<String,Object> data,
									  String signDriverFilePath,
									  String signCustFilePath,
									  String addrFilePath)
	{
		String sharedURL = "";

		try {
			String invoiceBgFilePath = "", outPutFilePath_PNG = "", outPutFilePath_PDF = "";

			// 데이타 추출..
			JSONObject jsonObject = new JSONObject(data);
			String jsonData = jsonObject.toString();
			JsonElement jelement = new JsonParser().parse(jsonData);
			JsonObject jObject = jelement.getAsJsonObject();
			String resultCode = jObject.get("resultCode").getAsString();
			String resultMsg = jObject.get("resultMsg").getAsString();

			if (jObject.get("resultData") != null) {
				JsonObject jobjectStore = jObject.get("resultData").getAsJsonObject();

				String SHNUMBER = String.format("%.0f", Double.parseDouble(jobjectStore.get("SHNUMBER").getAsString()));
				String VBELN = String.format("%.0f", Double.parseDouble(jobjectStore.get("VBELN").getAsString()));

				String curTimeYYYYMM = SysUtils.getCurrentTime("yyyyMM");
				String curTimeYYYYMMDD = SysUtils.getCurrentTime("yyyyMMdd");
				String saveDirPath = System.getProperty("user.dir") + File.separatorChar + "upload" + File.separatorChar + "invoice" + File.separatorChar + curTimeYYYYMM + File.separatorChar + curTimeYYYYMMDD;
				// 외부접근 URL생성
				sharedURL = "api/file/download/invoice/" + curTimeYYYYMM + "/" + curTimeYYYYMMDD + "/" + SHNUMBER + ".pdf/pdf/0";
				File dirPath = new File(saveDirPath);

				String saveFileName = "";
				File filePath = null;

				if (!dirPath.exists()) {
					dirPath.mkdirs();
				}
				invoiceBgFilePath = System.getProperty("user.dir") + File.separatorChar + "upload" + File.separatorChar + "invoice" + File.separatorChar + "invoice_bg_01.png";

				if(!signDriverFilePath.isEmpty()) signDriverFilePath = saveDirPath + File.separatorChar + signDriverFilePath;
				if(!signCustFilePath.isEmpty()) signCustFilePath = saveDirPath + File.separatorChar + signCustFilePath;
				if(!addrFilePath.isEmpty()) addrFilePath = saveDirPath + File.separatorChar + addrFilePath;
				outPutFilePath_PNG = saveDirPath + File.separatorChar + SHNUMBER + ".png";
				outPutFilePath_PDF = saveDirPath + File.separatorChar + SHNUMBER + ".pdf";

				File[] imgFile = new File[1];
				imgFile[0] = new File(outPutFilePath_PNG);

				// PDF생성
				String retStr = makeImgPdf(jobjectStore, imgFile, outPutFilePath_PDF,
						signDriverFilePath, signCustFilePath, addrFilePath);

				// 아래는 배경이미지를 깔고 데이타를 입히는 방식..(주석처리)

//
//				// 이미지 준비
//				BufferedImage invoiceBgImage = ImageIO.read(new File(invoiceBgFilePath));
//				BufferedImage signDriverImage = resizeImage(signDriverFilePath, 260, 100);
//				// 출력크기 설정..
//				int width = Math.max(invoiceBgImage.getWidth(), signDriverImage.getWidth());
//				int height = invoiceBgImage.getHeight();
//
//				BufferedImage mergedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//				Graphics2D graphics = (Graphics2D) mergedImage.getGraphics();
//
//				graphics.setBackground(Color.WHITE);
//				graphics.drawImage(invoiceBgImage, 0, 0, null);
//				// 서명이미지 세팅(기사)
//				graphics.drawImage(signDriverImage, 355, 2005, null); // 기사서명#1
//				graphics.drawImage(signDriverImage, 950, 2005, null); // 기사서명#2
//				// 착색제서명..
//				graphics.drawImage(signDriverImage, 820, 1530, null); // 기사서명#1
//				graphics.drawImage(signDriverImage, 820, 1630, null); // 기사서명#1
//				// 서명이미지 세팅(고객사)
//				graphics.drawImage(signDriverImage, 70, 2005, null); // 고객사 서명#1
//				graphics.drawImage(signDriverImage, 680, 2005, null); // 고객사 서명#2
//				graphics.drawImage(signDriverImage, 850, 2360, null); // 고객사 서명#3
//				// 전표데이타 세팅(EAI)
//				graphics.setColor(Color.BLUE);
//				graphics.setFont(graphics.getFont().deriveFont(38f));
//
//				graphics.drawString(jobjectStore.get("PSTDAT").getAsString() + " " + jobjectStore.get("PSTTIM").getAsString(), 290, 460);
//				graphics.drawString(jobjectStore.get("BATXT").getAsString(), 945, 455);
//				graphics.drawString(SHNUMBER, 403, 540);
//				graphics.drawString("(" + VBELN + ")", 373, 585);
//				graphics.drawString("경기94자6680", 945, 560);
//				graphics.drawString("(주)승진상사", 283, 662);
//				graphics.drawString("인천물류센터 0000058", 283, 762);
//				graphics.drawString("(주)수암석유상사 000-000-0000", 283, 850);
//				graphics.drawString("경기도 안산시 상록구 광덕산2로 22", 283, 897);
//
//				graphics.drawString("ULSD", 200, 1150);
//				graphics.drawString("99", 600, 1150);
//				graphics.drawString("L", 780, 1150);
//				graphics.drawString("10,000", 990, 1150);
//
//				graphics.drawString("9.5", 350, 1300);
//				graphics.drawString("819.9", 750, 1300);
//				graphics.drawString("677", 1050, 1300);
//
//				graphics.drawString("99,999", 550, 1380);
//				graphics.drawString("IC302", 1050, 1380);
//
//				graphics.drawString("유동점", 550, 1480);
//				graphics.drawString("9999", 1050, 1480);
//
//				//			graphics.drawString("비고텍스트01", 283, 1550);
//				//			graphics.drawString("비고텍스트04", 750, 1550);
//				//			graphics.drawString("비고텍스트02", 283, 1590);
//				//			graphics.drawString("비고텍스트05", 750, 1590);
//				//			graphics.drawString("비고텍스트03", 283, 1630);
//				//			graphics.drawString("비고텍스트06", 750, 1630);
//				//			graphics.drawString("비고텍스트09", 283, 1670);
//				//			graphics.drawString("비고텍스트07", 750, 1670);
//				//			graphics.drawString("비고텍스트12", 283, 1710);
//				//			graphics.drawString("비고텍스트08", 750, 1710);
//				//			graphics.drawString("비고텍스트10", 283, 1750);
//				//			graphics.drawString("비고텍스트11", 283, 1790);
//				graphics.drawString("", 283, 1550);
//				graphics.drawString("", 750, 1550);
//				graphics.drawString("착색제 수령 확인 : (수송담당 서명)", 283, 1590);
//				graphics.drawString("", 750, 1590);
//				graphics.drawString("", 283, 1630);
//				graphics.drawString("", 750, 1630);
//				graphics.drawString("착색제 투입 확인 : (입고 담당자 서명)", 283, 1670);
//				graphics.drawString("", 750, 1670);
//				graphics.drawString("", 283, 1710);
//				graphics.drawString("", 750, 1710);
//				graphics.drawString("", 283, 1750);
//				graphics.drawString("", 283, 1790);
//
//				graphics.drawString("박인범", 265, 2160);
//				graphics.drawString("최현철", 650, 2160);
//				graphics.drawString("박종호", 1000, 2160);
//
//
//				// 전표이미지 최종생성
//				ImageIO.write(mergedImage, "png", new File(outPutFilePath_PNG));
//				// 리사이즈
//				//ImageIO.write(resizeImage(outPutFilePath_PNG, 650, 1386), "png", new File(outPutFilePath_PNG));


			}


		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			// 전표이미지 경로리턴
			return sharedURL;
		}
	}

	public static BufferedImage resizeImage(String filePath, int width, int height)
			throws IOException {
		BufferedImage inputImage = ImageIO.read(new File(filePath));

		BufferedImage outputImage =
				new BufferedImage(width, height, inputImage.getType());

		Graphics2D graphics2D = outputImage.createGraphics();
		graphics2D.drawImage(inputImage, 0, 0, width, height, null);
		graphics2D.dispose();

		return outputImage;
	}

	public static String makeImgPdf(JsonObject jobjectStore, File[] imgDir ,
									String pdfDir,
									String drvSignUrl,
									String custSignUrl,
									String addSignUrl) throws Exception {
		String result = "";
		//PDF 문서를 생성함
		PDDocument doc = new PDDocument();
		try {
			//Post를 통해 생성할 이미지를 가져옴
			File[] imgFiles = imgDir;

			//첨부된 이미지 파일 갯수만큼 반복문 실행
			for ( int i = 0; i<imgFiles.length; i++) {
				//이미지 사이즈 확인
				//Image img = ImageIO.read(imgFiles[i]);

				//PDF 페이지를 생성함
				PDPage page = new PDPage(PDRectangle.A4);
				// 그래서 PDF 문서내에 삽입함
				doc.addPage(page);

				try (PDPageContentStream cont = new PDPageContentStream(doc, page)) {
					// 이미지 세팅..(서명)
					String ciImagePath = System.getProperty("user.dir") + File.separatorChar + "upload" + File.separatorChar + "invoice" + File.separatorChar + "ci_logo_01.png";
					//String ciImagePath = "D:\\HDO_2022\\project\\MOB\\ApiServer\\upload\\invoice\\ci_logo_01.png";
					//String signImagePath = "D:\\HDO_2022\\project\\MOB\\ApiServer\\upload\\invoice\\202207\\20220727\\driver_sign.png";
					//PDImageXObject pdSignImage = PDImageXObject.createFromFile(signImagePath, doc);
					PDImageXObject pdDrvSignImage = PDImageXObject.createFromFile(drvSignUrl, doc);
					PDImageXObject pdCustSignImage = PDImageXObject.createFromFile(custSignUrl, doc);

					int iw = pdDrvSignImage.getWidth()/10;
					int ih = pdDrvSignImage.getHeight()/10;

					//---------------------------------------------------------------------------------------------------------
					// 서명 이미지 세팅
					//---------------------------------------------------------------------------------------------------------
					if (!addSignUrl.isEmpty()) {
						PDImageXObject pdAddSignImage = PDImageXObject.createFromFile(addSignUrl, doc);
						// 기사(착색제확인)
						cont.drawImage(pdAddSignImage, 300f, 370f, iw*3, ih*3);
						// 고객사(착색제확인)
						cont.drawImage(pdAddSignImage, 300f, 330f, iw*3, ih*3);
					}


					// 고객사(인수확인)
					cont.drawImage(pdCustSignImage, 115f, 210f, iw*3, ih*3);
					// 기사(수송담당)
					cont.drawImage(pdDrvSignImage, 210f, 210f, iw*3, ih*3);
					// 고객사(인수확인)
					cont.drawImage(pdCustSignImage, 300f, 210f, iw*3, ih*3);
					// 기사(수송담당)
					cont.drawImage(pdDrvSignImage, 385f, 210f, iw*3, ih*3);
					// 고객사(직인)
					cont.drawImage(pdCustSignImage, 370, 70f, iw*3, ih*3);

					//---------------------------------------------------------------------------------------------------------
					// CI 이미지 세팅
					//---------------------------------------------------------------------------------------------------------
					PDImageXObject pdCiImage = PDImageXObject.createFromFile(ciImagePath, doc);
					iw = pdCiImage.getWidth()/5;
					ih = pdCiImage.getHeight()/5;
					cont.drawImage(pdCiImage, 120f, 795f, iw*4, ih*4);
					cont.drawImage(pdCiImage, 175f, 30f, iw*3, ih*3);

					//---------------------------------------------------------------------------------------------------------
					// 선그리기
					//---------------------------------------------------------------------------------------------------------
					// 박스
					PDF_drawRect(cont, Color.BLACK, 120, 70, 360, 660, 1);
					// 가로선 - 출하일자/출하구분
					PDF_drawLine(cont, Color.GRAY, 120, 700, 480, 700, 1f);
					PDF_drawLine(cont, Color.GRAY, 120, 670, 480, 670, 1f);
					// 세로선
					PDF_drawLine(cont, Color.GRAY, 185, 730, 185, 580, 1f);
					PDF_drawLine(cont, Color.GRAY, 300, 730, 300, 670, 1f);
					PDF_drawLine(cont, Color.GRAY, 375, 730, 375, 670, 1f);
					// 가로선 - 품명/황함량/단위/승인수량
					PDF_drawLine(cont, Color.GRAY, 120, 640, 480, 640, 1f);
					PDF_drawLine(cont, Color.GRAY, 120, 610, 480, 610, 1f);
					//PDF_drawLine(cont, Color.GRAY, 120, 580, 480, 580, 1f);
					PDF_drawLine(cont, Color.GRAY, 120, 580, 480, 580, 1f);
					PDF_drawLine(cont, Color.GRAY, 120, 550, 480, 550, 1f);
					PDF_drawLine(cont, Color.GRAY, 120, 500, 480, 500, 1f);
					PDF_drawLine(cont, Color.GRAY, 120, 470, 480, 470, 1f);
					// 세로선
					PDF_drawLine(cont, Color.GRAY, 250, 580, 250, 470, 1f);
					PDF_drawLine(cont, Color.GRAY, 310, 580, 310, 470, 1f);
					PDF_drawLine(cont, Color.GRAY, 370, 580, 370, 500, 1f);


					// y +30하고시작할것..
					// 가로선 - 온도/밀도
					PDF_drawLine(cont, Color.GRAY, 120, 440, 480, 440, 1f);
					PDF_drawLine(cont, Color.GRAY, 120, 410, 480, 410, 1f);
					//PDF_drawLine(cont, Color.GRAY, 120, 380, 480, 380, 1f);
					//PDF_drawLine(cont, Color.GRAY, 120, 290, 480, 290, 1f);
					// 세로선(비고 바닥까지)
					PDF_drawLine(cont, Color.GRAY, 185, 500, 185, 320, 1f);
					// 세로선(중량 바닥까지)
					PDF_drawLine(cont, Color.GRAY, 380, 500, 380, 410, 1f);
					PDF_drawLine(cont, Color.GRAY, 420, 500, 420, 410, 1f);
					// 가로선 - 차량격실~~확인
					PDF_drawLine(cont, Color.GRAY, 120, 320, 480, 320, 1f);
					PDF_drawLine(cont, Color.GRAY, 120, 270, 480, 270, 1f);
					PDF_drawLine(cont, Color.GRAY, 120, 245, 480, 245, 1f);
					PDF_drawLine(cont, Color.GRAY, 120, 210, 480, 210, 1f);
					// 세로선(서명바닥까지)
					PDF_drawLine(cont, Color.GRAY, 300, 320, 300, 210, 1f);
					PDF_drawLine(cont, Color.GRAY, 210, 270, 210, 210, 1f);
					PDF_drawLine(cont, Color.GRAY, 390, 270, 390, 210, 1f);
					// 가로선 - 승인담당
					PDF_drawLine(cont, Color.GRAY, 120, 175, 480, 175, 1f);
					// 세로선 - 승인담당~수송담당
					PDF_drawLine(cont, Color.GRAY, 180, 210, 180, 175, 1f);
					PDF_drawLine(cont, Color.GRAY, 240, 210, 240, 175, 1f);
					PDF_drawLine(cont, Color.GRAY, 300, 210, 300, 175, 1f);
					PDF_drawLine(cont, Color.GRAY, 360, 210, 360, 175, 1f);
					PDF_drawLine(cont, Color.GRAY, 420, 210, 420, 175, 1f);
					// 가로선 - 설명,직인
					PDF_drawLine(cont, Color.GRAY, 120, 130, 480, 130, 1f);
					PDF_drawLine(cont, Color.GRAY, 360, 105, 480, 105, 1f);
					// 세로선 - 명판,직인
					PDF_drawLine(cont, Color.GRAY, 165, 130, 165, 70, 1f);
					PDF_drawLine(cont, Color.GRAY, 360, 130, 360, 70, 1f);

					//---------------------------------------------------------------------------------------------------------
					// 텍스트 세팅
					//---------------------------------------------------------------------------------------------------------
					cont.beginText();
					//cont.setFont(PDType1Font.TIMES_ROMAN, 12);
					String fontPath = System.getProperty("user.dir") + File.separatorChar + "upload" + File.separatorChar + "invoice" + File.separatorChar + "nanumgothic.ttf";
					String fontBoldPath = System.getProperty("user.dir") + File.separatorChar + "upload" + File.separatorChar + "invoice" + File.separatorChar + "nanumgothicbold.ttf";
					PDType0Font font = PDType0Font.load(doc, new FileInputStream(fontPath));
					PDType0Font fontBold = PDType0Font.load(doc, new FileInputStream(fontBoldPath));

					cont.setFont(fontBold, 23);
					cont.setLeading(14.5f);
					cont.newLineAtOffset(220, 770);
					cont.setNonStrokingColor(Color.BLACK);
					cont.showText("출    하    전    표");
					cont.newLine();

					cont.newLineAtOffset(0, -10);
					cont.setNonStrokingColor(Color.DARK_GRAY);
					cont.setFont(fontBold, 17);
					cont.showText("M E T E R  T I C K E T");
					cont.newLine();

					cont.setFont(fontBold, 11);
					cont.newLineAtOffset(-88, -12);
					cont.setNonStrokingColor(Color.BLACK);
					cont.showText("출하일자                                              출하구분");
					cont.newLine();
					cont.setFont(font, 9);
					cont.newLineAtOffset(5, 0);
					cont.showText("DATE                                                                TYPE");
					cont.newLine();

					cont.setFont(fontBold, 11);
					cont.newLineAtOffset(-5, -2);
					cont.setNonStrokingColor(Color.BLACK);
					cont.showText("전표번호                                            수송장비번호");
					cont.newLine();
					cont.setFont(font, 9);
					cont.newLineAtOffset(-5, 0);
					cont.showText("TICKET NO.                                                       EQUIP.NO.");
					cont.newLine();

					cont.setFont(fontBold, 11);
					cont.newLineAtOffset(5, -1);
					cont.setNonStrokingColor(Color.BLACK);
					cont.showText("거래처명");
					cont.newLine();
					cont.setFont(font, 9);
					cont.newLineAtOffset(-5, 0);
					cont.showText("CUSTOMER");
					cont.newLine();

					cont.setFont(fontBold, 11);
					cont.newLineAtOffset(5, -1);
					cont.setNonStrokingColor(Color.BLACK);
					cont.showText("출 하 지");
					cont.newLine();
					cont.setFont(font, 9);
					cont.newLineAtOffset(-9, 0);
					cont.showText("STOCK POINT");
					cont.newLine();

					cont.setFont(fontBold, 11);
					cont.newLineAtOffset(10, -1);
					cont.setNonStrokingColor(Color.BLACK);
					cont.showText("도 착 지");
					cont.newLine();
					cont.setFont(font, 9);
					cont.newLineAtOffset(-5, 0);
					cont.showText("ARR. POINT");
					cont.newLine();

					cont.setFont(fontBold, 11);
					cont.newLineAtOffset(20, -1);
					cont.setNonStrokingColor(Color.BLACK);
					cont.showText("품                명              황 함 량        단  위               승  인  수  량");
					cont.newLine();
					cont.setFont(font, 9);
					cont.newLineAtOffset(0, 0);
					cont.showText("PRODUCT NAME                 SULFER             UNIT             SHIPPING QUANTITY");
					cont.newLine();

					cont.setFont(fontBold, 11);
					cont.newLineAtOffset(-15, -50);
					cont.setNonStrokingColor(Color.BLACK);
					cont.showText("온    도                                밀   도");
					cont.newLine();
					cont.setFont(font, 9);
					cont.newLineAtOffset(2, 0);
					cont.showText("TEMP                                         DENSITY                                     NO.");
					cont.newLine();

					cont.setFont(font, 9);
					cont.newLineAtOffset(252, 27);
					cont.showText("CARD");
					cont.newLineAtOffset(-247, -27);

					cont.setFont(fontBold, 11);
					cont.newLineAtOffset(-15, -2);
					cont.setNonStrokingColor(Color.BLACK);
					cont.showText("  환산수량");
					cont.newLine();
					cont.setFont(font, 7);
					cont.newLineAtOffset(0, 2);
					cont.showText("CONVERITED QTY");
					cont.newLine();

					cont.setFont(font, 8);
					cont.newLineAtOffset(257, 20);
					cont.showText("TANK NO.");
					cont.newLineAtOffset(-257, -20);

					cont.setFont(fontBold, 11);
					cont.newLineAtOffset(11, -4);
					cont.setNonStrokingColor(Color.BLACK);
					cont.showText("증 기 압");
					cont.newLine();
					cont.setFont(fontBold, 11);
					cont.newLineAtOffset(0, 2);
					cont.showText("유 동 점");
					cont.newLine();


					cont.setFont(fontBold, 11);
					cont.newLineAtOffset(248, 27);
					cont.showText("중   량");
					cont.newLine();
					cont.setFont(font, 10);
					cont.newLineAtOffset(0, 3);
					cont.showText(" (KG)");
					cont.newLineAtOffset(-250, -27);

					cont.setFont(fontBold, 11);
					cont.newLineAtOffset(0, -20);
					cont.setNonStrokingColor(Color.BLACK);
					cont.showText("비     고");
					cont.newLine();
					cont.setFont(font, 7);
					cont.newLineAtOffset(0, 2);
					cont.showText("REMARKS");
					cont.newLine();

					cont.setFont(fontBold, 13);
					cont.newLineAtOffset(10, -45);
					cont.showText("차량격실 유종, 수량확인");
					cont.newLine();
					cont.showText(" 하역 후 격실 잔량 확인");
					cont.newLine();

					cont.setFont(fontBold, 13);
					cont.newLineAtOffset(215, 25);
					cont.showText("주입구 확인");
					cont.newLine();

					cont.setFont(fontBold, 12);
					cont.newLineAtOffset(-218, -24);
					cont.showText("인수확인              수송담당             인수확인             수송담당");
					cont.newLine();

					cont.setFont(fontBold, 12);
					cont.newLineAtOffset(-15, -50);
					cont.showText("승인담당                       출하담당                       수송담당");
					cont.newLine();

					cont.setFont(font, 10);
					cont.newLineAtOffset(45, -20);
					cont.showText("본 제품은 구매인의 자발적인 주문에 의한 것임을 확인합니다.");
					cont.newLine();
					cont.newLineAtOffset(-43, 0);
					cont.showText("만약 본인의 주문 내용과 인수제품 내용이 다를 경우에는 즉시 반품 요청하겠습니다.");

					cont.setFont(fontBold, 12);
					cont.newLineAtOffset(10, -28);
					cont.showText("명                                                                          직    인");
					cont.newLine();
					cont.newLineAtOffset(0, -15);
					cont.showText("판");
					cont.newLine();

					cont.setFont(fontBold, 19);
					cont.newLineAtOffset(135, -25);
					cont.showText("현대오일뱅크주식회사");
					cont.newLine();

					cont.newLineAtOffset(-83, 682);
					//---------------------------------------------------------------------------------------------------------
					// 데이타 추가
					//---------------------------------------------------------------------------------------------------------

					cont.setNonStrokingColor(Color.BLUE);
					cont.setLeading(14.5f);

					String PSTDAT = String.format("%s", jobjectStore.get("PSTDAT").getAsString());
					String PSTTIM = String.format("%s", jobjectStore.get("PSTTIM").getAsString());
					String BATXT = String.format("%s", jobjectStore.get("BATXT").getAsString());
					cont.setFont(fontBold, 10);
					cont.newLineAtOffset(20, 6);
					cont.showText(PSTDAT);
					cont.newLineAtOffset(10, -12);
					cont.showText(PSTTIM);
					cont.newLineAtOffset(-30, 6);

					cont.setFont(fontBold, 11);
					cont.newLineAtOffset(200, 0);
					cont.showText(BATXT);
					cont.newLine();
					cont.newLineAtOffset(-200, -8);
					// ----------------------------------------
					String SHNUMBER = String.format("%.0f", Double.parseDouble(jobjectStore.get("SHNUMBER").getAsString()));
					String VBELN = String.format("%.0f", Double.parseDouble(jobjectStore.get("VBELN").getAsString()));
					String VEH_TEXT = String.format("%s", jobjectStore.get("VEH_TEXT").getAsString());

					cont.setFont(fontBold, 11);
					cont.newLineAtOffset(0, 0);
					cont.showText("        " + SHNUMBER);
					cont.newLine();
					cont.showText("     (" + VBELN + ")");

					cont.setFont(fontBold, 11);
					cont.newLineAtOffset(200, 8);
					cont.showText(VEH_TEXT);
					cont.newLine();
					cont.newLineAtOffset(-200, -8);

					// ----------------------------------------
					String KUNAG_T = String.format("%s", jobjectStore.get("KUNAG_T").getAsString());
					cont.setFont(fontBold, 11);
					cont.newLineAtOffset(0, -10);
					cont.showText(KUNAG_T);
					cont.newLine();

					// ----------------------------------------
					String WERKS_T = String.format("%s", jobjectStore.get("WERKS_T").getAsString());
					String TAS_DOCNO = String.format("%s", jobjectStore.get("TAS_DOCNO").getAsString());
					cont.setFont(fontBold, 11);
					cont.newLineAtOffset(0, -12);
					cont.showText(WERKS_T + "    " + TAS_DOCNO);
					cont.newLine();

					// ----------------------------------------
					String KUNNR_T = String.format("%s", jobjectStore.get("KUNNR_T").getAsString());
					String CITY1 = String.format("%s", jobjectStore.get("CITY1").getAsString());
					String STREET = String.format("%s", jobjectStore.get("STREET").getAsString());
					cont.setFont(fontBold, 11);
					cont.newLineAtOffset(0, -10);
					cont.showText(KUNNR_T);
					cont.newLine();
					cont.showText(CITY1 + " " + STREET);
					cont.newLine();

					// ----------------------------------------
					String MAKTX = String.format("%s", jobjectStore.get("MAKTX").getAsString());
					String SULFUR_T = String.format("%s", jobjectStore.get("SULFUR_T").getAsString());
					String VRKME = String.format("%s", jobjectStore.get("VRKME").getAsString());
					String ADQNT_T = String.format("%s", jobjectStore.get("ADQNT_T").getAsString());
					cont.setFont(fontBold, 11);
					cont.newLineAtOffset(-20, -45); cont.showText(MAKTX);
					cont.newLineAtOffset(85, 0); cont.showText(SULFUR_T);
					cont.newLineAtOffset(75, 0); cont.showText(VRKME);
					cont.newLineAtOffset(75, 0); cont.showText(ADQNT_T);
					cont.newLine();

					// ----------------------------------------
					String MTTMP_T = String.format("%s", jobjectStore.get("MTTMP_T").getAsString());
					String TDICH_T = String.format("%s", jobjectStore.get("TDICH_T").getAsString());
					String CARDNO01 = String.format("%s", jobjectStore.get("CARDNO01").getAsString());

					cont.setFont(fontBold, 11);
					cont.newLineAtOffset(-200, -29); cont.showText(MTTMP_T);
					cont.newLineAtOffset(120, 0); cont.showText(TDICH_T);
					cont.newLineAtOffset(110, 0); cont.showText(CARDNO01);
					cont.newLine();

					// ----------------------------------------
					String EXQTY_T = String.format("%s", jobjectStore.get("EXQTY_T").getAsString());
					String LGOBE = String.format("%s", jobjectStore.get("LGOBE").getAsString());

					cont.setFont(fontBold, 11);
					cont.newLineAtOffset(-240, -16); cont.showText(EXQTY_T);
					cont.newLineAtOffset(240, 0); cont.showText(LGOBE);
					cont.newLine();

					// ----------------------------------------
					String GALLON_T = String.format("%s", jobjectStore.get("GALLON_T").getAsString());
					String WEIGHT_T = String.format("%s", jobjectStore.get("WEIGHT_T").getAsString());

					cont.setFont(fontBold, 11);
					cont.newLineAtOffset(-240, -16); cont.showText(GALLON_T);
					cont.newLineAtOffset(240, 0); cont.showText(WEIGHT_T);
					cont.newLine();

					// ----------------------------------------
					String TXT01 = String.format("%s", jobjectStore.get("TXT01").getAsString());
					String TXT02 = String.format("%s", jobjectStore.get("TXT02").getAsString());
					String TXT03 = String.format("%s", jobjectStore.get("TXT03").getAsString());
					String TXT04 = String.format("%s", jobjectStore.get("TXT04").getAsString());
					String TXT05 = String.format("%s", jobjectStore.get("TXT05").getAsString());
					String TXT06 = String.format("%s", jobjectStore.get("TXT06").getAsString());
					String TXT07 = String.format("%s", jobjectStore.get("TXT07").getAsString());
					String TXT08 = String.format("%s", jobjectStore.get("TXT08").getAsString());
					String TXT09 = String.format("%s", jobjectStore.get("TXT09").getAsString());
					String TXT10 = String.format("%s", jobjectStore.get("TXT10").getAsString());
					String TXT11 = String.format("%s", jobjectStore.get("TXT11").getAsString());
					String TXT12 = String.format("%s", jobjectStore.get("TXT12").getAsString());

					//TXT01 = "착색제 수령 확인 : (수송담당 서명)";
					//TXT03 = "착색제 투입 확인 : (입고 담당자 서명)";

					if (TXT01.indexOf("착색제")>=0) {
						// 착색제라는 단어가 있다면...서명위치와 보기 적당한..값으로 대체..
						TXT02 = TXT01;
						TXT12 = TXT03;
						// 기존값은 비움.
						TXT01 = ""; TXT03 = "";
					}

					cont.newLineAtOffset(-100, 6);

					cont.setFont(fontBold, 9);
					cont.newLineAtOffset(-140, -12); cont.showText(TXT01);
					cont.newLineAtOffset(140, 0); cont.showText(TXT04);

					cont.newLineAtOffset(-140, -12); cont.showText(TXT02);
					cont.newLineAtOffset(140, 0); cont.showText(TXT05);

					cont.newLineAtOffset(-140, -12); cont.showText(TXT03);
					cont.newLineAtOffset(140, 0); cont.showText(TXT06);

					cont.newLineAtOffset(-140, -12); cont.showText(TXT09);
					cont.newLineAtOffset(140, 0); cont.showText(TXT07);

					cont.newLineAtOffset(-140, -12); cont.showText(TXT12);
					cont.newLineAtOffset(140, 0); cont.showText(TXT08);

					cont.newLineAtOffset(-140, -12); cont.showText("");
					cont.newLineAtOffset(140, 0); cont.showText(TXT10);

					cont.newLineAtOffset(-140, -12); cont.showText("");
					cont.newLineAtOffset(140, 0); cont.showText(TXT11);
					cont.newLine();

					// ----------------------------------------
					String NAME_LAST = String.format("%s", jobjectStore.get("NAME_LAST").getAsString());
					String NAME_FIRST = String.format("%s", jobjectStore.get("NAME_FIRST").getAsString());
					String LAST_NAME = String.format("%s", jobjectStore.get("LAST_NAME").getAsString());
					String FIRST_NAME = String.format("%s", jobjectStore.get("FIRST_NAME").getAsString());

					cont.setFont(fontBold, 12);
					cont.newLineAtOffset(-140, -125); cont.showText(NAME_LAST);
					cont.newLineAtOffset(120, 0); cont.showText(NAME_FIRST);
					cont.newLineAtOffset(120, 0); cont.showText(LAST_NAME + FIRST_NAME);
					cont.newLine();

					// ----------------------------------------
					cont.setFont(fontBold, 12);
					cont.newLineAtOffset(-265, -65);
					cont.showText(KUNNR_T);
					cont.newLine();
					cont.newLineAtOffset(0, -5);
					cont.showText(CITY1 + " " + STREET);
					cont.newLine();

					cont.endText();
				}
// 문서열기시 암호..
//				AccessPermission accessPermission = new AccessPermission();
//				StandardProtectionPolicy spp = new StandardProtectionPolicy("4426","4426",accessPermission);
//				doc.protect(spp);


				doc.save(pdfDir);


			}
		} catch (Exception e) {
			System.out.println("Exception! : " + e.getMessage());
		}

		try {
			doc.close();
			result = "success";
		} catch (IOException e) {
			result = "error";
			e.printStackTrace();
		}

		return result;
	}


	/**
	 * PDF에 사각형그리기
	 * @param cont
	 * @param color
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param fLineWidth
	 */
	public static void PDF_drawRect(PDPageContentStream cont, Color color, int x, int y, int width, int height, float fLineWidth) {
		try {
			Rectangle rect = new java.awt.Rectangle(x, y, width, height);
			cont.addRect(rect.x, rect.y, rect.width, rect.height);

			if (false) {
				cont.setNonStrokingColor(color);
				cont.fill();
			} else {
				cont.setLineWidth(fLineWidth);
				cont.setStrokingColor(color);
				cont.stroke();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void PDF_drawLine(PDPageContentStream cont, Color color, int x, int y, int width, int height, float fLineWidth) {
		try {
			Rectangle rect = new java.awt.Rectangle(x, y, width, height);
			cont.addLine(rect.x, rect.y, rect.width, rect.height);

			if (false) {
				cont.setNonStrokingColor(color);
				cont.fill();
			} else {
				cont.setLineWidth(fLineWidth);
				cont.setStrokingColor(color);
				cont.stroke();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}