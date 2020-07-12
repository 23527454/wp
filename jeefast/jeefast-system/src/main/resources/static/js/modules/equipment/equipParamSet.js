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
        isReadOnly:false,
        equipTreeName:null,
		upload:0,
		newTag:null,
		requestTag:null,
		loadTotal:false,
		selectChangeStatus:false,
        equipQueryList:false,//是否进入过设备搜索界面
        operateSelect:null,
        equipment:{
            deptId:null,
            deptName:null,
            subnetMask:null,
            address:1
        },
        zjTypeList:[
            {
                id: '0',
                name: '翼闸'
            },
            {
                id: '1',
                name: '摆闸'
            }
        ],
        djTypeList:[
            {
                id: '0',
                name: '直流电机'
            },
            {
                id: '1',
                name: '伺服电机'
            }
        ],
        zjModelList:[
            {
                id: '0',
                name: '常闭'
            },
            {
                id: '1',
                name: '常开'
            }
        ],
        crossModelList:[
            {
                id: '0',
                name: '刷卡'
            },
            {
                id: '1',
                name: '自由'
            },{
            	id:'2',
            	name:'禁止'
            }
        ],
        rememberList:[
            {
                id: '0',
                name: '不启用'
            },
            {
                id: '1',
                name: '启用'
            }
        ],
        zdModelList:[
            {
                id: '1',
                name: '反弹一个角度'
            },
            {
                id: '2',
                name: '速度、力矩减小'
            }
        ],
        babyCrossList:[
            {
                id: '0',
                name: '关闭'
            },
            {
                id: '1',
                name: '开启'
            }
        ],
        fxCrossList:[
            {
                id: '0',
                name: '关闸'
            },
            {
                id: '1',
                name: '不关闸'
            }
        ],
        zjWorkModelList:[
            {
                id: '0',
                name: '正常模式'
            },
            {
                id: '1',
                name: '老化模式'
            },
            {
                id: '2',
                name: '紧急通行'
            },
            {
                id: '3',
                name: '指令模式'
            },
            {
                id: '4',
                name: '停机维护'
            }
        ],
        alarmList:[
            {
                id: '0',
                name: '开启'
            },
            {
                id: '1',
                name: '关闭'
            }
        ],
        dbclList:[
            {
                id: '0',
                name: '亚克力300'
            },
            {
                id: '1',
                name: '金刚玻璃300'
            },
            {
                id: '2',
                name: '亚克力400'
            },
            {
                id: '3',
                name: '金刚玻璃400'
            },
            {
                id: '4',
                name: '亚克力500'
            },
            {
                id: '5',
                name: '金刚玻璃500'
            },
            {
                id: '6',
                name: '亚克力600'
            },
            {
                id: '7',
                name: '金刚玻璃600'
            }
        ],
        remark:[
        	{id:'2',name:'通行记录'},
        	{id:'3',name:'异常事件记录'},
        	{id:'4',name:'报警记录'}
        ],
        resouce :[
        	[
        		{id:'1',name:'左向刷卡'},
	    		{id:'2',name:'右向刷卡'},
	        	{id:'3',name:'左向通行'},
	        	{id:'4',name:'右向通行'},
	        	{id:'5',name:'左向等待超时'},
	        	{id:'6',name:'右向等待超时'}
        	],[
        		{id:'1',name:'主电机运行异常'},
        		{id:'2',name:'副电机运行异常'},
            	{id:'3',name:'红外检测异常'},	
            	{id:'4',name:'通讯异常'},
        		{id:'5',name:'驱动器异常'}
        	],
        	[
        		{id:'1',name:'左闯入报警'},
        		{id:'2',name:'右闯入报警'},
            	{id:'3',name:'左尾随报警'},
            	{id:'4',name:'右尾随报警'},
            	{id:'5',name:'左滞留报警'},
            	{id:'6',name:'右滞留报警'},
            	{id:'7',name:'左潜回报警'},
            	{id:'8',name:'右潜回报警'},
            	{id:'9',name:'挡板运行阻挡'},
            	{id:'10',name:'暴力闯闸'},
            	{id:'11',name:'停机强推'}
        	]
        	
        ],
        exceptionInfo:[
        	[
        		{id:'0',name:'左到位检测异常'},
        		{id:'1',name:'右到位检测异常'},
            	{id:'2',name:'零位检测异常'},	
            	{id:'3',name:'通讯异常'},
        		{id:'4',name:'HALL检测异常'}
        	],
        	[
        		{id:'0',name:'左到位检测异常'},
        		{id:'1',name:'右到位检测异常'},
            	{id:'2',name:'零位检测异常'},	
            	{id:'3',name:'通讯异常'},
        		{id:'4',name:'HALL检测异常'}
        	],
        	[
        		{id:'0',name:'1号红外异常'},
        		{id:'1',name:'2号红外异常'},
            	{id:'2',name:'3号红外异常'},	
            	{id:'3',name:'4号红外异常'},
        		{id:'4',name:'5号红外异常'},
        		{id:'5',name:'6号红外异常'},
        		{id:'6',name:'7号红外异常'},
            	{id:'7',name:'8号红外异常'},	
            	{id:'8',name:'9号红外异常'},
        		{id:'9',name:'10号红外异常'},
        		{id:'10',name:'11号红外异常'},
        		{id:'11',name:'12号红外异常'},
            	{id:'12',name:'13号红外异常'},	
            	{id:'13',name:'14号红外异常'},
        		{id:'14',name:'15号红外异常'},
        		{id:'15',name:'16号红外异常'},
        		{id:'16',name:'17号红外异常'},
            	{id:'17',name:'18号红外异常'},	
            	{id:'18',name:'19号红外异常'},
        		{id:'19',name:'20号红外异常'},
        		{id:'20',name:'21号红外异常'},
        		{id:'21',name:'22号红外异常'},
            	{id:'22',name:'23号红外异常'},	
            	{id:'23',name:'24号红外异常'}
        	],
        	[
        		{id:'0',name:'主副控制器通讯异常'},
        		{id:'1',name:'控制器与驱动器通讯异常'},
            	{id:'2',name:'主副驱动器通讯异常'}
        	],
        	[
        		{id:'0',name:'主驱动器运行异常'},
        		{id:'1',name:'从驱动器运行异常'}
        	]
        ],
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
        zhajiStatus:[
        	{
        		id:'0',
        		name:'闸机失能状态'
        	},{
        		id:'1',
        		name:'驱动器找零点中'
        	},{
        		id:'2',
        		name:'正向开门中'
        	},{
        		id:'3',
        		name:'反向开门中'
        	},{
        		id:'4',
        		name:'正向关门中'
        	},{
        		id:'5',
        		name:'反向关门中'
        	},{
        		id:'6',
        		name:'正向开门到位'
        	},{
        		id:'7',
        		name:'反向开门到位'
        	},{
        		id:'8',
        		name:'关门到位'
        	},{
        		id:'9',
        		name:'运行阻挡'
        	},{
        		id:'10',
        		name:'停机强推'
        	},{
        		id:'11',
        		name:'暴力闯闸'
        	},{
        		id:'12',
        		name:'闸机急停'
        	},{
        		id:'13',
        		name:'CAN通讯超时'
        	},{
        		id:'14',
        		name:'对轴运行阻挡'
        	},{
        		id:'15',
        		name:'对轴停机强推'
        	},{
        		id:'16',
        		name:'对轴暴力闯闸'
        	}
        ],
        qdExceptionList:[
        	{
        		id:'0',
        		name:'无异常'
        	},{
        		id:'1',
        		name:'上电HALL出错'
        	},{
        		id:'2',
        		name:'EEPROM出错'
        	},{
        		id:'3',
        		name:'转堵'
        	},{
        		id:'4',
        		name:'位置偏差过大'
        	},{
        		id:'5',
        		name:'识别缺相'
        	},{
        		id:'6',
        		name:'识别反向'
        	},{
        		id:'7',
        		name:'识别Z丢失'
        	},{
        		id:'8',
        		name:'识别Hall丢失'
        	},{
        		id:'9',
        		name:'Z丢失'
        	},{
        		id:'10',
        		name:'V相电流校零出错'
        	},{
        		id:'11',
        		name:'U相电流校零出错'
        	},{
        		id:'12',
        		name:'欠压'
        	},{
        		id:'13',
        		name:'过压'
        	},{
        		id:'14',
        		name:'过温'
        	},{
        		id:'15',
        		name:'过载'
        	},{
        		id:'16',
        		name:'过流'
        	}
        ],
        zhajiCrossStatus:[
        	{
        		id:'0',
        		name:'空闲状态'
        	},{
        		id:'1',
        		name:'左向刷卡'
        	},{
        		id:'2',
        		name:'右向刷卡'
        	},{
        		id:'3',
        		name:'左向进入'
        	},{
        		id:'4',
        		name:'右向进入'
        	},{
        		id:'5',
        		name:'左向出闸'
        	},{
        		id:'6',
        		name:'右向出闸'
        	},{
        		id:'7',
        		name:'左向等待超时'
        	},{
        		id:'8',
        		name:'右向等待超时'
        	},{
        		id:'9',
        		name:'左向闯入'
        	},{
        		id:'10',
        		name:'右向闯入'
        	},{
        		id:'11',
        		name:'左向尾随'
        	},{
        		id:'12',
        		name:'右向尾随'
        	},{
        		id:'13',
        		name:'左向滞留'
        	},{
        		id:'14',
        		name:'右向滞留'
        	},{
        		id:'15',
        		name:'左向潜回'
        	},{
        		id:'16',
        		name:'右向潜回'
        	}
        ],
        controllerStatus:[
        	{
        		id:'0',
        		name:'正常'
        	},{
        		id:'1',
        		name:'主副控制器通讯异常'
        	},{
        		id:'2',
        		name:'驱动器通讯异常'
        	},{
        		id:'3',
        		name:'上电红外检测异常'
        	},{
        		id:'4',
        		name:'HALL检测异常'
        	},{
        		id:'5',
        		name:'马达运行异常'
        	}
        ],
        heStatusList:[
        	{
        		id:'3',
        		name:'左到位(主)'
        	},{
        		id:'2',
        		name:'右到位(主)'
        	},{
        		id:'1',
        		name:'零位(主)'
        	},{
        		id:'7',
        		name:'左到位(从)'
        	},{
        		id:'6',
        		name:'右到位(从)'
        	},{
        		id:'5',
        		name:'零位(从)'
        	}
        ]
    },
    methods: {
        getDept: function(){
            //加载部门树
            $.get(baseURL + "sys/dept/select", function(r){
                ztree = $.fn.zTree.init($("#deptTree"), setting, r.deptList);
                var node = ztree.getNodeByParam("deptId", vm.equipment.deptId);
                if(node != null){
                	ztree.selectNode(node);

                	vm.equipment.deptName = node.name;
                }
            })
        },
        add: function(){
            vm.showList = 1;
            vm.title = "新增";
            vm.equipment = { deptId:null,deptName:null,subnetMask:'0.0.0.0',address:1};
            vm.getDept();
        },
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
        	vm.showList=2;
        	if(!vm.equipQueryList){
        		vm.equipQueryList=true;
        		 $("#jqGridEquip").jqGrid({
        		        url: baseURL + 'searchEquipment/list',
        		        datatype: "local",
        		        colModel: [			
        					{ label: 'sn码', name: 'equipSn', index: "equipSn", width: 140, key: true },
        					{ label: '网络地址', name: 'ip', index: "ip" },
        		            { label: '通信端口', name: 'port',width: 130},
        					{ label: '网关地址', name: 'gateWay'},
        					{ label: '上传地址', name: 'ipCenter'},
        					{ label: '上传端口', name: 'portCenter',width: 130 },
        					{ label: '序列号', name: 'equipReq',width: 200},
        					{ label: '设备地址', name: 'address', hidden:true },
        					{ label: '原ip', name: 'oldIp', hidden:true },
        					{ label: '原端口', name: 'oldPort', hidden:true },
        					{ label: '子网掩码', name: 'subnetMask', hidden:true }
        					
        		        ],
        				viewrecords: true,
        		        height: 370,
        		        rowNum: 1000,
        				rowList : [1000],
        		        rownumbers: true, 
        		        rownumWidth: 25, 
        		        autowidth:true,
        		        multiselect: true,
        		        multiboxonly:true,
        		      //  pager: "#jqGridPager",
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
        		        	if(xhr.code&&xhr.code!=0){
        		        		alert(xhr.msg);
        		        	}
        		        }
        		        
        		    });
        	}
        },
        equipQuery:function(){
        	var page = $("#jqGridEquip").jqGrid('getGridParam','page');
			$("#jqGridEquip").jqGrid('setGridParam',{ 
				datatype:'json'
            }).trigger("reloadGrid");
        },
        synEquipTime:function(){
        	showLoad();
        	$.ajax({
				type: "POST",
				url: baseURL + 'searchEquipment/settingInfo/synEquipTime',
				contentType: "application/json",
				data: JSON.stringify(vm.equipment),
				success: function(r){
					closeLoad();
					if(r.code === 0){
						alert('操作成功');
						vm.equipmentBack=JSON.parse(JSON.stringify(vm.equipment));
					}else{
						alert(r.msg);
					}
				}
			});
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
				confirm("确定保存所选设备数据吗?",function(){
					showLoad();
					$.ajax({
						type: "POST",
					    url: baseURL + 'searchEquipment/saveDataBase',
		                contentType: "application/json",
					    data: JSON.stringify(obj),
					    success: function(r){
					    	closeLoad();
					    	if(r.code === 0){
					    		//vm.equipment = r.equipment;
								alert('保存成功');
							}else{
								alert(r.msg);
							}
						}
					});
				});
			}
			
		},
		operateEquip:function(operateSelect){
			showLoad();
			$.ajax({
				type: "POST",
				url: baseURL + 'searchEquipment/operateEquip/'+operateSelect,
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
		},
		inputChange:function(paramType){
			var request = null;
			showLoad();
			if(paramType=='zmdWorkSpeed'){
				if(!vm.equipment.zmdWorkSpeed){
					alert('参数必填，不能为空');
					vm.equipment.zmdWorkSpeed=vm.equipmentBack.zmdWorkSpeed;
				}else{
					if(vm.equipment.zmdWorkSpeed<1||vm.equipment.zmdWorkSpeed>100){
						alert("主马达最大运行速度范围1~100");
						vm.equipment.zmdWorkSpeed=vm.equipmentBack.zmdWorkSpeed;
					}else{
						request=1;
					}
				}
				
			}else if(paramType=='fmdWorkSpeed'){
				if(!vm.equipment.fmdWorkSpeed){
					alert('参数必填，不能为空');
					vm.equipment.fmdWorkSpeed=vm.equipmentBack.fmdWorkSpeed;
				}else{
					if(vm.equipment.fmdWorkSpeed<1||vm.equipment.fmdWorkSpeed>100){
						alert("副马达最大运行速度范围1~100");
						vm.equipment.fmdWorkSpeed=vm.equipmentBack.fmdWorkSpeed;
					}else{
						request=1;
					}
				}
			}else if(paramType=='mdMaxWorkTime'){
				if(!vm.equipment.mdMaxWorkTime){
					alert('参数必填，不能为空');
					vm.equipment.mdMaxWorkTime=vm.equipmentBack.mdMaxWorkTime;
				}else{
					if(vm.equipment.mdMaxWorkTime<4||vm.equipment.mdMaxWorkTime>20){
						alert("马达最大运行时间范围4~20");
						vm.equipment.mdMaxWorkTime=vm.equipmentBack.mdMaxWorkTime;
					}else{
						request=1;
					}
				}
			}else if(paramType=='hwjcTime'){
				if(!vm.equipment.hwjcTime){
					alert('参数必填，不能为空');
					vm.equipment.hwjcTime=vm.equipmentBack.hwjcTime;
				}else{
					if(vm.equipment.hwjcTime<1||vm.equipment.hwjcTime>10){
						alert("红外检测时间范围1~10");
						vm.equipment.hwjcTime=vm.equipmentBack.hwjcTime;
						return;
					}else{
						request=1;
					}
				}
			}else if(paramType=='txjgTime'){
				if(!vm.equipment.txjgTime){
					alert('参数必填，不能为空');
					vm.equipment.txjgTime=vm.equipmentBack.txjgTime;
				}else{
					if(vm.equipment.txjgTime<0||vm.equipment.txjgTime>255){
						alert("通行隔离时间范围0~255");
						vm.equipment.txjgTime=vm.equipmentBack.txjgTime;
					}else{
						request=1;
					}
				}
			}else if(paramType=='ddryjrTime'){
				if(!vm.equipment.ddryjrTime){
					alert('参数必填，不能为空');
					vm.equipment.ddryjrTime=vm.equipmentBack.ddryjrTime;
				}else{
					if(vm.equipment.ddryjrTime<5||vm.equipment.ddryjrTime>255){
						alert("等待人员进入时间范围5~255");
						vm.equipment.ddryjrTime=vm.equipmentBack.ddryjrTime;
					}else{
						request=1;
					}
				}
			}else if(paramType=='ryzlTime'){
				if(!vm.equipment.ryzlTime){
					alert('参数必填，不能为空');
					vm.equipment.ryzlTime=vm.equipmentBack.ryzlTime;
				}else{
					if(vm.equipment.ryzlTime<60||vm.equipment.ryzlTime>255){
						alert("人员滞留时间范围60~255");
						vm.equipment.ryzlTime=vm.equipmentBack.ryzlTime;
					}else{
						request=1;
					}
				}
			}else if(paramType=='ysgzTime'){
				if(!vm.equipment.ysgzTime){
					alert('参数必填，不能为空');
					vm.equipment.ysgzTime=vm.equipmentBack.ysgzTime;
				}else{
					if(vm.equipment.ysgzTime<0||vm.equipment.ysgzTime>255){
						alert("延时关闸时间范围0~255");
						vm.equipment.ysgzTime=vm.equipmentBack.ysgzTime;
					}else{
						request=1;
					}
				}
			}else if(paramType=='zytxjgTime'){
				if(!vm.equipment.zytxjgTime){
					alert('参数必填，不能为空');
					vm.equipment.zytxjgTime=vm.equipmentBack.zytxjgTime;
				}else{
					if(vm.equipment.zytxjgTime<0||vm.equipment.zytxjgTime>255){
						alert("自由通行间隔时间范围0~255");
						vm.equipment.zytxjgTime=vm.equipmentBack.zytxjgTime;
					}else{
						request=1;
					}
				}
			}else if(paramType=='zkzSpeed'||paramType=='ckzSpeed'){
				if(!vm.equipment.zkzSpeed||!vm.equipment.ckzSpeed){
					alert('参数必填，不能为空');
					vm.equipment.zkzSpeed=vm.equipmentBack.zkzSpeed;
					vm.equipment.ckzSpeed=vm.equipmentBack.ckzSpeed;
				}else{
					if(vm.equipment.zkzSpeed<1||vm.equipment.zkzSpeed>5000){
						alert("主开闸速度范围1~5000");
						vm.equipment.zkzSpeed=vm.equipmentBack.zkzSpeed;
					}else if(vm.equipment.ckzSpeed<1||vm.equipment.ckzSpeed>5000){
						alert("从开闸速度范围1~5000");
						vm.equipment.ckzSpeed=vm.equipmentBack.ckzSpeed;
					}else{
						request=1;
					}
				}
			}
			else if(paramType=='zgzSpeed'||paramType=='cgzSpeed'){
				if(!vm.equipment.zgzSpeed||!vm.equipment.cgzSpeed){
					alert('参数必填，不能为空');
					vm.equipment.zgzSpeed=vm.equipmentBack.zgzSpeed;
					vm.equipment.cgzSpeed=vm.equipmentBack.cgzSpeed;
				}else{
					if(vm.equipment.zgzSpeed<1||vm.equipment.zgzSpeed>5000){
						alert("主关闸速度范围1~5000");
						vm.equipment.zgzSpeed=vm.equipmentBack.zgzSpeed;
					}else if(vm.equipment.cgzSpeed<1||vm.equipment.cgzSpeed>5000){
						alert("从关闸速度范围1~5000");
						vm.equipment.cgzSpeed=vm.equipmentBack.cgzSpeed;
					}else{
						request=1;
					}
				}
			}else if(paramType=='zzdElectric'||paramType=='czdElectric'){
				if(!vm.equipment.zzdElectric||!vm.equipment.czdElectric){
					alert('参数必填，不能为空');
					vm.equipment.zzdElectric=vm.equipmentBack.zzdElectric;
					vm.equipment.czdElectric=vm.equipmentBack.czdElectric;
				}else{
					if(vm.equipment.zzdElectric<0||vm.equipment.zzdElectric>1000){
						alert("主阻挡电流范围0~1000");
						vm.equipment.zzdElectric=vm.equipmentBack.zzdElectric;
					}else if(vm.equipment.czdElectric<0||vm.equipment.czdElectric>1000){
						alert("从阻挡电流范围0~1000");
						vm.equipment.czdElectric=vm.equipmentBack.czdElectric;
					}else{
						request=1;
					}
				}
			}else if(paramType=='zkzTime'||paramType=='ckzTime'){
				if(!vm.equipment.zkzTime||!vm.equipment.ckzTime){
					alert('参数必填，不能为空');
					vm.equipment.zkzTime=vm.equipmentBack.zkzTime;
					vm.equipment.ckzTime=vm.equipmentBack.ckzTime;
				}else{
					if(vm.equipment.zkzTime%50!=0){
						alert("主开闸时间只可50的数据量递增或递减");
						vm.equipment.zkzTime=vm.equipmentBack.zkzTime;
					}else if(vm.equipment.ckzTime%50!=0){
						alert("从开闸时间只可50的数据量递增或递减");
						vm.equipment.ckzTime=vm.equipmentBack.ckzTime;
					}else{
						request=1;
					}
				}
			}else if(paramType=='zgzTime'||paramType=='cgzTime'){
				if(!vm.equipment.zgzTime||!vm.equipment.cgzTime){
					alert('参数必填，不能为空');
					vm.equipment.zgzTime=vm.equipmentBack.zgzTime;
					vm.equipment.cgzTime=vm.equipmentBack.cgzTime;
				}else{
					if(vm.equipment.zgzTime%50!=0){
						alert("主关闸时间只可50的数据量递增或递减");
						vm.equipment.zgzTime=vm.equipmentBack.zgzTime;
					}else if(vm.equipment.cgzTime%50!=0){
						alert("从关闸时间只可50的数据量递增或递减");
						vm.equipment.cgzTime=vm.equipmentBack.cgzTime;
					}else{
						request=1;
					}
				}
			}else if(paramType=='zkzAngle'||paramType=='ckzAngle'){
				if(!vm.equipment.zkzAngle||!vm.equipment.ckzAngle){
					alert('参数必填，不能为空');
					vm.equipment.zkzAngle=vm.equipmentBack.zkzAngle;
					vm.equipment.ckzAngle=vm.equipmentBack.ckzAngle;
				}else{
					if(vm.equipment.zkzAngle<0||vm.equipment.zkzAngle>1800){
						alert("主开闸角度范围1~1800");
						vm.equipment.zkzAngle=vm.equipmentBack.zkzAngle;
					}else if(vm.equipment.ckzAngle<0||vm.equipment.ckzAngle>1800){
						alert("从开闸角度范围1~1800");
						vm.equipment.ckzAngle=vm.equipmentBack.ckzAngle;
					}else{
						request=1;
					}
				}
			}else if(paramType=='qtmcs'){
				if(!vm.equipment.qtmcs){
					alert('参数必填，不能为空');
					vm.equipment.qtmcs=vm.equipmentBack.qtmcs;
				}else{
					if(vm.equipment.qtmcs<0||vm.equipment.qtmcs>65535){
						alert("强推脉冲数范围0-65535");
						vm.equipment.qtmcs=vm.equipmentBack.qtmcs;
					}else{
						request=1;
					}
				}
			}else if(paramType=='qthfTime'){
				if(!vm.equipment.qthfTime){
					alert('参数必填，不能为空');
					vm.equipment.qthfTime=vm.equipmentBack.qthfTime;
				}else{
					if(vm.equipment.qthfTime<1000||vm.equipment.qthfTime>65535){
						alert("强推脉冲数范围1000-65535");
						vm.equipment.qthfTime=vm.equipmentBack.qthfTime;
					}else{
						request=1;
					}
				}
			}else if(paramType=='kzjgTime'||paramType=='gzjgTime'){
				if(!vm.equipment.kzjgTime||!vm.equipment.gzjgTime){
					alert('参数必填，不能为空');
					vm.equipment.kzjgTime=vm.equipmentBack.kzjgTime;
					vm.equipment.gzjgTime=vm.equipmentBack.gzjgTime;
				}else{
					if(vm.equipment.kzjgTime<0||vm.equipment.kzjgTime>255){
						alert("主开闸间隔时间范围0~255");
						vm.equipment.kzjgTime=vm.equipmentBack.kzjgTime;
					}else if(vm.equipment.gzjgTime<0||vm.equipment.gzjgTime>255){
						alert("从开闸间隔时间范围0~255");
						vm.equipment.gzjgTime=vm.equipmentBack.gzjgTime;
					}else{
						request=1;
					}
				}
			}else if(paramType=='zdftAngle'){
				if(!vm.equipment.zdftAngle){
					alert('参数必填，不能为空');
					vm.equipment.zdftAngle=vm.equipmentBack.zdftAngle;
				}else{
					if(vm.equipment.zdftAngle<0||vm.equipment.zdftAngle>900){
						alert("阻挡反弹角度范围0-900");
						vm.equipment.zdftAngle=vm.equipmentBack.zdftAngle;
					}else{
						request=1;
					}
				}
			}
			if(request){
				$.ajax({
					type: "POST",
					url: baseURL + 'searchEquipment/settingInfo/'+paramType,
					contentType: "application/json",
					data: JSON.stringify(vm.equipment),
					success: function(r){
						closeLoad();
						if(r.code === 0){
							alert('操作成功');
							vm.equipmentBack=JSON.parse(JSON.stringify(vm.equipment));
						}else{
							alert(r.msg);
						}
					}
				});
			}else{
				closeLoad();
			}
		},
		selectChange:function(paramType,value){
			if(vm.equipment.zjType&&vm.newTag=='工作模式'&&(paramType=='zjType'||paramType=='djType'
				||paramType=='zjModel'||paramType=='leftCross'||paramType=='rightCross'||paramType=='remember'
					||paramType=='fxwpCross'||paramType=='babyCross'||paramType=='zjWorkModel'||paramType=='yzwsgz')){
				showLoad();
				$.ajax({
					type: "POST",
					url: baseURL + 'searchEquipment/settingInfo/'+paramType,
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
			else if(vm.newTag=='报警参数'&&vm.equipment.crAlarm&&(paramType=='crAlarm'||paramType=='wsAlarm'||paramType=='zlAlarm'
				||paramType=='zjAlarm'||paramType=='qhAlarm')){
				showLoad();
				$.ajax({
					type: "POST",
					url: baseURL + 'searchEquipment/settingInfo/'+paramType,
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
			else if(vm.newTag=='电机参数'&&vm.equipment.dbcl&&(paramType=='zdModel'||paramType=='dbcl')){
				showLoad();
				$.ajax({
					type: "POST",
					url: baseURL + 'searchEquipment/settingInfo/'+paramType,
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
			else if(paramType=='operate'){
				
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
		reloadRecord: function () {
			vm.upload = 1;
			var page = $("#readRecordGrid").jqGrid('getGridParam','page');
			$("#readRecordGrid").jqGrid('setGridParam',{ 
				datatype:'json'/*,
                postData:{'roleName':''},
                page:page*/
            }).trigger("reloadGrid");
		},
		queryRecord:function(){
			$("#readRecordGrid").jqGrid('setGridParam',{ 
				datatype:'json'
            }).trigger("reloadGrid");
		},
		refreshEquipTree:function(){
			Dept.table.refresh();
		},
		queryEquipTree:function(){
			/*var data ={param:vm.equipTreeName};
			var parms = {query:data};
			Dept.table.refresh(parms);*/
			$('#deptTable').bootstrapTreeTable('expendNodeByName',vm.equipTreeName); 
		},
		restoreEquip:function(){
			showLoad();
        	$.ajax({
				type: "POST",
				url: baseURL + 'searchEquipment/settingInfo/restore',
				contentType: "application/json",
				data: JSON.stringify(vm.equipment),
				success: function(r){
					closeLoad();
					if(r.code === 0){
						alert('操作成功');
						var equipSn = vm.equipment.equipSn;
						vm.equipment={};
			    		vm.equipment.equipSn = equipSn;
			    		$.ajax({
		    				type: "POST",
		    				url: baseURL + 'searchEquipment/readInfo/workmodel',
		    				contentType: "application/json",
		    				data: JSON.stringify(vm.equipment),
		    				success: function(r){
		    					if(r.code === 0){
		    						vm.equipment = r.equipment;
		    						vm.equipmentBack=JSON.parse(JSON.stringify(vm.equipment));
		    					}else{
		    						alert(r.msg);
		    					}
		    				}
		    			});
			    		$('#myTab a:first').tab('show');
					}else{
						alert(r.msg);
					}
				}
			});
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
        {title: '设备名称', field: 'name', align: 'center', valign: 'middle', sortable: true, width: '100px',formatter:treeFormat}
      ];
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
    		return;
    	}else{
    		selectRow=selected[0].id;
    		vm.equipment={};
    		vm.equipment.equipSn = selectRow;
    		if(vm.newTag=='工作模式'){
    			showLoad();
    			$.ajax({
    				type: "POST",
    				url: baseURL + 'searchEquipment/readInfo/workmodel',
    				contentType: "application/json",
    				data: JSON.stringify(vm.equipment),
    				success: function(r){
    					closeLoad();
    					if(r.code === 0){
    						vm.equipment = r.equipment;
    						vm.equipmentBack=JSON.parse(JSON.stringify(vm.equipment));
    					}else{
    						alert(r.msg);
    					}
    				}
    			});
    		}
    		$('#myTab a:first').tab('show');
    	}
    	
    });

    $('a[data-toggle="tab"]').on('show.bs.tab', function (e) {
    	//showLoad();
    	var selected = $('#deptTable').bootstrapTreeTable('getSelections');
    	if(selected.length==0){
    		alert('请选择设备');
    		return;
    	}
		var newTag = $(e.target).text();//打开的新页签名称
		vm.upload = 0;
		if(newTag=='工作模式'&&vm.newTag!=newTag){
			vm.newTag = newTag;
			if(vm.equipment&&vm.equipment.zjType){
				return;
			}
			requestType = "workmodel";
			showLoad();
			$.ajax({
				type: "POST",
				url: baseURL + 'searchEquipment/readInfo/'+requestType,
				contentType: "application/json",
				data: JSON.stringify(vm.equipment),
				success: function(r){
					closeLoad();
					if(r.code === 0){
						vm.equipment = r.equipment;
						vm.equipmentBack=JSON.parse(JSON.stringify(vm.equipment));
					}else{
						alert(r.msg);
					}
				}
			});
			
			
		}else if(newTag=='时间参数'&&vm.newTag!=newTag){
			vm.newTag = newTag;
			if(vm.equipment&&vm.equipment.zmdWorkSpeed){
				return;
			}
			requestType = "timeparams";
			showLoad();
			$.ajax({
				type: "POST",
				url: baseURL + 'searchEquipment/readInfo/'+requestType,
				contentType: "application/json",
				data: JSON.stringify(vm.equipment),
				success: function(r){
					closeLoad();
					if(r.code === 0){
						vm.equipment = r.equipment;
						vm.equipmentBack=JSON.parse(JSON.stringify(vm.equipment));
					}else{
						alert(r.msg);
					}
				}
			});
		}else if(newTag=='报警参数'&&vm.newTag!=newTag){
			vm.newTag = newTag;
			if(vm.equipment&&vm.equipment.crAlarm){
				return;
			}
			requestType = "alarmparams";
			showLoad();
			$.ajax({
				type: "POST",
				url: baseURL + 'searchEquipment/readInfo/'+requestType,
				contentType: "application/json",
				data: JSON.stringify(vm.equipment),
				success: function(r){
					closeLoad();
					if(r.code === 0){
						vm.equipment = r.equipment;
						vm.equipmentBack=JSON.parse(JSON.stringify(vm.equipment));
					}else{
						alert(r.msg);
					}
				}
			});
		}else if(newTag=='电机参数'&&vm.newTag!=newTag){
			vm.newTag = newTag;
			if(vm.equipment&&vm.equipment.dbcl){
				return;
			}
			requestType = "djparams";
			showLoad();
			$.ajax({
				type: "POST",
				url: baseURL + 'searchEquipment/readInfo/'+requestType,
				contentType: "application/json",
				data: JSON.stringify(vm.equipment),
				success: function(r){
					closeLoad();
					if(r.code === 0){
						vm.equipment = r.equipment;
						vm.equipmentBack=JSON.parse(JSON.stringify(vm.equipment));
					}else{
						alert(r.msg);
					}
				}
			});
		}else if(newTag=='信息查询'&&vm.newTag!=newTag){
			vm.newTag = newTag;
			if(vm.equipment&&vm.equipment.version){
				return;
			}
			requestType = "version";
			showLoad();
			$.ajax({
				type: "POST",
				url: baseURL + 'searchEquipment/readInfo/'+requestType,
				contentType: "application/json",
				data: JSON.stringify(vm.equipment),
				success: function(r){
					closeLoad();
					if(r.code === 0){
						vm.equipment = r.equipment;
					}else{
						alert(r.msg);
					}
				}
			});
		}else if(newTag=='读取状态'&&vm.newTag!=newTag){
			vm.newTag = newTag;
			/*if(vm.equipment&&vm.equipment.readstatus){
				return;
			}*/
			vm.upload=3;
			if(!vm.loadTotal){
				vm.loadTotal=true;
				showLoad();
			 $("#readStatusGrid").jqGrid({
			        url: baseURL + 'searchEquipment/readStatusInfo',
			        datatype: "json",
			        postData:{'ip': vm.equipment.ip,"port":vm.equipment.port,"equipSn":vm.equipment.equipSn},
			        colModel: [			
						{ label: '运行模式', name: 'zjWorkModel', index: "zjWorkModel",width:75},
						{ label: '红外状态', name: 'redLine', index: "redLine",width:130 },
						{ label: '霍尔状态', name: 'heStatus', index: "heStatus",width:100 },
						{ label: '闸机状态', name: 'zzjStatus',width:145,formatter:zjStatusCell},
					/*	{ label: '从闸机状态', name: 'czjStatus',width:100},*/
						{ label: '驱动器异常', name: 'zDriverStatus',width:150,formatter:qdStatusCell},
						/*{ label: '从驱动器异常', name: 'cDriverStatus',width:100},*/
						{ label: '通行状态', name: 'zjCrossStatus',width:75},
						{ label: '控制器状态', name: 'kzqStatus',width:100 },
						{ label: '场内人数', name: 'personTotal',width:75 }	
						
			        ],
					viewrecords: true,
			        height: '80%',
			        rowNum: 24,
					rowList : [30],
			        rownumbers: false, 
			        rownumWidth: 25, 
			        autowidth:true,
			        multiselect: false,
			      //  pager: "#readStatusGridPager",
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
			        	$("#readStatusGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
			        	//alert("加载完成");
			        },
			        loadComplete:function(xhr){
			        	closeLoad();
			        	if(xhr.code&&xhr.code!=0){
			        		alert(xhr.msg);
			        	}else{
			        		vm.equipment.readstatus="1";
			        	}
			        }
			        
			    });
			}else{
				$("#readStatusGrid").jqGrid('setGridParam',{ 
	            }).trigger("reloadGrid");
			}
		}else if(newTag=='信息查询'&&vm.newTag!=newTag){
			vm.newTag = newTag;
			if(vm.equipment&&vm.equipment.version){
				return;
			}
			requestType = "version";
			showLoad();
			$.ajax({
				type: "POST",
				url: baseURL + 'searchEquipment/readInfo/'+requestType,
				contentType: "application/json",
				data: JSON.stringify(vm.equipment),
				success: function(r){
					closeLoad();
					if(r.code === 0){
						vm.equipment = r.equipment;
					}else{
						alert(r.msg);
					}
				}
			});
		}else if(newTag=='读取记录'&&vm.newTag!=newTag){
			closeLoad();
			vm.newTag = newTag;
			vm.upload=1;
			if(!vm.loadRecordTotal){
				vm.loadRecordTotal=true;
			 $("#readRecordGrid").jqGrid({
			        url: baseURL + 'searchEquipment/readRecordInfo',
			        datatype: "local",
			        postData:{'ip': vm.equipment.ip,"port":vm.equipment.port,"equipSn":vm.equipment.equipSn},
			        colModel: [			
			        	{ label: '序列号', name: 'equipReq',width:'180px',formatter:function(){
			        		return vm.equipment.equipReq;
			        	} },
			        	{ label: '设备名', name: 'equipName',width:'120px',formatter:function(){
			        		return vm.equipment.otherName;
			        	} },
						{ label: '备注', name: 'remark',width:'140px' },
						{ label: '事件来源', name: 'resource',width:'140px' },
						{ label: '事件详情', name: 'exceptionInfo',width:'140px'},
						{ label: '时间', name: 'date',width:'130px'}
						
			        ],
					viewrecords: true,
			        height: '70%',
			       // rowNum: 24,
					rowList : [30],
			        rownumbers: false, 
			        //rownumWidth: 25, 
			        autowidth:true,
			        multiselect: false,
			        pager: "#readRecordGridPager",
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
			        	$("#readRecordGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
			        	//alert("加载完成");
			        },
			        loadComplete:function(xhr){
			        	
			        	if(xhr.code&&xhr.code!=0){
			        		alert(xhr.msg);
			        	}
			        }
			        
			    });
			}else{
				$("#readRecordGrid").jqGrid('setGridParam',{ 
	            }).trigger("reloadGrid");
			}
		}
	});
});
