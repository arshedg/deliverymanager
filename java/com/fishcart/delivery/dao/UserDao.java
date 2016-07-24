/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fishcart.delivery.dao;

import com.fishcart.delivery.user.User;
import com.mysql.jdbc.StringUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

/**
 *
 * @author arsh
 */
public class UserDao extends SimpleJdbcDaoSupport{
    public User getUserDetails(String number){
        Long lNumber = Long.valueOf(number);
        String sql="select name,number,address,credit,gps from user where number=?";
        List<User> users = this.getJdbcTemplate().query(sql,new Object[]{lNumber}, new RowMapper() {

            @Override
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                User user = new User();
                user.setName(rs.getString("name"));
                user.setNumber(rs.getString("number"));
                user.setAddress(rs.getString("address"));
                user.setCredit(rs.getFloat("credit"));
                user.setLocation(rs.getString("gps"));
                return user;
            }
        });
        return getBetterUserObject(users);
    }
    public void updateUser(String name,String address,String number){
        
        this.getSimpleJdbcTemplate().update("update user set name=?,address=? where number=?", name,address,number);
    }
     public void addCredit(String number,float amount){
        String name = getName(number);
        if(UNKNOWN.equals(name)){
            return;
        }
        float credits = getCredits(number)+amount;
        this.getSimpleJdbcTemplate().update("update user set credit=? where number=?",credits,number);
        
    }
      public void setCredit(String number,float amount){
        this.getSimpleJdbcTemplate().update("update user set credit=? where number=?",amount,number);
        
    }
     
    public float getCredits(String number){
        String query = "select max(credit) from user where number=?";
        long price = this.getSimpleJdbcTemplate().queryForLong(query, number);
        return price;
    }
    public String getName(String number){
        
        try{
            List<String> names = this.getJdbcTemplate().query("select name from user where number=?",new String[]{number}, new RowMapper() {

            @Override
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString("name");
            }
        });
        return names.isEmpty()?UNKNOWN:names.get(0);
       // return this.getSimpleJdbcTemplate().queryForObject("select name from user where number=?", String.class, number);
        }catch(Throwable e){
            return UNKNOWN;
        }
    }
    private static final String UNKNOWN = "UNKNOWN";

    private User getBetterUserObject(List<User> users) {       
        for(User user:users){
            if(!StringUtils.isEmptyOrWhitespaceOnly(user.getAddress())){
                return user;
            }
        }
        if(users.isEmpty()){
            return null;
        }
        return users.get(0);
    }
}