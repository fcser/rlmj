package cn.java.rlmj.pojo;

import com.alibaba.fastjson.annotation.JSONField;

public class VisitorsRecord {
	private int id;
	@JSONField(name="personid")
	private int people_id;
	@JSONField(name="deviceid")
	private int equipment_id;
	private String datetime;
	
	
	public int getEquipment_id() {
		return equipment_id;
	}
	public void setEquipment_id(int equipment_id) {
		this.equipment_id = equipment_id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getPeople_id() {
		return people_id;
	}
	public void setPeople_id(int people_id) {
		this.people_id = people_id;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	
	
}
