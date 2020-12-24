ns("common") ;
var respParser = common.ajax.responseParser;
var subUsercount = 0;

var timerChangeName = [];
var timerChangeOrgan = [];
var timerChangePhone = [];

$(function() {
	init(); // 처음 로딩 될때 실행되는 메서드
	window.dupCheckYn = "N";
	 	
    /* 체크박스를 모두 선택 / 해제 하는 기능  */
	$("#listChkAll").on("click", function() {
		var btnAccountDel = $("#btnAccountDel");
		
        if ($("#listChkAll").prop("checked") == true) {
        	$("input[name=listChkbox]").prop("checked", true);
        	/* 태그 활성화 시키기 */
		    btnAccountDel.attr("class", "btnAccountDel");
		    btnAccountDel.attr('disabled', false);
        } else {        	
        	$("input[name=listChkbox]").prop("checked", false);
        	/* 태그 비 활성화 시키기 */
        	btnAccountDel.attr("class", "btnAccountDel btnDisable");
        	btnAccountDel.attr('disabled', true);
        }    
	});
 
    // 계정추가 버튼을 누른 경우
	// 이메일이 중복인 경우 추가하지 못하도록 함				
	$("#userAddBtn").on("click", function() {		
		var inputName = $("#inputName").val();			
		var inputId = $("#inputId").val();		
		var selGrade = $("#selGrade").val();
		var inputPhone = $("#inputPhone").val();
		var inputOrgan = $("#inputOrgan").val();
		var errorComment = $.i18n.prop('common.error');

		// validation 을 체크했다...
		if (!validationCheck(inputId, inputName, inputPhone, selGrade, inputOrgan) || !emailCheck(inputId)) {
			return;
		}

		$.ajax({
			type : "post",
			url : "/openapi/account/getemail",
			data : {
				"userid" : inputId
			},
			success : function(result) {
				var respObj = respParser.parse(result);
				var returnCode = result.returnCode;

		    	if (respObj.isSuccess()) {
		    		if (result.data == "1") {
		    			var idUsing = $.i18n.prop('account.id.using');
		    			//중복된 이메일				
						writeStatusRedMessage(idUsing);					
						return;
					}			
		    		// 회원가입 처리
					insertSubUser();	
		    	} else {
		    		var addFail = $.i18n.prop('account.id.addfail');
		    		alert(returnCode +" "+ errorComment);
		    		alert(addFail);
					return;
		    	}
			},
			error: function (error) {
				alert(errorComment);
			}
		});
	});	
});

