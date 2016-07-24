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
    order.immediate = document.getElementById('now').checked;
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
    var credit = $("#credit").val();
    var url = "api/updateuser?number=" + activeOrder.user.number + 
            "&name=" + name + "&address=" + address+"&credit="+credit;
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
    var credit = "<div style='position:absolute;right:5px'><i>Balance:Rs."+order.user.credit+"</i></div>";
    var details = "<div>" + getItemsShortHand(order.orders) + "</div>";
    return "<li " + status + ">" + data + credit+details + "</li>";
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
    if(isMobile){
        $("#map").attr("href","geo:"+order.user.location);
    }else{
        $("#map").attr("href","http://maps.google.com/?q="+order.user.location);
        $("#map").attr("target","_blank");
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
   
   
   
   
   
   var isMobile = false; //initiate as false
// device detection
if(/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|ipad|iris|kindle|Android|Silk|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i.test(navigator.userAgent) 
    || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(navigator.userAgent.substr(0,4))) isMobile = true;