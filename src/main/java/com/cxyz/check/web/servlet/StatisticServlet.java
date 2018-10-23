package com.cxyz.check.web.servlet;



import com.cxyz.check.entity.Statistic;
import com.cxyz.check.service.StatisticService;
import com.cxyz.check.util.GsonUtil;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;

public class StatisticServlet extends BaseServlet {
	private StatisticService service = new StatisticService();
	public String querySelfStatistic(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		request.setCharacterEncoding("utf-8");
		Statistic statistic = service.querySelfStatistic(request.getParameter
				("id"),Integer.parseInt(request.getParameter("grade")));
		response.setCharacterEncoding("utf-8");
		response.getWriter().print(GsonUtil.GsonString(statistic));
		return null;
	}
}
