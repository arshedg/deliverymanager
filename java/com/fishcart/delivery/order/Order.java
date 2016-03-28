/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fishcart.delivery.order;

import java.sql.Time;

/**
 *
 * @author arsh
 */
public class Order {
    private int orderId;
    private String product;
    private float  price;
    private float quantity;
    private OrderStatus orderStatus;
    private boolean immediate;
    private String orderedTime;
    private String number;
    private String deliveryPerson;
    /**
     * @return the product
     */
    public String getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * @return the price
     */
    public float getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * @return the quantity
     */
    public float getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the orderStatus
     */
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    /**
     * @param orderStatus the orderStatus to set
     */
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * @return the immediate
     */
    public boolean isImmediate() {
        return immediate;
    }

    /**
     * @param immediate the immediate to set
     */
    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
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
     * @return the orderedTime
     */
    public String getOrderedTime() {
        return orderedTime;
    }

    /**
     * @param orderedTime the orderedTime to set
     */
    public void setOrderedTime(String orderedTime) {
        this.orderedTime = orderedTime;
    }

    /**
     * @return the orderId
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /**
     * @return the deliveryPerson
     */
    public String getDeliveryPerson() {
        return deliveryPerson;
    }

    /**
     * @param deliveryPerson the deliveryPerson to set
     */
    public void setDeliveryPerson(String deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
    }
}
