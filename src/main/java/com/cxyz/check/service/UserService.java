package com.cxyz.check.service;

import com.cxyz.check.dao.UserDao;
import com.cxyz.check.entity.User;
import com.cxyz.check.exception.UserException;

import java.util.List;

public class UserService {
	private UserDao dao = new UserDao();
	
	public User login(User u) throws UserException
	{
		User user = dao.findUserById(u.get_id(),u.getType());
		if(!user.getPwd().equals(u.getPwd()))
			throw new UserException("密码错误！");
		return user;
	}
	
	public List<User> getGradeStus(int grade)
	{
		return dao.getGradeStus(grade);
	}
} 
