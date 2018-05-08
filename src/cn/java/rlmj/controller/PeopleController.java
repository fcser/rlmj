package cn.java.rlmj.controller;

/**
 * 1.用户注册
 * 2.删除用户
 * 3.修改用户信息（bug：修改用户信息后，图片更新了，原图片没有删除）
 * 4.查询用户
 * 5.请求用户照片
 */
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
//import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

import cn.java.rlmj.connect.Path;
import cn.java.rlmj.hrsdk.AFR_FSDK_FACEMODEL;
import cn.java.rlmj.hrsdk.ASVLOFFSCREEN;
import cn.java.rlmj.hrsdk.FaceInfo;
import cn.java.rlmj.pojo.People;
import cn.java.rlmj.sdkdo.AFRmain;
import cn.java.rlmj.sdkdo.Face;
import cn.java.rlmj.sdkdo.FaceModels;
import cn.java.rlmj.service.PeopleService;
import cn.java.rlmj.utils.JSONHelper;
import cn.java.rlmj.utils.KeyValue;

@Controller
public class PeopleController {

	@Resource
	private PeopleService peopleService;
	
	
	private final String idError="无权限访问，请重启客户端";
	
	@RequestMapping("/Deleteperson")
	public void deletePeople(String key, int personid, HttpServletResponse response) throws IOException {
		boolean isKey = KeyValue.checkKey(key);
		System.out.println("执行删除操作");
		JSONObject json = new JSONObject();
		json.put("personid", personid);
		String url=peopleService.getPicUrl(personid);
		if (isKey) {
			int match = peopleService.deletePeople(personid);

			if (match > 0) {
				File file=new File(url);
				file.delete();//删除该照片
				FaceModels.deleteFace(personid);//删除人脸库中人脸
				json.put("res", 0);
				json.put("err", "");
			} else {
				json.put("res", -1);
				json.put("err", "删除用户失败");
			}
		} else {
			json.put("res", -1);
			json.put("err", idError);
		}
		JSONHelper.sendJSON(json, response);
	}

	@RequestMapping("/Getpic")
	public void getPicture(String key, int personid, HttpServletResponse response) throws IOException {
		boolean isKey = KeyValue.checkKey(key);
		if (isKey) {
			JSONObject json = new JSONObject();
			String pic = peopleService.getPicUrl(personid);
			String ip = InetAddress.getLocalHost().getHostAddress();
			System.out.println("本地ip是：" + ip);
		    String picture="http://"+ip+":8080/rlmj/upload/"+pic.substring(pic.lastIndexOf("\\")+1);
		    System.out.println("本地url是：" + picture);
			json.put("picurl", picture);
			JSONHelper.sendJSON(json, response);
		} else {
			JSONObject json = new JSONObject();
			// json.put("person", people);
			json.put("res", -1);
			json.put("err", idError);
			JSONHelper.sendJSON(json, response);
		}
	}

