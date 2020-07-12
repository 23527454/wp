var setting = {
	data : {
		simpleData : {
			enable : true,
			idKey : "deptId",
			pIdKey : "parentId",
			rootPId : -1
		},
		key : {
			url : "nourl"
		}
	}
};
var ztree;

var vm = new Vue({
	el : '#jeefastapp',
	data : {
		showList : true,
		title : null,
		queryDeptName : null,
		dept : {
			parentName : null,
			parentId : 0,
			orderNum : 0
		}
	},
	methods : {
		getDept : function() {
			// 加载部门树
			$.get(baseURL + "fiacs/dept/select", function(r) {
				ztree = $.fn.zTree.init($("#deptTree"), setting, r.deptList);
				var node = ztree.getNodeByParam("deptId", vm.dept.parentId);
				if(node){
					ztree.selectNode(node);
					vm.dept.parentName = node.name;
				}
			})
		},
		add : function() {
			var selected = $('#deptTable').bootstrapTreeTable('getSelections');
			if (selected.length != 0) {
				var deptId = getDeptId();
				$.get(baseURL + "sys/dept/info/" + deptId, function(r) {
					vm.showList = false;
					vm.dept = {
						parentName : r.dept.name,
						parentId : r.dept.deptId,
						orderNum : 0
					};
				});
			} else {
				vm.dept = {
					parentName : '机构1',
					parentId : 1,
					orderNum : 0
				};
			}

			vm.getDept();
			vm.title = "新增";
			vm.showList = false;
		},
		update : function() {
			var deptId = getDeptId();
			if (deptId == null) {
				return;
			}

			$.get(baseURL + "sys/dept/info/" + deptId, function(r) {
				vm.showList = false;
				vm.title = "修改";
				vm.dept = r.dept;

				vm.getDept();
			});
		},
		lookInfo : function(){
			var deptId = getDeptId();
			if (deptId == null) {
				return;
			}

			$.get(baseURL + "fiacs/dept/deptInfo/" + deptId, function(r) {
				vm.showList = false;
				vm.title = "查看详情";
				vm.dept = r.dept;
			});
		},
		del : function() {
			var deptId = getDeptId();
			if (deptId == null) {
				return;
			}

			confirm('确定要删除选中的记录？', function() {
				$.ajax({
					type : "POST",
					url : baseURL + "sys/dept/delete",
					data : "deptId=" + deptId,
					success : function(r) {
						if (r.code === 0) {
							alert('操作成功', function() {
								vm.reload();
							});
						} else {
							alert(r.msg);
						}
					}
				});
			});
		},
		saveOrUpdate : function(event) {
			var url = vm.dept.deptId == null ? "sys/dept/save"
					: "sys/dept/update";
			$.ajax({
				type : "POST",
				url : baseURL + url,
				contentType : "application/json",
				data : JSON.stringify(vm.dept),
				success : function(r) {
					if (r.code === 0) {
						alert('操作成功', function() {
							vm.reload();
						});
					} else {
						alert(r.msg);
					}
				}
			});
		},
		deptTree : function() {
			layer.open({
				type : 1,
				offset : '50px',
				skin : 'layui-layer-molv',
				title : "选择机构",
				area : [ '300px', '450px' ],
				shade : 0,
				shadeClose : false,
				content : jQuery("#deptLayer"),
				btn : [ '确定', '取消' ],
				btn1 : function(index) {
					var node = ztree.getSelectedNodes();
					// 选择上级部门
					vm.dept.parentId = node[0].deptId;
					vm.dept.parentName = node[0].name;

					layer.close(index);
				}
			});
		},
		reload : function() {
			vm.showList = true;
			vm.queryDeptName = null;
			Dept.table.refresh();
		},
		query : function() {
			$('#deptTable').bootstrapTreeTable('expendNodeByName',
					vm.queryDeptName);
		}
	}
});

var Dept = {
	id : "deptTable",
	table : null,
	layerIndex : -1
};

/**
 * 初始化表格的列
 */

Dept.initColumn = function() {
	var columns = [ {
		field : 'selectItem',
		radio : true,
		width : '10px'
	},
	/*
	 * {title: '机构ID', field: 'deptId', visible: false, align: 'center', valign:
	 * 'middle', width: '50px'},
	 */
	{
		title : '机构名称',
		field : 'name',
		align : 'center',
		valign : 'middle',
		sortable : false,
		width : '180px'
	}
	/*
	 * {title: '上级部门', field: 'parentName', align: 'center', valign: 'middle',
	 * sortable: true, width: '100px'}, {title: '排序号', field: 'orderNum', align:
	 * 'center', valign: 'middle', sortable: true, width: '100px'}
	 */]
	return columns;
};

function getDeptId() {
	var selected = $('#deptTable').bootstrapTreeTable('getSelections');
	if (selected.length == 0) {
		alert("请选择一条记录");
		return false;
	} else {
		return selected[0].id;
	}
}

$(function() {
	//$.get(baseURL + "sys/dept/info", function(r) {
		var colunms = Dept.initColumn();
		var table = new TreeTable(Dept.id, baseURL + "fiacs/dept/list", colunms);
		table.setRootCodeValue(0);
		table.setExpandColumn(1);
		table.setIdField("deptId");
		table.setCodeField("deptId");
		table.setParentCodeField("parentId");
		table.setExpandAll(true);
		table.setHeight(window.innerHeight - 100);
		table.init();
		Dept.table = table;
	//});

	$('#' + Dept.id).bind('click', function() {
		$('#deptTable').bootstrapTreeTable('expendNode');
	});
});
