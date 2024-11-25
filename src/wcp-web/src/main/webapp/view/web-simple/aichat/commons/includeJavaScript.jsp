<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<style>
.farm_msg_box .table-responsive {
	overflow: scroll;
}

.farm_msg_box .syntaxhighlighter caption {
	display: none;
}
</style>
<script type="text/javascript" charset="utf-8"
	src="text/lib/syntax-highlighter/shCore.js"></script>
<link rel="stylesheet"
	href="text/lib/syntax-highlighter/shCoreDefault.css" type="text/css">
<script type="text/javascript">
	var servericon = "text/img/logos/wcpai1.png";
	//消息是否发送中
	var waitingServer = false;
	//消息框ID
	var txtboxid = 0;
	//加载历史消息
	function loadImList() {
		waitingServer = true;
		clearImList(false);
		$.post('aiweb/PubLoadhis.do', {}, function(flag) {
			if (flag.STATE == 0) {
				$(flag.msgs).each(function(i, obj) {
					msgArrived(obj.message, obj.type);
				});
			} else {
				var str = flag.MESSAGE;
				$('#' + textid).html(str);
			}
			rollNew();
			waitingServer = false;
			highlighterByImBar();
		}, 'json');
	}
	//清理聊天记录
	function clearImList(isClearServers) {
		$('#gpt-init-logos').show();
		$('.farm_msg_box').remove();
		$('#referenceTableId').hide();
		if (isClearServers) {
			waitingServer = true;
			$.post('aiweb/PubClear.do', {}, function() {
				waitingServer = false;
			});
		}
	}

	//在界面上展示一条消息（返回消息文本框的id）
	//type : mine,service
	function msgArrived(msgText, type) {
		$('#gpt-init-logos').hide();
		var myID = "static" + (++txtboxid);
		var myTextId = myID + "text";
		var dom = '';
		if (type == 'USER') {
			var imgsrc = "actionImg/Publoadfile.do?id=${USEROBJ.imgid}";
			dom = '<div id="'+myID+'" class="row farm_msg_box"><div class="col-xs-10"><div  id="'+myTextId+'" class="mine_text">'
					+ 'loading...</div></div><div class="col-xs-2 mine_img_box"><img class="mine_img" src="'+imgsrc+'"></div></div>';
		}
		if (type == 'ASSISTANT') {
			var imgsrc = servericon;
			dom = '<div id="'+myID+'" class="row farm_msg_box"><div class="col-xs-2 service_img_box"><img class="service_img" src="'
			+imgsrc+'"></div><div class="col-xs-10"><div id="'+myTextId+'" class="service_text">'
					+ 'loading...</div></div></div>';
		}
		if (type == 'FUNCTIP') {
			var imgsrc = servericon;
			dom = '<div id="'+myID+'" class="row farm_msg_box"><div class="col-xs-2 service_img_box"><img class="service_img" src="'
			+imgsrc+'"></div><div class="col-xs-10"><div id="'+myTextId+'" class="service_text">'
					+ 'loading...</div></div></div>';
		}
		$('#wcp-IM-msgbox-id').append(dom);
		$('#' + myTextId).html(msgText);
		rollNew();
		newMessageHandle();
		return myTextId;
	}

	//新消息创建的回调方法
	function newMessageHandle() {

	}

	//高亮代码格式
	function highlighterByImBar() {
		$('pre', '#wcp-IM-msgbox-id')
				.each(
						function(i, obj) {
							$(obj).attr('name', 'code');
							$(obj).attr(
									'class',
									'brush:' + $(obj).attr('title')
											+ ';toolbar:false');
						})
		SyntaxHighlighter.highlight();
	}

	//滚动条到最下方
	function rollNew() {
		var div = document.getElementById('wcp-IM-msgbox-id');
		div.scrollTop = div.scrollHeight;
	}

	//獲得當前加载中的消息
	function getLoadingMsgids() {
		var ids = null;
		//加载实时消息
		$('.farm_wcp_ai_msg').each(function(i, obj) {
			if (ids) {
				ids = ids + "," + $(obj).attr("id");
			} else {
				ids = $(obj).attr("id");
			}
		});

		return ids;
	}

	//异步加载ai返回的消息
	function loadAImsg() {
		setInterval(function() {
			var ids = getLoadingMsgids();
			if (ids) {
				//有待加载会话，静止提交消息
				initWcpAiQuestInput(false);
				//---
				$.post('aiweb/PubLoadmsg.do', {
					"ids" : ids
				}, function(flag) {
					if (flag.STATE == 0) {
						$(flag.msgs).each(
								function(i, obj) {
									if (obj.state == "COMPELET"
											|| obj.state == "EXPIRE") {
										//删除class
										$('#' + obj.id).removeAttr('class');
									}
									//载入文本消息
									$('#' + obj.id).html(obj.htmlmsg);
									highlighterByImBar();
									newMessageHandle();
									rollNew();
								});
						messageLoadHandle(flag.msgs);
					} else {
						var str = flag.MESSAGE;
						alert(str);
					}

				}, 'json');
			} else {
				//可以提交消息
				initWcpAiQuestInput(true);
			}
		}, 1000);
	}
	//接收到新消息的回调函数
	function messageLoadHandle(msgs) {

	}

	//消息发送
	function sendMsgToIM() {
		if (!waitingServer && $('#imMsgQuestionId').val().trim()) {
			waitingServer = true;
			var mineMsg = $('#imMsgQuestionId').val().replace(/<\/?.+?\/?>/g,
					'');
			$('#imMsgQuestionId').val(' ');
			$('#imMsgQuestionId').val("");
			$('#chatSubmitButtonId').hide();
			msgArrived(mineMsg.replace("\n", '<br />'), 'USER');
			var textid = msgArrived("loading...", 'ASSISTANT');
			$.post('aiweb/PubSendmgs.do', {
				'message' : mineMsg
			}, function(flag) {
				if (flag.STATE == 0) {
					$('#' + textid).html(flag.msg);
				} else {
					var str = flag.MESSAGE;
					$('#' + textid).html(str);
				}
				rollNew();
				waitingServer = false;
			}, 'json');
		}
	}
</script>