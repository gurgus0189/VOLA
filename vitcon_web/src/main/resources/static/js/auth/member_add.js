ns("common") ;
var respParser = common.ajax.responseParser;

window.onload = function() {    
	// 포탈타입 값이 존재한다면
	var registrationtype = $("#registrationtype").val();
	
	if (!isEmpty(registrationtype)) {		
	    $("#joinPw").css("display", "none");
	    $("#joinPwCnf").css("display", "none");
	} else {
		// 포탈사이트 계정으로 회원가입이 아닌경우 타입에 vitcon을 넣어주기로 하자.
		$("#registrationtype").prop("value", "vitcon");
	}
}

//Y가 체크한것 N이 체크가 안된것
$(function(){				
	window.dupCheckYn = "N"; // 체크 여부 
	
	$("#loginLink").on("click", function() {
		var locale = getCookie('localecd');
		location.href = "/auth/login.do?language="+ locale;
	});
	
	$("#findIdLink").on("click", function() {
		location.href = "/auth/member_id.do";
	});
	
	$("#findPasswordLink").on("click", function() {
		location.href = "/auth/member_pw.do";
	});
	
	$("#inputId").on("keyup", function(){		
		dupCheckYn = "N";
	});
	
	$("#btnIdChk").on("click", function() {
		var inputId = $("#inputId").val();		
		
		if (isEmpty(inputId)) {
			var emailCheckMsg = $.i18n.prop('common.inputemail');
			writeLoginWrong(emailCheckMsg);			
			return;
		}
		
		duplicateEmailCheck(inputId);		
	});
	
	// 휴대폰 번호 숫자만 입력하도록 처리
	$("#inputPhone").keydown(function(event) {
		if (event.which >= 48 && event.which <= 57) {
			return;
		}
		if (event.which == 8) {
			return;
		}
		if (event.which >= 96 && event.which <= 105) {
			return;
		}
		return false;
	});
	
	$("#btnPopNormal").on("click", function() {
		var inputCancel = $.i18n.prop('signup.cancelmsg');
		if (confirm(inputCancel)){
			var locale = getCookie('localecd');
			// location.href="/auth/login.do?language="+ locale;
			history.back(-1);
		}
	});
		
	$("#btnPopConfirm").on("click", function() {		
		var inputId = $("#inputId").val();
		var inputPw = $("#inputPw").val();
		var inputPwConfirm = $("#inputPwConfirm").val();
		var inputName = $("#inputName").val();
		var inputPhone = $("#inputPhone").val();
		var selectAlarm = $("#selectAlarm").val();		
		var selectLang = $("#selectLang").val();
		var portalid = $("#portalid").val();
		var registrationtype = $("#registrationtype").val();
		var errorComment = $.i18n.prop('common.error');
		
		var d = new Date();
		//var startdate = d.getFullYear() + "-" + d.getMonth() + "-" + d.getDate() + " 00:00:00";
		//var enddate = d.getFullYear() + "-" + d.getMonth() + "-" + d.getDate() + " 23:59:59";
		
		if (dupCheckYn != "Y") {			
			var idConfirmMsg = $.i18n.prop('signup.idconfirmmsg');
			writeLoginWrong(idConfirmMsg); // 중복 확인 메세지의 경우 			
			return;
		}
		
		// 유효성 체크
		if (!validationCheck(inputId, inputPw, inputPwConfirm, inputName, inputPhone)) {
			return;			
		}	
		// 이메일 중복 검사						
		$.ajax({
			type : "post",
			url : "/openapi/account/insertuser",
			data : {
			    "userid" : inputId,
				"passwd" : inputPw,
				"username" :inputName,
				"mobile" : inputPhone,
				"pushyn" : selectAlarm,
				"localecd" :selectLang,
				"portalid" : portalid,
				"registrationtype" : registrationtype				
			},
			success : function(result) {				
				var respObj = respParser.parse(result);				
				var returnCode = result.returnCode;
				
		    	if (respObj.isSuccess()) {		    		
					userLogin(inputId, inputPw, registrationtype, portalid);
		    	} else {
					if (result.returnCode == "801") {
						var mobileDuplicated = $.i18n.prop('mobile.duplicate');
						writeLoginWrong(mobileDuplicated);
						return;
					}
					
					alert(returnCode +" "+ errorComment);
		    		return;
		    	}															
			},
			error: function (error) {
				var errorComment = $.i18n.prop('common.error');
				alert(errorComment);
			}
		});
				
	});			 			 			
});

function userLogin(inputId, inputPw, registrationtype, portalid) {	
	var errorComment = $.i18n.prop('common.error');

	$.ajax({
		type : "post",
		url : "/openapi/auth/login",
		data : {
		    "userid" : inputId,
			"passwd" : inputPw,
			"portalid" : portalid,
			"registrationtype" : registrationtype,
		},
		success : function(result) {
			var respObj = respParser.parse(result);
			var returnCode = result.returnCode;

	    	if (respObj.isSuccess()) {
	    	    location.href="/dashboard/listview.do";
	    	} else {
				// 실패
				$("#inputPw").val('');
				$("#inputPw").focus();
				
				var errmsg = $.i18n.prop('common.error.service');				
				writeLoginWrong(errmsg);
				return;
	    	}		
		},
		error: function (error) {
			var errorComment = $.i18n.prop('common.error');
			alert(errorComment);
			//console.log("error : " + error);
		}
	});
}

