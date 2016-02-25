/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fishcart.delivery.dao;

import com.fishcart.delivery.order.Order;
import com.fishcart.delivery.order.OrderDetails;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author arsh
 */
@Component
public class OrderDetailsGenerator {
    @Autowired
    private OrderDao orderDao;
     @Autowired
    private UserDao userDao;
    
    public List<OrderDetails> getRecentOrders(){
        List<Order> orders = orderDao.getRecentOrders();
        List<OrderDetails> orderDetailsList= new ArrayList<>();
        Order lastOrder=null;
        OrderDetails lastOrderDetails=null;
        for(Order order:orders){
            if(shouldMerge(lastOrderDetails, order)){
                lastOrderDetails.setOrder(order);
            }else{
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setUser(userDao.getUserDetails(order.getNumber()));
                orderDetails.setOrder(order);
                orderDetailsList.add(orderDetails);
                lastOrderDetails = orderDetails;
            }
        }
       return orderDetailsList;
    } 
    private boolean shouldMerge(OrderDetails last,Order order){
        if(last==null) return false;
        return last.getUser().getNumber().trim().equals(order.getNumber().trim());
    }
}
