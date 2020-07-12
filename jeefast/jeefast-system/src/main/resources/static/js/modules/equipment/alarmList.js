$(function() {
	$("#jqGrid").jqGrid({
		url : baseURL + 'alarmEntity/list',
		datatype : "json",
		colModel : [ {
			label : 'id',
			name : 'id',
			width : 30,
			hidden : true,
			key : true
		}, {
			label : 'sn码',
			name : 'mac',
			width : 50,
			hidden : true
		}, {
			label : '序列号',
			name : 'equipReq',
			width : 100
		}, {
			label : '设备名',
			name : 'equipName',
			width : 70
		}, {
			label : '备注类型',
			name : 'remark',
			width : 70,
			formatter : valueFormatter
		}, {
			label : '事件来源',
			name : 'resource',
			width : 70,
			formatter : valueFormatter
		}, {
			label : '事件详情',
			name : 'exceptionInfo',
			width : 100,
			formatter : valueFormatter
		}, {
			label : '时间',
			name : 'date',
			width : 80
		} ],
		viewrecords : true,
		height : '100%',
		rowNum : 10,
		rowList : [ 10, 30, 50 ],
		rownumbers : false,
		rownumWidth : 25,
		autowidth : true,
		multiselect : false,
		pager : "#jqGridPager",
		jsonReader : {
			root : "page.records",
			page : "page.current",
			total : "page.pages",
			records : "page.total"
		},
		prmNames : {
			page : "page",
			rows : "limit",
			order : "order"
		},
		gridComplete : function() {
			// 隐藏grid底部滚动条
			$("#jqGrid").closest(".ui-jqgrid-bdiv").css({
				"overflow-x" : "hidden"
			});
		}
	});
});

var vm = new Vue({
	el : '#jeefastapp',
	data : {
		equipTreeName:null,
		q : {
			key : 1
		},
	},
	methods : {
		query : function() {
			vm.reload();
		},
		reload : function(event) {
			var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
				 postData:{'equipSn': null,'deptId':null},
				page : page
			}).trigger("reloadGrid");
		},
		queryEquipTree : function() {
			$('#deptTable').bootstrapTreeTable('expendNodeByName',
					vm.equipTreeName);
		}
	}
});

var Dept = {
	id : "deptTable",
	table : null,
	layerIndex : -1
};

Dept.initColumn = function() {
	var columns = [ {
		field : 'selectItem',
		radio : true,
		width : '10px'
	}, {
		title : '设备名称',
		field : 'name',
		align : 'center',
		valign : 'middle',
		sortable : true,
		width : '100px',
		formatter : treeFormat
	} ];
	return columns;
};

$(function () {
    $.get(baseURL + "sys/dept/info", function(r){
        var colunms = Dept.initColumn();
        var table = new TreeTable(Dept.id, baseURL + "equipment/equipTree", colunms);
        table.setRootCodeValue(r.deptId);
        table.setExpandColumn(1);
        table.setIdField("id");
        table.setCodeField("id");
        table.setParentCodeField("parentId");
        table.setExpandAll(true);
        table.setHeight(window.innerHeight-100);
        table.init();
        Dept.table = table;
    });
    
    $('#'+Dept.id).bind('click',function(){
    	var selected = $('#deptTable').bootstrapTreeTable('getSelections');
        $('#deptTable').bootstrapTreeTable('expendNode'); 
    	if(selected[0].id.length<6){
    		var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
				 postData:{'deptId': selected[0].id,'equipSn':null},
				page : page
			}).trigger("reloadGrid");
    	}else{
    		var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
				 postData:{'equipSn': selected[0].id,'deptId':null},
				page : page
			}).trigger("reloadGrid");
    	}
    });
});