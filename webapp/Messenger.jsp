<%-- 
    Document   : Messenger
    Created on : 3 Jul, 2016, 2:05:38 PM
    Author     : arsh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.fishcart.delivery.service.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css">
        <script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
        <script src="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
    </head>
    <body>

        <div data-role="page">
            <div data-role="header">
                <h1>Whatsapp Messenger</h1>
                <h2>Total message pushed :<%=MessageController.counter%></h2>
            </div>

            <div data-role="main" class="ui-content">
                <form method="post" action="./api/send">
                    <div class="ui-field-contain">
                        <label for="fullname">Enter numbers in comma seperated form</label><br/>
                        <textarea name="nos" id="nos"></textarea>
                        <br/>
                        <label for="fullname">Message</label><br/>
                        <textarea  name="message" id="message"></textarea>

                    </div>
                    <input type="submit" data-inline="true" value="Submit">
                </form>
            </div>
        </div>

    </body>
</html>

