/*
 * Copyright (c) 2017 Nexmotion, Inc
 * All rights reserved.
 * REVISION HISTORY (reverse chronological order)
 * =============================================================================
 * 2017/03/28
 * =============================================================================
 */
ns("common");

common = {
	getNewMarkTime : function() {
		var d = new Date();
		return d.getTime();
	},
	page : {
		pagenum : 5,
		scrnum : 10
	},
	ajax : {
		responseParser : {
			responseObj : null,
			
			returnCode : {
				OK : "200",
				ERROR_BAD_REQUEST : "400",
				ERROR_SERVICE : "401",
				ERROR_PARAMETER : "402",
				ERROR_UNAUTHORIZED : "501",
				ERROR_ACCESSDENIED : "502"
			},
			
			get : function() {
				return this;
			},
			
			getDataObj : function() {
				return this.responseObj.data;
			},
			
			parse : function(json) {
				this.responseObj = json;
				return this.get();
			},
			
			isSuccess : function() {
				if (this.responseObj != null && this.returnCode.OK == this.responseObj.returnCode) {
					return true;
				}
				return false;
			}
		}
	},
	parameterParser : {
		_parammap : {},
		parse : function () {
		    var hash;
		    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
		    for(var i = 0; i < hashes.length; i++)
		    {
		        hash = hashes[i].split('=');
		        this._parammap[hash[0]] = decodeURIComponent(hash[1]);
		    }
		},
		getParams : function(){
	        return this._parammap;
	    },
		getParam : function(key){
	        return this._parammap[key];
	    },
	}
}

// new Date(dt); dt에 number(숫자)타입이 들어가야 함...
function formatFullDate(dt) {
	if (typeof dt == 'number') {
		// convert string to date object	
		var date = new Date(dt);	
	}
	
	var month = date.getMonth()+1;
	var day = date.getDate();
	var year = date.getFullYear();
	
	var hour = date.getHours();
	var minute = date.getMinutes();
	var second = date.getSeconds();  
			
	return year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) +
			(hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second); 
}

function formatDate(dt) {	
	if (typeof dt == 'string') {
		// convert string to date object		
		dt = new Date(dt);
	}	
		
	var month = dt.getMonth()+1;
	var day = dt.getDate();
	var year = dt.getFullYear();
	
	var ret = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
	return ret;
}

function escapeHtml(unsafe) {
    return unsafe
         .replace(/&/g, "&amp;")
         .replace(/</g, "&lt;")
         .replace(/>/g, "&gt;")
         .replace(/"/g, "&quot;")
         .replace(/'/g, "&#039;");
}

function encodehtmlspecialchars(text) {
	return $('<textarea/>').text(text).html();		
}

function decodehtmlspecialchars(text) {
	return $("<textarea/>").html(text).text();		
}

//'변수명','값','만료일','도메인','보안'
function setCookie(cookieName, cookieValue, cookieExpire, cookiePath, cookieDomain, cookieSecure){
    var cookieText=escape(cookieName)+'='+escape(cookieValue);
    cookieText+=(cookieExpire ? '; EXPIRES='+cookieExpire.toGMTString() : '');
    cookieText+=(cookiePath ? '; PATH='+cookiePath : '');
    cookieText+=(cookieDomain ? '; DOMAIN='+cookieDomain : '');
    cookieText+=(cookieSecure ? '; SECURE' : '');
    document.cookie=cookieText;
}
 
//'변수명'
function getCookie(cookieName){
    var cookieValue=null;
    if(document.cookie){
        var array=document.cookie.split((escape(cookieName)+'=')); 
        if(array.length >= 2){
            var arraySub=array[1].split(';');
            cookieValue=unescape(arraySub[0]);
        }
    }
    return cookieValue;
}
 
//'변수명'
function deleteCookie(cookieName){
    var temp=getCookie(cookieName);
    if(temp){
        setCookie(cookieName,temp,(new Date(1)));
    }
}