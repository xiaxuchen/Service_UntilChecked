package com.cxyz.check.web.servlet;


import com.cxyz.check.dto.Check;
import com.cxyz.check.dto.RecordDetail;
import com.cxyz.check.dto.Task;
import com.cxyz.check.entity.CheckRecord;
import com.cxyz.check.entity.TaskInfo;
import com.cxyz.check.exception.CheckException;
import com.cxyz.check.service.CheckService;
import com.cxyz.check.util.GsonUtil;
import com.cxyz.check.util.datahandler.CheckDataHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;

public class CheckServlet extends BaseServlet {
	private CheckService service = new CheckService();
	
	/**
	 * 添加考勤记录
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public String addRecords(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setCharacterEncoding("utf-8");
		Check c = CheckDataHandler.getCheck(request);
		List<CheckRecord> mapList = new ArrayList<CheckRecord>();
		try {
			service.addRecord(c);
		} catch (CheckException e) {
			e.printStackTrace();
			response.getWriter().print(e.getMessage());
		}
		response.getWriter().print("提交成功!");
		return null;
	}
	
	/**
	 * 添加考勤任务
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public String addTask(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setCharacterEncoding("utf-8");
		Task task = GsonUtil.GsonToBean(request.getParameter("task"),Task.class);
		try {
			service.addTask(task);
		} catch (CheckException e) {
			response.getWriter().print(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 通过考勤完成情况id获取不良记录
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public String getRecords(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setCharacterEncoding("utf-8");
		try {
			Check check = service.getCheck(1);
			response.getWriter().print(GsonUtil.GsonString(check));
		} catch (CheckException e) {
			e.printStackTrace();
		}
		return null;
	} 
	
	
	/**
	 * 通过用户id获取不良记录
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public String getRecordDetails(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		System.out.println((String)request
					.getParameter("id")+Integer.parseInt(request.
					getParameter("type")));
		response.setCharacterEncoding("utf-8");
		try {
			List<RecordDetail> rds = service.getRds((String)request
					.getParameter("id"),Integer.parseInt(request.
					getParameter("type")),request.getParameter("date"));
			response.getWriter().print(GsonUtil.GsonString(rds));
		} catch (CheckException e) {
			e.printStackTrace();
			response.getWriter().print(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 通过班级id和日期获取当天所有的考勤任务（可以理解为课程信息）
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public String getTaskInfos(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setCharacterEncoding("utf-8");
		try {
			/**
			 * 请求时需要传入班级id和日期字符串
			 */
			List<TaskInfo> tasks = service.getTasks
				(Integer.parseInt(request.getParameter
						("grade")),request.getParameter("date"));
			response.getWriter().print(GsonUtil.GsonString(tasks));
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().print(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 通过传入用户id和类型获取考勤任务
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public String checkComp(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setCharacterEncoding("utf-8");
		TaskInfo checkTask = service.checkTask(request.getParameter("id"),
				Integer.parseInt(request.getParameter("type")));
		response.getWriter().print(GsonUtil.GsonString(checkTask));
		return null;
	}
}
