ns("common");
var respParser = common.ajax.responseParser;      

var naver_id_login;
var state;

$(function() {
	
	// 웹인지 앱인지 체크해서 아이지저장 체크박스 표시하는 함수
	isWebOrApp();
	
	var language = getCookie("localecd");
	
		
	
	/*
	if(language==null){
		
		//현재 브라우저 언어 가져오기
		language = navigator.languages && navigator.languages[0] || // Chrome / Firefox
        navigator.language ||   // All browsers
        navigator.userLanguage; // IE <= 10
		
		if(language.length >=2){
			language = language.substring(0,2);
		}
		else{
			//나머지 글로벌 언어는 영어로 통일 
			language = en;
		}
		alert ("The language is: " + language);
		
		//없는 쿠키 생성하기
		saveCookie("localecd", language, 365);
	}*/
	
	$("#languageSel > option[value='" + language + "']").prop("selected", true);
	
	$("#btnLogin").on("click", userLogin);
	
	$("#joinLink").on("click", function() {
		location.href = "/auth/member_join.do";		
	});
	
	$("#findIdLink").on("click", function() {
		location.href = "/auth/member_id.do";
	});
	
	$("#findPasswordLink").on("click", function() {
		location.href = "/auth/member_pw.do";
	});	
	
	
/*	//네이버 로그인 API 초기화	
	var host = (window.location.hostname==undefined || window.location.hostname=="v-ola.co.kr")?"www.v-ola.co.kr":window.location.hostname;
	var protocol = (window.location.protocol==undefined)?"https:":window.location.protocol;
	var callback_domain = protocol + "//" + host;
		
	naver_id_login = new naver_id_login("f9gkkvzQLYh_ytFVLqr7", callback_domain + "/portal/naverLoginCallBack.do");																	 
	state = naver_id_login.getUniqState();	
	naver_id_login.setDomain(callback_domain);
	naver_id_login.setState(state);
	naver_id_login.setPopup(); // <-- 이게 팝업을 열지 말지를 결정하는 것 !! 
	naver_id_login.init_naver_id_login();*/
	
});

/**
 * 웹인지 앱이지 체크해서 자동로그인 표시하는 함수
 * @returns
 */
function isWebOrApp() {	
	
	var sortValue = $("#app").val();
	// app 값이 존재한다면 자동로그인 박스를 보여주는 걸로 해야함
	if (isEmpty(sortValue)) {
		var idSave = $.i18n.prop('login.form.idsave');
		var cookieUserid = getUseridCookie("userid");
		
		$("#loginInputSave>label").html("<i></i>" + idSave);
		
		if (!isEmpty(cookieUserid)) {		
			// 비어있지 않다면 .. 쿠키가 존재한다면 ??		
			$("#inputId").val(cookieUserid);
			$("#logSaveChk").prop("checked", true);
		}
	}
	
}
/* 쿠키 가져오는 함수*/
function getCookie(cookie_name) {
	  var x, y;
	  var val = document.cookie.split(';');

	  for (var i = 0; i < val.length; i++) {
	    x = val[i].substr(0, val[i].indexOf('='));
	    y = val[i].substr(val[i].indexOf('=') + 1);
	    x = x.replace(/^\s+|\s+$/g, ''); // 앞과 뒤의 공백 제거하기
	    if (x == cookie_name) {
	      return unescape(y); // unescape로 디코딩 후 값 리턴
	    }
	  }
	}

 /*
  회원정보 가져오는 함수
 */