// 휴대전화 양식 정규표현식
function mobilePhoneCheck(mobile) {
	var regExp = /^\d{3}\d{3,4}\d{4,}$/;
	return (mobile != '' && mobile != 'undefined' && regExp.test(mobile));
}

function emailCheck(inputId) {    
    var regex=/^[a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z\.]*$/i;
    return (inputId != '' && inputId != 'undefined' && regex.test(inputId));
}

function duplicateEmailCheck(inputId) {	
	var errorComment = $.i18n.prop('common.error');
	
    if (!emailCheck(inputId)) {
		var emailFormConfirmMsg = $.i18n.prop('signup.emailform');
    	writeLoginWrong(emailFormConfirmMsg);
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
			
			if (!respObj.isSuccess()) {
				// 성공이 아닐 경우 서비스 에러 출력
				var errService = $.i18n.prop('common.error.service');
				writeLoginResult(errService);
				return;
			}
			
    		if (result.data == '1') {
				//중복된 이메일일 경우
    			var emailExist = $.i18n.prop('signup.idexist');
				writeLoginWrong(emailExist);
				return;
			}    		
    		
			dupCheckYn = "Y";
			var idNone = $.i18n.prop('signup.idnone');
			writeLoginResult(idNone);
		},
		error: function (error) {
			alert(errorComment);
			//console.log("error : " + error);
		}
	});
}

// 오류 메시지 표시 ON
function switchLoginWrong() {
	$("#loginWrong").css("display", "block");
	$("#loginResult").css("display", "none");	
}

// 컬럼 가능 메세지 표시 ON
function switchLoginResult() {
	$("#loginResult").css("display", "block");
	$("#loginWrong").css("display", "none");	
}

// 오류 메세지 작성
function writeLoginWrong(value) {	
	switchLoginWrong();
	$("#loginWrong>span").html(value);
}

// 컬럼 가능 메세지 작성
function writeLoginResult(value) {	
	switchLoginResult();
	$("#loginResult>span>em").html(value);
}

function validationCheck(inputId, inputPw, inputPwConfirm, inputName, inputPhone) {
	
	if (isEmpty(inputId)) {
		var inputIDMsg = $.i18n.prop('signup.inputid');
		writeLoginWrong(inputIDMsg);
		$("#inputId").focus();
		return false;
	}
 
	// 일반 회원 가입의 경우 여기서 체크 한다.  
	if ($("#registrationtype").val() == "vitcon") {		
	  if (isEmpty(inputPw)) {
		var inputPwMsg = $.i18n.prop('signup.inputpasswd');
	  	writeLoginWrong(inputPwMsg);
	  	$("#inputPw").focus();
	  	return false;
	  }

	  if (inputPw.length < 7) {
		var passwdtip = $.i18n.prop('signup.passwdtip');
	    writeLoginWrong(passwdtip);
	    $("#inputPw").focus();
	    return false;
	  }

	  if (inputPw.length > 30) {
		var passwdForm = $.i18n.prop('signup.passwdform');
	    writeLoginWrong(passwdForm);
	    $("#inputPw").focus();
	    return false;
	  }

	  if (inputPw != inputPwConfirm) {
		var passwdDiff = $.i18n.prop('signup.passwddiff');
	    writeLoginWrong(passwdDiff);
	    $("#inputPwConfirm").focus();
	    return false;
	  }  	  
	} 
	
	//공통 체크 사항
	if (isEmpty(inputName)) {
		var inputNameMsg = $.i18n.prop('signup.inputname');
		writeLoginWrong(inputNameMsg);
		$("#inputName").focus();
		return false;
	}
	
	if (isEmpty(inputPhone)) {
		var inputMobileMsg = $.i18n.prop('signup.inputmobile');
		writeLoginWrong(inputMobileMsg);
		$("#inputPhone").focus();
		return false;
	}

	if (!mobilePhoneCheck(inputPhone)) {
		var mobileForm = $.i18n.prop('signup.mobileform');
		writeLoginWrong(mobileForm);
		$("#inputPhone").focus();
		return false;
	}
		
	return true;	
}

/*// 숫자만 타이핑 하도록 처리하는 함수
function checkNumberPress(event) {	
	if (event.which && (event.which <= 47 || event.which >= 58)
            && event.which != 8 && event.which != 9
            && (event.which <= 95 || event.which >= 111)) {		
		// $("#inputPhone").empty();
        event.preventDefault();        
	}   
}*/

function isEmpty(value) {	
	if (value == null || value.trim() == "") {
		return true;
	}	
}