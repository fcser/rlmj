package cn.java.rlmj.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;

import cn.java.rlmj.pojo.People;
import cn.java.rlmj.service.PeopleService;
import cn.java.rlmj.utils.JSONHelper;
import cn.java.rlmj.utils.KeyValue;

@Controller
public class PictureController {

	@Resource
	private PeopleService peopleService;

	//@RequestMapping("/Getpic")
	public void getPicture(String key, int personid, HttpServletResponse response) throws IOException {
		boolean isKey = KeyValue.checkKey(key);
		JSONObject json = new JSONObject();
		json.put("personid", personid);
		if (isKey) {
			People people = peopleService.getPeopleById(personid);

			if (people != null) {
				json.put("res", 0);
				json.put("picurl", people.getPicture());
				json.put("err", "");
			} else {
				json.put("res", -1);
				json.put("err", "查询用户失败");
			}
		} else {
			json.put("res", -1);
			json.put("err", "身份认证失败");
		}
		JSONHelper.sendJSON(json, response);
	}
}
