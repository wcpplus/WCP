<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base href="<PF:basePath/>" />
<title>检索-<PF:ParameterValue key="config.sys.title" /></title>
<meta name="description"
	content='<PF:ParameterValue key="config.sys.mate.description"/>'>
<meta name="keywords"
	content='<PF:ParameterValue key="config.sys.mate.keywords"/>'>
<meta name="author"
	content='<PF:ParameterValue key="config.sys.mate.author"/>'>
<meta name="robots" content="index,follow">
<jsp:include page="../atext/include-web.jsp"></jsp:include>
<style type="text/css">
#chatSubmitButtonId {
	float: right;
	align-items: center;
	background-color: #9e9e9e;
	border-radius: 10px;
	color: #fff;
	cursor: pointer;
	display: flex;
	flex-shrink: 0;
	font-size: 24px;
	height: 40px;
	justify-content: center;
	line-height: 40px;
	opacity: 1;
	transition: all .25s;
	width: 40px;
}

#chatSubmitButtonId:hover {
	background-color: #cccccc;
}
</style>
<style>
.farm_msg_box {
	margin: 0px;
	margin-left: 0px;
	margin-right: 0px;
	padding-top: 20px;
}

.farm_msg_box .service_img_box {
	text-align: center;
	padding-right: 0px;
}

.farm_msg_box .mine_img_box {
	text-align: center;
	padding-left: 0px;
}

.farm_msg_box .mine_text {
	padding: 20px;
	border-radius: 5px;
	background-color: #b8f1cc;
	text-align: left;
}

.farm_msg_box .mine_img {
	width: 43px;
	height: 43px;
	margin-top: 6px;
	border-radius: 20px;
}

.farm_msg_box .service_text {
	padding: 20px;
	border-radius: 5px;
	background-color: #ffffff;
	text-align: left;
}

.farm_msg_box .service_text .docbox {
	text-align: center;
}

.farm_msg_box .service_text .docbox  img {
	max-width: 100%;
	max-height: 200px;
	margin: auto;
	border-radius: 4px;
}

.farm_msg_box .service_text .docbox  .title {
	margin-top: 12px;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
	overflow: hidden;
}

.farm_msg_box .service_img {
	width: 43px;
	height: 43px;
	margin-top: 6px;
	border-radius: 20px;
}

.service_text a {
	color: #999999;
	text-decoration: underline;
	margin-top: 20px;
}

.service_text a:hover {
	color: #ba2636;;
	text-decoration: underline;
}

.farm_wcp_ai_more {
	padding-top: 20px;
	text-align: right;
}

.farm_wcp_ai_more a {
	color: #717fa2;
}

#wcp-IM-msgbox-id code {
	font-size: 10px;
}

#referenceTableId div {
	overflow: hidden; /* 隐藏超出内容 */
	white-space: nowrap; /* 不允许文本换行 */
	text-overflow: ellipsis; /* 当内容溢出时显示省略号 */
	width: 250px; /* 设置一个固定的宽度，当内容超过这个宽度时才会出现省略号 */
}

#imMsgQuestionId {
	border: 0px #cccccc;
	font-size: 16px;
	max-height: 100px;
	overflow: auto;
	width: 100%;
	outline: none;
	resize: none;
	padding: 20px;
	padding-bottom: 0px;
	max-height: 100px;
	overflow: auto;
	width: 100%;
	outline: none;
	resize: none;
	padding: 20px;
	overflow: auto;
	width: 100%;
	outline: none;
	resize: none;
	padding: 20px;
	padding-bottom: 0px;
	max-height: 100px;
	overflow: auto;
	width: 100%;
	outline: none;
	resize: none;
	padding: 20px;
	padding-bottom: 0px;
	max-height: 100px;
	overflow: auto;
	outline: none;
	resize: none;
	padding: 20px;
	padding-bottom: 0px;
	max-height: 100px;
	overflow: auto;
	resize: none;
	padding: 20px;
	padding-bottom: 0px;
	max-height: 100px;
	overflow: auto;
	padding: 20px;
	padding-bottom: 0px;
	max-height: 100px;
	overflow: auto;
	padding-bottom: 0px;
	max-height: 100px;
	overflow: auto;
	max-height: 100px;
	overflow: auto;
}

#referenceTableId i {
	color: #ffad10;
}
</style>
</head>
<body style="background-color: #ffffff;">
	<jsp:include page="../commons/head.jsp">
		<jsp:param value="true" name="hideImBar" />
	</jsp:include>
	<jsp:include page="../commons/superContent.jsp"></jsp:include>
	<div class="containerbox" style="background-color: #ffffff;">
		<div class="container ">
			<div class="row">
				<div class="col-md-3">
					<div
						style="text-align: left; margin-left: 2px; font-size: 24px; margin: 20px;">
						<img alt="" src="text/img/logos/wcpai1.png"
							style="width: 24px; height: 24px; margin: 6px; margin-right: 8px; margin-top: -1px;">
						智能知识助手
					</div>
					<div id="wcp_ai_current_functions"
						class="hidden-xs hidden-sm hidden-md">
						<div
							style="margin-top: 50px; text-align: center; margin-bottom: 20px;">
							<button onclick="clearImList(true)" type="button"
								class="btn btn-success">
								<i class="glyphicon glyphicon-trash"></i>&nbsp;清空历史记录
							</button>
							<!-- 
							<a href="aiweb/creatKnow.do" class="btn btn-info"> <i
								class="glyphicon glyphicon-book" style="color: #ffffff;"></i>&nbsp;创建知识
							</a> -->
						</div>
						<table class="table table-hover" id="referenceTableId"
							style="display: none;">
							<thead>
								<tr>
									<th></th>
									<th>推荐知识</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<th scope="row"></th>
									<td></td>
								</tr>
							</tbody>
						</table>

					</div>
				</div>
				<div class="col-md-9">
					<div
						style="background-color: #f7f8fc; border-radius: 30px; min-height: 80%; width: 100%; margin: 20px; padding-bottom: 5px; padding-top: 20px;">
						<div id="hisMessageBoxId"
							style="padding: 20px; font-size: 16px; overflow: auto;">
							<div id="wcp-IM-msgbox-id"></div>
							<div id="wcp-IM-stopbox-id"
								style="text-align: center; padding-top: 20px;">
								<a onclick="stopAns()" class="btn btn-danger btn-sm">停止回答</a>
							</div>
						</div>
						<div
							style="user-select: none; margin: 20px; border: solid 1px #cccccc; padding-bottom: 10px; margin-top: 0px; border-radius: 8px; overflow: hidden; background-color: #ffffff;">
							<textarea rows="1" id="imMsgQuestionId"></textarea>
							<div
								style="text-align: right; margin: 10px; margin-top: 0px; height: 40px;">
								<div id="chatSubmitButtonId">
									<a onclick="sendMsgToIM()" role="button"><i
										style="color: #ffffff;" class="glyphicon glyphicon-send"></i>
									</a>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../commons/footServer.jsp"></jsp:include>
	<jsp:include page="../commons/foot.jsp"></jsp:include>
