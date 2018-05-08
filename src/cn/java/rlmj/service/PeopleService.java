package cn.java.rlmj.service;
/**
 * 1.查询用户信息
 * 2.注册用户
 * 3.删除用户
 * 4.修改用户信息
 * 5.请求查看某一用户照片
 * 
 */
import java.util.ArrayList;

import cn.java.rlmj.pojo.People;
import cn.java.rlmj.sdkdo.Face;

public interface PeopleService {
	public ArrayList<Face> getModels();

	public int deletePeople(int personid);//3

	public People getPeopleById(int id);
	
	public ArrayList<People> getPeoples(People people);

	public int insertPeople(People people);//2
	
	public int updatePeople(People people);//4
	
	public String getPicUrl(int people_id);//5
}
