package cn.wjdc.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wjdc.dao.UserDao;
import cn.wjdc.entity.User;
import cn.wjdc.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserDao userDao;
	@Override
	public void addUser(User user) {
		userDao.addUser(user);
		
	}
	@Override
	public int findByOpenid(String openid) {
		// TODO Auto-generated method stub
		return userDao.findByOpenid(openid);
	}

}
