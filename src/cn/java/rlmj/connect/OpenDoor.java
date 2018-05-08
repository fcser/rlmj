package cn.java.rlmj.connect;


import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class OpenDoor {
	private boolean success;
	
	public void setResult(boolean success) {
		this.success=success;
	}
	public boolean openDoorResult()
	{
		return success;
	}
	
	public byte[] returnMsg(){
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
	}
	//生成随机字符串
	public String getRandomString(int length){
		String str="zxcvbnmasdfghjklqwertyuiopQWERTYUIOPLKJHGFDSAZXCVBNM1236987450";
		Random random=new Random();
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<length;i++) {
			int number=random.nextInt(62);
			sb.append(str.charAt(number));
		}
		return sb.toString();
	}
	//生成监控钥匙
	public byte[] watch(String key,int operation) {
		int length=key.length();
		System.out.println(length);
		char[] keys=key.toCharArray();
		System.out.println(keys.length);
		byte[] a=new byte[34];
		a[0]=(byte)16;//发送给两端的操作类型
		for(int i=1;i<=length;i++)
		{
			System.out.println(i);
			a[i]=(byte) keys[i-1];//生成通信钥匙
		}
		a[length+1]=(byte)operation;//执行的操作
		return a;
	}

	public boolean orderToDoor(byte[] b,String ip){
		boolean isOk=false;
		try{
		Socket qs=new Socket(ip,8888);
		OutputStream baos=qs.getOutputStream();
		//byte[] msg=refleshId(data,(byte)1);//更新成功
		//System.out.println("回复信息长度"+msg.length);
		baos.write(b);
		baos.flush();
		baos.close();
		qs.close();
		isOk=openDoorResult();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	    return isOk;
	}
}
