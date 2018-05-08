package cn.java.rlmj.pojo;

import java.lang.reflect.Field;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @version :v1.0.0
 * @description :被注册人员
 * @author: zym
 * @date: 2018年3月1日下午9:34:47
 */
public class People {
	private int id;
	private String name;
	private int type;
	private int sex;
	@JSONField(name = "tel")
	private String phone;
	@JSONField(name = "pwd")
	private String password;
	private String birthday;
	@JSONField(name = "registerid")
	private Integer register;
	@JSONField(name = "houseid")
	private int house_id;
	@JSONField(name = "roomid")
	private int room_id;
	private byte[] model;
	@JSONField(name = "picurl")
	private String picture;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getHouse_id() {
		return house_id;
	}

	public void setHouse_id(int house_id) {
		this.house_id = house_id;
	}

	public int getRoom_id() {
		return room_id;
	}

	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getRegister() {
		return register;
	}

	public void setRegister(Integer register) {
		this.register = register;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public byte[] getModel() {
		return model;
	}

	public void setModel(byte[] model) {
		this.model = model;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
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
