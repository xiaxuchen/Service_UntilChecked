package com.cxyz.check.dao;

import com.cxyz.check.entity.User;
import com.cxyz.check.exception.UserException;
import com.cxyz.check.util.datahandler.UserDataHandler;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.itcast.jdbc.TxQueryRunner;

public class UserDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 通过id和type获取用户信息
	 * @param id
	 * @param type
	 * @return
	 * @throws UserException
	 */
	public User findUserById(String id,int type) throws UserException
	{
		String sql = null;
		if(type == User.STUDNET)
			sql = "SELECT * FROM tb_stu WHERE _id=?";
		else
			sql = "SELECT * FROM tb_tea WHERE _id=?";
		Map<String, Object> map = null;
		try {
			map = qr.query(sql, new MapHandler(),id);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
		
		if(map == null)
			throw new UserException("用户名错误！");
		
		return UserDataHandler.MapToUser(map,type);
	}
	
	public List<User> getGradeStus(int grade)
	{
		List<User> list = new ArrayList<User>();
		List<Map<String,Object>> mapList = null;
		String sql = "SELECT * FROM tb_stu WHERE grade_id=?";
		try {
			mapList = qr.query(sql,new MapListHandler(),grade);
			if(mapList == null)
				return null;
			for(Map map:mapList)
			{
				User stu = (User) UserDataHandler.MapToUser(map, User.STUDNET);
				list.add(stu);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}
}
