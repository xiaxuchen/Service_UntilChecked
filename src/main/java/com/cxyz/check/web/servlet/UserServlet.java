package com.cxyz.check.web.servlet;

import com.cxyz.check.entity.User;
import com.cxyz.check.exception.UserException;
import com.cxyz.check.service.UserService;
import com.cxyz.check.util.GsonUtil;
import com.cxyz.check.util.datahandler.UserDataHandler;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;

public class UserServlet extends BaseServlet{
	private UserService service = new UserService();
	
	public String leadIn(HttpServletRequest request,HttpServletResponse response)
	{
		return null;
	}
	
	public String login(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setCharacterEncoding("utf-8");
		User user = UserDataHandler.getUser(request);
		System.out.println(user.toString());
		User u = null;
		try {
			u = service.login(user);
		} catch (UserException e) {
			u = new User();
			u.setType(User.ERROR);
			u.set_name(e.getMessage());
			
		}
		response.getWriter().write(GsonUtil.GsonString(u));
		
		
		return null;
	}
	
	public String getGradeStus(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		int grade_id = Integer.parseInt(request.getParameter("grade"));
		List<User> gradeStus = service.getGradeStus(grade_id);
		response.setCharacterEncoding("utf-8");
		response.getWriter().write(GsonUtil.GsonString(gradeStus));
		return null;
	}
	
}
