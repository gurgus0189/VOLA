ns("common");
var respParser = common.ajax.responseParser;	
var valueArray = new Array();

function getDashboardUrl() {
	var url = window.location.search.substring(1); 
	var decodeUrl = decodeURI(url); // 한글짤려서 변환
	var urlArray = decodeUrl.split('&'); 
	return urlArray;
}

function replaceHtml(str){
	var html = str.replace(/&lt;/g,"<")
	 			.replace(/&gt;/g,">")
	 			.replace(/&quot;/g,'"');
	
	$(".notice_body_noticecontent").append(html);
}

$(document).ready(function() {

	$(".mMenu").click(function() {
		$(".nav").toggleClass("mNavOn");
		$(this).toggleClass("mMenuClose");
	});

	var dashboard_device_url = getDashboardUrl();	
	for(var i = 0; i<dashboard_device_url.length; i++) { 
		valueArray[i] = dashboard_device_url[i].split('='); 
		//console.log(valueArray);
		//console.log(valueArray[i][1]); // value 값
	}

    var number = parseInt(valueArray[0][1]);
    var url = "/openapi/notice/noticecontent";
    var parameter = {
    		seqno: number
    	};
 
	var errorComment = $.i18n.prop('common.error');
	
	$.ajax({
		url : url,
		data : parameter,
		//type : 'post',
		contentType : "application/json; charset=UTF-8",
		//dataType : 'json',
		success : function(response) {
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			var noticeContentStr = respObj.responseObj.data[0].noticecontent;
			replaceHtml(noticeContentStr);
		},
		error : function (err) {
			alert(errorComment);			
		}  
	});
   
	$("#list_btn").on("click",function(){
		location.href = "/notice/notice_list.do?menu_listview2=1";
	}); 
	
});

