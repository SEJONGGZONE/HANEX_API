package com.gzonesoft.utils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.google.gson.Gson;

public class SysUtils {
	
	public static ByteBuffer bufferSlice(ByteBuffer buffer, int position, int size) {
        ByteBuffer tmp = buffer.duplicate();
        tmp.position(position).limit(position + size);
        return tmp.slice();

    }

	public static BigInteger readUnsignedInteger(ByteBuffer buffer, int length, boolean littleEndian) {
	    return new BigInteger(1, readBytes(buffer, length, littleEndian));
	}

	public static BigInteger readSignedInteger(ByteBuffer buffer, int length, boolean littleEndian) {
	    return new BigInteger(readBytes(buffer, length, littleEndian));
	}

	public static byte[] readBytes(ByteBuffer buffer, int length, boolean reversed) {
	    byte[] bytes = new byte[length];
	    for (int i = 0; i < length; i++) {
	        bytes[reversed ? length - 1 - i : i] = buffer.get();
	    }
	    return bytes;
	}
	
	public static String bufferToString(ByteBuffer buff){
		StringBuilder sb = new StringBuilder();
		
		char ch;
		while(buff.remaining() > 0 && !Character.isWhitespace(ch = (char)buff.get())){
			sb.append(ch);
		}

		return new String(sb);
	}
	
	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}

		byte[] ba = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return ba;
	}

	public static byte[] HexStrToByte(String str) {
		// "10213F..." -> [10,21,3F,..]
		if (str == null || str.length() == 0)
			return new byte[0];
		int len = (str.length() >> 1);
		byte[] data = new byte[len];
		for (int i = 0; i < len; i++)
			data[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
		return data;
	}

	private static void appendBytes(StringBuffer stringBuffer, byte int8) {
		stringBuffer.append(Character.forDigit(((int8 >> 4) & 0x0F), 16));
		stringBuffer.append(Character.forDigit((int8 & 0x0F), 16));
	}

	// [10,21,3F,..] -> "10213F..."
	public static String ByteToHexStr(byte[] data) {
		if (data == null || data.length == 0)
			return "";

		StringBuffer sb = new StringBuffer(2 * data.length);
		for (short i = 0; i < data.length; i++)
			appendBytes(sb, data[i]);

		return sb.toString();
	}
	
	public static String getToday() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String dateText = dateFormat.format(now);
		return dateText;
	}

	public static String getToday(String strFormat) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
		String dateText = dateFormat.format(now);
		return dateText;
	}

	public static String getCurrentTime() {
		Date now = new Date();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");// new
		// SimpleDateFormat("hh:mm:ss");
		String timeText = timeFormat.format(now);
		return timeText;
	}

	public static String getCurrentTime(String strFormat) {
		Date now = new Date();
		SimpleDateFormat timeFormat = new SimpleDateFormat(strFormat);// new
		// SimpleDateFormat("hh:mm:ss");
		String timeText = timeFormat.format(now);
		return timeText;
	}
	
	public static String stringToEasyEye_DateTime(String date )
	{
		if (date == null) return "";
		Calendar cal = calendarFromString(date);
		return stringSimpleFromCalendar(cal);
	}
	
	public static long getCurrentTimeStamp(){

		//ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneOffset.UTC);
		Timestamp timestamp = Timestamp.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant());
		//Date date = Date.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant());
		
		return timestamp.getTime();
	}

	/**
	 * 
	 * @param date
	 * new Date()
	 * @param format
	 * "yyyy-MM-dd HH:mm:ss"
	 * @param timezone
	 * "UTC"
	 * @return
	 */
	public static String getFormatDateToString(Date date, String format, String timezone){
		SimpleDateFormat timeFormat = new SimpleDateFormat(format);
		
		timeFormat.setTimeZone(TimeZone.getTimeZone(timezone));

		String timeText = timeFormat.format(date);
		
		return timeText;
	}
	
	/*
	public static Date getStringToDate2(String strDt, String format){
		Date date = null;
		try {
			DateTimeFormatter patternFormat = DateTimeFormat.forPattern(format);
			LocalDateTime dt = patternFormat.parseLocalDateTime(strDt);
			date = dt.toDate();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return date;
	}
	
	public static Properties getStringToDateFormat(String strDt, String srcformat){
		Properties pt = new Properties();
		try {
			DateTimeFormatter patternFormat = DateTimeFormat.forPattern(srcformat);

			pt.put("type1", patternFormat.parseLocalDateTime(strDt));//2018-04-20T09:06:08.000
			pt.put("type2", patternFormat.parseLocalDate(strDt));//2018-04-20
			pt.put("type3", patternFormat.parseDateTime(strDt));//2018-04-20T09:06:08.000+09:00
	
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return pt;
	}	
	*/
	public static String getCurrentTimeISO() {
		DateTime dt = DateTime.now(DateTimeZone.UTC);
		
		DateTimeFormatter isoFormat = ISODateTimeFormat.dateTime();
		
		return dt.toString(isoFormat);
	}
	
	/**
	 * 
	 * @param strDt
	 * "2018-03-09 10:12:21"
	 * @param format
	 * yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static Date getStringToDate(String strDt, String format){
		SimpleDateFormat transFormat = new SimpleDateFormat(format);

		Date date = null;
		try {
			date = transFormat.parse(strDt);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return date;
	}
	
	/**
	 * 
	 * @param date
	 * new Date()
	 * @param format
	 * yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getDateToString(Date date, String format){
		SimpleDateFormat transFormat = new SimpleDateFormat(format);
		String timeText = transFormat.format(date);
		return timeText;
	}
	
	public static String getStringSimpleDateFormat(String strDt, String preformat, String newFormat){
		SimpleDateFormat transPreFormat = new SimpleDateFormat(preformat);
		SimpleDateFormat transNewFormat = new SimpleDateFormat(newFormat);

		try {
			Date date = transPreFormat.parse(strDt);
			strDt = transNewFormat.format(date);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return strDt;
	}	
	
	/*
		System.out.println(SysUtils.getStringJodaDateFormat("2017-11-09T20:12:00+07:00", "yyyy-MM-dd'T'HH:mm:ssZZ", "yyyy-MM-dd HH:mm:ss"));
		System.out.println(SysUtils.getStringJodaDateFormat("2017-11-09 22:12:00", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ssZZ"));
		System.out.println(SysUtils.getStringJodaDateFormat("2017-11-09 22:12:00", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ssZZ"));
	 */
	public static String getStringJodaDateFormat(String preDt, String preformat, String newFormat){

		try {
			DateTimeFormatter transPreFormat = DateTimeFormat.forPattern(preformat);
			DateTimeFormatter transNewFormat = DateTimeFormat.forPattern(newFormat);

			//System.out.println(transPreFormat.parseLocalDateTime(strDt));//2018-04-20T09:06:08.000
			//System.out.println(transPreFormat.parseLocalDate(strDt));//2018-04-20
			//System.out.println(transPreFormat.parseDateTime(strDt));//2018-04-20T09:06:08.000+09:00
	
			DateTime dateTime = transPreFormat.parseDateTime(preDt);
			
			//DateTimeZone.UTC을 사용하지 않으면 한국 시간으로 변경된다. UTC타임을 그대로 사용
			//transNewFormat = transNewFormat.withZone(DateTimeZone.UTC); //UTC타임 시간으로 추출
			
			//한국 시간으로 변환된다.
			preDt = dateTime.toString(transNewFormat);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return preDt;
	}	
	
	/**
	 * 포멧후 시간 더하기
	 * @param preDt
	 * @param preformat
	 * @param strType
	 * @param offset
	 * @param newFormat
	 * @return
	 */
	public static String getStringJodaDateAddFormat(String preDt, String preformat, String strType, int offset, String newFormat){

		try {
			//preDt.plusSeconds(1);
			DateTimeFormatter transPreFormat = DateTimeFormat.forPattern(preformat);
			DateTimeFormatter transNewFormat = DateTimeFormat.forPattern(newFormat);

			//System.out.println(transPreFormat.parseLocalDateTime(strDt));//2018-04-20T09:06:08.000
			//System.out.println(transPreFormat.parseLocalDate(strDt));//2018-04-20
			//System.out.println(transPreFormat.parseDateTime(strDt));//2018-04-20T09:06:08.000+09:00
	
			//String -> DateTime으로 변환
			DateTime preDateTime = transPreFormat.parseDateTime(preDt);
			DateTime newDateTime = null;
			//시간 더하기
			if("Days".equals(strType)) {
				newDateTime = preDateTime.plusDays(offset);
			}
			if("Hours".equals(strType)) {
				newDateTime = preDateTime.plusHours(offset);
			}
			if("Minutes".equals(strType)) {
				newDateTime = preDateTime.plusMinutes(offset);
			}
			if("Seconds".equals(strType)) {
				newDateTime = preDateTime.plusSeconds(offset);
			}

			//DateTime -> String 변환
			preDt = newDateTime.toString(transNewFormat);
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return preDt;
	}	
	
	public static Date getDateJodaDateFormat(String strDt, String format){

		Date dt = null;
		try {
			DateTimeFormatter transPreFormat = DateTimeFormat.forPattern(format);
	
			DateTime dateTime = transPreFormat.parseDateTime(strDt);
			dt = dateTime.toDate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return dt;
	}	
	
	/**
     * 캘린더 객체를 yyyy-MM-dd HH:mm:ss 형태의 문자열로 변환합니다.
     * 
     * @param cal 캘린더 객체
     * @return 변환된 문자열
     */
    public static String stringFromCalendar(Calendar cal)
    {
        // 날짜를 통신용 문자열로 변경
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(cal.getTime());
    }
     
    /**
     * 캘린더 객체를 yyyy-MM-dd형태의 문자열로 변환합니다.
     * 
     * @param cal 캘린더 객체
     * @return 변환된 문자열
     */
    public static String stringSimpleFromCalendar(Calendar cal)
    {
        // 날짜를 통신용 문자열로 변경
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(cal.getTime());
    }
     
    /**
     * yyyy-MM-dd HH:mm:ss 형태의 문자열을 캘린더 객체로 변환합니다.
     * 만약 변환에 실패할 경우 오늘 날짜를 반환합니다.
     * 
     * @param date 날짜를 나타내는 문자열
     * @return 변환된 캘린더 객체
     */
    public static Calendar calendarFromString(String date)
    {
        Calendar cal = Calendar.getInstance();
         
        try
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
            cal.setTime(formatter.parse(date));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return cal;
    }
     
    /**
     * yyyy-MM-dd 형태의 문자열을 캘린더 객체로 변환합니다.
     * 만약 변환에 실패할 경우 오늘 날짜를 반환합니다.
     * 
     * @param date 날짜를 나타내는 문자열
     * @return 변환된 캘린더 객체
     */
    public static Calendar calendarFromStringSimple(String date)
    {
        Calendar cal = Calendar.getInstance();
         
        try
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            cal.setTime(formatter.parse(date));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return cal;
    }
    
	/**
	 * Object를 Json으로 변환합니다.
	 * @param gpsData
	 */
	public static String createObjectToJson(Object obj){
		//JSONObject jsonObj = new JSONObject();
		Gson gson = new Gson();
		String json = gson.toJson(obj);
		return json;
	}
	/**
	 * Json을 Object로 변환합니다.
	 * @param str
	 * @param cls
	 * @return
	 */
	public static Object createJsonToObject(String str, Class<?> cls){
		Gson gson = new Gson();
		Object obj = gson.fromJson(str, cls);
		return obj;
	}
	
	/**
	원 문자열의 값이 null인경우 해당 문자열로 치환한다.
	@param strSrcData - 원 문자열
	@param strReplaceData - 치환될 문자열
	@return java.lang.String
	*/
	public static String getNvlStr(String strSrcData, String strReplaceData) {
		if ( (strSrcData == null) || (strSrcData.equals("")) ) {
			return strReplaceData;
		}
		return strSrcData;
	}
	
	/**
	 * 두 날짜 차이
	 * 부킹닷컴은 시작날짜로부터 ~몇일 형태를 사용하기 때문에...
	 * @param begin
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public static long diffOfDay(String begin, String end) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		Date beginDate = formatter.parse(begin);
		Date endDate = formatter.parse(end);

		long diff = endDate.getTime() - beginDate.getTime();
		long diffDays = diff / (24 * 60 * 60 * 1000);

		return diffDays;
	}
	
	/**
	 * 국가 코드를 입력받아 국가명을 되돌린다.
	 * @param strCode
	 * @return
	 */
	public static String getCountryName(String strCode) {
		String rVal = strCode;
		String[] locales = Locale.getISOCountries();
		
		for (String countryCode : locales) {
			Locale obj = new Locale("", countryCode);
			//System.out.println("Country Code = " + obj.getCountry() + ", Country Name = " + obj.getDisplayCountry(Locale.ENGLISH));
			if(obj.getCountry().equals(strCode.toUpperCase())) {
				rVal = obj.getDisplayCountry(Locale.ENGLISH);
				break;
			}
		}
		
		return rVal;
	}
	
	/**
	 * 숫자형태의 유니크 문자열
	 * @param len
	 * @return
	 */
    public static String getNumericUniqueNumber(int len)
    {
	    int leftLimit = 48; // letter '0'
	    int rightLimit = 57; // letter '9'
	    int targetStringLength = len;
	    Random random = new Random();

	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();

	    return generatedString;
    }	
    
    /**
     * 알파벳 형태의 유니크 문자열
     * @param len
     * @return
     */
    public static String getAlphaUniqueNumber(int len)
    {
	    int leftLimit = 65; // letter 'A'
	    int rightLimit = 90; // letter 'Z'
	    int targetStringLength = len;
	    Random random = new Random();

	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();

	    return generatedString;
    }


	/**
	 * SHA - 인크립트
	 * @param text
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String encryptSHA256(String text) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(text.getBytes());

		return bytesToHex(md.digest());
	}

	/**
	 * MD5 - 인크립트
	 * @param text
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String encryptMD5(String text) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(text.getBytes());

		return bytesToHex(md.digest());
	}


	private static String bytesToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for (byte b : bytes) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}

}
