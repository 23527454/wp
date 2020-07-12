$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/log/downFileList',
        datatype: "json",
        colModel: [			
			{ label: '文件名', name: 'username', index: "username", key: true,formatter:function(cellvalue){
				return '<a href="'+"/znzj/sys/log/downloadFile/"+cellvalue+'">'+cellvalue+'</a>';
			}},
        ],
		viewrecords: true,
       // height: window.innerHeight-130,
		height: "100%",
        rowNum: 100,
		rowList : [100],
        rownumbers: false, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: false,
       // pager: "#jqGridPager",
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
                page:page
            }).trigger("reloadGrid");
		}
	}
});