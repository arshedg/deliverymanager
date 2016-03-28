/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fishcart.delivery.user;

/**
 *
 * @author arsh
 */
public class Referral {
    private String user;
    private String refferedBy;

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the refferedBy
     */
    public String getRefferedBy() {
        return refferedBy;
    }

    /**
     * @param refferedBy the refferedBy to set
     */
    public void setRefferedBy(String refferedBy) {
        this.refferedBy = refferedBy;
    }
    
}
