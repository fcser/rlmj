package cn.java.rlmj.pojo;

import com.alibaba.fastjson.annotation.JSONField;

public class QueryStrangeResult {
	@JSONField(name = "deviceid")
	private int device_id;
	@JSONField(name = "houseid")
	private int house_id;
	@JSONField(name = "time")
	private String datetime;
	@JSONField(name = "picurl")
	private String picture;

	public int getDevice_id() {
		return device_id;
	}

	public void setDevice_id(int device_id) {
		this.device_id = device_id;
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

	public String getPicurl() {
		return picture;
	}

	public void setPicurl(String picture) {
		this.picture = picture;
	}
}

