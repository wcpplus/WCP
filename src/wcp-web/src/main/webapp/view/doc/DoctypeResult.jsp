<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<PF:basePath/>">
<title>文档分类数据管理</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<jsp:include page="/view/conf/include.jsp"></jsp:include>
</head>
<body class="easyui-layout">
	<div data-options="region:'west',split:true,border:false"
		style="width: 250px;">
		<div class="TREE_COMMON_BOX_SPLIT_DIV">
			<a id="DoctypeTreeReload" href="javascript:void(0)"
				class="easyui-linkbutton" data-options="plain:true"
				iconCls="icon-reload">刷新菜单</a> <a id="DoctypeTreeOpenAll"
				href="javascript:void(0)" class="easyui-linkbutton"
				data-options="plain:true" iconCls="icon-sitemap">全部展开</a>
		</div>
		<ul id="DoctypeTree"></ul>
	</div>
	<div class="easyui-layout" data-options="region:'center',border:false">
		<div data-options="region:'north',border:false">
			<form id="searchDoctypeForm">
				<table class="editTable">
					<tr>
						<td class="title">上级节点:</td>
						<td><input id="PARENTTITLE_RULE" type="text"
							readonly="readonly" style="background: #F3F3E8"> <input
							id="PARENTID_RULE" name="a.PARENTID:=" type="hidden"></td>
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
			<table id="dataDoctypeGrid">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true"></th>
						<th field="NAME" data-options="sortable:true" width="40">
							分类名称</th>
						<th field="SORT" data-options="sortable:true" width="40">
							排列顺序</th>
						<th field="TYPE" data-options="sortable:true" width="40">类型</th>
						<th field="READPOP" data-options="sortable:true" width="40">
							知识浏览权限</th>
						<th field="WRITEPOP" data-options="sortable:true" width="40">
							知识编辑权限</th>
						<th field="AUDITPOP" data-options="sortable:true" width="40">
							知识审核权限</th>
						<th field="PSTATE" data-options="sortable:true" width="40">
							状态</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</body>
