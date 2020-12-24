ns("common") ;
var respParser = common.ajax.responseParser;
var selectedDateArr = new Array();
var channelCnt = 0;
var grid;
var gridDataArrLink1 = [];
var gridDataArrLink2 = [];
var gridDataArrLink3 = [];
/*var startdate;
var enddate;*/

//decode special characters
var map = {amp: '&', quot: '"', '#039': "'"};

jQuery.browser = {};

$(document).ready(function() {
	/*//mobile menu
	$("#noData1").css("display", "none");
	$("#noData2").css("display", "none");
	$("#noData3").css("display", "none");*/
	
	$("#loading-container").hide();
	
	$(".mMenu").click(function() {
		$(".nav").toggleClass("mNavOn");
		$(this).toggleClass("mMenuClose");
	});
	
	jQuery.browser.msie = false;
    jQuery.browser.version = 0;
    if (navigator.userAgent.match(/MSIE ([0-9]+)\./)) {
        jQuery.browser.msie = true;
        jQuery.browser.version = RegExp.$1;
    }
    
	resiezejqGridWidth('gridTable', 'tabs-2', $('#tabs-2').width());
	resiezejqGridWidth('gridTable', 'tabs-3', $('#tabs-3').width());
	
	$( "#tabs" ).tabs();
	$('.tabs-nav a:first').trigger('click'); // Default
	
	$('.tabs-nav a').on('click', function (event) {
	    event.preventDefault();
	    
	    $('.tab-active').removeClass('tab-active');
	    $(this).parent().addClass('tab-active');
	    $('.tabs-stage div').hide();
	    $($(this).attr('href')).show();
	});

	$(".btnDivTitleRightSmall").hide();
	$("#schDay_li").show();
	$("#schMonth_li").hide();
	$("#schYear_li").hide();
	
	//금속검출기 선택시 통계(일/월/년) 선택
	$("#dateTypeList").on("change", function() {
		
		var datetypecd = $("#dateTypeList option:selected").val();
		
		$("#schDay_li").hide();
		$("#schMonth_li").hide();
		$("#schYear_li").hide();
		
		if(datetypecd == "day") {
			$("#schDay_li").show();
		} else if(datetypecd == "month") {
			$("#schMonth_li").show();
			
			var date = new Date(); 
			var year = date.getFullYear(); 
			var month = new String(date.getMonth()+1); 
			var day = new String(date.getDate()); 
			
			var default_date = year + "-" + (month < 10 ? "0" + month : month);	
		
			$("#schMonth").val(default_date);
		} else if(datetypecd == "year") {
			$("#schYear_li").show();
		} 
		
	});	
	
	//조회버튼 click
	$(".btnSearchTop").on("click", function() {
		var deviceid_type_list = $("#deviceNameList option:selected").val();
		var deviceid_type = deviceid_type_list.split("_");
		var deviceid = deviceid_type[0];
		var devicetypecd = Number(deviceid_type[1]);
		var datetypecd = $("#dateTypeList").val();
		
		if(deviceid_type_list == "") {
			alert($.i18n.prop('statistics.devicename.selectalert'));
			return;
		}
		if(datetypecd == "") {
			alert($.i18n.prop('statistics.datetype.selectalert'));
			return;
		}
		
		$("#loading-container").show();
		
		parameter_stats(deviceid_type_list, deviceid, devicetypecd, datetypecd);
		
		grid_stats1(devicetypecd);
		grid_stats2(devicetypecd);
		grid_stats3(devicetypecd);
		
	});
	
	// Link1 다운로드 버튼 
	$("#link1_download").on("click", function() {
		
		var deviceid_type_list = $("#deviceNameList option:selected").val();
		var deviceid_type = deviceid_type_list.split("_");
		var devicetypecd = Number(deviceid_type[1]);
		
		var start_channelid;
		var end_channelid;
		
		if(devicetypecd == 1 || devicetypecd == 2 || devicetypecd == 3) {
			start_channelid = 1;
			end_channelid = null;
		} else if(devicetypecd == 4) {
			start_channelid = 1;
			end_channelid = 2;
		} else {
			alert("error");
			return;
		}
	
		downloadCsv(start_channelid,end_channelid);
	});
	
	// Link2 다운로드 버튼 click
	$("#link2_download").on("click", function() {
		
		var deviceid_type_list = $("#deviceNameList option:selected").val();
		var deviceid_type = deviceid_type_list.split("_");
		var devicetypecd = Number(deviceid_type[1]);
		
		var start_channelid;
		var end_channelid;
		
		if(devicetypecd == 1 || devicetypecd == 2 || devicetypecd == 3) {
			start_channelid = 2;
			end_channelid = null;
		} else if(devicetypecd == 4) {
			start_channelid = 3;
			end_channelid = 4;
		} else {
			alert("error");
			return;
		}
		
		downloadCsv(start_channelid,end_channelid);
	});
	
	// Link3 다운로드 버튼 click
	$("#link3_download").on("click", function() {
		
		var deviceid_type_list = $("#deviceNameList option:selected").val();
		var deviceid_type = deviceid_type_list.split("_");
		var devicetypecd = Number(deviceid_type[1]);
		
		var start_channelid;
		var end_channelid;
		
		if(devicetypecd == 1 || devicetypecd == 2 || devicetypecd == 3) {
			start_channelid = 3;
			end_channelid = null;
		} else if(devicetypecd == 4) {
			start_channelid = 5;
			end_channelid = 6;
		} else {
			alert("error");
			return;
		}
		
		downloadCsv(start_channelid,end_channelid);
	});
	
	init();
	
	$("#ui-datepicker-div").attr("class","ui-datepicker ui-widget ui-picker-widget-content ui-helper-clearfix ui-corner-all");
	
	applicationCheck();
	
})

//화면 리사이즈 할수 있는 기능
function resiezejqGridWidth(grid_id, div_id, width) {	 
	 $(window).bind('resize', function() {
		  $("#" + grid_id).setGridWidth($('#' + div_id).width(), true);
	 }).trigger('resize');	 
}

// 아이폰일때 다운로드 숨김
function applicationCheck() {
	if (typeof webkit !== "undefined") { 
		$("#link1_download").hide();
		$("#link2_download").hide();
		$("#link3_download").hide();
	}
}

