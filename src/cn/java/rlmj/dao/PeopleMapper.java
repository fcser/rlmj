package cn.java.rlmj.dao;
/**
 * 
 * 1.查询用户信息
 * 2.注册用户
 * 3.删除用户
 * 4.修改用户信息
 * 5.请求查看某一用户照片
 * 
 */
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import cn.java.rlmj.pojo.People;
import cn.java.rlmj.sdkdo.Face;

@Service
public interface PeopleMapper {
	public int insertPeople(People people);

	public int deletePeople(int id);

	public int updatePeople(People people);//动态sql

	public People getPeopleById(int id);

	public People getPeopleByPhone(String phone);//注册用户用

	public ArrayList<People> getPeople(People people);//动态sql查询用户
	
	public ArrayList<Face> getModels();
	
	public String getPicUrl(int id);
	// public List<People> findPeoples(String roleName);
}
