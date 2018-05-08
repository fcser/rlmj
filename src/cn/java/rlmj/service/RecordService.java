package cn.java.rlmj.service;
import java.util.ArrayList;

/**
 * 1.查询用户进出门禁情况
 * 2.删除某用户进出门禁情况（保留，协议未定）
 */
import cn.java.rlmj.pojo.*;

public interface RecordService {

	public ArrayList<VisitorsRecord> getRecords(int people_id); 
	
	public int deleteRecords(int people_id);
	
	public int insertRecord(VisitorsRecord visitorsRecord);

	public ArrayList<QueryRecordResult> queryRecords(QueryRecord queryRecord);

}
