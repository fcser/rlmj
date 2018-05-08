package cn.java.rlmj.dao;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionFactoryUtils {

	private final static Class<SqlSessionFactoryUtils> LOCK=SqlSessionFactoryUtils.class;
	
	private static SqlSessionFactory sqlSessionFactory=null;
	private SqlSessionFactoryUtils(){}
	
	public static SqlSessionFactory getSqlSessionFactory(){
		synchronized(LOCK){
			if(sqlSessionFactory!=null){
				return sqlSessionFactory;
			}
			String resource="net/frank/configure/database/mybatis-config.xml";
			InputStream inputStream;
			try {
				inputStream=Resources.getResourceAsStream(resource);
				sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("数据库操作出错");
				e.printStackTrace();
				return null;
			}
			return sqlSessionFactory;
		}
	}
	
	public static SqlSession openSqlSession(){
		if(sqlSessionFactory==null){
			getSqlSessionFactory();
		}
		System.out.println("sqlsession打开成功");
		return sqlSessionFactory.openSession();
	}
}
