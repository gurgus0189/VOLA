ns("common") ;
var respParser = common.ajax.responseParser;
var deviceid ;
var devicename ;
var groupname ;
var devicetypecode;
var iconpath;
var selectedDate = "";
var grid;
var chart = [];
var settingValue = [];

//decode special characters
var map = {amp: '&', quot: '"', '#039': "'"};

jQuery.browser = {};

$(document).ready(function() {
	jQuery.browser.msie = false;
    jQuery.browser.version = 0;
    if (navigator.userAgent.match(/MSIE ([0-9]+)\./)) {
        jQuery.browser.msie = true;
        jQuery.browser.version = RegExp.$1;
    }
     //그룹 네임 , devicename 
	makeDeviceName();
	//mobile menu
	$(".mMenu").click(function() {
		$(".nav").toggleClass("mNavOn");
		$(this).toggleClass("mMenuClose");
	});
	
	resiezejqGridWidth('gridTable', 'tabs-2', $('#tabs-2').width());
	$( "#tabs" ).tabs();
	
	$("#noData1").css("display", "none");
	
	$('.tabs-nav a').on('click', function (event) {
	    event.preventDefault();
	    
	    $('.tab-active').removeClass('tab-active');
	    $(this).parent().addClass('tab-active');
	    $('.tabs-stage div').hide();
	    $($(this).attr('href')).show();
	});

	$('.tabs-nav a:first').trigger('click'); // Default
	
	//init();
})

$(window).load(function() {
	
	$("#loading-container").hide();
	
	//datepicker 초기설정
	setInitDate();
	
	//WebDashBoardController 에서 가져온 deviceid (thymeleaf 사용) 
	deviceid = $("#deviceid").val();

	//실시간 정보 가져오기
	getRealTimeDeviceData(deviceid); 
	
	// 과거데이터 toast grid
	drawDashBoardDetailInfo();
	
	//차트 로드
	chartLoad();
	
	setTimeout(function() {
		getChartData();
	},50);
	
});

/*function init() {
	
}*/

function setInitDate() {
	var now = new Date();

    var year= now.getFullYear();
    var mon = (now.getMonth()+1) > 9 ? '' + (now.getMonth() + 1) : '0' + (now.getMonth() + 1);
    var day = now.getDate() > 9 ? '' + now.getDate() : '0' + now.getDate();
    var hours = now.getHours() > 9 ? '' + now.getHours() : '0' + now.getHours();
    var minutes = now.getMinutes() > 9 ? '' + now.getMinutes() : '0' + now.getMinutes();
    var end = year + '-' + mon + '-' + day + " "+ hours +":"+ minutes;
    var startday = (now.getDate()-1);
    var startd = startday > 9 ? '' + startday : '0' + startday;
    var start = year + '-' + mon + '-' + startd + " "+ hours +":"+ minutes;

    selectedDate = start + "," + end;
    
    return selectedDate;
    
}

function getRealTimeDeviceData(deviceid) {
	var errorComment = $.i18n.prop('common.error');
	
	$.ajax({
		type:"GET",
		url:"/openapi/device/getdata",
		data: {"deviceid": deviceid},
		dataType:"JSON", // 옵션이므로 JSON으로 받을게 아니면 안써도 됨
		success : function(data) {
			var obj = data.data;
			var returnCode = data.returnCode;
			
			makeRealTimeDevice(obj);
		},
		error : function(xhr, status, error) {
			alert(errorComment);
		}
	});
}

