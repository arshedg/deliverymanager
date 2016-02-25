/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fishcart.delivery.service;

import com.fishcart.delivery.dao.OrderDao;
import com.fishcart.delivery.dao.OrderDetailsGenerator;
import com.fishcart.delivery.order.OrderDetails;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author arsh
 */
@RestController
public class OrderControler {
    @Autowired
    OrderDetailsGenerator detailsGenerator;
    @Autowired
    OrderDao orderDao;
    @RequestMapping("/orderdetails")
    public List<OrderDetails> getRecentOrders(){
        return detailsGenerator.getRecentOrders();
    }
    @RequestMapping("/changestatus")
    public String saveStatus(@RequestParam(value="orderid") String orderid,
                                  @RequestParam(value="status") String status){
        orderDao.setStatus(orderid, status);
        return "saved";
    }
    
 
}
