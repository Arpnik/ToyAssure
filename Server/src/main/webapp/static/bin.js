function getMemberUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/member";
}

function getBinUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/bin";
}


function fillDropDown()
{
	let url=getMemberUrl() + '/client';
	   $.ajax({
	   url: url,
	   type: 'GET',
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   processClient(response);	
	   },
	   error: handleAjaxError
	});
}

function processClient(data)
{
	$('#selectClient').empty();
	$('#selectClientId').empty();
	$('#selectClient').append('<option selected="" hidden="" disabled="" value="">Select client name</option>');
	$('#selectClientId').append('<option selected="" value="0">Select client name</option>');
	fillAnyDropdown('#selectClient',data);
	fillAnyDropdown('#selectClientId',data);
}

function fillAnyDropdown(dropselector,data)
{
	var $drop=$(dropselector);
	for (let i = 0; i < data.length; i++)
	{
		var row='<option value="'+data[i].id+'">'+data[i].name+'</option>';
		$drop.append(row);
	}
}


function createBins()
{
	let num=Number($('#inputBins').val());
	if(isNaN(num))
	{
		callWarnToast('Enter a valid positive integer');
		return;
	}
	if(num<1)
	{
		callWarnToast("Number of bins cannot be less than 1");
		return;
	}
	if(!Number.isInteger(num))
	{
		callWarnToast('Number of bins has to be integer');
		return;
	}
	var json={}
	json['numberOfBins']=num;
	let url=getBinUrl();
	$.ajax({
	   url: url,
	   type: 'POST',
	   data:JSON.stringify(json),
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   callConfirmToast("Bins created Successfully");
	   $('#inputBins').val("");
	   },
	   error: handleAjaxError
	});
}

function fillDisplayModalProduct(data)
{	
	var $thead=$('#filter-table').find('thead');
	var $tbody=$('#filter-table').find('tbody');
	$thead.empty();
	$tbody.empty();
	let sno=0;
	var clientSkuId=$('#filter-form input[name=clientSkuId]').val();
	var binId=Number($('#filter-form input[name=binId]').val());
		var row='<tr>'+
	      '<th scope="col">Bin ID</th>'+
	      '<th scope="col">Quantity</th>'+
	      '</tr>'
	    $thead.append(row);

	    for(d in data)
	    {
	    	if(Number(data[d].quantity)<=0)
	    	{
	    		continue;
	    	}
	    	row='<tr>'
			+ '<td>' + data[d].binId + '</td>'
			+ '<td>'  + data[d].quantity + '</td>'
			+ '</tr>';
			$tbody.append(row);
			sno+=1;
		}
	    $('#bin-modal').modal('toggle');

}

function displayEditQuantity(binSkuId,sno)
{
	var value = $("#filter-table tr:nth-child("+sno+")" ).find('td')[2].innerHTML;
	$("#filter-table tbody tr:nth-child("+sno+") :nth-child(3)").replaceWith('<td contenteditable=true style="outline:2px solid Blue;" >'+value+'</td>');
	$("#filter-table tbody tr:nth-child("+sno+") :nth-child(4)").replaceWith('<td><button class="btn btn-outline-success btn-sm" onclick="saveEditQuantity(' + binSkuId+','+sno+','+value+')">Save</button></td>');
	
}

function saveEditQuantity(binSkuId,sno,IntegerialValue)
{
	var value = Number($("#filter-table tr:nth-child("+sno+")" ).find('td')[2].innerHTML);
	let url=getBinUrl()+'/'+binSkuId;
	console.log(url);
	console.log(Number(value));
	if(isNaN(value))
	{
		callWarnToast('Enter a valid Integer quantity');
		$("#filter-table tbody tr:nth-child("+sno+") :nth-child(3)").replaceWith('<td contenteditable=false>'+IntegerialValue+'</td>');
	   	 $("#filter-table tbody tr:nth-child("+sno+") :nth-child(4)").replaceWith(' <td><button class="btn btn-outline-primary btn-sm" onclick="displayEditQuantity(' + binSkuId+','+sno + ')" >Edit</button></td>');
	   	 return;
	}
	if(!Number.isInteger(value))
	{
		callWarnToast('Enter a valid Integer quantity');
		$("#filter-table tbody tr:nth-child("+sno+") :nth-child(3)").replaceWith('<td contenteditable=false>'+IntegerialValue+'</td>');
	   	    $("#filter-table tbody tr:nth-child("+sno+") :nth-child(4)").replaceWith(' <td><button class="btn btn-outline-primary btn-sm" onclick="displayEditQuantity(' + binSkuId+','+sno + ')" >Edit</button></td>');
		return;
	}
	var json={}
	json['quantity']=Number(value);
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: JSON.stringify(json),
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {   
	   		callConfirmToast("Updated Product Successfully");
	   		$("#filter-table tbody tr:nth-child("+sno+") :nth-child(3)").replaceWith('<td contenteditable=false>'+value+'</td>');
	   		$("#filter-table tbody tr:nth-child("+sno+") :nth-child(4)").replaceWith(' <td><button class="btn btn-outline-primary btn-sm" onclick="displayEditQuantity(' + binSkuId+','+sno + ')" >Edit</button></td>');
	   },		
	   error:function(response){
	   	    handleAjaxError(response);
	   	    $("#filter-table tbody tr:nth-child("+sno+") :nth-child(3)").replaceWith('<td contenteditable=false>'+IntegerialValue+'</td>');
	   	    $("#filter-table tbody tr:nth-child("+sno+") :nth-child(4)").replaceWith(' <td><button class="btn btn-outline-primary btn-sm" onclick="displayEditQuantity(' + binSkuId+','+sno + ')" >Edit</button></td>');
	   	}
	});
}

