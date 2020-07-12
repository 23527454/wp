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
                    vm.equipment.officeId = node[0].deptId;
                    vm.equipment.officeName = node[0].name;
                    layer.close(index);
                }
            });
        },
        reload: function () {
            vm.showList = 0;
            Dept.table.refresh();
           // $('#deptTable').bootstrapTreeTable('refresh'); 
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
			/*var roleIds = grid.getGridParam("selarrrow");
			if(roleIds == null){
				alert('请选择要保存的设备');
			}else if(roleIds.length>1){
				alert('只能选择一个设备进行保存');
			}else{}*/

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
                    
    				confirm("会根据sn码覆盖之前的设备信息，确定保存吗?",function(){
    					//var obj = [];
    					//obj.push($("#jqGridEquip").jqGrid("getRowData",roleIds[0]));
    					var obj = $("#jqGridEquip").jqGrid("getRowData",rowKey);
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
							vm.showList=0;
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		backEquipDiv:function(){
			//vm.equipQuery();
			vm.showList=0;
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
	 $("#jqGridEquip").jqGrid({
	        url: baseURL + 'fiacs/equip/list',
	        postData:{"type":0},
			datatype:'json',
	        colModel: [			
				{ label: '序列号', name: 'equipSn', index: "equipSn", width: 160, key: true},
				{ label: '操作',name: 'operate', formatter: function (value, grid, rows, state) { 
					return "<a href=\"#\" onclick=\"vm.modifyEquip(" + rows.equipSn + ")\">修改</a>" } 
				},
				{ label: '所属机构', name: 'officeName', width: 120},
				{label: '机构id',name:'officeId',hidden:true},
				{ label: '设备名', name: 'equipName', width: 120 },
				{ label: '网络地址', name: 'equipIP', width: 120 },
	            { label: '通信端口', name: 'equipPort',width: 80},
				{ label: '网关地址', name: 'gateway',width: 120},
				{ label: '中心地址', name: 'centerIP',width: 120},
				{ label: '中心端口', name: 'centerPort',width: 80 },
				{ label: '子网掩码', name: 'subnetMask',width: 120},
				{ label: '设备类型', name: 'equipType',hidden: true},
				{ label: '打印地址', name: 'printIP',hidden: true},
				{ label: '原始地址', name: 'oldIP',hidden: true},
				{ label: '原始端口', name: 'oldPort',hidden: true},
				{ label: '打印端口', name: 'printPort',hidden: true}
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
	        	var gridId="jqGridEquip";
				$("#cb_"+gridId).hide();//隐藏全选按钮
				$("#jqGridEquip").find("td[aria-describedby='"+gridId+"_cb']").find("input[type='checkbox']").prop("type","radio") ;//将checkbox替换为radio
				$("#jqGridEquip").find("td[aria-describedby='"+gridId+"_cb']").find("input[type='radio']").prop("name",gridId);//radio设为同一名字
	        },
	        beforeSelectRow:function(){
	        	$("#jqGridEquip").jqGrid('clearSelect'); //执行自定义的函数（下面会讲）                
	 			$("#jqGridEquip").find(".ui-state-highlight").removeClass("ui-state-highlight").removeAttr("aria-selected"); //样式控制  
	 			 return true;
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
});
