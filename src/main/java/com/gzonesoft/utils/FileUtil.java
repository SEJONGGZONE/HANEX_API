package com.gzonesoft.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/***
	 * 파일 경로에서 파일목록 추출
	 * @param strPath
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<Hashtable<String, String>> getFileList(String strPath){
		ArrayList<Hashtable<String, String>> list = new ArrayList<Hashtable<String, String>> ();
		
		try{

		File myDir = new File(strPath);
		File[] files_t = myDir.listFiles();
		files_t = sortFileList(files_t, "FILE_NAME");

		for(int i=0; i<files_t.length; i++){
			Hashtable<String, String> ht = new Hashtable<String, String>();
			//files_t[i].setReadOnly(); //읽기전용
			ht.put("FILE_NAME", files_t[i].getName()); //파일명
			ht.put("IS_DIRECTORY", files_t[i].isDirectory() ? "True":"False"); //디렉토리 or 파일
			
			
			//ht.put("LAST_MODIFIED", getLongToDate(files_t[i].lastModified(),"yyyy/MM/dd HH:mm:ss")); //마지막 수정한 날짜

			//ht.put("CAN_WRITE", files_t[i].canWrite() ? "True":"False"); //쓰기 가능 유무
			//------------------ht.put("FILE_SIZE", Long.toString(files_t[i].length()/1000)); //파일사이즈
			//ht.put("FILE_SIZE", "0"); //파일사이즈
			ht.put("PARENT_PATH", files_t[i].getParent()); //파일명을 뺀 상위경로

			//System.out.print("["+(i+1)+"]FileName:"+files_t[i].getName());
			//System.out.print(" FILE_SIZE:"+Long.toString(files_t[i].length()));
			//System.out.print(" CanWrite :"+ (files_t[i].canWrite() ? "True":"False"));
			
			//System.out.println(" PARENT_PATH:"+files_t[i].getParent());
			//System.out.print(" ABSOLUTE_PATH:"+files_t[i].getAbsolutePath());
			//System.out.print(" CURRENT_PATH:"+files_t[i].getPath());
			//System.out.print(" CANONICAL_PATH:"+files_t[i].getCanonicalPath());
			
			list.add(ht);	
		}
		}catch(Exception ex){
			logger.error(ex.toString());
		}
		return list;
	}
	/***
	 * 파일정보 추출
	 * @param strPath
	 * @return
	 * @throws Exception
	 */
	public Hashtable<String, String> getFileInfo(String strPath){
		Hashtable<String, String> ht = new Hashtable<String, String>();
		
		try{
			File filepath = new File(strPath);
			
			
			ht.put("FILE_NAME", filepath.getName()); //파일명
			ht.put("IS_DIRECTORY", filepath.isDirectory() ? "True":"False"); //디렉토리 or 파일
			//ht.put("LAST_MODIFIED", getLongToDate(filepath.lastModified(),"yyyy/MM/dd HH:mm:ss")); //마지막 수정한 날짜

			//ht.put("CAN_WRITE", files_t[i].canWrite() ? "True":"False"); //쓰기 가능 유무
			//ht.put("FILE_SIZE", Long.toString(filepath.length()/1000)); //파일사이즈
			ht.put("PARENT_PATH", filepath.getParent()); //파일명을 뺀 상위경로
			//filepath.getAbsolutePath()); //파일 절대경로
			//filepath.getPath()); //전체경로
			//filepath.getCanonicalPath()); //상대경로		
		}catch(Exception ex){
			logger.error(ex.toString());
		}
		
		return ht;
	}
	/***
	 * 파일 정렬
	 * @param files
	 * @param compareType
	 * @return
	 */
	public static File[] sortFileList(File[] files, final String compareType) {

		Arrays.sort(files, new Comparator<Object>() {
			public int compare(Object object1, Object object2) {

				String s1 = "";
				String s2 = "";

				if (compareType.equals("FILE_NAME")) {
					s1 = ((File) object1).getName();
					s2 = ((File) object2).getName();
				} else if (compareType.equals("DATE")) {
					s1 = ((File) object1).lastModified() + "";
					s2 = ((File) object2).lastModified() + "";
				}

				return s1.compareTo(s2);

			}
		});

		return files;
	}
 
	public static String getLongToDate(long datetime, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(datetime);
		String date = sdf.format(cal.getTime());

		return date;
	}
	
	/**
	 * 파일 생성 및 내용 쓰기(이어쓰기)
	 * @param filePath
	 * @param strText
	 */
	public static void fileWiter(String filePath, String fileName, String strText){
		try{
			File filepath = new File(filePath);
			
			if(!filepath.exists()){
				filepath.mkdirs();
			}
			
			BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath+File.separator+fileName, true), "UTF8"));
			output.write(strText);
			output.close();
		}catch(Exception ex){
			logger.error(ex.toString());
		}
	}
	
	public static String fileReader(String filePath) {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			String line;
	        while ((line = br.readLine()) != null) {
                sb.append(line);
            }
		}catch(Exception ex) {
			logger.error(ex.toString());
		}finally {
            if(br != null) try {br.close(); } catch (Exception e) {}
        }
		return sb.toString();
	}
	
	/*
	public static void main(String[] args) throws Exception {
		FileUtil fileUtil = new FileUtil();
		ArrayList<Hashtable<String,String>> fileList = fileUtil.getFileList("\\경로");
		//ArrayList<Hashtable<String,String>> fileList = fileUtil.getFileList("\\\\192.168.2.51\\대외업무");
		for(int i=0; i < fileList.size(); i++){
			Hashtable<String,String> fileInfo = (Hashtable<String,String>)fileList.get(i);
			for(Enumeration<String> e = fileInfo.keys(); e.hasMoreElements();){
				Object key = e.nextElement();
				Object value = fileInfo.get(key);
				
				logger.debug("KEY:"+key+"  VALUE:"+value);
			}
		}
	}
	*/
}
