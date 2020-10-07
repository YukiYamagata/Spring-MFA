package com.example.demo.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class RegistrationForm {
    @NotBlank
    @Length(min = 1, max = 20)
    private String userName;

    @NotEmpty
    @Length(min = 1, max = 20)
    private String password;

    @NotEmpty
    @Length(min = 1, max = 255)
    private String name;

    private String useMfa;

    /**
     * コンストラクタ
     */
    public RegistrationForm() { }

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

    public String getUseMfa() {
        return useMfa;
    }

    public void setUseMfa(String useMfa) {
        this.useMfa = useMfa;
    }
}
