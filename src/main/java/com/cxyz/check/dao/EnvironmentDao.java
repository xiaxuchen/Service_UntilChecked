package com.cxyz.check.dao;

import com.cxyz.check.entity.ClassRoom;
import com.cxyz.check.entity.College;
import com.cxyz.check.entity.Grade;
import com.cxyz.check.entity.School;
import com.cxyz.check.exception.EnvironmentException;
import com.cxyz.check.util.datahandler.EnvironmentDataHandler;
import com.cxyz.check.web.servlet.EnvironmentServlet;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cn.itcast.jdbc.TxQueryRunner;

public class EnvironmentDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 通过id查询数据
	 * @param type
	 * @throws SQLException 
	 */
	public Object findEnvById(int id,int type) throws SQLException
	{
		Object obj = null;
		String table = null;
		switch(type)
		{
		case EnvironmentServlet.TYPE_CLASSROOM:table = "classroom";break;
		case EnvironmentServlet.TYPE_COLLEGE:table = "college";break;
		case EnvironmentServlet.TYPE_GRADE:table = "grade";break;
		case EnvironmentServlet.TYPE_SCHOOL:table = "school";break;
		}
		String sql1 = "SELECT * FROM ";
		String sql2 = " WHERE _id=1";
		String sql = sql1+table+sql2;
		Map<String, Object> map = qr.query(sql,new MapHandler());
		return EnvironmentDataHandler.MapToObject(map, type);
	}
	
	/**
	 * 查询所有数据
	 * @return
	 * @throws SQLException
	 */
	
	public List findAll(int type) throws SQLException
	{
		List<Map<String,Object>> list = null;
		String table = null;
		String sql = "SELECT * FROM ";
		switch(type)
		{
		case EnvironmentServlet.TYPE_CLASSROOM:table  = "classroom";
				list = qr.query(sql+table,
						new MapListHandler());break;
		case EnvironmentServlet.TYPE_COLLEGE:table = "college";
				list = qr.query(sql+table, 
						new MapListHandler());break;
		case EnvironmentServlet.TYPE_GRADE:table = "grade";
				list = qr.query(sql+table, 
						new MapListHandler());break;
		case EnvironmentServlet.TYPE_SCHOOL:table = "school";
				list = qr.query(sql+table, 
						new MapListHandler());break;
		}
		return EnvironmentDataHandler.MapListToListObject(list, type);
	}
	
	/**
	 * 导入环境（学校，学院，班级，教室）相关数据
	 * @throws SQLException 
	 * @throws EnvironmentException
	 */
	public void addEnv(List data,int type) throws SQLException, EnvironmentException
	{
		switch(type)
		{
		case EnvironmentServlet.TYPE_CLASSROOM:addClassRoom(data);break;
		case EnvironmentServlet.TYPE_COLLEGE:break;
		case EnvironmentServlet.TYPE_GRADE:addGrade(data);break;
		case EnvironmentServlet.TYPE_SCHOOL:addSchool(data);break;
		}
	}

	/**
	 * 添加教室
	 * @param data
	 * @throws SQLException 插入数据可能异常
	 * @throws EnvironmentException 如果数据已存在则抛出此异常
	 */
	private void addClassRoom(List<ClassRoom> data) throws SQLException, EnvironmentException {
		Object params[][] = new Object[data.size()][];
		String sql = "INSERT INTO classroom VALUES(?,?,?,?)";
		//查询语句，用来查询表中是否已经包含了该数据
		String selSql = "SELECT count(*) FROM classroom WHERE _id=?";
		int i = 0;
		for(ClassRoom cr:data)
		{
			Number count = (Number)qr.query(selSql, new ScalarHandler(),cr.get_id());
			if(count.intValue()!=0)
			{
				throw new EnvironmentException("_id为"+cr.get_id()+"的班级已存在");
			}
			params[i] = new Object[]{cr.get_id(),cr.get_name(),cr.getCollege().get_id(),cr.getState()};
			i++;
		}
		params.toString();
		qr.batch(sql, params);
	}
	/**
	 * 添加学校
	 * @param data
	 * @throws SQLException
	 * @throws EnvironmentException
	 */
	private void addSchool(List<School> data) throws SQLException, EnvironmentException
	{
		Object params[][] = new Object[data.size()][];
		String sql = "INSERT INTO school VALUES(?,?,?)";
		//查询语句，用来查询表中是否已经包含了该数据
		String selSql = "SELECT count(*) FROM school WHERE _id=?";
		int i = 0;
		for(School s:data)
		{
			Number count = (Number)qr.query(selSql, new ScalarHandler(),s.get_id());
			if(count.intValue()!=0)
			{
				throw new EnvironmentException("_id为"+s.get_id()+"的学校已存在");
			}
			params[i] = new Object[]{s.get_id(),s.get_name(),s.getManager().get_id()};
			i++;
		}
		params.toString();
		qr.batch(sql, params);
	}
	/**
	 * 添加班级
	 * @param data
	 * @throws EnvironmentException
	 * @throws SQLException
	 */
	private void addGrade(List<Grade> data) throws EnvironmentException, SQLException
	{
		Object params[][] = new Object[data.size()][];
		String sql = "INSERT INTO grade VALUES(?,?,?,?,?)";
		//查询语句，用来查询表中是否已经包含了该数据
		String selSql = "SELECT count(*) FROM grade WHERE _id=? OR classroom=?";
		int i = 0;
		for(Grade g:data)
		{
			Number count = (Number)qr.query(selSql, new ScalarHandler(),g.get_id(),g.getClassRoom().get_id());
			if(count.intValue()!=0)
			{
				throw new EnvironmentException("_id为"+g.get_id()+"的班级已存在或者晚自习教室重复");
			}
			params[i] = new Object[]{g.get_id(),g.get_name(),g.getCollege().get_id(),g.getClassRoom().get_id(),g.getHeadTeacher().get_id()};
			i++;
		}
		params.toString();
		qr.batch(sql, params);
	}
	/**
	 * 添加学院
	 * @param data
	 * @throws EnvironmentException
	 * @throws SQLException
	 */
	private void addCollege(List<College> data) throws EnvironmentException, SQLException{
		Object params[][] = new Object[data.size()][];
		String sql = "INSERT INTO college VALUES(?,?,?,?)";
		//查询语句，用来查询表中是否已经包含了该数据
		String selSql = "SELECT count(*) FROM college WHERE _id=?";
		int i = 0;
		for(College c:data)
		{
			Number count = (Number)qr.query(selSql, new ScalarHandler(),c.get_id());
			if(count.intValue()!=0)
			{
				throw new EnvironmentException("_id为"+c.get_id()+"的学院已存在");
			}
			params[i] = new Object[]{c.get_id(),c.get_name(),c.getSchool().get_id(),c.getManager().get_id()};
			i++;
		}
		params.toString();
		qr.batch(sql, params);
	}
}
