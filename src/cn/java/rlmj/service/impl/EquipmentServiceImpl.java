package cn.java.rlmj.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.java.rlmj.dao.EquipmentMapper;
import cn.java.rlmj.pojo.Equipment;
import cn.java.rlmj.service.EquipmentService;

@Service("equipmentService")
@Transactional
public class EquipmentServiceImpl implements EquipmentService {

	@Autowired
	private EquipmentMapper equipmentMapper;

	@Override
	public int deleteEquipment(int id) {
		return equipmentMapper.deleteEquipment(id);
	}

	@Override
	public int updateEquipment(Equipment equipment) {
		return equipmentMapper.updateEquipment(equipment);
	}

	@Override
	public ArrayList<Equipment> getEquipments() {
		return equipmentMapper.getEquipments();
	}

	@Override
	public int insertEquipment(Equipment equipment) {
		return equipmentMapper.insertEquipment(equipment);
	}

}
