package cn.java.rlmj.connect;

import java.io.IOException;
import java.net.*;

import cn.java.rlmj.sdkdo.AFRmain;
import cn.java.rlmj.sdkdo.GetFaceModel;
import cn.java.rlmj.service.PeopleService;

public class Server {
	private static final int PORT = 8888;
	private ServerSocket ss = null;
	private Socket s = null;

	public Server(PeopleService peopleService) {
		try {
			ss = new ServerSocket();
			ss.setReuseAddress(true);
			ss.bind(new InetSocketAddress(PORT));
			AFRmain.init();
			GetFaceModel.getFaceModels(peopleService);// 加载数据库人脸
			// EquipmentList.getEquipment();//加载门禁设备id和ip
			while (true) {
				s = ss.accept();
				new ServerThread(s).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			AFRmain.free();
			System.out.println("引擎被销毁");
		}

	}

	public void start() {

	}

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		new Server();
//	}

}
