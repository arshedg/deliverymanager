/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fishcart.delivery.service;

import com.fishcart.delivery.dao.OrderDao;
import com.fishcart.delivery.dao.OrderDetailsGenerator;
import com.fishcart.delivery.order.Order;
import com.fishcart.delivery.order.OrderDetails;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    public String saveStatus(@RequestParam(value="orderid") String orderids,
                                  @RequestParam(value="status") String status){
        orderDao.setStatus(orderids, status);
        return "saved";
    }
    @RequestMapping("/deliveryPerson")
    public String saveDeliveryGuy(@RequestParam(value="orderid") String orderid,
                                  @RequestParam(value="guy") String guy){
        orderDao.setDeliverer(orderid, guy);
        return "saved";
    }
    @RequestMapping(value = "/saveOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean saveOrder(@RequestBody Order order){
        int response = orderDao.saveOrder(order);
        if(response>0){
            return true;
        }else{
            return false;
        }
    }
    
    
 
}
