package com.gzonesoft.database;

import java.util.LinkedHashMap;

/**

 * Database 서비스 인터페이스
 * <pre>
 * <b>상세설명:</b>
 * 
 * </pre>
 * <pre>
 * <b>History:</b>
 *    박대근, 1.0, 2017.12.27 최초생성
 * </pre>
 *
 * @author 박대근
 * @version 1.0,2017.12.27 최초생성
 */
public interface ICommonDbService {
	public Object runProcedure(String procName, LinkedHashMap<String, Object> params);
}
