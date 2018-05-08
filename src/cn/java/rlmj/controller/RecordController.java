package cn.java.rlmj.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.java.rlmj.pojo.People;
import cn.java.rlmj.pojo.QueryRecord;
import cn.java.rlmj.pojo.QueryRecordResult;
import cn.java.rlmj.pojo.QueryStrange;
import cn.java.rlmj.pojo.QueryStrangeResult;
import cn.java.rlmj.pojo.UnknowRecord;
import cn.java.rlmj.service.*;
import cn.java.rlmj.utils.JSONHelper;
import cn.java.rlmj.utils.KeyValue;

/**
 * 1.查询进出记录 2.查询未注册人员进出记录
 * 
 * @author zym
 *
 */
@Controller
public class RecordController {

	@Resource
	private RecordService recordService;
	@Resource
	private UnknowRecordService unKnow;

	// private final String idError="无权限访问，请重启客户端";

	@RequestMapping("/Queryrecord")
	public void queryRecord(String key, QueryRecord record, HttpServletResponse response) throws IOException {
		boolean isKey = KeyValue.checkKey(key);
		System.out.println(record);
		if (isKey) {
			ArrayList<QueryRecordResult> queryResult = recordService.queryRecords(record);
			// JSONHelper.sendJSON(JSONObject.toJSONString(queryResult), response);
			JSONArray json = new JSONArray();
			for (QueryRecordResult p : queryResult) {
				JSONObject j = new JSONObject();
				j.put("record", p);
				json.add(p);
			}
			JSONHelper.sendJSON(json.toJSONString(), response);
		}
	}

	/*
	 * public void getVisitorsRecord(String key, int personid, int deviceid,String
	 * startTime,String endTime,HttpServletResponse response) throws IOException {
	 * boolean isKey = KeyValue.checkKey(key); if (isKey) { JSONObject json = new
	 * JSONObject(); //String pic = peopleService.getPicUrl(personid);
	 * JSONHelper.sendJSON(json, response); } else { JSONObject json = new
	 * JSONObject(); // json.put("person", people); json.put("res", -1);
	 * json.put("err", idError); JSONHelper.sendJSON(json, response); } }
	 */
	@RequestMapping("/Getstrange")
	public void getStrange(String key, QueryStrange record, HttpServletResponse response) throws IOException {
		boolean isKey = KeyValue.checkKey(key);
		System.out.println(record);
		if (isKey) {
			ArrayList<QueryStrangeResult> queryResult = unKnow.queryRecords(record);
			// JSONHelper.sendJSON(JSONObject.toJSONString(queryResult), response);
			JSONArray json = new JSONArray();
			for (QueryStrangeResult p : queryResult) {
				String pic = p.getPicurl();
				String ip = InetAddress.getLocalHost().getHostAddress();
				String picture = "http://" + ip + ":8080/rlmj/upload/strange/"
						+ pic.substring(pic.lastIndexOf("\\") + 1);
				p.setPicurl(picture);
				JSONObject j = new JSONObject();
				j.put("strangerecord", p);
				json.add(p);
			}
			JSONHelper.sendJSON(json.toJSONString(), response);
		}
	}
	/*
	 * public void getStranger(String key, int personid, int deviceid,String
	 * startTime,String endTime,HttpServletResponse response) throws IOException {
	 * boolean isKey = KeyValue.checkKey(key); if (isKey) { JSONObject json = new
	 * JSONObject(); //String pic = peopleService.getPicUrl(personid);
	 * JSONHelper.sendJSON(json, response); } else { JSONObject json = new
	 * JSONObject(); // json.put("person", people); json.put("res", -1);
	 * json.put("err", idError); JSONHelper.sendJSON(json, response); } }
	 */

}
