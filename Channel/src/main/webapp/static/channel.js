function getChannelUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/channel";
}


function fillAnyDropdown(dropselector,filler)
{
	var $drop=$(dropselector);
	for(let r in filler){
		var row='<option value="'+filler[r].id+'">'+filler[r].name+'</option>';
		$drop.append(row);
	}
}

function fillDropDowns()
{
	$('#selectChannel').empty();
	$('#selectClient').empty();
	$('#selectCustomer').empty();
	$('#selectChannel').append('<option selected hidden disabled value="">Select name</option>');
	$('#selectCustomer').append('<option selected hidden disabled value="">Select name</option>');
	$('#selectClient').append('<option selected hidden disabled value="">Select name</option>');

	var url=getChannelUrl();

	$.ajax({
	   url: url +'/customer',
	   type: 'GET',
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
		fillAnyDropdown('#selectCustomer',response);
	   },
	   error: handleAjaxError
	});

	$.ajax({
	   url: url +'/client',
	   type: 'GET',
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
		fillAnyDropdown('#selectClient',response);
	   },
	 error: handleAjaxError
	});

	$.ajax({
	   url: url,
	   type: 'GET',
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
		fillAnyDropdown('#selectChannel',response);
	   },
		error: handleAjaxError
	});	
}

// to store ordered Items
var cart={}

function validate()
{
	if($('#selectChannel option:selected').val()=="")
	{
		callWarnToast('Select appropriate channel');
		return false;
	}


	if($('#selectCustomer option:selected').val()=="")
	{
		callWarnToast('Select appropriate customer');
		return false;
	}

	if($('#selectClient option:selected').val()=="")
	{
		callWarnToast('Select appropriate client');
		return false;
	}

	return true;
}

function validateItem()
{
	let channelSkuId = $('#order-form input[name=channelSkuId]').val();
	let qty = Number($('#order-form input[name=quantity').val());
	let mrp=Number($('#order-form input[name=sellingPricePerUnit').val());

	if(!channelSkuId || channelSkuId.trim().length==0)
	{
		callWarnToast('Enter a valid channelSkuId');
		return false;
	}

	if(qty<=0)
	{
		callWarnToast("Ordered quantity cannot be less than equal to 0");
		return false;
	}
	
	if(!Number.isInteger(qty))
	{	
	 	callWarnToast("Quantity is not integer");
	 	return false;
	}

	console.log(mrp);
	console.log(isNaN(Number(mrp)));
	if(isNaN(mrp))
	{	
	 	callWarnToast("MRP is not integer");
	 	return false;
	}

	if(mrp<=0)
	{
		callWarnToast("Selling price cannot be less than equal to 0");
		return false;
	}

	return true;

}


function addInCart()
{

	if(!validate())
	{
		return;
	}

	if(!validateItem())
		return;

	let clientId=Number($('#selectClient option:selected').val());
	let channelId=Number($('#selectChannel option:selected').val());
	let channelSkuId=$('#order-form input[name=channelSkuId]').val();
	let qty = Number($('#order-form input[name=quantity').val());
	var json={};
	json['clientId']=clientId;
	json['channelId']=channelId;
	json['channelSkuId']=channelSkuId;
	json['quantity']=qty;
	console.log(Object.keys(cart).length)
	if(Object.keys(cart).length==0)
	{
		createUiTable();
	}
	if(!checkCart(json))
		return;
	checkItem(json);

}

function checkAdd()
{
	let channelSkuId = $('#order-form input[name=channelSkuId]').val();
	let qty = ($('#order-form input[name=quantity').val());
	let mrp=($('#order-form input[name=sellingPricePerUnit').val());
	if(channelSkuId && channelSkuId.trim().length!=0 && mrp && qty)
	{
		$('#add-order').prop('disabled',false);
	}
}

