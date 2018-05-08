package cn.java.rlmj.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TestThread extends Thread {
	Socket s=null;
	public TestThread(Socket s){
		this.s=s;
	}

	public void run(){
		System.out.println("进入副线程");
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
			System.out.println(br.readLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
