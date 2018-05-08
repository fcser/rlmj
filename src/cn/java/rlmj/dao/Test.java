package cn.java.rlmj.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.java.rlmj.pojo.Admin;

public class Test {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Logger log = LogManager.getLogger(Test.class);
		SqlSession sqlSession = null;
		try {
			// System.out.println("test0");
			sqlSession = SqlSessionFactoryUtils.openSqlSession();
			// System.out.println("test");
			AdminMapper adminMapper = sqlSession.getMapper(AdminMapper.class);
			// System.out.println("test1");
			// Role role=roleMapper.getRole(3L);
			Admin role = new Admin();
			role.setName("wiki");
			role.setPhone("15757515273");
			role.setPassword("123456");
			// System.out.println("test2");
			int a = adminMapper.insertAdmin(role);
			System.out.println("test2" + a);
			sqlSession.commit();// 提交事务
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

}
