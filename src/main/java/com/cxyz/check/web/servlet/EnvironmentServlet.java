package com.cxyz.check.web.servlet;

import com.cxyz.check.exception.EnvironmentException;
import com.cxyz.check.service.EnvironmentService;
import com.cxyz.check.util.datahandler.EnvironmentDataHandler;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;

public class EnvironmentServlet extends BaseServlet {
	
	public static final int TYPE_SCHOOL = 0;
	
	public static final int TYPE_COLLEGE = 1;
	
	public static final int TYPE_GRADE = 2;
	
	public static final int TYPE_CLASSROOM = 3;
	
	private EnvironmentService service = new EnvironmentService();
	/**
	 * 导入数据,通过请求中的type参数判断导入什么类型的数据
	 * @param request
	 * @param response
	 * @return
	 */
	public String leadIn(HttpServletRequest request, HttpServletResponse response)
	{
		int type = Integer.parseInt(request.getParameter("type"));
		List data = EnvironmentDataHandler.getDataList(request);
		try {
			service.leadIn(data, type);
		} catch (EnvironmentException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 查找
	 * @param request
	 * @param response
	 * @return
	 */
	public String find(HttpServletRequest request, HttpServletResponse response)
	{
		int type = Integer.parseInt(request.getParameter("type"));
		int id = Integer.parseInt(request.getParameter("id"));
		Object obj = service.find(id, type);
		if(obj!=null)
			System.out.println(obj);
		switch(type)
		{
		case EnvironmentServlet.TYPE_CLASSROOM:break;
		case EnvironmentServlet.TYPE_COLLEGE:break;
		case EnvironmentServlet.TYPE_GRADE:break;
		case EnvironmentServlet.TYPE_SCHOOL:break;
		}
		
		return null;
	}
	
	/**
	 * 查看全部
	 * @param request
	 * @param response
	 * @return
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response)
	{
		int type = Integer.parseInt(request.getParameter("type"));
		List data = service.findAll(type);
		if(data!=null)
			System.out.println(data.toString());
		return null;
	}
	
	public String update(HttpServletRequest request, HttpServletResponse response)
	{
		int type = Integer.parseInt(request.getParameter("type"));
		List data = EnvironmentDataHandler.getDataList(request);
		
		
		return null;
	}
	
	public String delete(HttpServletRequest request, HttpServletResponse response)
	{
		return null;
	}

}
