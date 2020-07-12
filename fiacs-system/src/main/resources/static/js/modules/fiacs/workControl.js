$(function() {
	$("#jqGridTwo").jqGrid({
		datatype : "json",
		url : baseURL + 'fiacs/equip/queryAllEquip',
		colModel : [ {
			label : '网点名称',
			name : 'equipName'
		} , {
			label : '设备序列号',
			name : 'equipSn',
			key : true
		}, {
			label : '设备IP',
			name : 'equipIP'
		}, {
			label : '设备端口',
			name : 'equipPort'
		}, {
			label : '中心地址',
			name : 'centerIP'
		}, {
			label : '中心端口',
			name : 'centerPort'
		}],
		viewrecords : true,
		height : '100%',
		rowNum : 10000,
		rowList : [ 10, 30, 50 ],
		rownumbers : true,
		rownumWidth : 25,
		autowidth : true,
		shrinkToFit : true,
		multiselect : false,
		// pager : "#jqGridPager",
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
			$("#jqGridTwo").closest(".ui-jqgrid-bdiv").css({
				"overflow-x" : "hidden"
			});
		},
		loadComplete : function(xhr) {
			if (xhr.code && xhr.code != 0) {
				vm.equipTotalTwo = 0;
				alert(xhr.msg);
			} else {
				console.log('数据库设备数据加载完成');
				vm.equipTotalTwo = xhr.page.records.length;
				var url = "ws://" + localStorage.getItem('webSocketIp')
				+ ":10003/?userId=" + localStorage.getItem('token');
				connect(url);
			}
		}
	});
	$('#myTab a:first').tab('show');
	$("#jqGrid").jqGrid({
		url : baseURL + 'alarmEntity/list',
		datatype : "local",
		colModel : [{
			label : '网点名称',
			name : 'equipName'
		},{
			label : '设备序列号',
			name : 'equipSn',
			key : true
		}, {
			label : '设备IP',
			name : 'ip'
		}, {
			label : '设备端口',
			name : 'port'
		}, {
			label : '设备版本号',
			name : 'version'
		}, {
			label : '上线时间',
			name : 'startTime'
		} ],
		viewrecords : true,
		height : '100%',
		rowNum : 10000,
		rowList : [ 10, 30, 50 ],
		rownumbers : true,
		rownumWidth : 25,
		autowidth : true,
		shrinkToFit : true,
		multiselect : false,
		// pager : "#jqGridPager",
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

	$("#jqGridThree").jqGrid({
		url : baseURL + 'alarmEntity/list',
		datatype : "local",
		colModel : [{
			label : '网点名称',
			name : 'equipName'
		},{
			label : '设备序列号',
			name : 'equipSn',
			key : true
		}, {
			label : '设备IP',
			name : 'equipIP'
		}, {
			label : '设备版本号',
			name : 'version'
		}, {
			label : '上线时间',
			name : 'startTime'
		} ],
		viewrecords : true,
		height : '100%',
		rowNum : 10000,
		rowList : [ 10, 30, 50 ],
		rownumbers : true,
		rownumWidth : 25,
		autowidth : true,
		shrinkToFit : true,
		multiselect: false,
		multiboxonly:true,
		// pager : "#jqGridPager",
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
			$("#jqGridThree").closest(".ui-jqgrid-bdiv").css({
				"overflow-x" : "hidden"
			});
		}
	});
});

var setting = {
	data: {
		simpleData: {
			enable: true,
			idKey: "deptId",
			pIdKey: "parentId",
			rootPId: -1
		},
		key: {
			url:"nourl"
		}
	}
};
var ztree;

var vm = new Vue({
	el : '#jeefastapp',
	data : {
		equipTotal : 0,
		equipTotalTwo : 0,
		notconnectGrid : null,
		serverPort : '10001',
		newTag : 'connect',
		belongDeptId:null,//加入数据库所属机构
		belongDeptName:null
	},
	methods : {
		getRoleDept: function(){
			//加载部门树
			$.get(baseURL + "fiacs/dept/select", function(r){
				ztree = $.fn.zTree.init($("#roleDeptTree"), setting, r.deptList);
			})
		},
		saveDataBase: function() {
			var grid = $("#jqGridThree");
			var rowKey = grid.getGridParam("selrow");
			if (!rowKey) {
				alert("请选择一条记录");
				return;
			}

			vm.getRoleDept();
			layer.open({
				type: 1,
				offset: '50px',
				skin: 'layui-layer-molv',
				title: "选择机构",
				area: ['300px', '450px'],
				shade: 0.3,
				shadeClose: false,
				content: jQuery("#saveBaseLayer"),
				btn: ['确定', '取消'],
				btn1: function (index) {
					var node = ztree.getSelectedNodes();
					//选择上级部门
					vm.belongDeptId = node[0].deptId;
					vm.belongDeptName = node[0].name;

					var obj = $("#jqGridThree").jqGrid("getRowData", rowKey);
					obj.operate = null;
					showLoad();
					$.ajax({
						type: "POST",
						url: baseURL + 'fiacs/equip/saveDataBase/' + vm.belongDeptId,
						contentType: "application/json",
						data: JSON.stringify(obj),
						success: function (r) {
							closeLoad();
							if (r.code === 0) {
								layer.close(index);
								$("#jqGridThree").jqGrid('delRowData',
									obj.equipSn);
								alert('保存成功');

							} else {
								alert(r.msg);
							}
						}
					});

				}
			});
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

/*$(function() {
	
	$('a[data-toggle="tab"]').on('show.bs.tab', function(e) {
		newTag = $(e.target).text();
		if (newTag = '在线' && vm.newTag != newTag) {
			vm.newTag = newTag;
			if (vm.notconnectGrid) {
				var page = $("#jqGridTwo").jqGrid('getGridParam', 'page');
				$("#jqGridTwo").jqGrid('setGridParam', {
					page : page
				}).trigger("reloadGrid");
			} else {
				vm.notconnectGrid = 1;
				
			}
		} else if (newTag == '不在线' && vm.newTag != newTag) {
			vm.newTag = newTag;
		}
	});
});*/