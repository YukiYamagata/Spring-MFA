package com.example.demo.authorization;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import com.example.demo.service.AccountUserDetails;
import com.example.demo.service.AuthUtil;

@Component
public class MyVoter implements AccessDecisionVoter<FilterInvocation> {

	private final AuthUtil authUtil;

	@Autowired
	public MyVoter(AuthUtil authUtil) {
		this.authUtil = authUtil;
	}

	@Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation filterInvocation, Collection<ConfigAttribute> attributes) {
    	HttpServletRequest request = filterInvocation.getHttpRequest();
    	String uri = request.getRequestURI();

    	if(authUtil.isAuthorized("*", uri)) { // 全てのRoleにアクセス許可されているか判定
        	return ACCESS_GRANTED;
        }

    	try {
    		AccountUserDetails principal = (AccountUserDetails) authentication.getPrincipal(); // AccountUserDetailsの取得
    		String roleName = principal.getUser().getRoleName();

    		if(authUtil.isAuthorized(roleName, uri)) { // 取得したRoleがアクセス許可されているか判定
	        	return ACCESS_GRANTED;
	        }
    	} catch (ClassCastException e) {

    	}

//    	if (authentication.isAuthenticated()) {
//
//    		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//
//    		int size = authorities.size();
//    		for (int i = 0; i < size; i++) {
//
//    			GrantedAuthority grantedAuthority = authorities.getClass();
//    			String roleName = grantedAuthority.getAuthority();
//
//				if(authUtil.isAuthorized(roleName, uri)) { // 取得したRoleがアクセス許可されているか判定
//		        	return ACCESS_GRANTED;
//		        }
//    		}
//    	}

    	return ACCESS_DENIED;
    }
}