package com.gzonesoft.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileInfoResponse {
	@JsonProperty("resultCode")
	public String resultCode = "";
	
	@JsonProperty("resultMsg")
	public String resultMsg = "";
	
	@JsonProperty("resultData")
	public List<FileDetail> fileDetails = new ArrayList<FileDetail>();

	public static class FileDetail{
		@JsonProperty("FILE_NO")
		public String fileNo = "";

		@JsonProperty("SIGN_FILE_CD")
		public String signFileCd = "";
		@JsonProperty("SIGN_FILE_PATH")
		public String signFilePath = "";

		@JsonProperty("INVOICE_FILE_CD")
		public String invoiceFileCd = "";
		@JsonProperty("INVOICE_FILE_PATH")
		public String invoiceFilePath = "";
	}
}
