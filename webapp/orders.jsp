<%-- 
    Document   : orders
    Created on : 10 Feb, 2016, 7:41:02 PM
    Author     : arsh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Order Book</title>
        <link rel="stylesheet" href="//code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css">
        <link rel="stylesheet" href="css/general.css">

        <script src="//code.jquery.com/jquery-1.10.2.min.js"></script>
        <script src="//code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
        <script src="script/orders.js"></script>

    </head>
    <body>
        <script>
            $("body").ready(onDocumentReady);
        </script>
        <div data-role="page" id="main">
            <div data-role="content" >
                <ul id="orderList" data-role="listview"  data-inset="true">
                        
                </ul>
            </div>
        </div>
        <div data-role="page" id="details">
            <div data-role="content" >
                <input type='text' id='name'/>
                <br/>
                <a id="number"></a>
                <br/>
                <textarea row="5" type='text' id='address'></textarea>
                <br/>
                
                <input type="submit" value="SAVE" onclick="saveUserDetails()" />
                <fieldset data-role="controlgroup" data-type="horizontal" data-mini="true">
                    <input   type="radio" name="status" id="todo" value="on" checked="checked">
                    <label id="lNow" for="todo">TODO</label>
                    <input  type="radio" name="status" id="confirmed" value="off">
                    <label id="lLater"for="confirmed">CONFIRMED</label>
                    <input  type="radio" name="status" id="delivered" value="off">
                    <label id="lLater"for="delivered">DELIVERED</label>
                    <input  type="radio" name="status" id="canceled" value="off">
                    <label id="lLater"for="canceled">CANCELED</label>
                     <input  type="radio" name="status" id="fake" value="off">
                    <label id="lLater"for="fake">FAKE</label>

                </fieldset>
                <table id="orderTable" data-role="table" class="ui-responsive">
                    <thead>
                        <tr>
                            <th>Product</th>
                            <th>Quantity</th>

                            <th>time</th
                        </tr>
                    </thead>
                    <tbody id="tbody">

                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>