<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<PF:basePath/>">
		<title>文档数据管理</title>
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
		<div data-options="region:'west',split:true,border:false"
			style="width: 200px;">
			<div class="TREE_COMMON_BOX_SPLIT_DIV">
				<a id="FarmdocTreeReload" href="javascript:void(0)"
					class="easyui-linkbutton" data-options="plain:true"
					iconCls="icon-reload">刷新菜单</a>
				<a id="FarmdocTreeOpenAll" href="javascript:void(0)"
					class="easyui-linkbutton" data-options="plain:true"
					iconCls="icon-sitemap">全部展开</a>
			</div>
			<ul id="FarmdocTree"></ul>
		</div>
		<div class="easyui-layout" data-options="region:'center',border:false">
			<div data-options="region:'north',border:false">
				<form id="searchFarmdocForm">
					<table class="editTable">
						<tr>
							<td class="title">
								标题:
							</td>
							<td>
								<input name="TITLE:like" type="text">
							</td>
							<td class="title">
								摘要:
							</td>
							<td>
								<input name="DOCDESCRIBE:like" type="text">
							</td>
							<td class="title">
								分类:
							</td>
							<td>
								<input id="PARENTTITLE_RULE" type="text" readonly="readonly"
									style="background: #F3F3E8">
								<input id="PARENTID_RULE" name="TYPEID:=" type="hidden">
							</td>
							<td class="title">
								所属小组:
							</td>
							<td>
								<input name="GROUPNAME:like" type="text">
							</td>
						</tr>
						<tr>
							<td class="title">
								阅读权限:
							</td>
							<td>
								<select name="READPOP:=">
									<option value="">
									</option>
									<option value="1">
										分类
									</option>
									<option value="0">
										创建人
									</option>
									<option value="2">
										小组
									</option>
									<option value="3">
										禁止编辑
									</option>
								</select>
							</td>
							<td class="title">
								编辑权限:
							</td>
							<td>
								<select name="WRITEPOP:=">
									<option value="">
									</option>
									<option value="1">
										分类
									</option>
									<option value="0">
										创建人
									</option>
									<option value="2">
										小组
									</option>
									<option value="3">
										禁止编辑
									</option>
								</select>
							</td>
							<td class="title">
								状态:
							</td>
							<td>
								<select name="STATE:=">
									<option value="">
									</option>
									<option value="1">
										开放
									</option>
									<option value="0">
										禁用
									</option>
									<option value="2">
										待审核
									</option>
								</select>
							</td>
							<td class="title">
								状态:
							</td>
							<td>
							</td>
						</tr>
						<tr style="text-align: center;">
							<td colspan="8">
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
				<table id="dataFarmdocGrid">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true"></th>
							<th field="TITLE" data-options="sortable:true" width="20">
								标题
							</th>
							<th field="DOCDESCRIBE" data-options="sortable:true" width="20">
								摘要
							</th>
							<th field="AUTHOR" data-options="sortable:true" width="20">
								作者
							</th>
							<th field="PUBTIME" data-options="sortable:true" width="40">
								发布时间
							</th>
							<th field="STATE" data-options="sortable:true" width="20">
								状态
							</th>
							<th field="READPOP" data-options="sortable:true" width="20">
								读权限
							</th>
							<th field="WRITEPOP" data-options="sortable:true" width="20">
								写权限
							</th>
							<th field="DOMTYPE" data-options="sortable:true" width="20">
								内容类型
							</th>
							<th field="GROUPNAME" data-options="sortable:true" width="20">
								所属小组
							</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</body>
	<script type="text/javascript">
	var url_delActionFarmdoc = "doc/del.do";//删除URL
	var url_formActionFarmdoc = "doc/form.do";//增加、修改、查看URL
	var url_searchActionFarmdoc = "doc/query.do";//查询URL
	var title_windowFarmdoc = "文档管理";//功能名称
	var gridFarmdoc;//数据表格对象
	var searchFarmdoc;//条件查询组件对象
	var toolBarFarmdoc = [{
		id : 'view',
		text : '查看',
		iconCls : 'icon-tip',
		handler : viewDataFarmdoc
	}, {
		id : 'add',
		text : '新增',
		iconCls : 'icon-add',
		handler : addDataFarmdoc
	}, 
	//{
	//	id : 'add',
	//	text : '批量添加文件资源',
	//	iconCls : 'icon-add',
	//	handler : addmultiDataFarmdoc
	//}, 
	{
		id : 'edit',
		text : '修改',
		iconCls : 'icon-edit',
		handler : editDataFarmdoc
	}, {
		id : 'del',
		text : '删除',
		iconCls : 'icon-remove',
		handler : delDataFarmdoc
	}, {
		id : 'settion',
		text : '批量设置权限',
		iconCls : 'icon-settings',
		handler : stttionPop
	}, {
		id : 'move2Type',
		text : '设置分类',
		iconCls : 'icon-move_to_folder',
		handler : move2Type
	}, {
		id : 'docComment',
		text : '管理文档留言',
		iconCls : 'icon-comment',
		handler : commentMng
	} ];
	$(function() {
		//初始化数据表格
		gridFarmdoc = $('#dataFarmdocGrid').datagrid( {
			url : url_searchActionFarmdoc,
			fit : true,
			fitColumns : true,
			'toolbar' : toolBarFarmdoc,
			pagination : true,
			closable : true,
			checkOnSelect : true,
			striped : true,
			border : false,
			rownumbers : true,
			ctrlSelect : true
		});
		//初始化条件查询
		searchFarmdoc = $('#searchFarmdocForm').searchForm( {
			gridObj : gridFarmdoc
		});
		$('#FarmdocTree').tree( {
			url : 'doctype/FarmDoctypeLoadTreeNode.do',
			onSelect : function(node) {
				$('#PARENTID_RULE').val(node.id);
				$('#PARENTTITLE_RULE').val(node.text);
				searchFarmdoc.dosearch( {
					'ruleText' : searchFarmdoc.arrayStr()
				});
			},
			loadFilter:function(data,parent){
				return data.treeNodes;
			}
		});
		$('#FarmdocTreeReload').bind('click', function() {
			$('#FarmdocTree').tree('reload');
		});
		$('#FarmdocTreeOpenAll').bind('click', function() {
			$('#FarmdocTree').tree('expandAll');
		});
	});
	//管理文档留言
	function commentMng() {
		var selectedArray = $(gridFarmdoc).datagrid('getSelections');
		if (selectedArray.length == 1) {
			$.farm.openWindow( {
				id : 'winsettionFarmdoc',
				width : 600,
				height : 300,
				modal : true,
				url : "doc/toManagerDocMsg.do" + '?operateType=2&ids='
						+ selectedArray[0].ID,
				title : '评论'
			});
		} else {
			$.messager.alert(MESSAGE_PLAT.PROMPT, MESSAGE_PLAT.CHOOSE_ONE_ONLY,
					'info');
		}
	}
	//查看
	function viewDataFarmdoc() {
		var selectedArray = $(gridFarmdoc).datagrid('getSelections');
		if (selectedArray.length == 1) {
			var url = url_formActionFarmdoc + '?operateType='
					+ PAGETYPE.VIEW + '&ids=' + selectedArray[0].ID;
			$.farm.openWindow( {
				id : 'winFarmdoc',
				width : 600,
				height : 450,
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
	function addDataFarmdoc() {
		var url = url_formActionFarmdoc + '?id='
		 		+ $('#PARENTID_RULE').val() + '&operateType='
				+ PAGETYPE.ADD;
		$.farm.openWindow( {
			id : 'winFarmdoc',
			width : 600,
			height : 450,
			modal : true,
			url : url,
			title : '新增'
		});
	}
	//批量新增
	function addmultiDataFarmdoc() {
		var url = url_formActionFarmdoc + '?id='
		 		+ $('#PARENTID_RULE').val() + '&operateType='
				+ PAGETYPE.ADD;
		$.farm.openWindow( {
			id : 'winFarmdoc',
			width : 600,
			height : 450,
			modal : true,
			url : url,
			title : '新增'
		});
	}
	
	
	
	//设置权限
	function stttionPop() {
		var selectedArray = $(gridFarmdoc).datagrid('getSelections');
		if (selectedArray.length >0) {
			$.farm.openWindow( {
				id : 'winsettionFarmdoc',
				width : 600,
				height : 200,
				modal : true,
				url : "doc/FarmDocRightedit.do" + '?operateType=2&ids='
						+ selectedArray[0].ID,
				title : '批量设置权限'
			});
		} else {
			$.messager.alert(MESSAGE_PLAT.PROMPT, MESSAGE_PLAT.CHOOSE_ONE,
			'info');
		}
	}
	//修改
	function editDataFarmdoc() {
		var selectedArray = $(gridFarmdoc).datagrid('getSelections');
		if (selectedArray.length == 1) {
			var url = url_formActionFarmdoc + '?operateType='
					+ PAGETYPE.EDIT + '&ids=' + selectedArray[0].ID;
			$.farm.openWindow( {
				id : 'winFarmdoc',
				width : 600,
				height : 450,
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
	function delDataFarmdoc() {
		var selectedArray = $(gridFarmdoc).datagrid('getSelections');
		if (selectedArray.length > 0) {
			// 有数据执行操作
			var str = selectedArray.length + MESSAGE_PLAT.SUCCESS_DEL_NEXT_IS;
			$.messager.confirm(MESSAGE_PLAT.PROMPT, str, function(flag) {
				if (flag) {
					$(gridFarmdoc).datagrid('loading');
					$.post(url_delActionFarmdoc + '?ids='
							+ $.farm.getCheckedIds(gridFarmdoc, 'ID'), {},
							function(flag) {
								$(gridFarmdoc).datagrid('loaded');
								if (flag.STATE == 0) {
									$(gridFarmdoc).datagrid('reload');
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
	
	//移动到分类
	function move2Type() {
		var selectedArray = $(gridFarmdoc).datagrid('getSelections');
		if (selectedArray.length > 0) {
			$.farm.openWindow( {
				id : 'toChooseTypeWin',
				width : 250,
				height : 300,
				modal : true,
				url : "doctype/form.do?operateType=3",
				title : '移动机构'
			});
		} else {
			$.messager.alert(MESSAGE_PLAT.PROMPT, MESSAGE_PLAT.CHOOSE_ONE,
					'info');
		}
	}
	
	//选择树回调函数
	function chooseTypeHook(obj){
		var docIds = $.farm.getCheckedIds(gridFarmdoc, 'ID');
		var typeId = obj.id;
		var url = "doc/move2Type.do";
		$.post(url, {docIds:docIds, typeId:typeId}, function(data){
			if(data.STATE == 0){
				$(gridFarmdoc).datagrid('reload');
				$("#toChooseTypeWin").window('close');
			}
		}, "json");
	}
</script>
</html>