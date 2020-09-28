package com.example.demo.entity;

import java.io.Serializable;

import org.jboss.aerogear.security.otp.api.Base32;

public class MyUser implements Serializable{
    private String userName; // ---------------- (1)

    private String password; // ---------------- (2)

    private String name; // -------------------- (3)

    private String roleName; // ---------------- (4)

    private boolean isUsing2FA;

    private String secret;

    public MyUser() {
		super();
		this.isUsing2FA = true;
		this.secret = Base32.random();
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

	public boolean isUsing2FA() {
		return isUsing2FA;
	}

	public void setUsing2FA(boolean isUsing2FA) {
		this.isUsing2FA = isUsing2FA;
	}

	public String getSecret() {
		return secret;
	}
}