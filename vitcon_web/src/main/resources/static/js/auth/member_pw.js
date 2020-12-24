ns("common") ;
var respParser = common.ajax.responseParser;

var flag = true;
$ (function(){
	$("#loginLink").on("click", function() {
		var locale = getCookie('localecd');
		location.href = "/auth/login.do?language="+ locale;
	});
	
	$("#joinLink").on("click", function() {
		location.href = "/auth/member_join.do";
	});
	
	$("#findIdLink").on("click", function() {
		location.href = "/auth/member_id.do";
	});
	
	$("#btnPopNormal").on("click", function() {
		var locale = getCookie('localecd');
		location.href = "/auth/login.do?language="+ locale;
	});
	
	$("#btnPopConfirm").on("click", function() {
		
		if (flag == false) {
			return;
		}
		
		var inputId = $("#inputId").val();
		
		if (!validationCheck(inputId)) {				
			return;
		}		
		
		// 메세지 스위치를 닫는다.
		switchLoginOff();

		flag = false;
	    $.ajax({
	        type :"post",
	        url: "/openapi/account/getpasswd",
	        data : {
	        	"userid" : inputId
	        },
	        success : function(result) {
	    		flag = true;
				var respObj = respParser.parse(result);
				var returnCode = result.returnCode;
				
		    	if (respObj.isSuccess()) {
					// 사용자 정보가 있는 경우 표시태그에 아이디값을 넣어준다.
		    		var sendMsg = $.i18n.prop('login.findpw.sendmsg');
					writeLoginResult(sendMsg);	
		    	} else {
		    		var noAccountMsg = $.i18n.prop('common.noaccount');
		    		writeLoginWrong(noAccountMsg);
					return;
		    	}													  
	        },
	        error: function (error) {
	    		flag = true;        	
	    		var errorComment = $.i18n.prop('common.error');
				alert(errorComment);
	        	//console.log("error : " + error);
			}
	    });
	    	    	    
	});
});

// 이메일 형식 체크
function emailCheck(inputId) {    
    var regex=/^[a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z\.]*$/i;
    return (inputId != '' && inputId != 'undefined' && regex.test(inputId));
}

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

//컬럼 가능 메세지 표시 ON
function switchLoginOff() {
	$("#loginResult").css("display", "none");
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

function validationCheck(inputId) {
			
	if (isEmpty(inputId)) {		
		var mailCheckMsg = $.i18n.prop('common.inputemail');
		writeLoginWrong(mailCheckMsg);
		$("#inputId").focus();
		return false;
	}
	
    if (!emailCheck(inputId)) {			    	
    	var mailFormCheckMsg = $.i18n.prop('login.findpw.emailform'); 
    	writeLoginWrong(mailFormCheckMsg);
    	return false;
    }
		
	return true;
}

function isEmpty(value) {		
	if (value == null || value.trim() == "") {
		return true;
	}	
}

function enterkey(){
    if (window.event.keyCode==13) {    	
        $("#btnPopConfirm").click();
    }
}	  	