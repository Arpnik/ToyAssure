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
//	$('#inputOrderId').val()="";
	$('#selectClient').empty();
	$('#selectCustomer').empty();
	$('#selectClient').append('<option selected hidden disabled value="">Select Client</option>');
	$('#selectCustomer').append('<option selected hidden disabled value="">Select Customer</option>');
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
		let date=data[i].createdDate;
		var actions=' <button class="btn btn-primary" onclick="showOrderDetails(' + data[i].orderId + ')">Show</button>';
		actions+=' <button  class="btn btn-primary" onclick="allocateOrder(' + data[i].orderId + ')">Allocate</button>';
		actions+=' <button  class="btn btn-primary" onclick="generateInvoice(' + data[i].orderId + ')">FullFill</button>';
		var row='<tr>'
		+ '<td>' + data[i].orderId + '</td>'
		+ '<td>' + data[i].channelOrderId + '</td>'
		+ '<td>' + data[i].status + '</td>'
		+ '<td>'  + date.dayOfMonth+'/'+date.monthValue+'/'+ date.year+' '+date.hour+':'+date.minute+':'+date.second + '</td>'
		+ '<td>' + actions + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}

}

function showOrderDetails(orderId)
{
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
		let amount=Number(data[i].orderedQuantity)*Number(data[i].sellingPricePerUnit);
		var row='<tr>'
		+ '<td>' + data[i].productName + '</td>'
		+ '<td>' + data[i].brandId + '</td>'
		+ '<td>' + (Math.round((data[i].orderedQuantity + Number.EPSILON) * 100) / 100) + '</td>'
		+ '<td>' + (Math.round((data[i].allocatedQuantity + Number.EPSILON) * 100) / 100) + '</td>'
		+ '<td>'  + data[i].sellingPricePerUnit + '</td>'
		+ '<td>'  + (Math.round((amount + Number.EPSILON) * 100) / 100) + '</td>'
		+ '</tr>';
        $tbody.append(row);
        sum+=Number(data[i].orderedQuantity)*Number(data[i].sellingPricePerUnit);
	}
	$('#totalBill').text('Total Amount: ₹'+Math.round((sum + Number.EPSILON) * 100) / 100);
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
	   callConfirmToast('Allocated maximum possible amount');
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
        success: function (data) {
        	fillTable();
            var link=document.createElement('a');
            link.href=fullUrl;
            link.download="invoice_"+orderId;
            link.click();
        },
        error:function(response)
        {
        	handleAjaxError(response);
        }
    });
}


//ipload order csv
var fileData = [];
var errorData = [];
var processCount = 0;

function uploadProducts()
{
	let clientId=$('#selectClient option:selected').val();
	if(clientId=="")
	{
		callWarnToast("Select an appropriate Client Name before uploading");
		return false;
	}
	let customerId=$('#selectCustomer option:selected').val();
	if(customerId=="")
	{
		callWarnToast("Select an appropriate Customer Name before uploading");
		return false;
	}
	let channelOrderId=$('#inputOrderId').val();
	if(!channelOrderId || channelOrderId.trim().length==0)
	{
		callWarnToast("Enter an appropriate Channel Order ID before uploading");
		return false;	
	}
	console.log(clientId);
	console.log(customerId);
	console.log(channelOrderId);
	resetUploadDialog(); 	
	console.log(channelOrderId);
	$('#upload-order-modal').modal('toggle');
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#orderFile');
	$file.val('');
	$('#orderFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function updateFileName(){
	var $file = $('#orderFile');
	var fileName = $file.val();
	$('#orderFileName').html(fileName);
}


function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function processData(){
	var file = $('#orderFile')[0].files[0];
	readFileData(file, readFileDataCallback);

}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	//Update progress
	updateUploadDialog();

	var items=[]
	for(let row=0;row<fileData.length;row++)
	{
		items.push(fileData[row]);
		// if(!Number.isInteger(Number(fileData[row].orderedQuantity)))
		// {
		// 	callWarnToast('OrderedQuantity is not an Integer for row:'+row);
		// 	return;
		// }
		processCount++;
		updateUploadDialog();
	}
	
	requestBody={};
	requestBody["clientId"]=$('#selectClient option:selected').val();;
	requestBody['customerId']=$('#selectCustomer option:selected').val();
	requestBody['channelOrderId']=$('#inputOrderId').val();
	requestBody['items']=items;
	console.log(requestBody);
	var json = JSON.stringify(requestBody);
	var url = getOrderUrl();

	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		callConfirmToast("Added Successfully");
	   		fillDropDowns();
	   		$('#inputOrderId').val("");
	   },
	   error:function(response)
	   {
	   		CsvHandlError(response,fileData);
	   		fillDropDowns();
	   		fillTable();
	   		
	   }
	});
}

function downloadErrors(){
	writeFileData(errorData);
}


function init()
{
	$('#refresh').click(fillTable);
	$('#add-order').click(uploadProducts);
	$('#orderFile').on('change', updateFileName);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
}



$(document).ready(init);
$(document).ready(fillDropDowns);
$(document).ready(fillTable);
