function getClientUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/member";
}


function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}


function fillDropDown()
{
	let url=getClientUrl()+'/client';
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
	var $drop=$('#selectClient');
	for (let i = 0; i < data.length; i++)
	{
		var row='<option value="'+data[i].id+'">'+data[i].name+'</option>';
		$drop.append(row);
	}
}


//upload modal
var fileData = [];
var errorData = [];
var processCount = 0;


function uploadProducts()
{
	let clientId=$('#selectClient option:selected').val();
	if(clientId=="")
	{
		callWarnToast("Select an appropriate client name before uploading");
		return false;
	}
	resetTable();
	resetUploadDialog(); 	
	$('#download-errors').attr('disabled',true);
	$('#upload-product-modal').modal('toggle');
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
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
	var $file = $('#productFile');
	var fileName = $file.val();
	$('#productFileName').html(fileName);
}

function processData(){
	var file = $('#productFile')[0].files[0];
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

	var products=[]
	for(let row=0;row<fileData.length;row++)
	{
		products.push(fileData[row]);
		processCount++;
		updateUploadDialog();
	}
	
	requestBody={};
	requestBody["productList"]=products;
	var json = JSON.stringify(requestBody);
	var url = getProductUrl()+'/'+($('#selectClient option:selected').val());
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
	   error:function(response)
	   {
	   		$('#download-errors').attr('disabled',false);
	   		CsvHandlError(response,fileData);

	   }
	});
}

function resetTable()
{
	var $thead = $('#product-table').find('thead');
	$thead.empty();
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
}

function downloadErrors(){
	writeFileData(errorData);
}

//edit products by clientId
function getProductsById()
{

	var clientId= $('#selectClient option:selected').val();
	if(clientId=="")
	{
		callWarnToast("Select an appropriate client name to view products");
		return false;
	}

	var $thead = $('#product-table').find('thead');
	$thead.empty();

	var row='<tr>'+
      '<th scope="col">Client SKU</th>'+
      '<th scope="col">Product Name</th>'+
      '<th scope="col">BrandId</th>'+
      '<th scope="col">MRP</th>'+
      '<th scope="col">Description</th>'+
      '<th scope="col">Actions</th>'+
      '</tr>'
    $thead.append(row);

	let url=getProductUrl()+'/client/'+clientId;
		$.ajax({
	   url: url,
	   type: 'GET',
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   displayProductList(response);
	   callConfirmToast('Successfully fetched');
	   },
	   error: handleAjaxError
	});
}


function displayProductList(data){
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button class="btn btn-outline-primary btn-sm" onclick="displayEditProduct(' + e.globalSkuId + ')">Edit</button>'
		var row = '<tr>'
		+ '<td>' + e.clientSkuId + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.brandId + '</td>'
		+ '<td>'  + e.mrp + '</td>'
		+ '<td>'  + e.description + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditProduct(product_id){
	var url = getProductUrl() + "/" + product_id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProduct(data);   
	   },
	   error: handleAjaxError
	});	
}

function displayProduct(data)
{
	$("#product-edit-form input[name=name]").val(data.name);	
	$("#product-edit-form input[name=brandId]").val(data.brandId);	
	$("#product-edit-form input[name=mrp]").val(data.mrp);
	$("#product-edit-form input[name=globalSkuId]").val(data.globalSkuId);
	$("textarea#Description").val(data.description);
	$('#edit-product-modal').modal('toggle');
}

function updateProduct()
{
	$('#edit-product-modal').modal('toggle');
	var id = $("#product-edit-form input[name=globalSkuId]").val();	
	var url = getProductUrl() + "/" + id;

	//Set the values to update
	var $form = $("#product-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getProductsById();   
	   		callConfirmToast("Updated Product Successfully");
	   },
	   error: handleAjaxError
	});

	return false;
}




function init()
{
	$('#upload-products').click(uploadProducts);
	$('#productFile').on('change', updateFileName);
	$('#process-data').click(processData);
	$('#display-products').click(getProductsById);
	$('#update-product').click(updateProduct);
	$('#download-errors').click(downloadErrors);
	$("#product-form").keypress(function(e) {
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
