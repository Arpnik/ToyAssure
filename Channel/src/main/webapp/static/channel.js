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
	console.log('inside');
	$('#selectChannel').empty();
	$('#selectClient').empty();
	$('#selectCustomer').empty();
	$('#selectChannel').append('<option selected hidden disabled value="">select channel</option>');
	$('#selectCustomer').append('<option selected hidden disabled value="">select customer</option>');
	$('#selectClient').append('<option selected hidden disabled value="">select client</option>');

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
	   error: function(response)
	   {
	   	console.log(response);
	   }

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
	 error: function(response)
	   {
	   	console.log(response);
	   }
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
		error: function(response)
	   {
	   	console.log(response);
	   }
	});	
}

// to store ordered Items
var cart={}

function validate()
{
	if($('#selectChannel option:selected').val()=="")
	{
		callWarnToast('Select appropriate Channel');
		return false;
	}


	if($('#selectCustomer option:selected').val()=="")
	{
		callWarnToast('Select appropriate Customer');
		return false;
	}

	if($('#selectClient option:selected').val()=="")
	{
		callWarnToast('Select appropriate Client');
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
		callWarnToast("Ordered Quantity cannot be less than equal to 0");
		return false;
	}
	// if(!Number.isInteger(qty))
	// {	
	// 	callWarnToast("Quantity is not integer");
	// 	return false;
	// }

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
	checkCart(json);
	checkItem(json);

}

function checkCart(json)
{
	var channelSkuId=json['channelSkuId'];
	if(cart.hasOwnProperty(channelSkuId))
    {
    	callWarnToast("Item already present so added in cart");
    	json['quantity']=cart[channelSkuId][2]+json['quantity'];
    }	
}


function makeCart(data)
{	

	let channelSkuId=$('#order-form input[name=channelSkuId]').val();
	let qty = Number($('#order-form input[name=quantity').val());
	let mrp = Number($('#order-form input[name=sellingPricePerUnit').val())
	if(!cart.hasOwnProperty(channelSkuId))
	{

    	cart[channelSkuId]=[data.productName,data.brandId,qty,mrp,mrp*qty];	
    	return;
	}
	var existing=cart[channelSkuId];
   	cart[channelSkuId]=[existing[0],existing[1],existing[2]+qty,existing[3],existing[4]+(mrp*qty)];
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
	   		 callConfirmToast('added to cart');
	   },
	   error: function(response){
	   		console.log(response);
	   		handleAjaxError(response);
	   		$('#order-form').trigger('reset');
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
		var buttonHtml = '<button class=\"btn btn-primary\" onclick=\'deleteItem(\"' + row + '\")\'>Delete</button>';
		buttonHtml += ' <button class=\"btn btn-primary\" onclick=\'displayEditOrder(\"' + row + '\")\'>Edit</button>';

		var item = '<tr>'
		+ '<td>'  + sno + '</td>'
		+ '<td>' + cart[row][0] + '</td>'
		+ '<td>' + cart[row][1]+ '</td>'
		+ '<td>' + row + '</td>'
		+ '<td>'  + cart[row][2] + '</td>'
		+ '<td>'  + cart[row][3] + '</td>'
		+ '<td>'  + cart[row][4] + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';

        $tbody.append(item);
        sno+=1;
	    bill+=(cart[row][4]);
	}
    $('#totalPrice').val(bill);

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
		callWarnToast("Enter valid Channel Order ID");
		return;
	}
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
		callWarnToast("Add some Items in Order");
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
	   		 callConfirmToast('added to cart');
	   		 resetUi();
	   },
	   error: function(response)
	   {
	   	console.log(response);
	   }
	});

}

function resetUi()
{
	$('#order-table').find('tbody').empty();
	$('#order-form').trigger('reset');
	fillDropDowns();
	$('#order-table').find('tbody').empty();
	$('#totalPrice').val(0);
	$('#channelOrderId').val("");
	   		 
}

function init()
{
	fillDropDowns();
	$('#add-order').click(addInCart);
	$('#confirm').click(placeOrder);
	$('#afresh').click(resetUi);
}


$(document).ready(init);