function resetProductFilter()
{
	console.log("h");
	$('#product-filter-form input[name=clientSkuId]').val("");
	fillDropDown();

}

function getProductInfo()
{
	var $form = $("#product-filter-form");
	var json = JSON.parse(toJson($form));
	console.log(json);
	let url=getBinUrl()+'/product';
	$.ajax({
	   url: url,
	   type: 'POST',
	   data:JSON.stringify(json),
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   callConfirmToast("Information fetched Successfully");
	   fillDisplayModalProduct(response);
	   resetProductFilter();
	   },
	   error: handleAjaxError
	});
}

function addRowInBins(data)
{
	console.log(data);
	var row='<div class="row">'+data+'</div>';
	var $div = $('#showBinIds');
	$div.append(row);
}

function processBinIds(response)
{
	console.log(response);
	var $div = $('#showBinIds');
	$div.empty();
	count=0;
	var data=""
	for(let row in response)
	{
		data+='<div class="col-6 col-md-4 border border-primary">'+response[row].binId+'</div>';
		count+=1
		if(count>=3)
		{
			addRowInBins(data);
			data="";
			count=0;
		}
	}
	if(count>0)
	{
		addRowInBins(data);
	}
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;

function processData(){
	var file = $('#binFile')[0].files[0];
	if(file == undefined)
	{
		callWarnToast("Select a file to upload");
		return false;
	}
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	//Update progress
	updateUploadDialog();

	var binData=[]
	for(let row=0;row<fileData.length;row++)
	{
		binData.push(fileData[row]);
		processCount++;
		updateUploadDialog();
	}
	
	requestBody={};
	requestBody["binList"]=binData;
	var json = JSON.stringify(requestBody);
	var url = getBinUrl()+'/client/'+($('#selectClient option:selected').val());
	console.log(json);
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
	   },
	   error: function(response){
	   		$("#download-errors").attr("disabled",false);
	   		CsvHandlError(response,fileData);
	   }
	});
}

function downloadErrors(){
	writeFileData(errorData);
}

function displayUploadData(){
	var clientId=$('#selectClient option:selected').val();
	if(!clientId)
	{
		callWarnToast("Select an appropriate client");
		return;
	}
 	resetUploadDialog();
 	$('#download-errors').attr('disabled',true);
	$('#upload-bin-modal').modal('toggle');
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#binFile');
	$file.val('');
	$('#binFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#binFile');
	var fileName = $file.val();
	$('#binFileName').html(fileName);
}


function validateBinId(id)
{
	if(isNaN(Number(id)) || !Number.isInteger(Number(id)) || Number(id)<=0)
	{
		callWarnToast("Bin ID should be a postive integer");
		return false;
	}

	return true;
}

function binProducts()
{
	var json=toJson($('#bin-filter-form'));
	console.log(json);
	if(!validateBinId(JSON.parse(json)['binId']))
	{
		return;
	}
	let url=getBinUrl()+'/product';
	$.ajax({
	   url: url,
	   type: 'POST',
	   data:json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   callConfirmToast("Information fetched Successfully");
	   fillDisplayModalBin(response);
	   $('#bin-filter-form').trigger('reset');
	   },
	   error: handleAjaxError
	});


}

function fillDisplayModalBin(data)
{
	var $thead=$('#filter-table').find('thead');
	var $tbody=$('#filter-table').find('tbody');
	$thead.empty();
	$tbody.empty();
	let sno=0;
	
	var row='<tr>'+
	      '<th scope="col">Client Name</th>'+
	      '<th scope="col">Client Sku ID</th>'+
	      '<th scope="col">Quantity</th>'+
	      '<th scope="col">Actions</th>'+
	      '</tr>'
	$thead.append(row);
	for(d in data)
	{
		if(Number(data[d].quantity)<=0)
	    {
	    		console.log('inside');
	    		continue;
	    	}
		let buttonHtml = ' <button class="btn btn-outline-primary btn-sm" onclick="displayEditQuantity(' + data[d].binSkuId+','+(sno+1) + ')" >Edit</button>'
	    	row='<tr>'
		+ '<td>' + data[d].name + '</td>'
		+ '<td>'  + data[d].clientSkuId + '</td>'
		+ '<td contenteditable="false">'  + data[d].quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
		$tbody.append(row);
		sno+=1;
	}
	$('#bin-modal').modal('toggle');
}

function init(){
	$('#bin-search').click(binProducts);
	$('#product-search').click(getProductInfo);
	$('#create-bin').click(createBins);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#binFile').on('change', updateFileName);
    $("#bin-form").keypress(function(e) {
    	console.log("inside");
    if (e.which == 13) {
    	console.log("inside");
        var tagName = e.target.tagName.toLowerCase(); 
        if (tagName !== "button") {
            return false;
        }
    }
});
}

$(document).ready(init);
$(document).ready(fillDropDown);	