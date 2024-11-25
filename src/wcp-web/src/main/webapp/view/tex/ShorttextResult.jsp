<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<PF:basePath/>">
<title>知识段落文本数据管理</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<jsp:include page="/view/conf/include.jsp"></jsp:include>
</head>
<body class="easyui-layout">
	<div data-options="region:'north',border:false">
		<form id="searchShorttextForm">
			<table class="editTable">
				<tr>
					<td class="title">业务知识ID:</td>
					<td><input name="SID:=" type="text"></td>
					<td class="title">知识名称:</td>
					<td><input name="TITLE:like" type="text"></td>  
					<td class="title">ID:</td>
					<td><input name="ID:like" type="text"></td>
				</tr> 
				<tr>
					<td class="title">状态:</td>
					<td><select name="PSTATE:like" >
							<option value=""></option>
							<option value="1">初始</option>
							<option value="2">嵌入</option>
							<option value="3">索引</option>
					</select></td>
					<td colspan="4" style="text-align: center;"><a id="a_search" href="javascript:void(0)"
						class="easyui-linkbutton" iconCls="icon-search">查询</a> <a
						id="a_reset" href="javascript:void(0)" class="easyui-linkbutton"
						iconCls="icon-reload">清除条件</a></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false">
		<table id="dataShorttextGrid">
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true"></th>
					<th field="TITLE" data-options="sortable:true" width="80">知识名称</th>
					<th field="PALL" data-options="sortable:true" width="20">段落</th>
					<th field="LEN" data-options="sortable:true" width="20">段落长度</th>
					<th field="SID" data-options="sortable:true" width="20">业务ID</th>
					<th field="STYPE" data-options="sortable:true" width="20">业务类型</th>
					<th field="CTIME" data-options="sortable:true" width="40">创建时间</th>
					<th field="EMBTIME" data-options="sortable:true" width="40">语义模型创建时间</th>
					<th field="EMBTMODEL" data-options="sortable:true" width="40">模型类型</th>
					<th field="PSTATE" data-options="sortable:true" width="40">状态</th>
				</tr>
			</thead>
		</table>
	</div>
