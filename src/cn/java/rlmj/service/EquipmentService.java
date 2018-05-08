package cn.java.rlmj.service;
/**
 * 1.查看门禁列表
 * 2.查看门禁摄像头（不涉及数据库，暂时保留）
 * 3.开门操作（不涉及数据库）
 * 4.注册门禁设备
 * 5.修改门禁设备
 * 6.删除门禁设备
 */
import java.util.ArrayList;

import cn.java.rlmj.pojo.Equipment;

public interface EquipmentService {
	public int deleteEquipment(int id);//6

	public int updateEquipment(Equipment equipment);//5

	public ArrayList<Equipment> getEquipments();//1

	public int insertEquipment(Equipment equipment);//4
}
