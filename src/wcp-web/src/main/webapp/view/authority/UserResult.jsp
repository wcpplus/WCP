<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<PF:basePath/>">
		<title>用户数据管理</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<jsp:include page="/view/conf/include.jsp"></jsp:include>
	</head>
	<body class="easyui-layout">
		<div data-options="region:'north',border:false">
			<form id="searchUserForm">
				<table class="editTable">
					<tr>
						<td class="title">
							名称:
						</td>
						<td>
							<input name="a.NAME:like" type="text">
						</td>
						<td class="title">
							登陆名:
						</td>
						<td>
							<input name="LOGINNAME:like" type="text">
						</td>
						<td class="title">
							状态:
						</td>
						<td>
							<select name="a.STATE:=" style="width: 120px;">
								<option value="1">
									可用
								</option>
								<option value="0">
									禁用
								</option>
								<option value="2">
									删除
								</option>
							</select>
						</td>
					</tr>
					<tr>
						<td class="title">
							类型:
						</td>
						<td>
							<select name="a.TYPE:=" style="width: 120px;">
								<option value="">
								</option>
								<option value="1">
									系统用户
								</option>
								<option value="2">
									其他
								</option>
								<option value="3">
									超级用户
								</option>
							</select>
						</td>
						<td class="title"></td>
						<td></td>
						<td class="title"></td>
						<td></td>
					</tr>
					<tr style="text-align: center;">
						<td colspan="6">
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
			<table id="dataUserGrid">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true"></th>
						<th field="NAME" data-options="sortable:true" width="30">
							名称
						</th>
						<th field="LOGINNAME" data-options="sortable:true" width="30">
							登陆名
						</th>
						<th field="TYPE" data-options="sortable:true" width="20">
							类型
						</th>
						<th field="STATE" data-options="sortable:true" width="20">
							状态
						</th>
						<th field="ORGNAME" data-options="sortable:true" width="30">
							组织机构
						</th>
						<th field="LOGINTIME" data-options="sortable:true" width="30">
							登陆时间
						</th>
					</tr>
				</thead>
			</table>
		</div>
	</body>
	<script type="text/javascript">
	var url_delActionUser = "user/del.do";//删除URL
	var url_formActionUser = "user/form.do";//增加、修改、查看URL
	var url_searchActionUser = "user/query.do";//查询URL 
	var title_windowUser = "用户管理";//功能名称
	var gridUser;//数据表格对象
	var searchUser;//条件查询组件对象
	var toolBarUser = [ {
		id : 'view',
		text : '查看',
		iconCls : 'icon-tip',
		handler : viewDataUser
	}, {
		id : 'add',
		text : '新增',
		iconCls : 'icon-add',
		handler : addDataUser
	}, {
		id : 'edit',
		text : '修改',
		iconCls : 'icon-edit',
		handler : editDataUser
	}, {
		id : 'del',
		text : '删除',
		iconCls : 'icon-remove',
		handler : delDataUser
	}, {
		id : 'initPassword',
		text : '密码初始化',
		iconCls : 'icon-lock',
		handler : initPasswd
	}];
	$(function() {
		//初始化数据表格
		gridUser = $('#dataUserGrid').datagrid( {
			url : url_searchActionUser,
			fit : true,
			fitColumns : true,
			'toolbar' : toolBarUser,
			pagination : true,
			closable : true,
			checkOnSelect : true,
			striped : true,
			queryParams : {
				'a.STATE:=' : 'easyui',
			},
			border : false,
			rownumbers : true,
			ctrlSelect : true
		});
		//初始化条件查询
		searchUser = $('#searchUserForm').searchForm( {
			gridObj : gridUser
		});
	});
	//查看
	function viewDataUser() {
		var selectedArray = $(gridUser).datagrid('getSelections');
		if (selectedArray.length == 1) {
			var url = url_formActionUser + '?operateType=' + PAGETYPE.VIEW
					+ '&ids=' + selectedArray[0].ID;
			$.farm.openWindow( {
				id : 'winUser',
				width : 600,
				height : 400,
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
	function addDataUser() {
		var url = url_formActionUser + '?ids=123123132&operateType=' + PAGETYPE.ADD;
		$.farm.openWindow( {
			id : 'winUser',
			width : 600,
			height : 400,
			modal : true,
			url : url,
			title : '新增'
		});
	}
	//修改
	function editDataUser() {
		var selectedArray = $(gridUser).datagrid('getSelections');
		if (selectedArray.length == 1) {
			var url = url_formActionUser + '?operateType=' + PAGETYPE.EDIT
					+ '&ids=' + selectedArray[0].ID;
			$.farm.openWindow( {
				id : 'winUser',
				width : 600,
				height : 400,
				modal : true,
				url : url,
				title : '修改'
			});
		} else {
			$.messager.alert(MESSAGE_PLAT.PROMPT, MESSAGE_PLAT.CHOOSE_ONE_ONLY,
					'info');
		}
	}

	//密码初始化
	function initPasswd() {
		var selectedArray = $(gridUser).datagrid('getSelections');
		if (selectedArray.length > 0) {
			// 有数据执行操作
			$.messager.confirm(MESSAGE_PLAT.PROMPT, "该操作将无法返回，是否继续执行密码初始化?",
					function(flag) {
						if (flag) {
							$.post("user/init.do"
									+ '?ids='
									+ $.farm.getCheckedIds(gridUser, 'ID'), {},
									function(flag) {
										var jsonObject = JSON.parse(flag, null);
										if (jsonObject.model.STATE == 0) {
											$.messager.alert(
													MESSAGE_PLAT.PROMPT, MESSAGE_PLAT.SUCCESS,
													'info');
										} else {
											var str = MESSAGE_PLAT.ERROR_SUBMIT
													+ flag.pageset.message;
											$.messager.alert(
													MESSAGE_PLAT.ERROR, str,
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
	function delDataUser() {
		var selectedArray = $(gridUser).datagrid('getSelections');
		if (selectedArray.length > 0) {
			// 有数据执行操作
			var str = selectedArray.length + MESSAGE_PLAT.SUCCESS_DEL_NEXT_IS;
			$.messager.confirm(MESSAGE_PLAT.PROMPT, str, function(flag) {
				if (flag) {
					$(gridUser).datagrid('loading');
					$.post(url_delActionUser + '?ids='
							+ $.farm.getCheckedIds(gridUser, 'ID'), {},
							function(flag) {
								var jsonObject = JSON.parse(flag, null);
								$(gridUser).datagrid('loaded');
								if (jsonObject.model.STATE == 0) {
									$(gridUser).datagrid('reload');
								} else {
									var str = MESSAGE_PLAT.ERROR_SUBMIT
											+ flag.pageset.message;
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