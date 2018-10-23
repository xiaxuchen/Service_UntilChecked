package com.cxyz.check.util.datahandler;

import com.cxyz.check.entity.College;
import com.cxyz.check.entity.Grade;
import com.cxyz.check.entity.User;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.itcast.commons.CommonUtils;


public class UserDataHandler {
	/**
	 * 通过request中的json串获取用户数据信息
	 * @param request
	 * @return
	 */
	public static User getUser(HttpServletRequest request)
	{
		User u = new User();
		u.set_id(request.getParameter("id"));
		u.setPwd(request.getParameter("pwd"));
		u.setType(Integer.parseInt(request.getParameter("type")));
		System.out.println(u.toString());
		return u;
	}

	/**
	 * 将dao层获取的数据进行装填
	 * @param map
	 * @param type
	 * @return
	 */
	public static User MapToUser(Map<String,Object> map,Integer type)
	{
		User u = CommonUtils.toBean(map, User.class);
		if(type == User.STUDNET) {
			Grade g = new Grade((Integer)map.get("grade_id"));
			g.set_name((String)map.get("grade_name"));
			u.setGrade(g);
			u.setType(type);
		}else if(type == User.TEACHER){
			College c = new College((Integer) map.get("college_id"));
			c.set_name((String) map.get("college_name"));
			u.setCollege(c);
			u.setType(type);
		}else
			return null;
		return u;
	}
	
}
