package com.mage.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.mage.po.User;
import com.mage.util.DBUtil;

public class UserDao {

	/**
	 * 	根据uname查询数据库
	 * @param uname
	 * @return
	 */
	public User queryUserByUname(String uname) {
		User user = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			// 1、获取数据库连接
			connection = DBUtil.getConnection();
			// 2、定义sql语句
			String sql = "select * from tb_user where uname = ?";
			// 3、预编译
			preparedStatement = connection.prepareStatement(sql);
			// 4、设置参数，下表从1开始
			preparedStatement.setString(1, uname);
			// 5、执行查询，返回resultSet结果集
			resultSet = preparedStatement.executeQuery();
			// 6、判断并分析结果集，得到user对象
			while(resultSet.next()){
				user = new User();
				user.setHead(resultSet.getString("head"));
				user.setMood(resultSet.getString("mood"));
				user.setNick(resultSet.getString("nick"));
				user.setUname(uname);
				user.setUpwd(resultSet.getString("upwd"));
				user.setUserId(resultSet.getInt("userId"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 7、关闭资源
			DBUtil.close(resultSet, preparedStatement, connection);
		}
		// 8、返回user对象
		return user;
	}
	/**
	 * 	昵称唯一性
	 * @param userId
	 * @return 
	 */
	public Integer chickNick(String nick, Integer userId) {
		Integer count =0;
		Connection conn = null;
		PreparedStatement sta=null;
		ResultSet res = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select count(*) from tb_user where userId=? and nick=?";
			//预编译
			sta = conn.prepareStatement(sql);
			sta.setInt(1, userId);
			sta.setString(2, nick);
			res = sta.executeQuery();
			while(res.next()){
				count = res.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			// 关闭资源
			DBUtil.close(res, sta, conn);
		}
		
		return count;
	}
	/**
	 * 	个人中心的修改
	 * @param nick
	 * @param head
	 * @param mood
	 * @param userId
	 * @return
	 */
	public int update(String nick, String head, String mood, Integer userId) {
		int row = 0;
		Connection conn = null;
		PreparedStatement sta = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "update tb_user set nick=?,head=?,mood=? where userId=?";
			sta = conn.prepareStatement(sql);
			sta.setString(1, nick);
			sta.setString(2, head);
			sta.setString(3, mood);
			sta.setInt(4, userId);
			row = sta.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			DBUtil.close(null, sta, conn);
		}
		return row;
	}
	
}