<script type="text/javascript">
	var url_delActionDoctype = "doctype/del.do";//删除URL
	var url_formActionDoctype = "doctype/form.do";//增加、修改、查看URL
	var url_searchActionDoctype = "doctype/query.do";//查询URL
	var title_windowDoctype = "文档分类管理";//功能名称
	var gridDoctype;//数据表格对象
	var searchDoctype;//条件查询组件对象
	var currentType, currentTypeName;
	var toolBarDoctype = [ {
		id : 'view',
		text : '查看分类信息',
		iconCls : 'icon-tip',
		handler : viewDataDoctype
	}, {
		id : 'add',
		text : '新增分类信息',
		iconCls : 'icon-add',
		handler : addDataDoctype
	}, {
		id : 'edit',
		text : '修改分类信息',
		iconCls : 'icon-edit',
		handler : editDataDoctype
	}, {
		id : 'del',
		text : '删除分类',
		iconCls : 'icon-remove',
		handler : delDataDoctype
	}, {
		id : 'r',
		text : '设置浏览权限',
		iconCls : 'icon-zoom',
		handler : setTypeRead
	}, {
		id : 'w',
		text : '设置编辑权限',
		iconCls : 'icon-page_paintbrush',
		handler : setTypeWrite
	}, {
		id : 'a',
		text : '设置审核权限',
		iconCls : 'icon-my-account',
		handler : setTypeAudit
	} ];
	$(function() {
		//初始化数据表格
		gridDoctype = $('#dataDoctypeGrid').datagrid({
			url : url_searchActionDoctype,
			fit : true,
			fitColumns : true,
			'toolbar' : toolBarDoctype,
			pagination : true,
			closable : true,
			checkOnSelect : true,
			striped : true,
			border : false,
			rownumbers : true,
			ctrlSelect : true
		});
		//初始化条件查询
		searchDoctype = $('#searchDoctypeForm').searchForm({
			gridObj : gridDoctype
		});
		$('#DoctypeTree').tree({
			url : 'doctype/FarmDoctypeLoadTreeNode.do',
			onSelect : function(node) {
				$('#PARENTID_RULE').val(node.id);
				$('#PARENTTITLE_RULE').val(node.text);
				searchDoctype.dosearch({
					'ruleText' : searchDoctype.arrayStr()
				});
			},
			loadFilter : function(data, parent) {
				return data.treeNodes;
			}
		});
		$('#DoctypeTreeReload').bind('click', function() {
			$('#DoctypeTree').tree('reload');
		});
		$('#DoctypeTreeOpenAll').bind('click', function() {
			$('#DoctypeTree').tree('expandAll');
		});
	});
	//查看
	function viewDataDoctype() {
		var selectedArray = $(gridDoctype).datagrid('getSelections');
		if (selectedArray.length == 1) {
			var url = url_formActionDoctype + '?operateType=' + PAGETYPE.VIEW
					+ '&ids=' + selectedArray[0].ID;
			$.farm.openWindow({
				id : 'winDoctype',
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
	function addDataDoctype() {
		var url = url_formActionDoctype + '?ids=' + $('#PARENTID_RULE').val()
				+ '&operateType=' + PAGETYPE.ADD;
		$.farm.openWindow({
			id : 'winDoctype',
			width : 600,
			height : 300,
			modal : true,
			url : url,
			title : '新增'
		});
	}
	//读权限
	function setTypeRead() {
		var selectedArray = $(gridDoctype).datagrid('getSelections');
		if (selectedArray.length == 1) {
			var url = 'doctypeplus/readsetPage.do?id=' + selectedArray[0].ID;
			$.farm.openWindow({
				id : 'winDoctype',
				width : 600,
				height : 500,
				modal : true,
				url : url,
				title : '设置浏览权限'
			});
		} else {
			$.messager.alert(MESSAGE_PLAT.PROMPT, MESSAGE_PLAT.CHOOSE_ONE_ONLY,
					'info');
		}
	}
	//写权限
	function setTypeWrite() {
		var selectedArray = $(gridDoctype).datagrid('getSelections');
		if (selectedArray.length == 1) {
			var url = 'doctypeplus/writesetPage.do?id=' + selectedArray[0].ID;
			$.farm.openWindow({
				id : 'winDoctype',
				width : 600,
				height : 500,
				modal : true,
				url : url,
				title : '设置编辑权限'
			});
		} else {
			$.messager.alert(MESSAGE_PLAT.PROMPT, MESSAGE_PLAT.CHOOSE_ONE_ONLY,
					'info');
		}
	}
	//审核权限
	function setTypeAudit() {
		var selectedArray = $(gridDoctype).datagrid('getSelections');
		if (selectedArray.length == 1) {
			var url = 'doctypeplus/auditsetPage.do?id=' + selectedArray[0].ID;
			$.farm.openWindow({
				id : 'winDoctype',
				width : 600,
				height : 500,
				modal : true,
				url : url,
				title : '设置审核权限'
			});
		} else {
			$.messager.alert(MESSAGE_PLAT.PROMPT, MESSAGE_PLAT.CHOOSE_ONE_ONLY,
					'info');
		}
	}
	//修改
	function editDataDoctype() {
		var selectedArray = $(gridDoctype).datagrid('getSelections');
		if (selectedArray.length == 1) {
			var url = url_formActionDoctype + '?operateType=' + PAGETYPE.EDIT
					+ '&ids=' + selectedArray[0].ID;
			$.farm.openWindow({
				id : 'winDoctype',
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
	function delDataDoctype() {
		var selectedArray = $(gridDoctype).datagrid('getSelections');
		if (selectedArray.length > 0) {
			// 有数据执行操作
			var str = selectedArray.length + MESSAGE_PLAT.SUCCESS_DEL_NEXT_IS;
			$.messager.confirm(MESSAGE_PLAT.PROMPT, str, function(flag) {
				if (flag) {
					$(gridDoctype).datagrid('loading');
					$.post(url_delActionDoctype + '?ids='
							+ $.farm.getCheckedIds(gridDoctype, 'ID'), {},
							function(flag) {
								$(gridDoctype).datagrid('loaded');
								if (flag.STATE == 0) {
									$(gridDoctype).datagrid('reload');
								} else {
									var str = MESSAGE_PLAT.ERROR_SUBMIT
											+ flag.MESSAGE;
									$.messager.alert(MESSAGE_PLAT.ERROR, str,
											'error');
								}
							}, 'json');
				}
			});
		} else {
			$.messager.alert(MESSAGE_PLAT.PROMPT, MESSAGE_PLAT.CHOOSE_ONE,
					'info');
		}
	}
</script>
</html>