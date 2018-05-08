
package cn.java.rlmj.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.java.rlmj.dao.EquipmentMapper;
import cn.java.rlmj.dao.SqlSessionFactoryUtils;
import cn.java.rlmj.pojo.Equipment;
import cn.java.rlmj.utils.KeyValue;

/**
 * Servlet implementation class Querydevices
 */
//@WebServlet("/Querydevices")
public class Querydevices extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Querydevices() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String key=request.getParameter("key");
		response.setCharacterEncoding("utf-8");
		PrintWriter write=response.getWriter();
		boolean isKey=KeyValue.checkKey(key);
		if(isKey){
			//返回门禁列表
			JSONArray jsons=new JSONArray();
			SqlSession sqlSession=null;
			
		     try{   
		     sqlSession=SqlSessionFactoryUtils.openSqlSession();
		     EquipmentMapper equipmentMapper=sqlSession.getMapper(EquipmentMapper.class);
		     ArrayList<Equipment> equipments=equipmentMapper.getEquipments();
		     for(int i=0;i<equipments.size();i++){
		    	 JSONObject json=new JSONObject();
		    	 json.put("id", equipments.get(i).getId());
		    	 json.put("houseid", equipments.get(i).getHouse_id());
		    	 json.put("time", equipments.get(i).getTime());
		    	 jsons.put(i, json);
		     }
		     }catch(Exception e){
		    	 e.printStackTrace();
		     	System.out.println("error");
		     }finally{
		     	if(sqlSession!=null){
		     		sqlSession.close();
		     	}
		     }
			write.write(jsons.toString());
		}
	}

}
