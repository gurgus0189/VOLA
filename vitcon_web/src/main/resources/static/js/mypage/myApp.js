ns("common");

function setDeviceGroupListStatus() {
	
	//checkbox 한개라도 체크되지 않을경우, selectbox disabled 처리
	if ($("input[name=listChkbox]").is(":checked")) {
		
		$("#deviceDelete").attr("class","btnCancel");
	} else {
		
		$("#deviceDelete").attr("class","btnCancel btnDisable");
	}
}

$(document).ready(function() {
	
	var url = "/openapi/deviceuser/getuserapp";
	var windowWidth = $(window).width();
	var autoWidth;
	var regdate_title = $.i18n.prop('common.list.date');
	var divWidth = $(".subTitle, .subTitleDevice").width();
	var regdateWidth,appidWidth,useridWidth,modelnameWidth;
	
	$(".mMenu").click(function() {
		$(".nav").toggleClass("mNavOn");
		$(this).toggleClass("mMenuClose");
	});
	
    if(windowWidth > 1279) {
	    //check_header = divWidth * 0.05;
    	regdateWidth = divWidth * 0.20;
    	appidWidth = divWidth - (regdateWidth + useridWidth + modelnameWidth);
    	useridWidth = divWidth * 0.23;
    	modelnameWidth = divWidth * 0.17;
    } else {
	   //check_header = windowWidth * 0.06;
    	regdateWidth = windowWidth * 0.29;
    	appidWidth = windowWidth - (regdateWidth + useridWidth + modelnameWidth);
    	useridWidth = windowWidth * 0.2;
    	modelnameWidth = windowWidth * 0.2;
    }
	
	var grid = new tui.Grid({
	    el: $('#grid'),
	    scrollX: false,
	    scrollY: false,
	    rowHeaders: ['checkbox'],
	    /*rowHeaders: [
	    	{
	    		type: 'checkbox',
	    		width: check_header
	    	},
	    ],*/

	    pagination: true,
	    columns: [
	        {
	            title: regdate_title,
	            name: 'regdate',
	            sortable: true,
	            width: regdateWidth,
	            resizable: false,	            
	            align: 'center',
	            whiteSpace: 'pre-wrap'
	        },
	        {
	            title: 'appid',
	            name: 'appid',
	            sortable: true,
	            width: appidWidth,
	            resizable: false,
	            align: 'center',
	            whiteSpace: 'normal'
	        },
	        {
	            title: 'userid',
	            name: 'userid',
	            sortable: true,
	            width: useridWidth,
	            resizable: false,
	            align: 'center',
	            whiteSpace: 'normal'
	        },
	        
	        {
	            title: 'modelname',
	            name: 'modelname',
	            sortable: true,
	            width: modelnameWidth,
	            resizable: false,
	            align: 'center',
	            whiteSpace: 'normal'
	        },
	       
	    ]
	});
	
	tui.Grid.applyTheme('default', {
		
	    cell: {
	        normal: {
	            background: '#014386',
	            border: '#0c4e91',
	            text: '#fff'
	        },
	        head: {
	            background: '#0b3f73',
	            border: '#0b3f73', 
	            text: '#208be4'
	        },
	        rowHead: {
	            border: ''
	        },
	        selectedHead: {
	           background: '#0b3f73'
	        },
	        evenRow: {
	            background: '#0c4e91'
	            	
	        },
	        oddRow: {
	            background: '#014386'
	            
	        },
	        
	        
	    }
	});
	
	grid.use('Net', {
		//initialRequest: false,
	    perPage: 10,
	    readDataMethod: 'GET',
	    withCredentials: true,
	    api: {
	        readData: '/openapi/deviceuser/getuserapp'
	    }
	});
	
	var check_val = new Array();
	
	grid.on("check", function (ev) {
		check_val.push(ev.rowKey);
	});
	
	grid.on('uncheck', function(ev) {
	    var index = check_val.indexOf(ev.rowKey);
	    check_val.splice(index,1);
	});

	grid.on('successResponse', function(data) { 
		var lengthData = data.responseData.data.contents.length;
		if(lengthData > 0) {
			$("#noData").css("display", "none");
			$("#grid").show();
		} else {
			$("#noData").css("display", "");
			$("#grid").hide();
		}
	});
	
	$(".tui-grid-cell-row-head").css("border-color","#014386");
	$(".tui-grid-head-area").css({"border-top":"4px solid #3c7cbc","border-bottom":"1px solid #0b3f73","background-color":"transparent"});
	$(".tui-grid-body-area").css("background-color","transparent");
	$(".tui-grid-border-line-top").css("background-color","#0b3f73");
	$(".tui-grid-border-line-bottom").css("background-color","#0b3f73");
	
});
