// Search Date
jQuery.fn.schDay = function(){
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

function init() {
	//datepicker 초기설정
	setInitDate();
	
	deviceNameList();
	
}

function setInitDate() {
	var now = new Date();
	var year= now.getFullYear();
	var mon = (now.getMonth()+1)>9 ? ''+(now.getMonth()+1) : '0'+(now.getMonth()+1);
	var day = now.getDate()>9 ? ''+now.getDate() : '0'+now.getDate();
	var start = year + '-' + mon + '-' + day + " 00:00";

	selectedDateArr[0] = start; 
	
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
    $(".searchDate").schDay();        // searchDate
	
	$("#schDay").datepicker({
        showButtonPanel: true,
        dateFormat: "yy-mm-dd",
        changeYear: true,
        changeMonth: true,        
        onClose : function (selectedDates) {
        	
	        var eleId = $(this).attr("id");
	        var optionName = "";
	        
	        $("#"+eleId).datepicker( "option", optionName, selectedDateArr[0] );
	        $(".searchDate").find(".chkbox2").removeClass("on");
	        selectedDateArr[0] = $("#schDay").val() + " 00:00";
        }
    }).attr('readonly');
	
	$('#schDay').datepicker('setDate', start);
	
	$("#schMonth").monthPicker();
	
}

function deviceNameList() {
	
	var url = "/openapi/device/name/list";
	
	var parameter = {
		
	}
	
	$.ajax({
		url : url,
		type : 'post',
		data : parameter,
		success : function(response) {
		
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			
			if (respObj.isSuccess()) {
				
				var html = "";
				var data = respObj.getDataObj();
				
				$.each(data, function(key, value) {
					html += "<option value=\"" + value.deviceid + "_" + value.devicetypecd + "\">" + value.devicename + "</option>";
				});
				
				$("#deviceNameList").append(html);
				
			}
			
		},
		error : function () {
			alert("error");
		}  
	});
}

function parameter_stats(deviceid_type_list, deviceid, devicetypecd, datetypecd) {
	
	var arraySize;
	var startdate = "";
	var enddate = "";
	var todayDate = new Date();
	
	var year = todayDate.getFullYear();
	var month = todayDate.getMonth()+1;
	var day = todayDate.getDate();
	var hour = todayDate.getHours();
	
	var today = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);	
	
	/*if(datetypecd == "day") {
		arraySize = 24;
		startdate = $("#schDay").val() + " 00:00:00";
		enddate = $("#schDay").val() + " " + (hour-1) + ":59:59";
	} else if(datetypecd == "month") {
		arraySize = 31; 
		startdate = $("#schMonth").val() + "-01 00:00:00";
		enddate = $("#schMonth").val() + "-31 " + (hour-1) + ":59:59";
	} else if(datetypecd == "year") {
		arraySize = 12;
		startdate = $("#schYear").val() + "-01-01 00:00:00";
		enddate = $("#schYear").val() + "-12-31 " + (hour-1) + ":59:59";
	}*/ 
	
	if(datetypecd == "day") {
		arraySize = 24;
		startdate = $("#schDay").val() + " 00:00:00";
		enddate = $("#schDay").val() + " 23:59:59";
	} else if(datetypecd == "month") {
		arraySize = 31; 
		startdate = $("#schMonth").val() + "-01 00:00:00";
		enddate = $("#schMonth").val() + "-31 23:59:59";
	} else if(datetypecd == "year") {
		arraySize = 12;
		startdate = $("#schYear").val() + "-01-01 00:00:00";
		enddate = $("#schYear").val() + "-12-31 23:59:59";
	}
	
	$("#chart1").empty();
	$("#chart2").empty();
	$("#chart3").empty();
	
	var html1 = "";
	var html2 = "";
	var html3 = "";
	
	html1 += "<div id=\"chart1\">";
	html2 += "<div id=\"chart2\">";
	html3 += "<div id=\"chart3\">";
	
	$("#chart1_container").append(html1);
	$("#chart2_container").append(html2);
	$("#chart3_container").append(html3);
	
	$("#grid1").remove();
	$("#grid2").remove();
	$("#grid3").remove();
	
	var htmlGrid1 = "<div id=\"grid1\">";
	var htmlGrid2 = "<div id=\"grid2\">";
	var htmlGrid3 = "<div id=\"grid3\">";
	
	$("#grid1_container").append(htmlGrid1);
	$("#grid2_container").append(htmlGrid2);
	$("#grid3_container").append(htmlGrid3);
	
	$("#grid1_container").hide();
	$("#grid2_container").hide();
	$("#grid3_container").hide();
	
	if(devicetypecd == 1 || devicetypecd == 2 || devicetypecd == 3) {
		T10_statsList(deviceid,startdate,enddate,datetypecd,arraySize);
		T10_grid_dataLink1(deviceid,startdate,enddate,datetypecd);
		T10_grid_dataLink2(deviceid,startdate,enddate,datetypecd);
		T10_grid_dataLink3(deviceid,startdate,enddate,datetypecd);
	} else if(devicetypecd == 4) {
		H10_statsList(deviceid,startdate,enddate,datetypecd,arraySize);
		H10_grid_dataLink1(deviceid,startdate,enddate,datetypecd);
		H10_grid_dataLink2(deviceid,startdate,enddate,datetypecd);
		H10_grid_dataLink3(deviceid,startdate,enddate,datetypecd);
	}
	
	$(".btnDivTitleRightSmall").show();
	
	setTimeout(function() {
		$("#grid1_container").show();
		$("#grid2_container").show();
		$("#grid3_container").show();
	},500);
	
}




function T10_statsList(deviceid,startdate,enddate,datetypecd,arraySize) {

	var url = "/openapi/statis/T10_statisticList";
	
	var errorComment = $.i18n.prop('common.error');
	
	var parameter = {
		deviceid: deviceid,
		startdate: startdate,
		enddate: enddate,
		datetypecd: datetypecd,
		arraySize: arraySize
	}
	
	$.ajax({
		url : url,
		data : parameter,
		success : function(response) {
			
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			if (respObj.isSuccess()) {
				var data = respObj.getDataObj();

				if(data == "null") {
					$("#grid1").hide();
					$("#grid2").hide();
					$("#grid3").hide();
					$(".btnDivTitleRightSmall").hide();
					$("#noData1").show();
					$("#noData2").show();
					$("#noData3").show();
					$("#loading-container").hide();
					return;
				}
				$("#noData1").hide();
				$("#noData2").hide();
				$("#noData3").hide();
				$("#loading-container").hide();
				
				var channelid1_min = data.channel1.min1;
				var channelid1_avg = data.channel1.avg1;
				var channelid1_max = data.channel1.max1;
				
				var channelid2_min = data.channel2.min2;
				var channelid2_avg = data.channel2.avg2;
				var channelid2_max = data.channel2.max2;
				
				var channelid3_min = data.channel3.min3;
				var channelid3_avg = data.channel3.avg3;
				var channelid3_max = data.channel3.max3;
				
				T10drawChart1(datetypecd,channelid1_min,channelid1_avg,channelid1_max);
				T10drawChart2(datetypecd,channelid2_min,channelid2_avg,channelid2_max);
				T10drawChart3(datetypecd,channelid3_min,channelid3_avg,channelid3_max);
				
			} else {
				alert(returnCode +" "+ errorComment);
			}
		},
		error : function () {
			alert("err");
		}  
	});
}




function H10_statsList(deviceid,startdate,enddate,datetypecd,arraySize) {

	var url = "/openapi/statis/H10_statisticList";
	
	var errorComment = $.i18n.prop('common.error');
	
	var parameter = {
		deviceid: deviceid,
		startdate: startdate,
		enddate: enddate,
		datetypecd: datetypecd,
		arraySize: arraySize
	}
	
	$.ajax({
		url : url,
		data : parameter,
		success : function(response) {
			
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			if (respObj.isSuccess()) {
				var data = respObj.getDataObj();
				
				if(data == "null") {
					$("#grid1").hide();
					$("#grid2").hide();
					$("#grid3").hide();
					$(".btnDivTitleRightSmall").hide();
					$("#noData1").show();
					$("#noData2").show();
					$("#noData3").show();
					$("#loading-container").hide();
					return;
				}
				
				$("#noData1").hide();
				$("#noData2").hide();
				$("#noData3").hide();
				$("#loading-container").hide();
				
				var channelid1_min = data.channel1.min1;
				var channelid1_avg = data.channel1.avg1;
				var channelid1_max = data.channel1.max1;
				
				var channelid2_min = data.channel2.min2;
				var channelid2_avg = data.channel2.avg2;
				var channelid2_max = data.channel2.max2;
				
				var channelid3_min = data.channel3.min3;
				var channelid3_avg = data.channel3.avg3;
				var channelid3_max = data.channel3.max3;
				
				var channelid4_min = data.channel4.min4;
				var channelid4_avg = data.channel4.avg4;
				var channelid4_max = data.channel4.max4;
				
				var channelid5_min = data.channel5.min5;
				var channelid5_avg = data.channel5.avg5;
				var channelid5_max = data.channel5.max5;
				
				var channelid6_min = data.channel6.min6;
				var channelid6_avg = data.channel6.avg6;
				var channelid6_max = data.channel6.max6;
				
				H10drawChart1(datetypecd,channelid1_min,channelid1_avg,channelid1_max,channelid2_min,channelid2_avg,channelid2_max);
				H10drawChart2(datetypecd,channelid3_min,channelid3_avg,channelid3_max,channelid4_min,channelid4_avg,channelid4_max);
				H10drawChart3(datetypecd,channelid5_min,channelid5_avg,channelid5_max,channelid6_min,channelid6_avg,channelid6_max);
				
			} else {
				alert(returnCode +" "+ errorComment);
			}
		},
		error : function () {
			alert("err");
		}  
	});
}














