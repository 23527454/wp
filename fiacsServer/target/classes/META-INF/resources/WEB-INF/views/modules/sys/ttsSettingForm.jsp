<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>语音配置管理</title>
	
	<meta name="decorator" content="default"/>
	<style type="text/css">
		.testDiv {
			bottom: 36px;
			height: calc(100vh - 280px);
			resize: none;
			overFlow-x: scroll;
			overFlow-y: scroll;
		}
		
		.keyword {
			color: red;
		}
		
		.textareadiv {
			border: 1px #e3e3e3 solid;
			resize: none;
			height: 97px;
			width: 671px;
			padding: 9px;
			color: #848484;
			font-size: 12px;
			overflow: auto;
			line-height: 28px;
		}
		
		.search-choice {
		  -webkit-border-radius: 3px;
		  -moz-border-radius   : 3px;
		  border-radius        : 3px;
		  -moz-background-clip   : padding;
		  -webkit-background-clip: padding-box;
		  background-clip        : padding-box;
		  background-color: #e4e4e4;
		  filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#f4f4f4', endColorstr='#eeeeee', GradientType=0 ); 
		  background-image: -webkit-gradient(linear, 0 0, 0 100%, color-stop(20%, #f4f4f4), color-stop(50%, #f0f0f0), color-stop(52%, #e8e8e8), color-stop(100%, #eeeeee));
		  background-image: -webkit-linear-gradient(top, #f4f4f4 20%, #f0f0f0 50%, #e8e8e8 52%, #eeeeee 100%);
		  background-image: -moz-linear-gradient(top, #f4f4f4 20%, #f0f0f0 50%, #e8e8e8 52%, #eeeeee 100%);
		  background-image: -o-linear-gradient(top, #f4f4f4 20%, #f0f0f0 50%, #e8e8e8 52%, #eeeeee 100%);
		  background-image: linear-gradient(#f4f4f4 20%, #f0f0f0 50%, #e8e8e8 52%, #eeeeee 100%); 
		  -webkit-box-shadow: 0 0 2px #ffffff inset, 0 1px 0 rgba(0,0,0,0.05);
		  -moz-box-shadow   : 0 0 2px #ffffff inset, 0 1px 0 rgba(0,0,0,0.05);
		  box-shadow        : 0 0 2px #ffffff inset, 0 1px 0 rgba(0,0,0,0.05);
		  color: #333;
		  border: 1px solid #aaaaaa;
		  line-height: 13px;
		  padding: 3px 20px 3px 5px;
		  margin: 3px 0 3px 5px;
		  position: relative;
		  cursor: default;
		}
		
		.choice-label {
		  -webkit-border-radius: 3px;
		  -moz-border-radius   : 3px;
		  border-radius        : 3px;
		  -moz-background-clip   : padding;
		  -webkit-background-clip: padding-box;
		  background-clip        : padding-box;
		  background-color: #e4e4e4;
		  filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#f4f4f4', endColorstr='#eeeeee', GradientType=0 ); 
		  background-image: -webkit-gradient(linear, 0 0, 0 100%, color-stop(20%, #f4f4f4), color-stop(50%, #f0f0f0), color-stop(52%, #e8e8e8), color-stop(100%, #eeeeee));
		  background-image: -webkit-linear-gradient(top, #f4f4f4 20%, #f0f0f0 50%, #e8e8e8 52%, #eeeeee 100%);
		  background-image: -moz-linear-gradient(top, #f4f4f4 20%, #f0f0f0 50%, #e8e8e8 52%, #eeeeee 100%);
		  background-image: -o-linear-gradient(top, #f4f4f4 20%, #f0f0f0 50%, #e8e8e8 52%, #eeeeee 100%);
		  background-image: linear-gradient(#f4f4f4 20%, #f0f0f0 50%, #e8e8e8 52%, #eeeeee 100%); 
		  -webkit-box-shadow: 0 0 2px #ffffff inset, 0 1px 0 rgba(0,0,0,0.05);
		  -moz-box-shadow   : 0 0 2px #ffffff inset, 0 1px 0 rgba(0,0,0,0.05);
		  box-shadow        : 0 0 2px #ffffff inset, 0 1px 0 rgba(0,0,0,0.05);
		  color: #333;
		  border: 1px solid #aaaaaa;
		  line-height: 13px;
		  padding: 3px 5px 3px 5px;
		  margin: 3px 0 3px 15px;
		  position: relative;
		  cursor: cursor:pointer;
		}
		.choice-label:hover {
		  cursor:pointer;
		  background-position: -42px -10px;
		}
		
		.search-choice-close {
		  display: block;
		  position: absolute;
		  right: 3px;
		  top: 4px;
		  width: 12px;
		  height: 12px;
		  font-size: 1px;
		  background: url('${ctxStatic}/images/chosen-sprite.png') -42px 1px no-repeat;
		}
		
		.search-choice-close:hover {
		  cursor:pointer;
		  background-position: -42px -10px;
		}
	</style>
	
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				}
			});
			
			
			var reg = /[^}{}]+(?=})/g;
    		var text = $("#voiceConfig").val();
    		var dict = eval($("#dictjson").val());
    		text = text.replace(reg,function(m){
    			for(var i=0;i<dict.length;i++){
    				if(dict[i].value == m)
    					return '<span contenteditable="false" class="search-choice" >'+ dict[i].label+' <a href="javascript:void(0)" class="search-choice-close"></a> </span>&nbsp'; 
    			}
    			return "";
    		});
    		text = text.replace(/{|}/g,"");		
			
    		$("#txtSayWords").html(text);
    		
			$(".search-choice-close").click(function(){ 
				$(this)[0].parentElement.remove();
			});
			
			
		});
		
		function pasteHtmlAtCaret(html) {
		    var sel, range;
		    if (window.getSelection) {
		        // IE9 and non-IE
		        sel = window.getSelection();
		        if (sel.getRangeAt && sel.rangeCount) {
		            range = sel.getRangeAt(0);
		            range.deleteContents();

		            // Range.createContextualFragment() would be useful here but is
		            // only relatively recently standardized and is not supported in
		            // some browsers (IE9, for one)
		            var el = document.createElement("div");
		            el.innerHTML = html;
		            var frag = document.createDocumentFragment(), node, lastNode;
		            while ( (node = el.firstChild) ) {
		                lastNode = frag.appendChild(node);
		            }
		            var firstNode = frag.firstChild;
		            range.insertNode(frag);
		            
		            // Preserve the selection
		            if (lastNode) {
		                range = range.cloneRange();
		                range.setStartAfter(lastNode);
		                
		                range.collapse(true);

		                sel.removeAllRanges();
		                sel.addRange(range);
		            }
		        }
		    } else if ( (sel = document.selection) && sel.type != "Control") {
		        // IE < 9
		        var originalRange = sel.createRange();
		        originalRange.collapse(true);
		        sel.createRange().pasteHTML(html);
		        
		    }
		}

		
		var defaultVal = "输入语音播报内容...不少于10个字"; //默认文本框内容
		function AutoContents(type) {
			var txtVal = $("#txtSayWords").html(); //文本框获取或失去焦点时的文本框内容
			//文本框获取焦点
			if (type == "in") {
				//主动说话操作
				if (txtVal == defaultVal) {//表示内容为空
					//清空文本框
					$("#txtSayWords").html("");
				}
			} else if (type == "out") {//文本框失去焦点
				//如果内容为除去默认值以外的值，则需自动填充默认值+除默认值以外的
				//主动说话操作
				if (txtVal == "") {//没有输入内容
					//如果焦点离开文本框时内容为空，表示没有输入任何内容，则需自动填充默认值
					$("#txtSayWords").html(defaultVal);
				}
			}
		}

		var number = 0;
		function tag(obj,name) {
			var html = '<span contenteditable="false" class="search-choice" id="tag'+number+'" name="tag'+number+'">'+name +'<a href="javascript:void(0)" class="search-choice-close"></a> </span> &nbsp';
			$('#txtSayWords').focus();
			pasteHtmlAtCaret(html);
			$(".search-choice-close").click(function(){ 
				$(this)[0].parentElement.remove();
			});
			number++;
		}
		
		function save(){
			$('#txtSayWords').find('span').each(function(){
				var t = $(this).text().trim();
				var dict = eval($("#dictjson").val());
				for(var i=0;i<dict.length;i++){
    				if(dict[i].label == t){
    					$(this).text('{'+ dict[i].value+'}');
    					return;
    				}
    					
				}
			});
			var re = new RegExp(String.fromCharCode(160), "g");
			$('#voiceConfig').val($('#txtSayWords').text().replace(re,''));
		}
	</script>
</head>
<body>
	<input id="dictjson" value='${ json}' style="display:none;">
	<div class="panel panel-default">
	  <div class="panel-heading">可选语音播放项<small> (点击按钮插入可选项到语音播报中)</small></div>
	  <div class="panel-body">
	  	<c:forEach items="${dict}" var="d">
	    	<input class="choice-label" name="tag" value="${ d.label}" type="button" onclick="javascript:tag(this,'${ d.label}')" />
	    </c:forEach>
	   	
	  </div>
	</div>

	
	
	<form:form id="inputForm" modelAttribute="ttsSetting" action="${ctx}/sys/ttsSetting/save" method="post" class="form-horizontal" style="margin-top:40px;">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="form-group" style="display:none;">
			<label class="control-label col-sm-2">语音类型：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-sm-4">
				<form:input path="voiceType" htmlEscape="false" maxlength="1" class="input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-2">语音配置：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-sm-4">
				<form:input path="voiceConfig" htmlEscape="false" maxlength="500" class="input-xlarge required" style="display:none;"/>
				<div class="textareadiv">
				<div id="txtSayWords" contenteditable="true"
					onfocus="AutoContents('in')" onblur="AutoContents('out')">
					</div>
				</div>
			</div>
		</div>
		<div class="form-group">
			<%-- <label class="control-label col-sm-2">作用范围：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-sm-4">
				<form:input path="scope" htmlEscape="false" maxlength="20" class="input-xlarge required"/>
			</div> --%>
			<input name="scope" value="0" style="display:none;"/>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<shiro:hasPermission name="sys:ttsSetting:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" onclick="save();"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</div>
	</form:form>
</body>
</html>