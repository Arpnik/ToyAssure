
//HELPER METHOD
function toJson($form){
    var serialized = $form.serializeArray();
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}


function handleAjaxError(response){
	var response = JSON.parse(response.responseText);
	var error=response.message
    console.log(error);
    if(error.startsWith('\n'))
    {
        error=error.split(':');
        error=error.slice(1).join();
        console.log(error);
    }
    callAlertToast(error);
}

var options={
  "delay": 3000,
}   

function makeToasterHeader(background)
{
    $('#toast-header').append('<button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close"><span aria-hidden="true">&times;</span></button>')
    $('.toast-header').removeClass('bg-danger');
    $('.toast-header').removeClass('bg-warning');
    $('.toast-header').removeClass('bg-success');
    $('.toast-header').addClass(background);
}

function callAlertToast(message)
{ 
    $('#PopUpToast').toast('dispose');
    $('#toast-header').empty();
    $('#toast-header').append('<i class="fas fa-exclamation-circle" style="font-size:30px"></i>');
    $('#toast-header').append('<strong class="mr-auto">ALERT</strong>');
    makeToasterHeader("bg-danger");
    $('#PopUpToast').toast(options);
    $('#toast-body').empty();
    $('#toast-body').text(message);
    $('#PopUpToast').toast('show');
}

function callWarnToast(message)
{   
   $('#PopUpToast').toast('dispose');
    $('#toast-header').empty();
    $('#toast-header').append('<i class="fas fa-exclamation-triangle" style="font-size:30px"></i>');
    $('#toast-header').append('<strong class="mr-auto">WARNING</strong>');
    makeToasterHeader("bg-warning");
    $('#PopUpToast').toast(options);
    $('#toast-body').empty();
    $('#toast-body').text(message);
    $('#PopUpToast').toast('show');
}

function callConfirmToast(message)
{
    $('#PopUpToast').toast('dispose');
    $('#toast-header').empty();
    $('#toast-header').append('<i class="fa fa-check" style="font-size:30px"></i>');
    $('#toast-header').append('<strong class="mr-auto">SUCCESS</strong>');
    makeToasterHeader("bg-success");
    $('#PopUpToast').toast(options);
    $('#toast-body').empty();
    $('#toast-body').text(message);
    $('#PopUpToast').toast('show');
}


function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: ",",
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}	
	}
	Papa.parse(file, config);
}

function CsvHandlError(response,fileData)
{
    let res=JSON.parse(response.responseText)["message"];
    callAlertToast("Download Errors");
    if(!res.includes("requestBody"))
    {
        errorData.push(JSON.parse(response.responseText));
        updateUploadDialog();
        return false;
    }
    res=res.split("\n")
    for(row in res)
        {
            if(res[row].trim().length!=0)
            {
                var result=res[row].split(":");
                var index=(JSON.stringify(result[0].split(".")[0].match("\\[[^\\]]*]")[0]).slice(2,-2))
                var createdError=fileData[Number(index)];
                if(createdError.hasOwnProperty("message"))
                {
                    var newMessage=createdError["message"].concat("\t",result[1]);
                    createdError["message"]=newMessage;
                }
                else
                createdError["message"]=result[1];
            }
        }
        for(let row=0;row<fileData.length;row++)
        {
            var error=fileData[row];
            if(error.hasOwnProperty("message"))
            {
                errorCount++;
                errorData.push(error);
                console.log(error);
            }
        }
        updateUploadDialog();
}

function writeFileData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: ","
	};
	
	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'download.csv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'download.csv');
    tempLink.click(); 
}
