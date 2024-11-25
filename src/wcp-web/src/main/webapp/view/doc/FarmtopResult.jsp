<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<PF:basePath/>">
<title>置顶文档数据管理</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<jsp:include page="/view/conf/include.jsp"></jsp:include>
</head>
<body class="easyui-layout">
	<div data-options="region:'north',border:false">
		<form id="searchFarmtopForm">
			<table class="editTable">
				<tr>
					<td class="title">标题:</td>
					<td><input name="A.TITLE:like" type="text"></td>
					<td class="title"></td>
					<td></td>
				</tr>
				<tr style="text-align: center;">
					<td colspan="4"><a id="a_search" href="javascript:void(0)"
						class="easyui-linkbutton" iconCls="icon-search">查询</a> <a
						id="a_reset" href="javascript:void(0)" class="easyui-linkbutton"
						iconCls="icon-reload">清除条件</a></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false">
		<table id="dataFarmtopGrid">
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true"></th>
					<th field="TITLE" data-options="sortable:true" width="20">标题</th>
					<th field="DOCDESCRIBE" data-options="sortable:true" width="20">
						摘要</th>
					<th field="AUTHOR" data-options="sortable:true" width="20">作者
					</th>
					<th field="PUBTIME" data-options="sortable:true" width="40">
						发布时间</th>
					<th field="STATE" data-options="sortable:true" width="20">状态</th>
					<th field="READPOP" data-options="sortable:true" width="20">
						读权限</th>
					<th field="WRITEPOP" data-options="sortable:true" width="20">
						写权限</th>
					<th field="DOMTYPE" data-options="sortable:true" width="20">
						内容类型</th>
					<th field="SORT" data-options="sortable:true" width="20">排序</th>
				</tr>
			</thead>
		</table>
	</div>
</body>
<script type="text/javascript">
	var url_delActionFarmtop = "farmtop/del.do";//删除URL
	var url_formActionFarmtop = "farmtop/form.do";//增加、修改、查看URL
	var url_searchActionFarmtop = "farmtop/query.do";//查询URL
	var title_windowFarmtop = "置顶文档管理";//功能名称
	var gridFarmtop;//数据表格对象
	var searchFarmtop;//条件查询组件对象
	var toolBarFarmtop = [ {
		id : 'view',
		text : '查看',
		iconCls : 'icon-tip',
		handler : viewDataFarmtop
	}, {
		id : 'add',
		text : '新增',
		iconCls : 'icon-add',
		handler : addDataFarmtop
	}, {
		id : 'edit',
		text : '修改',
		iconCls : 'icon-edit',
		handler : editDataFarmtop
	}, {
		id : 'del',
		text : '删除',
		iconCls : 'icon-remove',
		handler : delDataFarmtop
	} ];
	$(function() {
		//初始化数据表格
		gridFarmtop = $('#dataFarmtopGrid').datagrid({
			url : url_searchActionFarmtop,
			fit : true,
			fitColumns : true,
			'toolbar' : toolBarFarmtop,
			pagination : true,
			closable : true,
			checkOnSelect : true,
			border : false,
			striped : true,
			rownumbers : true,
			ctrlSelect : true
		});
		//初始化条件查询
		searchFarmtop = $('#searchFarmtopForm').searchForm({
			gridObj : gridFarmtop
		});
	});
	//查看
	function viewDataFarmtop() {
		var selectedArray = $(gridFarmtop).datagrid('getSelections');
		if (selectedArray.length == 1) {
			var url = url_formActionFarmtop + '?pageset.pageType='
					+ PAGETYPE.VIEW + '&ids=' + selectedArray[0].ID;
			$.farm.openWindow({
				id : 'winFarmtop',
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
	function addDataFarmtop() {
		var url = url_formActionFarmtop + '?operateType=' + PAGETYPE.ADD;
		$.farm.openWindow({
			id : 'winFarmtop',
			width : 600,
			height : 300,
			modal : true,
			url : url,
			title : '新增'
		});
	}
	//修改
	function editDataFarmtop() {
		var selectedArray = $(gridFarmtop).datagrid('getSelections');
		if (selectedArray.length == 1) {
			var url = url_formActionFarmtop + '?operateType=' + PAGETYPE.EDIT
					+ '&ids=' + selectedArray[0].ID;
			$.farm.openWindow({
				id : 'winFarmtop',
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
	function delDataFarmtop() {
		var selectedArray = $(gridFarmtop).datagrid('getSelections');
		if (selectedArray.length > 0) {
			// 有数据执行操作
			var str = selectedArray.length + MESSAGE_PLAT.SUCCESS_DEL_NEXT_IS;
			$.messager.confirm(MESSAGE_PLAT.PROMPT, str, function(flag) {
				if (flag) {
					$(gridFarmtop).datagrid('loading');
					$.post(url_delActionFarmtop + '?ids='
							+ $.farm.getCheckedIds(gridFarmtop, 'ID'), {},
							function(flag) {
								var jsonObject = JSON.parse(flag, null);
								$(gridFarmtop).datagrid('loaded');
								if (jsonObject.STATE == 0) {
									$(gridFarmtop).datagrid('reload');
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