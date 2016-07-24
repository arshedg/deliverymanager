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
              <div data-role="header" id="mainHeader" >
                 <a id="side-menu-button" data-icon="bars"  class="ui-btn-right" style="margin-top:0px;" href="orderpanel.html">Menu</a>
            </div> 
           
            <div data-role="content" >
                <ul id="orderList" data-role="listview"  data-inset="true">

                </ul>

            </div>
        </div>
        <div data-role="page" id="details">
            <div data-role="header" id="mainHeader" style="padding:10px;" >
                <a href="#" data-icon="gear" id="map" >MAP</a>
                <a href="#" data-icon="gear"  onclick="saveUserDetails()">Save</a>
         
            </div>  
            <div data-role="content" >
                <input type='text' id='name'/>
                <br/>
                <a id="number"></a>
              
                <br/>
                <textarea row="5" type='text' id='address'></textarea>
                <br/>

                <div data-role="panel" id="editOrder" data-theme="b"
                     data-display="overlay" data-position="right" style="width: 100%;">
                    <div data-role="header" >
                        
                        Edit Order
                    </div>
                    <div id="editOrderContent" data-role="content">
                        <form class="ui-filterable">
                            <input id="productName" data-type="search" placeholder="Search products">
                        </form>    
                        <ul id="productNameList" data-role="listview" data-inset="true" data-filter="true" data-filter-reveal="true" data-input="#productName">
                        
                        </ul>
                        <input id='productQuanity' type='number' value=''></input>
                        <label>
                            <input type="radio" name="timing" id="now">Immediate
                        </label>
                        <label for="later">Booking</label>
                        <input type="radio" name="timing" id="later" class="custom"> 
                
                        <form class="ui-filterable">
                                <input id="productStatus" data-type="search" placeholder="stauts...">
                        </form>    
                        <ul data-role="listview" data-inset="true" data-filter="true" data-filter-reveal="true" data-input="#productStatus">
                                <li onclick="autoCompleteOnSelect(this, 'productStatus')">TODO</li>
                                <li onclick="autoCompleteOnSelect(this, 'productStatus')">CONFIRMED</li>
                                <li onclick="autoCompleteOnSelect(this, 'productStatus')">DELIVERED</li>
                                <li onclick="autoCompleteOnSelect(this, 'productStatus')">CANCELED</li>
                        </ul>
                    </div>
                    <input id="orderButton" type="submit" value="SAVE" onclick="orderEditRequest()">
                    <input id="orderButton" type="submit" value="CLOSE" onclick="closePanel('editOrder')">
                </div>

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

                <div data-role="fieldcontain">
                    <label  for="credit">Credit RS:</label>
                    <input type='text' id='credit'/>
                </div>
                <table id="orderTable" data-role="table" class="ui-responsive">
                    <thead>
                        <tr>
                            <th>Product</th>
                            <th>Quantity</th>
                            <th>Time</th>
                            <th>Expected delivery</th>
                            <th>Status</th>
                            <th>Modify</th>
                            <th>cancel</th>
                        </tr>
                    </thead>
                    <tbody id="tbody">

                    </tbody>
                </table>
                <div data-role="fieldcontain">
                    <label for="guy" class="select">Shipping method:</label>
                    <select name="guy" id="guy">
                        <option value="none">-----</option>
                        <option value="asil">Asil</option>
                        <option value="siraj">Siraj</option>
                        <option value="vaishakh">Vaishakh</option>
                        <option value="nisar">Nisar</option>
                        <option value="sabith">Sabith</option>
                        <option value="shaheer">Shaheer</option>
                    </select>
                </div>
            </div>
        </div>
    </body>
</html>
