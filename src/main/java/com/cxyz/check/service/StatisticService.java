package com.cxyz.check.service;

import com.cxyz.check.dao.StatisticDao;
import com.cxyz.check.entity.Statistic;

public class StatisticService {
	private StatisticDao dao = new StatisticDao();
	
	public Statistic querySelfStatistic(String stu_id, Integer grade_id)
	{
		return dao.SelectOne(stu_id,grade_id);
	}
}
