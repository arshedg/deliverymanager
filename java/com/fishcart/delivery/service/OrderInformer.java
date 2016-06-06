/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fishcart.delivery.service;


import com.fishcart.delivery.dao.OrderDao;
import com.fishcart.delivery.dao.OrderDetailsGenerator;
import com.fishcart.delivery.order.Order;
import com.fishcart.delivery.order.legacy.OrderDetails;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author arsh
 */
@Controller
@RestController
public class OrderInformer {
    @Autowired
    OrderDao orderDao;
    @Autowired
    OrderDetailsGenerator generator;
    @RequestMapping(value="/orders",method = {RequestMethod.POST,RequestMethod.GET},produces = {"application/json"})    
    public @ResponseBody List<OrderDetails> getOrderDetails(){
        return orderDao.getPendingOrders();
    }
    
    @RequestMapping(value="/orderhistory",method =  {RequestMethod.POST,RequestMethod.GET},produces = {"application/text"})    
    public void getOrderHistory(@RequestParam(value="date") String date,
                                                        HttpServletResponse response) throws IOException{
        String data = generator.getOrderHistory(date);
        response.setHeader("Content-Disposition","attachment; filename=report.csv");
        IOUtils.copy(IOUtils.toInputStream(data, "UTF-8"),response.getOutputStream());
        response.flushBuffer();
    }
    @RequestMapping(value="/ordersbycustomer",method = RequestMethod.POST,produces = {"application/json"})
    public @ResponseBody Collection<OrderDetails> getOrdersByCustomer(){
        return orderDao.getPendingOrdersGroupedByCustomer();
    }
    
    @RequestMapping(value="/lastorder",method = RequestMethod.GET)
    public Long getLastOrderId(){
        return orderDao.getLastOrderId();
    }
    @RequestMapping(value="/updatefeedback",method = RequestMethod.POST)
    public @ResponseBody String updateFeedback(@RequestParam(value="number") String number,
                                 @RequestParam(value="feedback") String feedback){
         orderDao.updateFeedBack(number, feedback);
         return "success";
    }
}
