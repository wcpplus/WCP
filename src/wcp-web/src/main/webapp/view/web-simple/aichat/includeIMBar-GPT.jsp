<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<%@ taglib uri="/view/conf/farmdoc.tld" prefix="DOC"%>
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
</style>
<div style="margin: 10px; margin-top: 20px;">
	<div class="input-group">
		<input type="text" id="imMsgQuestionId" name="question"
			style="background-color: #f6f7f7; border-top-left-radius: 12px; border-bottom-left-radius: 12px;"
			class="form-control" placeholder="请在此处录入您的问题..."> <span
			class="input-group-btn">
			<button class="btn btn-info" id="imMsgQuestionButtonId"
				style="border-top-right-radius: 12px; border-bottom-right-radius: 12px;"
				onclick="sendMsgToIM()" type="button">
				<i class="glyphicon glyphicon-send"></i>&nbsp;&nbsp;
			</button>
		</span>
	</div>
	<!-- /input-group -->
</div>
<div id="wcp-IM-msgbox-id"
	style="height: 80%; overflow: auto; color: #666666; font-size: 14px;">
	<!-- 消息展示位置 -->
	<div id="gpt-init-logos" style="text-align: center;">
		<img
			style="height: 128px; height: 128px; margin-top: 80px; opacity: 0.3;"
			src="text/img/logos/wcpai1.png" />
	</div>
</div>
<div
	style="text-align: right; color: #999999; padding-top: 8px; padding-right: 20px;">
	<a style="cursor: pointer;" href='aiweb/PubAiChat.do'>最大化</a>&nbsp;
	<a style="cursor: pointer;" onclick="clearImList(true)">清空记录</a>&nbsp;
	<a style="cursor: pointer;" onclick="loadImList()">历史记录</a>
</div>
<jsp:include page="commons/includeJavaScript.jsp"></jsp:include>
<script type="text/javascript">

	$(function() {
		$('#imMsgQuestionId').bind('keypress', function(event) {
			if (event.keyCode == "13") {
				sendMsgToIM();
			}
		});
		loadImList();
		loadAImsg();
	});

	//初始化提问框的状态是否可用
	function initWcpAiQuestInput(albe) {
		if (!albe) {
			$('#imMsgQuestionButtonId').attr("disabled", "disabled");
			$('#imMsgQuestionId').attr("disabled", "disabled");
			$('#imMsgQuestionId').attr("placeholder", "请等待回答...");
		} else {
			$('#imMsgQuestionButtonId').removeAttr("disabled");
			$('#imMsgQuestionId').removeAttr("disabled");
			$('#imMsgQuestionId').attr("placeholder", "请在此处录入您的问题...");
		}
	}

	

	

	
	
</script>