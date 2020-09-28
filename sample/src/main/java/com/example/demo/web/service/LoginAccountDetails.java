package com.example.demo.web.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import com.example.demo.entity.Account;

import lombok.Getter;

public class LoginAccountDetails extends User {

	private static final long serialVersionUID = 1L;

	@Getter
	private final Account account;

	public LoginAccountDetails(Account account) {
		super(account.getAccountId(), account.getPassword(), AuthorityUtils
				.createAuthorityList("ROLE_USER"));
		this.account = account;
	}
}
