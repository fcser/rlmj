package cn.java.rlmj.sdkdo;
/**
 * 
 * @version :v1.0.0
 * @description :������ŵ�ģ��
 * @author: zym
 * @date: 2018��3��16������8:30:00
 */
public class Face {
    public byte[] model;
    public int id;
    public int house_id;
    
    public Face(byte[] model,int id,int house_id) {
    	this.model=model;
    	this.id=id;
    	this.house_id=house_id;
    }
    
    public Face() {
    	
    }
	public byte[] getModel() {
		return model;
	}
	public void setModel(byte[] model) {
		this.model = model;
	}
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
    
}
