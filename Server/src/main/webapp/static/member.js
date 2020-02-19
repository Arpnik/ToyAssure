function getMemberUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/member";
}


function addMember()
{
	var $form = $("#member-form");
	let name=$("#member-form input[name=name]").val();	
	if( !name ||name.trim().length==0)
	{
		callWarnToast("Enter valid Client/Customer NAME");
		return false;
	}
	var json = toJson($form);
	if(!JSON.parse(json).hasOwnProperty("type"))
	 {
	 	callWarnToast("Select Client/Customer TYPE");
	 	return false;
	 }
	var url = getMemberUrl();
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getMemberList();
	   		$form[0].reset();
	   		callConfirmToast("Added Successfully");
	   },
	   error: handleAjaxError
	});

	return false;
}

function getMemberList(){
	var url = getMemberUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayMemberList(data);  
	   },
	   error: handleAjaxError
	});
}

function displayMemberList(data){
	var $tbody = $('#member-table').find('tbody');
	$tbody.empty();
	let sno=1;
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + sno + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>'  + e.type + '</td>'
		+ '</tr>';
        $tbody.append(row);
        sno+=1;
	}
}


function init(){
	$('#add-member').click(addMember);
}



$(document).ready(init);
$(document).ready(getMemberList);