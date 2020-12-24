ns("common");
var respParser = common.ajax.responseParser;
var selectedDateArr = new Array();
var grid;

var alarmDataArr = [];
var gridAlarmDataArr = [];

var firstPage = 1;
var visiblePage = 0; // 화면에 나타낼 페이지 수 
var perPage = 20; // 페이지당 데이터 갯수
var setTotalData = 0; // set당 전체 데이터 갯수
var setPage = 0;
var homeSeqno = 0;
var lastSet = false;

var lastIndex = 0;
var lastSeqno = 0;


function getAlarmData(h_group,h_type,h_deviceid) {
	
	var url = "/openapi/alarm/alarmData";
	
	var selectedStartDate = $("#searchStartDate").val();
	var selectedEndDate = $("#searchEndDate").val();
	
	var diff = dateDiff(selectedStartDate,selectedEndDate);
	
	if(diff > 31) {
		alert($.i18n.prop('alarm.list.alert'));
		return;
	}
	
	$("#loading-container").show();
	
	var parameter = { 
		startdate : selectedStartDate,
		enddate : selectedEndDate,
		groupid : h_group,
		devicetypecd : h_type,
		deviceid : h_deviceid
	};		
	
	var data;
	
	$.ajax({
		url : url,
		type : 'get',
		data : parameter,
		success : function(response) {
			
			alarmDataArr = response.data;
			
			gridCreate(h_group,h_type,h_deviceid,alarmDataArr);
			
			$("#loading-container").hide();
			
			if(alarmDataArr == 0) {
				$("#pagination_container").hide();
				$("#noData1").show();
				$("#grid").hide();
				return;
			} else {
				$("#pagination_container").show();
				$("#noData1").hide();
				$("#grid").show();
			}
			
			homeSeqno = alarmDataArr[0].seqno;
			
		},
		error : function () {
			alert("error");
		}  
	});
	
}

function getAlarmNextData(lastSeqno) {
	
	var url = "/openapi/alarm/alarmNextData";
	
	var selectedStartDate = $("#searchStartDate").val();
	var selectedEndDate = $("#searchEndDate").val();
	var groupid = $("#deviceGroupList").val();
	var devicetypecd = $("#deviceTypeList").val();
	var deviceid = $("#deviceList").val();

	var parameter = { 
		startdate : selectedStartDate,
		enddate : selectedEndDate,
		groupid : groupid,
		devicetypecd : devicetypecd,
		deviceid : deviceid,
		lastSeqno : lastSeqno
	};		
	
	var data;
	
	$.ajax({
		url : url,
		type : 'get',
		data : parameter,
		async: false,
		success : function(response) {
			
			data = response.data;
			
		},
		error : function () {
			alert("error");
		}  
	});
	
	return data;
}

function getAlarmPreData(firstSeqno) {
	
	var url = "/openapi/alarm/alarmPreData";
	
	var selectedStartDate = $("#searchStartDate").val();
	var selectedEndDate = $("#searchEndDate").val();
	var groupid = $("#deviceGroupList").val();
	var devicetypecd = $("#deviceTypeList").val();
	var deviceid = $("#deviceList").val();
	
	var parameter = { 
		startdate : selectedStartDate,
		enddate : selectedEndDate,
		groupid : groupid,
		devicetypecd : devicetypecd,
		deviceid : deviceid,
		firstSeqno : firstSeqno
	};		
	
	var data;
	
	$.ajax({
		url : url,
		type : 'get',
		data : parameter,
		async: false,
		success : function(response) {
			
			data = response.data;
		
		},
		error : function () {
			alert("error");
		}  
	});
	
	return data;
}