function makeData(data) {
	
	var dataArray = new Array();
	$.each(data, function(k, v) {
		if (k == 1) {
			dataArray[0] = v;
		} else if (k == 2) {
			dataArray[1] = v;
		} else if (k == 3) {
			dataArray[2] = v;
		}
	});
	// 채널3개중 1개 안보일때 사용됨 
	// ex) 온도1 : 20 , 온도2 : null , 온도3 : 20
	for (var i =0 ; i<dataArray.length; i++) {
		if (dataArray[i] == undefined){
			dataArray.splice(i,i);
		}
	}
	
	return dataArray;
}
function makeColor(channelColorArray) {
	if(channelColorArray==undefined){
	      code = 0;
		}
	
	var result = "";
	switch (channelColorArray) {
		case 0: 
			result="skyNum";
			//result = "";
			break; // black
		case 1: 
			result = "dangerNum"; // red
			break;
		case 2: 
			result = "dangerNum";
			break;
		case 3: 
			result = "cautionNum";
			break;
		case 4: 
			result = "cautionNum";
			break;
	}
	return result;
}


function makeDataKey(data) {
	var dataArray = new Array();
	$.each(data, function(k, v) {
		if (k == 1) {
			dataArray[0] = k;
		} else if (k == 2) {
			dataArray[1] = k;
		} else if (k == 3) {
			dataArray[2] = k;
		}
	});
	// 채널3개중 1개 안보일때 사용됨 
	// ex) 온도1 : 20 , 온도2 : null , 온도3 : 20
	for (var i =0 ; i<dataArray.length; i++) {
		if (dataArray[i] == undefined){
			dataArray.splice(i,i);
		}
	}
	return dataArray;
}

function getWifiClass(ri) {
	
	var ret = "icoWifi ";

	ri = parseInt(ri);
	
	if(ri == 999) {
		ret += "icoWifi00"; // 신호 x
		return ret;
	}
	
	if (ri < -80) {
		ret += "icoWifi01";   // 약 
		return ret;
	} else {
		ret += "icoWifi02";   // 강 
		return ret;
	}

	ret += "icoWifi02";
	return ret;
}

function makeRealTimeDeviceHtml(str) {
	//var obj = JSON.parse(str.toString().replace(/&([^;]+);/g, (m, c) => map[c]));
	
	var obj = JSON.parse(str.toString().replace(/&([^;]+);/g, function (m, c) { 
		return map[c];
	}));
	var html = "";
	var data = obj.dt; // 실시간 정보
	var channeldataArray = makeData(data); // 실시간 정보 배열로
	var channelname = obj.tp;
	var channelColor = obj.ar;
	var channelColorArray = makeData(channelColor);
	var channelid = makeDataKey(channelColor);
	var deviceTypecd = obj.tp;
	var channelName = new Array();
	var colorClass = new Array();
	
	for (var i = 0; i < channelColorArray.length; i++) {
		colorClass[i] = makeColor(channelColorArray[i]);
		
		var channel = "channel.code_" + deviceTypecd + "_" + channelid[i];
		var channelmeasure = "type.code_"+ deviceTypecd +"_"+ channelid[i];
		var	channelmeasureCode = $.i18n.prop(channelmeasure);
		
		channel = $.i18n.prop(channel);
		html += "\n<li><span class=\"roundBox01 "+ colorClass[i] +"\">" + channel + "</span><em class=\"temperatureNum\">"+ channeldataArray[i] +"</em><span class=\"temperatureC\">"+ channelmeasureCode +"</span></li>";
	}
	
	return html;
}
function setDeviceData(str) {
	var obj = JSON.parse(str.toString().replace(/&([^;]+);/g, function (m, c) { 
		return map[c];
	}));
	
	var deviceid = obj.id; // device id
	var data = obj.dt; // device data
	var devicetype = obj.tp; // device type
	var devicealarm = obj.ar; //devicealarm["채널아이디"]
	var timestamp = obj.ts * 1000; //timestamp
	var dt = new Date(timestamp);
	var month = dt.getMonth()+1;
	var day = dt.getDate();
	var year = dt.getFullYear();
	var hour = dt.getHours();
	var minute = dt.getMinutes();
	var second = dt.getSeconds();	
	
	var channelhtml = "";
	var channelname = "";
	var channelmeasure = "";
	var channelclass = "";
	var alarmcode = "";
	
	var alarmcodedevicename = getAlarmColorClass(0); //default
	
	for (var channelid in data) {
		var val = data[channelid]; // channelid 에 해당하는 데이터
		// channelid : channelid(예: 센서명)
		// value : value(예: 센서값)
		
		if($.isNumeric(val)) dashTemNum = val.toFixed(1); 
		else dashTemNum = val;
		
		alarmcode = devicealarm[channelid];
		channelclass = getAlarmColorClass(alarmcode);	
		// 변경예정(다국어 적용)
		channelname = "channel.code_" + devicetype + "_" + channelid;
		var channelnameCode = $.i18n.prop(channelname);

		channelmeasure = "type.code_"+ devicetype +"_"+ channelid;
		var	channelmeasureCode = $.i18n.prop(channelmeasure);
		
		if(obj.tp == 4) {
			channelhtml += "\n<li><span class=\"roundBox01 "+ channelclass +"\">" + channelnameCode + "</span><em class=\"temperatureNum_H\">"+ dashTemNum +"</em><span class=\"temperatureC_H\">"+ channelmeasureCode +"</span></li>";
		} else {
			channelhtml += "\n<li><span class=\"roundBox01 "+ channelclass +"\">" + channelnameCode + "</span><em class=\"temperatureNum_H\">"+ dashTemNum +"</em><span class=\"temperatureC_H\">"+ channelmeasureCode +"</span></li>";
		}
		
		
		//$("$.ui-jqgrid .ui-jqgrid-hbox")
		
		/* if (alarmcode == "1")
			alarmcodedevicename = channelclass; //red */
	}
		
	return channelhtml;
	
}

