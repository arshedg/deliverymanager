/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var orderDetails;
var activeOrder;
var orderEditRequestId;
var availableStatus = [
    "TODO", "CONFIRMED", "DELIVERED", "CANCELED", "FAKE"
];
var availableProducts=[];
function onDocumentReady() {
    fetchAllProducts();
    $("input[name='status']").bind("change", function (event, ui) {
        setStatus(event, ui);
    });

    $("#guy").bind("change", function (event, ui) {
        setDeliveryGuy(event, ui);
    });
    $(document).on("pagechange", function (e, data) {
        var pageid = data.toPage[0].id;
        if (pageid == "main") {
            loadOrders(orderDetails);
        }
        if (pageid == "details") {
            $("#orderTable").table("refresh");
            $("#guy").selectmenu("refresh");
            resetProductNames();
            selectStatus(getStatusFromList(activeOrder.orders));
        }
    });

    initOrders();

}
function closePanel(panelId){
    $( "#"+panelId ).panel( "close" );
}
function orderEditRequest(){
    var order = new Object();
    order.orderId = orderEditRequestId;
    order.product = $("#productName").val();
    order.quantity =$("#productQuanity").val();
    order.orderStatus = $("#productStatus").val();
    saveOrder(order);
}
function saveOrder(order){
     $.ajax({
           type: "POST",
           url: "api/saveOrder",
        
           contentType: 'application/json',
           success: function (msg) {
               alert(msg);
           },
            error: function (e) {
            alert("error occured")
            // handeNetworkError();
        },
           data: JSON.stringify(order)
       });
}
function autoCompleteOnSelect(option, fieldId) {
    var value = $(option).text();
    $("#" + fieldId).val(value);
    $("ul:jqmData(role='listview')").children().addClass('ui-screen-hidden');
}
function resetProductNames(){
    var content = "";
    for(var i=0;i<availableProducts.length;i++){
        var product = availableProducts[i];
        content+="<li onclick=\"autoCompleteOnSelect(this, 'productName')\">"+
                product.name+
                "</li>";        
    }
    $("#productNameList").html(content);
    $("#productNameList").listview("refresh");
    $("#productNameList").trigger("updateLayout");
}
function fetchAllProducts() {
    var url = "api/product/listall";
    $.ajax({
        url: url,
        error: function (e) {
            alert("error occured")
            // handeNetworkError();
        }
        ,
        success: function (response) {
            availableProducts = response;
        },
    });
}
function saveUserDetails() {
    var res = confirm("Are you sure?");
    if (res == false) {
        return;
    }
    var name = $("#name").val();
    var address = $("#address").val();
    var url = "api/updateuser?number=" + activeOrder.user.number + "&name=" + name + "&address=" + address;
    $.ajax({
        beforeSend: function () {
            $.mobile.loading('show');
        }, //Show spinner
        complete: function () {
            $.mobile.loading('hide');
        }, //Hide spinner
        url: url,
        error: function (e) {
            alert("status change failed")
            // handeNetworkError();
        }
        ,
        success: function (response) {
            activeOrder.user.name = name;
            activeOrder.user.address = address;
            alert(response);
        },
    });
}
function statusChanged(newStatus, orderList) {
    var oldStatus = getStatusFromList(orderList).toUpperCase();
    if (oldStatus === newStatus) {
        return false;
    }
    return true;
}
function setStatus(event, ui) {
    var statusValue = event.target.id.toUpperCase();
    if (!statusChanged(statusValue, activeOrder.orders)) {
        return;
    }
    var ids = getAllOrderIds(activeOrder);
    requestStatusChange(ids, statusValue);
}
function cancelOrder(orderId) {
    var should = confirm("are you sure to cancel this order");
    if (!should) {
        return;
    }
    requestStatusChange(orderId, "CANCELED", cancelSingleOrderCallBack);
}
function cancelSingleOrderCallBack(id) {
    var list = activeOrder.orders;
    var order = getOrderFromActiveOrder(id);
    order.orderStatus = "CANCELED";
    var status = getStatusFromList(list);
    propogateStatus(activeOrder.orders, status);
}
function getOrderFromActiveOrder(id) {
    var list = activeOrder.orders;
    for (var i = 0; i <= list.length; i++) {
        if (list[i].orderId == id) {
            return list[i];
        }
    }
}
function requestStatusChange(ids, statusValue, callback) {
    var url = "api/changestatus?orderid=" + ids + "&status=" + statusValue;
    $.ajax({
        beforeSend: function () {
            $.mobile.loading('show');
        }, //Show spinner
        complete: function () {
            $.mobile.loading('hide');
        }, //Hide spinner
        url: url,
        error: function (e) {
            alert("status change failed");
            selectStatus(getStatusFromList(activeOrder.orders));
            // handeNetworkError();
        }
        ,
        success: function (response) {
            if (callback == null) {
                propogateStatus(activeOrder.orders, statusValue);
            }
            else {
                callback(ids);
            }
            alert(response);
        },
    });
}
function setDeliveryGuy(event, ui) {
    var statusValue = event.target.selectedOptions[0].value;
    var ids = getAllOrderIds(activeOrder);
    var url = "api/deliveryPerson?orderid=" + ids + "&guy=" + statusValue;
    $.ajax({
        beforeSend: function () {
            $.mobile.loading('show');
        }, //Show spinner
        complete: function () {
            $.mobile.loading('hide');
        }, //Hide spinner
        url: url,
        error: function (e) {
            alert("status change failed");
            // handeNetworkError();
        }
        ,
        success: function (response) {
            activeOrder.orders[0].deliveryPerson = statusValue;
            alert(response);
        },
    });
}
function propogateStatus(list, selectedStatus) {
    for (var i = 0; i < list.length; i++) {
        list[i].orderStatus = selectedStatus;
    }
}

