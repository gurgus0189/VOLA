ns("common") ;
var respParser = common.ajax.responseParser;      

$(function() {

	$("#popupMemberSecession").hide();
	$("#popupBgLayer").hide();	
	
	$("#memberSecessionId").css("color","#fff");
	
	init();
	
	$("#btnAlarmCheck").on("click", function() {
		var updatealarmalertupdate = $.i18n.prop('account.updatealarm.alertupdate');
		var errorComment = $.i18n.prop('common.error');
		
		if (confirm(updatealarmalertupdate)) {
		    var checkedYn = $("#listChk1").prop("checked");		
		    var syscheckedYn = $("#listChk2").prop("checked");
		    var userid = $("#userid").val();
		    var localecd2 = $("#languageSel_push").val();
		    
		    checkedYn = (checkedYn == true) ? "1" : "0";
		    syscheckedYn = (syscheckedYn == true) ? "1" : "0";
		    
		    console.log(checkedYn);
		    console.log(syscheckedYn);
		    
		    $.ajax({
				type : "post",
				url : "/openapi/account/updateuser",
				data : {
					"userid" : userid,
					"pushyn" : checkedYn,
					"pushsysyn" : syscheckedYn,
					"localecd" : localecd2,
				},
				success : function(result) {
					var respObj = respParser.parse(result);
					var returnCode = result.returnCode;
					
					var updatealarmupdate = $.i18n.prop('account.updatealarm.update');
			    	if (respObj.isSuccess()) {		
			    		alert(updatealarmupdate);
			    	} else {
			    		alert(returnCode +" "+ errorComment);
			    	}
				}
			});
		    
		}
	});
	
	$("#btnPwCheck").on("click", function() {
		var inputPw = $("#inputPw").val(); 	
		var inputNewPw = $("#inputNewPw").val();
		var inputCnfPw = $("#inputCnfPw").val();	
		var userid = $("#userid").val();
		var errorComment = $.i18n.prop('common.error');
		
		var demoID = $(".idViewTxt span").text();
		
		if(demoID == "demo@vitcon.co.kr") {
			alert($.i18n.prop('account.passwd.demo'));
			return;
		}
		
		$.ajax({
			type : "post",
			url : "/openapi/account/getuserinfo",
			data : {
				"userid" : userid,
				"passwd" : inputPw
			},
			success : function(result) {
				var respObj = respParser.parse(result);					
				var returnCode = result.returnCode;

				if (respObj.isSuccess()) {		    	
		    		if (result.data == "1") { // 회원정보가 맞게 들어온 경우	
		    			
		    			if (!validationCheck(inputPw, inputCnfPw, inputNewPw)) { // 유효성 체크.. 맞지 않는 경우 함수 종료시킴
		    				return;
		    			}
		    			
		    			changeNewPassword(userid, inputNewPw);	// 비밀번호 변경 함수	    			
		    		} else {
		    			var passwdwronginput = $.i18n.prop('account.passwd.wronginput');
		    			alert(passwdwronginput);
		    		}	    		
		    	} else {
		    		alert(errorComment);
		    	}
			}		
		});	
	}); 
	
	$("#memberSecessionCheck").on("click", function() {
		
		var memberSecessionId = $("#memberSecessionId").val();
		
		if(memberSecessionId == "demo@vitcon.co.kr") {
			alert($.i18n.prop('account.member.secession.impossible'));
			return;   
		}
		
		if(memberSecessionId == "" || memberSecessionId == null || memberSecessionId == undefined) {
			alert($.i18n.prop('account.member.secession.item'));
			return;
		}
		
		deviceidCheck(memberSecessionId);	
		
	});
	
});

function init() {		
	var errorComment = $.i18n.prop('common.error');
	
	 $.ajax({
			type : "post",
			url : "/openapi/account/getuser",		
			success : function(result) {
				var respObj = respParser.parse(result);
				var returnCode = result.returnCode;
				
		    	if (respObj.isSuccess()) {		    		
		    		var data = result.data;		  
		    		var pushyn = data.pushyn;
		    		var pushsysyn = data.pushsysyn;
		    		var localecd2 = data.localecd;
		    		var registrationtype = data.registrationtype;		    				    		
		    		isAccountVitConUser(registrationtype); // 해당 유저가 VITCON 유저인지 포탈사이트로 가입한 유저인지 체크
		    				    						    	
		    		pushyn = (pushyn == "1") ? true : false;	
		    		pushsysyn = (pushsysyn == "1") ? true : false;
		    		
		    		$("#listChk1").attr("checked", pushyn);
		    		$("#listChk2").attr("checked", pushsysyn);
		    		$("#languageSel_push").val(localecd2).prop("selected", true);
		    		
		    	} else {
		    		alert(returnCode +" "+ errorComment);
		    	}
			}
		});
}

function isAccountVitConUser(registrationtype) {
			
	if (registrationtype != "vitcon") {		
		$("#setupAlarmTxt").css("display", "block");
		$("input[type=password]").attr("disabled", true);
		$(".pwChangeTbox").attr("class", "pwChangeTbox btnDisable");
		
		$("#btnPwCheck").attr("disabled", true);
		$("#btnPwCheck").attr("class", "btnCheck btnDisable");
		
	} else {
		$("#setupAlarmTxt").css("display", "none");
	}
	
}

function enterkey(){
    if (window.event.keyCode==13) {                     
    	$("#btnPwCheck").click();
    }
}	  

