$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/log/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: "id", width: 30, key: true ,hidden:true },
			{ label: '用户名', name: 'username', index: "username", width: 50 }, 			
			{ label: '用户操作', name: 'operation', width: 150 },
			{ label: '序列号', name: 'equipReq', width: 120 },
			{ label: '设备名', name: 'equipName', width: 80 },
			{ label: '请求方法', name: 'method', width: 150 ,hidden:true}, 			
			{ label: '请求参数', name: 'params', width: 80  ,hidden:true},
            { label: '执行时长(毫秒)', name: 'time', width: 80  ,hidden:true},
			{ label: 'IP地址', name: 'ip', width: 70 }, 			
			{ label: '创建时间', name: 'createDate', width: 90 }			
        ],
		viewrecords: true,
       // height: window.innerHeight-130,
		height: "100%",
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: false,
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

var vm = new Vue({
	el:'#jeefastapp',
	data:{
		q:{
			key: null
		},
	},
	methods: {
		query: function () {
			vm.reload();
		},
		reload: function (event) {
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{'key': vm.q.key},
                page:page
            }).trigger("reloadGrid");
		}
	}
});