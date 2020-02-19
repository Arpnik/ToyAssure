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
	$('#selectChannel').append("<option hidden disabled>select channel</option>");
	
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
	if(clientId.startsWith("select"))
	{
		callWarnToast('select an appropriate client');
		return;
	}
	if(channelId.startsWith("select"))
	{
		callWarnToast('select an appropriate channel');
		return;
	}
	resetUploadDialog(); 	
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
	   		CsvHandlError(response,fileData);
	   }
	});
}

// function handleErrorsInCSV(response)
// {
// 	let res=JSON.parse(response.responseText)["message"];
//     callAlertToast("Download Errors");
//     if(!res.startsWith("\n"))
//     {
//         errorData.push(JSON.parse(response.responseText));
//         updateUploadDialog();
//         return false;
//     }
//     res=res.split("\n")
//     for(row in res)
//         {
//             if(res[row].trim().length!=0)
//             {
//                 var result=res[row].split(":");
//                 var index=result[0];
//                 if(index.includes('.'))
//                 {	
//                 	index=index.split('.')[0];
//                 	index=index[index.length-2];
//                 }
//                 var createdError=fileData[Number(index)];
//                 createdError["message"]=result.slice(1).join(' ');
//             }
//         }
//         for(let row=0;row<fileData.length;row++)
//         {
//             var error=fileData[row];
//             if(error.hasOwnProperty("message"))
//             {
//                 errorCount++;
//                 errorData.push(error);
//             }
//         }
//         updateUploadDialog();
// }

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
}


$(document).ready(init);
$(document).ready(fillClientDropDown);
$(document).ready(fillChannelDropDown);