function getPortalUserInfo(portalid, email, name, registrationtype) {
	var errorComment = $.i18n.prop('common.error');

    $.ajax({
	    type : "post",
		url : "/openapi/account/getportalid",
		data : {
		    "portalid" : portalid,
		    "registrationtype" : registrationtype
		},
		success : function(result) {
			var respObj = respParser.parse(result);
			var returnCode = result.returnCode;
			
	    	if (respObj.isSuccess()) {	    		
				var data = result.data;
				
				if (data == null || data == "") {
				    // 계정이 없는 경우 회원가입 처리한다.										
					portalUserJoin(portalid, email, name, registrationtype);
					return;
				} else {
					// 계정이 존재하는 경우 로그인 처리 한다.										
					portalUserLogin(portalid, registrationtype);
					return;
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

 /*
 로그인 처리하는 함수(포탈사이트를 통해서 로그인하는 경우)
 */
function portalUserLogin(portalid, registrationtype) {	
	userLoginPortal(portalid, registrationtype, loginResultPortal);
}
 
/* // 구글 로그인 링크 태그 잡는 함수
function loginWithGoogle() {
	// 태그를 조금 모호하게 잡았는데 ... 이게 나중에 어떻게 될지 모르겠다.
   $("div#google-login-btn span.abcRioButtonContents span:first-child").click();	 
} */

/*
 * 구글 회원정보 얻어오는 함수
 */
/* function onSignIn(googleUser) {
   var profile = googleUser.getBasicProfile();
	
   var portalid = profile.getId(); // Do not send to your backend! Use an ID token instead.
   var name = profile.getName();    
   var email = profile.getEmail();
   var registrationtype = "google";
   
   getPortalUserInfo(portalid, email, name, registrationtype);
} */

function changeLanguageUser() {
	
	
	if (typeof redirurl != "undefined" && redirurl != null) {
		location.href = redirurl;
	} else {
		location.href = "/dashboard/listview.do";
	}	
	
	/*var localecd = $("#languageSel").val();
	var errorComment = $.i18n.prop('common.error');

	$.ajax({
		type : "post",
		url : "/openapi/account/changelanguage",
		data : {
		    "localecd" : localecd
		},
		success : function(result) {			
			var respObj = respParser.parse(result);
			var returnCode = result.returnCode;

			console.log("체인지랭기지유저");
			alert("테스트스스스");
			if (respObj.isSuccess()) {	    
				//$.cookie("localecd", localecd);
				if (typeof redirurl != "undefined" && redirurl != null) {
					location.href = redirurl;
				} else {
					location.href = "/dashboard/listview.do";
				}	
	    	} else {
	    		alert(returnCode +" "+ errorComment);
	    	}
		},
		error: function (error) {
			alert(errorComment);
		}
	});*/
}

 /*
 회원 가입 폼으로 이동하는 함수
 */
function portalUserJoin(portalid, email, name, registrationtype) {	 
    $("#portalid").val(portalid);
    $("#email").val(email);
    $("#name").val(name);
    $("#registrationtype").val(registrationtype);   
    
    document.portalform.submit();
}

function changeLanguageStatus() {
	// 콤보 박스 유지 하는건데 제대로 구현 하지 못함 ...
}

function changeLanguage() {
	var selectValue = $("#languageSel").val();
    
	if (typeof appWebview !== "undefined") {
		appWebview.saveLocale(selectValue);
	} else if (typeof webkit !== "undefined") {
		// ios
		webkit.messageHandlers.saveLocale.postMessage(selectValue);
	}
	
	location.href="login.do?language=" + selectValue;
}

function isServiceTime(result) {
	if (result.returnCode == 802)
		return false;
	
	return true;
}

function isSaveUseridChecked(userid){
	if ($("#logSaveChk").is(":checked")) {		
		// 로그인 성공 , 체크박스가 체크 되어있을 경우 아이디 쿠키를 굽는다.
		saveLogin(userid);
		// 추가 2018-11-21 로그인시 토스트 메시지
		var x = document.getElementById("snackbar");
	    x.className = "show";
	    setTimeout(function(){ x.className = x.className.replace("로그인중입니다.", ""); }, 1000);
	} else {
		// 로그인에 성공하였지만 체크가 되어잇지 않은 경우 쿠키를 제거한다.
		saveCookie("userid", null, -1);
	}
}

function loginResultVitcon(result, userid, passwd) {
	
	if (result.returnCode == 200) {
		// 여기에서 자동로그인 여부를 체크하고 쿠키를 저장하면 될것 같아.. 
		isSaveUseridChecked(userid);	// 자동로그인 여부를 체크합니다.!!	
		sendLoginVitcon(userid, passwd);				
		changeLanguageUser();
		return;
	}
	
	/*if (!isServiceTime(result)) {
		var noService = $.i18n.prop('common.noservice');
		writeLoginWrong(noService);
		return;
	}*/
		
	$("#inputPw").val('');
	$("#inputPw").focus();
	var noAccountMsg = $.i18n.prop('common.noaccount');
	writeLoginWrong(noAccountMsg);
	return;
}

function loginResultPortal(result, portalid, registrationtype) {
	if (!isServiceTime(result)) {
		var noService = $.i18n.prop('common.noservice');
		writeLoginWrong(noService);
		return;
	}
	
	if (result.returnCode != 200) {		
		return;
	}
	
	sendLoginPortal(portalid, registrationtype);	    	
	changeLanguageUser();
}

function userLogin() {
	var inputId = $("#inputId").val();
	var inputPw = $("#inputPw").val();	
	// 자동로그인 여부 체크 해야함...
	if (!isEmptyCheck(inputId, inputPw)) {		
		return;
	}
	userLoginVitcon(inputId, inputPw, loginResultVitcon);
}

function isEmpty(value) {
	if (value == null || value.trim() == "") {
		// 값이 비어있는지 확인한다.(비어있다.)
		return true;
	}
	// 값이 비어있지 않다.
	return false;
}

/*
 * 빈값을 체크하는 함수
 */
function isEmptyCheck(inputId, inputPw) {
	
	if (inputId == null || inputId.trim() == "") {
		var idAlert = $.i18n.prop('signup.inputid');
		writeLoginWrong(idAlert);
		return false;
	}
	
	// 6자리 이상으로 입력
	if (inputPw == null || inputPw.trim() == "") {
		var pwAlert = $.i18n.prop('signup.inputpasswd');		
		writeLoginWrong(pwAlert);
		return false;
	}	
	
	return true;
}

/*
 * 엔터 키를 누를경우 로그인 될 수 있도록 하는 함수 
 */
function enterkey(){
    if (window.event.keyCode==13) {                     
      userLogin();                         
    }
}	  	

//오류 메시지 표시 ON
function switchLoginWrong() {	
	$("#loginWrong").css("display", "block");	
}

// 오류 메세지 작성
function writeLoginWrong(value) {
	$("#loginWrong>span").html(value);
	switchLoginWrong();	
}


/*//카카오 로그인 API 초기화
//url: '/v2/user/me', 이 url 마다 얻을 수 있는 정보가 다르다.. 
//https://developers.kakao.com/docs/js/demos/login 이 url 에 데모 더보기를 참조 하길
Kakao.init('5dfb3b876f2976570a7f9fe38bd33f14');
function loginWithKakao() {    		
  var registrationtype = "kakao";
  // 로그인 창을 띄웁니다.
  Kakao.Auth.login({
      success: function(authObj) {  	
      Kakao.API.request({
          url: '/v2/user/me',                     
          success: function(res) {             
          	getPortalUserInfo(res.id, res.kakao_account.email, res.properties.nickname, registrationtype);                    	                     	
           },
           fail: function(error) {
               alert(JSON.stringify(error));
           }
         });
   },
   fail: function(err) {
       alert(JSON.stringify(err));
   }
 });
};*/

function init() {
	// auth2라는 기능을 가져온것 이라고 판단하면 쉽겠다....
	 gapi.load("auth2", function() {		
		// 전역 변수 gauth 선언
		window.gauth = gapi.auth2.init({
			client_id : '1031800859012-j1db7ic5cgeo33qi2kbtjc0jg3igpk5r.apps.googleusercontent.com'
		})
				
		// GoogleAuth.then(oninit, onError);
		//then 작업이 끝난 다음에 ~!!
		gauth.then(function() {			
			//checkLoginStatus()
		}, function() {			
		});
		
	}); 
}

function loginWithGoogle() {
  if (!gauth.isSignedIn.get()) { // 로그인이 되어있지 않다면    	
	    gauth.signIn().then(
	        function(success) {
	        	googleLoginProcess();
	        },
	        function(error) {	       
	        	console.log("login failed");
	        	// 로그인에 실패한 경우..
	        }
	    );		
	} else {
		// 로그인 되어있는 경우구나...
		googleLoginProcess();
	}
	    
	/*else {			
			alert(gauth.isSignedIn.get());
			gauth.signOut().then(function() {
				// 로그아웃ㅇ
			});			
		} */
}

//구글 로그인 성공 -> 그 다음 처리를 진행한다.
function googleLoginProcess() {
	var profile = gauth.currentUser.get().getBasicProfile();
	
	var portalid = profile.getId();
	var email = profile.getEmail();
	var name = profile.getName();
	var registrationtype = "google";	
		   		   		  
	getPortalUserInfo(portalid, email, name, registrationtype);
}

/* ======================================== */

/*
로그인 정보를 저장한다.(아이디를 저장한다.)	
*/	
function saveLogin(userid) {
	// userid 가 비어있지 않다면 ~!!
	if (!isEmpty(userid)) {		
		// userid 쿠키에 id 값을 7일간 저장한다.
		saveCookie("userid", userid, 7);							
	} 
}

/*
setSaveCookie
*/
function saveCookie(name, value, expiredays) {
	var now = new Date();
	now.setDate(now.getDate() + expiredays);
	document.cookie = name + "=" + escape(value) + ";path=/;expires=" + now.toGMTString() + ";";
	//alert(now.toGMTString());
}

/*
getUseridCookie
쿠키값을 가져온다.
*/
function getUseridCookie(cname) {	
	var cookie = document.cookie + ";"; // 쿠키배열(모든 쿠키 배열을 가져온다.)	
	var idx = cookie.indexOf(cname, 0); // cname 이라는 쿠키를 0번쨰 인덱스 부터 찾는다.	
	// idx 부터 마지막 쿠키 인덱스 까지 다가져온다.
	// name = ~~~ 다 뗴어버리고 +1 시킨다. 그럼 값만 남겠지 처음에는?
	// name= 뗴에버리고 
	var val = "";	
	// userid라는 항목이 있다면... 인덱스가 있다면
	if (idx != -1) { // 쿠키가 존재한다면
	    cookie = cookie.substring(idx, cookie.length);
	    // idx 부터 마지막 쿠키 인덱스 까지 다 가져온다.
	    var begin = cookie.indexOf("=", 0) + 1;
	    var end = cookie.indexOf(";", begin);
	    val = unescape(cookie.substring(begin, end));	    
	}
	return val;
}