function getAlarmHomeData() {
	
	var url = "/openapi/alarm/alarmHomeData";
	
	var selectedStartDate = $("#searchStartDate").val();
	var selectedEndDate = $("#searchEndDate").val();
	var groupid = $("#deviceGroupList").val();
	var devicetypecd = $("#deviceTypeList").val();
	var deviceid = $("#deviceList").val();
	
	var parameter = { 
		startdate : selectedStartDate,
		enddate : selectedEndDate,
		groupid : groupid,
		devicetypecd : devicetypecd,
		deviceid : deviceid,
		homeSeqno : homeSeqno
	};		

	var data;
	
	$.ajax({
		url : url,
		type : 'get',
		data : parameter,
		async: false,
		success : function(response) {
			
			data = response.data;
			
		},
		error : function () {
			alert("error");
		}  
	});
	
	return data;
}

function pagination_init(firstPage,perPage,setTotalData) {
	
	var pageHtml = "";
	var page_count = 1;
	
	$("#pagination_container").empty();
	
	pageHtml += "\n   <a class=\"page-link\" onclick=\"pageHome();\" >&lt;&lt;</a>";
	pageHtml += "\n   <a class=\"page-link\" id=\"pre_btn\" onclick=\"pagePre();\" >&lt;</a>";
	
	for(var i=firstPage; i<(visiblePage+firstPage); i++) {
	
		pageHtml += "\n   <a class=\"page-link\" id=\"page_" + i + "\" value=\"" + page_count + "\">" + i + "</a>";
		++page_count;
	}
	
	pageHtml += "\n   <a class=\"page-link\" id=\"next_btn\" onclick=\"pageNext();\"  >&gt;</a>";
	
	$("#pagination_container").append(pageHtml);
	
	if( (firstPage) != 1 ) $("#pre_btn").css("display","");
	else $("#pre_btn").css("display","none");
	
	
	
	if(setTotalData != 100) $("#next_btn").css("display","none");
    else $("#next_btn").css("display","");
	
	$(".page-link").on("click", function() {
		
		var cnt = $(this).attr("value");
		var textValue = $(this).text();
		
		$(".page-link").css("background-color","#193C5C");
		$("#page_"+textValue).css("background-color","#f56565");
		
		gridAlarmDataArr = alarmDataArr.slice( perPage * (cnt - 1), perPage * cnt);
		
		grid.resetData(gridAlarmDataArr);

	});
	
}

function pageNext() {
	
	$("#loading-container").show();
	
	firstPage += 5;
	setPage++;
	
	lastIndex = (alarmDataArr.length-1);
	lastSeqno = alarmDataArr[lastIndex].seqno;

	alarmDataArr = getAlarmNextData(lastSeqno);
	
	setTotalData = alarmDataArr.length;
	
	visiblePage = ( Math.ceil( setTotalData / perPage) );
	
	pagination_init(firstPage,perPage,setTotalData);
	
	gridPageArr = alarmDataArr.slice(0, perPage);
	
	
	
	grid.resetData(gridPageArr);
	
	$("#page_" + firstPage).css("background-color","#f56565");
	
	
	
	if(setTotalData == 0 ) {
		lastSet = true;
	} else {
		lastSet = false;
	}
	
	setTimeout(function() {
		$("#loading-container").hide();
	},400);
	
}

function pagePre() {

	$("#loading-container").show();
	
	firstPage -= 5;
	setPage--;
	
	if(!lastSet) {
		var firstSeqno = alarmDataArr[0].seqno;
		alarmDataArr = getAlarmPreData(firstSeqno);
	} else {
		alarmDataArr = getAlarmPreData(lastSeqno-1);
		lastSet = false;
	}
	
	setTotalData = alarmDataArr.length;
	
	visiblePage = ( Math.ceil( setTotalData / perPage) );
	
	pagination_init(firstPage,perPage,setTotalData);
	
	gridPageArr = alarmDataArr.slice(0, perPage);
	
	grid.resetData(gridPageArr);
	
	$("#page_"+firstPage).css("background-color","#f56565");

	setTimeout(function() {
		$("#loading-container").hide();
	},400);
	
}

