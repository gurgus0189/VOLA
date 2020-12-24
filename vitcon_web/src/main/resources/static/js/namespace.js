/*
 * Copyright (c) 2015 Nexmotion, Inc
 * All rights reserved.
 * REVISION HISTORY (reverse chronological order)
 * =============================================================================
 * 2015/11/02 조원덕 작성
 * Javascript Name Space 구현 라이브러리.
 * window.ns 와 window.namespace 두가지를 예약하므로 본 파일 첨부 시 두개의 변수는 제한됩니다.
 * =============================================================================
 */
(function(window,undefined){
    var nameSpace = function (arg){

        var tmp = arg.split(".");
        var curidx = 0;
        var txt = "window";

        var next = function(){
            txt += "." + tmp[curidx++];
            return txt;
        }

        var hasNext = function(){
            if ( tmp.length > curidx ) return true;
            return false;
        }

        while (hasNext()){
            obj = next();
            if (typeof(eval(obj)) != "undefined") continue;
            eval(obj + " = {};");
        }
    }

    window.ns = nameSpace;
    window.namespace = nameSpace;
})(window);
