package cn.java.rlmj.servlet;

/**
 * TODO
 *未完成：1.查询和编辑用户
 *2.返回开门结果
 */
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;

import com.alibaba.fastjson.JSONObject;

import cn.java.rlmj.dao.PeopleMapper;
import cn.java.rlmj.dao.SqlSessionFactoryUtils;
import cn.java.rlmj.utils.KeyValue;

/**
 * Servlet implementation class Deleteperson
 */
// @WebServlet("/Deleteperson")
public class Deleteperson extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Deleteperson() {
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
		/*String key = request.getParameter("key");
		String person = request.getParameter("personid");
		response.setCharacterEncoding("utf-8");
		PrintWriter writer = response.getWriter();
		int id = Integer.parseInt(person);
		boolean isKey = KeyValue.checkKey(key);
		if (isKey) {
			SqlSession sqlSession = null;
			JSONObject json = new JSONObject();
			try {
				sqlSession = SqlSessionFactoryUtils.openSqlSession();
				PeopleMapper peopleMapper = sqlSession.getMapper(PeopleMapper.class);
				peopleMapper.deletePeople((long) id);
				sqlSession.commit();
				json.put("personid", id);
				json.put("res", 0);
				json.put("err", "");
				// writer.write(json.toString());
			} catch (Exception e) {
				e.printStackTrace();
				sqlSession.rollback();
				json.put("personid", id);
				json.put("res", -1);
				json.put("err", "删除用户失败");
			} finally {
				sqlSession.close();
				writer.write(json.toString());
			}
		}*/
	}

}
