package cn.java.rlmj.servlet;

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
import cn.java.rlmj.pojo.People;
import cn.java.rlmj.utils.KeyValue;

/**
 * Servlet implementation class Getpic
 */
// @WebServlet("/Getpic")
public class Getpic extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Getpic() {
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
		String person = request.getParameter("personid");
		response.setCharacterEncoding("utf-8");
		PrintWriter writer = response.getWriter();
		int id = Integer.parseInt(person);
		boolean isKey = KeyValue.checkKey(key);
		if (isKey) {
			SqlSession sqlSession = null;
			try {
				sqlSession = SqlSessionFactoryUtils.openSqlSession();
				PeopleMapper peopleMapper = sqlSession.getMapper(PeopleMapper.class);
				People people = peopleMapper.getPeopleById( id);
				JSONObject json = new JSONObject();
				json.put("picurl", people.getPicture());
				writer.write(json.toString());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				sqlSession.close();
			}
		}
	}

}
