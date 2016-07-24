/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fishcart.delivery.service;

import com.fishcart.delivery.dao.UserDao;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author arsh
 */
@RestController
public class UserControler {
    @Autowired
    UserDao userDao;
    
    @RequestMapping("/updateuser")
    public String updateUser(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="name") String name,@RequestParam(value="address") String address,
                                @RequestParam(value="number") String number, 
                                @RequestParam(value="credit",required = false) Float credit) throws IOException, ServletException{
        if(name.trim().equals("")||address.trim().equals("")){
            return "Name or Address cannot be empty. failed";
        }
        userDao.updateUser(name, address, number);
        if(credit!=null){
            userDao.setCredit(number, credit);    
        }
        
        return "User details saved";
    }
}