function getAlarmColorClass(code) {
	
	if(code==undefined){
	      code = 0;
		}
	
	var result = "";
	
	switch (code) {
		case 0: 
			result = "skyNum";  // 정상
			 //result = ""; 
			break; // black
		case 1: 
			result = "dangerNum"; // red  상한
			break;
		case 2: 
			result = "dangerNum";  // 하한
			//result = "blueNum"; 
			break;
		case 3: 
			result = "cautionNum"; // 근접 
			break;
		case 4: 
			result = "cautionNum";
			 //result = "skyNum"; 
			break;
		case 5: 
			result = "dangerNum"; // red  상한
			break;
		 default:		 
	}
	return result;
	 //return "black"; 
}
function makeRealTimeDeviceWifiHtml(str) {
	var obj = JSON.parse(str.toString().replace(/&([^;]+);/g, function (m, c) { 
		return map[c];
	}));
	
	var regdateTimeHtml = "";
	var timestamp = obj.ts * 1000; //timestamp
	var dt = new Date(timestamp);
	var month = dt.getMonth()+1;
	var day = dt.getDate();
	var year = dt.getFullYear();
	var hour = dt.getHours();
	var minute = dt.getMinutes();
	var second = dt.getSeconds();	
	var datetime = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + " " + 
		(hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second);
	regdateTimeHtml = "<time class=\"dashTime\">" + datetime + "</time>"

	var classwifi = getWifiClass(obj.ri); // 와이 파이 정보 
	var wifiHtml = "";
	if(classwifi == "icoWifi icoWifi03"){
		wifiHtml = "<span class=\"icoWifi icoWifi03\" title=\"아주좋음\">아주좋음</span>";
	} else if (classwifi == "icoWifi icoWifi02") {
		wifiHtml = "<span class=\"icoWifi icoWifi02\" title=\"아주종음\">아주종음</span>";
	} else if (classwifi == "icoWifi icoWifi01") {
		wifiHtml = "<span class=\"icoWifi icoWifi01\" title=\"보통\">보통</span>";
	} else if (classwifi == "icoWifi icoWifi01") {
		wifiHtml = "<span class=\"icoWifi icoWifi00\" title=\"끊김\">끊김</span>";
	}
	
	$("#dashDetailHeadR").append(regdateTimeHtml);
	$("#dashDetailHeadR").append(wifiHtml);
}

