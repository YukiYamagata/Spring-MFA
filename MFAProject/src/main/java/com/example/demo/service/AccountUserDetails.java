package com.example.demo.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.entity.MyUser;

public class AccountUserDetails implements UserDetails {
    private final MyUser myUser;

    public AccountUserDetails(MyUser myUser) {
        this.myUser = myUser;
    }

    public MyUser getUser() { // -------------------------------------------------------- (1)
        return myUser;
    }

    public String getName() { // -------------------------------------------------------- (2)
        return this.myUser.getName();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // ----------------- (3)
        return AuthorityUtils.createAuthorityList("ROLE_" + this.myUser.getRoleName());
    }

    @Override
    public String getPassword() { // ---------------------------------------------------- (4)
        return this.myUser.getPassword();
    }

    @Override
    public String getUsername() { // ---------------------------------------------------- (5)
        return this.myUser.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() { // ------------------------------------------- (6)
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { // -------------------------------------------- (7)
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { // --------------------------------------- (8)
        return true;
    }

    @Override
    public boolean isEnabled() { // ----------------------------------------------------- (9)
        return true;
    }
}