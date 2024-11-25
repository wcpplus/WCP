<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<PF:basePath/>">
    <title>文档发布排名数据管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <jsp:include page="/view/conf/include.jsp"></jsp:include>
  </head>
<body class="easyui-layout">
	<div data-options="region:'north',border:false">
		<form id="searchReleaseRankingForm">
			<table class="editTable">
				<tr>
					<td class="title">用户名称:</td>
					<td>
						<input name="b.USERNAME:like" type="text">
					</td>
					<td class="title"></td>
					<td></td>
				</tr>
				<tr style="text-align: center;">
					<td colspan="4">
						<a id="a_search" href="javascript:void(0)"
							class="easyui-linkbutton" iconCls="icon-search">查询</a>
						<a id="a_reset" href="javascript:void(0)"
							class="easyui-linkbutton" iconCls="icon-reload">清除条件</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false">
		<table id="dataReleaseRankingGrid">
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true"></th>
					<th field="USERNAME" data-options="sortable:true" width="30">用户名称</th>
					<th field="DOCNUM" data-options="sortable:true" width="30">文档数量</th>
					<th field="GOODNUM" data-options="sortable:true" width="30">好评文章</th>
					<th field="BADNUM" data-options="sortable:true" width="30">差评文章</th>
					<th field="GOODRATE" data-options="sortable:true,formatter: function(value,row,index){return value + '%'}" width="30">文章好评率</th>
					<th field="VISITNUM" data-options="sortable:true" width="30">被访问总量</th>
					<th field="PRAISEYES" data-options="sortable:true" width="30">累计好评</th>
					<th field="PRAISENO" data-options="sortable:true" width="30">累计差评</th>
				</tr>
			</thead>
		</table>
	</div>
</body>
<script type="text/javascript">
	var url_searchActionReleaseRanking = "farmReleaseRanking/query.do";//查询URL
	var title_windowReleaseRanking = "文档发布排名管理";//功能名称
	var gridReleaseRanking;//数据表格对象
	var searchReleaseRanking;//条件查询组件对象
	var toolBarReleaseRanking = [];
	$(function() {
		//初始化数据表格
		gridReleaseRanking = $('#dataReleaseRankingGrid').datagrid({
			url : url_searchActionReleaseRanking,
			fit : true,
			fitColumns : true,
			'toolbar' : toolBarReleaseRanking,
			pagination : true,
			closable : true,
			checkOnSelect : true,
			border : false,
			striped : true,
			rownumbers : true,
			ctrlSelect : true
		});
		//初始化条件查询
		searchReleaseRanking = $('#searchReleaseRankingForm').searchForm({
			gridObj : gridReleaseRanking
		});
	});
</script>
</html>