function insertSubUser() {
	var inputName = $("#inputName").val();			
	var inputId = $("#inputId").val();
	var inputOrgan = $("#inputOrgan").val();
	var inputPhone = $("#inputPhone").val();
	var selGrade = $("#selGrade").val();
	var localecd = undefined;
	var errorComment = $.i18n.prop('common.error');
	var registrationtype = "vitcon";
	
	var cookie = document.cookie;
	var startIndexOf = cookie.indexOf("localecd");
	
	var endIndexOf = cookie.indexOf(';', startIndexOf);
	
	if (endIndexOf == -1) {
		endIndexOf = cookie.length;
	}
	
	localecd = cookie.substring(startIndexOf + 'localecd='.length, endIndexOf);
	
	/*var now = new Date();
    var year= now.getFullYear();
    var month = (now.getMonth()+1)>9 ? ''+(now.getMonth()+1) : '0'+(now.getMonth()+1);
    var day = now.getDate()>9 ? ''+now.getDate() : '0'+now.getDate();
    var startdate = year + '-' + month + '-' + day + " " + now.getHours() + ":" + now.getMinutes();
    
    if (month == 12) {
    	year++;
    	month = "01";
    } else {
    	month = ((parseInt(month) + 1) + '');
    	month = month.length > 1 ? month : '0' + month;
    }
    
    var enddate = year + '-' + month + '-' + day + " " + now.getHours() + ":" + now.getMinutes();*/
    
	var now = new Date();
	var year = now.getFullYear();
	var month = now.getMonth()+1;
	var month_end = now.getMonth()+2;
	var day = now.getDate();
	var hour = now.getHours();
	var minute = now.getMinutes();
	var second = now.getSeconds();

	var startdate = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + " " + 
		(hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second);
	
	var lastDay_start = ( new Date(year, month, 0) ).getDate();
	var lastDay_end;
	
	if(day == lastDay_start) lastDay_end = ( new Date( year, month_end, 0) ).getDate(); 
	else lastDay_end = day;

	var enddate = year + "-" + (month_end < 10 ? "0" + month_end : month_end) + "-" + (lastDay_end < 10 ? "0" + lastDay_end : lastDay_end) + " " + 
	(hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second);
	
	
	if (endIndexOf == -1) {
		endIndexOf = cookie.length;
	}
	
	// var inputMibile = $("#inputMobile").val(); 	

	$.ajax({
		type : "post",
		url : "/openapi/account/insertsubuser",
		data : {
			"userid" : inputId,
			"passwd" : inputId,
			"username" : inputName,
			"grade" : selGrade,
			"organization" : inputOrgan, 
			"mobile" : inputPhone,		
			"startdate" : startdate,
			"enddate" : enddate,
			"localecd" : localecd,
			"pushyn" : "1",
			"registrationtype" : registrationtype
			// "countNum" : "0" //이거 추후에 추가 해야되는건뎅... 음 ...
		},
		success : function(result) {			
					
			var respObj = respParser.parse(result);
			var returnCode = result.returnCode;
			
			if (result.returnCode == 801) {
				// $("#phoneChkTxt").css("display", "block");
				var mobileDuplicated = $.i18n.prop('mobile.duplicate');		
				
				writeStatusRedMessage(mobileDuplicated);
				return;
			}

	    	if (respObj.isSuccess()) {
	    		$("#phoneChkTxt").css("display", "none");
				var idadd = $.i18n.prop('signup.mypageuser');			
	    		writeStatusMessage(idadd);
				initEntryTag();
				subUsercount++;
				// 이메일 ,이름, 조직, 번호 순				
				var html = makeUserHtml(inputId, inputName, inputOrgan, selGrade, inputPhone, 0, subUsercount);
				$("#listTableUser").append(html);
				// 계정이 추가되고나서 전체 체크박스에 대해서 표시해야함.
				$("#listChkAll").css("display", "none");
		    	$("#listChkAll").next().css("display", "inline-block");
		    	$("#listChkAll").prop("checked", false);
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
			
		},
		error: function (error) {
			alert(errorComment);
		}
	});
}

// 널갑과 빈값을 체크하는 함수
function isEmpty(value) {	
	if (value == null || value.trim() == "") {
		return true;
	}	
}

// 유효성 체크하는 함수
function validationCheck(inputId, inputName, inputMobile, selGrade, inputOrgan) {
	if (isEmpty(inputId)) 					
		return false;

	if (isEmpty(inputOrgan)) 					
		return false;

	if (isEmpty(inputName))						
	    return false;

	if (!mobilePhoneCheck(inputMobile)) {
		if (isEmpty(inputMobile)) {
		    $("#phoneCnfTxt").css("display", "none");
		    $("#phoneChkTxt").text($.i18n.prop('account.add.tip'));		
		    $("#phoneChkTxt").show();
		}		
		return false;
	}

	if (selGrade == "000") {
		$("#authChkTxt").show();
	  	return false;
	}
	
	return true;	
}

function mobilePhoneCheck(mobile) {
	var regExp = /^\d{3}\d{3,4}\d{4,}$/;
	return (mobile != '' && mobile != 'undefined' && regExp.test(mobile));
}

// 입력 값 태그 초기화 함수
function initEntryTag() {
	$("#inputId").val("");
	$("#inputName").val("");
	$("#inputOrgan").val("");
	$("#inputPhone").val("");
	$("#selGrade").val("000");
	
	var userAddBtn = $("#userAddBtn"); 
	
	//userAddBtn.attr('disabled', false);
	userAddBtn.attr("class", "userAddBtn btnDisable");	
}

/* 체크박스 선택한 후 계정 삭제하기 */
function deleteSubUser() {
	
	var btnAccountDel = $("#btnAccountDel");
	var deleteDataArr = [];	
	var userStr = "";	
	
	// 선택된 체크박스를 분별
	$("input[name=listChkbox]").each(function() {		
		if ($(this).is(":checked")) {
			deleteDataArr.push($(this).attr("id"));
		}
	});	
		
	// 선택된 체크박스가 없다면 함수 종료
	if (deleteDataArr.length == 0) {		
		return;
	} 

	var removeidalertremove = $.i18n.prop('account.removeid.alertremove');
	if (!confirm(removeidalertremove)) {
		return;
	}	
			
    for (var i = 0; i < deleteDataArr.length; i++) {    			
    	userStr += "," + $("#" + deleteDataArr[i]).attr("user-src");    	
    	$("#" + deleteDataArr[i]).parents("ul.listTableBody").remove();       
    }
    
    var dataLenth = $(".listTableBody").length;
    
    if (dataLenth <= 0) {					
    	$("#listChkAll").css("display", "none");
    	$("#listChkAll").next().css("display", "none");
    }
 	
    var errorComment = $.i18n.prop('common.error');
    
	// 첫번째 콤마 제거                    
	userStr  = userStr.replace(/^,/, "");		
    $.ajax({
	    type : "post",
	    url : "/openapi/account/deletemiddlemanager",
	    data : {
	    	"userid" : userStr
	    },
	    success : function(response) {	    	
			var respObj = respParser.parse(response);
			var returnCode = response.returnCode;
			var btnAccountDel = $("#btnAccountDel");
			
			var removeidremove = $.i18n.prop('account.removeid.remove');
	    	if (respObj.isSuccess()) {				
	    		/* 태그 비 활성화 시키기 */
			    btnAccountDel.attr('disabled', true);
			    btnAccountDel.attr("class", "btnAccountDel btnDisable");			    
			    writeStatusMessage(removeidremove);
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
    	},
    	error : function () {
    		alert(errorComment);
		}  
	});	
}

/* 단일 체크박스 선택한 경우 삭제버튼 활성화 */    
function checkEachEvent() {	
	var isCehcked = "N";
	var btnAccountDel = $('#btnAccountDel');
	
	$("input[name=listChkbox]").each(function() {		
		if ($(this).is(":checked")) {
			isCehcked = "Y";									
		}			
	});
			
	if (isCehcked == "Y") {   
    	/* 태그 활성화 시키기 */
    	btnAccountDel.attr('disabled', false);
    	btnAccountDel.attr("class", "btnAccountDel");    	
    } else {    	
    	/* 태그 비 활성화 시키기 */
    	btnAccountDel.attr('disabled', true);
    	btnAccountDel.attr("class", "btnAccountDel btnDisable");
    }
    
 }

function writeUserAddEvent() {	
	var inputName = $("#inputName").val();			
	var inputId = $("#inputId").val();
	var inputOrgan = $("#inputOrgan").val();
	var selGrade = $("#selGrade").val();
	var inputPhone = $("#inputPhone").val();
			
	var userAddBtn = $('#userAddBtn');
	
	// 휴대전화 형식에 맞지 않음 표시와 모든항목을 모두 기입해 주세요 표시 중 한가지만 입력 되게 하기 위해서...
	var phoneChkTxt = $("#phoneChkTxt").css("display");
	
	// 휴대전화 형식에 맞지 않음 경우..
	// block 조건은 이메일 형식에 맞지 않고, phonechkTxt메세지가 none인 경우에만 띄워주도록!!
	if (!mobilePhoneCheck($("#inputPhone").val()) && phoneChkTxt == "none") {
		$("#phoneCnfTxt").show();
		// $("#phoneCnfTxt").css("display", "block");
	} else {
		$("#phoneCnfTxt").hide();
		// $("#phoneCnfTxt").css("display", "none");
	}
	
	
	$("#emailChkTxt").hide();
	$("#phoneChkTxt").hide();
	$("#authChkTxt").hide();
	
	if (inputName.length  < 1 || selGrade == "000" || !emailCheck(inputId) || !mobilePhoneCheck(inputPhone) || inputOrgan.length < 1) {
		//아이디가 형식에 안맞는 경우
		if (!emailCheck(inputId)) {
			$("#emailChkTxt").show();
		}
		
		/* 태그 비 활성화 시키기 */
		userAddBtn.attr("class", "userAddBtn btnDisable");
		//userAddBtn.attr('disabled', true);
		return;
	}		
			
	/* 태그 활성화 시키기 */
	userAddBtn.attr("class", "userAddBtn");	
	//userAddBtn.attr('disabled', false);	
}

/* 비밀번호 리셋 버튼 클릭 이벤트 */
function resetPassWord(inputId) {
	var resetpasswdalertpasswd = $.i18n.prop('account.resetpasswd.alertpasswd');
	var errorComment = $.i18n.prop('common.error');
	
 	if (confirm(resetpasswdalertpasswd)) { 		
     	$.ajax({
     		type : "post",
     		url : "/openapi/account/updateuser",
     		data : {
     			"userid" : inputId,
     			"passwd" : inputId
     		},
     		success : function(result) {
     			var respObj = respParser.parse(result);
     			var returnCode = result.returnCode;
     			
     			var resetpasswdreset = $.i18n.prop('account.resetpasswd.reset');
    	    	if (respObj.isSuccess()) {
    	    		writeStatusMessage(resetpasswdreset);
    	    	} else {
    	    		alert(returnCode +" "+ errorComment);
    	    	}

     		},
     		error: function (error) {
     			alert(errorComment);
     		}
     	});	
 	}    	
}

function emailCheck(inputId) {    
    var regex=/^[a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z\.]{1,}$/i;
    return (inputId != '' && inputId != 'undefined' && regex.test(inputId));
}

function init() {
	var errorComment = $.i18n.prop('common.error');
	
	$.ajax({
		type : "get",
		url : "/openapi/account/getaccountlist",		
		success : function(result) {			
 			var respObj = respParser.parse(result);
 			var returnCode = result.returnCode;
 			var dataLenth = result.data.length;
					
	    	if (respObj.isSuccess()) {
	    		var data = result.data;			
				subUsercount = data.length;
				
				if (dataLenth <= 0) {					
					$("#listChkAll").css("display", "none");
					$("#listChkAll").next().css("display", "none");
				}
				
				for (var i = 0; i < subUsercount; i++) {	 			
					var html = makeUserHtml(data[i].userid, data[i].username, data[i].organization, data[i].grade, data[i].mobile, data[i].countNum, i);
					$("#listTableUser").append(html);
				}
				
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
									
		},
		error: function (error) {
			alert(errorComment);
		}
	});
}

// 센서관리 Form 전송 함수
function manageDeviceSensor(userid) {	
	// 중간관리자와 사용자 userid 임
	$("#muserid").val(userid);
	document.mypageUserForm.submit();
}


function changeNameByTimer(idx) {	
	var inputName = $("#inputName" + idx).val();
	var inputId = $("#inputId" + idx).val();
	var errorComment = $.i18n.prop('common.error');
	
	if (isEmpty(inputName)) {
		return;
	}
	
	$.ajax({
		type : "post",
		url : "/openapi/account/updateuser",
		data : {
			"userid" : inputId,
			"username" : inputName				
		},
		success : function(result) {
 			var respObj = respParser.parse(result);
 			var returnCode = result.returnCode;
 			
	    	if (respObj.isSuccess()) {
	    		var nameUpdate = $.i18n.prop('account.name.update');
	    		writeStatusMessage(nameUpdate);
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}

		},
		error: function (error) {
			alert(errorComment);
		}
	});	
}

function changeOrganByTimer(idx) {	
	var inputId = $("#inputId" + idx).val();
	var inputOrgan = $("#inputOrgan" + idx).val();
	var errorComment = $.i18n.prop('common.error');
	
	if (isEmpty(inputOrgan)) {
		return;
	}
	
	$.ajax({
		type : "post",
		url : "/openapi/account/updateuser",
		data : {
			"userid" : inputId,
			"organization" : inputOrgan				
		},
		success : function(result) {
 			var respObj = respParser.parse(result);
 			var returnCode = result.returnCode;
 			
	    	if (respObj.isSuccess()) {
	    		var organUpdate = $.i18n.prop('account.organization.update');
	    		writeStatusMessage(organUpdate);
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
	    	
		},
		error: function (error) {	        	
			alert(errorComment);
		}	
	});	
}

function changePhoneTimer(idx) {	
	var inputId = $("#inputId" + idx).val();
	var inputPhone = $("#inputPhone" + idx).val();
	var errorComment = $.i18n.prop('common.error');
	
	if (!mobilePhoneCheck(inputPhone)) {
		return;
	}
	
	$.ajax({
		type : "post",
		url : "/openapi/account/updateuser",
		data : {
			"userid" : inputId,
			"mobile" : inputPhone				
		},
		success : function(result) {			
 			var respObj = respParser.parse(result);
 			var returnCode = result.returnCode; 			
 			if (result.returnCode == 801) {
 				 				
	    		var mobileDuplicated = $.i18n.prop('account.mobile.duplicate');
 				writeStatusRedMessage(mobileDuplicated);
 				return;
 			}
 			
	    	if (respObj.isSuccess()) {	    		
	    		var mobileupdate = $.i18n.prop('account.mobile.update');
	    		writeStatusMessage(mobileupdate);
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
	    	
		},
		error: function (error) {
			alert(errorComment);
		}
	});	
}

function changePhone(idx, event) {	
	if (timerChangePhone[idx])
		clearTimeout(timerChangePhone[idx]);

	//엔터키인 경우
	if (event.keyCode == 13) {
		changePhoneTimer(idx);
	} else {
		timerChangePhone[idx] = setTimeout("changePhoneTimer(" + idx + ")", 1000);
	}
}

// idx 는 데이터 상자 Length 로 돌린 idx 임
function changeName(idx, event) {		
	// timerChangeName[idx] 에 setTimeout(); 이런식으로 들어가고
	// 또 change가 발생하면 clearTimeout(timerChangeName[idx]) 로 함수를 종료함	
	if (timerChangeName[idx])
		clearTimeout(timerChangeName[idx]);		

	//엔터키인 경우
	if (event.keyCode == 13) {
		changeNameByTimer(idx);
	} else {
		// 1초 뒤에 실행되는 함수
		timerChangeName[idx] = setTimeout("changeNameByTimer(" + idx + ")", 1000);
	}
}

function changeOrgan(idx, event) {
	if (timerChangeOrgan[idx])
		clearTimeout(timerChangeOrgan[idx]);
	
	//엔터키인 경우
	if (event.keyCode == 13) {
		changeOrganByTimer(idx);
	} else {
		timerChangeOrgan[idx] = setTimeout("changeOrganByTimer(" + idx + ")", 1000);
	}
}

function changeRole(idx) {
	var inputId = $("#inputId" + idx).val();
	var selAuth = $("#selAuth" + idx).val();	
	var errorComment = $.i18n.prop('common.error');
	
	$.ajax({
		type : "post",
		url : "/openapi/account/updateuser",
		data : {
			"userid" : inputId,
			"grade" : selAuth				
		},
		success : function(result) {
 			var respObj = respParser.parse(result);
 			var returnCode = result.returnCode;
 			
	    	if (respObj.isSuccess()) {
	    		var authorityupdate = $.i18n.prop('account.authority.update');
	    		writeStatusMessage(authorityupdate);
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}	
		},
		error: function (error) {
			alert(errorComment);
		}
	});
	
}

// 이메일 ,이름, 조직, 번호 순
function makeUserHtml(userid, username, organization, grade, mobile, countNum, i) {
	var middleSelected = grade == "20" ? "selected" : "";
	var normalSelected = grade == "21" ? "selected" : "";
	
	if (organization == undefined) {
		organization = '';
	}
	
	if (username == undefined) {
		username = '';
	}

	var idx = (i+1);	
	// jquery 사용할 때 class 값을 사용하여서.. 조금 애매함...
	// $(this).parents("ul.listTableBody").remove(); 이런식으로 잡았음
	var user = $.i18n.prop('common.user');
	var middleManager = $.i18n.prop('common.middlemanager');
	var deviceManageInfo = $.i18n.prop('account.device.manageinfo');

	var html = "<ul class='listTableBody' id='listTableBody" + idx + "'>";
	html += "<li><input type='checkbox' name='listChkbox' id='listChk" + idx + "' user-src=\"" + userid 
		  + "\" onclick='checkEachEvent()'><label for='listChk" + idx + "' class='listChk'></label></li>";
	html += "<li><input type='text' name='inputId' id='inputId" + idx + "'class='userEditTbox' value='" + userid + "' readonly></li>";
	html += "<li><input type='text' name='inputName' id='inputName" + idx + "' value='" + username + "' class='userEditTbox' onKeyUp=\"changeName('" + idx + "', event)\"></li>";
	html += "<li><input type='text' name='inputOrgan' id='inputOrgan" + idx + "' value='" + organization + "' class='userEditTbox' onKeyUp=\"changeOrgan('" + idx + "', event)\"></li>";
	html += "<li><input type='text' name='inputPhone' id='inputPhone" + idx + "' value='" + mobile + "' class='userEditTbox' onKeyUp=\"changePhone('" + idx + "', event)\" onKeyDown=\"checkSubOnlyNumber(event, " + idx + ")\" maxlength='15'></li>";
	html += "<li>";
	html += "<select name='selAuth' id='selAuth" + idx + "' class='userEditSel' onChange=\"changeRole('" + idx + "')\">";
	html += "<option value='21' " + normalSelected + ">"+ user +"</option>";
	html += "<option value='20' " + middleSelected + ">"+ middleManager +"</option></select></li>";				
	html += " <li><a class='btnSensorNum' title="+ $.i18n.prop('account.device.change.management') +" onclick=manageDeviceSensor('" + userid + "')>" + countNum + "</a></li>";
	html += " <li><a class='btnReset' id=\"btnReset" + idx + "\" onclick=resetPassWord('" + userid + "') title="+ $.i18n.prop('account.passwd.reset.do') +">비밀번호 초기화</a></li>";			
	html += "</ui>";
	
	return html;				
}

//숫자만 입력 가능 함수
function checkSubOnlyNumber(event, idx) {
		
	// 휴대전화 형식에 맞지 않음 표시와 모든항목을 모두 기입해 주세요 표시 중 한가지만 입력 되게 하기 위해서...
	//var phoneChkTxt = $("#phoneChkTxt").css("display");
	
	/*// 휴대전화 형식에 맞지 않음 경우..
	if (!mobilePhoneCheck($("#inputPhone" + idx).val()) && phoneChkTxt == "none") {
		writeStatusRedMessage("형식 맞춰줘");
	} */
		
	if (event.which && (event.which < 48 || event.which > 57)
                    && event.which != 8 
                    && (event.which < 95 || event.which > 105)) {
		
        event.preventDefault();
	} 
}

// 숫자만 입력 가능 함수
function checkOnlyNumber(event) {
		
	if (event.which && (event.which < 48 || event.which > 57)
                    && event.which != 8 
                    && (event.which < 95 || event.which > 105)) {
		
        event.preventDefault();
	} 
}

// 상태 메세지 작성
function writeStatusMessage(value) {
	switchStatusMessage();
	$("#popupInnerEditResult").append(
			$("#popupEditResult").html("<i></i>" + value)
	);	
	$("#popupLayerEditResult").fadeIn(500).fadeOut(6000);	
}

function writeStatusRedMessage(value) {
	switchStatusRedMessage();
	$("#popupInnerEditResult").append(
			$("#popupEditResultRed").html("<i></i>" + value)
	);	
	$("#popupLayerEditResult").fadeIn(500).fadeOut(6000);	
}

function switchStatusMessage() {
	$("#popupEditResultRed").css("display", "none");
	$("#popupEditResult").css("display", "block");
}

function switchStatusRedMessage() {
	$("#popupEditResultRed").css("display", "block");
	$("#popupEditResult").css("display", "none");
}
