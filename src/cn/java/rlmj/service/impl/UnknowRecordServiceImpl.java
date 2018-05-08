package cn.java.rlmj.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.java.rlmj.dao.UnknowRecordMapper;
import cn.java.rlmj.pojo.QueryStrange;
import cn.java.rlmj.pojo.QueryStrangeResult;
import cn.java.rlmj.pojo.UnknowRecord;
import cn.java.rlmj.service.UnknowRecordService;
@Service("unknowRecordService")
@Transactional
public class UnknowRecordServiceImpl implements UnknowRecordService {

	@Autowired
	private UnknowRecordMapper unknowRecord;
	@Override
	public ArrayList<UnknowRecord> getUnknowRecords(String beginTime, String endTime) {
		// TODO Auto-generated method stub
		return unknowRecord.getRecords(beginTime, endTime);
	}
	
	@Override
	public ArrayList<QueryStrangeResult> queryRecords(QueryStrange record) {
		return unknowRecord.queryStrangeRecords(record);
	}

	@Override
	public int insertRecord(UnknowRecord unknowRecords) {
		// TODO Auto-generated method stub
		return unknowRecord.insertRecord(unknowRecords);
	}

}
