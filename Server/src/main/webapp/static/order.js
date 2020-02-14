function getMemberUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/member";
}

















$(document).ready(init);
$(document).ready(fillDropDown);
