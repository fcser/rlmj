package cn.java.rlmj.service;

import java.util.ArrayList;

import cn.java.rlmj.pojo.QueryStrange;
import cn.java.rlmj.pojo.QueryStrangeResult;
import cn.java.rlmj.pojo.UnknowRecord;

/**
 * 1.查询未注册人员门禁出现情况（不经过数据库）
 *
 */
public interface UnknowRecordService {
    public ArrayList<UnknowRecord> getUnknowRecords(String beginTime,String endTime);

	public ArrayList<QueryStrangeResult> queryRecords(QueryStrange record);
	
	public int insertRecord(UnknowRecord unknowRecord);
}
