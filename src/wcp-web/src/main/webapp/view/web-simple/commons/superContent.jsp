<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<div class="super_content">
	<div id="showHomeMessageId" style="color: red;"></div>
</div>
<c:if test="${USEROBJ!=null}">
<script>
	var url = "webusermessage/showHomeMessage.do";
	$.post(url, {}, function(obj){
		var message = "未读消息【" + obj.unReadCount + "】";
		if(obj.newMessage){
			message += "；最新消息："+obj.newMessage + "；";
		}
		var href = "<a href='webuser/PubHome.do?type=usermessage&userid=${USEROBJ.id}'>"+message+"</a>";
		$("#showHomeMessageId").html(href);
	},"json");
</script>
</c:if>