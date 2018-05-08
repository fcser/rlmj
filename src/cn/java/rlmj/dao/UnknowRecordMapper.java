package cn.java.rlmj.dao;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import cn.java.rlmj.pojo.*;
@Service
public interface UnknowRecordMapper {

	public ArrayList<UnknowRecord> getRecords(String beginTime,String endTime);
	
	public ArrayList<QueryStrangeResult> queryStrangeRecords(QueryStrange record);
	
	public int insertRecord(UnknowRecord unknowRecord);
}
