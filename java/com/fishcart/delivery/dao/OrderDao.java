/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fishcart.delivery.dao;

import com.fishcart.delivery.order.Order;
import com.fishcart.delivery.order.OrderStatus;
import com.fishcart.delivery.order.legacy.OrderDetails;
import com.fishcart.delivery.util.Util;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

/**
 *
 * @author arsh
 */
public class OrderDao  extends SimpleJdbcDaoSupport{
    
    @Autowired
    ReferralDao referralDao;
    public List<Order> getRecentOrders(){
        String date = get3DaysBack();
        String sql = "select o.id, o.number,o.status,o.product, o.quantity,o.delivery_person ,o.immediate,o.slot,o.stamp as time from orders o where DATE(stamp) >='"+date+"' order by time desc";
        return this.getJdbcTemplate().query(sql,  OrderRowMapper.getInstance() );
    }
    public List<Order> getOrderHistory(String date){
        List<Order> orders = getAllImmediateOrders(date);
        orders.addAll(getPrevsDayBooking(date));
        Collections.sort(orders);
        return orders;
    }
    private List<Order> getAllImmediateOrders(String date){
        String sqlDate = Util.getSqlDate(Util.getDate(date, "00:00"));
        String sql = "select o.id, o.number,o.status,o.product, o.quantity,o.delivery_person ,o.immediate,o.stamp as time from orders o where DATE(stamp) ='"+sqlDate+"' and immediate=true and status='DELIVERED' order by time desc";
        return  this.getJdbcTemplate().query(sql,  OrderRowMapper.getInstance() );

    }
    private List<Order> getPrevsDayBooking(String date){
        Calendar givenDate = Calendar.getInstance();
        givenDate.setTime(Util.getDate(date,"08:00"));
        Timestamp givenDateStamp = new Timestamp(givenDate.getTime().getTime());
        String prevsDate = Util.previousDate(givenDate.getTime());
        Date prevsDateWithTime = Util.getDate(prevsDate, "08:00");
        givenDate.setTime(prevsDateWithTime);
        Timestamp prevsDayStamp = new Timestamp(givenDate.getTime().getTime());
        String sql = "select o.id, o.number,o.status,o.product, o.quantity,o.delivery_person ,o.immediate,o.stamp as time from orders o where (stamp between ? and ?) and immediate=false and status='DELIVERED' order by time desc";
        return this.getJdbcTemplate().query(sql,new Object[]{prevsDayStamp,givenDateStamp},  OrderRowMapper.getInstance() );
        
    }
    /*sqlDate
    1. all immediate orders of given day
   
    w. All orders from prevsday 06- current day 06
    */
   private String get3DaysBack(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -3);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        return df.format(cal.getTime());

    }
    public String getNumberFromOrderId(String id){
        String normalizedId = id.trim();
        try{
        return this.getSimpleJdbcTemplate().queryForObject("select  number from orders where id=?", String.class, normalizedId);
        }catch(Throwable e){
            return null;
        }
    }
 public void updateFeedBack(String number, String feedback) {
        String sql = "update orders set feedback=? where number=? and status='TODO'";
        this.getSimpleJdbcTemplate().update(sql, feedback, number);
    }
    public int saveOrder(Order order){
        String sql = "";
        if(order.getOrderId()!=null){
            sql = "update orders set product=?,quantity=?,status=?,immediate=? where id=?";
        }
        return this.getSimpleJdbcTemplate().update(sql, order.getProduct(),order.getQuantity(),order.getOrderStatus().name(),order.isImmediate(),order.getOrderId());
    }
    public List<OrderDetails> getPendingOrders() {
        String sql = "select name, o.number, address, o.feedback,o.product, o.quantity, o.immediate,o.stamp as time from orders o,(select name,address,number from user)as y where o.number = y.number and o.id>1500 order by time desc";
        //return this.getJdbcTemplate().queryForList(sql, OrderDetails.class);
        
        return this.getJdbcTemplate().query(sql, new RowMapper() {

            public Object mapRow(ResultSet rs, int i) throws SQLException {
                OrderDetails details = new OrderDetails();
                details.setName(rs.getString("name"));
                details.setNumber(rs.getString("number"));
                details.setAddress(rs.getString("address"));
                details.setProduct(rs.getString("product"));
                details.setQuantity(rs.getString("quantity"));
                Timestamp time = rs.getTimestamp("time");
                details.setTime(Util.getIndianTime(time));
                details.setFeedback(rs.getString("feedback"));
                details.setImmediate(rs.getBoolean("immediate"));
                return details;
            }
        });
    }

    public Long getLastOrderId() {
        String query = "select MAX(id) from orders";
        return this.getSimpleJdbcTemplate().queryForObject(query, Long.class);
    }

    public Collection<OrderDetails> getPendingOrdersGroupedByCustomer() {
        List<OrderDetails> orders = this.getPendingOrders();
        Map<String, OrderDetails> groupedOrder = new HashMap<>();
        orders.stream().forEach(order -> {
            if (groupedOrder.containsKey(order.getNumber())) {
                OrderDetails details = groupedOrder.get(order.getNumber());
                details.setProduct(details.getProduct() + "," + order.getProduct());
            } else {
                groupedOrder.put(order.getNumber(), order);
            }
        });
        return groupedOrder.values();
    }
    
    public boolean setStatus(String ids,String status){
        String idArray[] = ids.split(",");
        referralreward(idArray[0],status);
        String queryParams = getQueryParams(idArray.length);
        String sql = "update orders set status=? where id in ("+queryParams+")";
        String params[] = prefixStringToArray(status, idArray);
        this.getJdbcTemplate().update(sql, params);
        return true;
    }
    private void referralreward(String id,String status){
        if(!OrderStatus.DELIVERED.equals(getStatus(status))){
            return;
        }
        String number = getNumberFromOrderId(id);
        if(number!=null){
            referralDao.rewardReferrer(number);
        }
    }
    private String[] prefixStringToArray(String val,String[] arr){
        String newArr[] = new String[arr.length+1];
        newArr[0] = val;
        System.arraycopy(arr,0, newArr, 1, arr.length);
        return newArr;
    }
    private String getQueryParams(int count){
        if(count==0){
            return "";
        }
        String query="";
        for(int i=0;i<count;i++){
            query+="?,";
        }
        return query.substring(0, query.length()-1);
    }
     static OrderStatus getStatus(String string){
        if(string==null||string.trim().equals("")){
            return OrderStatus.TODO;
        }
        try{
        return OrderStatus.valueOf(string.toUpperCase());
        }catch(Exception ex){
             return OrderStatus.TODO; 
        }
    }

    public boolean setDeliverer(String ids, String guy) {
        String idArray[] = ids.split(",");
        String queryParams = getQueryParams(idArray.length);
        String sql = "update orders set delivery_person=? where id in ("+queryParams+")";
        String params[] = prefixStringToArray(guy, idArray);
        this.getJdbcTemplate().update(sql, params);
        return true;
    }
}
class OrderRowMapper implements RowMapper{

    public static OrderRowMapper getInstance(){
        return new OrderRowMapper();
    }
    
   @Override
   public Object mapRow(ResultSet rs, int i) throws SQLException {
                Order order = new Order();
                order.setOrderId(rs.getInt("id"));
                order.setNumber(rs.getString("number"));
                order.setProduct(rs.getString("product"));
                order.setQuantity(rs.getFloat("quantity"));
                order.setDeliveryPerson(rs.getString("delivery_person"));
                Timestamp time = rs.getTimestamp("time");
                order.setOrderedTime(Util.getIndianTime(time));
                order.setImmediate(rs.getBoolean("immediate"));
                order.setSlot(rs.getString("slot"));
                order.setOrderStatus(OrderDao.getStatus(rs.getString("status")));
                return order;
            }
    
}