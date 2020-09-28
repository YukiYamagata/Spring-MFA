package com.example.demo.service;

import com.example.demo.entity.MyUser;

public interface AccountUserService {
	public void create(MyUser user);
	public MyUser findUserByUserName(String userName);
}