function T10drawChart1(datetypecd,channelid1_min,channelid1_avg,channelid1_max) {

	var temp = $.i18n.prop('statistics.temp');
	var temp_avg = $.i18n.prop('statistics.temp.avg');
	var temp_max = $.i18n.prop('statistics.temp.max');
	var temp_min = $.i18n.prop('statistics.temp.min');
	
	var statis_hour = $.i18n.prop('statistics.hour');
	var statis_day = $.i18n.prop('statistics.day');
	var statis_month = $.i18n.prop('statistics.month');

	var chartWidth = "";
	var windowWidth = $(window).width();
	var divWidth = $(".contents").width();

	if(windowWidth > 1279) gridWidth = '1098'; 
	else if(windowWidth > 300 && windowWidth < 500) gridWidth = divWidth;
	else if(windowWidth > 700 && windowWidth <= 1279) gridWidth = divWidth;
	
	Highcharts.Pointer.prototype.reset = function() {
		  return undefined;
		};
			
	var xCategories = [];
	
	if(datetypecd == "day") {
		xCategories = 
			['00'+statis_hour,'01'+statis_hour,'02'+statis_hour,'03'+statis_hour,'04'+statis_hour,'05'+statis_hour,'06'+statis_hour,
				'07'+statis_hour,'08'+statis_hour,'09'+statis_hour,'10'+statis_hour,'11'+statis_hour,'12'+statis_hour,'13'+statis_hour,
				   '14'+statis_hour,'15'+statis_hour,'16'+statis_hour,'17'+statis_hour,'18'+statis_hour,'19'+statis_hour,'20'+statis_hour,
				      '21'+statis_hour,'22'+statis_hour,'23'+statis_hour];
	} else if(datetypecd == "month") {
		xCategories = 
			['1'+statis_day,'2'+statis_day,'3'+statis_day,'4'+statis_day,'5'+statis_day,'6'+statis_day,'7'+statis_day,'8'+statis_day,
				'9'+statis_day,'10'+statis_day,'11'+statis_day,'12'+statis_day,'13'+statis_day,'14'+statis_day,'15'+statis_day,'16'+statis_day,
				  '17'+statis_day,'18'+statis_day,'19'+statis_day,'20'+statis_day,'21'+statis_day,'22'+statis_day,'23'+statis_day,'24'+statis_day,
				     '25'+statis_day,'26'+statis_day,'27'+statis_day,'28'+statis_day,'29'+statis_day,'30'+statis_day,'31'+statis_day];
	} else if(datetypecd == "year") {
		xCategories = 
			['1'+statis_month, '2'+statis_month, '3'+statis_month, '4'+statis_month, '5'+statis_month, '6'+statis_month, '7'+statis_month,
				'8'+statis_month, '9'+statis_month, '10'+statis_month, '11'+statis_month, '12'+statis_month];
	} 
	
  var ch1 = { name: temp_avg  , data: channelid1_avg, unit: "℃", type: "line" , color: "#C5ECFF" };
  var ch2 = { name: temp_max  , data: channelid1_max, unit: "℃", type: "line" , color: "#FF7C52" };
  var ch3 = { name: temp_min  , data: channelid1_min, unit: "℃", type: "line" , color: "#92A7AA" };
 
  var title = [temp];
  var yAxisTitle = temp + " ℃";
  
  var dataset = [ch1];
  var dataset1 = [ch2];
  var dataset2 = [ch3];
  
  $('<div class="chart" style="height: 25em">')
    .appendTo("#chart1")
    .highcharts({
      chart: {
        zoomType: 'x',
        width: gridWidth
      },
      title: {
        text: title[0],
        align: "center"
      },
      credits: {
        enabled: false
      },
      xAxis: {
        categories: xCategories
      },
      legend: {
    	  enabled: true,
	      symbolWidth: 30
	  },
	  yAxis: {
    	title: {
          text: yAxisTitle,
        }
	  },
      tooltip: { 
    	  shared: true,
    	  valueDecimals: 1 
      },
      exporting: {
        buttons: {
            contextButton: {
                enabled: false
            },
            exportButton: {
                text: 'Download',
                menuItems: ['downloadPNG','downloadJPEG','downloadPDF','downloadSVG','downloadCSV']
            },
            printButton: {
                text: 'Print',
                onclick: function () {
                    this.print();
                }
            }
        }
	  },
      series: [
        {
          data: dataset[0].data,
          name: dataset[0].name,
          type: dataset[0].type,
          color: dataset[0].color,
          tooltip: {
            valueSuffix: " " + dataset[0].unit
          }
        },
        {
          data: dataset1[0].data,
          name: dataset1[0].name,
          type: dataset1[0].type,
          color: dataset1[0].color,
          lineWidth: 1,
          dashStyle: 'dash',
          tooltip: {
            valueSuffix: " " + dataset1[0].unit
          },
          marker: {
        	  radius: 0
          }
        },
        {
          data: dataset2[0].data,
          name: dataset2[0].name,
          type: dataset2[0].type,
          color: dataset2[0].color,
          lineWidth: 1,
          dashStyle: 'dash',
          tooltip: {
            valueSuffix: " " + dataset2[0].unit
          },
          marker: {
        	  radius: 0
          }
        }
      ]
   });
	
}


function T10drawChart2(datetypecd,channelid2_min,channelid2_avg,channelid2_max) {

	var temp = $.i18n.prop('statistics.temp');
	var temp_avg = $.i18n.prop('statistics.temp.avg');
	var temp_max = $.i18n.prop('statistics.temp.max');
	var temp_min = $.i18n.prop('statistics.temp.min');
	
	var statis_hour = $.i18n.prop('statistics.hour');
	var statis_day = $.i18n.prop('statistics.day');
	var statis_month = $.i18n.prop('statistics.month');
	
	var chartWidth = "";
	var windowWidth = $(window).width();
	var divWidth = $(".contents").width();

	if(windowWidth > 1279) gridWidth = '1098'; 
	else if(windowWidth > 300 && windowWidth < 500) gridWidth = divWidth;
	else if(windowWidth > 700 && windowWidth <= 1279) gridWidth = divWidth;
	
	Highcharts.Pointer.prototype.reset = function() {
	  return undefined;
	};

	var xCategories = [];
	
	if(datetypecd == "day") {
		xCategories = 
			['00'+statis_hour,'01'+statis_hour,'02'+statis_hour,'03'+statis_hour,'04'+statis_hour,'05'+statis_hour,'06'+statis_hour,
				'07'+statis_hour,'08'+statis_hour,'09'+statis_hour,'10'+statis_hour,'11'+statis_hour,'12'+statis_hour,'13'+statis_hour,
				   '14'+statis_hour,'15'+statis_hour,'16'+statis_hour,'17'+statis_hour,'18'+statis_hour,'19'+statis_hour,'20'+statis_hour,
				      '21'+statis_hour,'22'+statis_hour,'23'+statis_hour];
	} else if(datetypecd == "month") {
		xCategories = 
			['1'+statis_day,'2'+statis_day,'3'+statis_day,'4'+statis_day,'5'+statis_day,'6'+statis_day,'7'+statis_day,'8'+statis_day,
				'9'+statis_day,'10'+statis_day,'11'+statis_day,'12'+statis_day,'13'+statis_day,'14'+statis_day,'15'+statis_day,'16'+statis_day,
				  '17'+statis_day,'18'+statis_day,'19'+statis_day,'20'+statis_day,'21'+statis_day,'22'+statis_day,'23'+statis_day,'24'+statis_day,
				     '25'+statis_day,'26'+statis_day,'27'+statis_day,'28'+statis_day,'29'+statis_day,'30'+statis_day,'31'+statis_day];
	} else if(datetypecd == "year") {
		xCategories = 
			['1'+statis_month, '2'+statis_month, '3'+statis_month, '4'+statis_month, '5'+statis_month, '6'+statis_month, '7'+statis_month,
				'8'+statis_month, '9'+statis_month, '10'+statis_month, '11'+statis_month, '12'+statis_month];
	} 
		
	
  var ch1 = { name: temp_avg  , data: channelid2_avg, unit: "℃", type: "line" , color: "#C5ECFF" };
  var ch2 = { name: temp_max  , data: channelid2_max, unit: "℃", type: "line" , color: "#FF7C52" };
  var ch3 = { name: temp_min  , data: channelid2_min, unit: "℃", type: "line" , color: "#92A7AA" };

  var title = [temp];
  var yAxisTitle = temp + " ℃";
  
  var dataset = [ch1];
  var dataset1 = [ch2];
  var dataset2 = [ch3];
  
  $('<div class="chart" style="height: 25em">')
    .appendTo("#chart2")
    .highcharts({
      chart: {
        zoomType: 'x',
        width: gridWidth
      },
      title: {
        text: title[0],
        align: "center"
      },
      credits: {
        enabled: false
      },
      xAxis: {
        crosshair: true,
        categories: xCategories
      },
      legend: {
    	enabled: true,
        symbolWidth: 30
      },
	  yAxis: {
    	title: {
           text: yAxisTitle,
        }
      },
      tooltip: { 
    	  shared: true,
    	  valueDecimals: 1 
      },
      exporting: {
        buttons: {
            contextButton: {
                enabled: false
            },
            exportButton: {
                text: 'Download',
                menuItems: ['downloadPNG','downloadJPEG','downloadPDF','downloadSVG','downloadCSV']
            },
            printButton: {
                text: 'Print',
                onclick: function () {
                    this.print();
                }
            }
        }
	  },
      series: [
        {
          data: dataset[0].data,
          name: dataset[0].name,
          type: dataset[0].type,
          color: dataset[0].color,
          tooltip: {
            valueSuffix: " " + dataset[0].unit
          }
        },
        {
          data: dataset1[0].data,
          name: dataset1[0].name,
          type: dataset1[0].type,
          color: dataset1[0].color,
          lineWidth: 1,
          dashStyle: 'dash',
          tooltip: {
            valueSuffix: " " + dataset[0].unit
          },
          marker: {
        	  radius: 0
          }
        },
        {
          data: dataset2[0].data,
          name: dataset2[0].name,
          type: dataset2[0].type,
          color: dataset2[0].color,
          lineWidth: 1,
          dashStyle: 'dash',
          tooltip: {
            valueSuffix: " " + dataset2[0].unit
          },
          marker: {
        	  radius: 0
          }
        }
      ]
   });
	
}

