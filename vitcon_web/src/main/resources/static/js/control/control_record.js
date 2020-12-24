ns("common") ;
var respParser = common.ajax.responseParser;
var selectedDate = "";
$(document).ready(function() {
	//mobile menu
	$(".mMenu").click(function() {
		$(".nav").toggleClass("mNavOn");
		$(this).toggleClass("mMenuClose");
	});
			
	$("#btnSearchTop").on("click", function() {		
		getDeviceData();
	});
	
	$("#deviceGroupList").on("change", getDeviceTypeList);
	
	init();
});	

function init() {
	
	//datepicker 초기설정
	setInitDate();
	
	//디바이스그룹 리스트
	getGroupList();
	
	//디바이스 리스트
	//getDeviceTypeList();
	
	// 맨 처음 화면 보여줄 경우
	getDeviceData();
}

function setInitDate() {
	var now = new Date();
	
    var year= now.getFullYear();
    var month = (now.getMonth()+1)>9 ? ''+(now.getMonth()+1) : '0'+(now.getMonth()+1);
    var day = now.getDate()>9 ? ''+now.getDate() : '0'+now.getDate();
    var today = year + '-' + month + '-' + day;
    var html = "<em>" + today + "</em><i></i>";
    selectedDate =today;
	
    //datepicker
	flatpickr('#date', {
		defaultDate: [today],
		onChange: function(selectedDates, dateStr, instance) {
	        var html = "<em>" + dateStr + "</em><i></i>";
	        $("#date").html(html);
	        //selectedDate = selectedDates.map(date => this.formatDate(date, "Y-m-d")) + ""; //문자열 바꾸기 트릭
	        selectedDate = selectedDates.map(function (date) { return formatDate(date, "Y-m-d"); }) + ""; //문자열 바꾸기 트릭	        
	        //getDeviceList($("#deviceGroupList option:selected").val());
	    },
	    disableMobile: "true"
	});
    
	$("#date").html(html);   
}

//SELECT 디바이스 리스트 가져오기
function getDeviceTypeList(groupid) {
	var url = "/openapi/device/type/list";
	var errorComment = $.i18n.prop('common.error');
	var param = {
		groupid : $("#deviceGroupList").val()
	};

	$.ajax({
		method: 'POST',
		url : url,
		responseType:'json',
		data: param,
		success : function(response) {
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;

	    	if (respObj.isSuccess()) {
	    		var data = respObj.getDataObj();
	    		var html = "";
	    		
				//var searchType = $.i18n.prop('control.search.type');
	    		//html += "<option value=\"\">"+ searchType +"</option>";
				$.each(data, function(key, value) {
		    		html += "<option value=\"" + value.devicetypecd + "\">" + value.devicetypename+ "</option>";
				});
				
				//list 화면의 타입리스트
				$("#deviceTypeList").html(html);
				
				// 데이터가 있을경우와 없을경우 구분
				if (data.length > 0) {
					$("#listTableAlarm").css("display", "");
					$("#noData").css("display", "none");
				} else {
					$("#listTableAlarm").css("display", "none");
					$("#noData").css("display", "");
				}	
	    	} else {
	    	    alert(returnCode +" "+ errorComment);
	    	}
		},
		error : function () {
			alert(errorComment);
		}  
	});
	
}

function getDeviceData() {
	var groupid = $("#deviceGroupList").val();
	var devicetypecd = $("#deviceTypeList").val();
	var errorComment = $.i18n.prop('common.error');
	
	$.ajax({
		type : "post",
		url : "/openapi/control/controlrecordlist",
		data : {
			"regdate" : selectedDate,
			"groupid" : groupid,
			"devicetypecd" : devicetypecd,
		},
		success : function(result) {
			var returnCode = result.returnCode;
			
			drawDeviceData(result);												
		},
		error: function (error) {
			alert(errorComment);
		}
	});
	
}

// jqGrid 그려주는 함수
function drawDeviceData(result) {
	$("#listTableAlarm").empty();// 조회할 경우 한번 비운다...
	var respObj = respParser.parse(result);	
		
	if (respObj.isSuccess()) {		
		var data = respObj.getDataObj();
		var date = $.i18n.prop('common.list.date');
		var deviceName = $.i18n.prop('common.devicename');
		var searchResult = $.i18n.prop('control.search.result');
		var errorComment = $.i18n.prop('common.error');
		var html = "<ul class='listTableHead'><li>"+ date +"</li><li>"+ deviceName +"</li><li>"+ searchResult +"</li></ul>";				
		
		$.each(data, function(i, v){
			var index = (i+1);
			html += "\n <ul class='listTableBody' id='listTableBody" + index + "'>";
			html += "\n <li><time class='alarmDay'>" + v.regdate + "</time><time class='alarmTime'>" + v.regdateTime + "</time></li>";
			html += "\n <li>" + v.devicename + "</li>";
			html += "\n <li><span class='alarmCon'>" + v.data + "</span></li>";
			html += "\n </ul>";	    				    			
		});		
		$("#listTableAlarm").append(html);
		
		// 데이터가 있을경우와 없을경우 구분
		if (result.data.length > 0) {
			$("#listTableAlarm").css("display", "");
			$("#noData").css("display", "none");
		} else {
			$("#listTableAlarm").css("display", "none");
			$("#noData").css("display", "");
		}
	} else {
		alert(errorComment);
		return;
	}			
}

// 그룹 select 태그 설정
function getGroupList() {
	var url = "/openapi/device/group/list";
	var errorComment = $.i18n.prop('common.error');
	
	$.ajax({
		url : url,
		success : function(response) {
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			
	    	if (respObj.isSuccess()) {
	    		var data = respObj.getDataObj();
	    		var html = "";
	    		
				var searchGroup = $.i18n.prop('control.search.group');
				
	    		html += "<option value=\"\">"+ searchGroup +"</option>";
				$.each(data, function(key, value) {
			        html += "<option value=\"" + value.groupid + "\">" + value.groupname+ "</option>";
				});
				//list 화면의 그룹리스트
				$("#deviceGroupList").append(html);
				//Layer 화면의 그룹리스트
				// $("#deviceUpdateGroupList").append(html);
				
				//디바이스 리스트
				getDeviceTypeList();
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
		},
		error : function () {
			alert(errorComment);
		}  
	});
}

// 널값 체크하는 함수
function isEmpty(value) {	
	if (value == null || value.trim() == "") {
		return true;
	}	
}