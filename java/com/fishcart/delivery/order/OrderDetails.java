/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fishcart.delivery.order;

import com.fishcart.delivery.user.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author arsh
 */
public class OrderDetails {
    private User user;
    private String number;
    private String address;
    private List<Order> orders;

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
   public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the orders
     */
    public List<Order> getOrders() {
        return orders;
    }

    /**
     * @param orders the orders to set
     */
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
     public void setOrder(Order order) {
        if(orders==null){
            orders = new ArrayList<>();
        }
        orders.add(order);
    }
}