// 비밀번호를 변경하는 함수
function changeNewPassword(userid, inputNewPw) {
	var errorComment = $.i18n.prop('common.error');
	
	 $.ajax({
			type : "post",
			url : "/openapi/account/updateuser",
			data : {
				"userid" : userid,
				"passwd" : inputNewPw,
			},
			success : function(result) {
				var respObj = respParser.parse(result);
				var returnCode = result.returnCode;
				
		    	if (respObj.isSuccess()) {		
		    		var passwdupdate = $.i18n.prop('account.passwd.update');
		    		alert(passwdupdate);
		    		location.reload();
		    	} else {
		    		alert(returnCode +" "+ errorComment);
		    	}
			}
		});
}

// 유효성 체크 함수
function validationCheck(inputPw, inputCnfPw, inputNewPw) {
	
	if (isEmpty(inputPw)) {		
		var passwdinput = $.i18n.prop('account.passwd.input');
		alert(passwdinput);
		$("#inputId").focus();
		return false;
	}		
	
	if (inputNewPw.length < 7) {
	    var passwdlimit = $.i18n.prop('account.passwd.limit');
	    alert(passwdlimit);
	    $("#inputNewPw").focus();
	    return false;
	}
	
	if (inputCnfPw != inputNewPw) {
		var passwddiff = $.i18n.prop('account.passwd.diff');
	    alert(passwddiff);
	    $("#inputCnfPw").focus();
	  return false;
	}
	  	  	
	return true;	
}	

// 빈값 체크 함수
function isEmpty(value) {	
	if (value == null || value.trim() == "") {
		return true;
	}	
}


function deviceidCheck(memberSecessionId) {
	
	var url = "/openapi/account/deviceidCheck";
	var errorComment = $.i18n.prop('common.error');
	
	var parameter = { 
		userid: memberSecessionId
	};
	
	$.ajax({
		url : url,
		data : parameter,
		type : "post",
		success : function(result) {
			
			var respObj = respParser.parse(result);
			var returnCode = result.returnCode;
			
	    	if (respObj.isSuccess()) {		
	    		
	    		var deviceidCount = respObj.responseObj.data;
	    		
	    		//console.log("디바이스 갯수 : " + deviceidCount);
	    		
	    		if(deviceidCount != 0) {
	    			alert($.i18n.prop('account.member.secession.devicecount') + "\n" + $.i18n.prop('account.member.secession.devicedelete'));
	    			return;
	    		} 
	    		
	    		subAccountCheck(memberSecessionId);
	    		
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
			
		}
	});
	
}

function subAccountCheck(memberSecessionId) {
	
	var url = "/openapi/account/subAccountCheck";
	var errorComment = $.i18n.prop('common.error');
	
	var parameter = { 
		userid: memberSecessionId
	};
	
	$.ajax({
		url : url,
		data : parameter,
		type : "post",
		success : function(result) {
			
			var respObj = respParser.parse(result);
			var returnCode = result.returnCode;
			
	    	if (respObj.isSuccess()) {		
	    		
	    		var accountCount = respObj.responseObj.data;
	    		
	    		//console.log("하위계정 갯수 : " + accountCount);
	    		
	    		
	    		if(accountCount != 0) {
	    			alert($.i18n.prop('account.member.secession.subaccount') + "\n" + $.i18n.prop('account.member.secession.subaccountdelete'));
	    			return;
	    		} 
	    		
	    		$("#popupMemberSecession").show();
	    		$("#popupBgLayer").show();
	    		
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
			
		}
	});
}

function useridDelete(memberSecessionId) {
	
	var url = "/openapi/account/useridDelete";
	var errorComment = $.i18n.prop('common.error');
	
	var parameter = { 
		userid: memberSecessionId
	};
	
	$.ajax({
		url : url,
		data : parameter,
		type : "post",
		success : function(result) {
			
			var respObj = respParser.parse(result);
			var returnCode = result.returnCode;
			
	    	if (respObj.isSuccess()) {		
	    		
	    		//console.log(respObj);
	    		
	    		alert($.i18n.prop('account.member.secession.sucess'));
	    		
	    		location.href = "/auth/logout.do";
	    		
	    		
	    		//userappDelete(memberSecessionId);
	    		
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
			
		}
	});
	
}


function certificationNumber() {
	
	alert($.i18n.prop('account.member.secession.issuancesucess'));
	
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for( var i=0; i < 5; i++ )
        text += possible.charAt(Math.floor(Math.random() * possible.length));

    //console.log(text);
    
    $("#input_certificationNumber").val(text);
    
}

function applyPopupLayer() {
	
	var certificationNumber = $("#input_certificationNumber").val();
	var input_value = $("#input_popMember2Txtbox").val();
	var memberSecessionId = $("#memberSecessionId").val();
	
	if(certificationNumber == "") {
		alert($.i18n.prop('account.member.secession.issuance'));
		return;
	}

	//console.log(certificationNumber);
	//console.log(input_value);
	
	if(certificationNumber == input_value) {
		var alertremove = $.i18n.prop('account.member.secession.alertmessage_1') + "\n" + $.i18n.prop('account.member.secession.alertmessage_2');
		
		//확인창
		if (!confirm(alertremove)) {
			return false;
		}
		
		useridDelete(memberSecessionId);
		
	} else {
		alert($.i18n.prop('account.member.secession.check'));
		$("#input_popMember2Txtbox").val("");
		return;
	}
	
}

function closePopupLayer() {
	
	$("#popupMemberSecession").hide();
	$("#popupBgLayer").hide();	

}







