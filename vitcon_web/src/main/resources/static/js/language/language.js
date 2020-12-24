/**
 * 다국어 처리 
 */
$(document).ready(function() {
	loadMessageProperties();
})

/**
 * 메시지 로드
 * @returns
 */
function loadMessageProperties() {
	var locale = undefined;
	//var locale = "";
	
/*	try {	
		locale = ("body").attr("localecd");
	} catch (e) {
		// TODO: handle exception
		console.log(e);
	}*/
	
/*	if (!locale) {
		locale = $.cookie("localecd");
	}
*/	
	//쿠키에서 locale 가져오기
	locale = $.cookie("localecd");
	
	//alert('locale=' + locale);
		
	jQuery.i18n.properties({
	    name: 'messages', 
	    path: '/js/messages/', 
	    mode: 'both',
	    language: locale,
	    encoding: "UTF-8",
	    async: false,
	    callback: function() {
	        // We specified mode: 'both' so translated values will be
	        // available as JS vars/functions and as a map
	        // Accessing a simple value through the map
	    	//console.log("msg_hello->" + $.i18n.prop('login.welcome.msg'));
	    }
	});
}
