ns("common") ;
var respParser = common.ajax.responseParser;
//decode special characters
var map = {amp: '&', quot: '"', '#039': "'"};

$(document).ready(function() {
	//JQuery mobile 로 인해서 추가함
	$("a").each(function() {
		$(this).attr("rel", "external");
	});
	
	//mobile menu
	$(".mMenu").click(function() {
		$(".nav").toggleClass("mNavOn");
		$(this).toggleClass("mMenuClose");
	});
	
	//체크박스all
	$("#listChkAll").click(function() {
	    if ($("#listChkAll").prop("checked")) {
	        $("input[name=listChkbox]").prop("checked",true);
	    } else {
	        $("input[name=listChkbox]").prop("checked",false);
	    }
	});
	
	$(".btnClosePop").click(function() {
		closePopup();
	});
	
	$(".btnPopNormal").click(function() {
		closePopup();
	});
	
	$("#deviceGroupList").change(function() {
		updateGroup();
	});
	
	$("#channelrangeUpdate").click(function() {
		channelrangeUpdate();
	});
	
	$("#channelrangeAllUpdate").click(function() {
		channelrangeUpdateAll();
	});
	
	$("#deviceDelete").click(function() {
		deleteDevice();
	});
	
	init();
})


// 숫자 - . 만 입력하도록 하는 함수
function onlyNumberPress(val) {	
    // 숫자를 제외한 나머지만 반환하는 정규식
    // 12.34214asd -> asd만 반환
    var pattern1 = /[^-?\d+\.?\d+]+/gi;
    // 소수점 한 자리 까지만 유지하는 정규식
    // 123124.3214 -> 123124.3만 반환
    var pattern2 = /^[-]?\d*[\.]?\d{0,1}/gi;

    // 문자열 제거
    var temp1 = val.replace(pattern1, "");
    
    // 제거된 문자열에서 정수부분이 9자리 미만이 되도록 조정
    var temp2 = temp1.split('.');
    
    if (temp2[0].length > 9) {
        var i = temp2[0].substr(0, 9);
        temp1 = i + temp2[1];
    }

    return pattern2.exec(temp1);	
	/*	
	event = event || window.event;
	var keyID = (event.which) ? event.which : event.keyCode;	
		
	if( ( keyID >=48 && keyID <= 57 ) || ( keyID >=96 && keyID <= 105 )
			|| keyID == 189 || keyID == 190 || keyID == 8  
			|| (keyID >=37 && keyID <= 40)) {
		return;
	} 
	
	event.preventDefault();	*/
}

function init() {
	//디바이스 리스트
	getDeviceList();
	
	//디바이스타입 리스트
	getDeviceTypeList();
	
	//디바이스그룹 리스트
	getGroupList();
}

function getDeviceList(devicetypecd) {
	
	// 헤더 체크 부분 리셋시킴
	if ($("#listChkAll").is(":checked")) {
        $('#listChkAll').prop('checked', false).checkboxradio('refresh');
    }
	
	var url = "/openapi/device/list";
	var parameter = { devicetypecd : devicetypecd};
	var errorComment = $.i18n.prop('common.error');
	
	//상단탭 selected 설정
	$(".tabOn").attr('class','');
	if (devicetypecd == null) {
		$("#deviceType").attr('class','tabOn');
		$('#deviceRangeUpdate').css("display", "none");
	} else {
		$("#deviceType_" + devicetypecd).attr('class','tabOn');
		$('#deviceRangeUpdate').css("display", "");
	}
	
	$.ajax({
		url : url,
		data : parameter,
		success : function(response) {
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			if (respObj.isSuccess()) {
				var data = respObj.getDataObj();
				//리스트 초기화 (첫번째 ul 제외)
				$("#deviceList ul:not(:first)").remove();
				
				var end_date = new Array();
				var nowday = new Date();
				nowday = nowday.getTime();
				var distance = [];
				var d = [];
				var html = "";
				var plan = "";
				
				for(var i=0; i<data.length; i++) {
					var date_create = data[i].createdate;
					var date_end = data[i].enddate;
					var endArr = date_end.split("-");
					end_date[i]  = new Date(endArr[0], parseInt(endArr[1])-1, endArr[2]);

					distance[i] = (end_date[i].getTime() - nowday);
					d[i] = Math.floor(distance[i] / (1000*60*60*24));

				}
				
				$.each(data, function(key, value) {
					
					var title = $.i18n.prop('device.setting');
					var sensorSetting = $.i18n.prop('device.setting');
					var deviceNameCode = "device.code_"+ value.devicetypecd +"";
					var devicetypeName = $.i18n.prop(deviceNameCode);
					
					if(value.plantype == 1 || value.plantype == 0) {}
					else value.plantype = 0;
					
					plan = $.i18n.prop('device.plantype_' + value.plantype);
					
					html += "<ul class=\"listTableBody\">"
						+ "<li><input type=\"checkbox\" id=\"" + value.deviceid + "\" name=\"listChkbox\" value=\"" + value.deviceid + "\" onchange=\"setDeviceGroupListStatus();\"><label for=\"" + value.deviceid + "\" class=\"listChk\"></label></li>" 
						+ "<li>" + value.groupname + "</li>"
						+ "<li>" + value.devicename + "</li>"
						//+ "<li>" + value.devicetypename + "</li>"
						+ "<li>" + devicetypeName + "</li>"
						+ "<li>" + plan + "</li>"
						+ "<li>" + value.deviceid + "</li>"
						+ "<li>" + value.createdate + "</li>"
						+ "<li>" + value.enddate + "</li>";
											
						if(d[key] >= 0) {
							html += "<li><a class=\"btnSet\" title=\"" + title + "\" onclick=\"showChannelrangeUpdate('" + value.deviceid + "')\">"+ sensorSetting +"</a></li>";
						} else if(d[key] < 0) {
							html += "<li><a class=\"btnSet\" title=\"" + title + "\" onclick=\"showExpiration()\">"+ sensorSetting +"</a></li>";
						}
						
						//+ "<li><a class=\"btnSet\" title=\"" + title + "\" onclick=\"showChannelrangeUpdate('" + value.deviceid + "')\">"+ sensorSetting +"</a></li>"
						html += "</ul>";
				});
				
				$("#deviceList").append(html);
				setDeviceGroupListStatus();

				//데이터가 있을경우와 없을경우 구분
				if (data.length > 0) {
					$("#deviceList").css("display", "");
					$("#noData").css("display", "none");
				} else {
					$("#deviceList").css("display", "none");
					$("#noData").css("display", "");
				}

				$("#listChkAll").prop("checked",false);
			} else {
				alert(returnCode +" "+ errorComment);
			}
		},
		error : function () {
			alert(errorComment);
		}  
	});
}

