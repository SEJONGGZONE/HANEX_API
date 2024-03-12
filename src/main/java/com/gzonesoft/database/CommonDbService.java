package com.gzonesoft.database;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**

 * Database 서비스 구현
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

@Service("commonDbService")
public class CommonDbService implements ICommonDbService{

	@Autowired private CommonDAO commonDAO;

	@Override
	public Object runProcedure(String procName, LinkedHashMap<String, Object> params) {
		return commonDAO.runProcedure(procName, params);
	}
    
}
