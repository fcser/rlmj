package cn.java.rlmj.pojo;

import java.lang.reflect.Field;

import com.alibaba.fastjson.annotation.JSONField;

public class Equipment {

	private int id;
	@JSONField(name = "houseid")
	private int house_id;
	private String time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHouse_id() {
		return house_id;
	}

	public void setHouse_id(int house_id) {
		this.house_id = house_id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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
