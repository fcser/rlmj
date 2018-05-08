package cn.java.rlmj.service;

/**
 * 1.用户登录
 */
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface LoginService {
	public static final String LOGIN_STATE_ADMIN_NOT_EXISTS = "没有这个管理员";
	public static final String LOGIN_STATE_ADMIN_PASSWORD_ERROR = "管理员密码错误";

	public JSONObject login(String phone, String password, int type, Map<String, Integer> keyMap,
			Map<String, Integer> typeMap);
}
