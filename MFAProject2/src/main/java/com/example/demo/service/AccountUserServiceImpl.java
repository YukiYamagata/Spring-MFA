package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.MyUser;
import com.example.demo.repository.UserDao;

@Service
public class AccountUserServiceImpl implements AccountUserService {

	private UserDao userDao;

	@Autowired
	public AccountUserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public void create(MyUser user) {
		userDao.save(user);
	}

	@Override
	public MyUser findUserByUserName(String userName) {
		return userDao.findUserByUserName(userName);
	}
}
