package cn.java.rlmj.servlet;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.java.rlmj.connect.Server;
import cn.java.rlmj.service.PeopleService;

/**
 * Servlet implementation class TcpTest
 */
@WebServlet("/TcpTest")
public class TcpTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Resource
	private PeopleService peopleService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TcpTest() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		new Server(peopleService);
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		System.out.println("会distory");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
