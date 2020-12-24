ns("common") ;
var respParser = common.ajax.responseParser;
var selectedDateArr = new Array();
var excelColArr = new Array();
jQuery.browser = {};
var grid;

var pastDataArr = [];
var gridPastDataArr = [];

var firstPage = 1;
var visiblePage = 0; // 화면에 나타낼 페이지 수 
var perPage = 20; // 페이지당 데이터 갯수
var setTotalData = 0; // set당 전체 데이터 갯수
var setPage = 0;
var homeSeqno = 0;

var lastSet = false;

var lastIndex = 0;
var lastSeqno = 0;

function getPastData() {
	
	$("#loading-container").show();
	
	var url = "/openapi/past/pastData";
	var deviceid_type = $("#deviceList").val();
	var deviceidTypeArr = deviceid_type.split('_');
	
	var parameter = {
		startdate : selectedDateArr[0],
		enddate : selectedDateArr[1],
		deviceid : deviceidTypeArr[0]
	};

	var data;
	
	$.ajax({
		url : url,
		type : 'post',
		data : parameter,
		async: false,
		success : function(response) {
			
			data = response.data;
	
			setTimeout(function() {
				$("#loading-container").hide();
			},400);
			
			if(data == 0) {
				$("#pagination_container").hide();
				$("#noData1").show();
				$("#grid").hide();
				return;
			} else {
				$("#pagination_container").show();
				$("#noData1").hide();
				$("#grid").show();
			}
			
			homeSeqno = data[0].maxseqno;
			
		},
		error : function () {
			
			alert("error");
		}  
	});
	
	return data;
}

