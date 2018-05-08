package cn.java.rlmj.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.*;

public class JSONHelper {

	public static void sendJSONBoolean(boolean b, HttpServletResponse response) throws IOException {
		sendJSON(Boolean.toString(b), response);
	}

	public static void sendJSON(JSONObject json, HttpServletResponse response) throws IOException {
		sendJSON(json.toJSONString(), response);
	}
	public static void sendJSONArray(JSONArray json, HttpServletResponse response) throws IOException {
		sendJSON(json.toJSONString(), response);
	}

	public static void sendJSON(String json, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();
		out.close();
	}
}
