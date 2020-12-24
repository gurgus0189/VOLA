var check = false;

//동의 체크시 다음버튼 활성화
$( document ).ready(function() {
	
	var language = getCookie("localecd");
	
	if(language == "ko" || language == "en") agreeStatusCheck();
	else if(language == "ja") agreeStatusCheck_ja();
		
	$("#next").on("click", function() {
		// 2개의 체크박스를 클릭한 경우에만 이동할수 이도록 처리!!
		if(language == "ko" || language == "en") {
			var chkCnt = $('.joinAgreeChkBox:checked').length;		
			if (chkCnt == 3) {						
				document.portalform.submit();			
			}
		} else if(language == "ja") {
			var chkCnt = $('.joinAgreeChkBox:checked').length;		
			if (chkCnt == 2) {						
				document.portalform.submit();			
			}
		}
		
	});
	
	$("#btnPopNormal").on("click", function() {
		var locale = getCookie('localecd');
		location.href = "/auth/login.do?language="+ locale;
	});
		
	// 로그인 링크 설정
	$("#loginLink").on("click", function() {		
		var locale = getCookie('localecd');
		location.href = "/auth/login.do?language="+ locale;
	});
	
	// 아이디 찾기 링크 설정
	$("#findIdLink").on("click", function() {
		location.href = "/auth/member_id.do";
	});
	
	//패스워드 링크 설정
	$("#findPasswordLink").on("click", function() {
		location.href = "/auth/member_pw.do";
	});
	
	$("#joinAgreeChk0").on("click",function() {
		$("#next").attr("class","btnPopConfirm"); 
        $("#agreeYN").val("Y");
        
        if(check) {
        	$("input:checkbox[class='joinAgreeChkBox']").prop("checked", false); 
        	check = false;
        	
        } else {
        	$("input:checkbox[class='joinAgreeChkBox']").prop("checked", true); 
        	check = true;
        }
         
        if(language == "ko" || language == "en") {
        	agreeStatusCheck();
        } else {
        	agreeStatusCheck_ja();
        }
        
	});
	
   $('.joinAgreeChkBox' ).change( function() {
	   if(language == "ko" || language == "en") agreeStatusCheck();
	   else if(language == "ja") agreeStatusCheck_ja();
   });   
   
   if(language == "ko") { 
	   
	   $("#firstMsg").load("/js/terms/firstMsg.html");
	   $("#secondMsg").load("/js/terms/secondMsg.html");
	   $("#thirdMsg").load("/js/terms/thirdMsg.html");
	   
   } else if(language == "en") {
	   
	   $("#firstMsg").load("/js/terms/firstMsg_en.html");
	   $("#secondMsg").load("/js/terms/secondMsg_en.html");
	   $("#thirdMsg").load("/js/terms/thirdMsg_en.html");
	   
   } else if(language == "ja") {
	   
	   $(".joinAgree_ko").remove();
	   
	   var firstMsg = $.i18n.prop('signup.service.term.first001');
		firstMsg += "\n" + $.i18n.prop('signup.service.term.first002');
		firstMsg += "\n" + $.i18n.prop('signup.service.term.first003');
		firstMsg += "\n" + $.i18n.prop('signup.service.term.first004');
		firstMsg += "\n" + $.i18n.prop('signup.service.term.first005');
		firstMsg += "\n" + $.i18n.prop('signup.service.term.first006');
		firstMsg += "\n" + $.i18n.prop('signup.service.term.first007');
		firstMsg += "\n" + $.i18n.prop('signup.service.term.first008');
		firstMsg += "\n" + $.i18n.prop('signup.service.term.first009');
		firstMsg += "\n" + $.i18n.prop('signup.service.term.first010');
		firstMsg += "\n" + $.i18n.prop('signup.service.term.first011');
		firstMsg += "\n" + $.i18n.prop('signup.service.term.first012');
		firstMsg += "\n" + $.i18n.prop('signup.service.term.first013');
	    $("#firstMsg").text(firstMsg);
		
		var secondMsg = $.i18n.prop('signup.service.term.second001');
		secondMsg += "\n" + $.i18n.prop('signup.service.term.second002');
		secondMsg += "\n" + $.i18n.prop('signup.service.term.second003');
		secondMsg += "\n" + $.i18n.prop('signup.service.term.second004');
		secondMsg += "\n" + $.i18n.prop('signup.service.term.second005');
		secondMsg += "\n" + $.i18n.prop('signup.service.term.second006');
		secondMsg += "\n" + $.i18n.prop('signup.service.term.second007');
		secondMsg += "\n" + $.i18n.prop('signup.service.term.second008');
		secondMsg += "\n" + $.i18n.prop('signup.service.term.second009');
		secondMsg += "\n" + $.i18n.prop('signup.service.term.second010');
		secondMsg += "\n" + $.i18n.prop('signup.service.term.second011');
		secondMsg += "\n" + $.i18n.prop('signup.service.term.second012');
		secondMsg += "\n" + $.i18n.prop('signup.service.term.second013');
	    $("#secondMsg").text(secondMsg);
	   
   } 
   
});

// 체크박스를 체크하는 경우 실행되는 함수
// 여기서 비활성화 표시를 결정한다.
function agreeStatusCheck() {
	var chkCnt = $('.joinAgreeChkBox:checked').length;
	
    if (chkCnt == 3) {
    	// $("#next").attr("href","member_add.do");
        $("#next").attr("class","btnPopConfirm"); 
        $("#agreeYN").val("Y");
        $("input:checkbox[id='joinAgreeChk0']").prop("checked", true); 
        check = true;
    } else {
    	// $("#next").attr("href", null);
        $("#next").attr("class","btnPopConfirm btnPopDisable");
        $("#next").css("cursor", "pointer");
        $("#agreeYN").val("N");
        $("input:checkbox[id='joinAgreeChk0']").prop("checked", false); 
        check = false;
    }
}

function agreeStatusCheck_ja() {
	var chkCnt = $('.joinAgreeChkBox:checked').length;
	
    if (chkCnt == 2) {
    	// $("#next").attr("href","member_add.do");
        $("#next").attr("class","btnPopConfirm"); 
        $("#agreeYN").val("Y");
        $("input:checkbox[id='joinAgreeChk0']").prop("checked", true); 
        check = true;
    } else {
    	// $("#next").attr("href", null);
        $("#next").attr("class","btnPopConfirm btnPopDisable");
        $("#next").css("cursor", "pointer");
        $("#agreeYN").val("N");
        $("input:checkbox[id='joinAgreeChk0']").prop("checked", false); 
        check = false;
    }
}