function T10drawChart3(datetypecd,channelid3_min,channelid3_avg,channelid3_max) {

	var temp = $.i18n.prop('statistics.temp');
	var temp_avg = $.i18n.prop('statistics.temp.avg');
	var temp_max = $.i18n.prop('statistics.temp.max');
	var temp_min = $.i18n.prop('statistics.temp.min');
	
	var statis_hour = $.i18n.prop('statistics.hour');
	var statis_day = $.i18n.prop('statistics.day');
	var statis_month = $.i18n.prop('statistics.month');
	
	var chartWidth = "";
	var windowWidth = $(window).width();
	var divWidth = $(".contents").width();

	if(windowWidth > 1279) gridWidth = '1098'; 
	else if(windowWidth > 300 && windowWidth < 500) gridWidth = divWidth;
	else if(windowWidth > 700 && windowWidth <= 1279) gridWidth = divWidth;
	
	Highcharts.Pointer.prototype.reset = function() {
	  return undefined;
	};

		
	var xCategories = [];
	
	if(datetypecd == "day") {
		xCategories = 
			['00'+statis_hour,'01'+statis_hour,'02'+statis_hour,'03'+statis_hour,'04'+statis_hour,'05'+statis_hour,'06'+statis_hour,
				'07'+statis_hour,'08'+statis_hour,'09'+statis_hour,'10'+statis_hour,'11'+statis_hour,'12'+statis_hour,'13'+statis_hour,
				   '14'+statis_hour,'15'+statis_hour,'16'+statis_hour,'17'+statis_hour,'18'+statis_hour,'19'+statis_hour,'20'+statis_hour,
				      '21'+statis_hour,'22'+statis_hour,'23'+statis_hour];
	} else if(datetypecd == "month") {
		xCategories = 
			['1'+statis_day,'2'+statis_day,'3'+statis_day,'4'+statis_day,'5'+statis_day,'6'+statis_day,'7'+statis_day,'8'+statis_day,
				'9'+statis_day,'10'+statis_day,'11'+statis_day,'12'+statis_day,'13'+statis_day,'14'+statis_day,'15'+statis_day,'16'+statis_day,
				  '17'+statis_day,'18'+statis_day,'19'+statis_day,'20'+statis_day,'21'+statis_day,'22'+statis_day,'23'+statis_day,'24'+statis_day,
				     '25'+statis_day,'26'+statis_day,'27'+statis_day,'28'+statis_day,'29'+statis_day,'30'+statis_day,'31'+statis_day];
	} else if(datetypecd == "year") {
		xCategories = 
			['1'+statis_month, '2'+statis_month, '3'+statis_month, '4'+statis_month, '5'+statis_month, '6'+statis_month, '7'+statis_month,
				'8'+statis_month, '9'+statis_month, '10'+statis_month, '11'+statis_month, '12'+statis_month];
	}
	
  var ch1 = { name: temp_avg  , data: channelid3_avg, unit: "℃", type: "line" , color: "#C5ECFF" };
  var ch2 = { name: temp_max  , data: channelid3_max, unit: "℃", type: "line" , color: "#FF7C52" };
  var ch3 = { name: temp_min  , data: channelid3_min, unit: "℃", type: "line" , color: "#92A7AA" };

  var title = [temp];
  var yAxisTitle = temp + " ℃";
  
  var dataset = [ch1];
  var dataset1 = [ch2];
  var dataset2 = [ch3];
  
  $('<div class="chart" style="height: 25em">')
    .appendTo("#chart3")
    .highcharts({
      chart: {
        zoomType: 'x',
        width: gridWidth
      },
      title: {
        text: title[0],
        align: "center"
        
      },
      credits: {
        enabled: false
      },
      xAxis: {
        crosshair: true,
        categories: xCategories
      },
      legend: {
    	  enabled: true,
	      symbolWidth: 30
	  },
      yAxis: {
    	title: {
          text: yAxisTitle,
        }
      },
      tooltip: { 
    	  shared: true,
    	  valueDecimals: 1 
      },
      exporting: {
        buttons: {
            contextButton: {
                enabled: false
            },
            exportButton: {
                text: 'Download',
                menuItems: ['downloadPNG','downloadJPEG','downloadPDF','downloadSVG','downloadCSV']
            },
            printButton: {
                text: 'Print',
                onclick: function () {
                    this.print();
                }
            }
        }
	  },
      series: [
        {
          data: dataset[0].data,
          name: dataset[0].name,
          type: dataset[0].type,
          color: dataset[0].color,
          tooltip: {
            valueSuffix: " " + dataset[0].unit
          }
        },
        {
          data: dataset1[0].data,
          name: dataset1[0].name,
          type: dataset1[0].type,
          color: dataset1[0].color,
          lineWidth: 1,
          dashStyle: 'dash',
          tooltip: {
            valueSuffix: " " + dataset[0].unit
          },
          marker: {
        	  radius: 0
          }
        },
        {
          data: dataset2[0].data,
          name: dataset2[0].name,
          type: dataset2[0].type,
          color: dataset2[0].color,
          lineWidth: 1,
          dashStyle: 'dash',
          tooltip: {
            valueSuffix: " " + dataset2[0].unit
          },
          marker: {
        	  radius: 0
          }
        }
      ]
   });
	
}

















