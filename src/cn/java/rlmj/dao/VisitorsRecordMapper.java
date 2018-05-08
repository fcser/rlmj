package cn.java.rlmj.dao;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import cn.java.rlmj.pojo.QueryRecord;
import cn.java.rlmj.pojo.QueryRecordResult;
import cn.java.rlmj.pojo.VisitorsRecord;

@Service
public interface VisitorsRecordMapper {
	public int insertVisitorsRecord(VisitorsRecord visitorsRecord);

	public int deleteVisitorsRecord(int people_id);

	public int updateVisitorsRecord(VisitorsRecord visitorsRecord);

	public ArrayList<VisitorsRecord> getVisitorsRecord(int id);

	public ArrayList<VisitorsRecord> getVisitorsRecords();
	
	public ArrayList<QueryRecordResult> queryRecords(QueryRecord queryRecord);
	//public ArrayList<VisitorsRecord> getRecords(int people_id,int device_id,String beginTime,String endTime);
}
