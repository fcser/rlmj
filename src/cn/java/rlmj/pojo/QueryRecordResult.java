package cn.java.rlmj.pojo;


import com.alibaba.fastjson.annotation.JSONField;

public class QueryRecordResult {
	@JSONField(name = "personid")
	private int people_id;
	@JSONField(name = "personname")
	private String person_name;
	@JSONField(name = "deviceid")
	private int device_id;
	@JSONField(name = "houseid")
	private int house_id;
	@JSONField(name = "time")
	private String datetime;

	

	public int getPeople_id() {
		return people_id;
	}

	public void setPeople_id(int people_id) {
		this.people_id = people_id;
	}

	public int getDevice_id() {
		return device_id;
	}

	public void setDevice_id(int device_id) {
		this.device_id = device_id;
	}

	public String getPerson_name() {
		return person_name;
	}

	public void setPerson_name(String person_name) {
		this.person_name = person_name;
	}

	public int getHouse_id() {
		return house_id;
	}

	public void setHouse_id(int house_id) {
		this.house_id = house_id;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
}
