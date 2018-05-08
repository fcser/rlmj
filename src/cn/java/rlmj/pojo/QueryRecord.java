package cn.java.rlmj.pojo;

import java.lang.reflect.Field;

import com.alibaba.fastjson.annotation.JSONField;

public class QueryRecord {
	@JSONField(name = "personid")
	private int peopleid;
	@JSONField(name = "deviceid")
	private int deviceid;
	@JSONField(name = "starttime")
	private String starttime;
	@JSONField(name = "endtime")
	private String endtime;

	public QueryRecord() {
	}

	public int getPeopleid() {
		return peopleid;
	}

	public void setPeopleid(int peopleid) {
		this.peopleid = peopleid;
	}

	public int getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(int deviceid) {
		this.deviceid = deviceid;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	@Override
	public String toString() {
		String result = "[";
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field f : fields) {
			f.setAccessible(true);
			try {
				result += f.getName() + ":" + f.get(this) + ";";
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		result += "]";
		return result;
	}
}
