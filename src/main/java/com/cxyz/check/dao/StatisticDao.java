package com.cxyz.check.dao;

import com.cxyz.check.entity.Statistic;
import com.cxyz.check.exception.StatisticException;
import com.cxyz.check.util.DateTime;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.JdbcUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class StatisticDao {
	private QueryRunner qr = new TxQueryRunner();
	/**
	 * 从统计表中通过学生id查询一条记录
	 */
	public Statistic SelectOne(String stu_id,Integer grade_id)
	{
		String sql = "SELECT * FROM tb_statistics WHERE stu_id=?";
		String countSql = "SELECT count(*) FROM tb_taskinfo t " +
				"JOIN tb_completion c ON t._id=c.taskinfo_id " +
				"WHERE c.state=1 AND t.grade_id=?";
		Connection conn = null;
		try {
			conn = JdbcUtils.getConnection();
			Map<String, Object> map = qr.query(conn,sql,new MapHandler(),stu_id);
			Statistic statistic = CommonUtils.toBean(map, Statistic.class);
			statistic.setUpdate(DateTime.fromTS((Timestamp)map.get("update_time")));
			statistic.setTimes(((Number) qr.query(countSql,
					new ScalarHandler(),grade_id)).intValue());
			return statistic;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally
		{
			try {
				JdbcUtils.releaseConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 每次提交考勤记录的时候进行一个统计表的更新
	 * @param mapList
	 * @param connection 当进行事务的时候传入的连接
	 * @throws StatisticException
	 */
	public void updateList(Connection connection,List<Map<String,String>> mapList) throws StatisticException {
		Connection conn = null;
		try {
			if(connection != null)
				conn = connection;
			else
				conn = JdbcUtils.getConnection();
			for(Map map : mapList)
			{
				String sql = "update tb_statistics set "+map.get("function")
						+"="+map.get("function")+"+1 where stu_id=?";
					qr.update(conn,sql,map.get("stu_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new StatisticException("提交失败");
		}finally {
			try {
				JdbcUtils.releaseConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