function getAllOrderIds(order) {
    var list = order.orders;
    var idList = "";
    for (var i = 0; i < list.length; i++) {
        idList += list[i].orderId + ",";
    }
    return idList.substring(0, idList.length - 1);
}
function initOrders() {
    var url = "api/orderdetails";
    $.ajax({
        beforeSend: function () {
            $.mobile.loading('show');
        }, //Show spinner
        complete: function () {
            $.mobile.loading('hide');
        }, //Hide spinner
        url: url,
        dataType: 'json',
        error: function (e) {
            alert("error")
            // handeNetworkError();
        }
        ,
        success: function (response) {
            orderDetails = response;
            loadOrders(response);
        },
    });
}
function loadOrders(orderList) {
    var dom = "";
    for (var i = 0; i < orderList.length; i++) {
        dom += generateOrderLink(orderList[i], i);
    }
    $("#orderList").html(dom);
    $("#orderList").listview("refresh");

}
function generateOrderLink(order, id) {
    var status = "class='" + getStatusColor(order) + "'"
    var pos = "pos='" + id + "'";
    var action = "onclick=setDetails(" + id + ")";
    var username = getValidName(order.user.name);
    var data = "<a " + pos + " " + action + " href='#details'>" + username + "</a>";
    var details = "<div>" + getItemsShortHand(order.orders) + "</div>";
    return "<li " + status + ">" + data + details + "</li>";
}
function getItemsShortHand(orders) {
    var text = "";
    for (var i = 0; i < orders.length; i++) {
        text += orders[i].product + "*" + orders[i].quantity + "&#09;,";
    }
    return text.substring(0, text.length - 1);

}
function getValidName(name) {
    return name != null && name != "" ? name : "empty name";
}
function setDetails(id) {
    var order = orderDetails[id];
    activeOrder = order;
    var username = order.user.name;
    $("#name").val(username != null && username != "" ? username : "empty name");
    $("#address").val(order.user.address);
    $("#number").attr("href", "tel:" + order.user.number);
    $("#number").text(order.user.number);
    $("#credit").val(order.user.credit);
    var deliveryPerson = order.orders[0].deliveryPerson;
    if (deliveryPerson == null) {
        deliveryPerson = "none";
    }
    $("#guy").val(deliveryPerson);
    var orderList = order.orders;
    generateOrderTable(orderList);
}
function selectStatus(status) {
    var statusId = status.toLowerCase();
    var statusElem = $("#" + statusId);
    if (statusElem.length == 0) {
        return;
    }
    statusElem.trigger("click").trigger("click");
}
function generateOrderTable(orderList) {
    var dom = "";
    for (var i = 0; i < orderList.length; i++) {
        dom += generateRow(orderList[i]);
    }
    $("#tbody").html(dom);

}
function generateRow(order) {
    var timing = order.immediate == true ? "Immediate" : "booking";
    return "<tr>" +
            "<td> "+ order.product + "</td>" +
            "<td>" + order.quantity + "</td>" +
            "<td>" + order.orderedTime + "</td>" +
            "<td>" + timing + "</td>" +
            "<td>" + order.orderStatus + "</td>" +
            "<td><a href='#editOrder' onclick='generateEditOrder(" + order.orderId + ")'>edit</a></div></td>" +
            "<td><a src='#' onclick='cancelOrder(" + order.orderId + ")'>delete</a></div></td>" +
            "</tr>";
}
function generateEditOrder(orderId) {
    var order = getOrderFromActiveOrder(orderId);
    orderEditRequestId = orderId;
    $("#productName").val(order.product);
    $("#productQuanity").val(order.quantity);
    $("#productStatus").val(order.orderStatus);
    if (order.immediate) {
        $("#now").click().click();
    } else {
        $("#later").click().click();
    }

}
function getStatusFromList(list) {
    var confirmedFlag = false;
    var anyDelivery = false;
    for (var i = 0; i < list.length; i++) {
        if (list[i].orderStatus == "TODO") {
            return "TODO";
        }
        if (list[i].orderStatus == "FAKE") {
            return "FAKE";
        }
        if (list[i].orderStatus == "CONFIRMED") {
            confirmedFlag = true;
        }
        if (list[i].orderStatus == "DELIVERED") {
            anyDelivery = true;
        }
    }
    if (confirmedFlag == true) {
        return "CONFIRMED";
    }
    else if (anyDelivery) {
        return "DELIVERED";
    } else {
        return "CANCELED"
    }
}
function getStatusColor(order) {
    var list = order.orders;
    if (list) {
        var status = getStatusFromList(list);
        if (status == "TODO")
            return "red";
        if (status == "CONFIRMED")
            return "yellow";
        if (status == "DELIVERED")
            return "green";
        if (status == "CANCELED")
            return "black";
        if (status == "FAKE")
            return "white";
    }
    return "empty";
}
    