function pageHome() {
	
	$("#loading-container").show();
	
	firstPage = 1;
	
	alarmDataArr = getAlarmHomeData();
	
	setTotalData = alarmDataArr.length;
	
	visiblePage = ( Math.ceil( setTotalData / perPage) );
	
	pagination_init(firstPage,perPage,setTotalData);
	
	
	
	gridPageArr = alarmDataArr.slice(0, perPage);
	
	grid.resetData(gridPageArr);
	
	$("#page_"+firstPage).css("background-color","#f56565");

	setTimeout(function() {
		$("#loading-container").hide();
	},400);
}


$(document).ready(function() {

	$("#loading-container").hide();
	
	//mobile menu
	$(".mMenu").click(function() {
		$(".nav").toggleClass("mNavOn");
		$(this).toggleClass("mMenuClose");
	});
	
	init();

	//조회버튼 click
	$(".btnSearchTop").on("click", function() {
		//getAlarmList($("#deviceGroupList").val(),$("#deviceTypeList").val(),$("#deviceList").val());
		
		getAlarmData($("#deviceGroupList").val(),$("#deviceTypeList").val(),$("#deviceList").val());
	});
	
	$("#deviceGroupList").on("change", function(){
//		console.log("그룹리스트체인지");
//		console.log("그룹리스트" + $(this).val() + "타입리스트" + $("#deviceTypeList").val());
		getDeviceTypeList($(this).val(),"");
//		getDeviceList($(this).val(),$("#deviceTypeList").val(),$("#deviceList").val());
		getDeviceList($(this).val(),"","");
	});

	$("#deviceTypeList").on("change", function(){
//		console.log("타입리스트체인지");
//		console.log("그룹리스트" + $("#deviceGroupList").val() + " 타입리스트"+ $(this).val()+ "디바이스리스트 :" + $(this).val(),$("#deviceList").val());
//		getDeviceList($("#deviceGroupList").val(),$(this).val(),$("#deviceList").val());
		getDeviceList($("#deviceGroupList").val(),$(this).val(),"");
		
	});
	
	
});

//Search Date
jQuery.fn.schDate = function(){
    var $obj = $(this);
    var $chk = $obj.find("input[type=radio]");
    $chk.click(function(){
        $('input:not(:checked)').parent(".chkbox2").removeClass("on");
        $('input:checked').parent(".chkbox2").addClass("on");
    });
};

// DateClick
jQuery.fn.dateclick = function(){
    var $obj = $(this);
    $obj.click(function(){
        $(this).parent().find("input").focus();
    });
}

function setSearchDate(start){

    var num = start.substring(0,1);
    var str = start.substring(1,2);
    var today = new Date();

    //var year = today.getFullYear();
    //var month = today.getMonth() + 1;
    //var day = today.getDate();
		
    var endDate = $.datepicker.formatDate('yy-mm-dd', today);
    
    $('#searchEndDate').val(endDate);
	
    if(str == 'd'){
        today.setDate(today.getDate() - num);
    }else if (str == 'w'){
        today.setDate(today.getDate() - (num*7));
    }else if (str == 'm'){
        today.setMonth(today.getMonth() - num);
        today.setDate(today.getDate() + 1);
    }

    var startDate = $.datepicker.formatDate('yy-mm-dd', today);
    $('#searchStartDate').val(startDate);

    // 종료일은 시작일 이전 날짜 선택하지 못하도록 비활성화
    $("#searchEndDate").datepicker( "option", "minDate", startDate );

    // 시작일은 종료일 이후 날짜 선택하지 못하도록 비활성화
    $("#searchStartDate").datepicker( "option", "maxDate", endDate );
}

