package cn.java.rlmj.dao;

import org.springframework.stereotype.Service;

import cn.java.rlmj.pojo.Admin;

@Service
public interface AdminMapper {
	public int insertAdmin(Admin admin);
	public int deleteAdmin(int id);
	public int updateAdmin(Admin admin);
	public Admin getAdmin(String phone);
}
