package com.cxyz.check.service;

import com.cxyz.check.dao.CheckDao;
import com.cxyz.check.dao.UserDao;
import com.cxyz.check.dto.Check;
import com.cxyz.check.dto.RecordDetail;
import com.cxyz.check.dto.Task;
import com.cxyz.check.entity.TaskInfo;
import com.cxyz.check.exception.CheckException;

import java.util.List;

public class CheckService {
	private CheckDao dao = new CheckDao();
	
	private UserDao udao = new UserDao();
	
	public void addTask(Task task)
	{
		dao.addTask(task);
	}
	
	public void addRecord(Check c)
	{
		dao.addRecords(c);
	}
	
	public Check getCheck(int id) throws CheckException
	{
			return dao.findRecord(id);
	}
	
	public List<RecordDetail> getRds(String userid, Integer type, String date) throws CheckException
	{
		return dao.getRecordDetails(userid,type,date);
	}
	
	/**
	 * 获取所有的taskinfo
	 * @return
	 */
	public List<TaskInfo> getTasks(int grade, String date)
	{
		List<TaskInfo> tasks = dao.getTasks(grade,date);
		for(TaskInfo task:tasks)
		{
			/**
			 * 装填考勤人和发起人信息
			 */
			try {
				task.setSponser(udao.findUserById
						(task.getSponser().get_id(),task.
								getSponser().getType()));
				task.setChecker((udao.findUserById
						(task.getChecker().get_id(),task.
								getChecker().getType())));
			} catch (CheckException e) {
				e.printStackTrace();
			}
		}
		return tasks;
	}
	
	/**
	 * 确认当前是否有考勤任务，如果有则返回，没有则返回null
	 */
	public TaskInfo checkTask(String id,int type)
	{
		TaskInfo compByUId = dao.getCompByUId(id,type);
		System.out.println(compByUId);
		if(compByUId == null)
			return null;
		try {
			compByUId.setSponser(udao.
					findUserById(compByUId.getSponser().get_id(), compByUId.getType()));
		} catch (CheckException e) {
			e.printStackTrace();
		}
		return compByUId;
	}
	
}
