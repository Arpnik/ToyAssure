function getMemberUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/member";
}

function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}


function fillDropDowns()
{
	$('#selectClient').empty();
	$('#selectCustomer').empty();
	$('#selectClient').append('<option selected hidden disabled>Select Client</option>');
	$('#selectCustomer').append('<option selected hidden disabled>Select Customer</option>');
	let url=getMemberUrl()+'/client';
	   $.ajax({
	   url: url,
	   type: 'GET',
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   filldropDown(response,'#selectClient');
	   },
	   error: handleAjaxError
	});

	url=getMemberUrl()+'/customer';
	   $.ajax({
	   url: url,
	   type: 'GET',
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   filldropDown(response,'#selectCustomer');
	   },
	   error: handleAjaxError
	});

}

function filldropDown(data,selector)
{
	var $drop=$(selector);
	for (let i = 0; i < data.length; i++)
	{
		var row='<option value="'+data[i].id+'">'+data[i].name+'</option>';
		$drop.append(row);
	}
}







function fillTable()
{
	   let url=getOrderUrl();
	   $.ajax({
	   url: url,
	   type: 'GET',
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   showOrders(response);
	   },
	   error: handleAjaxError
	});

}


function showOrders(data)
{
	$tbody=$('#order-table').find('tbody');
	$tbody.empty();
	for (let i = 0; i < data.length; i++)
	{
		var actions=' <button class="btn btn-primary" onclick="showOrderDetails(' + data[i].orderId + ')">Show</button>';
		actions+=' <button  class="btn btn-primary"onclick="allocateOrder(' + data[i].orderId + ')">Allocate</button>';
		actions+=' <button  class="btn btn-primary"onclick="generateInvoice(' + data[i].orderId + ')">FullFill</button>';
		var row='<tr>'
		+ '<td>' + data[i].orderId + '</td>'
		+ '<td>' + data[i].channelOrderId + '</td>'
		+ '<td>' + data[i].status + '</td>'
		+ '<td>'  + data[i].createdDate + '</td>'
		+ '<td>' + actions + '</td>'
		+ '</tr>';
        $tbody.append(row);
        console.log(data[i].createDate);
	}

}

function showOrderDetails(orderId)
{
	console.log('hello');
	var url=getOrderUrl()+'/'+orderId;
	$.ajax({
	   url: url,
	   type: 'GET',
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		$('#invoiceNumber').text('Order Id-'+orderId);
	   		showItems(response);
			$('#order-details-modal').modal('toggle');

	   },
	   error: handleAjaxError
	});
}

function showItems(data)
{
	$tbody=$('#item-table').find('tbody');
	$tbody.empty();
	var sum=0;
	for (let i = 0; i < data.length; i++)
	{
		var row='<tr>'
		+ '<td>' + data[i].productName + '</td>'
		+ '<td>' + data[i].brandId + '</td>'
		+ '<td>' + data[i].orderedQuantity + '</td>'
		+ '<td>'  + data[i].sellingPricePerUnit + '</td>'
		+ '<td>'  + Number(data[i].orderedQuantity)*Number(data[i].sellingPricePerUnit) + '</td>'
		+ '</tr>';
        $tbody.append(row);
        sum+=Number(data[i].orderedQuantity)*Number(data[i].sellingPricePerUnit);
	}
	$('#totalBill').text('Total Amount: ₹'+sum);
}

function allocateOrder(orderId)
{
	let url=getOrderUrl()+'/'+orderId;
	   $.ajax({
	   url: url,
	   type: 'PUT',
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   fillTable();
	   },
	   error: handleAjaxError
	});
}

function generateInvoice(orderId)
{
	var fullUrl=getOrderUrl()+'/invoice/'+orderId;
    $.ajax({
        url: fullUrl,
        method: 'GET',
        xhrFields: {
            responseType: 'blob'
        },
        success: function (data) {
            var a = document.createElement('a');
            var url = window.URL.createObjectURL(data);
            a.href = url;
            a.download = 'invoice_'+orderId+'.pdf';
            document.body.append(a);
            a.click();
            a.remove();
            window.URL.revokeObjectURL(url);
        }
    });
}


function init()
{
	$('#refresh').click(fillTable);
}



$(document).ready(init);
$(document).ready(fillDropDowns);
$(document).ready(fillTable);
