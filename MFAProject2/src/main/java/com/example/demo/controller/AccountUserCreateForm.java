package com.example.demo.controller;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class AccountUserCreateForm {
	@NotBlank
	@Length(min = 1, max = 18)
	private String userName;

	@NotEmpty
	@Length(min = 1, max = 255)
	private String password;

	@NotEmpty
	@Length(min = 1, max = 255)
	private String name;

	@NotEmpty
	@Email
	@Length(max = 255)
	private String mail;

	private String useMfa;

	/**
	 * コンストラクタ
	 */
	public AccountUserCreateForm() { }

	/**
	 * getter, setter
	 */
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getUseMfa() {
		return useMfa;
	}

	public void setUseMfa(String useMfa) {
		this.useMfa = useMfa;
	}
}
