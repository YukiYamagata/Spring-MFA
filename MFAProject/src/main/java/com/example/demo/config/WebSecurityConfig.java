package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.google2fa.CustomAuthenticationProvider;
import com.example.demo.google2fa.CustomWebAuthenticationDetailsSource;
import com.example.demo.service.AccountUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AccountUserDetailsService userDetailsService;

    @Autowired
    private CustomWebAuthenticationDetailsSource authenticationDetailsSource;

    @Bean
    PasswordEncoder passwordEncoder() {
        //BCryptアルゴリズムを使用してパスワードのハッシュ化を行う
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 認可の設定
        http.authorizeRequests()
                .antMatchers("/loginForm").permitAll()  // loginFormは全ユーザーアクセス許可
                .anyRequest().authenticated();          //それ以外は認証を求める

        // ログイン設定
        http.formLogin() //フォームログイン(フォーム認証)を行う
                .authenticationDetailsSource(authenticationDetailsSource)
                .loginPage("/loginForm")                //ログインURLを指定 ログインしていないかつ、認証が必要なページにアクセスしようとしたら、このControllerが呼ばれる
                .loginProcessingUrl("/authenticate")    //login.htmlにおいて、<form>のactionと一致させる
                .usernameParameter("userName")          //login.htmlにおいて、ユーザ名を入力する<input>のname要素と一致させる
                .passwordParameter("password")          //login.htmlにおいて、パスワードを入力する<input>のname要素と一致させる
                .defaultSuccessUrl("/home")             //ログイン成功したら、遷移するURL
                .failureUrl("/loginForm?error=true");   //ログイン失敗したら、遷移するURL

        // ログアウト設定
        http.logout()
                .logoutSuccessUrl("/loginForm").permitAll(); //ログアウトページには認証なしでアクセスできる

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // AuthenticationManagerBuilderに、実装したCustomAuthenticationProviderを設定する
    	auth.authenticationProvider(authProvider());
    }
}