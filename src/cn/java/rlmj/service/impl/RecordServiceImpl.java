package cn.java.rlmj.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.java.rlmj.dao.VisitorsRecordMapper;
import cn.java.rlmj.pojo.QueryRecord;
import cn.java.rlmj.pojo.QueryRecordResult;
import cn.java.rlmj.pojo.VisitorsRecord;
import cn.java.rlmj.service.RecordService;
@Service("recordService")
@Transactional
public  class RecordServiceImpl implements RecordService {

	@Autowired
	private VisitorsRecordMapper visitorsRecord;
	
	@Override
	public ArrayList<VisitorsRecord> getRecords(int people_id) {
		// TODO Auto-generated method stub
		return visitorsRecord.getVisitorsRecord(people_id);
	}

	@Override
	public int deleteRecords(int people_id) {
		// TODO Auto-generated method stub
		return visitorsRecord.deleteVisitorsRecord(people_id);
	}
	
	@Override
	public ArrayList<QueryRecordResult> queryRecords(QueryRecord queryRecord) {
		return visitorsRecord.queryRecords(queryRecord);
	}

	@Override
	public int insertRecord(VisitorsRecord visitorsRecords) {
		// TODO Auto-generated method stub
		//int house_id=visitorsRecords.getEquipment_id();
		System.out.println("插入人脸记录");
		return visitorsRecord.insertVisitorsRecord(visitorsRecords);
	}

	/*@Override
	public int updateVisitorsRecord(VisitorsRecord visitorsRecord) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<VisitorsRecord> getVisitorsRecord(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<VisitorsRecord> getVisitorsRecords() {
		// TODO Auto-generated method stub
		return null;
	}*/

}
