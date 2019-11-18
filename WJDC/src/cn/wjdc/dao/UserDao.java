package cn.wjdc.dao;

import cn.wjdc.entity.User;

public interface UserDao {
	public void addUser(User user);
	
	public int findByOpenid(String openid);
}
