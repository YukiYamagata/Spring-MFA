package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.MyUser;
import com.example.demo.repository.UserDao;

@Service
public class RegistrationUserServiceImpl implements RegistrationUserService {

	private UserDao userDao;

	@Autowired
	public RegistrationUserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * ユーザアカウントを登録する。
	 * @param MyUser
	 */
	@Override
	public void register(MyUser user) {
		userDao.save(user);
	}

	/**
	 * userNameを検索条件に、DBに登録されているユーザを取得する。
	 * @param userName
	 * @return MyUser
	 */
	@Override
	public MyUser findUserByUserName(String userName) {
		return userDao.findUserByUserName(userName);
	}
}