function H10drawChart1(datetypecd,channelid1_min,channelid1_avg,channelid1_max,channelid2_min,channelid2_avg,channelid2_max) {

	var temp = $.i18n.prop('statistics.temp');
	var hum = $.i18n.prop('statistics.hum');
	var temp_avg = $.i18n.prop('statistics.temp.avg');
	var temp_max = $.i18n.prop('statistics.temp.max');
	var temp_min = $.i18n.prop('statistics.temp.min');
	var hum_avg = $.i18n.prop('statistics.hum.avg');
	var hum_max = $.i18n.prop('statistics.hum.max');
	var hum_min = $.i18n.prop('statistics.hum.min');
	
	var statis_hour = $.i18n.prop('statistics.hour');
	var statis_day = $.i18n.prop('statistics.day');
	var statis_month = $.i18n.prop('statistics.month');
	
	var chartWidth = "";
	var windowWidth = $(window).width();
	var divWidth = $(".contents").width();

	if(windowWidth > 1279) gridWidth = '1098'; 
	else if(windowWidth > 300 && windowWidth < 500) gridWidth = divWidth;
	else if(windowWidth > 700 && windowWidth <= 1279) gridWidth = divWidth;

	$("#chart1").bind("mousemove mouseleave", function(e) {
		  for (let i = 0; i < Highcharts.charts.length; ++i) {
		    let chart = Highcharts.charts[i];
		    let event = chart.pointer.normalize(e.originalEvent); // Find coordinates within the chart
		    let point;
		    for (let j = 0; j < chart.series.length && !point; ++j) {
		      point = chart.series[j].searchPoint(event, true);
		    }
		    if (!point) return;
		  
		    if (e.type === "mousemove") {
		      point.onMouseOver();
		      chart.xAxis[0].drawCrosshair(event, point);
		    } else {
		      point.onMouseOut();
		      chart.tooltip.hide(point);
		      chart.xAxis[0].hideCrosshair();
		    }
		  }
		});
	
	
	Highcharts.Pointer.prototype.reset = function() {
		  return undefined;
		};

		function syncExtremes(e) {
		  var thisChart = this.chart;

		  if (e.trigger !== "syncExtremes") {
		    // Prevent feedback loop
		    Highcharts.each(Highcharts.charts, function(chart) {
		      if (chart !== thisChart) {
		        if (chart.xAxis[0].setExtremes) {
		          // It is null while updating
		          chart.xAxis[0].setExtremes(e.min, e.max, undefined, false, {
		            trigger: "syncExtremes"
		          });
		        }
		      }
		    });
		  }
		}
		
	var xCategories = [];
	
	if(datetypecd == "day") {
		xCategories = 
			['00'+statis_hour,'01'+statis_hour,'02'+statis_hour,'03'+statis_hour,'04'+statis_hour,'05'+statis_hour,'06'+statis_hour,
				'07'+statis_hour,'08'+statis_hour,'09'+statis_hour,'10'+statis_hour,'11'+statis_hour,'12'+statis_hour,'13'+statis_hour,
				   '14'+statis_hour,'15'+statis_hour,'16'+statis_hour,'17'+statis_hour,'18'+statis_hour,'19'+statis_hour,'20'+statis_hour,
				      '21'+statis_hour,'22'+statis_hour,'23'+statis_hour];
	} else if(datetypecd == "month") {
		xCategories = 
			['1'+statis_day,'2'+statis_day,'3'+statis_day,'4'+statis_day,'5'+statis_day,'6'+statis_day,'7'+statis_day,'8'+statis_day,
				'9'+statis_day,'10'+statis_day,'11'+statis_day,'12'+statis_day,'13'+statis_day,'14'+statis_day,'15'+statis_day,'16'+statis_day,
				  '17'+statis_day,'18'+statis_day,'19'+statis_day,'20'+statis_day,'21'+statis_day,'22'+statis_day,'23'+statis_day,'24'+statis_day,
				     '25'+statis_day,'26'+statis_day,'27'+statis_day,'28'+statis_day,'29'+statis_day,'30'+statis_day,'31'+statis_day];
	} else if(datetypecd == "year") {
		xCategories = 
			['1'+statis_month, '2'+statis_month, '3'+statis_month, '4'+statis_month, '5'+statis_month, '6'+statis_month, '7'+statis_month,
				'8'+statis_month, '9'+statis_month, '10'+statis_month, '11'+statis_month, '12'+statis_month];
	}
		
	for(var i=0; i<2; i++) {
		
	  var ch1 = { name: temp_avg  , data: channelid1_avg, unit: "℃", type: "line" , color: "#C5ECFF" };
	  var ch2 = { name: hum_avg  , data: channelid2_avg, unit: "%", type: "line" , color: "#5966FF" };
		 
	  var ch3 = { name: temp_max  , data: channelid1_max, unit: "℃", type: "line" , color: "#FF7C52" };
	  var ch4 = { name: hum_max  , data: channelid2_max, unit: "%", type: "line" , color: "#FF7C52" };

	  var ch5 = { name: temp_min  , data: channelid1_min, unit: "℃", type: "line" , color: "#92A7AA" };
	  var ch6 = { name: hum_min  , data: channelid2_min, unit: "%", type: "line" , color: "#92A7AA" };
	 
	  var title = [temp,hum];
	  var yAxisTitle = [temp + " (℃)",hum + " (%)"];
	  
	  var dataset = [ch1 ,ch2];
	  var dataset1 = [ch3 ,ch4];
	  var dataset2 = [ch5 ,ch6];
	  
	  $('<div class="chart" style="height: 25em">')
	    .appendTo("#chart1")
	    .highcharts({
	      chart: {
	        zoomType: 'x',
	        width: gridWidth
	      },
	      title: {
	        text: title[i],
	        align: "center"
	      },
	      lang: {
	          noData: "Nichts zu anzeigen"
	      },
	      credits: {
	        enabled: false
	      },
	      xAxis: {
	        crosshair: true,
	        categories: xCategories
	      },
	      legend: {
	        enabled: true,
	        symbolWidth: 30
	      },
	      yAxis: {
	    	  title: {
		            text: yAxisTitle[i],
		        }
	      },
	      tooltip: { 
	    	  shared: true,
	    	  valueDecimals: 1 
	      },
	      exporting: {
		        buttons: {
		            contextButton: {
		                enabled: false,
		                symbol: 'square'
		            },
		            exportButton: {
		                text: 'Download',
		                menuItems: ['downloadPNG','downloadJPEG','downloadPDF','downloadSVG','downloadCSV']
		            },
		            printButton: {
		                text: 'Print',
		                onclick: function () {
		                    this.print();
		                }
		            }
		        }
		  },
	      series: [
	        {
	          data: dataset[i].data,
	          name: dataset[i].name,
	          type: dataset[i].type,
	          color: dataset[i].color,
	          tooltip: {
	            valueSuffix: " " + dataset[i].unit
	          }
	        },
	        {
	        	
	          data: dataset1[i].data,
	          name: dataset1[i].name,
	          type: dataset1[i].type,
	          color: dataset1[i].color,
	          lineWidth: 1,
	          dashStyle: 'dash',
	          tooltip: {
	            valueSuffix: " " + dataset1[i].unit
	          },
	          marker: {
                radius: 0
	          }
	        },
	        {
	          data: dataset2[i].data,
	          name: dataset2[i].name,
	          type: dataset2[i].type,
	          color: dataset2[i].color,
	          lineWidth: 1,
	          dashStyle: 'dash',
	          tooltip: {
	            valueSuffix: " " + dataset2[i].unit
	          },
	          marker: {
                radius: 0
	          }
	        }
	      ]
	   });
	}
	
}


function H10drawChart2(datetypecd,channelid3_min,channelid3_avg,channelid3_max,channelid4_min,channelid4_avg,channelid4_max) {

	var temp = $.i18n.prop('statistics.temp');
	var hum = $.i18n.prop('statistics.hum');
	var temp_avg = $.i18n.prop('statistics.temp.avg');
	var temp_max = $.i18n.prop('statistics.temp.max');
	var temp_min = $.i18n.prop('statistics.temp.min');
	var hum_avg = $.i18n.prop('statistics.hum.avg');
	var hum_max = $.i18n.prop('statistics.hum.max');
	var hum_min = $.i18n.prop('statistics.hum.min');
	
	var statis_hour = $.i18n.prop('statistics.hour');
	var statis_day = $.i18n.prop('statistics.day');
	var statis_month = $.i18n.prop('statistics.month');
	
	var chartWidth = "";
	var windowWidth = $(window).width();
	var divWidth = $(".contents").width();

	if(windowWidth > 1279) gridWidth = '1098'; 
	else if(windowWidth > 300 && windowWidth < 500) gridWidth = divWidth;
	else if(windowWidth > 700 && windowWidth <= 1279) gridWidth = divWidth;
	
	$("#chart2").bind("mousemove mouseleave", function(e) {
		  for (let i = 0; i < Highcharts.charts.length; ++i) {
		    let chart = Highcharts.charts[i];
		    let event = chart.pointer.normalize(e.originalEvent); // Find coordinates within the chart
		    let point;
		    for (let j = 0; j < chart.series.length && !point; ++j) {
		      point = chart.series[j].searchPoint(event, true);
		    }
		    if (!point) return;
		  
		    if (e.type === "mousemove") {
		      point.onMouseOver();
		      chart.xAxis[0].drawCrosshair(event, point);
		    } else {
		      point.onMouseOut();
		      chart.tooltip.hide(point);
		      chart.xAxis[0].hideCrosshair();
		    }
		  }
		});
	
	
	Highcharts.Pointer.prototype.reset = function() {
	  return undefined;
	};

	function syncExtremes(e) {
	  var thisChart = this.chart;

	  if (e.trigger !== "syncExtremes") {
	    // Prevent feedback loop
	    Highcharts.each(Highcharts.charts, function(chart) {
	      if (chart !== thisChart) {
	        if (chart.xAxis[0].setExtremes) {
	          // It is null while updating
	          chart.xAxis[0].setExtremes(e.min, e.max, undefined, false, {
	            trigger: "syncExtremes"
	          });
	        }
	      }
	    });
	  }
	}
	
	var xCategories = [];
	
	if(datetypecd == "day") {
		xCategories = 
			['00'+statis_hour,'01'+statis_hour,'02'+statis_hour,'03'+statis_hour,'04'+statis_hour,'05'+statis_hour,'06'+statis_hour,
				'07'+statis_hour,'08'+statis_hour,'09'+statis_hour,'10'+statis_hour,'11'+statis_hour,'12'+statis_hour,'13'+statis_hour,
				   '14'+statis_hour,'15'+statis_hour,'16'+statis_hour,'17'+statis_hour,'18'+statis_hour,'19'+statis_hour,'20'+statis_hour,
				      '21'+statis_hour,'22'+statis_hour,'23'+statis_hour];
	} else if(datetypecd == "month") {
		xCategories = 
			['1'+statis_day,'2'+statis_day,'3'+statis_day,'4'+statis_day,'5'+statis_day,'6'+statis_day,'7'+statis_day,'8'+statis_day,
				'9'+statis_day,'10'+statis_day,'11'+statis_day,'12'+statis_day,'13'+statis_day,'14'+statis_day,'15'+statis_day,'16'+statis_day,
				  '17'+statis_day,'18'+statis_day,'19'+statis_day,'20'+statis_day,'21'+statis_day,'22'+statis_day,'23'+statis_day,'24'+statis_day,
				     '25'+statis_day,'26'+statis_day,'27'+statis_day,'28'+statis_day,'29'+statis_day,'30'+statis_day,'31'+statis_day];
	} else if(datetypecd == "year") {
		xCategories = 
			['1'+statis_month, '2'+statis_month, '3'+statis_month, '4'+statis_month, '5'+statis_month, '6'+statis_month, '7'+statis_month,
				'8'+statis_month, '9'+statis_month, '10'+statis_month, '11'+statis_month, '12'+statis_month];
	}
		
	for(var i=0; i<2; i++) {
		
	  var ch1 = { name: temp_avg  , data: channelid3_avg, unit: "℃", type: "line" , color: "#C5ECFF" };
	  var ch2 = { name: hum_avg  , data: channelid4_avg, unit: "%", type: "line" , color: "#5966FF" };
		 
	  var ch3 = { name: temp_max  , data: channelid3_max, unit: "℃", type: "line" , color: "#FF7C52" };
	  var ch4 = { name: hum_max  , data: channelid4_max, unit: "%", type: "line" , color: "#FF7C52" };

	  var ch5 = { name: temp_min  , data: channelid3_min, unit: "℃", type: "line" , color: "#92A7AA" };
	  var ch6 = { name: hum_min  , data: channelid4_min, unit: "%", type: "line" , color: "#92A7AA" };
	 
	  var title = [temp,hum];
	  var yAxisTitle = [temp + " (℃)",hum + " (%)"];
	  
	  var dataset = [ch1 ,ch2];
	  var dataset1 = [ch3 ,ch4];
	  var dataset2 = [ch5 ,ch6];
	  
	  $('<div class="chart" style="height: 25em">')
	    .appendTo("#chart2")
	    .highcharts({
	      chart: {
	        zoomType: 'x',
	        width: gridWidth
	      },
	      title: {
	        text: title[i],
	        align: "center"
	      },
	      credits: {
	        enabled: false
	      },
	      xAxis: {
	        crosshair: true,
	        categories: xCategories
	      },
	      legend: {
		        enabled: true,
		        symbolWidth: 30,
		    },
	      yAxis: {
	    	  title: {
	            text: yAxisTitle[i],
	          }
	      },
	      tooltip: { 
	    	  shared: true,
	    	  valueDecimals: 1 
	      },
	      exporting: {
	    	  buttons: {
	            contextButton: {
	                enabled: false,
	                symbol: 'square'
	            },
	            exportButton: {
	                text: 'Download',
	                menuItems: ['downloadPNG','downloadJPEG','downloadPDF','downloadSVG','downloadCSV']
	            },
	            printButton: {
	                text: 'Print',
	                onclick: function () {
	                    this.print();
	                }
	            }
	        }
		  },
	      series: [
	        {
	          data: dataset[i].data,
	          name: dataset[i].name,
	          type: dataset[i].type,
	          color: dataset[i].color,
	          tooltip: {
	            valueSuffix: " " + dataset[i].unit
	          }
	        },
	        {
	          data: dataset1[i].data,
	          name: dataset1[i].name,
	          type: dataset1[i].type,
	          color: dataset1[i].color,
	          lineWidth: 1,
	          dashStyle: 'dash',
	          tooltip: {
	            valueSuffix: " " + dataset1[i].unit
	          },
	          marker: {
                radius: 0
	          }
	        },
	        {
	          data: dataset2[i].data,
	          name: dataset2[i].name,
	          type: dataset2[i].type,
	          color: dataset2[i].color,
	          lineWidth: 1,
	          dashStyle: 'dash',
	          tooltip: {
	            valueSuffix: " " + dataset2[i].unit
	          },
	          marker: {
                radius: 0
	          }
	        }
	      ]
	   });
	
	} 

}

