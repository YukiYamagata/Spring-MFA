package com.example.demo.web.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;

@Service
@Transactional(rollbackOn = Exception.class)
public class AccountService {

	@Autowired
	AccountRepository accountRepository;

	public void create(Account account) {
		accountRepository.save(account);
	}

	public Account findAccount(String accountId) {
		return accountRepository.findOne(accountId);
	}
}