function getPastNextData(lastSeqno) {

	var url = "/openapi/past/pastNextData";
	var deviceid_type = $("#deviceList").val();
	var deviceidTypeArr = deviceid_type.split('_');
	
	var parameter = {
		startdate : selectedDateArr[0],
		enddate : selectedDateArr[1],
		deviceid : deviceidTypeArr[0],
		lastSeqno : lastSeqno
	};
	
	var data;
	
	$.ajax({
		url : url,
		type : 'post',
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

function getPastPreData(firstSeqno) {
	
	var url = "/openapi/past/pastPreData";
	var deviceid_type = $("#deviceList").val();
	var deviceidTypeArr = deviceid_type.split('_');
	
	var parameter = {
		startdate : selectedDateArr[0],
		enddate : selectedDateArr[1],
		deviceid : deviceidTypeArr[0],
		firstSeqno : firstSeqno
	};

	var data;
	
	$.ajax({
		url : url,
		type : 'post',
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

function getPastHomeData() {
	
	var url = "/openapi/past/pastHomeData";
	var deviceid_type = $("#deviceList").val();
	var deviceidTypeArr = deviceid_type.split('_');
	
	var parameter = {
		startdate : selectedDateArr[0],
		enddate : selectedDateArr[1],
		deviceid : deviceidTypeArr[0],
		homeSeqno : homeSeqno
	};
	
	var data;
	
	$.ajax({
		url : url,
		type : 'post',
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
		
		gridPageArr = pastDataArr.slice( perPage * (cnt - 1), perPage * cnt);
		
		grid.resetData(gridPageArr);

	});
}

function pageNext() {
	
	$("#loading-container").show();
	
	firstPage += 5;
	setPage++;
	
	lastIndex = (pastDataArr.length-1);
	lastSeqno = pastDataArr[lastIndex].minseqno;
	
	pastDataArr = getPastNextData(lastSeqno);
	
	var length = pastDataArr.length;
	
	setTotalData = pastDataArr.length;
	
	visiblePage = ( Math.ceil( setTotalData / perPage) );
	
	pagination_init(firstPage,perPage,length);
	
	$("#page_" + firstPage).css("background-color","#f56565");
	
	gridPageArr = pastDataArr.slice(0, perPage);
	
	grid.resetData(gridPageArr);
	
	
	if(length == 0) {
		lastSet = true;
	} else {
		lastSet = false;
	}
	
	setTimeout(function() {
		$("#loading-container").hide();
	},500);
	
}

function pagePre() {
	
	$("#loading-container").show();
	
	firstPage -= 5;
	setPage--;
	
	if(!lastSet) {
		var firstSeqno = pastDataArr[0].maxseqno;
		pastDataArr = getPastPreData(firstSeqno);
	} else {
		pastDataArr = getPastPreData(lastSeqno-1);
		lastSet = false;
	}

	var length = pastDataArr.length;
	
	setTotalData = pastDataArr.length;
	
	visiblePage = ( Math.ceil( setTotalData / perPage) );
	
	pagination_init(firstPage,perPage,length);
	
	$("#page_"+firstPage).css("background-color","#f56565");
	
	gridPageArr = pastDataArr.slice(0, perPage);
	
	grid.resetData(gridPageArr);
	
	setTimeout(function() {
		$("#loading-container").hide();
	},500);
}

function pageHome() {
	
	$("#loading-container").show();
	
	firstPage = 1;
	
	pastDataArr = getPastHomeData();
	
	var length = pastDataArr.length;
	
	setTotalData = pastDataArr.length;
	
	visiblePage = ( Math.ceil( setTotalData / perPage) );
	
	pagination_init(firstPage,perPage,length);
	
	$("#page_"+firstPage).css("background-color","#f56565");
	
	gridPageArr = pastDataArr.slice(0, perPage);
	
	grid.resetData(gridPageArr);
	
	setTimeout(function() {
		$("#loading-container").hide();
	},500);
}

$(document).ready(function() {	
	
	$("#loading-container").hide();
	
    jQuery.browser.msie = false;
    jQuery.browser.version = 0;
    if (navigator.userAgent.match(/MSIE ([0-9]+)\./)) {
        jQuery.browser.msie = true;
        jQuery.browser.version = RegExp.$1;
    }
	
	//mobile menu
	$(".mMenu").click(function() {
		$(".nav").toggleClass("mNavOn");
		$(this).toggleClass("mMenuClose");
	});
	
	//조회버튼 click
	$(".btnSearchTop").on("click", function() {
		getPastList();
	});
	
	//조회버튼 click
	$("#deviceGroupList").on("change", function() {
		getDeviceList($("#deviceGroupList").val());
	});
	
	//다운로드 버튼 click
	$("#download").on("click", function() {
		//JSONToCSVConvertor(JSON.stringify($('#gridTable').jqGrid('getGridParam','data')), selectedDateArr[0] + " to " + selectedDateArr[1], true)
		//JSONToCSVConvertor(JSON.stringify($('#gridTable').jqGrid('getRowData')), selectedDateArr[0] + " to " + selectedDateArr[1], true)
		downloadCsv();
	});
	
	init();
	
	$("#ui-datepicker-div").attr("class","ui-datepicker ui-widget ui-picker-widget-content ui-helper-clearfix ui-corner-all");
	
	
	applicationCheck();
})

//아이폰일때 다운로드 숨김
function applicationCheck() {
	if (typeof webkit !== "undefined") { 
		$("#download").hide();
	}
}

// Search Date
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
    selectedDateArr[1] = endDate + " 23:59";
    
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
    selectedDateArr[0] = startDate+ " 00:00";
    
    // 종료일은 시작일 이전 날짜 선택하지 못하도록 비활성화
    //$("#searchEndDate").datepicker( "option", "minDate", startDate);
    // 시작일은 종료일 이후 날짜 선택하지 못하도록 비활성화
    //$("#searchStartDate").datepicker( "option", "maxDate", endDate);
}

function init() {
	//datepicker 초기설정
	setInitDate();
	
	//디바이스그룹 리스트
	getGroupList();

	//디바이스 리스트
	getDeviceList();

}

function setInitDate() {
	var now = new Date();
	var year= now.getFullYear();
    var mon = (now.getMonth()+1)>9 ? ''+(now.getMonth()+1) : '0'+(now.getMonth()+1);
    var day = now.getDate()>9 ? ''+now.getDate() : '0'+now.getDate();
    var start = year + '-' + mon + '-' + day + " 00:00";
    var end = year + '-' + mon + '-' + day + " 23:59";
    //var html = "<em>" + start + " to " + end + "</em><i></i>";
    selectedDateArr[0] = start; 
	selectedDateArr[1] = end;
    
	// 쿠키 가져오기
	var language = getCookie("localecd");

      //datepicker 한국어로 사용하기 위한 언어설정
	if(language == "en") {
	 $.datepicker.setDefaults($.datepicker.regional['en']);
	} else if(language == "ja") {
	 $.datepicker.setDefaults($.datepicker.regional['ja']);
	} else if(language == "ko") {
	 $.datepicker.setDefaults($.datepicker.regional['ko']);
	}
  
    $(".dateclick").dateclick();    // DateClick
    $(".searchDate").schDate();        // searchDate
	
	$("#searchStartDate, #searchEndDate").datepicker({
        showButtonPanel: true,
        dateFormat: "yy-mm-dd",
        changeYear: true,
        changeMonth: true,        
        onClose : function (selectedDates) {
        	
	        var eleId = $(this).attr("id");
	        var optionName = "";
	        
	        $("#"+eleId).datepicker( "option", optionName, selectedDateArr[0] );
	        
	        $(".searchDate").find(".chkbox2").removeClass("on");
	        selectedDateArr[0] = $("#searchStartDate").val() + " 00:00";
	        selectedDateArr[1] = $("#searchEndDate").val()+ " 23:59";

        }
    }).attr('readonly', 'readonly');
	
	$('#searchStartDate').datepicker('setDate', start);
	$('#searchEndDate').datepicker('setDate', end);
	
}

function getDeviceList(groupid) {
	var url = "/openapi/device/list";
	var parameter = { groupid : groupid};
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
	    		var selectDevice = $.i18n.prop('past.device.select');
				var html = "";
				html += "<option value=\"\">"+ selectDevice +"</option>";
				$.each(data, function(key, value) {
					html += "<option value=\"" + value.deviceid +  "_" + value.devicetypename + "\">" + value.devicename + "</option>";
				});
				$("#deviceList").html(html);
				
				
				
			} else {
				alert(returnCode +" "+ errorComment);
			}
		},
		error : function () {
			alert(errorComment);
		}  
	});
}

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
	    		//var selectGroup = $.i18n.prop('past.group.select');
	    		var html = "";
	    		//html += "<option value=\"\">"+ selectGroup +"</option>";
				$.each(data, function(key, value) {
			        html += "<option value=\"" + value.groupid + "\">" + value.groupname + "</option>";
				});
				//list 화면의 그룹리스트
				$("#deviceGroupList").append(html);
				//Layer 화면의 그룹리스트
				$("#deviceUpdateGroupList").append(html);

				getDeviceList($("#deviceGroupList").val());
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
	    	
		},
		error : function () {
			alert(errorComment);
		}  
	});
}

