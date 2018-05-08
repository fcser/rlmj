package cn.java.rlmj.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @version :v1.0.0
 * @description :为用户产生唯一标识身份的key
 * @author: zym
 * @date: 2018-3-27下午9:13:38
 */
public class KeyValue {
	/**
	 * 使用ConcurrentHashMap,无须加锁，内部有分段锁，提高并发量，有几段就运行最多几个线程并发
	 * synchronized并发量为1，只能串行执行。
	 */
	public static volatile ConcurrentHashMap<String, Integer> keyMap = new ConcurrentHashMap<>();
	public static volatile ConcurrentHashMap<String, Integer> typeMap = new ConcurrentHashMap<>();

	// 加把锁
	public static String getKey(int id) {
		String a = null;
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		a = id + df.format(day);
		return a;
	}

	public static boolean checkKey(String key) {
		boolean a = false;
		if (keyMap.get(key) != null) {
			a = true;
		}
		return a;
	}

	public static int removeKey(String key) {
		return keyMap.remove(key);
	}
}
