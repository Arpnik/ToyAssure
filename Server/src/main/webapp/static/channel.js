function getChannelUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/channel";
}

function getMemberUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/member";
}

function fillClientDropDown()
{
	let url=getMemberUrl()+'/client';
	$.ajax({
	   url: url,
	   type: 'GET',
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		fillAnyDropdown('#selectClient',response);
	   },
	   error: handleAjaxError
	});

}
function fillChannelDropDown()
{
	selectedOption=$('#selectChannel option:selected').val()
	$('#selectChannel').empty();
	$('#selectChannel').append("<option selected hidden disabled>Select name</option>");
	
	let url=getChannelUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		fillAnyDropdown('#selectChannel',response);
	   		$('#selectChannel').val(selectedOption);
	   },
	   error: handleAjaxError
	});

}
function fillAnyDropdown(dropselector,filler)
{	var $drop=$(dropselector);
    for(let item in filler){
        var row='<option value="'+filler[item].id+'">'+filler[item].name+'</option>';
        $drop.append(row);
    }
}


// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function displayUploadData(){
	let clientId=$('#selectClient option:selected').val();
	let channelId=$('#selectChannel option:selected').val();
	console.log(clientId);
	console.log(channelId);
	console.log(clientId);
	console.log(channelId);
	if(clientId.startsWith("Select"))
	{
		callWarnToast('Select an appropriate client');
		return;
	}
	if(channelId.startsWith("Select"))
	{
		callWarnToast('Select an appropriate channel');
		return;
	}
	resetUploadDialog(); 	
	$('#download-errors').attr('disabled',true);
	$('#upload-channel-modal').modal('toggle');
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#channelFile');
	$file.val('');
	$('#channelFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function processData(){
	var file = $('#channelFile')[0].files[0];
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
	updateUploadDialog();
	//work from here 
	var skus=[]
	for(let row=0;row<fileData.length;row++)
	{
		skus.push(fileData[row]);
		processCount++;
		updateUploadDialog();
	}
	
	requestBody={};
	requestBody["channelId"]=Number($('#selectChannel option:selected').val());
	requestBody["clientAndChannelSkuList"]=skus;
	requestBody["clientId"]=Number($('#selectClient option:selected').val());
	var json = JSON.stringify(requestBody);
	var url = getChannelUrl()+'/listing';
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
	   		$('download-errors').attr('disabled',true);
	   		CsvHandlError(response,fileData);
	   }
	});
}

function downloadErrors(){
	writeFileData(errorData);
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function addChannel()
{
	var $form = $("#channel-form");
	var json = toJson($form);
	var url = getChannelUrl();
	console.log(json);
	console.log(typeof(json));
	let name=JSON.parse(json)['name'];
	if( name.trim().length==0)
	{
		callWarnToast("Enter valid channel name");
		return false;
	}
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		$form.trigger('reset');
	   		callConfirmToast("Added Successfully");
	   		fillChannelDropDown();
	   },
	   error: function(response)
	   {
	   	handleAjaxError(response);
	   	$form.trigger('reset');
	   }
	});
}

function updateFileName(){
	var $file = $('#channelFile');
	var fileName = $file.val();
	$('#channelFileName').html(fileName);
}

function init()
{
	$('#add-channel').click(addChannel);
	$('#upload-channels').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#channelFile').on('change', updateFileName);

    $('#channel-form').keypress(function(e) {
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
$(document).ready(fillClientDropDown);
$(document).ready(fillChannelDropDown);