function H10drawChart3(datetypecd,channelid5_min,channelid5_avg,channelid5_max,channelid6_min,channelid6_avg,channelid6_max) {

	var temp = $.i18n.prop('statistics.temp');
	var hum = $.i18n.prop('statistics.hum');
	var temp_avg = $.i18n.prop('statistics.temp.avg');
	var temp_max = $.i18n.prop('statistics.temp.max');
	var temp_min = $.i18n.prop('statistics.temp.min');
	var hum_avg = $.i18n.prop('statistics.hum.avg');
	var hum_max = $.i18n.prop('statistics.hum.max');
	var hum_min = $.i18n.prop('statistics.hum.min');
	
	var statis_hour = $.i18n.prop('statistics.hour');
	var statis_day = $.i18n.prop('statistics.day');
	var statis_month = $.i18n.prop('statistics.month');
	
	var chartWidth = "";
	var windowWidth = $(window).width();
	var divWidth = $(".contents").width();

	if(windowWidth > 1279) gridWidth = '1098'; 
	else if(windowWidth > 300 && windowWidth < 500) gridWidth = divWidth;
	else if(windowWidth > 700 && windowWidth <= 1279) gridWidth = divWidth;
	
	$("#chart3").bind("mousemove mouseleave", function(e) {
	  for (let i = 0; i < Highcharts.charts.length; ++i) {
	    let chart = Highcharts.charts[i];
	    let event = chart.pointer.normalize(e.originalEvent); // Find coordinates within the chart
	    let point;
	    for (let j = 0; j < chart.series.length && !point; ++j) {
	      point = chart.series[j].searchPoint(event, true);
	    }
	    if (!point) return;
	  
	    if (e.type === "mousemove") {
	      point.onMouseOver();
	      chart.xAxis[0].drawCrosshair(event, point);
	    } else {
	      point.onMouseOut();
	      chart.tooltip.hide(point);
	      chart.xAxis[0].hideCrosshair();
	    }
	  }
	});
	
	
	Highcharts.Pointer.prototype.reset = function() {
	  return undefined;
	};

	function syncExtremes(e) {
	  var thisChart = this.chart;

	  if (e.trigger !== "syncExtremes") {
	    // Prevent feedback loop
	    Highcharts.each(Highcharts.charts, function(chart) {
	      if (chart !== thisChart) {
	        if (chart.xAxis[0].setExtremes) {
	          // It is null while updating
	          chart.xAxis[0].setExtremes(e.min, e.max, undefined, false, {
	            trigger: "syncExtremes"
	          });
	        }
	      }
	    });
	  }
	}
		
	var xCategories = [];
	
	if(datetypecd == "day") {
		xCategories = 
			['00'+statis_hour,'01'+statis_hour,'02'+statis_hour,'03'+statis_hour,'04'+statis_hour,'05'+statis_hour,'06'+statis_hour,
				'07'+statis_hour,'08'+statis_hour,'09'+statis_hour,'10'+statis_hour,'11'+statis_hour,'12'+statis_hour,'13'+statis_hour,
				   '14'+statis_hour,'15'+statis_hour,'16'+statis_hour,'17'+statis_hour,'18'+statis_hour,'19'+statis_hour,'20'+statis_hour,
				      '21'+statis_hour,'22'+statis_hour,'23'+statis_hour];
	} else if(datetypecd == "month") {
		xCategories = 
			['1'+statis_day,'2'+statis_day,'3'+statis_day,'4'+statis_day,'5'+statis_day,'6'+statis_day,'7'+statis_day,'8'+statis_day,
				'9'+statis_day,'10'+statis_day,'11'+statis_day,'12'+statis_day,'13'+statis_day,'14'+statis_day,'15'+statis_day,'16'+statis_day,
				  '17'+statis_day,'18'+statis_day,'19'+statis_day,'20'+statis_day,'21'+statis_day,'22'+statis_day,'23'+statis_day,'24'+statis_day,
				     '25'+statis_day,'26'+statis_day,'27'+statis_day,'28'+statis_day,'29'+statis_day,'30'+statis_day,'31'+statis_day];
	} else if(datetypecd == "year") {
		xCategories = 
			['1'+statis_month, '2'+statis_month, '3'+statis_month, '4'+statis_month, '5'+statis_month, '6'+statis_month, '7'+statis_month,
				'8'+statis_month, '9'+statis_month, '10'+statis_month, '11'+statis_month, '12'+statis_month];
	}
		
		
	for(var i=0; i<2; i++) {
		
	  var ch1 = { name: temp_avg  , data: channelid5_avg, unit: "℃", type: "line" , color: "#C5ECFF" };
	  var ch2 = { name: hum_avg  , data: channelid6_avg, unit: "%", type: "line" , color: "#5966FF" };
		 
	  var ch3 = { name: temp_max  , data: channelid5_max, unit: "℃", type: "line" , color: "#FF7C52" };
	  var ch4 = { name: hum_max  , data: channelid6_max, unit: "%", type: "line" , color: "#FF7C52" };

	  var ch5 = { name: temp_min  , data: channelid5_min, unit: "℃", type: "line" , color: "#92A7AA" };
	  var ch6 = { name: hum_min  , data: channelid6_min, unit: "%", type: "line" , color: "#92A7AA" };
	 
	  var title = [temp,hum];
	  var yAxisTitle = [temp + " (℃)",hum + " (%)"];
	  
	  var dataset = [ch1 ,ch2];
	  var dataset1 = [ch3 ,ch4];
	  var dataset2 = [ch5 ,ch6];
	  
	  $('<div class="chart" style="height: 25em">')
	    .appendTo("#chart3")
	    .highcharts({
	      chart: {
	        zoomType: 'x',
	        width: gridWidth
	      },
	      title: {
	        text: title[i],
	        align: "center"
	      },
	      credits: {
	        enabled: false
	      },
	      xAxis: {
	        crosshair: true,
	        categories: xCategories
	      },
	      legend: {
	    	  enabled: true,
		      symbolWidth: 30
		  },
		  yAxis: {
	    	title: {
	          text: yAxisTitle[i],
	        }
		  },
	      tooltip: { 
	    	  shared: true,
	    	  valueDecimals: 1 
	      },
	      exporting: {
	        buttons: {
	            contextButton: {
	                enabled: false
	            },
	            exportButton: {
	                text: 'Download',
	                menuItems: ['downloadPNG','downloadJPEG','downloadPDF','downloadSVG','downloadCSV']
	            },
	            printButton: {
	                text: 'Print',
	                onclick: function () {
	                    this.print();
	                }
	            }
	        }
		  },
	      series: [
	        {
	          data: dataset[i].data,
	          name: dataset[i].name,
	          type: dataset[i].type,
	          color: dataset[i].color,
	          tooltip: {
	            valueSuffix: " " + dataset[i].unit
	          }
	        },
	        {
	          data: dataset1[i].data,
	          name: dataset1[i].name,
	          type: dataset1[i].type,
	          color: dataset1[i].color,
	          lineWidth: 1,
	          dashStyle: 'dash',
	          tooltip: {
	            valueSuffix: " " + dataset1[i].unit
	          },
	          marker: {
                radius: 0
	          }
	        },
	        {
	          data: dataset2[i].data,
	          name: dataset2[i].name,
	          type: dataset2[i].type,
	          color: dataset2[i].color,
	          lineWidth: 1,
	          dashStyle: 'dash',
	          tooltip: {
	            valueSuffix: " " + dataset2[i].unit
	          },
	          marker: {
                radius: 0
	          }
	        }
	      ]
	   });
	
	} 
}