function getDeviceTypeList() {
	var url = "/openapi/device/type/list";
	var errorComment = $.i18n.prop('common.error');
	
	$.ajax({
		url : url,
		success : function(response) {
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
	    	if (respObj.isSuccess()) {
	    		var data = respObj.getDataObj();
	    		var allDevices = $.i18n.prop('common.alldevice');
	    		var html = "";
	    		html += "<li id=\"deviceType\" class=\"tabOn\"><a href=\"javascript:void(0);\" onclick=\"getDeviceList();\">"+ allDevices +"</a></li>";
				$.each(data, function(key, value) {
					html += "<li id=\"deviceType_" + value.devicetypecd + "\"><a href=\"javascript:void(0);\" onclick=\"getDeviceList(" + value.devicetypecd + ");\">" + value.devicetypename + "</a></li>";
				});
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

function getGroupList() {
	var url = "/openapi/device/group/list";
	var groupChange = $.i18n.prop('device.groupchange');
	var errorComment = $.i18n.prop('common.error');
	
	$.ajax({
		url : url,
		success : function(response) {
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
	    	if (respObj.isSuccess()) {
	    		var data = respObj.getDataObj();
	    		var html = "";
	    		html += "<option value=\"\">"+ groupChange +"</option>";
				$.each(data, function(key, value) {
			        html += "<option value=\"" + value.groupid + "\">" + value.groupname+ "</option>";
				});
				//list 화면의 그룹리스트
				$("#deviceGroupList").append(html);
				//Layer 화면의 그룹리스트
				$("#deviceUpdateGroupList").append(html);
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
		},
		error : function () {
			alert(errorComment);
		}  
	});
}

function updateGroup() {
	var errorComment = $.i18n.prop('common.error');
	var url = "/openapi/device/group/update";
	var groupid = $("#deviceGroupList").val();
	if (!groupid) {
		return;
	}
	var deviceid = "";
	$("input[name=listChkbox]:checked").each(function() {
		deviceid += "," + $(this).val();
	}); 
	var parameter = { 
		groupid : groupid,
		deviceid : deviceid.substring(1, deviceid.length)
	};
	
	$.ajax({
		url : url,
		data : parameter,
		success : function(response) {
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
	    	if (respObj.isSuccess()) {
	    		//상단탭 devicetypecd 값 
	    		var arr = $(".tabOn").attr('id').split("_");
	    		getDeviceList(arr[1]);
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
		},
		error : function () {
			alert(errorComment);
		}  
	});	
}

function setDeviceGroupListStatus() {
	//checkbox 한개라도 체크되지 않을경우, selectbox disabled 처리
	if ($("input[name=listChkbox]").is(":checked")) {
		$("#deviceGroupList").removeAttr("disabled");
		$("#deviceGroup").attr("class","selBox01");
		$("#btnRange").attr("class","btnRange");
		$("#btnRange").attr("onclick","showChannelrangeAllUpdate()");
		$("#deviceDelete").attr("class","btnCancel");
	} else {
		$("#deviceGroupList").attr("disabled", "disabled");
		$("#deviceGroup").attr("class","selBox01 btnDisable");
		$("#btnRange").attr("class","btnRange btnDisable");
		$("#btnRange").attr("onclick","");
		$("#deviceDelete").attr("class","btnCancel btnDisable");
	}
}

function showExpiration() {
	alert($.i18n.prop('device.expired.alert'));
}


function showChannelrangeUpdate(deviceid) {
	var url = "/openapi/device/setting/get";
	var errorComment = $.i18n.prop('common.error');
	var parameter = { 
		deviceid : deviceid
	};
	
	$.ajax({
		url : url,
		data : parameter,
		success : function(response) {
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
	    	if (respObj.isSuccess()) {
	    		var data = respObj.getDataObj();
	    		var tabHtml = "";
				var tab_pushHtml
				var tab_pushContentHtml = "";
				var tab_systemPushContentHtml = "";
				var tabContentHtml = new Array(data.channelrange.length);
				var channelidArr = new Array();
				var from = new Array(data.channelrange.length);
				var to = new Array(data.channelrange.length);
				var toggle = false;
	    		//채널아이디
	    		var channelidArr = new Array();
	    		
	    		$(".btnPopCenter").show();
				$(".tab_content").remove();
				$(".tabs li").remove(); // tabs li 지워야 다시 로드될때 추가가 안된다
				/*$(".extra-controls").remove();*/
        		
	    		$.each(data, function(index, value) {
			        if (index == "channelrange") {
			        	$.each(value, function(key, val) {
			        		var channelrangedata = $.parseJSON(val.channelrangedata.replace(/&([^;]+);/g, function (m, c) { return map[c]; }));
			        		var channelrangedatadefult = $.parseJSON(val.channelrangedatadefult.replace(/&([^;]+);/g, function (m, c) { return map[c]; }));
			        		var channelrangedatauser;
			        		
			        		var min = channelrangedatadefult.MIN;
			        		var max = channelrangedatadefult.MAX;
			        		if (val.channelrangedatauser) {
			        			channelrangedatauser = $.parseJSON(val.channelrangedatauser.replace(/&([^;]+);/g, function (m, c) { return map[c]; })); 
			        			min = channelrangedatauser.MIN;
			        			max = channelrangedatauser.MAX;
			        		} else {
			        			channelrangedatauser = channelrangedatadefult;
			        		}
			        		
			        		
			        	    var	sensorCode = "channel.code_" + data.devicetypecd + "_" + val.channelid;
			        		var channelName = $.i18n.prop(sensorCode);
			        	    var	channelmeasure = "type.code_" + data.devicetypecd + "_" + val.channelid;
			        		var channelmeasureCode = $.i18n.prop(channelmeasure);

			        		var index = val.channelid-1; // 0~2 인덱스
			        		var push_tab_index = data.channelrange.length+1;
			        		
			        		if(data.devicetypecd == 4) {
			        			tabHtml = "<li class=\"tab_H\" rel=\"tab" + (index+1) + "\" id=\"tab_id" + (index+1) + "\">" + channelName + "</li>";
			        			tab_pushHtml = "<li class=\"tab_H\" rel=\"tab"+ push_tab_index + "\" id=\"tab_id" + push_tab_index + "\">" + $.i18n.prop('device.push_h') + "</li>";
			        			tab_systemPushHtml = "<li class=\"tab_H\" rel=\"tab8\" id=\"tab_id8\">" + $.i18n.prop('device.syspush') + "</li>";
			        		} else {
			        			tabHtml = "<li class=\"tab_T\" rel=\"tab" + (index+1) + "\" id=\"tab_id" + (index+1) + "\">" + channelName + "</li>";
			        			tab_pushHtml = "<li class=\"tab_T\" rel=\"tab"+ push_tab_index + "\" id=\"tab_id" + push_tab_index + "\"  >" + $.i18n.prop('device.push_t') + "</li>";
			        			tab_systemPushHtml = "<li class=\"tab_T\" rel=\"tab5\" id=\"tab_id5\"  >" + $.i18n.prop('device.syspush') + "</li>";
			        		}
			        		
			        		tab_pushContentHtml = "<div id=\"tab" + push_tab_index + "\" class=\"tab_content\" >"
												+ 	"<ul>"
												+	 "<li id=\"tab_li_id" + push_tab_index + "\" >" 
												+ 		"<div class=\"extra-controls\">"
												+		 "<div class=\"push_name\">" + $.i18n.prop('device.push') + "</div>"
												+			"<label class=\"switch\">";
												
												if(data.pushenable == "Y") {
													tab_pushContentHtml += "<input type=\"checkbox\" class=\"pushEnable_input\" id=\"pushEnable\" checked >";
													toggle = false;
												}
												else{
													tab_pushContentHtml += "<input type=\"checkbox\" class=\"pushEnable_input\" id=\"pushEnable\"  >";
													toggle = true;
												}					
												 				
												tab_pushContentHtml = tab_pushContentHtml			
												
												+				"<span class=\"slider round\"></span>"
												+			"</label>"
												+		"</div>"
												+		"<div class=\"push_txt\">" + $.i18n.prop('device.push.danger') + "</div>" 
												+		"<div class=\"push_content\">"
												+		  "<div class=\"push_txt\">" + $.i18n.prop('device.push.setting') + "</div>"
												+		  "<div class=\"pushInterval_container\">"
												+			 "<div class=\"push_name\">" + $.i18n.prop('device.interval') + "</div>"		
												+			 "<select class=\"pushInterval_select_box\" id=\"pushInterval\" >"
												+				"<option value=\"1\">1</option>"
												+				"<option value=\"5\">5</option>"
												+				"<option value=\"10\">10</option>"
												+				"<option value=\"30\">30</option>"
												+				"<option value=\"60\">60</option>"
												+  			"</select>"
												+			 "<label class=\"push_text\">" + $.i18n.prop('device.minute') + "</label>"
												+		  "</div>"
												
												+		  "<div class=\"pushSendCount_container\">"
												+		 	"<div class=\"push_name\">" + $.i18n.prop('device.numberOfTimes') + "</div>";
												
												if(data.pushrepeat > 0) {
													tab_pushContentHtml += 
														"<div class=\"\"  >"
													+	"<input type=\"radio\" class=\"radio_btn\" id=\"pushSendCountOn\" name=\"trans\" value=\"1\" checked>" 
													+	"<select class=\"pushSendCount_select_box\" id=\"pushSendCount\">"
													+		"<option value=\"1\">1</option>"
													+		"<option value=\"5\">5</option>"
													+		"<option value=\"10\">10</option>"
													+  	"</select>" 
													+   "<label class=\"push_text\">" + $.i18n.prop('device.time') + "</label>"
													+	"</div>"
													+	"<div class=\"\"  >"
													+	"<input type=\"radio\" class=\"radio_btn\" id=\"pushSendCountOff\" name=\"trans\" value=\"0\">"
													+   "<label class=\"push_text\">" + $.i18n.prop('device.noLimit') + "</label>"
													+	"</div>"
												} else if(data.pushrepeat == 0) {
													tab_pushContentHtml += 
														"<div class=\"\"  >"
													+	"<input type=\"radio\" class=\"radio_btn\" id=\"pushSendCountOn\" name=\"trans\" value=\"1\">" 
													+	"<select class=\"pushSendCount_select_box\" id=\"pushSendCount\">"
													+		"<option value=\"1\">1</option>"
													+		"<option value=\"5\">5</option>"
													+		"<option value=\"10\">10</option>"
													+  	"</select>" 
													+   "<label class=\"push_text\">" + $.i18n.prop('device.time') + "</label>"
													+	"</div>"
													+	"<div class=\"\"  >"
													+	"<input type=\"radio\" class=\"radio_btn\" id=\"pushSendCountOff\" name=\"trans\" value=\"0\" checked>"
													+   "<label class=\"push_text\">" + $.i18n.prop('device.noLimit') + "</label>"
													+	"</div>"
												}
													
												tab_pushContentHtml = tab_pushContentHtml
												+		  "</div>"
												+		"</div>"
												+		  "<div class=\"deviceStoreInterval_container\">"
												+			 "<div class=\"push_name\">" + $.i18n.prop('device.storageInterval') + "</div>"
												+			 "<select class=\"deviceStoreInterval_input\" id=\"storeInterval\">"
												+				"<option value=\"1\">1</option>"
												+				"<option value=\"5\">5</option>"
												+				"<option value=\"10\">10</option>"
												+				"<option value=\"30\">30</option>"
												+				"<option value=\"60\">60</option>"
												+  			"</select>" 
												+			 "<label class=\"push_text\">" + $.i18n.prop('device.minute') + "</label>"
												+		  "</div>"
												+	 "</li>"
												+ 	"</ul>"
												+ "</div>";
			        		tabContentHtml = 
			        			"<div id=\"tab" + (index+1) + "\" class=\"tab_content\">"
			        			+ "<input type=\"hidden\" id=\"hidden_min" + index + "\" value=\""+ channelrangedatauser.MIN + "\"/>"
			        			+ "<input type=\"hidden\" id=\"hidden_max" + index + "\" value=\""+ channelrangedatauser.MAX + "\"/>"
		       					+ "<ul>"
		       					+ "<li id=\"tab_li_id" + (index+1) + "\">" 
		       					+ "<div class=\"extra-controls\">"
		        				+ "<div class=\"channelnameContainer\">"+ channelName +"</div>"       			
		        				+ "<a style=\"cursor:pointer\" class=\"btnPopReset\" id=\"reset_btn" + index + "\" >" + $.i18n.prop('device.reset') + "<i></i></a>"
		        				+ "<div class=\"button_container\">"
		        				+ "<div class=\"leftContainer\">"
		        				+ "<button class=\"button button_min_down\" id=\"min_down_btn" + index + "\" value=\"\"><img class=\"ico_down_left\" src=\"../images/ico_down_left.png\"></button>"
		        				+ "<input type=\"text\" maxlength=\"6\" class=\"inp min_inputbox\" id=\"from" + index + "\" value=\""+ channelrangedatauser.MIN + "\" onKeyUp=\"this.value=onlyNumberPress(this.value)\" />"
		        				+ "<button class=\"button button_min_up\" id=\"min_up_btn" + index + "\" value=\"\"><img class=\"ico_up_left\" src=\"../images/ico_up_left.png\"></button>"
		        				+ "</div>"
		        				+ "<div class=\"boxContainer\"></div>"
		        				+ "<div class=\"rightContainer\">"
		        				+ "<button class=\"button button_max_down\" id=\"max_down_btn" + index + "\" value=\"\"><img class=\"ico_down_right\" src=\"../images/ico_down_right.png\"></button>"
		        				+ "<input type=\"text\" maxlength=\"6\"  class=\"inp max_inputbox\" id=\"to" + index + "\" value=\""+ channelrangedatauser.MAX + "\" onKeyUp=\"this.value=onlyNumberPress(this.value)\" />"
		        				+ "<button class=\"button button_max_up\" id=\"max_up_btn" + index + "\" value=\"\"><img class=\"ico_up_right\" src=\"../images/ico_up_right.png\"></button>"
		        				+ "</div>"
		        				+ "</div>"
		        				+ "<div class=\"range-slider\">"
		        				+ "<input type=\"text\" id=\"range" + index + "\" class=\"js-range-slider\" value=\"\" />"
		        				+ "</div>"
		        				+ "</div>"
		       					+ "</li>"
		       					+ "</ul>"
		       					+ "</div>";
			        		
			        		channelidArr[key] = val.channelid;
			        		
			        		$(".tabs").append(tabHtml);
		       				$(".tab_container").append(tabContentHtml);
		       				
		       				var postfix = "℃";
	        				
	        				if(data.devicetypecd == 4) {
	        					if(index == 1 || index == 3 || index == 5) postfix = '％';
	        				}
		       				
		       				$("#range"+index).ionRangeSlider({
		        				type : "double",
		        				min : channelrangedata.MIN,
		        				max : channelrangedata.MAX,
		        				from: channelrangedatauser.MIN,
		        				to: channelrangedatauser.MAX,
		        				step : 0.1,
		        				postfix : postfix,
		        				prettify_enabled : false,
		        				grid : true,
		        				grid_num : 5,
		        				values_separator : "~",
		        			}); 
			        		
			        	});
			        }
				});
	    		
	    		var push_tab_num;
				
				if (data.devicetypecd == 4) {
					push_tab_num = 8;
				} else {
					push_tab_num = 5;
				} 
				
				tab_systemPushContentHtml = 
					"<div id=\"tab" + push_tab_num + "\" class=\"tab_content\" >"
					+ 	"<ul>"
					+	 "<li id=\"tab_li_id" + push_tab_num + "\">" 
					+ 		"<div class=\"extra-controls\">"
					+		 "<div class=\"push_name\">" + $.i18n.prop('device.pushsys') + "</div>"
					+			"<label class=\"switch\" style=\"float:left;\">"
					
					if(data.pushsysenable == "Y") {
						tab_systemPushContentHtml += "<input type=\"checkbox\" class=\"pushEnable_input\" id=\"pushSysEnable\" checked >";
						systemPushToggle = false;
					} else{
						tab_systemPushContentHtml += "<input type=\"checkbox\" class=\"pushEnable_input\" id=\"pushSysEnable\"  >";
						systemPushToggle = true;
					}		
						
					tab_systemPushContentHtml = tab_systemPushContentHtml
					+				 "<span class=\"slider round\"></span>"
					+			"</label>"
					+			"<div class=\"pushsys_label pushsys_btn\" onclick=\"applySysControlLayer('" + deviceid + "');\"  >"+ $.i18n.prop('common.apply') +"</div>"
					+		"</div>"
					+		"</div>"
					+	 "</li>"
					+	"</ul>"
					+"</div>";
	    		
	    		var udevicename = decodehtmlspecialchars(data.devicename);
	    		
	    		$(".tabs").append(tab_pushHtml);
	    		$(".tabs").append(tab_systemPushHtml);
				$(".tab_container").append(tab_pushContentHtml);
				$(".tab_container").append(tab_systemPushContentHtml);
	    		$("#deviceUpdateDevicename").text(udevicename);
	    		$("#deviceList_deviceID").text(deviceid);
	    		/*$(".btnPopCenter").append(buttonHtml);*/
	    		$("#popupBgLayer").show();
	      		$("#popupDeviceControlLayer").show();
	    		$("#deviceUpdateDevicename").val(udevicename);
	    		$("#deviceUpdateDevicetypecd").val(data.devicetypecd);
	    		$("#deviceUpdateDeviceid").val(data.deviceid);
	    		$("#deviceUpdateGroupList").val(data.groupid);
	    		$(".tab_content").hide();
			    $(".tab_content:first").show();
				
				  //range = $range.data("ionRangeSlider");
  	      	  	   $(".js-range-slider").on("change", function() {
  	        			var id = $(this).attr("id");
  	        			var id_num = id.substr(id.length - 1);
  	        			var object = $("#range"+id_num).data("ionRangeSlider");
  	        			
  	        			from[id_num] = object.result.from;
  	        			to[id_num] = object.result.to;
  	        			
  	        			$("#from"+id_num).prop("value",from[id_num]);
  	        			$("#to"+id_num).prop("value",to[id_num]);
  	        			
  	        		}); 
  	      	   		  	      
     	       	   var updataRange = function(id_num) {
     	       			$("#range"+id_num).data("ionRangeSlider").update({
     	       				from : from[id_num],
     	       				to : to[id_num]
     	       			});
     	       		};
     	       		
              		$(".min_inputbox").on("change", function() {
           				var id = $(this).attr("id");
           				var id_num = id.substr(id.length - 1);
           				from[id_num] = +$(this).prop("value");
           				updataRange(id_num);
     	       		});
     	       		$(".max_inputbox").on("change", function() {
     	       			var id = $(this).attr("id");
     	       			var id_num = id.substr(id.length - 1);
     	       			to[id_num] = +$(this).prop("value");
     	       			updataRange(id_num);
     	       		});
     	       		$(".button_min_down").on("click", function() {
     	       			var id = $(this).attr("id");
     	       			var id_num = id.substr(id.length - 1);
     	       			var object = $("#range"+id_num).data("ionRangeSlider");
     	       			from[id_num] = object.result.from;
     	       			from[id_num] -= 0.1;
     	       			updataRange(id_num);
     	       		});
     	       		
     	       		$(".button_min_up").on("click", function() {
     	       			var id = $(this).attr("id");
     	       			var id_num = id.substr(id.length - 1);
     	       			var object = $("#range"+id_num).data("ionRangeSlider");
     	       			from[id_num] = object.result.from;
     	       			from[id_num] += 0.1;
     	       			updataRange(id_num);
     	       		});
     	       		
     	       		$(".button_max_down").on("click", function() {
     	       			var id = $(this).attr("id");
     	       			var id_num = id.substr(id.length - 1);
     	       			var object = $("#range"+id_num).data("ionRangeSlider");
     	       			to[id_num] = object.result.to;
     	       			to[id_num] -= 0.1;
     	       			updataRange(id_num);
     	       		});
     	       		
     	       		$(".button_max_up").on("click", function() {
     	       			var id = $(this).attr("id");
     	       			var id_num = id.substr(id.length - 1);
     	       			var object = $("#range"+id_num).data("ionRangeSlider");
     	       			to[id_num] = object.result.to;
     	       			to[id_num] += 0.1;
     	       			updataRange(id_num);
     	       		});
     	       		
     	       	$(".btnPopReset").on("click", function() {
	       			var id = $(this).attr("id");
	       			var id_num = id.substr(id.length - 1);
	       			var object = $("#range"+id_num).data("ionRangeSlider");
	       			var hidden_min_value = $("#hidden_min"+id_num).val();
	       			var hidden_max_value = $("#hidden_max"+id_num).val();
	       			
	 				$("#reset_btn"+id_num).css("background", "#a8c4eb");
		       		setTimeout(function(){ $("#reset_btn"+id_num).css("background", "#fff"); },100); 
		       		
	 	       		from[id_num] = hidden_min_value;
		 	       	to[id_num] = hidden_max_value
		 	       	updataRange(id_num);
	      		});
	   	       
     	        if(data.pushenable == "Y") {
     				$(".push_content").css("background","#fff");
     				$(".push_content").find(".pushInterval_select_box, .pushSendCount_select_box, .radio_btn, .deviceStoreInterval_input").prop('disabled', false);
     			} else {
     				$(".push_content").css("background","#EAEAEA");
     				$(".push_content").find(".pushInterval_select_box, .pushSendCount_select_box, .radio_btn, .deviceStoreInterval_input").prop('disabled', true);
     			}
     	       	
	   	       	$(".pushEnable_input").on("click", function() {
	   	       		if(!toggle) { // 비활성화
	   	       			$(".push_content").css("background","#EAEAEA");
	   	       			toggle = true;
	   	       			$(".push_content").find(".pushInterval_select_box, .pushSendCount_select_box, .radio_btn, .deviceStoreInterval_input").prop('disabled', true);
	   	       		}
	   	       		else { // 활성화
	   	       		$(".push_content").css("background","#fff");
	   	       			toggle = false;
	   	       			$(".push_content").find(".pushInterval_select_box, .pushSendCount_select_box, .radio_btn, .deviceStoreInterval_input").prop('disabled', false);
	   	       		}
	   	       	});
	   	       	
	   	     	$("#pushInterval").val(data.pushinterval).prop("selected", true); // 푸시 간격
	   	     	$("#pushSendCount").val(data.pushrepeat).prop("selected", true);  // 푸시 횟수
	   	     	$("#storeInterval").val(data.saveinterval).prop("selected", true);  // 디바이스 저장간격
	   	     	
	   	     	if(data.pushrepeat == 0) {
	   	     		$("#pushSendCount").val("1");
	   	     		$(".pushSendCount_select_box").prop('disabled', true);
	   	     	}
	   	     	
	   	     	$("#pushSendCountOn").on("click", function() {
	   	       		$(".pushSendCount_select_box").prop('disabled', false);
	   	       	});
	   	       	
	   	       	$("#pushSendCountOff").on("click", function() {
	   	       		$(".pushSendCount_select_box").prop('disabled', true);
	   	       	});
     	    		
 	    		$("#sRangeId").val("");
 				for (var i=0; i<channelidArr.length; i++) {
 					$("#range-slider" + channelidArr[i]).rangeslider();
 					$("#sRangeId").val($("#sRangeId").val() + "," + channelidArr[i]);
 	    		}
			    
 				$("ul.tabs li").first().addClass("active").css("color","#000");
 				
 				$("ul.tabs li").click(function () {
			    	
			    	$("ul.tabs li").removeClass("active");
			    	$("ul.tabs li").css("background","#F1F1F1");
			        $(this).addClass("active").css("background", "#fff");
			        
			        $(".tab_content").hide()
			        var activeTab = $(this).attr("rel");
			        $("#" + activeTab).fadeIn(1)
			        
			        if(data.devicetypecd == 4) {
			        	if(activeTab == "tab8") {
				        	$(".btnPopCenter").hide();
				        } else {
				        	$(".btnPopCenter").show();
				        }
			        } else {
			        	if(activeTab == "tab5") {
				        	$(".btnPopCenter").hide();
				        } else {
				        	$(".btnPopCenter").show();
				        }
			        }
			        
			    });
			
			} else {
				alert("에러입니다.");
			}
		},
		error : function() {
			alert("error");
		}
	});

	$('#deviceUpdate').css("display", "");
	$('#deviceBackGround').css("display", "");	
}

function channelrangeUpdate() {
	var url = "/openapi/device/setting/set";
	var deviceid = $("#deviceUpdateDeviceid").val();
	var devicename = $("#deviceUpdateDevicename").val();
	var groupid = $("#deviceUpdateGroupList").val();
	var devicetypecd = $("#deviceUpdateDevicetypecd").val();
	var channelrangeList = new Array();
	
	//get channelrange 
	var sRangeIdArr = $("#sRangeId").val().substring(1, $("#sRangeId").val().length).split(",");
	var pushEnableCheck = $("#pushEnable").prop("checked");		// true,false;
	var pushEnable; // Y,N 값
	var pushRepeat;
	
	var pushInterval = $("#pushInterval").val();
	var pushSendCountOn = $("#pushSendCountOn").prop("checked"); 
	var pushSendCountOff = $("#pushSendCountOff").prop("checked");
	var saveInterval = $("#storeInterval").val();
	
	if(pushEnableCheck) {
		pushEnable = "Y";
		
	} else {
		pushEnable = "N";
	}
	
	if(pushSendCountOn) {
		pushRepeat = $("#pushSendCount").val(); // 1
	} 
	if(pushSendCountOff) {
		pushRepeat = $("#pushSendCountOff").val(); // 0 제한없음
	}
	
	for (var i=0; i<sRangeIdArr.length; i++) {
		var data = new Object();
		var rangeData = new Object();
		data.channelid = sRangeIdArr[i];
		rangeData.MIN = $("#from" + (sRangeIdArr[i]-1)).val();
		rangeData.MAX = $("#to" + (sRangeIdArr[i]-1)).val();
		data.channelrangedata = JSON.stringify(rangeData);
		channelrangeList.push(data) ;
	}
	var channelrange = JSON.stringify(channelrangeList);
    var parameter = { 
    	deviceid : deviceid,
    	devicename : devicename,
    	groupid : groupid,
    	devicedesc : "",
    	channelrange : channelrange,
    	devicetypecd : devicetypecd,
    	pushEnable : pushEnable,
    	pushInterval : pushInterval,
    	pushRepeat : pushRepeat,
    	saveInterval : saveInterval
	};
    
    var errorComment = $.i18n.prop('common.error');
    
    $.ajax({
		url : url,
		data : parameter,
		success : function(response) {
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
	    	if (respObj.isSuccess()) {
	    		closePopup();
	    		getDeviceList();
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
		},
		error : function () {
			alert(errorComment);
		}  
	});
}

function applySysControlLayer(deviceid) {
	
	var alertupdate = "수정 하시겠습니까?";
	
	if (!confirm(alertupdate)) {
		return false;
	}
	
	var url = "/openapi/device/setting/sysPushSet";
	var groupid = $("#deviceUpdateGroupList").val();
	var pushSysEnableCheck = $("#pushSysEnable").prop("checked");		// true,false;
	var pushSysEnable; // Y,N 값

	if(pushSysEnableCheck) {
		pushSysEnable = "Y";
	} else {
		pushSysEnable = "N";
	}
	
	var parameter = {
		deviceid: deviceid,
		groupid: groupid,
		pushSysEnable: pushSysEnable		
	};
	
	var errorComment = $.i18n.prop('common.error');

	$.ajax({
		url : url,
		data : parameter,
		success : function(response) {
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			if (respObj.isSuccess()) {
				alert("적용 되었습니다.");
				closePopup();
				
			} else {
				alert(returnCode + " " + errorComment);
			}
		},
		error : function() {
			var errorComment = $.i18n.prop('common.error');
			alert(errorComment);
		}
	});
	
}

function applyAllUpdateSysControlLayer(deviceidArr) {
	
	var alertupdate = "수정 하시겠습니까?";
	
	if (!confirm(alertupdate)) {
		return false;
	}

	var url = "/openapi/device/setting/sysAllPushSet";
	var pushSysEnableCheck = $("#pushSysEnable").prop("checked");		// true,false;
	var pushsysenable; // Y,N 값

	if(pushSysEnableCheck) {
		pushsysenable = "Y";
	} else {
		pushsysenable = "N";
	}
	
	var parameter = {
		deviceids : deviceidArr,
		pushsysenable : pushsysenable		
	};
	
	var errorComment = $.i18n.prop('common.error');

	$.ajax({
		url : url,
		data : parameter,
		success : function(response) {
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			if (respObj.isSuccess()) {
				alert("적용 되었습니다.");
				closePopup();
				
			} else {
				alert(returnCode + " " + errorComment);
			}
		},
		error : function() {
			var errorComment = $.i18n.prop('common.error');
			alert(errorComment);
		}
	});
	
}

function showChannelrangeAllUpdate() {
	//일괄변경의 url도 showChannelrangeUpdate() 함수의 url 과 같다.
	var url = "/openapi/device/setting/get";
	var deviceid = "";
	var deviceidArr = new Array();
	var i = 0;
	$("input[name=listChkbox]:checked").each(function() {
		deviceidArr[i] = $(this).val();
		i++;
	}); 
	/*console.log("asd");*/
	//선택된 값의 첫번째 값을 기준으로 일괄변경 값을 설정한다.
	var parameter = { 
		deviceid : deviceidArr[0]
	};
	
	var errorComment = $.i18n.prop('common.error');
	
	$.ajax({
		url : url,
		data : parameter,
		success : function(response) {
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
	    	if (respObj.isSuccess()) {
	    		var data = respObj.getDataObj();
	    		var tabHtml = "";
				var tab_pushHtml
				var tab_pushContentHtml = "";
				var tab_systemPushContentHtml = "";
				var tabContentHtml = new Array(data.channelrange.length);
				var channelidArr = new Array();
				var from = new Array(data.channelrange.length);
				var to = new Array(data.channelrange.length);
	    		
	    		//채널아이디
	    		var channelidArr = new Array();
	    		
	    		$(".btnPopCenter").show();
	    		$(".tab_content").remove();
				$(".tabs2 li").remove(); // tabs li 지워야 다시 로드될때 추가가 안된다
				/*$(".extra-controls").remove();*/
	    		
	    		$.each(data, function(index, value) {
			        if (index == "channelrange") {
			        	$.each(value, function(key, val) {
			        		var channelrangedata = $.parseJSON(val.channelrangedata.replace(/&([^;]+);/g, function (m, c) { return map[c]; }));
			        		var channelrangedatadefult = $.parseJSON(val.channelrangedatadefult.replace(/&([^;]+);/g, function (m, c) { return map[c]; }));
			        		var channelrangedatauser;
			        		
			        		var min = channelrangedatadefult.MIN;
			        		var max = channelrangedatadefult.MAX;
			        		if (val.channelrangedatauser) {
			        			channelrangedatauser = $.parseJSON(val.channelrangedatauser.replace(/&([^;]+);/g, function (m, c) { return map[c]; }));
				        		min = channelrangedatauser.MIN;
				        		max = channelrangedatauser.MAX;
			        		} else {
			        			channelrangedatauser = channelrangedatadefult;
			        		}
			        		
			        	    var	sensorCode = "channel.code_" + data.devicetypecd + "_" + val.channelid;
			        		var channelName = $.i18n.prop(sensorCode);
			        	    var	channelmeasure = "type.code_" + data.devicetypecd + "_" + val.channelid;
			        		var channelmeasureCode = $.i18n.prop(channelmeasure);
			        		
			        		var index = val.channelid-1; // 0~2 인덱스
			        		var push_tab_index = data.channelrange.length+1;
			        		
			        		if(data.devicetypecd == 4) {
			        			tabHtml = "<li class=\"tab_H\" rel=\"tab" + (index+1) + "\" id=\"tab_id" + (index+1) + "\">" + channelName + "</li>";
			        			tab_pushHtml = "<li class=\"tab_H\" rel=\"tab"+ push_tab_index + "\" id=\"tab_id" + push_tab_index + "\">" + $.i18n.prop('device.push_h') + "</li>";
			        			tab_systemPushHtml = "<li class=\"tab_H\" rel=\"tab8\" id=\"tab_id8\"  >" + $.i18n.prop('device.syspush') + "</li>";
			        		} else {
			        			tabHtml = "<li class=\"tab_T\" rel=\"tab" + (index+1) + "\" id=\"tab_id" + (index+1) + "\">" + channelName + "</li>";
			        			tab_pushHtml = "<li class=\"tab_T\" rel=\"tab"+ push_tab_index + "\" id=\"tab_id" + push_tab_index + "\"  >" + $.i18n.prop('device.push_t') + "</li>";
			        			tab_systemPushHtml = "<li class=\"tab_T\" rel=\"tab5\" id=\"tab_id5\"  >" + $.i18n.prop('device.syspush') + "</li>";
			        		}
			        		
			        		tab_pushContentHtml = "<div id=\"tab" + push_tab_index + "\" class=\"tab_content\" >"
												+ 	"<ul>"
												+	 "<li id=\"tab_li_id" + push_tab_index + "\" >" 
												+ 		"<div class=\"extra-controls\">"
												+		 "<div class=\"push_name\">" + $.i18n.prop('device.push') + "</div>"
												+			"<label class=\"switch\">";
												
												if(data.pushenable == "Y") {
													tab_pushContentHtml += "<input type=\"checkbox\" class=\"pushEnable_input\" id=\"pushEnable\" checked >";
													toggle = false;
												}
												else{
													tab_pushContentHtml += "<input type=\"checkbox\" class=\"pushEnable_input\" id=\"pushEnable\"  >";
													toggle = true;
												}					
												 				
												tab_pushContentHtml = tab_pushContentHtml			
												
												+				"<span class=\"slider round\"></span>"
												+			"</label>"
												+		"</div>"
												+		"<div class=\"push_txt\">" + $.i18n.prop('device.push.danger') + "</div>" 
												+		"<div class=\"push_content\">"
												+		"<div class=\"push_txt\">" + $.i18n.prop('device.push.setting') + "</div>"
												+		  "<div class=\"pushInterval_container\">"
												+			 "<div class=\"push_name\">" + $.i18n.prop('device.interval') + "</div>"		
												+			 "<select class=\"pushInterval_select_box\" id=\"pushInterval\" >"
												+				"<option value=\"1\">1</option>"
												+				"<option value=\"5\">5</option>"
												+				"<option value=\"10\">10</option>"
												+				"<option value=\"30\">30</option>"
												+				"<option value=\"60\">60</option>"
												+  			"</select>"
												+			 "<label class=\"push_text\">" + $.i18n.prop('device.minute') + "</label>"
												+		  "</div>"
												
												+		  "<div class=\"pushSendCount_container\">"
												+		 	"<div class=\"push_name\">" + $.i18n.prop('device.numberOfTimes') + "</div>";
												
												if(data.pushrepeat > 0) {
													tab_pushContentHtml += 
														"<div class=\"\">"
													+	"<input type=\"radio\" class=\"radio_btn\" id=\"pushSendCountOn\" name=\"trans\" value=\"1\" checked>" 
													+	"<select class=\"pushSendCount_select_box\" id=\"pushSendCount\">"
													+		"<option value=\"1\">1</option>"
													+		"<option value=\"5\">5</option>"
													+		"<option value=\"10\">10</option>"
													+  	"</select>" 
													+   "<label class=\"push_text\">" + $.i18n.prop('device.time') + "</label>"
													+	"</div>"
													+	"<div class=\"\">"
													+	"<input type=\"radio\" class=\"radio_btn\" id=\"pushSendCountOff\" name=\"trans\" value=\"0\">"
													+   "<label class=\"push_text\">" + $.i18n.prop('device.noLimit') + "</label>"
													+	"</div>"
												} else if(data.pushrepeat == 0) {
													tab_pushContentHtml +=
														"<div class=\"\">"
													+	"<input type=\"radio\" class=\"radio_btn\" id=\"pushSendCountOn\" name=\"trans\" value=\"1\">" 
													+	"<select class=\"pushSendCount_select_box\" id=\"pushSendCount\">"
													+		"<option value=\"1\">1</option>"
													+		"<option value=\"5\">5</option>"
													+		"<option value=\"10\">10</option>"
													+  	"</select>" 
													+   "<label class=\"push_text\">" + $.i18n.prop('device.time') + "</label>"
													+	"</div>"
													+	"<div class=\"\">"
													+	"<input type=\"radio\" class=\"radio_btn\" id=\"pushSendCountOff\" name=\"trans\" value=\"0\" checked>"
													+   "<label class=\"push_text\">" + $.i18n.prop('device.noLimit') + "</label>"
													+	"</div>"
												}
													
												tab_pushContentHtml = tab_pushContentHtml
												+		  "</div>"
												+		"</div>"
												+		  "<div class=\"deviceStoreInterval_container\">"
												+			 "<div class=\"push_name\">" + $.i18n.prop('device.storageInterval') + "</div>"
												+			 "<select class=\"deviceStoreInterval_input\" id=\"storeInterval\">"
												+				"<option value=\"1\">1</option>"
												+				"<option value=\"5\">5</option>"
												+				"<option value=\"10\">10</option>"
												+				"<option value=\"30\">30</option>"
												+				"<option value=\"60\">60</option>"
												+  			"</select>" 
												+			 "<label class=\"push_text\">" + $.i18n.prop('device.minute') + "</label>"
												+		  "</div>"
												+	 "</li>"
												+ 	"</ul>"
												+ "</div>";
			        		tabContentHtml = 
			        			"<div id=\"tab" + (index+1) + "\" class=\"tab_content\">"
			        			+ "<input type=\"hidden\" id=\"hidden_min" + index + "\" value=\""+ channelrangedatauser.MIN + "\"/>"
			        			+ "<input type=\"hidden\" id=\"hidden_max" + index + "\" value=\""+ channelrangedatauser.MAX + "\"/>"
		       					+ "<ul>"
		       					+ "<li id=\"tab_li_id" + (index+1) + "\">" 
		       					+ "<div class=\"extra-controls\">"
		        				+ "<div class=\"channelnameContainer\">"+ channelName +"</div>"       			
		        				+ "<a style=\"cursor:pointer\" class=\"btnPopReset\" id=\"reset_btn" + index + "\" >" + $.i18n.prop('device.reset') + "<i></i></a>"
		        				+ "<div class=\"button_container\">"
		        				+ "<div class=\"leftContainer\">"
		        				+ "<button class=\"button button_min_down\" id=\"min_down_btn" + index + "\" value=\"\"><img class=\"ico_down_left\" src=\"../images/ico_down_left.png\"></button>"
		        				+ "<input type=\"text\" maxlength=\"6\" class=\"inp min_inputbox\" id=\"fromAll" + index + "\" value=\""+ channelrangedatauser.MIN + "\" onKeyUp=\"this.value=onlyNumberPress(this.value)\" />"
		        				+ "<button class=\"button button_min_up\" id=\"min_up_btn" + index + "\" value=\"\"><img class=\"ico_up_left\" src=\"../images/ico_up_left.png\"></button>"
		        				+ "</div>"
		        				+ "<div class=\"boxContainer\"></div>"
		        				+ "<div class=\"rightContainer\">"
		        				+ "<button class=\"button button_max_down\" id=\"max_down_btn" + index + "\" value=\"\"><img class=\"ico_down_right\" src=\"../images/ico_down_right.png\"></button>"
		        				+ "<input type=\"text\" maxlength=\"6\"  class=\"inp max_inputbox\" id=\"toAll" + index + "\" value=\""+ channelrangedatauser.MAX + "\" onKeyUp=\"this.value=onlyNumberPress(this.value)\" />"
		        				+ "<button class=\"button button_max_up\" id=\"max_up_btn" + index + "\" value=\"\"><img class=\"ico_up_right\" src=\"../images/ico_up_right.png\"></button>"
		        				+ "</div>"
		        				+ "</div>"
		        				+ "<div class=\"range-slider\">"
		        				+ "<input type=\"text\" id=\"range" + index + "\" class=\"js-range-slider\" value=\"\" />"
		        				+ "</div>"
		        				+ "</div>"
		       					+ "</li>"
		       					+ "</ul>"
		       					+ "</div>";
			        		
			        		channelidArr[key] = val.channelid;
			        		
			        		$(".tabs2").append(tabHtml);
			        		$(".tab_container2").append(tabContentHtml);
		       				
			        		var postfix = "℃";
	        				
	        				if(data.devicetypecd == 4) {
	        					if(index == 1 || index == 3 || index == 5) postfix = '％';
	        				}
			        		
		       				$("#range"+index).ionRangeSlider({
		        				type : "double",
		        				min : channelrangedata.MIN,
		        				max : channelrangedata.MAX,
		        				from: channelrangedatauser.MIN,
		        				to: channelrangedatauser.MAX,
		        				step : 0.1,
		        				postfix : postfix,
		        				prettify_enabled : false,
		        				grid : true,
		        				grid_num : 5,
		        				values_separator : "~",
		        			}); 
			        	});
			        }
				});
	    		
	    		var push_tab_num;
				
				if (data.devicetypecd == 4) {
					push_tab_num = 8;
				} else {
					push_tab_num = 5;
				} 
				
				tab_systemPushContentHtml = 
					"<div id=\"tab" + push_tab_num + "\" class=\"tab_content\" >"
					+ 	"<ul>"
					+	 "<li id=\"tab_li_id" + push_tab_num + "\">" 
					+ 		"<div class=\"extra-controls\">"
					+		 "<div class=\"push_name\">" + $.i18n.prop('device.pushsys') + "</div>"
					+			"<label class=\"switch\" style=\"float:left;\">"
					
					if(data.pushsysenable == "Y") {
						tab_systemPushContentHtml += "<input type=\"checkbox\" class=\"pushEnable_input\" id=\"pushSysEnable\" checked >";
						systemPushToggle = false;
					} else{
						tab_systemPushContentHtml += "<input type=\"checkbox\" class=\"pushEnable_input\" id=\"pushSysEnable\"  >";
						systemPushToggle = true;
					}		
						
					tab_systemPushContentHtml = tab_systemPushContentHtml
					+				 "<span class=\"slider round\"></span>"
					+			"</label>"
					+		  "</div>"
					+		"</div>"
					+	 "</li>"
					+	"</ul>"
					+"</div>";
	    		
	    		$(".tabs2").append(tab_pushHtml);
	    		$(".tabs2").append(tab_systemPushHtml);
				$(".tab_container2").append(tab_pushContentHtml);
				$(".tab_container2").append(tab_systemPushContentHtml);
	    		
	    		$(".tab_content").hide();
			    $(".tab_content:first").show();
				
				  //range = $range.data("ionRangeSlider");
  	      	  	   $(".js-range-slider").on("change", function() {
  	        			var id = $(this).attr("id");
  	        			var id_num = id.substr(id.length - 1);
  	        			var object = $("#range"+id_num).data("ionRangeSlider");
  	        			
  	        			from[id_num] = object.result.from;
  	        			to[id_num] = object.result.to;
  	        			
  	        			$("#fromAll"+id_num).prop("value",from[id_num]);
  	        			$("#toAll"+id_num).prop("value",to[id_num]);
  	        			
  	        		}); 
  	      	   		  	      
     	       	   var updataRange = function(id_num) {
     	       			$("#range"+id_num).data("ionRangeSlider").update({
     	       				from : from[id_num],
     	       				to : to[id_num]
     	       			});
     	       		};
     	       		
              		$(".min_inputbox").on("change", function() {
           				var id = $(this).attr("id");
           				var id_num = id.substr(id.length - 1);
           				from[id_num] = +$(this).prop("value");
           				updataRange(id_num);
           				
     	       		});
     	       		$(".max_inputbox").on("change", function() {
     	       			var id = $(this).attr("id");
     	       			var id_num = id.substr(id.length - 1);
     	       			to[id_num] = +$(this).prop("value");
     	       			updataRange(id_num);
     	       		});
     	       		$(".button_min_down").on("click", function() {
     	       			var id = $(this).attr("id");
     	       			var id_num = id.substr(id.length - 1);
     	       			var object = $("#range"+id_num).data("ionRangeSlider");
     	       			from[id_num] = object.result.from;
     	       			from[id_num] -= 0.1;
     	       			updataRange(id_num);
     	       		});
     	       		
     	       		$(".button_min_up").on("click", function() {
     	       			var id = $(this).attr("id");
     	       			var id_num = id.substr(id.length - 1);
     	       			var object = $("#range"+id_num).data("ionRangeSlider");
     	       			from[id_num] = object.result.from;
     	       			from[id_num] += 0.1;
     	       			updataRange(id_num);
     	       		});
     	       		
     	       		$(".button_max_down").on("click", function() {
     	       			var id = $(this).attr("id");
     	       			var id_num = id.substr(id.length - 1);
     	       			var object = $("#range"+id_num).data("ionRangeSlider");
     	       			to[id_num] = object.result.to;
     	       			to[id_num] -= 0.1;
     	       			updataRange(id_num);
     	       		});
     	       		
     	       		$(".button_max_up").on("click", function() {
     	       			var id = $(this).attr("id");
     	       			var id_num = id.substr(id.length - 1);
     	       			var object = $("#range"+id_num).data("ionRangeSlider");
     	       			to[id_num] = object.result.to;
     	       			to[id_num] += 0.1;
     	       			updataRange(id_num);
     	       		});
     	       		
     	       	$(".btnPopReset").on("click", function() {
	       			var id = $(this).attr("id");
	       			var id_num = id.substr(id.length - 1);
	       			var object = $("#range"+id_num).data("ionRangeSlider");
	       			var hidden_min_value = $("#hidden_min"+id_num).val();
	       			var hidden_max_value = $("#hidden_max"+id_num).val();
	       			
	 				$("#reset_btn"+id_num).css("background", "#a8c4eb");
		       		setTimeout(function(){ $("#reset_btn"+id_num).css("background", "#fff"); },100); 
		       		
	 	       		from[id_num] = hidden_min_value;
		 	       	to[id_num] = hidden_max_value
		 	       	updataRange(id_num);
	      		});
	   	       
     	        if(data.pushenable == "Y") {
      				$(".push_content").css("background","#fff");
      				$(".push_content").find(".pushInterval_select_box, .pushSendCount_select_box, .radio_btn, .deviceStoreInterval_input").prop('disabled', false);
      			} else {
      				$(".push_content").css("background","#EAEAEA");
      				$(".push_content").find(".pushInterval_select_box, .pushSendCount_select_box, .radio_btn, .deviceStoreInterval_input").prop('disabled', true);
      			}
     	       	
	   	       	$(".pushEnable_input").on("click", function() {
	   	       		if(!toggle) { // 비활성화
	   	       			$(".push_content").css("background","#EAEAEA");
	   	       			toggle = true;
	   	       			$(".push_content").find(".pushInterval_select_box, .pushSendCount_select_box, .radio_btn, .deviceStoreInterval_input").prop('disabled', true);
	   	       		}
	   	       		else { // 활성화
	   	       		$(".push_content").css("background","#fff");
	   	       			toggle = false;
	   	       			$(".push_content").find(".pushInterval_select_box, .pushSendCount_select_box, .radio_btn, .deviceStoreInterval_input").prop('disabled', false);
	   	       		}
	   	       	});
	   	       	
	   	     	$("#pushInterval").val(data.pushinterval).prop("selected", true); // 푸시 간격
	   	     	$("#pushSendCount").val(data.pushrepeat).prop("selected", true);  // 푸시 횟수
	   	     	$("#storeInterval").val(data.saveinterval).prop("selected", true);  // 디바이스 저장간격
	   	     	
	   	     	if(data.pushrepeat == 0) {
	   	     		$("#pushSendCount").val("1");
	   	     		$(".pushSendCount_select_box").prop('disabled', true);
	   	     	}
	   	     	
	   	     	$("#pushSendCountOn").on("click", function() {
	   	       		$(".pushSendCount_select_box").prop('disabled', false);
	   	       	});
	   	       	
	   	       	$("#pushSendCountOff").on("click", function() {
	   	       		$(".pushSendCount_select_box").prop('disabled', true);
	   	       	});
		    		
	    		$("#sRangeIdAll").val("");
	    		for (var i=0; i<channelidArr.length; i++) {
		   	          $("#rangeAll" + channelidArr[i]).rangeslider();
		              $("#sRangeIdAll").val($("#sRangeIdAll").val() + "," + channelidArr[i]);
	    		}
	    		
	    		$("ul.tabs2 li").first().addClass("active").css("color","#000");
	    		
	    		$("ul.tabs2 li").click(function () {
			    	
			        $("ul.tabs2 li").removeClass("active");
			    	$("ul.tabs2 li").css("background","#F1F1F1");
			        $(this).addClass("active").css("background", "#fff");
			        
			        $(".tab_content").hide()
			        var activeTab = $(this).attr("rel");
			        $("#" + activeTab).fadeIn(1)
			       
			    });
	    		
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
		},
		error : function () {
			alert(errorComment);
		}  
	});	
	
	$('#deviceAllUpdate').css("display", "");
	$('#deviceBackGround').css("display", "");
}

function channelrangeUpdateAll() {
	var url = "/openapi/device/setting/update";
	var deviceid = "";
	var channelrangeList = new Array();
	
	$("input[name=listChkbox]:checked").each(function() {
		deviceid += "," + $(this).val();
	});
	
	//get channelrange 
	var sRangeIdAllArr = $("#sRangeIdAll").val().substring(1, $("#sRangeIdAll").val().length).split(",");
	
	var pushEnableCheck = $("#pushEnable").prop("checked");		// true,false;
	var pushSysEnableCheck = $("#pushSysEnable").prop("checked");
	var pushEnable; // Y,N 값
	var pushSysEnable // Y,N 값
	var pushRepeat;
	
	var pushInterval = $("#pushInterval").val();
	var pushSendCountOn = $("#pushSendCountOn").prop("checked"); 
	var pushSendCountOff = $("#pushSendCountOff").prop("checked");
	var saveInterval = $("#storeInterval").val();
	
	if(pushEnableCheck) {
		pushEnable = "Y";
	} else {
		pushEnable = "N";
	}
	
	if(pushSysEnableCheck) {
		pushSysEnable = "Y";
	} else {
		pushSysEnable = "N";
	}
	
	if(pushSendCountOn) {
		pushRepeat = $("#pushSendCount").val(); // 1
	} 
	if(pushSendCountOff) {
		pushRepeat = $("#pushSendCountOff").val(); // 0 제한없음
	}
	
	/*console.log("pushEnable :" + pushEnable);
	console.log("pushInterval :" + pushInterval);
	console.log("pushRepeat :" + pushRepeat);
	console.log("saveInterval :" + saveInterval);*/
	
	for (var i=0; i<sRangeIdAllArr.length; i++) {
		var data = new Object();
		var rangeData = new Object();
		data.channelid = sRangeIdAllArr[i];
		rangeData.MIN = $("#fromAll" + (sRangeIdAllArr[i]-1)).val();
		rangeData.MAX = $("#toAll" + (sRangeIdAllArr[i]-1)).val();
		//rangeData.MIN = $( "#slider-range-all" + sRangeIdAllArr[i] ).slider( "option", "values" )[0];
		//rangeData.MAX = $( "#slider-range-all" + sRangeIdAllArr[i] ).slider( "option", "values" )[1]
		data.channelrangedata = JSON.stringify(rangeData);
		channelrangeList.push(data) ;
	}
	var channelrange = JSON.stringify(channelrangeList);
    var parameter = { 
    	deviceids : deviceid.substring(1, deviceid.length),
    	channelrange : channelrange,
    	pushEnable : pushEnable,
    	pushSysEnable : pushSysEnable,
    	pushInterval : pushInterval,
    	pushRepeat : pushRepeat,
    	saveInterval : saveInterval
	};
    
    var errorComment = $.i18n.prop('common.error');
    
    $.ajax({
		url : url,
		data : parameter,
		success : function(response) {
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
	    	if (respObj.isSuccess()) {
	    		closePopup();
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
		},
		error : function () {
			alert(errorComment);
		}  
	});
}

//디바이스 삭제
function deleteDevice() {
	
	var alertremove = $.i18n.prop('device.alertremove');

	//확인창
	if (!confirm(alertremove)) {
		return false;
	}
	
	var url = "/openapi/device/remove";
	var deviceids = "";
	$("input[name=listChkbox]:checked").each(function() {
		deviceids += "," + $(this).val();
	});
	
	var parameter = { 
		deviceids : deviceids.substring(1, deviceids.length)
	};
	//console.log(parameter);
	var errorComment = $.i18n.prop('common.error');

	//console.log(parameter);
	
	$.ajax({
		url : url,
		data : parameter,
		success : function(response) {
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
	    	if (respObj.isSuccess()) {
	    		//상단탭 devicetypecd 값 
	    		var arr = $(".tabOn").attr('id').split("_");
	    		getDeviceList(arr[1]);
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
		},
		error : function () {
			alert(errorComment);
		}  
	});
}

//팝업창 종료
function closePopup() {
	$('#deviceUpdate').css("display", "none");
	$('#deviceAllUpdate').css("display", "none");
	$('#deviceBackGround').css("display", "none");
}