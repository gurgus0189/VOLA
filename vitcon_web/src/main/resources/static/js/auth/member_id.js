ns("common") ;
var respParser = common.ajax.responseParser;

$(function(){
	$("#loginLink").on("click", function() {
		var locale = getCookie('localecd');
		location.href = "/auth/login.do?language="+ locale;
	});
	
	$("#joinLink").on("click", function() {
		location.href = "/auth/member_join.do";
	});
	
	$("#findPasswordLink").on("click", function() {
		location.href = "/auth/member_pw.do";
	});
	
	$("#btnPopNormal").on("click", function() {
		var locale = getCookie('localecd');
		location.href = "/auth/login.do?language="+ locale;		
	});
	
	// 휴대폰 번호 숫자만 입력하도록 처리
	$("#inputPhone").keydown(function(event) {
		if (event.which && (event.which <= 47 || event.which >= 58)
	            && event.which != 8 && event.which != 9
	            && (event.which <= 95 || event.which >= 111)) {		
	        event.preventDefault();	        
		}
	});
	
	$("#btnPopConfirm").on("click", function() {		
		
		var inputName = $("#inputName").val();
		var inputPhone = $("#inputPhone").val();		
		var errorComment = $.i18n.prop('common.error');
		
		if (!validationCheck(inputName, inputPhone)) {				
			return;
		}		
		
		$.ajax({
			type : "post",
			url : "/openapi/account/getuserid",
			data : {
				"username" : inputName,
				"mobile" : inputPhone
			},
			success: function(result) {
				var respObj = respParser.parse(result);
				var returnCode = result.returnCode;
				
		    	if (respObj.isSuccess()) {
		    		var data = result.data;
					
					if (data == null) {
						var noAccountMsg = $.i18n.prop('common.noaccount');
						writeLoginWrong(noAccountMsg);
						return;
					} 
					// 사용자 정보가 있는 경우 표시태그에 아이디값을 넣어준다.
					writeLoginResult(data.userid);	
		    	} else {
		    		alert(returnCode +" "+ errorComment);
		    		//alert("에러 입니다."); 
		    	}
								
			},
			error: function (error) {
				var errorComment = $.i18n.prop('common.error');
				alert(errorComment);
				//console.log("error : " + error);
			}
		
		});
	});
	
});

//오류 메시지 표시 ON
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

//휴대전화 양식 정규표현식
function mobilePhoneCheck(mobile) {
        var regExp = /^\d{3}\d{3,4}\d{4,}$/;
        return (mobile != '' && mobile != 'undefined' && regExp.test(mobile));
}

function validationCheck(inputName, inputPhone) {
		
	if (isEmpty(inputName)) {
		var inputNameAlert = $.i18n.prop('common.inputname');
		writeLoginWrong(inputNameAlert);
		$("#inputName").focus();
		return false;
	}
	
	if (isEmpty(inputPhone)) {		
		var inputMobileAlert = $.i18n.prop('login.findid.inputmobile');
		writeLoginWrong(inputMobileAlert);
		$("#inputPhone").focus();
		return false;
	}
	
    if (!mobilePhoneCheck(inputPhone)) {
    	var mobileFormAlert = $.i18n.prop('login.findid.mobileform');
        writeLoginWrong(mobileFormAlert);
        $("#inputPhone").focus();
        return false;
    }
	
	return true;
}

//숫자만 입력 가능 함수
function checkOnlyNumber(event) {
	if (event.which && (event.which <= 47 || event.which >= 58)
                                && event.which != 8 && event.which != 9
                                && (event.which <= 95 || event.which >= 111)) {                      
        event.preventDefault();        
    }  
}

function isEmpty(value) {		
	if (value == null || value.trim() == "") {
		return true;
	}	
}

function enterkey(){          
    if (window.event.keyCode==13){ 
             // keyCode가 13 인경우라면 들어와서 fnLogin을 실행하겠다.                      
    	$("#btnPopConfirm").click();                         
    }
}