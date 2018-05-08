package cn.java.rlmj.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.java.rlmj.dao.AdminMapper;
import cn.java.rlmj.dao.PeopleMapper;
import cn.java.rlmj.pojo.Admin;
import cn.java.rlmj.pojo.People;
import cn.java.rlmj.service.LoginService;
import cn.java.rlmj.utils.KeyValue;

@Service("loginService")
@Transactional
public class LoginServiceImpl implements LoginService {

	@Autowired
	private AdminMapper adminMapper;

	@Autowired
	private PeopleMapper peopleMapper;

	@Override
	public JSONObject login(String phone, String password, int type, Map<String, Integer> keyMap,
			Map<String, Integer> typeMap) {
		JSONObject pf = new JSONObject();
		if (type == 0) {
			// 管理员来了
			Admin admin = adminMapper.getAdmin(phone);
			if (admin == null) {
				System.out.println("没有这个用户");
				pf.put("res", -1);
				pf.put("err", LOGIN_STATE_ADMIN_NOT_EXISTS);
				pf.put("person", " ");
				pf.put("key", " ");
			} else if (!admin.getPassword().equals(password)) {
				System.out.println("密码错误");
				pf.put("res", -1);
				pf.put("err", LOGIN_STATE_ADMIN_PASSWORD_ERROR);
				pf.put("person", " ");
				pf.put("key", " ");
			} else {
				System.out.println("登录成功");
				String key = KeyValue.getKey((int) admin.getId());
				JSONObject person = (JSONObject) JSONObject.toJSON(admin);
				person.put("type", 0);
				pf.put("res", 0);
				pf.put("err", "");
				pf.put("person", person);
				pf.put("key", key);
				KeyValue.keyMap.put(key, (int) admin.getId());// 保存key
				typeMap.put(key, type);
			}
		} else if (type == 2 || type == 1) {
			// 户主和常住人员来了
			People people = peopleMapper.getPeopleByPhone(phone);
			if (people == null) {
				System.out.println("没有这个用户");
				pf.put("res", -1);
				pf.put("err", "没有这个用户");
				pf.put("person", " ");
				pf.put("key", " ");
			} else if (!people.getPassword().equals(password)) {
				pf.put("res", -1);
				pf.put("err", "密码错误");
				pf.put("person", " ");
				pf.put("key", " ");
			} else if (people.getType() != 1 && people.getType() != 2) {
				System.out.println("不是常住人员");
				pf.put("res", -1);
				pf.put("err", "不是常住人员");
				pf.put("person", " ");
				pf.put("key", " ");
			} else {
				System.out.println("登录成功");
				people.setModel(null);
				String key = KeyValue.getKey((int) people.getId());
				JSONObject person = (JSONObject) JSONObject.toJSON(people);
				pf.put("res", 0);
				pf.put("err", "");
				pf.put("person", person);
				pf.put("key", key);
				KeyValue.keyMap.put(key, (int) people.getId());// 保存key
				typeMap.put(key, type);
			}
		} else {
			// 有人入侵了
			pf.put("res", -1);
			pf.put("err", "走错片场了？");
			pf.put("person", " ");
			pf.put("key", " ");
		}
		return pf;
	}

}
