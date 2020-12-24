ns("common") ;
var respParser = common.ajax.responseParser;

function makeDeviceHtml(devices, groupid) {
	var muserid = $("#userSensorInfo>em").attr("value");
	
	var html = "";
	var devicename = undefined;
	var max = 5;
	var len = 0;
	var checkboxhtml = "";
	var selectedCheck = "";
	
	len = devices.length;
	if (devices.length < max) {
		len = max;
	}
	
	if (devices.length > max) {
		len += max - (len % max);
	}
	
	html += "\n	<ul class=\"listTableBody\" id='listTableBody_" + groupid + "'>";
	
	for (var i = 0; i < len; i++) {		
		if (devices[i] != undefined) {		
	
			if (devices[i].subuser != undefined) {
				subUser = devices[i].subuser;
				selectedCheck = "checked";
			} else {
				selectedCheck = "";
			}

		}
		
		if (i > 0 && (i % max) == 0) {
			html += "\n	</ul>";
			/* html += "\n	<ul class=\"listTableBody\">";		 */
			html += "\n	<ul class=\"listTableBody\" id='listTableBody_" + groupid + "'>";
		}
			
		devicename = "";
		checkboxhtml = "";
		if (devices[i] != undefined) {
			devicename = devices[i].devicename;
			checkboxhtml = "\n	<input type=\"checkbox\" id=\"listChk" + devices[i].deviceid + "\" name=\"listChkbox\" data-src='" + devices[i].deviceid + "'" + selectedCheck + ">";
			checkboxhtml += "\n		<label for=\"listChk" + devices[i].deviceid + "\" class=\"listChk\"></label>";
		}
	
		html += "\n	<li>";
		html += checkboxhtml;
		if (devicename != null && devicename != "") {
			html += "\n			<span class=\"sensorNameTxt\">" + devicename + "</span>"
		}
	 	html += "\n	</li>";
	 	
	}
	html += "\n	</ul>";
 	return html;
}

function makeDevice(group) {	
	var html = makeDeviceHtml(group.devices,group.groupid);
	
	var listTableBodyID = "#listTableBody_" + group.groupid;

	return html;
	
}

function listChkAllEvent(groupId) {
	
	var checkedYn = $("#listChkAll" + groupId).prop("checked");
	var listTableBody = $("ul#listTableBody_" + groupId + " li input[name=listChkbox]");
	
	if (checkedYn) {	
		listTableBody.each(function() {
			$(this).prop("checked", true); // 찍혔을떄	
		});
	} else {	
		listTableBody.each(function() {	
			$(this).prop("checked", false);	
		});
	}
			
}

function makeDeviceGroupHtml(group) {
	var html = ""; 	
	html += "\n<div class=\"listTable listTableSensor\" id=\"listTableSensor_"+ group.groupid +"\">"
	html += "\n	<ul class='listTableHead' id='listTableHead" + group.groupid + "'>";
	html += "\n		<li><input type='checkbox' id='listChkAll" + group.groupid + "' onchange='listChkAllEvent(" + group.groupid + ")'><label for='listChkAll" + group.groupid + "' class='listChk'></label><span class='sensorGroupTxt'>" + group.groupname + "</span></li>";
	html += "\n	</ul>";
	html += makeDevice(group);
	html += "\n</div>";
	return html ;
}

function makeDeviceGroup(group) {
	var listTableID = "#listTableSensor_" + group.groupid; 
	
	if ($(listTableID).length > 0) {
		return;
	}
	//listTableSensor
	/* var listTableID= "#listTable_" + groupid; */
	var html = makeDeviceGroupHtml(group);
	$("#userSensorInfo").append(html);
}

function makeUserSensorBoardList() {
	var muserid = $("#userSensorInfo>em").attr("value");
	var errorComment = $.i18n.prop('common.error');
	
	$.ajax({
		type : "get",		
		url : "/openapi/deviceuser/getdevicelist",
		dataType : "json",
		data : {
			"userid" : muserid
		},
		success : function(result) {
			var respObj = respParser.parse(result);
			var returnCode = result.returnCode;
			
	    	if (respObj.isSuccess()) {
	    	   var data = respObj.getDataObj();
			   var group = undefined;
			   var groupid = undefined;
			   var groupname = undefined;			 
			    
			   for (var i = 0; i < data.length; i++) {
			       group = data[i];
			       groupid = group.groupid;
			       groupname = group.groupname;
			       makeDeviceGroup(group);			   
			   }
			   
	      } else {
	    	  alert(returnCode +" "+ errorComment);
	      }
	    	
		},error: function (error) {
			alert(errorComment);
		}
		
	});
}

function manageDeviceAuthor() {
	var chekcedDeviceArr = "";
	var userid = $("#userSensorInfo>em").attr("value");
	var errorComment = $.i18n.prop('common.error');
	
	$("input[name=listChkbox]").each(function() {		
		if ($(this).is(":checked")) {
			chekcedDeviceArr += "," + $(this).attr("data-src"); 
		}			
	});
	
	chekcedDeviceArr  = chekcedDeviceArr.replace(/^,/, "");
	
	$.ajax({
		type : "post",
		url : "/openapi/deviceuser/create",
		data : {
			"userid" : userid,
			"deviceid" : chekcedDeviceArr
		},
		success : function(result) {			
 			var respObj = respParser.parse(result);
 			var returnCode = result.returnCode;
 			
	    	if (respObj.isSuccess()) {
	    		var manageUpdate = $.i18n.prop('account.select.user.manage.update');
	    		alert(manageUpdate);
	    		history.back();	    		
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
	    	
		},
		error: function (error) {	        	
			alert(errorComment);
		}
	});
}

$(function() {
	makeUserSensorBoardList();	
});

$(document).ready(function() {
	  $(".mMenu").click(function(){
	    $(".nav").toggleClass("mNavOn");
	    $(this).toggleClass("mMenuClose");
	  });
	});

