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
	$('#selectClient').append('<option selected="" hidden="" disabled="" value="">Select Client Name</option>');
	$('#selectClientId').append('<option selected="" value="">Select Client Name</option>');
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
		callWarnToast('Please enter a valid integer number');
		return;
	}
	if(num<1)
	{
		callWarnToast("Number of Bins cannot be less than 1, Enter a valid integer number");
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

function fillDisplayModal(data)
{	
	var $thead=$('#filter-table').find('thead');
	var $tbody=$('#filter-table').find('tbody');
	$thead.empty();
	$tbody.empty();
	console.log(data[0]);
	let sno=0;
	var clientSkuId=$('#filter-form input[name=clientSkuId]').val();
	var binId=Number($('#filter-form input[name=binId]').val());
	if(binId==0 || (clientSkuId && clientSkuId.trim().length!=0 && binId!=0))
	{
		var row='<tr>'+
	      '<th scope="col">Bin ID</th>'+
	      '<th scope="col">Quantity</th>'+
	      '</tr>'
	    $thead.append(row);

	    for(d in data)
	    {
	    	row='<tr>'
			+ '<td>' + data[d].binId + '</td>'
			+ '<td contenteditable="false">'  + data[d].quantity + '</td>'
			+ '</tr>';
			$tbody.append(row);
			sno+=1;
		}
	    $('#bin-modal').modal('toggle');
	    resetFilter();
		return;
	}

	var row='<tr>'+
	      '<th scope="col">Client Name</th>'+
	      '<th scope="col">Client Sku ID</th>'+
	      '<th scope="col">Quantity</th>'+
	      '<th scope="col">Actions</th>'+
	      '</tr>'
	$thead.append(row);
	for(d in data)
	{
		let buttonHtml = ' <button class="btn btn-primary" onclick="displayEditQuantity(' + data[d].binSkuId+','+(sno+1) + ')" >edit</button>'
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
	resetFilter();

}

function displayEditQuantity(binSkuId,sno)
{
	var value = $("#filter-table tr:nth-child("+sno+")" ).find('td')[2].innerHTML;
	$("#filter-table tbody tr:nth-child("+sno+") :nth-child(3)").replaceWith('<td contenteditable=true style="outline:2px solid Blue;" >'+value+'</td>');
	$("#filter-table tbody tr:nth-child("+sno+") :nth-child(4)").replaceWith('<td><button class="btn btn-success" onclick="saveEditQuantity(' + binSkuId+','+sno+','+value+')">Save</button></td>');
	
}

function saveEditQuantity(binSkuId,sno,intialValue)
{
	var value = Number($("#filter-table tr:nth-child("+sno+")" ).find('td')[2].innerHTML);
	let url=getBinUrl()+'/'+binSkuId;
	console.log(url);
	console.log(Number(value));
	if(isNaN(value))
	{
		callWarnToast('Enter a valid integer quantity');
		$("#filter-table tbody tr:nth-child("+sno+") :nth-child(3)").replaceWith('<td contenteditable=false>'+intialValue+'</td>');
	   	 $("#filter-table tbody tr:nth-child("+sno+") :nth-child(4)").replaceWith(' <td><button class="btn btn-primary" onclick="displayEditQuantity(' + binSkuId+','+sno + ')" >edit</button></td>');
	   	 return;
	}
	if(!Number.isInteger(value))
	{
		callWarnToast('Enter a valid integer quantity');
		$("#filter-table tbody tr:nth-child("+sno+") :nth-child(3)").replaceWith('<td contenteditable=false>'+intialValue+'</td>');
	   	    $("#filter-table tbody tr:nth-child("+sno+") :nth-child(4)").replaceWith(' <td><button class="btn btn-primary" onclick="displayEditQuantity(' + binSkuId+','+sno + ')" >edit</button></td>');
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
	   		$("#filter-table tbody tr:nth-child("+sno+") :nth-child(4)").replaceWith(' <td><button class="btn btn-primary" onclick="displayEditQuantity(' + binSkuId+','+sno + ')" >edit</button></td>');
	   },		
	   error:function(response){
	   	    handleAjaxError(response);
	   	    $("#filter-table tbody tr:nth-child("+sno+") :nth-child(3)").replaceWith('<td contenteditable=false>'+intialValue+'</td>');
	   	    $("#filter-table tbody tr:nth-child("+sno+") :nth-child(4)").replaceWith(' <td><button class="btn btn-primary" onclick="displayEditQuantity(' + binSkuId+','+sno + ')" >edit</button></td>');
	   	}
	});
}

function resetFilter()
{

	$('#filter-form input[name=clientSkuId]').val("");
	$('#filter-form input[name=binId]').val("");
	fillDropDown();

}

function getProductInfo()
{
	var $form = $("#filter-form");
	var json = JSON.parse(toJson($form));
	if(!json.hasOwnProperty('clientSkuId'))
	{
		json['clientSkuId']="";
	}
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
	   fillDisplayModal(response);
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
	requestBody["requestBody"]=binData;
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

function init(){
	$('#create-bin').click(createBins);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#binFile').on('change', updateFileName);
    $('#get-productInfo').click(getProductInfo);
}

$(document).ready(init);
$(document).ready(fillDropDown);	