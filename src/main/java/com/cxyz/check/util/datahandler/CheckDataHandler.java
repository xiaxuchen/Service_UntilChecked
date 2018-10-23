package com.cxyz.check.util.datahandler;


import com.cxyz.check.dto.Check;
import com.cxyz.check.dto.RecordDetail;
import com.cxyz.check.dto.Task;
import com.cxyz.check.entity.CheckRecord;
import com.cxyz.check.entity.ClassRoom;
import com.cxyz.check.entity.Grade;
import com.cxyz.check.entity.TaskCompletion;
import com.cxyz.check.entity.TaskInfo;
import com.cxyz.check.entity.User;
import com.cxyz.check.util.Date;
import com.cxyz.check.util.DateTime;
import com.cxyz.check.util.GsonUtil;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.itcast.commons.CommonUtils;

public class CheckDataHandler {
	
	/**
	 * 将json转化为Task对象
	 * @return
	 */
	public static Task getTask()
	{
		Task task = new Task();
		TaskInfo info = new TaskInfo();
		info.set_id(CommonUtils.uuid());
		info.set_name("java");
		info.setClassRoom(new ClassRoom(21));
		info.setStart(new DateTime());
		info.setEnd(new DateTime());
		task.setInfo(info);
		List<TaskCompletion> tcs = new ArrayList<TaskCompletion>();
		TaskCompletion tc;
		for(int i =0;i<10;i++)
		{
			tc = new TaskCompletion();
			tc.setTaskInfo(new TaskInfo(info.get_id()));
			tc.setDate(new Date().setYear(2018).setMonth(10).setDay(18));
			tc.setState(0);
			tc.setUpdatetime(new DateTime());
			tcs.add(tc);
		}
		task.setTcs(tcs);
		return task;
	}
	
	public static Check getCheck(HttpServletRequest request)
	{
	
		Check check = GsonUtil.GsonToBean(request.getParameter("check"),Check.class);
		return check;
	}
	
	/**
	 * 将map转化为TaskCompletion
	 * @param map
	 * @return
	 */
	public static TaskCompletion MapTOTaskCompletion(Map<String,Object> map)
	{
		TaskCompletion tc = null;
		if(map!=null)
		{
			tc = CommonUtils.toBean(map, TaskCompletion.class);
			tc.setUpdatetime(DateTime.fromTS((Timestamp) map.get("update_time"),false));
			tc.setDate(Date.fromSDate((java.sql.Date)map.get("_date"),false));
			return tc;
		}
		return null;
	}
	
	public static List<CheckRecord> MapListToCRS(List<Map<String,Object>> mapList)
	{
		List<CheckRecord> crs = new ArrayList<CheckRecord>();
		if(mapList!=null)
		{
			for(Map map:mapList)
			{
				crs.add(MapToRecord(map));
			}
			return crs;
		}
		return null;
	}
	
	/**
	 * 将map转化为CheckRecord
	 * @param map
	 * @return
	 */
	public static CheckRecord MapToRecord(Map<String,Object> map)
	{
		CheckRecord cr = CommonUtils.toBean(map, CheckRecord.class);
		cr.setTaskCompletion(new TaskCompletion((Integer) map.get("completion_id")));
		cr.setStudent(new User((String) map.get("stu_id")));
		return cr;
	}
	
	/**
	 * 将map转化为TaskInfo
	 * @param map
	 * @return
	 */
	public static TaskInfo MapToTask(Map<String,Object> map)
	{
		TaskInfo ti =CommonUtils.toBean(map, TaskInfo.class);
		ti.setEnd(DateTime.fromTS(new Timestamp(((Time)map.get("end_time")).getTime()), true));
		ti.setSponser(new User((String)map.get("sponsor_id"),(Integer)map.get("s_type")));
		ti.setChecker(new User((String)map.get("checker_id")));
		ti.setClassRoom(new ClassRoom((Integer) map.get("room_id")));
		ti.setStart(DateTime.fromTS(new Timestamp(((Time)map.get("start_time")).getTime()), true));
		ti.setCompletion(new TaskCompletion((Integer)map.get("_id")));
		ti.setGrade(new Grade((Integer) map.get("grade_id")));
		ti.set_name(map.get("_name").toString());
		return ti;
	}
	
	public static List<TaskInfo> MapListToTasks(List<Map<String,Object>> mapList)
	{
		List<TaskInfo> tasks = new ArrayList<TaskInfo>();
		for(Map map:mapList)
		{
			tasks.add(MapToTask(map));
		}
		return tasks;
	}
	
	/**
	 * 将List<Map<String,Object>>转化为List<RecordDetail>
	 * @param mapList
	 * @return
	 */
	public static List<RecordDetail> MapListToRDs(List<Map<String,Object>> mapList)
	{
		List<RecordDetail> data = new ArrayList<RecordDetail>();
		RecordDetail bean = null;
		for(Map map :mapList)
		{
			bean = CommonUtils.toBean(map, RecordDetail.class);
			User sponsor = new User();
			sponsor.set_id((String)map.get("sponsor_id"));
			sponsor.setType((Integer) map.get("s_type"));
			User checker = new User();
			checker.set_id((String)map.get("checker_id"));
			checker.setType((Integer) map.get("c_type"));
			bean.setCheckTime(DateTime.fromTS((Timestamp)map.get("update_time")));
			bean.setSponsor(sponsor);
			bean.setChecker(checker);
			if(bean!=null)
			data.add(bean);
		}
		return data;
	}
	
}
