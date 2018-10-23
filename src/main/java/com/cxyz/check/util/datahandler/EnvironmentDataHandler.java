package com.cxyz.check.util.datahandler;

import com.cxyz.check.entity.ClassRoom;
import com.cxyz.check.entity.College;
import com.cxyz.check.entity.Grade;
import com.cxyz.check.entity.School;
import com.cxyz.check.entity.User;
import com.cxyz.check.web.servlet.EnvironmentServlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.itcast.commons.CommonUtils;


public class EnvironmentDataHandler {

	/**
	 * 通过type和传入的map将map中的数据转化为相应的对象
	 * @param map map型的数据
	 * @param type 类型
	 * @return 转化为Object类型的数据，使用的时候需要强转为相应的数据类型
	 */
	public static Object MapToObject(Map map,int type)
	{
		if(map == null)
			return null;
		Object obj = null;
		if(type == EnvironmentServlet.TYPE_CLASSROOM)
		{
			ClassRoom cr = CommonUtils.toBean(map, ClassRoom.class);
			cr.setCollege(new College((Integer) map.get("college_id")));
			obj = cr;
		}
		else if(type == EnvironmentServlet.TYPE_SCHOOL)
		{
			School s = CommonUtils.toBean(map, School.class);
			s.setManager(new User((String) map.get("teacher_id")));
			obj = s;
		}
		else if(type == EnvironmentServlet.TYPE_GRADE)
		{
			Grade g = CommonUtils.toBean(map, Grade.class);
			g.setClassRoom(new ClassRoom((Integer) map.get("classroom_id")));
			g.setCollege(new College((Integer) map.get("college_id")));
			g.setHeadTeacher(new User((String) map.get("teacher_id")));
			obj = g;
		}
		else if(type == EnvironmentServlet.TYPE_COLLEGE)
		{
			College c = CommonUtils.toBean(map,College.class);
			c.setManager(new User((String) map.get("teacher_id")));
			c.setSchool(new School((Integer) map.get("school_id")));
			obj = c;
		}
		return obj;
	}

	/**
	 * 把请求的参数变成list集合对象返回
	 * @param request
	 * @return
	 */
	public static List getDataList(HttpServletRequest request) {
		ArrayList list = new ArrayList();
		Grade g = new Grade();
		g.set_name("你好");
		list.add(g);
		return list;
	}
	
	/**
	 * 通过type和传入的List<Map>将list中的数据转化为相应的List<对象>
	 * @param list 
	 * @param type
	 * @return 
	 */
	public static List MapListToListObject(List<Map<String,Object>> list,int type)
	{
		if(list==null)
			return null;
		List data = new ArrayList();
		for(Map map:list)
		{
			Object obj = MapToObject(map, type);
			data.add(obj);
		}
		return data;
	}
	
	/**
	 * 把请求参数变成对象返回
	 * @param request
	 * @return
	 */
	public static Object getObject(HttpServletRequest request)
	{
		return null;
	}
}
