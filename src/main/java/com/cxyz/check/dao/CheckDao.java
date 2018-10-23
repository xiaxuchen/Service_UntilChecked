package com.cxyz.check.dao;


import com.cxyz.check.dto.Check;
import com.cxyz.check.dto.RecordDetail;
import com.cxyz.check.dto.Task;
import com.cxyz.check.entity.CheckRecord;
import com.cxyz.check.entity.OtherState;
import com.cxyz.check.entity.TaskCompletion;
import com.cxyz.check.entity.TaskInfo;
import com.cxyz.check.entity.User;
import com.cxyz.check.exception.CheckException;
import com.cxyz.check.util.Date;
import com.cxyz.check.util.DateTime;
import com.cxyz.check.util.datahandler.CheckDataHandler;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.JdbcUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class CheckDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 添加大量的任务
	 * @param tasks
	 * @throws CheckException
	 */
	public void addTasks(List<Task> tasks) throws CheckException
	{
		Connection conn = null;
		try
		{
			JdbcUtils.beginTransaction();
			conn = JdbcUtils.getConnection();
			for(Task task:tasks)
			{
				TaskInfo info = task.getInfo();
				qr.update(conn,"INSERT INTO tb_taskinfo VALUES(?,?,?,?,?," +
						"?,?,?,?,?,?)",new Object[]{info.get_id(),info.
						get_name(),info.getSponser().get_id(),info.
						getChecker().get_id(),info.getStart().getTime(),info.
						getEnd().getTime(),info.getType(),
						info.getGrade().get_id(),info.getSponser().getType(),
						info.getChecker().getType(),info.
						getClassRoom().get_id()});
				List<TaskCompletion> tcs = task.getTcs();
				Object params[][] = new Object[tcs.size()][];
				int i = 0;
				for(TaskCompletion tc:tcs)
				{
					params[i] = new Object[]{tc.get_id(),tc.getDate().toString(),
							tc.getState(), Date.fromUDate(new java.util.Date()).toString(),
							info.get_id()};
					i++;
				}
				qr.batch(conn,"INSERT INTO tb_completion VALUES(?,?,?,?,?)", params);
			}
			JdbcUtils.commitTransaction();
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
				throw new CheckException("导入失败！请检查格式！");
			} catch (SQLException e1) {
				throw new CheckException("出大问题了，没回滚！");
			}
		}finally{
			try {
				JdbcUtils.releaseConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 添加单个的考勤，一般做生成临时考勤
	 * @param task
	 * @throws CheckException
	 */
	public void addTask(Task task) throws CheckException
	{
		Connection conn = null;
		try
		{
			JdbcUtils.beginTransaction();
			conn = JdbcUtils.getConnection();
			TaskInfo info = task.getInfo();
			qr.update("INSERT INTO tb_taskinfo VALUES(?,?,?,?,?," +
					"?,?,?,?,?,?)",new Object[]{info.get_id(),info.
					get_name(),info.getSponser().get_id(),info.
					getChecker().get_id(),info.getStart().getTime(),info.getEnd()
					.getTime(),info.getType(),
					info.getGrade().get_id(),info.getSponser().getType(),
					info.getChecker().getType(),info.
					getClassRoom().get_id()});
			List<TaskCompletion> tcs = task.getTcs();
			Object params[][] = new Object[tcs.size()][];
			int i = 0;
			for(TaskCompletion tc:tcs)
			{
				params[i] = new Object[]{tc.get_id(),tc.getDate().toString(),
						tc.getState(),Date.fromUDate(new java.util.Date()).toString(),info.get_id()};
				i++;
			}
			qr.batch(conn,"INSERT INTO tb_completion VALUES(?,?,?,?,?)", params);
			JdbcUtils.commitTransaction();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				JdbcUtils.rollbackTransaction();
				throw new CheckException("导入失败！");
			} catch (SQLException e1) {
				throw new CheckException("出大问题了，没回滚！");
			}
		}finally{
			try {
				JdbcUtils.releaseConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 提交考勤结果
	 * @param c
	 * @throws CheckException
	 */
	public void addRecords(Check c) throws CheckException
	{
		System.out.println(c);
		List<CheckRecord> records = c.getRecords();
		System.out.println(c.getRecords().size());
		List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
		Map<String,String> map = null;
		if(c.getRecords()!=null)
		{
			if(!records.isEmpty())
			{
				for(CheckRecord record:records)
				{
					map = new HashMap<String,String>();
					map.put("function",checkFunction(record.getResult()));
					map.put("stu_id",record.getStudent().get_id());
					mapList.add(map);
				}
			}
		}
		Connection conn = null;
		try
		{
			JdbcUtils.beginTransaction();
			conn = JdbcUtils.getConnection();
			System.out.println(qr.update("UPDATE tb_completion SET state=?,update_time=?" +
					" WHERE _id=?",c.getCompletion().getState(),(DateTime.fromUDate(new java.util.Date(), true)).toString()
					,c.getCompletion().get_id()));
			if(c.getCompletion().getState() == TaskCompletion.NORMAL)
			{
				System.out.println(c.getRecords().size());
				Object params[][] = new Object[c.getRecords().size()][];
				int i = 0;
				for(CheckRecord cr:c.getRecords())
				{
					params[i] = new Object[]{cr.get_id(),c.getCompletion().
							get_id(),cr.getStudent().get_id(),cr.getDes(),
							cr.getResult()};
					i++;
				}
				System.out.println(qr.batch(conn,"INSERT INTO tb_record VALUES(?,?,?,?,?)", params));
				new StatisticDao().updateList(conn,mapList);
			}else if(c.getCompletion().getState() == TaskCompletion.OTHER){
				qr.update("INSERT INTO tb_otherstate VALUES(?,?)",c.getCompletion().get_id(),c.getState().getDes());
			}
			JdbcUtils.commitTransaction();
		}catch (Exception e) {
			System.out.print("提交失败");
			try {
				JdbcUtils.rollbackTransaction();
				throw new CheckException("提交失败");
			} catch (SQLException e1) {
				e1.printStackTrace();
				System.out.println("回滚也失败了");
			}
		}finally{
			try {
				JdbcUtils.releaseConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private String checkFunction(int rs) {
		String function = null;
		switch (rs)
		{
			case CheckRecord.VACATE:function="_leave";break;
			case CheckRecord.LATE:function="late";break;
			case CheckRecord.ABSENTEEISM:function="early_leave";break;
			case CheckRecord.EARLYLEAVE:function="truant";break;
		}
		return function;
	}

	/**
	 * 通过考勤完成情况id获取考勤信息
	 * @param id
	 * @return
	 * @throws CheckException
	 */
	public Check findRecord(int id) throws CheckException
	{
		Check c = new Check();
		try {
			 Map<String, Object> map = qr.query("SELECT * FROM tb_completion WHERE _id=?",
					new MapHandler(),id);
			if(map==null)
				 throw new CheckException("无此考勤任务");
			System.out.println(map.toString());
			if((Integer)map.get("state") == TaskCompletion.NORMAL)
			{
				List<Map<String, Object>> listMap = qr.query("SELECT * FROM tb_record WHERE " +
					"completion_id=? AND result!=-5",new MapListHandler(),id);
				c.setRecords(CheckDataHandler.MapListToCRS(listMap));
			}else if((Integer)map.get("state") == TaskCompletion.OTHER)
			{
				Map<String, Object> osmap = qr.query("SELECT * FROM tb_otherstate WHERE _id=?", 
						new MapHandler(),id);
				OtherState os = CommonUtils.toBean(osmap, OtherState.class);
				os.setTaskCompletion(new TaskCompletion((Integer) osmap.get("completion_id")));
				c.setState(os);
			}
			c.setCompletion(CheckDataHandler.MapTOTaskCompletion(map));
			return c;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	/**
	 * 通过班级id获取改班级所有的任务信息
	 * @param grade
	 * @return
	 */
	public List<TaskInfo> getTasks(int grade,String date)
	{
		Connection conn = null;
		try {
			String sql ="SELECT * FROM tb_taskinfo t,tb_completion c " +
					"WHERE t._id=c.taskinfo_id and c._date=? and t.grade_id=?";
			conn = JdbcUtils.getConnection();
			List<Map<String, Object>> mapList = qr.query(conn,sql,new MapListHandler(),new Object[]{date,grade});
			if(mapList == null)
				return null;
			List<TaskInfo> tasks = CheckDataHandler.MapListToTasks(mapList);
			return tasks;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(conn!=null)
				try {
					JdbcUtils.releaseConnection(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return null;
	}
	
	/**
	 * 通过用户id查询用户所有的不良考勤记录
	 * @param userid 用户id
	 * @param type 查询的记录类型，传入null查询所有
	 * @return
	 * @throws CheckException 当发生sql异常的时候抛出
	 * @throws SQLException 
	 */
	public List<RecordDetail> getRecordDetails(String userid, Integer type, String date) throws CheckException
	{
		String sql = "select t._name,t.sponsor_id,t.s_type," +
				"t.checker_id,t.c_type,c._id,c.update_time," +
				"r.result,r.des from (tb_record r JOIN tb_completion" +
				" c on r.completion_id=c._id) JOIN tb_taskinfo" +
				" t on t._id=c.taskinfo_id where r.stu_id=?";
		Object params[] = new Object[]{userid};
		if(type != CheckRecord.ALL)
		{
			sql+="and r.result=?";
			params = new Object[]{userid,type};
		}
		
		if(date != null)
		{
			sql+="and c._date=?";
			if(type != CheckRecord.ALL)
				params = new Object[]{userid,type,date};
			else
				params = new Object[]{userid,date};
		}
		
		Connection conn = null;
		try {
			conn = JdbcUtils.getConnection();
			List<Map<String, Object>> mapList = qr.query(conn,sql, new MapListHandler(),params);
			if(mapList == null)
				return null;
			List<RecordDetail> rds = CheckDataHandler.MapListToRDs(mapList);
			for(RecordDetail rd:rds)
			{
				User checker = rd.getChecker();
				User sponsor = rd.getSponsor();
				checker.set_name(getName(conn,checker.get_id(),checker.getType()));
				sponsor.set_name(getName(conn,sponsor.get_id(),sponsor.getType()));
			}
			return rds;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CheckException("查询失败");
		}finally{
			try {
				JdbcUtils.releaseConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 通过用户id检查当前是否有考勤任务
	 * @param id 用户id
	 * @return
	 */
	public TaskInfo getCompByUId(String id,int type)
	{
		String sql ="SELECT t.*,c._id FROM tb_taskinfo t,tb_completion c " +
				"WHERE t._id=c.taskinfo_id and c._date=?" +
				"and ? between t.start_time and t.end_time " +
				"and t.checker_id=? and t.c_type=? and c.state=?";
		Map<String, Object> map;
		DateTime dateTime = DateTime.fromUDate(new java.util.Date());
		try {
			map = qr.query(sql,new MapHandler(),new 
					Object[]{dateTime.getDate(),dateTime.
				getTime(),id,type,TaskCompletion.WAIT_CHECK});
			if(map == null)
				return null;
			TaskInfo task = CheckDataHandler.MapToTask(map);
			return task;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String getName(Connection conn,String id,int type)
	{
		String s_sql = "SELECT _name FROM tb_stu where _id=?";
		String t_sql = "SELECT _name FROM tb_tea where _id=?";
		try {
		if(type == User.STUDNET)
			return (String)qr.query(conn,s_sql,new ScalarHandler(),id);
		else if(type == User.TEACHER)
			return (String)qr.query(conn,t_sql,new ScalarHandler(),id);
		} catch (SQLException e) {
				e.printStackTrace();
			}
		return null;
	}
	
}
