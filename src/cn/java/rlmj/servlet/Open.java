package cn.java.rlmj.servlet;

/**
 * 开关门命令接收控制
 */
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import cn.java.rlmj.connect.OpenDoor;
import cn.java.rlmj.utils.KeyValue;

/**
 * Servlet implementation class Open
 */
//@WebServlet("/Open")
public class Open extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Open() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String key = request.getParameter("key");
		String deviced = request.getParameter("deviceid");
		response.setCharacterEncoding("utf-8");
		PrintWriter writer = response.getWriter();
		int device = Integer.parseInt(deviced);
		boolean isKey = KeyValue.checkKey(key);
		if (isKey) {
			// 发送开门命令
			System.out.println("准备开门");
			OpenDoor openDoor = new OpenDoor();
			byte[] msg = openDoor.returnMsg();
			openDoor.orderToDoor(msg, deviced);
			// 将开门结果返回，，未最终完成
			JSONObject json = new JSONObject();
			try {
				json.put("deviceid", device);
				json.put("res", 0);
				json.put("err", "");
				writer.write(json.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