function init() {
	
	// 일별,그룹,타입,디바이스를 가져고 들어올때
	var dashboard_device_url = getDashboardUrl();
	var valueArray = new Array();

	for(var i = 0; i<dashboard_device_url.length; i++) { 
		valueArray[i] = dashboard_device_url[i].split('='); 
			//console.log(valueArray);
			//console.log(valueArray[i]); // value 값
		}
	
	var h_group ="";
	var h_type ="";
	var h_deviceid ="";
	var h_datetime = ""; // 오늘날짜
		
	if(dashboard_device_url.length==5){
		h_group = valueArray[1][1];	
		h_type = valueArray[2][1];
		h_deviceid = valueArray[3][1];
		h_datetime = valueArray[4][1];
	}

	//datepicker 초기설정
	setInitDate(h_datetime);
	//디바이스그룹 리스트
	getGroupList(h_group);
	//디바이스타입 리스트
	getDeviceTypeList(h_group,h_type);
	//디바이스아이디 리스트
	getDeviceList(h_group,h_type,h_deviceid); 
	
	
	
	
	getAlarmData(h_group,h_type,h_deviceid);
	
	//알람내역 리스트
	//getAlarmList(h_group,h_type,h_deviceid);
}

function getDashboardUrl() {
	
	var url = window.location.search.substring(1); 
	var decodeUrl = decodeURI(url); // 한글짤려서 변환
	//console.log(decodeUrl);
	var urlArray = decodeUrl.split('&'); 
	//console.log(decodeUrl);
	return urlArray;

}

function setInitDate(h_datetime) {
	var now = new Date();
    var year= now.getFullYear();
    var mon = (now.getMonth()+1)>9 ? ''+(now.getMonth()+1) : '0'+(now.getMonth()+1);
    var day = now.getDate()>9 ? ''+now.getDate() : '0'+now.getDate();
    //var today = year + '-' + mon + '-' + day;
    //selectedDate = today;
    var start = year + '-' + mon + '-' + day + " 00:00:00";
    var end = year + '-' + mon + '-' + day + " 23:59:59";
    //var html = "<em>" + today + "</em><i></i>";
    selectedDateArr[0] = start; 
	selectedDateArr[1] = end;
    
	// 쿠키 가져오기
	var language = getCookie("localecd");
	
    //datepicker 한국어로 사용하기 위한 언어설정
	if(language == "ko") {
	 $.datepicker.setDefaults($.datepicker.regional["ko"]);
	} else if(language == "en") {
	 $.datepicker.setDefaults($.datepicker.regional["en"]);
	} else if(language == "ja") {
	 $.datepicker.setDefaults($.datepicker.regional["ja"]);
	} 
	 
	$(".dateclick").dateclick();    // DateClick
	$(".searchDate").schDate();        // searchDate
	
	$("#searchStartDate, #searchEndDate").datepicker({
        showButtonPanel: true,
        dateFormat: "yy-mm-dd",
        changeYear: true,
        changeMonth: true,        
        onClose : function () {
        	
	        var eleId = $(this).attr("id");
	        var optionName = "";
	        
	        $("#"+eleId).datepicker( "option", optionName, selectedDateArr[0] );
	        $(".searchDate").find(".chkbox2").removeClass("on");
	        
        }
    }).attr('readonly', 'readonly');
	
	if(h_datetime == "") {
		$('#searchStartDate').datepicker('setDate', start);
	} else {
		$('#searchStartDate').datepicker('setDate', h_datetime);
	}
	$('#searchEndDate').datepicker('setDate', end);
	
  
}

