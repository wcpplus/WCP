<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!--小组用户-->
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false">
		<form id="searchDocgroupuserForm">
			<table class="editTable">
				<tr>
					<td class="title">
						状态:
					</td>
					<td>
						<select name="a.PSTATE:=">
							<option value="">
							</option>
							<option value="1">
								在用
							</option>
							<option value="2">
								删除
							</option>
							<option value="3">
								申请
							</option>
							<option value="0">
								邀请
							</option>
						</select>
					</td>
					<td>
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
		<table id="dataDocgroupuserGrid">
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true"></th>
					<th field="USERNAME" data-options="sortable:true" width="70">
						用户名称
					</th>
					<th field="EDITIS" data-options="sortable:true" width="70">
						是否有编辑权限
					</th>
					<th field="LEADIS" data-options="sortable:true" width="50">
						是否管理员
					</th>
					<th field="PSTATE" data-options="sortable:true" width="20">
						状态
					</th>
				</tr>
			</thead>
		</table>
	</div>
</div>
<script type="text/javascript">
	var url_delActionDocgroupuser = "docgroupUser/del.do";//删除URL
	var url_formActionDocgroupuser = "docgroupUser/form.do";//增加、修改、查看URL
	var url_searchActionDocgroupuser = "docgroupUser/query.do?ids=${ids}";//查询URL
	var title_windowDocgroupuser = "小组成员管理";//功能名称
	var gridDocgroupuser;//数据表格对象
	var searchDocgroupuser;//条件查询组件对象
	var toolBarDocgroupuser = [ {
		id : 'add',
		text : '添加',
		iconCls : 'icon-add',
		handler : addDataDocgroupuser
	}, {
		id : 'edit',
		text : '设置管理员',
		iconCls : 'icon-group_green_new',
		handler : setAdmin
	}, {
		id : 'del',
		text : '取消管理员',
		iconCls : 'icon-group_green_edit',
		handler : reSetAdmin
	}, {
		id : 'del',
		text : '移出',
		iconCls : 'icon-remove',
		handler : delDataDocgroupuser
	} ];
	$(function() {
		//初始化数据表格
		gridDocgroupuser = $('#dataDocgroupuserGrid').datagrid( {
			url : url_searchActionDocgroupuser,
			fit : true,
			fitColumns : true,
			'toolbar' : toolBarDocgroupuser,
			pagination : true,
			closable : true,
			checkOnSelect : true,
			striped : true,
			border : false,
			rownumbers : true,
			ctrlSelect : true
		});
		//初始化条件查询
		searchDocgroupuser = $('#searchDocgroupuserForm').searchForm( {
			gridObj : gridDocgroupuser
		});
	});
	function setAdmin() {
		var selectedArray = $(gridDocgroupuser).datagrid('getSelections');
		if (selectedArray.length > 0) {
			// 有数据执行操作
			$.messager.confirm(MESSAGE_PLAT.PROMPT, "是否设置管理员权限？",
					function(flag) {
						if (flag) {
							$(gridDocgroupuser).datagrid('loading');
							$.post("docgroupUser/FarmDocgroupUserSetAdmin.do"
									+ '?ids='
									+ $.farm.getCheckedIds(gridDocgroupuser,
											'ID'), {}, function(flag) {
								$(gridDocgroupuser).datagrid('loaded');
								if (flag.STATE == 0) {
									$(gridDocgroupuser).datagrid('reload');
								} else {
									var str = MESSAGE_PLAT.ERROR_SUBMIT
											+ flag.MESSAGE;
									$.messager.alert(MESSAGE_PLAT.ERROR, str, 'error');
								}
							}, 'json');
						}
					});
		} else {
			$.messager.alert(MESSAGE_PLAT.PROMPT, MESSAGE_PLAT.CHOOSE_ONE,
					'info');
		}
	}
	function reSetAdmin() {
		var selectedArray = $(gridDocgroupuser).datagrid('getSelections');
		if (selectedArray.length > 0) {
			// 有数据执行操作
			$.messager.confirm(MESSAGE_PLAT.PROMPT, "是否取消管理员权限？",
					function(flag) {
						if (flag) {
							$(gridDocgroupuser).datagrid('loading');
							$.post("docgroupUser/FarmDocgroupUserResetAdmin.do"
									+ '?ids='
									+ $.farm.getCheckedIds(gridDocgroupuser,
											'ID'), {}, function(flag) {
								$(gridDocgroupuser).datagrid('loaded');
								if (flag.STATE == 0) {
									$(gridDocgroupuser).datagrid('reload');
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

	//导入
	function addDataDocgroupuser() {
		var url = "post/chooseUser.do";
		$.farm.openWindow( {
			id : 'chooseUserWin',
			width : 600,
			height : 400,
			modal : true,
			url : url,
			title : '导入用户'
		});
		chooseWindowCallBackHandle = function(row) {
			var userids;
			$(row).each(function(i, obj) {
				if (userids) {
					userids = userids + ',' + obj.ID;
				} else {
					userids = obj.ID;
				}
			});
			$.post("docgroupUser/add.do", {
				'userids' : userids,
				ids : '${ids}'
			}, function(flag) {
				if (flag.STATE == 0) {
					$('#chooseUserWin').window('close');
					$(gridDocgroupuser).datagrid('reload');
				} else {
					$.messager.alert(MESSAGE_PLAT.ERROR, flag.MESSAGE,
							'error');
				}
			}, 'json');
		};
	}
	//修改
	function editDataDocgroupuser() {
		var selectedArray = $(gridDocgroupuser).datagrid('getSelections');
		if (selectedArray.length == 1) {
			var url = url_formActionDocgroupuser + '?operateType='
					+ PAGETYPE.EDIT + '&ids=' + selectedArray[0].ID;
			$.farm.openWindow( {
				id : 'winDocgroupuser',
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
	function delDataDocgroupuser() {
		var selectedArray = $(gridDocgroupuser).datagrid('getSelections');
		if (selectedArray.length > 0) {
			// 有数据执行操作
			var str = selectedArray.length + MESSAGE_PLAT.SUCCESS_DEL_NEXT_IS;
			$.messager.confirm(MESSAGE_PLAT.PROMPT, str, function(flag) {
				if (flag) {
					$(gridDocgroupuser).datagrid('loading');
					$.post(url_delActionDocgroupuser + '?ids='
							+ $.farm.getCheckedIds(gridDocgroupuser, 'ID'), {},
							function(flag) {
								$(gridDocgroupuser).datagrid('loaded');
								if (flag.STATE == 0) {
									$(gridDocgroupuser).datagrid('reload');
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