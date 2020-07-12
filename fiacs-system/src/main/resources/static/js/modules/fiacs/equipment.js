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
    	equipTreeName:null,
        showList: 0,
        title: null,
        paramQuery:0,
        belongDeptId:null,//加入数据库所属机构
        belongDeptName:null,
        equipment:{
            deptId:null,
            deptName:null,
            subnetMask:null,
            address:1
        },
        operateList:[],
        paramQueryList:[{
        	id:'0',
        	name:'全部'
        },{
        	id:'1',
        	name:'未入数据库'
        }],
        accessTypeList:[{
        	id:'0',
        	name:'双门互锁'
        },{
        	id:'1',
        	name:'四门互锁'
        }],
        siteTypeArray: new Array(
        	'营业网点',
        	'中心金库')
    },
    methods: {
        getDept: function(){
            //加载部门树
            $.get(baseURL + "fiacs/dept/select", function(r){
                ztree = $.fn.zTree.init($("#deptTree"), setting, r.deptList);
                var node = ztree.getNodeByParam("deptId", vm.equipment.officeId);
                if(node != null){
                	ztree.selectNode(node);

                	vm.equipment.deptName = node.name;
                }
            })
        },
        resetEquip:function(type){
			vm.equipment.resetType=type;
			showLoad();
			$.ajax({
				type: "POST",
				url: baseURL + 'fiacs/equip/resetEquip',
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
		},
        queryEquipTree:function(){
			$('#deptTable').bootstrapTreeTable('expendNodeByName',vm.equipTreeName); 
		},
        getRoleDept: function(){
            //加载部门树
            $.get(baseURL + "fiacs/dept/select", function(r){
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
            vm.equipment = {accessType:0,equipType:0,officeId:null,officeName:null,subnetMask:'0.0.0.0',equipIP:'192.168.1.162',equipPort:"6000",centerIP:'192.168.1.8',centerPort:'10086',gateway:'192.168.1.1'};
            vm.getDept();
        },
        del: function () {
        	var selected = $('#deptTable').bootstrapTreeTable('getSelections');
       	 if(selected==null||selected.length==0){
       		 alert("请选择数据");
             	return;
       	 }

            confirm('确定要删除选中的记录？', function(){
            	 $.get(baseURL + "fiacs/equip/deleteEquip/"+selected[0].id, function(r){
                     if(r.code!=0){
                     	alert("删除失败");
                     	return;
                     }
                     alert("删除成功");
                     Dept.table.refresh();
            	 });
            });
        },
        look: function(){
        	 var selected = $('#deptTable').bootstrapTreeTable('getSelections');
        	 if(selected==null||selected.length==0){
        		 alert("请选择数据");
              	return;
        	 }
        	 $.get(baseURL + "fiacs/equip/infoOfficeId/"+selected[0].id, function(r){
                 if(r.code!=0){
                 	alert("加载详情错误");
                 	return;
                 }
                 if(r.equipment==null){
                	 alert("加载详情错误");
                  	return;
                 }
                 vm.showList = 1;
                 vm.title = "修改";
                 vm.equipment = r.equipment;
                 vm.getDept();
             });
        },
        saveOrUpdate: function (event) {
        	showLoad();
            var url = vm.equipment.id == null ? "fiacs/equip/saveEquip" : "fiacs/equip/updateEquip";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.equipment),
                success: function(r){
                	closeLoad();
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
                    if(node[0].siteType==0||node[0].siteType==1){
                    	alert('只有中心金库、营业网点、营业子网点才能关联设备');
                    	return;
                    }
                    vm.equipment.officeId = node[0].deptId;
                    vm.equipment.officeName = node[0].name;
                    if(node[0].siteType==2){
                    	vm.equipment.siteType = 1;
                    	
                    }else{
                    	vm.equipment.siteType = 0;
                    }
                    vm.equipment.equipName = node[0].name;
                    layer.close(index);
                }	
            });
        },
        reload: function () {
            vm.showList = 0;
            Dept.table.refresh();
           // $('#deptTable').bootstrapTreeTable('refresh'); 
        },
        equipQueryDiv:function(){
        	
        },
        equipQuery:function(){
        	
        },
        saveDataBase: function(){
        	var grid = $("#jqGridEquip");
        	var rowKey = grid.getGridParam("selrow");
        	if (!rowKey) {
        		alert("请选择一条记录");
        		return;
        	}
			var roleIds = grid.getGridParam("selarrrow");
			if(roleIds == null){
				alert('请选择要保存的设备');
			}else if(roleIds.length>1){
				alert('只能选择一个设备进行保存');
			}else{
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
	                    
	    				confirm("会根据sn码覆盖之前的设备信息，确定保存吗?",function(){
	    					var obj = $("#jqGridEquip").jqGrid("getRowData",roleIds[0])[0];
	    					obj.operate=null;
	    					showLoad();
	    					$.ajax({
	    						type: "POST",
	    					    url: baseURL + 'fiacs/equip/saveDataBase/'+vm.belongDeptId,
	    		                contentType: "application/json",
	    					    data: JSON.stringify(obj),
	    					    success: function(r){
	    					    	closeLoad();
	    					    	if(r.code === 0){
	    					    		layer.close(index);
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
		modifyEquip:function(rowKey){
			vm.equipment = $("#jqGridEquip").jqGrid("getRowData",rowKey);
			vm.equipment.operate=null;
			//vm.equipment = rows;
			vm.title="(设备sn码："+vm.equipment.equipSn+")";
			vm.showList=4;
		},
		saveEquipBaseInfo:function(){
			showLoad();
			$.ajax({
				type: "POST",
			    url: baseURL + 'fiacs/equip/synEquipInfo',
                contentType: "application/json",
			    data: JSON.stringify(vm.equipment),
			    success: function(r){
			    	closeLoad();
			    	if(r.code === 0){
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
			//vm.equipQuery();
			vm.showList=2;
		},
		resetEquip:function(type){
			vm.equipment.resetType=type;
			showLoad();
			$.ajax({
				type: "POST",
				url: baseURL + 'fiacs/equip/resetEquip',
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
        {title: '机构名称', field: 'name', align: 'center', valign: 'middle', sortable: true, width: '110px'},
        {title: '序列号', field: 'equipSn', align: 'center', valign: 'middle', sortable: true, width: '90px'},
        /*{title: '设备名称', field: 'equipName', align: 'center', valign: 'middle', sortable: true, width: '60px',hidden:true},*/
        {title: '网络地址', field: 'equipIP', align: 'center', valign: 'middle', sortable: true, width: '80px'},
        {title: '端口', field: 'equipPort', align: 'center', valign: 'middle', sortable: true, width: '40px'},
        {title: '网关', field: 'gateway', align: 'center', valign: 'middle', sortable: true, width: '65px'},
        {title: '子网掩码', field: 'subnetMask', align: 'center', valign: 'middle', sortable: true, width: '70px'},
        {title: '中心ip', field: 'centerIP', align: 'center', valign: 'middle', sortable: true, width: '70px'},
        {title: '中心端口', field: 'centerPort', align: 'center', valign: 'middle', sortable: true, width: '60px'}/*,
        {title: '打印ip', field: 'printIP', align: 'center', valign: 'middle', sortable: true, width: '80px'},
        {title: '打印端口', field: 'printPort', align: 'center', valign: 'middle', sortable: true, width: '80px'}*/
     ]
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
    //$.get(baseURL + "sys/dept/info", function(r){
        var colunms = Dept.initColumn();
        var table = new TreeTable(Dept.id, baseURL + "fiacs/dept/list", colunms);
       // table.setRootCodeValue(r.deptId);
        table.setRootCodeValue(0);
        table.setExpandColumn(1);
        table.setIdField("deptId");
        table.setCodeField("deptId");
        table.setParentCodeField("parentId");
        table.setExpandAll(false);
        table.setHeight(window.innerHeight-95);
        table.init();
        Dept.table = table;
  //  });
    
    $('#'+Dept.id).bind('click',function(){
    	 $('#deptTable').bootstrapTreeTable('expendNode'); 
    	 var selected = $('#deptTable').bootstrapTreeTable('getSelections');
    	 selectRow=selected[0].id;
    });

});
