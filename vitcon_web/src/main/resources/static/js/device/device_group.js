ns("common") ;
var respParser = common.ajax.responseParser;

$(document).ready(function() {
	//mobile menu
	$(".mMenu").click(function() {
		$(".nav").toggleClass("mNavOn");
		$(this).toggleClass("mMenuClose");
	});
	
	$("#btnAddGroup").click(function() {
		showPopupAddGroup();
	});
	
	$("#addGroup").click(function() {
		var groupname = $("#addGroupname").val();
		if (isEmpty(groupname)) {
			var groupnameNull = $.i18n.prop('device.group.name.null');
			
			alert(groupnameNull);
			return;
		}
						
		// 그룹 추가
		addGroup();
	});
	
	$("#editGroup").click(function() {
		var groupname = $("#editGroupname").val();
		if (isEmpty(groupname)) {
			var groupnameNull = $.i18n.prop('device.group.name.null');
			
			alert(groupnameNull);
			return;
		}
		
		// 그룹 수정
		editGroup();
	});
	
	$(".btnClosePop").click(function() {
		closePopup();
	});
	
	$(".btnPopNormal").click(function() {
		closePopup();
	});
	
	init();
})

function init() {
	//디바이스그룹 리스트
	getGroupList();
}


function getGroupList() {
	var url = "/openapi/device/group/managementlist";
	var update = $.i18n.prop('common.update');
	var remove = $.i18n.prop('common.remove');
	var removetip = $.i18n.prop('device.group.removetip');
	var errorComment = $.i18n.prop('common.error');
	
	$.ajax({
		url : url,
		success : function(response) {
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			
	    	if (respObj.isSuccess()) {	    		
	    		var data = respObj.getDataObj();
				//리스트 초기화 (첫번째 ul 제외)
				$("#groupList ul:not(:first)").remove();
				$(".listTableBottom").remove();

	    		var html = "";
				$.each(data, function(key, value) {
					html += "<ul class=\"listTableBody\">";
					html += "<li>" + value.groupname + "</li>";
					html += "<li>" + value.count + "</li>";
					//기본그룹은 수정 및 삭제 금지
					if (value.defaultyn == "Y") {
						html += "<li><a class=\"btnEdit btnDisable\" title="+ $.i18n.prop('common.update.explanation') +">"+ update +"</a></li>";
						html += "<li><a class=\"btnDel btnDisable\" title="+ $.i18n.prop('common.remove.explanation') +">"+ remove +"</a></li>";
					} else {						
						var grpname = encodehtmlspecialchars(value.groupname);						
						html += "<li><a class=\"btnEdit\" title="+ $.i18n.prop('common.update.explanation') +" onclick=\"showPopupEditGroup('" + value.groupid + "', '" + grpname + "');\">"+ update +"</a></li>";
						html += "<li><a class=\"btnDel\" title="+ $.i18n.prop('common.remove.explanation') +" onclick=\"deleteGroup('" + value.groupid + "', '" + value.count + "');\">"+ remove +"</a></li>";
					}
					html += "</ul>";
					if (key == (data.length -1)) {
						html += "<div class=\"listTableBottom\">"+ removetip +"</div>";
					}
				});
				//list 화면의 그룹리스트
				$("#groupList").append(html);
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
		},
		error : function () {
			var errorComment = $.i18n.prop('common.error');
			alert(errorComment);
		}  
	});
}

//그룹추가 팝업창 표출
function showPopupAddGroup() {
	$('#popupAddGroup').css("display", "");
	$('#popupBgLayer').css("display", "");
	$("#addGroupname").val("");
}

//그룹추가
function addGroup() {
	var groupname = $("#addGroupname").val();
	var url = "/openapi/device/group/make";
	var parameter = { 
		groupname : groupname,
		defaultyn : 'N'
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
	    		getGroupList();
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
		},
		error : function () {
			var errorComment = $.i18n.prop('common.error');
			alert(errorComment);
		}  
	});	
}

//그룹수정 팝업창 표출
function showPopupEditGroup(groupid, groupname) {
	$('#popupEditGroup').css("display", "");
	$('#popupBgLayer').css("display", "");
	$("#editGroupid").val(groupid);
	groupname = decodehtmlspecialchars(groupname);
	$("#editGroupname").val(groupname);
}

//그룹수정
function editGroup() {
	var groupid = $("#editGroupid").val();
	var groupname = $("#editGroupname").val();
	var url = "/openapi/device/group/changegroup";
	var parameter = { 
		groupname : groupname,
		groupid : groupid
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
	    		getGroupList();
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
		},
		error : function () {
			var errorComment = $.i18n.prop('common.error');
			alert(errorComment);
		}  
	});	
}

//그룹삭제
function deleteGroup(groupid, count) {
	var alertcancel = $.i18n.prop('device.removegroup.alertcancel');
	var alertremove = $.i18n.prop('device.removegroup.alertremove');
	var errorComment = $.i18n.prop('common.error');
	
	//validation
	if (count > 0) {
		alert(alertcancel);
		return false;
	}
	
	//확인창
	if (!confirm(alertremove)) {
		return false;
	}
	var url = "/openapi/device/group/delete";
	var parameter = { 
		groupid : groupid
	};
	$.ajax({
		url : url,
		data : parameter,
		success : function(response) {
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			
	    	if (respObj.isSuccess()) {
	    		getGroupList();
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
		},
		error : function () {
			var errorComment = $.i18n.prop('common.error');
			alert(errorComment);
		}  
	});	
}

//팝업창 종료
function closePopup() {
	$('#popupAddGroup').css("display", "none");
	$('#popupEditGroup').css("display", "none");
	$('#popupBgLayer').css("display", "none");
}

function isEmpty(value) {
	if (value == null || value.trim() == "") {
		// 값이 비어있는지 확인한다.(비어있다.)
		return true;
	}
	// 값이 비어있지 않다.
	return false;
}
