package cn.java.rlmj.controller;

/**
 * 1.查看门禁列表
 * 2.查看门禁摄像头（不涉及数据库，暂时保留）
 * 3.开门操作（不涉及数据库）（门禁开门成功与否，未能反馈）
 * 4.注册门禁设备
 * 5.修改门禁设备
 * 6.删除门禁设备
 */
import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.java.rlmj.connect.EquipmentList;
import cn.java.rlmj.connect.OpenDoor;
import cn.java.rlmj.pojo.Equipment;
import cn.java.rlmj.service.EquipmentService;
import cn.java.rlmj.utils.JSONHelper;
import cn.java.rlmj.utils.KeyValue;

@Controller
public class DeviceController {

	@Resource
	private EquipmentService equipmentService;
	//private final String error="无权限访问，请重启客户端";
	@RequestMapping("/Deletedevice")
	public void deleteDevice(String key, int deviceid, HttpServletResponse response) throws IOException {
		boolean isKey = KeyValue.checkKey(key);
		JSONObject json = new JSONObject();
		json.put("deviceid", deviceid);
		if (isKey) {
			int match = equipmentService.deleteEquipment(deviceid);

			if (match > 0) {
				json.put("res", 0);
				json.put("err", "");
			} else {
				json.put("res", -1);
				json.put("err", "设备注册失败");
			}
		} else {
			json.put("res", -1);
			json.put("err", "身份认证失败");
		}
		JSONHelper.sendJSON(json, response);
	}
	@RequestMapping("/Watch")
	public void watch(String key,int deviceid,HttpServletResponse response) throws IOException {
		System.out.println("准备查看监控");
		boolean isKey=KeyValue.checkKey(key);
		JSONObject json = new JSONObject();
		if(isKey) {
			OpenDoor openDoor = new OpenDoor();
			String ip = EquipmentList.equipments.map.get(deviceid);
			String watchKey=openDoor.getRandomString(32);//获取32位的key
			json.put("deviceip", ip);
			json.put("deviceport", 8888);
			System.out.println("生成的随机key："+watchKey);
			byte[] order=openDoor.watch(watchKey, 1);
			boolean isOk=openDoor.orderToDoor(order, ip);
			if(isOk) {
				json.put("res", 0);
				json.put("err", "");
				json.put("key", watchKey);
			}else {
				//开门失败，，有bug，未等终端反馈开门结果，，我就发送了结果给客户端
				json.put("res", 0);
				json.put("err", "");
				json.put("key", watchKey);
			}
		}else {
			json.put("res", -1);
			json.put("err", "无权限访问，请重启客户端");
			json.put("key", "");
			json.put("deviceip", "");
			json.put("deviceport", 0);
		}
		JSONHelper.sendJSON(json, response);
	}

	@RequestMapping("/Open")
	public void openDoor(String key, int deviceid, HttpServletResponse response) throws IOException {
		boolean isKey = KeyValue.checkKey(key);
		JSONObject json = new JSONObject();
		if (isKey) {
			OpenDoor openDoor = new OpenDoor();
			String ip = EquipmentList.equipments.map.get(deviceid);
			byte[] msg = openDoor.returnMsg();
			boolean result = openDoor.orderToDoor(msg, ip);
			if (result) {
				json.put("deviceid", deviceid);
				json.put("res", 0);
				json.put("err", "");
			} else {
				//开门失败，，有bug，未等终端反馈开门结果，，我就发送了结果给客户端
				json.put("deviceid", deviceid);
				json.put("res", 0);
				json.put("err", "");
			}
		} else {
			json.put("deviceid", deviceid);
			json.put("res", -1);
			json.put("err", "无权限访问，请重启客户端");
		}
		JSONHelper.sendJSON(json, response);
	}

	@RequestMapping("/Editdevice")
	public void editDevice(String key, Equipment device, HttpServletResponse response) throws IOException {
		boolean isKey = KeyValue.checkKey(key);
		JSONObject json = new JSONObject();
		json.put("deviceid", device.getId());
		if (isKey) {
			int match = equipmentService.updateEquipment(device);

			if (match > 0) {
				json.put("res", 0);
				json.put("err", "");
			} else {
				json.put("res", -1);
				json.put("err", "设备修改失败");
			}
		} else {
			json.put("res", -1);
			json.put("err", "无权限访问，请重启客户端");
		}
		JSONHelper.sendJSON(json, response);
	}

	@RequestMapping("/Querydevices")
	public void queryDevice(String key, HttpServletResponse response) throws IOException {
		boolean isKey = KeyValue.checkKey(key);
		JSONArray jsons = new JSONArray();
		if (isKey) {
			// 返回门禁列表
			ArrayList<Equipment> equipments = equipmentService.getEquipments();
			System.out.println("查询结果长度：" + equipments.size());
			for (int i = 0; i < equipments.size(); i++) {
				JSONObject json = new JSONObject();
				json.put("id", equipments.get(i).getId());
				json.put("houseid", equipments.get(i).getHouse_id());
				json.put("time", equipments.get(i).getTime());
				jsons.add(json);
			}
		}
		JSONHelper.sendJSON(jsons.toJSONString(), response);
	}

	@RequestMapping("/Registerdevice")
	public void registerDevice(String key, String device, HttpServletResponse response) throws IOException {
		boolean isKey = KeyValue.checkKey(key);
		JSONObject json = new JSONObject();
		Equipment equipment = JSONObject.parseObject(device, Equipment.class);
		System.out.println(equipment);
		json.put("id", equipment.getId());
		if (isKey) {
			int match = equipmentService.insertEquipment(equipment);

			if (match > 0) {
				json.put("res", 0);
				json.put("err", "");
			} else {
				json.put("res", -1);
				json.put("err", "设备注册失败");
			}
		} else {
			json.put("res", -1);
			json.put("err", "无权限访问，请重启客户端");
		}
		JSONHelper.sendJSON(json, response);
	}
}
