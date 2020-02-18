
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
    if(!(error.startsWith('[')||error.startsWith('{')))
    {
        callAlertToast(error);
        return;
    }
    res=JSON.parse(error);
    console.log(res);
    console.log(res.message);
    if(error.startsWith('{'))
    {
        callAlertToast(res.message);
        return;
    }
    var Errors="";
    for(row in res)
    {
        Errors+=row.message;
        Errors+=', ';
    }
    callAlertToast(Errors);
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
    console.log("hello");
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