function getPastReadData(deviceType) {

	var regdateHeader = $.i18n.prop('common.list.date');
	var channelHeader1 = $.i18n.prop('channel.code_4_1');
	var channelHeader2 = $.i18n.prop('channel.code_4_2');
	var channelHeader3 = $.i18n.prop('channel.code_4_3');
	var channelHeader4 = $.i18n.prop('channel.code_4_4');
	var channelHeader5 = $.i18n.prop('channel.code_4_5');
	var channelHeader6 = $.i18n.prop('channel.code_4_6');
	
	var windowWidth = $(window).width();
	var divWidth = $(".searchDate").width();
	var regdateWidth = windowWidth * 0.25;
	var nameWidth = windowWidth * 0.25;
	var alarmWidth = windowWidth * 0.5;
	
	var whiteSpace = "";
	var regdateWidth;
	
	if(windowWidth > 1279) {
		gridWidth = divWidth;
		regdateWidth = gridWidth * 0.15;
		whiteSpace = 'normal';
	}
	else if(windowWidth > 300 && windowWidth < 500) {
		gridWidth = windowWidth;
		regdateWidth = gridWidth * 0.22;
		whiteSpace = 'pre-wrap';
	}
	else if(windowWidth > 700 && windowWidth <= 1279) {
		gridWidth = windowWidth;
		regdateWidth = gridWidth * 0.22;
		whiteSpace = 'normal';
	}
	
	pastDataArr = getPastData();
	
	setTotalData = pastDataArr.length; // set당 전체 데이터 갯수
	visiblePage = ( Math.ceil( setTotalData / perPage) ); // 화면에 나타낼 페이지 수 
	
	gridPastDataArr = pastDataArr.slice(0,perPage);
	
	pagination_init(firstPage,perPage,setTotalData);
	
	
	$("#page_1").css("background-color","#f56565");
	
	var device_type = "";
	
	if(deviceType == "H10") {
		device_type = [
			{
				header: regdateHeader,
				name: 'regdate',
				align: 'center',
				whiteSpace: whiteSpace,
				width : regdateWidth
			},
			{
				header: channelHeader1,
				name: 'channel1',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: channelHeader2,
				name: 'channel2',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: channelHeader3,
				name: 'channel3',
				align: 'center',
				whiteSpace: 'pre-wrap'
			},
			{
				header: channelHeader4,
				name: 'channel4',
				align: 'center',
				whiteSpace: 'pre-wrap'
			},
			{
				header: channelHeader5,
				name: 'channel5',
				align: 'center',
				whiteSpace: 'pre-wrap'
			},
			{
				header: channelHeader6,
				name: 'channel6',
				align: 'center',
				whiteSpace: 'pre-wrap'
			}
		]
	} else {
		device_type = [
			{
				header: regdateHeader,
				name: 'regdate',
				align: 'center',
				whiteSpace: whiteSpace,
				width : regdateWidth
			},
			{
				header: channelHeader1,
				name: 'channel1',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: channelHeader3,
				name: 'channel2',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: channelHeader5,
				name: 'channel3',
				align: 'center',
				whiteSpace: 'pre-wrap'
			}
		]
	}
	
	grid = new tui.Grid({
		
		el: document.getElementById('grid'),
		data: gridPastDataArr,
		scrollX: true,
		scrollY: false,
		rowHeight : 'auto',
		rowHeaders: [ { type: 'rowNum'} ],
		pagination: true,
		columns: device_type
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
	
	
	//$("body").oLoader('hide');
}

function getPastList() {

	firstPage = 1;
	
	$("#grid").empty();
	$("#pagination_container").empty();
	
	var windowWidth = $(window).width();
	var phone = $(".ui-tabs .tabDash").width();
	var width;
	
	var groupid = $("#deviceGroupList").val();
	var deviceid_type = $("#deviceList").val();
	
	var deviceidTypeArr = deviceid_type.split('_');
	
	var errorComment = $.i18n.prop('common.error');

	var startDateValue = $("#searchStartDate").val();
	var endDateValue = $("#searchEndDate").val();
	
	var diff = dateDiff(startDateValue,endDateValue);
	
	if(diff > 31) {
		//$("body").oLoader('hide');
		alert($.i18n.prop('alarm.list.alert'));
		return;
	}
	
	//기간 validation
	if (selectedDateArr[0] == null || selectedDateArr[1] == null) {
		var selectPeriod = $.i18n.prop('select.period');
		alert(selectPeriod);
		$("#noData1").show();
		return false;
	}
	
	//그룹 validation
	if (groupid == "") {
		var selectGroup = $.i18n.prop('select.group');
		alert(selectGroup);
		$("#noData1").show();
		return false;
	}
	
	//디바이스 validation
	if (deviceid_type == "") {
		var selectDevice = $.i18n.prop('select.device');
		alert(selectDevice);
		$("#noData1").show();
		return false;
	}

	
	// 디바이스 과거데이터 읽기
	getPastReadData(deviceidTypeArr[1]);
	//$("#loading-container").hide();
}


function downloadCsv() {
	
	var devicename_type = $("#deviceList > option:selected").val()
	var deviceTypeArr = devicename_type.split("_");
	
	if (typeof appWebview === "undefined" && typeof webkit === "undefined") {
		// 피씨
		$("#startdate").val(selectedDateArr[0]);
		$("#enddate").val(selectedDateArr[1]);
		$("#deviceid").val(deviceTypeArr[0]);
		$("#groupid").val($("#deviceGroupList").val());
		$("#devicename").val(deviceTypeArr[0]);
		$("#devicetypecd").val(deviceTypeArr[1]);
		$("#localecode").val($.cookie("localecd"));
		
		document.downForm.action = "/statis/past/excel/list";
		document.downForm.submit();

	} else if (typeof webkit !== "undefined") {
		//alert("아이폰");
		var url = "/statis/past/excel/list?";
		url += "startdate=" + selectedDateArr[0];
		url += "&enddate=" + selectedDateArr[1];
		url += "&deviceid=" + deviceTypeArr[0];
		url += "&groupid=" + $("#deviceGroupList").val();
		url += "&devicename=" + deviceTypeArr[0];
		url += "&localecode=" + $.cookie("localecd");
		url += "&devicetypecd=" + deviceTypeArr[1];
		
		location.href = url;
	} else {
		//alert("안드로이드");
		var url = "/statis/past/excel/list?";
		url += "startdate=" + selectedDateArr[0];
		url += "&enddate=" + selectedDateArr[1];
		url += "&deviceid=" + deviceTypeArr[0];
		url += "&groupid=" + $("#deviceGroupList").val();
		url += "&devicename=" + deviceTypeArr[0];
		url += "&localecode=" + $.cookie("localecd");
		url += "&devicetypecd=" + deviceTypeArr[1];
		
		$("#fileifr").attr("src", url);
	}
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