function makeRealTimeDevice(obj) {
	
	// dashDetailList
	var id = obj.id // 디바이스id
	
	//var DeviceHtml = makeRealTimeDeviceHtml(obj); // 실시간 정보 
	var DeviceHtml = setDeviceData(obj); // 실시간 정보 
	var dashDetailListId= "#dashDetailList";
	$(dashDetailListId).append(DeviceHtml);
	
	var DeviceWifi = makeRealTimeDeviceWifiHtml(obj); // 와이파이 세기 및 시간 정보 
}

//화면 리사이즈 할수 있는 기능
function resiezejqGridWidth(grid_id, div_id, width) {	 
	 $(window).bind('resize', function() {
		  $("#" + grid_id).setGridWidth($('#' + div_id).width(), true);
	 }).trigger('resize');	 
}

function makeDeviceName() {
	devicename = encodehtmlspecialchars($("#devicename").val());
	groupname = escapeHtml($("#groupname").val());
	iconpath = $("#iconpath").val();
	
	devicetypecode = $("#devicetypecd").val();
	
	var html = "";
	var temp = $.i18n.prop('common.temp');
	html += "\n<h2 class=\"dashDetailHead\"><i class=\"" + iconpath+"_detail" + "\">"+ temp +"</i><span>" + groupname + "</span><em>" + devicename + "</em></h2>" ;
	
	$("#dashDetailHeadR").before(html);
}


function getLinkSettingValue(deviceid,channelid) {
	
	var url = "/openapi/device/setting/get";
	var parameter = { 
			deviceid : deviceid
		};
	
	var settingArr = [];
	
	$.ajax({
		url : url,
		data : parameter,
		async : false,
		success : function(response) {
			var respObj = respParser.parse(response);
			
			if(respObj.isSuccess()) {
			
				var data = respObj.getDataObj();
				var map = {amp: '&', quot: '"', '#39': "'", '#40' : "(", '#41' : ")", lt: '<', gt: '>', '#35' : '#'};
				
				var val = response.data.channelrange[channelid-1];
				
				var channelrangedata = $.parseJSON(val.channelrangedata.replace(/&([^;]+);/g, function (m, c) { return map[c]; }));
				var channelrangedatadefult = $.parseJSON(val.channelrangedatadefult.replace(/&([^;]+);/g, function (m, c) { return map[c]; }));
				var channelrangedatauser; 
				
        		if(val.channelrangedatauser) {
        			channelrangedatauser = $.parseJSON(val.channelrangedatauser.replace(/&([^;]+);/g, function (m, c) { return map[c]; })); 
        		}
        		else {
        			channelrangedatauser = channelrangedatadefult;
        		}
        		
        		var	channelcode = "channel.code_" + data.devicetypecd + "_" + val.channelid;
        		channelcode = $.i18n.prop(channelcode);
        		
        		settingArr[0] = channelrangedatauser.MIN;
        		settingArr[1] = channelrangedatauser.MAX;
        		settingArr[2] = channelrangedata.MIN;
        		settingArr[3] = channelrangedata.MAX;
        		settingArr[4] = channelcode;
			
			}
		}
	});
	
	return settingArr;
	
}

/* 채널 아이디 가져오기 */
function getChannelid() {
	
	var url = "/openapi/past/dashboarddetaillist";
	
	var channelname;
	
	var parameter = {
		deviceid : deviceid,
	};
	
	$.ajax({
		type : "POST",
		url : url,
		data : parameter,
		async : false,
		success :function(result) {			
			
			if (result.returnCode != "200")
				return;
			
			channelname = result.data.channelname;
			
		}
	});
	
	return channelname;
	
}


