package cn.java.rlmj.connect;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.ibatis.session.SqlSession;

import cn.java.rlmj.dao.EquipmentMapper;
import cn.java.rlmj.dao.SqlSessionFactoryUtils;
import cn.java.rlmj.dao.UnknowRecordMapper;
import cn.java.rlmj.dao.VisitorsRecordMapper;
import cn.java.rlmj.hrsdk.AFR_FSDK_FACEMODEL;
import cn.java.rlmj.hrsdk.ASVLOFFSCREEN;
import cn.java.rlmj.hrsdk.FaceInfo;
import cn.java.rlmj.pojo.UnknowRecord;
import cn.java.rlmj.pojo.VisitorsRecord;
import cn.java.rlmj.sdkdo.AFRmain;
import cn.java.rlmj.sdkdo.Face;
import cn.java.rlmj.sdkdo.FaceModels;
import cn.java.rlmj.service.RecordService;
import cn.java.rlmj.service.UnknowRecordService;
import cn.java.rlmj.service.impl.RecordServiceImpl;
import cn.java.rlmj.service.impl.UnknowRecordServiceImpl;

public class ServerThread extends Thread{

	private Socket s=null;
	private File file=null;
	private File dir=null;
	private BufferedImage img=null;
	private BufferedInputStream bin=null;
	private String name=null;
	private OpenDoor openDoor=null;
	public ServerThread(Socket s){
		this.s=s;
		openDoor=new OpenDoor();
		Date day=new Date();
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMddHHmmss");
		name=df.format(day);
		System.out.println(getClass().getResource("/").getFile().toString());
		//dir=new File("d:\\rlmjPic");
		dir=new File(Path.path+"\\strange");
		int count=1;
		if(!dir.exists()){
			dir.mkdir();
		}
		file=new File(dir,name+".jpg");
		while(file.exists()){
			file=new File(dir,name+"("+(count++)+")"+".jpg");
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		byte[] type=new byte[1];
		try {
			bin=new BufferedInputStream(s.getInputStream());
			bin.read(type);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("传过来数据="+type[0]+"转换后"+Integer.toBinaryString((type[0]&0xFF)+0x100));
		if(Integer.toBinaryString((type[0]&0xFF)+0x100).substring(1).equals("11111101")){//图片传输
			try {
				byte[] length=new byte[4];//图片大小
			    bin.read(length);
			    img = ImageIO.read(bin);
			    ASVLOFFSCREEN inputImg=AFRmain.changePic(img);
			    FaceInfo[] faceInfosA = AFRmain.doFaceDetection( inputImg);
			    if (faceInfosA.length < 1) {
			        System.out.println("no face in Image");
			        return;
			    }
			    AFR_FSDK_FACEMODEL model[]=new  AFR_FSDK_FACEMODEL[50];
			    int faceNum=faceInfosA.length;
			    for(int i=0;i<faceNum;i++){
				    model[i]=AFRmain.extractFRFeature( inputImg, faceInfosA[i]);
			    }
			    boolean isFace=false;
			    VisitorsRecord vs=new VisitorsRecord();
			    Face toCompareFace=null;
			    //RecordService rs=new RecordServiceImpl();
			    for(int i=0;i<faceNum;i++)
				    for(int j=0;j<FaceModels.facemodel.size();j++){
					    toCompareFace=FaceModels.facemodel.get(j);
					    if(AFRmain.toCompare(model[i], AFR_FSDK_FACEMODEL.fromByteArray(toCompareFace.model))>0.6){
						    //识别通过发送开门指令
						    vs.setPeople_id(toCompareFace.getId());
						    vs.setEquipment_id(EquipmentList.equipments.getKeyByValue(s.getInetAddress().getHostAddress()));
						    vs.setDatetime(getaTime());
						    isFace=true;
						    System.out.println("识别通过");
						    String ip=s.getInetAddress().getHostAddress();//获取终端ip
						    byte[] msg=openDoor.returnMsg();
						    openDoor.orderToDoor(msg, ip);//发送命令给终端
						    //RecordService rs=new RecordServiceImpl();
						    //rs.insertRecord(vs);//将这条访问记录插入数据库
						    //file.delete();//识别通过了，，删除缓存照片
						    insertRecord(vs);
						    break;
					    }
				    }
			    if(!isFace){
				    System.out.println("不是住户");
				    ImageIO.write(img,"jpg",file);
				    UnknowRecord un=new UnknowRecord();
				    un.setDatetime(getaTime());
				    un.setPicture(file.getName());
				    un.setEquipment_id(EquipmentList.equipments.getKeyByValue(s.getInetAddress().getHostAddress()));
				    //UnknowRecordService uns=new UnknowRecordServiceImpl();
				    //uns.insertRecord(un);
				    insertUnknowRecord(un);
			    }
			     //销毁人脸
			     for(int i=0;i<faceNum;i++){
			    	model[i].freeUnmanaged();
			     }
				 //ImageIO.write(img,name+".jpg",file);
				 System.out.println("执行到最后");
			 } catch ( Exception e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }
		 }else if(Integer.toBinaryString((type[0]&0xFF)+0x100).substring(1).equals("00001010")){
			 //开门结果,可能存在并发问题
			 //byte[] data=new byte[4];//menj���
			 byte[] result=new byte[1];
			 try {
				 //bin.read(data);
				 bin.read(result);
				 if(Integer.toBinaryString((result[0]&0xFF)+0x100).substring(1).equals("00000101")){
					 System.out.println("开门成功");
					 openDoor.setResult(true);
				 }else {
					 System.out.println("开门失败");
					 openDoor.setResult(false);
				 }
			 } catch (IOException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }
			
		 }else if(Integer.toBinaryString((type[0]&0xFF)+0x100).substring(1).equals("00001101")){
		
			 //请求同步时间
			 String ip=s.getInetAddress().getHostAddress();
			 openDoor.orderToDoor(getTime(), ip);
		 }else if(Integer.toBinaryString((type[0]&0xFF)+0x100).substring(1).equals("00001110")){
			//更新id
			byte[] data=new byte[4];
			try {
				bin.read(data);
				//String d=Integer.toBinaryString((data[0]&0xFF)+0x100).substring(1)+" "+Integer.toBinaryString((data[1]&0xFF)+0x100).substring(1)+" "+Integer.toBinaryString((data[2]&0xFF)+0x100).substring(1)+" "+Integer.toBinaryString((data[3]&0xFF)+0x100).substring(1);
				int id=byteToInt(data);//门禁id
				System.out.println("该门禁的id是："+id);
				//System.out.println("门禁id二进制形式："+d);
				String ip=s.getInetAddress().getHostAddress();//门禁ip
				EquipmentList.equipments.map.put(id, ip);
				byte[] msg=refleshId(data,(byte)1);//更新成功
				openDoor.orderToDoor(msg, ip);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			System.out.println("错误命令，无法执行");
		}
		
		
	}

	private boolean insertRecord(VisitorsRecord vs) {
		// TODO Auto-generated method stub
		SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
		boolean b=false;
		try {
			VisitorsRecordMapper equipmentMapper = sqlSession.getMapper(VisitorsRecordMapper.class);
			equipmentMapper.insertVisitorsRecord(vs);
			sqlSession.commit();
			b=true;
		}catch(Exception e){
			sqlSession.rollback();
			e.printStackTrace();
		}
		return b;
	}

	private static boolean insertUnknowRecord(UnknowRecord un) {
		SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
		boolean b=false;
		try {
			UnknowRecordMapper equipmentMapper = sqlSession.getMapper(UnknowRecordMapper.class);
			equipmentMapper.insertRecord(un);
			sqlSession.commit();
			b=true;
		}catch(Exception e){
			sqlSession.rollback();
			e.printStackTrace();
		}
		return b;
	}

	//开门命令
	/*public byte[] returnMsg(){
		Date day=new Date();
		SimpleDateFormat df=new SimpleDateFormat("HH:mm:ss");
		String time=df.format(day);
		String[] b=time.split(":");
		
		byte[] a=new byte[5];
		a[0]=(byte)10;
		a[1]=(byte)10;
		a[2]=(byte) Integer.parseInt(b[0]);
		a[3]=(byte) Integer.parseInt(b[1]);
		a[4]=(byte) Integer.parseInt(b[2]);
		return a;
	}*/
	public String getaTime() {
		Date day=new Date();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(day);
	}
	//ͬ同步时间
	public byte[] getTime(){
		Date day=new Date();
		SimpleDateFormat df=new SimpleDateFormat("HH:mm:ss");
		String time=df.format(day);
		String[] b=time.split(":");
		System.out.println("同步时间是："+df);
		byte[] a=new byte[4];
		a[0]=(byte)13;
		a[1]=(byte) Integer.parseInt(b[0]);
		a[2]=(byte) Integer.parseInt(b[1]);
		a[3]=(byte) Integer.parseInt(b[2]);
		return a;
	}
	//返回更新id结果
	public byte[] refleshId(byte[] b,byte isTrue){
		byte[] a=new byte[6];
		a[0]=(byte)14;
		a[1]=b[0];
		a[2]=b[1];
		a[3]=b[2];
		a[4]=b[3];
		a[5]=isTrue;
		return a;
	}
	//byte转int
	public int byteToInt(byte[] a){
		return (a[3]&0xFF)<<24|(a[2]&0xFF)<<16|(a[1]&0xFF)<<8|(a[0]&0xFF);
	}
}