</body>
<script type="text/javascript">
	var url_delActionShorttext = "shorttext/delembedding.do";//删除URL
	var url_formActionShorttext = "shorttext/form.do";//增加、修改、查看URL
	var url_searchActionShorttext = "shorttext/query.do";//查询URL
	var title_windowShorttext = "知识段落文本管理";//功能名称
	var gridShorttext;//数据表格对象
	var searchShorttext;//条件查询组件对象
	var toolBarShorttext = [ {
		id : 'view',
		text : '配置信息',
		iconCls : 'icon-lightbulb',
		handler : viewIndexTypeinfo
	},{
		id : 'view',
		text : '查看索引',
		iconCls : 'icon-tip',
		handler : viewDataShorttext
	} 
	//, {
	//	id : 'add',
	//	text : '新增',
	//	iconCls : 'icon-add',
	//	handler : addDataShorttext
	//}
	//, {
	//	id : 'edit',
	//	text : '修改',
	//	iconCls : 'icon-edit',
	//	handler : editDataShorttext
	//}
	, {
		id : 'del',
		text : '删除记录',
		iconCls : 'icon-remove',
		handler : delDataShorttext
	}
	, {
		id : 'del',
		text : '生成模型',
		iconCls : 'icon-communication',
		handler : embeddingStart
	}
	, {
		id : 'del',
		text : '删除模型',
		iconCls : 'icon-remove',
		handler : delDataEmbedding
	},  {
		id : 'del',
		text : '创建索引',
		iconCls : 'icon-communication',
		handler : insertEmbdbStart
	},{
		id : 'del',
		text : '刪除索引',
		iconCls : 'icon-remove',
		handler : restatetext
	}
	  ];
	$(function() {
		//初始化数据表格
		gridShorttext = $('#dataShorttextGrid').datagrid({
			url : url_searchActionShorttext,
			fit : true,
			fitColumns : true,
			'toolbar' : toolBarShorttext,
			pagination : true,
			closable : true,
			checkOnSelect : true,
			border : false,
			striped : true,
			rownumbers : true,
			ctrlSelect : true
		});
		//初始化条件查询
		searchShorttext = $('#searchShorttextForm').searchForm({
			gridObj : gridShorttext
		});
	});
	
	//配置信息
	function viewIndexTypeinfo() {
		$.farm.openWindow({
			id : 'winShorttext',
			width : 600,
			height : 300,
			modal : true,
			url : 'shorttext/indexType.do',
			title : '索引配置信息'
		});
	}
	
	//查看
	function viewDataShorttext() {
		var selectedArray = $(gridShorttext).datagrid('getSelections');
		if (selectedArray.length == 1) {
			var url = url_formActionShorttext + '?pageset.pageType='
					+ PAGETYPE.VIEW + '&ids=' + selectedArray[0].ID;
			$.farm.openWindow({
				id : 'winShorttext',
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
	function addDataShorttext() {
		var url = url_formActionShorttext + '?operateType=' + PAGETYPE.ADD;
		$.farm.openWindow({
			id : 'winShorttext',
			width : 600,
			height : 300,
			modal : true,
			url : url,
			title : '新增'
		});
	}
	//修改
	function editDataShorttext() {
		var selectedArray = $(gridShorttext).datagrid('getSelections');
		if (selectedArray.length == 1) {
			var url = url_formActionShorttext + '?operateType=' + PAGETYPE.EDIT
					+ '&ids=' + selectedArray[0].ID;
			$.farm.openWindow({
				id : 'winShorttext',
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

	//启动向量生成器
	function embeddingStart() {
		$.messager.confirm(MESSAGE_PLAT.PROMPT, "是否立即生成模型，该操作将在后台执行？", function(flag) {
			if (flag) {
				$.post('shorttext/embedding.do', {}, function(flag) {
					var jsonObject = JSON.parse(flag, null);
					if (jsonObject.STATE == 0) {
						$.messager.alert(MESSAGE_PLAT.PROMPT, "启动成功!", 'info');
					} else {
						var str = MESSAGE_PLAT.ERROR_SUBMIT + jsonObject.MESSAGE;
						$.messager.alert(MESSAGE_PLAT.ERROR, str, 'error');
					}
				});
			}
		});
	}

	//加载模型索引
	function insertEmbdbStart() {
		$.messager.confirm(MESSAGE_PLAT.PROMPT, "是否立即將模型加载到索引库中，该操作将在后台执行？", function(flag) {
			if (flag) {
				$.post('shorttext/insertEmbdb.do', {}, function(flag) {
					var jsonObject = JSON.parse(flag, null);
					if (jsonObject.STATE == 0) {
						$.messager.alert(MESSAGE_PLAT.PROMPT, "启动成功!", 'info');
					} else {
						var str = MESSAGE_PLAT.ERROR_SUBMIT + jsonObject.MESSAGE;
						$.messager.alert(MESSAGE_PLAT.ERROR, str, 'error');
					}
				});
			}
		});
	}
	//重置状态
	function restatetext() {
		var selectedArray = $(gridShorttext).datagrid('getSelections');
		if (selectedArray.length > 0) {
			// 有数据执行操作
			var str = selectedArray.length + MESSAGE_PLAT.SUCCESS_DEL_NEXT_IS;
			$.messager.confirm(MESSAGE_PLAT.PROMPT, "是否立即重置数据状态?", function(flag) {
				if (flag) {
					$(gridShorttext).datagrid('loading');
					$.post('shorttext/restate.do?ids='
							+ $.farm.getCheckedIds(gridShorttext, 'ID'), {},
							function(flag) {
								var jsonObject = JSON.parse(flag, null);
								$(gridShorttext).datagrid('loaded');
								if (jsonObject.STATE == 0) {
									$(gridShorttext).datagrid('reload');
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
	//删除
	function delDataEmbedding() {
		var selectedArray = $(gridShorttext).datagrid('getSelections');
		if (selectedArray.length > 0) {
			// 有数据执行操作
			var str = selectedArray.length + MESSAGE_PLAT.SUCCESS_DEL_NEXT_IS;
			$.messager.confirm(MESSAGE_PLAT.PROMPT, str, function(flag) {
				if (flag) {
					$(gridShorttext).datagrid('loading');
					$.post(url_delActionShorttext + '?ids='
							+ $.farm.getCheckedIds(gridShorttext, 'ID'), {},
							function(flag) {
								var jsonObject = JSON.parse(flag, null);
								$(gridShorttext).datagrid('loaded');
								if (jsonObject.STATE == 0) {
									$(gridShorttext).datagrid('reload');
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
	//删除
	function delDataShorttext() {
		var selectedArray = $(gridShorttext).datagrid('getSelections');
		if (selectedArray.length > 0) {
			// 有数据执行操作
			var str = selectedArray.length + MESSAGE_PLAT.SUCCESS_DEL_NEXT_IS;
			$.messager.confirm(MESSAGE_PLAT.PROMPT, str, function(flag) {
				if (flag) {
					$(gridShorttext).datagrid('loading');
					$.post('shorttext/del.do?ids='
							+ $.farm.getCheckedIds(gridShorttext, 'ID'), {},
							function(flag) {
								var jsonObject = JSON.parse(flag, null);
								$(gridShorttext).datagrid('loaded');
								if (jsonObject.STATE == 0) {
									$(gridShorttext).datagrid('reload');
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