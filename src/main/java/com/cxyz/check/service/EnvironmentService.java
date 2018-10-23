package com.cxyz.check.service;

import com.cxyz.check.dao.EnvironmentDao;

import java.sql.SQLException;
import java.util.List;

public class EnvironmentService {
	private EnvironmentDao dao = new EnvironmentDao();
	
	public void leadIn(List data,int type)
	{
		try {
			dao.addEnv(data, type);
		} catch (SQLException e) {
			System.out.println("我日，报错了");
			throw new RuntimeException();
		}
	}
	
	public List findAll(int type)
	{
		try {
			return dao.findAll(type);
		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
			return null;
		}
	}
	
	public Object find(int id,int type)
	{
		try {
			return dao.findEnvById(id, type);
		} catch (SQLException e) {
			System.out.println("异常");
			return null;
		}
	}
}