function getGroupList(k_groupid) {
	
	$("#deviceGroupList").html("");
	
	var url = "/openapi/device/group/list";
	var searchByGroup = $.i18n.prop('alarm.search.group');
	var errorComment = $.i18n.prop('common.error');

	$.ajax({
		url : url,
		success : function(response) {
			var respObj = respParser.parse(response);
    		var returnCode = response.returnCode;
    		
	    	if (respObj.isSuccess()) {
	    		var data = respObj.getDataObj();
	    		var html = "";

	    		html += "<option value=\"\">"+ searchByGroup +"</option>";
				$.each(data, function(key, value) {
			        html += "<option value=\"" + value.groupid + "\">" + value.groupname+ "</option>";
				});
				//list 화면의 그룹리스트
				$("#deviceGroupList").append(html);
			
				//셀렉트
				$("#deviceGroupList").val(k_groupid).prop("selected", true);
				
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
		},
		error : function () {
    		alert(errorComment);
		}  
	});
}

function getDeviceTypeList(k_groupid, k_type) {
	var url = "/openapi/device/type/list";
	var searchByType = $.i18n.prop('alarm.search.type');
	var errorComment = $.i18n.prop('common.error');
	var param = {
//		groupid : $("#deviceGroupList").val()
		groupid : k_groupid
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
	    		
	    		if(data.length==0){
	    			
	    			html += "<option value=\"\">"+ $.i18n.prop('alarm.search.none') +"</option>";
	    			$("#deviceTypeList").html(html);
	    			$("#deviceList").html(html);
	    		}
	    		else{
	    			
	    			html += "<option value=\"\">"+ searchByType +"</option>";
	    			$.each(data, function(key, value) {
	    				html += "<option value=\"" + value.devicetypecd + "\">" + value.devicetypename+ "</option>";
	    			});
	    			//list 화면의 타입리스트
	    			$("#deviceTypeList").html(html);
	    		
	    			//셀렉트
					$("#deviceTypeList").val(k_type).prop("selected", true);
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

/*function getDeviceList(k_groupid, k_type, k_deviceid) {
	
	if( $("#deviceTypeList").val() == null){
		$("#deviceList").html("");
		return;
	}
	
	//타입이 null 아닐경우만 조회 한다
	var url = "/openapi/device/list";
//	var parameter = { groupid : $("#deviceGroupList").val(), devicetypecd : $("#deviceTypeList").val()};
	var parameter = { groupid : k_groupid, devicetypecd : k_type};
	var errorComment = $.i18n.prop('common.error');
	
	$.ajax({
		url : url,
		data : parameter,
		success : function(response) {
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			if (respObj.isSuccess()) {
				var data = respObj.getDataObj();
				var returnCode = response.returnCode;
	    		var selectDevice = $.i18n.prop('alarm.search.device');
				var html = "";
				html += "<option value=\"\">"+ selectDevice +"</option>";
				$.each(data, function(key, value) {
					html += "<option value=\"" + value.deviceid + "\">" + value.devicename + "</option>";
				});
				$("#deviceList").html(html);
				
				//셀렉트
				$("#deviceList").val(k_deviceid).prop("selected", true);
				
			} else {
				alert(returnCode +" "+ errorComment);
			}
		},
		error : function () {
			alert(errorComment);
		}  
	});
}*/

function getDeviceList(k_groupid, k_type, k_deviceid) {
	
	/*if( $("#deviceTypeList").val() == null){
		$("#deviceList").html("");
		return;
	}*/
	
	//타입이 null 아닐경우만 조회 한다
	var url = "/openapi/device/list";
//	var parameter = { groupid : $("#deviceGroupList").val(), devicetypecd : $("#deviceTypeList").val()};
	var parameter = { groupid : k_groupid, devicetypecd : k_type};
	var errorComment = $.i18n.prop('common.error');
	
	$.ajax({
		url : url,
		data : parameter,
		success : function(response) {
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			if (respObj.isSuccess()) {
				
				var data = respObj.getDataObj();
	    		var html = "";
	    		
	    		if(data.length==0){
	    			
	    			html += "<option value=\"\">"+ $.i18n.prop('alarm.search.none') +"</option>";
	    			$("#deviceList").html(html);
	    		}
	    		else{
	    			var selectDevice = $.i18n.prop('alarm.search.device');
	    			
	    			html += "<option value=\"\">"+ selectDevice +"</option>";
					$.each(data, function(key, value) {
						html += "<option value=\"" + value.deviceid + "\">" + value.devicename + "</option>";
					});
					$("#deviceList").html(html);
				
					//셀렉트
					$("#deviceList").val(k_deviceid).prop("selected", true);
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

function gridCreate(h_group,h_type,h_deviceid,alarmDataArr) {
	
	  firstPage = 1;
	  $("#grid").empty();
	 
	  var regdate_title = $.i18n.prop('common.list.date');
	  var devicename_title = $.i18n.prop('alarm.list.devicename');
	  var alarmcontents_title = $.i18n.prop('alarm.list.contents');
	
	
	  var windowWidth = $(window).width();
	  var divWidth = $(".searchDate").width();
	  var regdateWidth = windowWidth * 0.25;
	  var nameWidth = windowWidth * 0.25;
	  var alarmWidth = windowWidth * 0.5;
	
	  var whiteSpace = 'pre-wrap';
	  
	  if(windowWidth > 1279) {
		  regdateWidth = divWidth * 0.15;
		  nameWidth = divWidth * 0.15;
		  alarmWidth = divWidth * 0.7;
		  whiteSpace = 'normal';
	  }
	  
	  

	  setTotalData = alarmDataArr.length; // set당 전체 데이터 갯수
	  visiblePage = ( Math.ceil( setTotalData / perPage) ); // 화면에 나타낼 페이지 수 
	
	  gridAlarmDataArr = alarmDataArr.slice(0,perPage);
	
	  pagination_init(firstPage,perPage,setTotalData);
	
	  $("#page_1").css("background-color","#f56565");
		
		
		
	  
	  grid = new tui.Grid({
			
			el: document.getElementById('grid'),
			data : gridAlarmDataArr,
			scrollX: true,
			scrollY: false,
			rowHeight : 'auto',
			rowHeaders: [ { type: 'rowNum'} ],
			pagination: true,
			columns: [
				{
					header: regdate_title,
					name: 'regdate',
					align: 'center',
					whiteSpace: whiteSpace,
					width: regdateWidth
				},
				{
					header: devicename_title,
					name: 'devicename',
					align: 'center',
					whiteSpace: 'pre-wrap',
					width: nameWidth
				},
				{
					header: alarmcontents_title,
					name: 'alarmmessage',
					align: 'left',
					whiteSpace: 'pre-wrap',
					width: alarmWidth
				}
			]
		});	
		
		var Grid = tui.Grid; // or require('tui-grid')

		Grid.applyTheme('striped', {
			
		    grid: {
		        text: '#000'
		    },
		    row: {
		    	even : {
		    		background: '#203844',    			
		    	},
		    	odd : {
		    		background: '#122835'
		    	},
		    	hover: {
		    	    background: '#5dc6ff'
		    	}
		    },
		    cell: {
		    	nomal: {
		    		border: '#014386'
		    	},
		    },
		    scrollbar : {
		    	background: '#09212D',       
		    	emptySpace: '#09212D',     
		    	border : '#09212D',
		    	thumb : '#208BE4',
		    	active : '#5dc6ff'
		    }
		});
	  
		$(".tui-grid-body-area").css("background-color","#122835");
		$(".tui-grid-cell-header").css({ "background-color":"#173C4F","color":"#339BD8","border-color":"#173C4F","border-top":"3px solid #339BD8"});
		$(".tui-grid-border-line-top").css("background-color","#0b3f73");
		$(".tui-grid-container").attr("style","z-index:1;");
	
		
}


function dateDiff(_date1, _date2) {
    var diffDate_1 = _date1 instanceof Date ? _date1 : new Date(_date1);
    var diffDate_2 = _date2 instanceof Date ? _date2 : new Date(_date2);
 
    diffDate_1 = new Date(diffDate_1.getFullYear(), diffDate_1.getMonth()+1, diffDate_1.getDate());
    diffDate_2 = new Date(diffDate_2.getFullYear(), diffDate_2.getMonth()+1, diffDate_2.getDate());
 
    var diff = Math.abs(diffDate_2.getTime() - diffDate_1.getTime());
    diff = Math.ceil(diff / (1000 * 3600 * 24));
   
    return diff;
}
 

 





