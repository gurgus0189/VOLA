ns("common") ;
var respParser = common.ajax.responseParser;

$(function() {
	init();
});

function init() {
	writeProblemDeviceHtml();
	writeOldDeviceHtml();
}

function writeProblemDeviceHtml() {
	var errorComment = $.i18n.prop('common.error');
	
	$.ajax({
		type : "post",
		url : "/openapi/problem/devicelist",
		success : function(result) {
			var respObj = respParser.parse(result);
			var returnCode = result.returnCode;
			var listTableAnalysis = $("#listTableAnalysis_P");
						
	    	if (respObj.isSuccess()) {
				var data = result.data;
				// var countNum= 1;
				var html = "";
				for (var i = 0; i < data.length; i++) {					
					html +="<ul class='listTableBody' id='listTableBody_P" + (i+1) + "'>";										
					html += makeProblemDeviceHtml(data[i], i);																	
					html+="</ul>";											
				}	
				listTableAnalysis.append(html);			
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}	    	
		}, 
		error: function (error) {
			alert(errorComment);
		}		
	});
	
}

function makeProblemDeviceHtml(device, i) {
	
	var index = (i+1);
	var updateDate = device.updatedate;
		
	var ret = formatFullDate(updateDate);
	
	var forwardDate = ret.substring(0,10);
	var backwardDate = ret.substring(10);
	//1970-01-01 00:00:00
	var html = "";
	
	html+="<li>" + index + "</li>";
	html+="<li>" + device.devicename + "</li>";
	html+="<li>" + device.groupname + "</li>";
	html+="<li>" + device.deviceid + "</li>";
	html+="<li><time class='alarmDay'>" + forwardDate +"</time><time class='alarmTime'>" + backwardDate + "</time></li>";
	return html;
}

function writeOldDeviceHtml() {
	var errorComment = $.i18n.prop('common.error');
	
	$.ajax({
		type : "post",
		url : "/openapi/old/devicelist",
		success : function(result) {
			var respObj = respParser.parse(result);
			var returnCode = result.returnCode;
			var listTableAnalysis = $("#listTableAnalysis_O");
			
	    	if (respObj.isSuccess()) {
				var data = result.data;
				var html = "";
				
				for (var i = 0; i < data.length; i++) {					
					html +="<ul class='listTableBody' id='listTableBody_O" + (i+1) + "'>";										
					html += makeOldDeviceHtml(data[i], i);																	
					html+="</ul>";											
				}	
				listTableAnalysis.append(html);			
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}	    	
		}, 
		error: function (error) {
			alert(errorComment);
		}		
	});
	
}

function makeOldDeviceHtml(device, i) {
	
	var index = (i+1);
	var createDate = device.createdate;
	
	var ret = formatFullDate(createDate);
	
	var forwardDate = ret.substring(0,10);
	var backwardDate = ret.substring(10);
	//1970-01-01 00:00:00
	var html = "";
	
	
	
	html+="<li>" + index + "</li>";
	html+="<li>" + device.devicename + "</li>";
	html+="<li>" + device.groupname + "</li>";
	html+="<li>" + device.deviceid + "</li>";
	html+="<li><time class='alarmDay'>" + forwardDate +"</time><time class='alarmTime'>" + backwardDate + "</time></li>";
	
	return html;
}