//TODO 공통코드로 이전 필요
//Function to convert hex format to a rgb color
function rgb2hex(orig){
 var rgb = orig.replace(/\s/g,'').match(/^rgba?\((\d+),(\d+),(\d+)/i);
 return (rgb && rgb.length === 4) ? "#" +
  ("0" + parseInt(rgb[1],10).toString(16)).slice(-2) +
  ("0" + parseInt(rgb[2],10).toString(16)).slice(-2) +
  ("0" + parseInt(rgb[3],10).toString(16)).slice(-2) : orig;
}

function chartLoad() {
	
	$("#loading-container").show();
	
	channelname = getChannelid();
	
	var channelid = Object.keys(channelname);
	var length = Object.keys(channelname).length;
	
	for(var i=0; i<length; i++) {
		
		var html = "<br><div id=\"container_" + channelid[i] + "\" style=\"width:100%; height: 500px; border-top: 2px solid #3c7cbc;\"></div>";
		$("#tabs-1").append(html);
		
		settingValue = getLinkSettingValue(deviceid,(channelid[i]));
		
		var min = settingValue[0];
		var max = settingValue[1];
		var minRange = settingValue[2];
		var maxRange = settingValue[3];
		var channelcode = settingValue[4];
		
		// highchart 그리기
		createChart(i,channelid[i],channelname,min,max,minRange,maxRange,channelcode);     
		
	}
	
}

function createChart(index,channelid,channelname,min,max,minRange,maxRange,channelcode) {

	var upperLimit = $.i18n.prop('listview.upper.limit');
	var lowerLimit = $.i18n.prop('listview.lower.limit');

	var rangeSelector1H = $.i18n.prop('listview.rangeselector_1H');
	var rangeSelector4H = $.i18n.prop('listview.rangeselector_4H');
	var rangeSelector8H = $.i18n.prop('listview.rangeselector_8H');
	var rangeSelector16H = $.i18n.prop('listview.rangeselector_16H');
	var rangeSelectorAll = $.i18n.prop('listview.rangeselector_all');
	
	if (Highcharts.VMLRenderer) {
	    Highcharts.VMLRenderer.prototype.symbols.doublearrow = Highcharts.SVGRenderer.prototype.symbols.doublearrow;
	}
	
	// Create the chart
	chart[index] = Highcharts.stockChart("container_" + channelid, {
		 chart: {
            zoomType: 'x'
         },
         plotOptions: {
	        series: {
	            zones: [{
	                value: min, // Values up to 0 (not including) ...
	                color: '#329CD6', // ... have the color blue 
                	fillColor: {
                        linearGradient: {
                            x1:0, y1:0, x2:0, y2:1
                        },
                        stops: [
                        	[0, 'rgba(50,156,214,0.5)'],
                        	[1, 'rgba(89,255,255,0.1)']
                        ]
                    },
	            },{
	                value: max, // Values up to 10 (not including) ...
	            },{
	                color: '#FF2F3D', // Values from 10 (including) and up have the color red
	                fillColor: {
                        linearGradient: {
                            x1:0, y1:0, x2:0, y2:1
                        },
                        stops: [
                        	[0, 'rgba(255,47,61,0.5)'],
                            [1, 'rgba(254,165,0,0.1)']
                        ]
                    },
                    
	            }]
	        },
	     },
		
		time: {
	        timezone: 'Asia/seoul'
	    },
	    credits: {
	    	enabled: false
        },
	    title: {
	        text: channelcode
	    },
	    yAxis: {
	        title: {
	            text: channelcode,
	        },
	        
	        plotLines: [{
	            value: min,
	            color: '#329CD6',
	            dashStyle: 'shortdash',
	            width: 2,
	            label: {
	                text: lowerLimit,
	                style: {
		            	color: '#329CD6',
		            	fontWeight: 'bold',
		            }
	            },
	            
	        }, {
	            value: max,
	            color: '#FF2F3D',
	            dashStyle: 'shortdash',
	            width: 1,
	            label: {
	                text: upperLimit,
	                style: {
		            	color: '#FF2F3D',
		            	fontWeight: 'bold',
		            }
	            }
	        }],
	    },
	    rangeSelector: {
            buttons: [{
                type: 'hour',
                count: 1,
                text: rangeSelector1H
            }, {
                type: 'hour',
                count: 4,
                text: rangeSelector4H
            }, {
                type: 'hour',
                count: 8,
                text: rangeSelector8H
            }, {
                type: 'hour',
                count: 16,
                text: rangeSelector16H
            }, {
                type: 'all',
                text: rangeSelectorAll
            }],
            inputEnabled: false, // it supports only days
            selected: 4 // all
        },
        tooltip: {
        	borderWidth: 3,
	    	formatter: function () {
	           
	    		var date = new Date(this.x);
	    		var month = date.getMonth()+1;
	    		var day = date.getDate();
	    		var year = date.getFullYear();
	    		var hour = date.getHours();
	    		var minute = date.getMinutes();
	    		var second = date.getSeconds();	
	    		
	    		var datetime = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + " " + 
	    		(hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second);
	    		
	            return ['<b>' + datetime + '</b>'].concat(
	                this.points ?
	                    this.points.map(function (point) {
	                        return point.series.name + ': ' + point.y.toFixed(1);
	                    }) : []
	            );
	        },
	    },
	    legend: {
	        enabled: true,
	        symbolWidth: 40
	    },
	    
	    loading: {
	        labelStyle: {
	            width: '100px',
	            height: '100px',
	            fontSize: '5em',
	            opcity: '1.0',
	            backgroundColor: '#122835'
	        },
	        style: {
	        	backgroundColor: '#122835'
	        }
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
	    series : [{
	    	name: channelname[channelid],
	    	type: 'area',
	    	data: null,
	    	lineWidth: 2,
	    	color: '#92A7AA',
	    	fillColor: {
                linearGradient: {
                    x1:0, y1:0, x2:0, y2:1
                },
                stops: [
            		[0, 'rgba(146,167,170,0.5)'],
            		[1, 'rgba(255,255,255,0.1)']
                ]
            },
            threshold: null
	    }]
	});
	
	//chart[index].showLoading("loading");
	
}

function getChartData() {
	
	var url = "/openapi/graph/list";
	
	var nowDate = new Date(); //현재시간
	var month = nowDate.getMonth()+1;
	var day = nowDate.getDate();
	var year = nowDate.getFullYear();
	var hour = nowDate.getHours();
	var minute = nowDate.getMinutes();
	var second = nowDate.getSeconds();	
	
    var minDate = new Date();
    minDate.setTime(minDate.getTime() - (1 * 24 * 60 * 60 * 1000));
	
    var minmonth = minDate.getMonth()+1;
	var minday = minDate.getDate();
	var minyear = minDate.getFullYear();
	var minhour = minDate.getHours();
	var minminute = minDate.getMinutes();
	var minsecond = minDate.getSeconds();	
    
    var nowdatetime = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + " " + 
	(hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second);	
    
    var mindatetime = minyear + "-" + (minmonth < 10 ? "0" + minmonth : minmonth) + "-" + (minday < 10 ? "0" + minday : minday) + " " + 
	(hour < 10 ? "0" + minhour : minhour) + ":" + (minminute < 10 ? "0" + minminute : minminute) + ":" + (minsecond < 10 ? "0" + minsecond : minsecond);
	
	var channelids = "";
	
	for(var channelid in channelname) {
	
		channelids += "," + channelid;
	}
	
	if(channelids == "") {
		$("#noData1").css("display", "");
		$("#loading-container").hide();
		return;
	}
	
	var parameter = { 
		deviceid: deviceid,
		channelids: channelids,
		startdate: mindatetime,
		enddate: nowdatetime
	};
	
	var channelid = Object.keys(channelname);
	var length = Object.keys(channelname).length;
	
	$.ajax({
		url : url,
		data : parameter,
		async : false,
		success : function(result) {
			
			for(var i=0; i<length; i++) {
				
				chart[i].series[0].setData(result.data[i]);
				
				//chart[i].hideLoading();
			}
			

			$("#loading-container").hide();
			
			
		}
	});	
}

//toast Grid
function drawDashBoardDetailInfo() {
	
	var selectedDateArr = selectedDate.split(",");
	//기간 validation
	if (selectedDateArr[0] == null || selectedDateArr[1] == null) {
		var noSelection = $.i18n.prop('common.search.norangeselect');
		alert(noSelection);
		return false;
	}
	
	var time = setInitDate();
	var timesplit = time.split(",");

	var starttime = timesplit[0];  
	var endtime = timesplit[1];
		
	var parameter = {
		deviceid : deviceid,
		startdate : starttime,
		enddate : endtime
	};
	
	var regdateHeader = $.i18n.prop('common.list.date');
	var channelHeader1 = $.i18n.prop('channel.code_4_1');
	var channelHeader2 = $.i18n.prop('channel.code_4_2');
	var channelHeader3 = $.i18n.prop('channel.code_4_3');
	var channelHeader4 = $.i18n.prop('channel.code_4_4');
	var channelHeader5 = $.i18n.prop('channel.code_4_5');
	var channelHeader6 = $.i18n.prop('channel.code_4_6');
	
	var windowWidth = $(window).width();
	var divWidth = $(".dashDetailView").width();
	var regdate;
	var gridWidth;
	var widthSpace = "";
	
	if(windowWidth > 1279) {
		gridWidth = '1098'; 
		widthSpace = 'normal';
	}
	else if(windowWidth > 300 && windowWidth < 500) {
		gridWidth = windowWidth;
		regdate = gridWidth * 0.22;
		widthSpace = 'pre-wrap';
	}
	else if(windowWidth > 700 && windowWidth <= 1279) {
		gridWidth = windowWidth;
		widthSpace = 'pre-wrap';
	}
	
	devicetypecode = $("#devicetypecd").val();
	
	var columns_type = "";
	
	if(devicetypecode == 1 || devicetypecode == 2 || devicetypecode == 3) {
		
		columns_type = [
			{
				header: regdateHeader,
				name: 'regdate',
				align: 'center',
				whiteSpace: widthSpace,
				width : regdate
			},
			{
				header: channelHeader1,
				name: 'channel1',
				align: 'center',
				whiteSpace: 'pre-wrap'
			},
			{
				header: channelHeader3,
				name: 'channel2',
				align: 'center',
				whiteSpace: 'pre-wrap'
			},
			{
				header: channelHeader5,
				name: 'channel3',
				align: 'center',
				whiteSpace: 'pre-wrap'
			}
		]
	}
	
	else if(devicetypecode == 4) {
		
		columns_type = [
			{
				header: regdateHeader,
				name: 'regdate',
				align: 'center',
				whiteSpace: widthSpace,
				width : regdate
			},
			{
				header: channelHeader1,
				name: 'channel1',
				align: 'center',
				whiteSpace: 'pre-wrap'
			},
			{
				header: channelHeader2,
				name: 'channel2',
				align: 'center',
				whiteSpace: 'pre-wrap'
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
		
	}
	
	grid = new tui.Grid({
		
		el: document.getElementById('grid'),
		
		data: {
	      api: {
				readData: { url: '/openapi/past/dashBoardDetailGridlist', method: 'GET' }
	      },
	      initialRequest: false
		},
		scrollX: true,
		scrollY: false,
		rowHeight : 'auto',
		width : gridWidth,
		rowHeaders: [ { type: 'rowNum'} ],
		pageOptions: {
			perPage: 20
		},
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

	grid.readData(1,parameter,true);
	
	grid.getPagination()._options.visiblePages = 5;
	
}
