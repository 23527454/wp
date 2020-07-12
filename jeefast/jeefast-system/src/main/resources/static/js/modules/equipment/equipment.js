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
    el:'#jeefastapp',
    data:{
        showList: 0,
        title: null,
        paramQuery:0,
        equipTotal:0,
        equipQueryList:false,//是否进入过设备搜索界面
        operateSelect:null,
        belongDeptId:null,//加入数据库所属机构
        belongDeptName:null,
        equipment:{
            deptId:null,
            deptName:null,
            subnetMask:null,
            address:1
        },
        operateList:[
        	{
        		id:'0',
        		name:'左开闸'
        	},{
        		id:'1',
        		name:'右开闸'
        	},{
        		id:'2',
        		name:'关闸'
        	},{
        		id:'3',
        		name:'急停'
        	},{
        		id:'4',
        		name:'取消急停'
        	},{
        		id:'5',
        		name:'锁离合'
        	},{
        		id:'6',
        		name:'开离合'
        	},{
        		id:'7',
        		name:'手动设零'
        	},{
        		id:'8',
        		name:'自动找零'
        	},
        ],
        paramQueryList:[{
        	id:'0',
        	name:'全部'
        },{
        	id:'1',
        	name:'未入数据库'
        }]
    },
    methods: {
        getDept: function(){
            //加载部门树
            $.get(baseURL + "sys/dept/selectRoleDept", function(r){
                ztree = $.fn.zTree.init($("#deptTree"), setting, r.deptList);
                var node = ztree.getNodeByParam("deptId", vm.equipment.deptId);
                if(node != null){
                	ztree.selectNode(node);

                	vm.equipment.deptName = node.name;
                }
            })
        },
        getRoleDept: function(){
            //加载部门树
            $.get(baseURL + "sys/dept/selectRoleDept", function(r){
                ztree = $.fn.zTree.init($("#roleDeptTree"), setting, r.deptList);
               /* var node = ztree.getNodeByParam("deptId", vm.equipment.deptId);
                if(node != null){
                	ztree.selectNode(node);
                }*/
            })
        },
        add: function(){
            vm.showList = 1;
            vm.title = "新增";
            vm.equipment = { deptId:null,deptName:null,subnetMask:'0.0.0.0',address:1,ip:'192.168.1.162',port:"6000",ipCenter:'192.168.1.8',portCenter:'10086',gateWay:'192.168.1.1'};
            vm.getDept();
        },
        //只修改数据库
        update: function () {
        	var equipmentId = getSelectedRow();
            if(equipmentId == null){
                return ;
            }

            $.get(baseURL + "equipment/info/"+equipmentId, function(r){
                vm.showList = 1;
                vm.title = "修改";
                
                if(r.code!=0){
                	alert("加载详情错误");
                	return;
                }
                vm.equipment = r.equipment;

                vm.getDept();
            });
        },
        del: function () {
        	var equipmentIds = getSelectedRows();
			if(equipmentIds == null){
				return ;
			}

            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "equipment/delete",
                    contentType: "application/json",
				    data: JSON.stringify(equipmentIds),
                    success: function(r){
                        if(r.code === 0){
                            alert('操作成功', function(){
                                vm.reload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        saveOrUpdate: function (event) {
            var url = vm.equipment.id == null ? "equipment/save" : "equipment/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.equipment),
                success: function(r){
                    if(r.code === 0){
                        alert('操作成功', function(){
                            vm.reload();
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
        deptTree: function(){
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择机构",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级部门
                    vm.equipment.deptId = node[0].deptId;
                    vm.equipment.deptName = node[0].name;
                    layer.close(index);
                }
            });
        },
        reload: function () {
            vm.showList = 0;
            //Dept.table.refresh();
            var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:{'deptId': selectRow},
                page:page
            }).trigger("reloadGrid");
        },
        equipQueryDiv:function(){
        	showLoad();
        	vm.showList=2;
        	vm.paramQuery=0;
        	if(!vm.equipQueryList){
        		vm.equipQueryList=true;
        		 $("#jqGridEquip").jqGrid({
        		        url: baseURL + 'searchEquipment/list',
        		        postData:{"type":vm.paramQuery},
        				datatype:'json',
        		        colModel: [			
        					{ label: 'sn码', name: 'equipSn', index: "equipSn", width: 140, key: true,hidden:true },
        					{ label: '序列号', name: 'equipReq',width: 180},
        					{ label: '所属机构', name: 'deptName', width: 120},
        					 { label: '设备名', name: 'otherName', width: 120 },
        					{ label: '网络地址', name: 'ip', index: "ip" },
        		            { label: '通信端口', name: 'port',width: 80},
        					{ label: '网关地址', name: 'gateWay',width: 120},
        					{ label: '上传地址', name: 'ipCenter',width: 120},
        					{ label: '上传端口', name: 'portCenter',width: 80 },
        					{ label: '设备地址', name: 'address', hidden:true },
        					{ label: '原ip', name: 'oldIp', hidden:true },
        					{ label: '原端口', name: 'oldPort', hidden:true },
        					{ label: '子网掩码', name: 'subnetMask',width: 120}
        					
        		        ],
        				viewrecords: true,
        		        height: '100%',
        		        rowNum: 10000,
        				rowList : [10000],
        		        rownumbers: false, 
        		        rownumWidth: 25, 
        		        autowidth:true,
        		        multiselect: true,
        		        multiboxonly:true,
        		        //pager: "#jqGridEquipPager",
        		        jsonReader : {
        		            root: "page.records",
        		            page: "page.current",
        		            total: "page.pages",
        		            records: "page.total"
        		        },
        		        prmNames : {
        		            page:"page", 
        		            rows:"limit", 
        		            order: "order"
        		        },
        		        gridComplete:function(){
        		        	//隐藏grid底部滚动条
        		        	$("#jqGridEquip").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        		        	//alert("加载完成");
        		        },
        		        loadComplete:function(xhr){
        		        	closeLoad();
        		        	if(xhr.code&&xhr.code!=0){
        		        		vm.equipTotal = 0;
        		        		alert(xhr.msg);
        		        	}else{
        		        		vm.equipTotal = xhr.page.records.length;
        		        	}
        		        }
        		        
        		    });
        	}else{
        		var page = $("#jqGridEquip").jqGrid('getGridParam','page');
    			$("#jqGridEquip").jqGrid('setGridParam',{ 
    				postData:{"type":vm.paramQuery},
    				datatype:'json'
                }).trigger("reloadGrid");
        	}
        },
        equipQuery:function(){
        	showLoad();
        	var page = $("#jqGridEquip").jqGrid('getGridParam','page');
			$("#jqGridEquip").jqGrid('setGridParam',{ 
				postData:{"type":vm.paramQuery},
				datatype:'json'
            }).trigger("reloadGrid");
        },
        saveDataBase: function(){
        	var grid = $("#jqGridEquip");
        	var rowKey = grid.getGridParam("selrow");
        	if (!rowKey) {
        		alert("请选择一条记录");
        		return;
        	}
			var roleIds = grid.getGridParam("selarrrow");
			var obj = [];
			if(roleIds == null){
				alert('请选择要保存的设备');
			}else{
				for(var i=0;i<roleIds.length;i++){
					obj.push($("#jqGridEquip").jqGrid("getRowData",roleIds[i]));
				}
				vm.getRoleDept();
	            layer.open({
	                type: 1,
	                offset: '50px',
	                skin: 'layui-layer-molv',
	                title: "选择机构",
	                area: ['300px', '450px'],
	                shade: 0,
	                shadeClose: false,
	                content: jQuery("#saveBaseLayer"),
	                btn: ['确定', '取消'],
	                btn1: function (index) {
	                    var node = ztree.getSelectedNodes();
	                    //选择上级部门
	                    vm.belongDeptId = node[0].deptId;
	                    vm.belongDeptName = node[0].name;
	                    
	    				confirm("注意：序列号重复，将会覆盖原有数据，确定保存吗?",function(){
	    					showLoad();
	    					$.ajax({
	    						type: "POST",
	    					    url: baseURL + 'searchEquipment/saveDataBase/'+vm.belongDeptId,
	    		                contentType: "application/json",
	    					    data: JSON.stringify(obj),
	    					    success: function(r){
	    					    	closeLoad();
	    					    	if(r.code === 0){
	    					    		layer.close(index);
	    					    		//vm.equipment = r.equipment;
	    								alert('保存成功');
	    								vm.equipQuery();
	    							}else{
	    								alert(r.msg);
	    							}
	    						}
	    					});
	    				});
	    			
	                }
	            });
	        }
			
		},
		//发送指令修改
		modifyEquip:function(){
			var grid = $("#jqGridEquip");
        	var rowKey = grid.getGridParam("selrow");
        	if (!rowKey) {
        		alert("请选择一条记录");
        		return;
        	}
			var roleIds = grid.getGridParam("selarrrow");
			var obj = [];
			if(roleIds.length>1){
				alert('只能选择一条记录');
				return;
			}
			vm.equipment = $("#jqGridEquip").jqGrid("getRowData",rowKey);
			vm.equipment.oldIp = vm.equipment.ip;
			vm.equipment.oldPort = vm.equipment.port;
			vm.title="更改(设备sn码："+vm.equipment.equipSn+")";
			vm.showList=4;
		},
		saveEquipBaseInfo:function(){
			showLoad();
			$.ajax({
				type: "POST",
			    url: baseURL + 'searchEquipment/settingInfo/baseInfo',
                contentType: "application/json",
			    data: JSON.stringify(vm.equipment),
			    success: function(r){
			    	closeLoad();
			    	if(r.code === 0){
			    		vm.equipment = r.equipment;
						alert('操作成功',function(){
							vm.equipQuery();
							vm.showList=2;
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		backEquipDiv:function(){
			vm.equipQuery();
			vm.showList=2;
		},
		operateEquip:function(){
			var rowId = getSelectedRow();
			if(rowId == null){
				return ;
			}
			vm.operateSelect=null;
			vm.equipment = $("#jqGrid").jqGrid("getRowData",rowId);
			vm.title="操控(设备sn码："+vm.equipment.equipSn+")";
			vm.showList=3;
		},
		selectChange:function(paramType,value){
			if(vm.operateSelect){
				showLoad();
				$.ajax({
					type: "POST",
					url: baseURL + 'searchEquipment/operateEquip/'+vm.operateSelect,
					contentType: "application/json",
					data: JSON.stringify(vm.equipment),
					success: function(r){
						closeLoad();
						if(r.code === 0){
							alert('操作成功');
						}else{
							alert(r.msg);
						}
					}
				});
			}
		}
    },
    created:function(){
    	setTimeout(function(){
    		var page = $("#jqGrid").jqGrid('getGridParam','page');
        	$('#deptTable').bootstrapTreeTable('selectFirstRow');
        	var selected = $('#deptTable').bootstrapTreeTable('getSelections');
        	if(selected){
        		$("#jqGrid").jqGrid('setGridParam',{ 
        			postData:{'deptId': selected[0].id },
        			page:page
        		}).trigger("reloadGrid");
        	}else{
        		$("#jqGrid").jqGrid('setGridParam',{ 
        			postData:{'deptId': 1 },
        			page:page
        		}).trigger("reloadGrid");
        	}
    	},500);
    }
});

var Dept = {
    id: "deptTable",
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */


Dept.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true, width: '10px'},
        /*{title: '部门ID', field: 'deptId', visible: false, align: 'center', valign: 'middle', width: '50px'},*/
        {title: '机构名称', field: 'name', align: 'center', valign: 'middle', sortable: true, width: '100px'}
        /*{title: '上级部门', field: 'parentName', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {title: '排序号', field: 'orderNum', align: 'center', valign: 'middle', sortable: true, width: '100px'}*/]
    return columns;
};


function getDeptId () {
    var selected = $('#deptTable').bootstrapTreeTable('getSelections');
    if (selected.length == 0) {
        alert("请选择一条记录");
        return false;
    } else {
        return selected[0].id;
    }
}

var selectRow;

$(function () {
    $.get(baseURL + "sys/dept/info", function(r){
        var colunms = Dept.initColumn();
        var table = new TreeTable(Dept.id, baseURL + "sys/dept/list", colunms);
        table.setRootCodeValue(r.deptId);
        table.setExpandColumn(1);
        table.setIdField("deptId");
        table.setCodeField("deptId");
        table.setParentCodeField("parentId");
        table.setExpandAll(true);
        table.setHeight(window.innerHeight-90);
        table.init();
        Dept.table = table;
    });
    
    $('#'+Dept.id).bind('click',function(){
    	 $('#deptTable').bootstrapTreeTable('expendNode'); 
    	 var selected = $('#deptTable').bootstrapTreeTable('getSelections');
    	if(selectRow==selected[0].id){
    		return;
    	}else{
    		selectRow=selected[0].id;
    	//	alert(selected[0].id);
    		
    		var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:{'deptId': selectRow},
                page:page
            }).trigger("reloadGrid");
    	}
    	
    });
    
    

    $("#jqGrid").jqGrid({
        url: baseURL + 'equipment/list',
        datatype: "json",
        colModel: [			
        	{ label: '主键', name: 'id',  width: 100, key: true,hidden:true },
			{ label: 'sn码', name: 'equipSn',  width: 100,hidden:true },
			{ label: '序列号', name: 'equipReq',width: 140},
            { label: '所属机构', name: 'deptName', width: 75 },
            { label: '设备名', name: 'otherName', width: 75 },
			{ label: '网络地址', name: 'ip', width: 100 },
			{ label: '网络端口', name: 'port', width: 60},
			{ label: '网关', name: 'gateWay', width: 80},
			{ label: '上传地址', name: 'ipCenter', width: 80},
			{ label: '上传端口', name: 'portCenter', width: 60}
        ],
		viewrecords: true,
        height: '100%',
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: false, 
        //rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.records",
            page: "page.current",
            total: "page.pages",
            records: "page.total"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});