function createUiTable()
{
	var row='<tr>'+
            '<th scope="col">S.No.</th>'+
            '<th scope="col">Product</th>'+
            '<th scope="col">Brand</th>'+
            '<th scope="col">Channel SKU</th>'+
            '<th scope="col">Quantity</th>'+
            '<th scope="col">Selling Price</th>'+
            '<th scope="col">Total Price</th>'+
            '<th scope="col">Actions</th>'+
        '</tr>';
    var $thead=$('#order-table').find('thead');
    	$thead.empty();
    	$thead.append(row);

}

function checkCart(json)
{
	var channelSkuId=json['channelSkuId'];
	if(cart.hasOwnProperty(channelSkuId))
    { 
    	callWarnToast('Item already present, please edit the quantity from ordered list');
    	$('#order-form').trigger('reset');
    	$('#add-order').prop('disabled',true);
    	return false;
    }	
    return true;
}


function makeCart(data)
{	

	let channelSkuId=$('#order-form input[name=channelSkuId]').val();
	let qty = Number($('#order-form input[name=quantity').val());
	let mrp = Number($('#order-form input[name=sellingPricePerUnit').val())
	if(!cart.hasOwnProperty(channelSkuId))
	{

    	cart[channelSkuId]=[data.productName,data.brandId,qty,mrp.toFixed(2),(mrp*qty).toFixed(2)];	
    	return;
	}
	var existing=cart[channelSkuId];
   	cart[channelSkuId]=[existing[0],existing[1],existing[2]+qty,existing[3].toFixed(2),(existing[3]*(existing[2]+qty)).toFixed(2)];
}

function checkItem(json)
{
	$.ajax({
	   url: getChannelUrl()+'/product',
	   type: 'POST',
	   data: JSON.stringify(json),
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		 makeCart(response);
	   		 displayItems();
	   		 $('#order-form').trigger('reset');
	   		 $('#add-order').prop('disabled',true);
	   		 callConfirmToast('added to cart');
	   },
	   error: function(response){
	   		console.log(response);
	   		handleAjaxError(response);
	   		$('#order-form').trigger('reset');
	   		$('#add-order').prop('disabled',true);
	   }
	});
}

function displayItems()
{
	var $tbody=$('#order-table').find('tbody');
	$tbody.empty();

	let bill=0;
	let sno=1;
	for(row in cart)
	{
		var buttonHtml = '<button class=\"btn btn-outline-info btn-sm\" onclick=\'displayEditOrder(\"' + row + '\")\'>Edit</button>'; 
		buttonHtml += '  <button class=\"btn btn-outline-danger btn-sm\" onclick=\'deleteItem(\"' + row + '\")\'>Delete</button>';'<button class=\"btn btn-outline-danger btn-sm\" onclick=\'deleteItem(\"' + row + '\")\'>Delete</button>';

		var item = '<tr>'
		+ '<td>'  + sno + '</td>'
		+ '<td>' + cart[row][0] + '</td>'
		+ '<td>' + cart[row][1]+ '</td>'
		+ '<td>' + row + '</td>'
		+ '<td>'  + cart[row][2] + '</td>'
		+ '<td>'  + (Math.round(cart[row][3] * 100) / 100).toFixed(2) + '</td>'
		+ '<td>'  + (Math.round(cart[row][4] * 100) / 100).toFixed(2) + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';

        $tbody.append(item);
        sno+=1;
        bill+=Math.round(cart[row][4] * 100) / 100;
	    console.log(Number(bill));
	}
    $('#totalPrice').val(Number(bill).toFixed(2));

}

function displayEditOrder(row)
{
	$('#order-edit-form input[name=name]').val(cart[row][0]);
	$('#order-edit-form input[name=brandId]').val(cart[row][1]);
	$('#order-edit-form input[name=quantity]').val(cart[row][2]);
	$('#order-edit-form input[name=channelSkuId]').val(row);
	console.log($('#order-edit-form input[name=channelSkuId]').val());
	$('#edit-order-modal').modal('toggle');
}


