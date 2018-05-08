package cn.java.rlmj.dao;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import cn.java.rlmj.pojo.*;

@Service
public interface EquipmentMapper {

	public int insertEquipment(Equipment equipment);

	public int deleteEquipment(int id);

	public int updateEquipment(Equipment equipment);

	public Equipment getEquipment(int id);

	public ArrayList<Equipment> getEquipments();
}
