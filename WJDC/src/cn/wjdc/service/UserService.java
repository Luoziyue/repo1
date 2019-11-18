package cn.wjdc.service;

import cn.wjdc.entity.User;

public interface UserService {
	public void addUser(User user);
	
	public int findByOpenid(String openid);
}
