package cn.java.rlmj.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;

import cn.java.rlmj.service.LoginService;
import cn.java.rlmj.utils.JSONHelper;
import cn.java.rlmj.utils.KeyValue;

@Controller
public class LoginController {

	@Resource
	private LoginService loginService;

	@RequestMapping("/Login")
	public void login(String tel, String pwd, int type,HttpServletResponse response) throws IOException {
		System.out.println("tel:" + tel + ";pwd:" + pwd + ";type:" + type);
		//System.out.println(request.getSession().getServletContext().getRealPath("/upload"));
		JSONObject json = loginService.login(tel, pwd, type, KeyValue.keyMap, KeyValue.typeMap);
		JSONHelper.sendJSON(json, response);
	}

	@RequestMapping("/Logout")
	public void logout(String key, HttpServletResponse response) throws IOException {
		boolean isKey = KeyValue.checkKey(key);
		JSONObject json = new JSONObject();
		if (isKey) {
			KeyValue.removeKey(key);
			json.put("res", 0);
			json.put("error", "");
		} else {
			json.put("res", -1);
			json.put("error", "没有登录的记录");
		}
		JSONHelper.sendJSON(json, response);
	}
}
