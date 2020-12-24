ns("common") ;
jQuery.browser = {};
var respParser = common.ajax.responseParser;	
var grid;

function grid_notice() {
	
	var classificationHeader = $.i18n.prop('title.notice.classification');
	var noticeHeader = $.i18n.prop('title.notice');
	var regdateHeader = $.i18n.prop('title.notice.time');
	
	var windowWidth = $(window).width();
	  
	var regdate_title = $.i18n.prop('common.list.date');
	var devicename_title = $.i18n.prop('alarm.list.devicename');
	var alarmcontents_title = $.i18n.prop('alarm.list.contents');
	  
	var divWidth = $(".subTitle").width();
	var classificationWidth = windowWidth * 0.2;
	var noticetitleWidth = windowWidth * 0.4;
	var regdateWidth = windowWidth * 0.29;
	
	if(windowWidth > 1279) {
	    classificationWidth = divWidth * 0.25;
	    noticetitleWidth = divWidth * 0.5;
	    regdateWidth = divWidth * 0.26;
	} else if (windowWidth > 700 && windowWidth <= 1279) {
		classificationWidth = divWidth * 0.15;
	    noticetitleWidth = divWidth * 0.60;
	    regdateWidth = divWidth * 0.3;
	}
	
	grid = new tui.Grid({
		
		el: document.getElementById('grid'),
		
		data: {
	      api: {
				readData: { url: '/openapi/notice/list', method: 'GET' }
	      },
	      initialRequest: false
		},
		scrollX: true,
		scrollY: false,
		rowHeight : 'auto',
		rowHeaders: [ { type: 'rowNum'} ],
		//pagination: true,
		pageOptions: {
			perPage: 20
		},
		columns: [
			{
				header: 'seqno',
				name: 'seqno',
				align: 'center',
				whiteSpace: 'normal'
			},
			{
				header: classificationHeader,
				name: 'classification',
				align: 'center',
				whiteSpace: 'normal',
				width: classificationWidth
			},
			{
				header: noticeHeader,
				name: 'noticetitle',
				align: 'left',
				whiteSpace: 'normal',
				width: noticetitleWidth/*,
				renderer: {
	               type: noticeDetailRenderer
	            }*/
			},
			{
				header: regdateHeader,
				name: 'regdate',
				align: 'center',
				whiteSpace: 'pre-wrap',
				width: regdateWidth
			}
		]
	});	
	
	var Grid = tui.Grid; // or require('tui-grid')

	Grid.applyTheme('striped', {
		
		grid: {
	        text: '#000'
	    },
	    row: {
	    	even : {
	    		background: '#203844',    			
	    	},
	    	odd : {
	    		background: '#122835'
	    	},
	    	hover: {
	    	    background: '#5dc6ff'
	    	}
	    },
	    cell: {
	    	nomal: {
	    		border: '#014386'
	    	},
	    },
	    scrollbar : {
	    	background: '#09212D',       
	    	emptySpace: '#09212D',     
	    	border : '#09212D',
	    	thumb : '#208BE4',
	    	active : '#5dc6ff'
	    }
	});
	
	grid.on("click", function (ev) {
		
		var index = ev.rowKey;
		var getData = grid.getRow(index);
		
		if(getData == null) return;
		
		var seqno = grid.getRow(index).seqno;
		
		location.href = '/notice/notice_list_detail.do?menu_listview2?seqno=' + seqno;
		
	});
	
	grid.hideColumn('seqno');
	
	$(".tui-grid-body-area").css("background-color","#122835");
	$(".tui-grid-cell-header").css({ "background-color":"#173C4F","color":"#339BD8","border-color":"#173C4F","border-top":"3px solid #339BD8"});
	$(".tui-grid-border-line-top").css("background-color","#0b3f73");
}

function readData() {
	
	var localecd = getCookie("localecd");
	
	var parameter = {
		localecd : localecd
	};
		
	grid.readData(1,parameter,true);
	
}

$(document).ready(function() {
	
	$(".mMenu").click(function() {
		$(".nav").toggleClass("mNavOn");
		$(this).toggleClass("mMenuClose");
	});
	
	grid_notice();
	readData();
	grid.getPagination()._options.visiblePages = 5;
	
});