	@RequestMapping("/Registerperson")
	public void registerPeople(String key, String person, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		boolean isKey = KeyValue.checkKey(key);
		System.out.println(person);
		People people = JSONObject.parseObject(person, People.class);
		System.out.println(people);
		if (isKey) {
			String savePath = request.getSession().getServletContext().getRealPath("/upload");
			//System.out.println(savePath);
			System.out.println(Path.path);
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());

			if (multipartResolver.isMultipart(request)) {
				// 将request变成多部分request
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				// 获取multiRequest 中所有的文件名
				Iterator<String> iter = multiRequest.getFileNames();

				while (iter.hasNext()) {
					// 一次遍历所有文件
					MultipartFile file = multiRequest.getFile(iter.next().toString());
					if (file != null) {
						Date day = new Date();
						SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
						String time = df.format(day);
						String fileOrigName = file.getOriginalFilename();
						int index = fileOrigName.lastIndexOf('.');
						String end = fileOrigName.substring(index, fileOrigName.length());
						System.out.println("图片后缀：" + end);
						String path = savePath + "\\" + file.getOriginalFilename().substring(0, index) + "_" + time+ end;
						// 上传
						file.transferTo(new File(path));
						boolean isOk;
						System.out.println(KeyValue.typeMap);
						System.out.println(key);
						if (KeyValue.typeMap.get(key) != 0) {
							people.setRegister(KeyValue.keyMap.get(key));
						} else {
							people.setRegister(null);
						}
						JSONObject json = new JSONObject();
						//json.put("person", people);
						try {
							isOk = registPerson(people, path);
							people.setModel(null);
							json.put("person", people);
							if (isOk) {
								//p.setModel(null);
								// 注册成功
								json.put("res", 0);
								json.put("err", "");
							} else {
								json.put("res", -1);
								json.put("err", "注册失败");
							}
						} catch (Exception e) {
							e.printStackTrace();
							json.put("res", -1);
							json.put("err", "注册失败，io错误");
						} finally {
							JSONHelper.sendJSON(json, response);
						}
					}
				}
			}
		} else {
			JSONObject json = new JSONObject();
			json.put("person", people);
			json.put("res", -1);
			json.put("err", idError);
			JSONHelper.sendJSON(json, response);
		}
	}

	@RequestMapping("/Editperson")
	public void updatePeople(String key, int personid, String person, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		boolean isKey = KeyValue.checkKey(key);
		System.out.println(person);
		People people = JSONObject.parseObject(person, People.class);// json注入People
		people.setId(personid);// 加入personid不知道有没有意义
		JSONObject json = new JSONObject();
		//json.put("person", people);
		System.out.println("iskey:" + isKey);
		json.put("res", -1);
		json.put("err", "修改信息失败");
		if (isKey) {
			String url=peopleService.getPicUrl(personid);
			System.out.println(people);
			String savePath = request.getSession().getServletContext().getRealPath("/upload");

			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());

			if (multipartResolver.isMultipart(request)) {
				System.out.println("Enter multipart");
				// 将request变成多部分request
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				// 获取multiRequest 中所有的文件名
				Iterator<String> iter = multiRequest.getFileNames();

				if (iter.hasNext()) {
					while (iter.hasNext()) {
						System.out.println("loop");
						// 一次遍历所有文件
						MultipartFile file = multiRequest.getFile(iter.next().toString());
						if (file != null) {
							System.out.println("file is not null");
							Date day = new Date();
							SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
							String time = df.format(day);
							String fileOrigName = file.getOriginalFilename();
							int index = fileOrigName.lastIndexOf('.');
							String end = fileOrigName.substring(index, fileOrigName.length());
							String path = savePath +"\\"+ file.getOriginalFilename().substring(0, index)+"_" + time + end;
							// 上传
							file.transferTo(new File(path));
							boolean isOk;
							// people.setRegister(KeyValue.keyMap.get(key));
							try {
								isOk = updatePerson(people, path);
								people.setModel(null);
								json.put("person", people);
								if (isOk) {
									// 注册成功
									File files=new File(url);
									files.delete();//删除该照片
									FaceModels.deleteFace(personid);//删除人脸库中人脸
									json.put("res", 0);
									json.put("err", "");
								} else {
									json.put("res", -1);
									json.put("err", "修改信息失败");
								}
							} catch (Exception e) {
								e.printStackTrace();
								json.put("res", -1);
								json.put("err", "修改信息失败，io请求错误");
							}
						}
					}
				} else {
					int influence = peopleService.updatePeople(people);
					people.setModel(null);
					json.put("person", people);
					System.out.println("influence:" + influence);
					if (influence > 0) {
						//File file=new File(url);
						//file.delete();//删除该照片
						//FaceModels.deleteFace(personid);//删除人脸库中人脸
						json.put("res", 0);
						json.put("err", "");
					} else {
						json.put("res", -1);
						json.put("err", "修改信息失败，数据库写入错误");
					}
				}
			} else {
				int influence = peopleService.updatePeople(people);
				people.setModel(null);
				json.put("person", people);
				System.out.println("influence:" + influence);
				if (influence > 0) {
					//File file=new File(url);
					//file.delete();//删除该照片
					//FaceModels.deleteFace(personid);//删除人脸库中人脸
					json.put("res", 0);
					json.put("err", "");
				} else {
					json.put("res", -1);
					json.put("err", "修改信息失败，数据库写入错误");
				}
			}
		} else {
			json.put("person", people);
			json.put("res", -1);
			json.put("err", idError);
		}
		System.out.println(json);
		JSONHelper.sendJSON(json, response);
	}

	@RequestMapping("/Queryperson")
	public void queryPerson(String key, String person, HttpServletResponse response) throws IOException {
		boolean isKey = KeyValue.checkKey(key);
		System.out.println("查询人员");
		if (isKey) {
			People people = JSONObject.parseObject(person, People.class);
			System.out.println(people);
			ArrayList<People> persons = peopleService.getPeoples(people);
			System.out.println("查询结果长度：" + persons.size());
			// 未完成，不知道可不可行？
			JSONArray json = new JSONArray();
			for (People p : persons) {
				String pic = p.getPicture();
				String ip = InetAddress.getLocalHost().getHostAddress();
				System.out.println("本地ip是：" + ip);
			    String picture="http://"+ip+":8080/rlmj/upload/"+pic.substring(pic.lastIndexOf("\\")+1);
			    System.out.println("本地url是：" + picture);
			    p.setPicture(picture);
				JSONObject j = new JSONObject();
				p.setModel(null);
				j.put("person", p);
				json.add(p);
			}
			JSONHelper.sendJSON(json.toJSONString(), response);
		} else {
			JSONObject json = new JSONObject();
			// json.put("person", people);
			json.put("res", -1);
			json.put("err", idError);
			JSONHelper.sendJSON(json, response);
		}

	}

	private boolean registPerson(People person, String picture) throws Exception {
		// 进行人员注册
		boolean isOk = false;
		// String register=KeyValue.keyMap.get(key);
		// 钥匙正确，可以进行注册
		BufferedImage img = ImageIO.read(new File(picture));
		ASVLOFFSCREEN inputImg = AFRmain.changePic(img);
		FaceInfo[] faceInfosA = AFRmain.doFaceDetection(inputImg);
		if (faceInfosA.length < 1) {
			System.out.println("no face in Image");
			return false;
		}
		AFR_FSDK_FACEMODEL model = AFRmain.extractFRFeature(inputImg, faceInfosA[0]);
		byte[] faceModel = model.toByteArray();

		model.freeUnmanaged();// 销毁人脸模型
		People people = person;

		people.setModel(faceModel);
		people.setPicture(picture);
		System.out.println("姓名=" + people.getName());

		int influence = peopleService.insertPeople(people);
		System.out.println("注册后返回的id：" + people.getId());
		System.out.println("influence:" + influence);
		if (influence > 0) {
			addModel(faceModel, people.getId(), people.getHouse_id());
			isOk = true;
		}
		return isOk;

	}

	private void addModel(byte[] model, int id, int house_id) {
		// boolean isOK=false;
		Face face = new Face(model, id, house_id);
		FaceModels.facemodel.add(face);
	}

	private boolean updatePerson(People person, String picture) throws Exception {
		// 进行人员注册
		boolean isOk = false;
		// String register=KeyValue.keyMap.get(key);
		// 钥匙正确，可以进行注册
		BufferedImage img = ImageIO.read(new File(picture));
		ASVLOFFSCREEN inputImg = AFRmain.changePic(img);
		FaceInfo[] faceInfosA = AFRmain.doFaceDetection(inputImg);
		if (faceInfosA.length < 1) {
			System.out.println("no face in Image");
			return false;
		}
		AFR_FSDK_FACEMODEL model = AFRmain.extractFRFeature(inputImg, faceInfosA[0]);
		byte[] faceModel = model.toByteArray();

		model.freeUnmanaged();// 销毁人脸模型
		People people = person;

		people.setModel(faceModel);
		people.setPicture(picture);
		System.out.println("姓名=" + people.getName());

		int influence = peopleService.updatePeople(people);
		System.out.println("influence:" + influence);
		if (influence > 0)
			isOk = true;
		return isOk;

	}
}