function grid_stats1(devicetypecd) {
	
	var time = $.i18n.prop('common.list.date');
	var temp_avg = $.i18n.prop('statistics.temp.avg');
	var temp_max = $.i18n.prop('statistics.temp.max');
	var temp_min = $.i18n.prop('statistics.temp.min');
	var hum_avg = $.i18n.prop('statistics.hum.avg');
	var hum_max = $.i18n.prop('statistics.hum.max');
	var hum_min = $.i18n.prop('statistics.hum.min');

	//$("#grid1").empty();
	
	var windowWidth = $(window).width();
	var regdateWidth;
	
	if(windowWidth > 1279) {
		gridWidth = '1098'; 
		regdateWidth = gridWidth * 0.15;
	}
	else if(windowWidth > 300 && windowWidth < 500) {
		gridWidth = windowWidth;
		regdateWidth = gridWidth * 0.22;
	}
	else if(windowWidth > 700 && windowWidth <= 1279) {
		gridWidth = windowWidth;
		regdateWidth = gridWidth * 0.22;
	}
	
	var columns_type = "";
	
	if(devicetypecd == 1 || devicetypecd == 2 || devicetypecd == 3) {
		columns_type = [
			{
				header: time,
				name: 'regdateLink1',
				align: 'center',
				whiteSpace: 'normal',
				width: regdateWidth
			},
			{
				header: temp_avg,
				name: 'tempAvgLink1',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: temp_max,
				name: 'tempMaxLink1',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: temp_min,
				name: 'tempMinLink1',
				align: 'center',
				whiteSpace: 'normal'
			}
		]
	} else if(devicetypecd == 4) {
		columns_type = [
			{
				header: time,
				name: 'regdateLink1',
				align: 'center',
				whiteSpace: 'normal',
				width: regdateWidth
			},
			{
				header: temp_avg,
				name: 'tempAvgLink1',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: temp_max,
				name: 'tempMaxLink1',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: temp_min,
				name: 'tempMinLink1',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: hum_avg,
				name: 'humAvgLink1',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: hum_max,
				name: 'humMaxLink1',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: hum_min,
				name: 'humMinLink1',
				align: 'center',
				whiteSpace: 'normal'
			}
		]
	} 
	
	var grid = new tui.Grid({
		el: document.getElementById('grid1'),
		data : gridDataArrLink1,
		scrollX: true,
		scrollY: true,
		width : gridWidth,
		bodyHeight: 215, 
		rowHeaders: [ { type: 'rowNum'} ],
		columns: columns_type
		/*summary: {
			height: 40,
			position: 'bottom', // or 'top'
			align: 'center',
			columnContent: {
				classpath: {
					template: function(valueMap) {
						return `Max: ${valueMap.cnt}`;
					}
				}
			}
		}*/
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
	    	background: '#09212D',        // 208BE4
	    	emptySpace: '#09212D',        // #0B3F73
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





function grid_stats2(devicetypecd) {
	
	var time = $.i18n.prop('common.list.date');
	var temp_avg = $.i18n.prop('statistics.temp.avg');
	var temp_max = $.i18n.prop('statistics.temp.max');
	var temp_min = $.i18n.prop('statistics.temp.min');
	var hum_avg = $.i18n.prop('statistics.hum.avg');
	var hum_max = $.i18n.prop('statistics.hum.max');
	var hum_min = $.i18n.prop('statistics.hum.min');
	
	var windowWidth = $(window).width();
	var regdateWidth;
	
	if(windowWidth > 1279) {
		gridWidth = '1098'; 
		regdateWidth = gridWidth * 0.15;
	}
	else if(windowWidth > 300 && windowWidth < 500) {
		gridWidth = windowWidth;
		regdateWidth = gridWidth * 0.22;
	}
	else if(windowWidth > 700 && windowWidth <= 1279) {
		gridWidth = windowWidth;
		regdateWidth = gridWidth * 0.22;
	}
	
	var columns_type = "";
	
	if(devicetypecd == 1 || devicetypecd == 2 || devicetypecd == 3) {
		columns_type = [
			{
				header: time,
				name: 'regdateLink2',
				align: 'center',
				whiteSpace: 'normal',
				width: regdateWidth
			},
			{
				header: temp_avg,
				name: 'tempAvgLink2',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: temp_max,
				name: 'tempMaxLink2',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: temp_min,
				name: 'tempMinLink2',
				align: 'center',
				whiteSpace: 'normal'
			}
		]
	} else if(devicetypecd == 4) {
		columns_type = [
			{
				header: time,
				name: 'regdateLink2',
				align: 'center',
				whiteSpace: 'normal',
				width: regdateWidth
			},
			{
				header: temp_avg,
				name: 'tempAvgLink2',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: temp_max,
				name: 'tempMaxLink2',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: temp_min,
				name: 'tempMinLink2',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: hum_avg,
				name: 'humAvgLink2',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: hum_max,
				name: 'humMaxLink2',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: hum_min,
				name: 'humMinLink2',
				align: 'center',
				whiteSpace: 'normal'
			}
		]
	} 
	
	var grid = new tui.Grid({
		el: document.getElementById('grid2'),
		data : gridDataArrLink2,
		scrollX: true,
		scrollY: true,
		width : gridWidth,
		bodyHeight: 215, 
		rowHeaders: [ { type: 'rowNum'} ],
		columns: columns_type
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
	    	background: '#09212D',        // 208BE4
	    	emptySpace: '#09212D',        // #0B3F73
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




function grid_stats3(devicetypecd) {
	
	var time = $.i18n.prop('common.list.date');
	var temp_avg = $.i18n.prop('statistics.temp.avg');
	var temp_max = $.i18n.prop('statistics.temp.max');
	var temp_min = $.i18n.prop('statistics.temp.min');
	var hum_avg = $.i18n.prop('statistics.hum.avg');
	var hum_max = $.i18n.prop('statistics.hum.max');
	var hum_min = $.i18n.prop('statistics.hum.min');
	
	var windowWidth = $(window).width();
	var regdateWidth;
	
	if(windowWidth > 1279) {
		gridWidth = '1098'; 
		regdateWidth = gridWidth * 0.15;
	}
	else if(windowWidth > 300 && windowWidth < 500) {
		gridWidth = windowWidth;
		regdateWidth = gridWidth * 0.22;
	}
	else if(windowWidth > 700 && windowWidth <= 1279) {
		gridWidth = windowWidth;
		regdateWidth = gridWidth * 0.22;
	}
	
	var columns_type = "";
	
	if(devicetypecd == 1 || devicetypecd == 2 || devicetypecd == 3) {
		columns_type = [
			{
				header: time,
				name: 'regdateLink3',
				align: 'center',
				whiteSpace: 'normal',
				width: regdateWidth
			},
			{
				header: temp_avg,
				name: 'tempAvgLink3',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: temp_max,
				name: 'tempMaxLink3',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: temp_min,
				name: 'tempMinLink3',
				align: 'center',
				whiteSpace: 'normal'
			}
		]
	} else if(devicetypecd == 4) {
		columns_type = [
			{
				header: time,
				name: 'regdateLink3',
				align: 'center',
				whiteSpace: 'normal',
				width: regdateWidth
			},
			{
				header: temp_avg,
				name: 'tempAvgLink3',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: temp_max,
				name: 'tempMaxLink3',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: temp_min,
				name: 'tempMinLink3',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: hum_avg,
				name: 'humAvgLink3',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: hum_max,
				name: 'humMaxLink3',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: hum_min,
				name: 'humMinLink3',
				align: 'center',
				whiteSpace: 'normal'
			}
		]
	} 
	
	var grid = new tui.Grid({
		el: document.getElementById('grid3'),
		data : gridDataArrLink3,
		scrollX: true,
		scrollY: true,
		width : gridWidth,
		bodyHeight: 215, 
		rowHeaders: [ { type: 'rowNum'} ],
		columns: columns_type
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
	    	background: '#09212D',        // 208BE4
	    	emptySpace: '#09212D',        // #0B3F73
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














function H10_grid_dataLink1(deviceid,startdate,enddate,datetypecd) {

	var url = "/openapi/statis/H10_link1_grid_statisticList";
	
	var errorComment = $.i18n.prop('common.error');
	
	var parameter = {
		deviceid: deviceid,
		startdate: startdate,
		enddate: enddate,
		datetypecd: datetypecd,
		channelid_start: '1',
		channelid_end: '2'
	}
	
	$.ajax({
		url : url,
		data : parameter,
		async : false,
		type : 'post',
		success : function(response) {
			
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			if (respObj.isSuccess()) {
				var data = respObj.getDataObj();
				
				gridDataArrLink1 = data.link1;
				
			} else {
				alert(returnCode +" "+ errorComment);
			}
		},
		error : function () {
			alert("err");
		}  
	});
}


function H10_grid_dataLink2(deviceid,startdate,enddate,datetypecd) {

	var url = "/openapi/statis/H10_link2_grid_statisticList";
	
	var errorComment = $.i18n.prop('common.error');
	
	var parameter = {
		deviceid: deviceid,
		startdate: startdate,
		enddate: enddate,
		datetypecd: datetypecd,
		channelid_start: '3',
		channelid_end: '4'
	}
	
	$.ajax({
		url : url,
		data : parameter,
		async : false,
		type : 'post',
		success : function(response) {
			
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			if (respObj.isSuccess()) {
				var data = respObj.getDataObj();
				
				gridDataArrLink2 = data.link2;
				
			} else {
				alert(returnCode +" "+ errorComment);
			}
		},
		error : function () {
			alert("err");
		}  
	});
}



function H10_grid_dataLink3(deviceid,startdate,enddate,datetypecd) {

	var url = "/openapi/statis/H10_link3_grid_statisticList";
	
	var errorComment = $.i18n.prop('common.error');
	
	var parameter = {
		deviceid: deviceid,
		startdate: startdate,
		enddate: enddate,
		datetypecd: datetypecd,
		channelid_start: '5',
		channelid_end: '6'
	}
	
	$.ajax({
		url : url,
		data : parameter,
		async : false,
		type : 'post',
		success : function(response) {
			
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			if (respObj.isSuccess()) {
				var data = respObj.getDataObj();
				
				gridDataArrLink3 = data.link3;
				
			} else {
				alert(returnCode +" "+ errorComment);
			}
		},
		error : function () {
			alert("err");
		}  
	});
}













function T10_grid_dataLink1(deviceid,startdate,enddate,datetypecd) {

	var url = "/openapi/statis/T10_link1_grid_statisticList";
	
	var errorComment = $.i18n.prop('common.error');
	
	var parameter = {
		deviceid: deviceid,
		startdate: startdate,
		enddate: enddate,
		datetypecd: datetypecd,
		channelid_start: '1'
	}
	
	$.ajax({
		url : url,
		data : parameter,
		async : false,
		type : 'post',
		success : function(response) {
			
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			if (respObj.isSuccess()) {
				var data = respObj.getDataObj();

				gridDataArrLink1 = data.link1;
				
			} else {
				alert(returnCode +" "+ errorComment);
			}
		},
		error : function () {
			alert("err");
		}  
	});
}


function T10_grid_dataLink2(deviceid,startdate,enddate,datetypecd) {

	var url = "/openapi/statis/T10_link2_grid_statisticList";
	
	var errorComment = $.i18n.prop('common.error');
	
	var parameter = {
		deviceid: deviceid,
		startdate: startdate,
		enddate: enddate,
		datetypecd: datetypecd,
		channelid_start: '2'
	}
	
	$.ajax({
		url : url,
		data : parameter,
		async : false,
		type : 'post',
		success : function(response) {
			
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			if (respObj.isSuccess()) {
				var data = respObj.getDataObj();
				
				gridDataArrLink2 = data.link2;
				
			} else {
				alert(returnCode +" "+ errorComment);
			}
		},
		error : function () {
			alert("err");
		}  
	});
}

function T10_grid_dataLink3(deviceid,startdate,enddate,datetypecd) {

	var url = "/openapi/statis/T10_link3_grid_statisticList";
	
	var errorComment = $.i18n.prop('common.error');
	
	var parameter = {
		deviceid: deviceid,
		startdate: startdate,
		enddate: enddate,
		datetypecd: datetypecd,
		channelid_start: '3'
	}
	
	$.ajax({
		url : url,
		data : parameter,
		async : false,
		type : 'post',
		success : function(response) {
			
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			if (respObj.isSuccess()) {
				var data = respObj.getDataObj();
				
				gridDataArrLink3 = data.link3;
				
			} else {
				alert(returnCode +" "+ errorComment);
			}
		},
		error : function () {
			alert("err");
		}  
	});
}






function downloadCsv(start_channelid,end_channelid) {

	var deviceid_type_list = $("#deviceNameList option:selected").val();
	var deviceid_type = deviceid_type_list.split("_");
	var deviceid = deviceid_type[0];
	var devicetypecd = Number(deviceid_type[1]);
	var datetypecd = $("#dateTypeList").val();
	var localecode = $("#localecode").val($.cookie("localecd"));
	
	var arraySize;
	var startdate = "";
	var enddate = "";
	var today = new Date();
	var hour = today.getHours();
	
	if(datetypecd == "day") {
		arraySize = 24;
		startdate = $("#schDay").val() + " 00:00:00";
		enddate = $("#schDay").val() + " 23:59:59";
	} else if(datetypecd == "month") {
		arraySize = 31; 
		startdate = $("#schMonth").val() + "-01 00:00:00";
		enddate = $("#schMonth").val() + "-31 23:59:59";
	} else if(datetypecd == "year") {
		arraySize = 12;
		startdate = $("#schYear").val() + "-01-01 00:00:00";
		enddate = $("#schYear").val() + "-12-31 23:59:59";
	} 
	
	if (typeof appWebview === "undefined" && typeof webkit === "undefined") {
		// 피씨
		$("#down_deviceid").val(deviceid);
		$("#down_startdate").val(startdate);
		$("#down_enddate").val(enddate);
		$("#down_datetypecd").val(datetypecd);
		$("#down_devicetypecd").val(devicetypecd);
		$("#down_arraySize").val(arraySize);
		$("#down_localecode").val($.cookie("localecd"));
		$("#down_startchannelid").val(start_channelid);
		$("#down_endchannelid").val(end_channelid);
		
		document.downForm.action = "/statis/entire/excel/excel_list";
		document.downForm.submit();

	} else if (typeof webkit !== "undefined") {
		//alert("아이폰");
		/*var url = "/statis/past/excel/list?";
		url += "startdate=" + selectedDateArr[0];
		url += "&enddate=" + selectedDateArr[1];
		url += "&deviceid=" + deviceTypeArr[0];
		url += "&groupid=" + $("#deviceGroupList").val();
		url += "&devicename=" + deviceTypeArr[0];
		url += "&localecode=" + $.cookie("localecd");
		url += "&devicetypecd=" + deviceTypeArr[1];
		
		location.href = url;*/
	} else {
		//alert("안드로이드");
		var url = "/statis/entire/excel/excel_list?";
		url += "down_deviceid=" + deviceid;
		url += "&down_startdate=" + startdate;
		url += "&down_enddate=" + enddate;
		url += "&down_datetypecd=" + datetypecd;
		url += "&down_devicetypecd=" + devicetypecd;
		url += "&down_arraySize=" + arraySize;
		url += "&down_localecode=" + $.cookie("localecd");
		url += "&down_startchannelid=" + start_channelid;
		
		if(devicetypecd == 4) {
			url += "&down_endchannelid=" + end_channelid;
		} else { // T10일 경우 온도1만 조회하므로 down_endchannelid에 임시로 0 넣어줌
			url += "&down_endchannelid=" + 0;
		}
		
		$("#fileifr").attr("src", url);
	}
	
}






