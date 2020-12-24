ns("common") ;
var respParser = common.ajax.responseParser;
var selectedDateArr = new Array();
var channelCnt = 0;

//decode special characters
var map = {amp: '&', quot: '"', '#039': "'"};

$(document).ready(function() {
	//mobile menu
	$("#noData1").css("display", "none");
	$("#noData2").css("display", "none");
	$("#noData3").css("display", "none");
	$(".mMenu").click(function() {
		$(".nav").toggleClass("mNavOn");
		$(this).toggleClass("mMenuClose");
	});
	
	//조회버튼 click
	$(".btnSearchTop").on("click", function() {
		//기간 validation
		//디바이스 타입 validation		
		if (selectedDateArr[0] == null || selectedDateArr[1] == null) {
			var selectDate = $.i18n.prop('statistics.select.date');
			alert(selectDate);
			return false;
		}
				
		if ($("#deviceTypeList").val() == "") {
			var selectType = $.i18n.prop('statistics.select.type');
			alert(selectType);
			return false;
		}
		
		var startDateValue = $("#searchStartDate").val();
		var endDateValue = $("#searchEndDate").val();
		
		var diff = dateDiff(startDateValue,endDateValue);
		
		if(diff > 31) {
			alert($.i18n.prop('alarm.list.alert'));
			return;
		}
		
		//전체통계
		allStats();
		
		//그룹별통계
		groupStats();
		
		//디바이스별 통계
		deviceStats();
	});
	
	//전체 통계 다운로드 버튼 click
	$("#allStatsDownload").on("click", function() {
		/*  let file = new Blob([$('#allStatsList').html()], {type:"application/vnd.ms-excel"});
		  let url = URL.createObjectURL(file);
		  let a = $("<a />", {
		    href: url,
		    download: "filename.xls"}).appendTo("body").get(0).click();
		    e.preventDefault();*/
		
		if (selectedDateArr[0] == null || selectedDateArr[1] == null) {
			var noDateSelection = $.i18n.prop('common.search.norangeselect');
			alert(noDateSelection);
			return false;
		}
		
		var allDown = $.i18n.prop('statistics.download.all');
		// alert(allDown);
						
		/*$("#devicetypecd").val($("#deviceTypeList").val());		
		$("#startdate").prop("value", selectedDateArr[0]);
		$("#enddate").prop("value", selectedDateArr[1]);
		
		document.downForm.action = "/statis/entire/excel/list";
		document.downForm.submit();*/
		
		downloadCsv("/statis/entire/excel/list");
		
	});
	
	//그룹별 통계 다운로드 버튼 click
	$("#groupStatsDownload").on("click", function() {
		var groupDown = $.i18n.prop('statistics.download.group');
		if (selectedDateArr[0] == null || selectedDateArr[1] == null) {
			var noDateSelection = $.i18n.prop('common.search.norangeselect');
			alert(noDateSelection);
			return false;
		}
						
		/*$("#devicetypecd").val($("#deviceTypeList").val());
		$("#startdate").prop("value", selectedDateArr[0]);
		$("#enddate").prop("value", selectedDateArr[1]);
		document.downForm.action = "/statis/group/excel/list";
		document.downForm.submit();*/
		
		downloadCsv("/statis/group/excel/list");
	});
	
	//디바이스별 통계 다운로드 버튼 click
	$("#deviceStatsDownload").on("click", function() {
	    var deviceDown = $.i18n.prop('statistics.download.device');
		 
	    if (selectedDateArr[0] == null || selectedDateArr[1] == null) {
			var noDateSelection = $.i18n.prop('common.search.norangeselect');
	    	alert(noDateSelection);
	     	return false;
	    }			
	     	    				
	   /* $("#devicetypecd").val($("#deviceTypeList").val());
	    $("#startdate").prop("value", selectedDateArr[0]);
	    $("#enddate").prop("value", selectedDateArr[1]);
	    document.downForm.action = "/statis/device/excel/list";
	    document.downForm.submit();	*/    
	    
	    downloadCsv("/statis/device/excel/list");
	    
	});
	
	init();
	
	$("#ui-datepicker-div").attr("class","ui-datepicker ui-widget ui-picker-widget-content ui-helper-clearfix ui-corner-all");
	
	applicationCheck();
})

