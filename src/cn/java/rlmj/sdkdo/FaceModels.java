package cn.java.rlmj.sdkdo;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * 
 * @version :v1.0.0
 * @description :����ģ�ͼ���
 * @author: zym
 * @date: 2018��2��10������1:07:36
 */
public class FaceModels {
    public static ArrayList<Face> facemodel=new ArrayList<Face>(); 
    
    public static boolean deleteFace(int id) {
    	boolean b=false;
    	/*for(Face a:facemodel) {
    		//性能不佳，，可以用更高级的查找算法
    		//可能会报错，迭代过程中集合不允许被改变
    		System.out.println("来删除人脸");
    		if(a.getId()==id) {
    			facemodel.remove(a);
    			b=true;
    		}
    	}*/
    	//这种方式可以避免异常的发生
    	Iterator<Face> it=facemodel.iterator();
    	while(it.hasNext()) {
    		Face a=it.next();
    		if(a.getId()==id) {
    			it.remove();
    		}
    	}
    	return b;
    }
    
}
