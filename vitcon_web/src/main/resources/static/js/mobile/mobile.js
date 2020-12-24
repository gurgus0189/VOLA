var redirurl = null;

//포털 사용자로 로그인이 성공했을 때, app 에 정보 전송
function sendLoginPortal(portalid, registrationtype) {
	var msg = {};
	msg["portalid"] = portalid;
	msg["registrationtype"] = registrationtype;
	msg["autologin"] = $("#logSaveChk").prop("checked") ? "Y" : "N";
	msg["logindvs"] = "portal";
	msg["localecd"] = $("#languageSel").val(); 
	
	var stringmsg = JSON.stringify(msg);

	if (typeof appWebview !== "undefined") {
		appWebview.saveLoginData(stringmsg);
	} else if (typeof webkit !== "undefined") {
		// ios
		webkit.messageHandlers.saveLoginData.postMessage(stringmsg);
	}
}

// 빛컨 사용자로 로그인이 성공했을 때, app 에 정보 전송
function sendLoginVitcon(userid, passwd) {
	var msg = {};
	msg["userid"] = userid;
	msg["passwd"] = passwd;
	msg["autologin"] = $("#logSaveChk").prop("checked") ? "Y" : "N";
	msg["logindvs"] = "normal";
	msg["localecd"] = $("#languageSel").val(); 
	
	var stringmsg = JSON.stringify(msg);
	
	if (typeof appWebview !== "undefined") {
		// android
		appWebview.saveLoginData(stringmsg);
	} else if (typeof webkit !== "undefined") {
		// ios
		webkit.messageHandlers.saveLoginData.postMessage(stringmsg);
	}
}

// 로그인이 성공했을 때(빛컨 혹은 포털) 공통으로 사용하는 메소드로, mobile 에게 성공여부를 알려주고, 실패시 login 페이지로 이동
function mobileUserLogin(result) {
	if (result.returnCode != 200) {
		location.href = '/auth/login.do';
		return;
	}		
	
	console.log("success login >>> ");
	// TODO : 모바일로 로그인에 성공했다고 알려주는 코딩 필요
}

/* 빛컨 로그인 함수(공통) */
function userLoginVitcon(userid, passwd, func) {
    var isAuto = userid.split('|')[1];
    if (typeof isAuto != "undefined") {
        userid = userid.split('|')[0];
        $("#inputId").val(userid);
        $("#logSaveChk").prop("checked", true);
    }

	$.ajax({
		type : "post",
		url : "/openapi/auth/login",
		data : {
		    "userid" : userid,
			"passwd" : passwd,
		},
		success : function(result) {			
			func(result, userid, passwd);								
		},
		error: function (error) {
		  	console.log("error : " + error);
		}
	});
}

/*
 * 포털을 통하여 로그인, 성공시 app 에 성공 데이터 전달
 */
function userLoginPortal(portalid, registrationtype, func) {	
	if (typeof portalid == 'number')
		portalid = portalid.toString();
		
    var isAuto = portalid.split('|')[1];
    if (typeof isAuto != "undefined") {
        portalid = portalid.split('|')[0];
        $("#logSaveChk").prop("checked", true);
    }

    $.ajax({
        type:"post",
        url: "/openapi/auth/login",
        data: {
	        "portalid": portalid,       	
	        "registrationtype": registrationtype,
        },
        success : function(result) {        	
	        func(result, portalid, registrationtype);
        },
        error: function (error) {
            console.log("error : " + error);
        }
    });
}

$(function() {	
	if (typeof appWebview == "undefined" && typeof webkit == "undefined") {
		// 모바일 웹뷰가 아닌 경우 
		if ($("#mAddDevice") != 'undefined')
			$("#mAddDevice").hide();		
	}
});