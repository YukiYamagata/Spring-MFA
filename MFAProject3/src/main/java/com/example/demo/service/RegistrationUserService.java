package com.example.demo.service;

import com.example.demo.entity.MyUser;

public interface RegistrationUserService {
    public void register(MyUser user);
    public MyUser findUserByUserName(String userName);
}
