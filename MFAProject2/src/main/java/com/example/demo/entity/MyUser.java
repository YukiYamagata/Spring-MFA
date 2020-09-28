package com.example.demo.entity;

import java.io.Serializable;

public class MyUser implements Serializable{
    private String userName;

    private String password;

    private String name;

    private String roleName;

    private String mail;

    private boolean useMfa;

    private String secretKey;

	public MyUser() {
		super();
	}

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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public boolean isUseMfa() {
		return useMfa;
	}

	public void setUseMfa(boolean useMfa) {
		this.useMfa = useMfa;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
}