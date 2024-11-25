<%@page import="java.net.URLEncoder"%>
<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script src="text/lib/autocomplete/completer.min.js"></script>
<link rel="stylesheet" href="text/lib/autocomplete/completer.css">
<style type="text/css">
#wcp-wide-search {
	min-height: 320PX;
	background-image: url(text/img/search-line.svg);
	padding-bottom: 20px;
	background-color: #f3f4f6;
	background-repeat: no-repeat;
	background-position: bottom;
	margin-bottom: 20px; 
}

#wcp-wide-frominput {
	outline: none;
	background-position: 16px 14px;
	background-repeat: no-repeat;
	border-radius: 8px;
	height: 50px;
	border: 1px solid #d5d5da;
	background-color: #ffffff;
	background-image:
		url(data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz48c3ZnIHdpZHRoPSIyMXB4IiBoZWlnaHQ9IjIwcHgiIHZpZXdCb3g9IjAgMCAyMSAyMCIgdmVyc2lvbj0iMS4xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIj4gICAgICAgIDx0aXRsZT5zZWFyY2g8L3RpdGxlPiAgICA8ZGVzYz5DcmVhdGVkIHdpdGggU2tldGNoLjwvZGVzYz4gICAgPGRlZnM+PC9kZWZzPiAgICA8ZyBpZD0iRWFnbGUtLS3luK7liqnkuK3lv4MiIHN0cm9rZT0ibm9uZSIgc3Ryb2tlLXdpZHRoPSIxIiBmaWxsPSJub25lIiBmaWxsLXJ1bGU9ImV2ZW5vZGQiIHRyYW5zZm9ybT0idHJhbnNsYXRlKC0zOS4wMDAwMDAsIC0xNjAuMDAwMDAwKSIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2UtbGluZWpvaW49InJvdW5kIj4gICAgICAgIDxnIGlkPSJoZXJvIiB0cmFuc2Zvcm09InRyYW5zbGF0ZSgtNzQuMDAwMDAwLCA2MC4wMDAwMDApIiBzdHJva2U9IiMxNzJCNEQiIHN0cm9rZS13aWR0aD0iMiI+ICAgICAgICAgICAgPGcgaWQ9Iua0u+WKqOWMuuWfnyIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoMTA0LjAwMDAwMCwgOTAuMDAwMDAwKSI+ICAgICAgICAgICAgICAgIDxnIGlkPSJHcm91cC0yNiIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoMTAuMDAwMDAwLCA5LjAwMDAwMCkiPiAgICAgICAgICAgICAgICAgICAgPGcgaWQ9InNlYXJjaCIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoMC41MDAwMDAsIDIuMDAwMDAwKSI+ICAgICAgICAgICAgICAgICAgICAgICAgPGNpcmNsZSBpZD0iT3ZhbCIgY3g9IjcuNSIgY3k9IjcuNSIgcj0iNy41Ij48L2NpcmNsZT4gICAgICAgICAgICAgICAgICAgICAgICA8cGF0aCBkPSJNMTgsMTggTDEyLjgsMTIuOCIgaWQ9IlNoYXBlIj48L3BhdGg+ICAgICAgICAgICAgICAgICAgICA8L2c+ICAgICAgICAgICAgICAgIDwvZz4gICAgICAgICAgICA8L2c+ICAgICAgICA8L2c+ICAgIDwvZz48L3N2Zz4=);
	padding-left: 50px;
	width: 100%;
}

#wcp-wide-gobutton {
	border-radius: 8px;
	height: 50px; 
	width: 50px;
	border: 1px solid #d5d5da;
	background-color: #D9534F;
	padding-top: 12px;
	text-align: center;
	cursor: pointer;
	color: #ffffff;
	font-size: 18px;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
}

#wcp-wide-gobutton:hover {
	background-color: #e4726f;
	color: #ffffff;
}

div.ac {
	border-style: solid;
	border-width: 1px;
	border-color: #d5d5da;
	position: absolute;
	border-radius: 8px;
	display: none;
	overflow: auto;
	display: none;
	margin-top: 4px;
}

div.ac>ul>li.normal {
	padding: 8px;
}

div.ac>ul>li.normal {
	padding: 8px;
}

div.ac>ul>li.selected {
	background-color: #ffffff;
}
</style>
<div class="row wcp-margin-top8">
	<div class="col-md-1"></div>
	<div class="col-md-6">
		<div style="text-align: left; margin-top: 100px;">
			<h1 style="font-size: 36px;padding-bottom: 8px;"> 
				<c:set var="existThemes" value="false"></c:set>
				<PF:DictionaryHandle var="node" key="THEMES-HELP-FILES">
					<c:set var="existThemes" value="true"></c:set>
				${node.value}
				</PF:DictionaryHandle>
				<c:if test="${existThemes==false }">
					<!-- 请创建附件字典字典THEMES-HELP-FILES来设置主题title和主题图片 -->
					<PF:ParameterValue key="config.sys.title" />
				</c:if>
			</h1>
		</div>
		<div>
			<div class="media">
				<div class="pull-right hidden-xs">
					<div id="wcp-wide-gobutton">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
					</div>
				</div>
				<div class="media-body">
					<input id="wcp-wide-frominput" type="text" name="word"
						style="font-size: 18px;" value="${word}" placeholder="请输入检索关键字...">
				</div>
			</div>
		</div>
		<div class=" hidden-xs"
			style="margin: 4px; margin-top: 10px;; margin-bottom: 8px; padding-left: 8px; padding-right: 8px; font-size: 16px;">
			<c:if test="${!empty hotCase }">
			热门搜索:&nbsp;&nbsp;
			<c:forEach items="${hotCase}" var="node">
					<a class="hotWordsearch"> <span
						style="cursor: pointer; text-decoration: underline;">${fn:length(node) <= 10 ? node : fn:substring(node,0,10) }</span>
					</a>&nbsp;
			</c:forEach>
			</c:if>
		</div>
	</div>
	<div class="col-md-5 hidden-xs hidden-sm">
		<div style="text-align: center;">
			<!-- 请创建附件字典字典THEMES-HELP-FILES来设置主题title和主题图片 -->
			<PF:DictionaryHandle var="node" key="THEMES-HELP-FILES">
				<img style="margin-top: 40px; max-height: 280px; max-width: 100%;"
					src="actionImg/Publoadimg.do?id=${node.key}">
			</PF:DictionaryHandle>
			<c:if test="${existThemes==false }">
				<img style="margin-top: 40px; max-height: 280px; max-width: 100%;"
					src="text/img/homeimg<%=(int)(Math.floor(Math.random()*5))%>.png">
			</c:if>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		$('.hotWordsearch').bind('click', function() {
			luceneSearch($(this).text());
		});
		$('#wcp-wide-frominput').bind('keypress', function(event) {
			if (event.keyCode == "13") {
				luceneSearch($('#wcp-wide-frominput').val());
			}
		});
		$('#wcp-wide-gobutton').bind('click', function(event) {
			luceneSearch($('#wcp-wide-frominput').val());
		});
	});
</script>