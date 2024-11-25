<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<PF:basePath/>">
    <title>推荐服务数据管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <jsp:include page="/view/conf/include.jsp"></jsp:include>
    <link rel="stylesheet" type="text/css"
		href="text/lib/kindeditor/themes/default/default.css">
	<script type="text/javascript"
		src="text/lib/kindeditor/kindeditor-all-min.js"></script>
	<script type="text/javascript"
		src="text/lib/kindeditor/zh_CN.js"></script>
  </head>
<body class="easyui-layout">
	<div data-options="region:'north',border:false">
		<form id="searchWeburlForm">
			<table class="editTable">
				<tr>
					<td class="title">名称:</td>
					<td>
						<input name="WEBNAME:like" type="text">
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
		<table id="dataWeburlGrid">
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true"></th>
					<th field="WEBNAME" data-options="sortable:true" width="30">名称</th>
					<th field="URL" data-options="sortable:true" width="30">URL</th>
					<th field="SORT" data-options="sortable:true" width="30">排序</th>
				</tr>
			</thead>
		</table>
	</div>
</body>
<script type="text/javascript">
	var url_delActionWeburl = "weburl/del.do";//删除URL
	var url_formActionWeburl = "weburl/form.do";//增加、修改、查看URL
	var url_searchActionWeburl = "weburl/query.do";//查询URL
	var title_windowWeburl = "推荐服务管理";//功能名称
	var gridWeburl;//数据表格对象
	var searchWeburl;//条件查询组件对象
	var toolBarWeburl = [ {
		id : 'view',
		text : '查看',
		iconCls : 'icon-tip',
		handler : viewDataWeburl
	}, {
		id : 'add',
		text : '新增',
		iconCls : 'icon-add',
		handler : addDataWeburl
	}, {
		id : 'edit',
		text : '修改',
		iconCls : 'icon-edit',
		handler : editDataWeburl
	}, {
		id : 'del',
		text : '删除',
		iconCls : 'icon-remove',
		handler : delDataWeburl
	} ];
	$(function() {
		//初始化数据表格
		gridWeburl = $('#dataWeburlGrid').datagrid({
			url : url_searchActionWeburl,
			fit : true,
			fitColumns : true,
			'toolbar' : toolBarWeburl,
			pagination : true,
			closable : true,
			checkOnSelect : true,
			border : false,
			striped : true,
			rownumbers : true,
			ctrlSelect : true
		});
		//初始化条件查询
		searchWeburl = $('#searchWeburlForm').searchForm({
			gridObj : gridWeburl
		});
	});
	//查看
	function viewDataWeburl() {
		var selectedArray = $(gridWeburl).datagrid('getSelections');
		if (selectedArray.length == 1) {
			var url = url_formActionWeburl + '?pageset.pageType='
					+ PAGETYPE.VIEW + '&ids=' + selectedArray[0].ID;
			$.farm.openWindow({
				id : 'winWeburl',
				width : 600,
				height : 300,
				modal : true,
				url : url,
				title : '浏览'
			});
		} else {
			$.messager.alert(MESSAGE_PLAT.PROMPT, MESSAGE_PLAT.CHOOSE_ONE_ONLY,
					'info');
		}
	}
	//新增
	function addDataWeburl() {
		var url = url_formActionWeburl + '?operateType=' + PAGETYPE.ADD;
		$.farm.openWindow({
			id : 'winWeburl',
			width : 600,
			height : 300,
			modal : true,
			url : url,
			title : '新增'
		});
	}
	//修改
	function editDataWeburl() {
		var selectedArray = $(gridWeburl).datagrid('getSelections');
		if (selectedArray.length == 1) {
			var url = url_formActionWeburl + '?operateType=' + PAGETYPE.EDIT
					+ '&ids=' + selectedArray[0].ID;
			$.farm.openWindow({
				id : 'winWeburl',
				width : 600,
				height : 300,
				modal : true,
				url : url,
				title : '修改'
			});
		} else {
			$.messager.alert(MESSAGE_PLAT.PROMPT, MESSAGE_PLAT.CHOOSE_ONE_ONLY,
					'info');
		}
	}
	//删除
	function delDataWeburl() {
		var selectedArray = $(gridWeburl).datagrid('getSelections');
		if (selectedArray.length > 0) {
			// 有数据执行操作
			var str = selectedArray.length + MESSAGE_PLAT.SUCCESS_DEL_NEXT_IS;
			$.messager.confirm(MESSAGE_PLAT.PROMPT, str, function(flag) {
				if (flag) {
					$(gridWeburl).datagrid('loading');
					$.post(url_delActionWeburl + '?ids='
							+ $.farm.getCheckedIds(gridWeburl, 'ID'), {},
							function(flag) {
								var jsonObject = JSON.parse(flag, null);
								$(gridWeburl).datagrid('loaded');
								if (jsonObject.STATE == 0) {
									$(gridWeburl).datagrid('reload');
								} else {
									var str = MESSAGE_PLAT.ERROR_SUBMIT
											+ jsonObject.MESSAGE;
									$.messager.alert(MESSAGE_PLAT.ERROR, str,
											'error');
								}
							});
				}
			});
		} else {
			$.messager.alert(MESSAGE_PLAT.PROMPT, MESSAGE_PLAT.CHOOSE_ONE,
					'info');
		}
	}
</script>
</html>