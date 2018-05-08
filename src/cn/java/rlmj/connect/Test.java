package cn.java.rlmj.connect;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;

import cn.java.rlmj.controller.RecordController;
import cn.java.rlmj.dao.SqlSessionFactoryUtils;
import cn.java.rlmj.dao.UnknowRecordMapper;
import cn.java.rlmj.dao.VisitorsRecordMapper;
import cn.java.rlmj.pojo.UnknowRecord;
import cn.java.rlmj.pojo.VisitorsRecord;
import cn.java.rlmj.service.RecordService;
import cn.java.rlmj.service.UnknowRecordService;
import cn.java.rlmj.service.impl.UnknowRecordServiceImpl;

public class Test {
	
	//private static  UnknowRecordService unKnow;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		VisitorsRecord vs=new VisitorsRecord();
		/*UnknowRecord un=new UnknowRecord();
		un.setDatetime(getaTime());
	    un.setPicture("aaa.jpg");
	    un.setEquipment_id(5858);*/
		vs.setPeople_id(9);
	    vs.setEquipment_id(5858);
	    vs.setDatetime(getaTime());
	    //RecordController unn=new  RecordController();
	    insertRecord(vs);
	   // insertUnknowRecord(un);
	    /*Date day=new Date();
		SimpleDateFormat df=new SimpleDateFormat("HH:mm:ss");
		String time=df.format(day);
		String[] b=time.split(":");
		System.out.println("同步时间是："+df);
		byte[] a=new byte[4];
		a[0]=(byte)13;
		a[1]=(byte) Integer.parseInt(b[0]);
		a[2]=(byte) Integer.parseInt(b[1]);
		a[3]=(byte) Integer.parseInt(b[2]);
		System.out.println(a[0]+"-----"+a[1]+":"+a[2]+":"+a[3]);*/
		/*OpenDoor o=new OpenDoor();
		String key=o.getRandomString(32);
		byte[] a=o.watch(key, 1);
		System.out.println(a);*/
	}
	private static boolean insertRecord(VisitorsRecord vs) {
		// TODO Auto-generated method stub
		SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
		boolean b=false;
		try {
			VisitorsRecordMapper equipmentMapper = sqlSession.getMapper(VisitorsRecordMapper.class);
			equipmentMapper.insertVisitorsRecord(vs);
			sqlSession.commit();
			b=true;
		}catch(Exception e){
			sqlSession.rollback();
			e.printStackTrace();
		}
		return b;
	}
	public static String getaTime() {
		Date day=new Date();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(day);
	}
	private static boolean insertUnknowRecord(UnknowRecord un) {
		SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
		boolean b=false;
		try {
			sqlSession = SqlSessionFactoryUtils.openSqlSession();
			UnknowRecordMapper equipmentMapper = sqlSession.getMapper(UnknowRecordMapper.class);
			equipmentMapper.insertRecord(un);
			sqlSession.commit();
			b=true;
		}catch(Exception e){
			sqlSession.rollback();
			e.printStackTrace();
		}
		return b;
	}

}