function updateOrderItem()
{
	var channelSkuId=$('#order-edit-form input[name=channelSkuId]').val();
	var json={}
	json['channelId']=Number($('#selectChannel option:selected').val());
	json['channelSkuId']=channelSkuId;
	json['clientId']=Number($('#selectClient option:selected').val());
	json['quantity']=Number($('#order-edit-form input[name=quantity]').val());
	console.log(json);
	$('#edit-order-modal').modal('toggle');
	$.ajax({
	   url: getChannelUrl()+'/product',
	   type: 'POST',
	   data: JSON.stringify(json),
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		var existing=cart[channelSkuId];
	   		var qty=Number($('#order-edit-form input[name=quantity]').val());
   			cart[channelSkuId]=[existing[0],existing[1],qty,existing[3],(existing[3]*qty)];
   			displayItems();
	   },
	   error: function(response){
	   		console.log(response);
	   		handleAjaxError(response);
	   		$('#order-form').trigger('reset');
	   		$('#add-order').prop('disabled',true);
	   }
	});

}

function deleteItem(channelSkuId)
{
	delete cart[channelSkuId];
	displayItems();
}

function validateChannelOrderId()
{
	var channelOrderId=$('#channelOrderId').val();
	console.log(channelOrderId);
	if(!channelOrderId ||channelOrderId.trim().length==0)
	{
		callWarnToast("Enter valid channel order ID");
		return false;
	}
	return true;
}

function processed()
{
	items=[]
	for(row in cart)
	{
		var item={};
		item['channelSkuId']=row;
		item['sellingPricePerUnit']=cart[row][3];
		item['orderedQuantity']=cart[row][2];
		items.push(item);
	}
	return items;
}


function placeOrder()
{
	if(!validate())
		return;

	let channelId=$('#selectChannel option:selected').val();
	let customerId=$('#selectCustomer option:selected').val();
	let clientId=$('#selectClient option:selected').val();
	if(!validateChannelOrderId())
		return;
	let channelOrderId=$('#channelOrderId').val();
	var json={}
	json['channelOrderId']=channelOrderId;
	json['clientId']=clientId;
	json['customerId']=customerId;
	json['channelId']=channelId;
	json['items']=processed();
	if(json.items.length==0)
	{
		callWarnToast("Add some items in order");
		return;
	}

	$.ajax({
	   url: getChannelUrl()+'/order',
	   type: 'POST',
	   data: JSON.stringify(json),
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		 callConfirmToast('Order placed successfully');
	   		 resetUi();
	   },
	   error: function(response)
	   {
	   		handleAjaxError(response);
	   }
	});

}
function callReset()
{
	resetUi();
	$('#confirmation-modal').modal('toggle');
}
function resetUi()
{
	$('#order-table').find('tbody').empty();
	$('#order-table').find('thead').empty();
	$('#order-form').trigger('reset');
	fillDropDowns();
	$('#order-table').find('tbody').empty();
	$('#totalPrice').val("");
	$('#channelOrderId').val("");
	cart={};
	$('#add-order').prop('disabled',true);
	   		 
}

function confirmation()
{
	$('#confirmation-modal').modal('toggle');

}

function init()
{
	fillDropDowns();
	$('#add-order').click(addInCart);
	$('#confirm').click(placeOrder);
	$('#afresh').click(confirmation);
	$('#update-order').click(updateOrderItem);
	$('#confirm-reset').click(callReset);
	var previous1;
	var previous2;

    $("#selectClient").on('focus', function () {
        previous1 = this.value;
    }).change(function() {
    	if(previous1!="")
    	{
    		confirmation();
    	}
        // Make sure the previous value is updated
        previous1 = this.value;
    });

    $("#selectChannel").on('focus', function () {
        previous2 = this.value;
    }).change(function() {
    	if(previous2!="")
    	{
    		confirmation();
    	}
        // Make sure the previous value is updated
        previous2 = this.value;
    });
}


$(document).ready(init);