</body>


<jsp:include page="commons/includeJavaScript.jsp"></jsp:include>
<script type="text/javascript">
//消息是否发送中
var waitingServer = false;
	$(function() {
		document.getElementById('imMsgQuestionId').addEventListener('input',
				function() {
					initWindowHeight();
				});
		$(window).resize(function() {
			initWindowHeight();
		});
		initWindowHeight();
		initTextEvent();
		loadImList();
		loadAImsg();
		
	});
	
	
	//停止回答
	function stopAns(){
		 var ids= getLoadingMsgids() ;
		 if(ids){
			 $.post('aiweb/PubStopMsg.do?ids='+ids, {}, function(flag) {
					if (flag.STATE == 0) {
						//$('#wcp-IM-stopbox-id').hide();
					} else {
						alert(flag.MESSAGE);
					}
			}, 'json');
		 }
	}
	
	//初始化提问框的状态是否可用
	function initWcpAiQuestInput(albe) {
		if (!albe) {
			$('#imMsgQuestionId').val(' ');
			$('#imMsgQuestionId').val("");
			$('#imMsgQuestionButtonId').attr("disabled", "disabled");
			$('#imMsgQuestionId').attr("disabled", "disabled");
			$('#imMsgQuestionId').attr("placeholder", "请等待回答...");
			$('#wcp-IM-stopbox-id').show();
		} else {
			$('#imMsgQuestionButtonId').removeAttr("disabled");
			$('#imMsgQuestionId').removeAttr("disabled");
			$('#imMsgQuestionId').attr("placeholder", "请在此处录入您的问题...");
			$('#wcp-IM-stopbox-id').hide();
			$('#chatSubmitButtonId').show();
		}
	}
	
	//初始化文本框事件
	function initTextEvent() {
		$("#imMsgQuestionId").keypress(function(e) {
			if (e.which == 13 && !e.shiftKey) {
				var textareaValue = $(this).val();
				sendMsgToIM();
				setTimeout(() => {
					$("#imMsgQuestionId").val(textareaValue);
						initWindowHeight() ;
				    }, 0);
				return;
			}
		});
		$('#wcp-IM-stopbox-id').hide();
	}
	
	//初始化文本框高度
	function initTexteareHeight(){
		$('#imMsgQuestionId').height('auto');
		$('#imMsgQuestionId').height($('#imMsgQuestionId').prop('scrollHeight') + 'px');
	}
	
	//初始化窗口高度
	function initWindowHeight() {
		initTexteareHeight();
		$('#hisMessageBoxId').css('height',
				$(window).height() - $('#imMsgQuestionId').height() - 280);
	}
	
	//滚动条到最下方
	function rollNew() {
		var div = document.getElementById('hisMessageBoxId');
		div.scrollTop = div.scrollHeight;
	}
	
	//接收到新消息的回调函数
	function messageLoadHandle(msgs) {
		$(msgs).each(function(i,obj){
			var num=0;
			if(obj.reference){
				$('tbody','#referenceTableId').html(''); 
				$('#referenceTableId').show();
				$(obj.reference).each(function(i2,obj2){
					num++;
					if($('#ref'+obj2.markId,'#referenceTableId').length <=0){
						var innerTitle=obj2.title.replace("'","");
						if(obj2.excellent){
							innerTitle="<i class='glyphicon glyphicon-star-empty'></i>"+innerTitle;
						}
						$('tbody','#referenceTableId').append('<tr id="ref'+obj2.markId+'"><th scope="row">'+(i2+1)+'</th><td><div><a href='+obj2.url+' title='+obj2.title.replace("'","")+'>'+innerTitle+'</a></div></td></tr>');
					}
				}); 
			}
			if(num<=0){
				$('tbody','#referenceTableId').html('');
				$('#referenceTableId').hide();
			}
		}); 
	}
	
	//function sendMsgToIM(){
	//	alert('提交对话');
	//}
</script>
<script type="text/javascript">
		//消息加载完成
		function newMessageHandle(){
			$('a','#wcp-IM-msgbox-id').each(function(i,obj){
				$(obj).attr('target','_blank');
			});
			$('a','#referenceTableId').each(function(i,obj){
				$(obj).attr('target','_blank');
			});
			
			$('a','#wcp_ai_current_functions').each(function(i,obj){
				$(obj).attr('target','_blank');
			});
		}
	</script>
</html>