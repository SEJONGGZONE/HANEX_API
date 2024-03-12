package com.gzonesoft.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringJoiner;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.gzonesoft.utils.LogUtil;
import com.gzonesoft.utils.SysUtils;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;

/**

 * 공통 Data access object(DAO)
 * <pre>
 * <b>상세설명:</b>
 * 일반적인 경우(Select, Update, Delete)가 아닌 특별한 동작을 원할 경우 추가하세요
 * </pre>
 * <pre>
 * <b>History:</b>
 *    박대근, 1.0, 2017.12.27 최초생성
 * </pre>
 *
 * @author 박대근
 * @version 1.0,2017.12.27 최초생성
 */
@Repository("commonDAO")
public class CommonDAO {
	@Resource(name="db1DataSourceFormatter") private Log4jdbcProxyDataSource db1DataSourceFormatter;

	public Object runProcedure(String procName, LinkedHashMap<String, Object> params) {
		Connection con = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		
		String resultCd = "00";
		String resultMsg = "";
		List<LinkedHashMap<String, Object>> resultData = null;
		
		LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
		
		try {
			con = db1DataSourceFormatter.getConnection();
			
			// ----------------------------------
			// 파라미터 개수만큼 ? 만들기
			// ----------------------------------
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < params.size() + 3; i++) { //마지막 Out 파라미터 3개 추가
				stringBuilder.append("?,");
			}
			
			String sttInputParam = "{call " + procName + "("+ stringBuilder.substring(0, stringBuilder.length() - 1) + ")}"; //마지막 콤마 제거
			LogUtil.infoLog(String.format("[%s] DB쿼리 요청 => %s : %s ", SysUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss"), sttInputParam, params));
			
			cstmt = con.prepareCall(sttInputParam);

			for (String mapkey : params.keySet()){
				cstmt.setString(mapkey, params.get(mapkey).toString());
			}

			// ----------------------------------
			// Out Param 설정
			// ----------------------------------
			cstmt.registerOutParameter("RTN_CD", java.sql.Types.VARCHAR);
			cstmt.registerOutParameter("RTN_MSG", java.sql.Types.VARCHAR);
			cstmt.registerOutParameter("RTN_CURSOR", java.sql.Types.REF_CURSOR);
			
			cstmt.executeQuery();
			resultCd = cstmt.getString("RTN_CD");
			resultMsg = cstmt.getString("RTN_MSG");
			rs = (ResultSet)cstmt.getObject("RTN_CURSOR");
			
			if(rs != null) {
		        // ResultSet 의 MetaData를 가져온다.
		        ResultSetMetaData metaData = rs.getMetaData();
		        // ResultSet 의 Column의 갯수를 가져온다.
		        int sizeOfColumn = metaData.getColumnCount();
	
		        LinkedHashMap<String, Object> map;
		        String column;
				
	
		        resultData = new ArrayList<LinkedHashMap<String, Object>>();
		        StringJoiner strJoiner = new StringJoiner(",");
		        while (rs.next())
		        {
		            // 내부에서 map을 초기화
		            map = new LinkedHashMap<String, Object>();
		            // Column의 갯수만큼 회전
		            for (int indexOfcolumn = 0; indexOfcolumn < sizeOfColumn; indexOfcolumn++)
		            {
		                column = metaData.getColumnName(indexOfcolumn + 1);
		                // map에 값을 입력 map.put(columnName, columnName으로 getString)
		                map.put(column, rs.getString(column));
		            }
		            // list에 저장
		            resultData.add(map);
		            strJoiner.add(map.toString());
		        }
				if (procName.equals("DIP_LOGIN.LOGIN_REQ")) {
					LogUtil.infoLog(String.format("%s 결과 => %s ", procName, "결과 PASS.."));
				} else {
					LogUtil.infoLog(String.format("%s 결과 => %s ", procName, strJoiner.toString()));
				}
		        LogUtil.infoLog(String.format("-------------------------------------------------------------"));
			}
			
	        resultMap.put("outResultCd", resultCd);
	        resultMap.put("outResultMsg", resultMsg);
	        resultMap.put("outCursor", resultData);

		} catch (Exception e) {
	        resultMap.put("outResultCd", "01");
	        resultMap.put("outResultMsg", e.toString());
	        resultMap.put("outCursor", null);
	        
			LogUtil.errorLog(String.format("[%s] %s (%s)", SysUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss"), procName, e.toString()));
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {}
			if (cstmt != null) try { cstmt.close();} catch (Exception e) {}
			if (con != null) try { con.close(); } catch (Exception e) {}
		}

		return resultMap;
	}
}
