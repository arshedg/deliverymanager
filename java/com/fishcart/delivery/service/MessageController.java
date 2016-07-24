/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fishcart.delivery.service;

import com.fishcart.delivery.util.HttpClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author arsh
 */
@RestController
public class MessageController {

    String MSG_URL="http://whatsapp.ind-cloud.everdata.com/whatsapp/chat-api/src/send.php";
    String MSG_URL2="http://localhost:5000/Chat-API/src/send.php";
   public volatile static int counter=0;
   
    @RequestMapping("/send")
    public String sendMessage(@RequestParam(value = "nos") String nos,
            @RequestParam(value = "message") String message) {
        String numbers = format(nos);
        return HttpClient.postWhatsapp(MSG_URL,numbers,message);
    }

    @RequestMapping("/recieve")
    public void recieveMessage(@RequestParam(value = "nos") String nos,
            @RequestParam(value = "message") String message) {

    }
    private String format(String nos) {
        if (nos == null || nos.isEmpty()) {
            throw new RuntimeException("number is empty");
        }
        StringBuilder builder = new StringBuilder();
       
        String number[] =  nos.split("\r\n");;
        for (String no : number) {
            if (no.length() < 10) {
                continue;
            }
            if(!isNumeric(no)){
                continue;
            }
            counter++;
            if (no.length() == 10) {
                builder.append("91").append(no).append(",");
            } else {
                builder.append(no).append(",");
            }
        }
       return builder.substring(0, builder.length()-1);
    }

    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}
