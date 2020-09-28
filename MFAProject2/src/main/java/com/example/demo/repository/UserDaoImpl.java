package com.example.demo.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.MyUser;

@Repository
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * userNameを検索条件にSELECT文を実行して、DBに登録されているユーザを検索する
     * @param userName
     * @return User
     */
    @Override
    public MyUser findUserByUserName(String userName) {
        String sql = "SELECT username, password, name, rolename, mail, useMfa, secretkey FROM users WHERE username = ?";

        //ユーザを一件取得
        Map<String, Object> result = jdbcTemplate.queryForMap(sql, userName);

        // Entityクラス(User型)に変換
        MyUser user = convMapToUser(result);

        return user;
    }

    /**
     * Userを登録する。
     * @param MyUser
     */
    @Override
    public void save(MyUser user) {
    	String sql = "INSERT INTO users(username, password, name, rolename, mail, usemfa, secretkey) VALUES (?, ?, ?, ?, ?, ?, ?)";
    	jdbcTemplate.update(sql, user.getUserName(), user.getPassword(), user.getName(), user.getRoleName(), user.getMail(), user.isUseMfa(), user.getSecretKey());
    }

    /**
     * SQL SELECT文を実行した結果(Map<String, Object>)をUser型に変換する
     * @param Map<String, Object>
     * @return User
     */
    private MyUser convMapToUser(Map<String, Object> map) {
        MyUser user = new MyUser();

        user.setUserName((String) map.get("username"));
        user.setPassword((String) map.get("password"));
        user.setName((String) map.get("name"));
        user.setRoleName((String) map.get("rolename"));
        user.setMail((String) map.get("mail"));
        user.setUseMfa((boolean) map.get("useMfa"));
        user.setSecretKey((String) map.get("secretkey"));

        return user;
    }
}