package cn.java.rlmj.servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONException;
import org.json.JSONObject;

import cn.java.rlmj.dao.PeopleMapper;
import cn.java.rlmj.dao.SqlSessionFactoryUtils;
import cn.java.rlmj.hrsdk.AFR_FSDK_FACEMODEL;
import cn.java.rlmj.hrsdk.ASVLOFFSCREEN;
import cn.java.rlmj.hrsdk.FaceInfo;
import cn.java.rlmj.pojo.People;
import cn.java.rlmj.sdkdo.AFRmain;
import cn.java.rlmj.utils.KeyValue;

/**
 * Servlet implementation class Registerperson
 */
// @WebServlet("/Registerperson")
public class Registerperson extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*
	 * private String key; private String person; private byte[] faceModel;
	 */
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Registerperson() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String savePath = this.getServletContext().getRealPath("/upload");
		String key = null;
		String person = null;
		byte[] faceModel = null;
		File dir = new File(savePath);
		System.out.println("文件目录+" + savePath);
		// 判断上传文件的保存目录是否存在
		if (!dir.exists() && !dir.isDirectory()) {
			System.out.println(savePath + "目录不存在，需要创建");
			// 创建目录
			dir.mkdir();
		}
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String filename = df.format(day);
		System.out.println(filename);
		int count = 1;
		if (!dir.exists()) {
			dir.mkdir();
		}
		File file = new File(dir, filename + ".jpg");
		while (file.exists()) {
			file = new File(dir, filename + "(" + (count++) + ")" + ".jpg");
		}
		// 消息提示
		String sort = null;
		try {
			System.out.println("开始接受图片");
			// 使用Apache文件上传组件处理文件上传步骤：
			// 1、创建一个DiskFileItemFactory工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 2、创建一个文件上传解析器
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 解决上传文件名的中文乱码
			upload.setHeaderEncoding("UTF-8");
			// 3、判断提交上来的数据是否是上传表单的数据
			if (!ServletFileUpload.isMultipartContent(request)) {
				// 按照传统方式获取数据
				System.out.println("解析出错");
				return;
			}
			// 4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
			List<FileItem> list = upload.parseRequest(request);
			for (FileItem item : list) {
				// 如果fileitem中封装的是普通输入项的数据
				if (item.isFormField()) {
					System.out.println("进入1");
					String keya = item.getFieldName();
					// 解决普通输入项的数据的中文乱码问题
					String value = item.getString("UTF-8");
					System.out.println(keya + "=" + value);
					if (keya.equals("key")) {
						key = value;// 获取key的值
					} else if (keya.equals("person")) {
						person = value;// 获取person的值
					}
				} else {// 如果fileitem中封装的是上传文件
						// 得到上传的文件名称，
						// String filename = item.getName();
						// System.out.println(filename);
					System.out.println("进入2" + sort);
					if (filename == null || filename.trim().equals("")) {
						continue;
					}
					// 获取item中的上传文件的输入流
					InputStream in = item.getInputStream();
					BufferedImage img = ImageIO.read(in);
					ASVLOFFSCREEN inputImg = AFRmain.changePic(img);
					FaceInfo[] faceInfosA = AFRmain.doFaceDetection(inputImg);
					if (faceInfosA.length < 1) {
						System.out.println("no face in Image");
						return;
					}
					AFR_FSDK_FACEMODEL model = AFRmain.extractFRFeature(inputImg, faceInfosA[0]);
					faceModel = model.toByteArray();

					// FileOutputStream out=new FileOutputStream(filename);
					ImageIO.write(img, "jpg", file);
					model.freeUnmanaged();// 销毁人脸模型
					/*
					 * byte[] b=new byte[1024]; while(in.read(b)!=-1){ out.write(b); }
					 */
					in.close();
					// 关闭输出流
					// out.close();
					// 删除处理文件上传时生成的临时文件
					item.delete();
				}

			}

		} catch (Exception e) {
			// String message= "文件上传失败！";
			e.printStackTrace();
			System.out.println("文件上传失败！");

		} finally {
			boolean isOk;
			try {
				isOk = registPerson(key, person, faceModel, file.getName());
				JSONObject json = new JSONObject();
				response.setCharacterEncoding("UTF-8");
				PrintWriter writer = response.getWriter();
				if (isOk) {
					// 注册成功
					json.put("person", person);
					json.put("res", 0);
					json.put("err", "");
					writer.write(json.toString());
				} else {
					json.put("person", person);
					json.put("res", -1);
					json.put("err", "注册失败");
					writer.write(json.toString());
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public boolean registPerson(String key, String person, byte[] model, String picture) throws JSONException {
		// 进行人员注册
		boolean isOk = false;
		// String register=KeyValue.keyMap.get(key);
		if (KeyValue.keyMap.get(key) != null) {
			// 钥匙正确，可以进行注册
			JSONObject json = new JSONObject(person);
			People people = new People();
			people.setId(json.getInt("id"));
			people.setName(json.getString("name"));
			people.setType(json.getInt("type"));
			people.setBirthday(json.getString("birthday"));
			people.setSex(json.getInt("sex"));
			people.setHouse_id(json.getInt("houseid"));
			people.setRoom_id(json.getInt("roomid"));
			people.setPassword(json.getString("pwd"));
			people.setPhone(json.getString("tel"));
			people.setRegister(KeyValue.keyMap.get(key));
			people.setModel(model);
			people.setPicture(picture);
			System.out.println("姓名=" + people.getName());
			SqlSession sqlSession = null;

			try {
				// System.out.println("test0");
				sqlSession = SqlSessionFactoryUtils.openSqlSession();
				// System.out.println("test");
				PeopleMapper peopleMapper = sqlSession.getMapper(PeopleMapper.class);

				int a = peopleMapper.insertPeople(people);
				System.out.println("test2" + a);
				sqlSession.commit();// 提交事务
				isOk = true;
			} catch (Exception e) {
				e.printStackTrace();
				sqlSession.rollback();// 事务回滚
				System.out.println("error");
			} finally {
				if (sqlSession != null) {
					sqlSession.close();
				}
			}
		}
		return isOk;

	}

}
