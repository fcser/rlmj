package cn.java.rlmj.pojo;
/**
 * 
 * @version :v1.0.0
 * @description :����Ա
 * @author: zym
 * @date: 2018��3��1������9:34:28
 */
public class Admin {
	private int id;
	private String phone;
	private String name;
	private String password;

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