// 아이폰일때 다운로드 숨김
function applicationCheck() {
	if (typeof webkit !== "undefined") { 
		$("#allStatsDownload").hide();
		$("#groupStatsDownload").hide();
		$("#deviceStatsDownload").hide();
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
    selectedDateArr[1] = endDate + " 23:59:00";
    
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
    selectedDateArr[0] = startDate+ " 00:00:00";

    // 종료일은 시작일 이전 날짜 선택하지 못하도록 비활성화
    $("#searchEndDate").datepicker( "option", "minDate", startDate );

    // 시작일은 종료일 이후 날짜 선택하지 못하도록 비활성화
    $("#searchStartDate").datepicker( "option", "maxDate", endDate );
}

function downloadCsv(url_input) {
	
	
	if (typeof appWebview === "undefined" && typeof webkit === "undefined") {
		// 피씨
		$("#devicetypecd").val($("#deviceTypeList").val());
	    $("#startdate").prop("value", selectedDateArr[0]);
	    $("#enddate").prop("value", selectedDateArr[1]);
		$("#localecode").val($.cookie("localecd"));
		
		document.downForm.action = url_input;
		document.downForm.submit();

	} else if (typeof webkit !== "undefined") {
		//alert("아이폰");
		var url = url_input + "?";
		
		url += "devicetypecd=" + $("#deviceTypeList").val();
		url += "&startdate=" + selectedDateArr[0];
		url += "&enddate=" + selectedDateArr[1];
		url += "&localecode=" + $.cookie("localecd");
		
		location.href = url;
	} else {
		//alert("안드로이드");
		var url = url_input + "?";
		
		url += "devicetypecd=" + $("#deviceTypeList").val();
		url += "&startdate=" + selectedDateArr[0];
		url += "&enddate=" + selectedDateArr[1];
		url += "&localecode=" + $.cookie("localecd");
		
		$("#fileifr").attr("src", url);
	}
}

function init() {
	//datepicker 초기설정
	setInitDate();
	
	//디바이스타입 리스트
	getDeviceTypeList();
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
	
	//datepicker
	/*flatpickr('#date', {
		enableTime: true,
		mode: 'range',
		defaultDate: [start, end],
		onChange: function(selectedDates, dateStr, instance) {
			var html = "<em>" + dateStr + "</em><i></i>";
			$("#date").html(html);
			selectedDateArr = dateStr.split(" to ");
		},
	});
	$("#date").html(html);*/
}

//디바이스타입 리스트
function getDeviceTypeList() {
	var url = "/openapi/device/type/list";
	var errorComment = $.i18n.prop('common.error');
	
	$.ajax({
		url : url,
		success : function(response) {
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			if (respObj.isSuccess()) {
				var searchType = $.i18n.prop('statistics.search.type');
				var data = respObj.getDataObj();
				var html = "";
				html += "<option value=\"\">"+ searchType +"</option>";
				$.each(data, function(key, value) {
					html += "<option value=\"" + value.devicetypecd + "\">" + value.devicetypename+ "</option>";
				});
				//list 화면의 타입리스트
				$("#deviceTypeList").append(html);
			} else {
				alert(returnCode +" "+ errorComment);
			}
		},
		error : function () {
			alert(errorComment);
		}  
	});
}

//전체통계
function allStats() {
	var url = "/openapi/statis/entire/datalist";
	var devicetypecd = $("#deviceTypeList").val();
	var errorComment = $.i18n.prop('common.error');
	var parameter = { 
		startdate : selectedDateArr[0],
		enddate : selectedDateArr[1],
		devicetypecd : devicetypecd
	};
	
	$.ajax({
		url : url,
		data : parameter,
		success : function(response) {
			//$("#allStatsList").empty();
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			if (respObj.isSuccess()) {
				var data = respObj.getDataObj();
				//리스트 초기화 (첫번째 ul 제외)
				$("#allStatsList ul:not(:first)").remove();
				var html = "";
				$.each(data, function(key, value) {
					//console.log("key " + key + ", value = " + JSON.stringify(value));
					html += "<ul class=\"listTableBody\">"
						+ "<li><span class=\"statsName\">" + $.i18n.prop("channel.code_"+ value.devicetypecd +"_" + value.channelid) + "</li>"
						+ "<li>" + roundToTwo(value.avgVal) + "</li>"
						+ "<li>" + roundToTwo(value.minVal) + "</li>"
						+ "<li>" + roundToTwo(value.maxVal) + "</li>"
						+ "<li>" + roundToTwo(value.calcVal) + "</li>"
						+ "<li>" + roundToTwo(value.stdVal) + "</li>"
						+ "</ul>";
					
				});

				// 데이터가 있을경우와 없을경우 구분
				if (response.data.length > 0) {
					$("#allStatsList").show();
					$("#noData1").hide();
					$("#groupStatsList").show();
					$("#noData2").hide();
					$("#deviceStatsList").show();
					$("#noData3").hide();
					$("#groupChart").show();
					$("#deviceChart").show();
				} else {
					$("#allStatsList").hide();
					$("#noData1").show();
					$("#groupStatsList").hide();
					$("#noData2").show();
					$("#deviceStatsList").hide();
					$("#noData3").show();
					$("#groupChart").hide();
					$("#deviceChart").hide();
				}

				$("#allStatsList").append(html);
			} else {
				alert(returnCode +" "+ errorComment);
			}
		},
		error : function () {
			alert(errorComment);
		}  
	});
}

//그룹별통계
function groupStats() {
	var url = "/openapi/statis/group/datalist";
	var devicetypecd = $("#deviceTypeList").val();
	var errorComment = $.i18n.prop('common.error');
	var parameter = { 
		startdate : selectedDateArr[0],
		enddate : selectedDateArr[1],
		devicetypecd : devicetypecd
	};
	
	$.ajax({
		url : url,
		data : parameter,
		success : function(response) {
			$("#groupChart").empty();
			//$("#groupStatsList").empty();
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			if (respObj.isSuccess()) {
				var data = respObj.getDataObj();
				var cnt = response.cnt;
				//리스트 초기화 (첫번째 ul 제외)
				$("#groupStatsList ul:not(:first)").remove();
				var html = "";
				var chartHtml = "";
				var i=0;
				var j=1;

				//console.log(data);
				
				var avgArr = new Array();
				var maxminArr = new Array();
				$.each(data, function(key, value) {
					if (i !== 0) {
						if (i%cnt == 0) {
							j++;
						}
					}
					//채널 개수만큼
					if (i < cnt) {
						var avg = $.i18n.prop('common.avg');
						chartHtml += "<div class=\"subTitleSmallH4\">"
								 + "<h4>-"
								 + avg
								 + "(" + $.i18n.prop("channel.code_"+ value.devicetypecd +"_" + value.channelid) + ")</h4>"
								 + "</div>"
								 + "<div id=\"groupAvgChart_" + i + "\" class=\"statsGraph\"></div>"
								 + "<div class=\"subTitleSmallH4\">"
								 + "<h4>-max-min(" + $.i18n.prop("channel.code_"+ value.devicetypecd +"_" + value.channelid) + ")</h4>"
								 + "</div>"
								 + "<div id=\"groupMaxminChart_" + i + "\" class=\"statsGraph\"></div>";
						
						avgArr[i] = new Array();
						maxminArr[i] = new Array();
						avgArr[i%cnt][0] = ["그룹", $.i18n.prop("channel.code_"+ value.devicetypecd +"_" + value.channelid)];
						maxminArr[i%cnt][0] = ["그룹", $.i18n.prop("channel.code_"+ value.devicetypecd +"_" + value.channelid) +"_MIN", $.i18n.prop("channel.code_"+ value.devicetypecd +"_" + value.channelid) +"_MAX"];
					}
					
					var groupname = value.groupname.replace(/&($[^;]+);/g, function (m, c) { return map[c]; })
													.replace(/&#40;/g,"(")
								                    .replace(/&#41;/g,")")
								                    .replace(/&#35;/g , "#")
								                    .replace(/&amp;/g , "&");
					
					avgArr[i%cnt][j] = [groupname, roundToTwo(value.avgVal)];
					maxminArr[i%cnt][j] = [groupname, value.minVal, value.maxVal];
					
					html += "<ul class=\"listTableBody\">"
						+ "<li><span class=\"statsName\">" + groupname + "</li>"
						+ "<li>" + $.i18n.prop("channel.code_"+ value.devicetypecd +"_" + value.channelid) + "</li>"
						+ "<li>" + roundToTwo(value.avgVal) + "</li>"
						+ "<li>" + roundToTwo(value.minVal) + "</li>"
						+ "<li>" + roundToTwo(value.maxVal) + "</li>"
						+ "<li>" + roundToTwo(value.calcVal) + "</li>"
						+ "<li>" + roundToTwo(value.stdVal) + "</li>"
						+ "</ul>";
					i++;
				});
				$("#groupChart").append(chartHtml);
				$("#groupStatsList").append(html);
				
				for (var i=0; i<cnt; i++) {
					drawChart(JSON.stringify(avgArr[i]), "groupAvgChart_" + i, "avg");
					drawChart(JSON.stringify(maxminArr[i]), "groupMaxminChart_" + i, "maxmin");
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

//디바이스별통계
function deviceStats() {
	var url = "/openapi/statis/sensor/datalist";
	var devicetypecd = $("#deviceTypeList").val();
	var errorComment = $.i18n.prop('common.error');
	var parameter = { 
		startdate : selectedDateArr[0],
		enddate : selectedDateArr[1],
		devicetypecd : devicetypecd
	};

	$.ajax({
		url : url,
		data : parameter,
		success : function(response) {
			$("#deviceChart").empty();
			//$("#deviceStatsList").empty();
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			if (respObj.isSuccess()) {
				var data = respObj.getDataObj();
				var cnt = response.cnt;
				//리스트 초기화 (첫번째 ul 제외)
				$("#deviceStatsList ul:not(:first)").remove();
				var html = "";
				var chartHtml = "";
				var i=0;
				var j=1;
				
				var avgArr = new Array();
				var maxminArr = new Array();
				$.each(data, function(key, value) {
					/*console.log("key " + key + ", value = " + JSON.stringify(value));*/
					if (i !== 0) {
						if (i%cnt == 0) {
							j++;
						}
					}
					//채널 개수만큼
					if (i < cnt) {
						var avg = $.i18n.prop('common.avg');
						chartHtml += "<div class=\"subTitleSmallH4\">"
								 + "<h4>-"
								 + avg
								 + "(" + $.i18n.prop("channel.code_"+ value.devicetypecd +"_" + value.channelid) + ")</h4>"
								 + "</div>"
								 + "<div id=\"deviceAvgChart_" + i + "\" class=\"statsGraph\"></div>"
								 + "<div class=\"subTitleSmallH4\">"
								 + "<h4>-max-min(" + $.i18n.prop("channel.code_"+ value.devicetypecd +"_" + value.channelid) + ")</h4>"
								 + "</div>"
								 + "<div id=\"deviceMaxminChart_" + i + "\" class=\"statsGraph\"></div>";
						
						avgArr[i] = new Array();
						maxminArr[i] = new Array();
						
						
						avgArr[i%cnt][0] = ["디바이스", $.i18n.prop("channel.code_"+ value.devicetypecd +"_" + value.channelid)];
						maxminArr[i%cnt][0] = ["디바이스", $.i18n.prop("channel.code_"+ value.devicetypecd +"_" + value.channelid) +"_MIN", $.i18n.prop("channel.code_"+ value.devicetypecd +"_" + value.channelid) +"_MAX"];
					}
					avgArr[i%cnt][j] = [value.devicename, roundToTwo(value.avgVal)];
					maxminArr[i%cnt][j] = [value.devicename, value.minVal, value.maxVal];
					
					html += "<ul class=\"listTableBody\">"
						+ "<li><span class=\"statsName\">" + value.devicename + "</li>"
						+ "<li>" + value.groupname + "</li>"
						+ "<li>" + $.i18n.prop("channel.code_"+ value.devicetypecd +"_" + value.channelid) + "</li>"
						+ "<li>" + roundToTwo(value.avgVal) + "</li>"
						+ "<li>" + roundToTwo(value.minVal) + "</li>"
						+ "<li>" + roundToTwo(value.maxVal) + "</li>"
						+ "<li>" + roundToTwo(value.calcVal) + "</li>"
						+ "<li>" + roundToTwo(value.stdVal) + "</li>"
						+ "</ul>";
					i++;
				});
				$("#deviceChart").append(chartHtml);
				$("#deviceStatsList").append(html);
				
				for (var i=0; i<cnt; i++) {
					drawChart(JSON.stringify(avgArr[i]), "deviceAvgChart_" + i, "avg");
					drawChart(JSON.stringify(maxminArr[i]), "deviceMaxminChart_" + i, "maxmin");
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

//필요한 경우에만 소수점 2자리에서 반올림
function roundToTwo(num) {    
  return +(Math.round(num + "e+2")  + "e-2");
}

function drawChart(chartData, divId, flag) {
	if (chartData == undefined) {
		return;
	}
	
	google.charts.load('current', {packages: ['corechart', 'bar']});
	google.charts.setOnLoadCallback(drawBasic);
	
	function drawBasic() {
		var data =  google.visualization.arrayToDataTable($.parseJSON(chartData));
		
		var avgOptions = {
			chartArea: {width: '70%'},
			hAxis: {
				minValue:10,
				textStyle: {
					color: "white"
				}
			},
			vAxis: {
				textStyle: {
					color: "white"
				}
			},
			legend: {
				textStyle: {
					color: "white",
					fontSize: 11
				}
			},
			backgroundColor: '#014386',
			colors: ["#ffffff"],
			bar: {groupWidth: "20%"}
		};
		
		var maxminOptions = {
			chartArea: {width: '70%'},
			//colors: ['#ffab91', '#b0120a'],
			hAxis: {
				minValue:10,
				textStyle: {
					color: "white"
				}
			},
			vAxis: {
				textStyle: {
					color: "white"
				}
			},
			legend: {
				textStyle: {
					color: "white",
					fontSize: 11
				}
			},
			backgroundColor: '#014386',
			colors: ["#5ac1f9", "#f49696"],
			bar: {groupWidth: "40%"}
			
		};
		
		var view = new google.visualization.DataView(data);
		var chart = new google.visualization.BarChart(document.getElementById(divId));
		
		if (flag == "avg") {
			view.setColumns([0, 1, { calc: "stringify",
									 sourceColumn: 1,
									 type: "string",
									 role: "annotation" }]);
			chart.draw(view, avgOptions);
		} else {
			view.setColumns([0, 1, { calc: "stringify",
									 sourceColumn: 1,
									 type: "string",
									 fontSize: 20,
									 role: "annotation" }, 
							2, { calc: "stringify",
								 sourceColumn: 2,
								 type: "string",
								 fontSize: 20,
								 role: "annotation" }]);
			chart.draw(view, maxminOptions);
		}
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
