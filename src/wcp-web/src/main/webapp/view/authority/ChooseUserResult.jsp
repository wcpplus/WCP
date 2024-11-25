<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false">
		<form id="searchChooseUserForm">
			<table class="editTable">
				<tr>
					<td class="title">
						名称:
					</td>
					<td>
						<input name="NAME:like" type="text">
					</td>
					<td class="title">
						登陆名:
					</td>
					<td>
						<input name="LOGINNAME:like" type="text">
					</td>
				</tr>
				<tr>
					<td class="title">
						类型:
					</td>
					<td>
						<select name="TYPE:=" style="width: 120px;">
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
					<td class="title">
						状态:
					</td>
					<td>
						<select name="a.STATE:=" style="width: 120px;">
							<option value="">
							</option>
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
		<table id="dataChooseUserGrid">
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
</div>
<script type="text/javascript">
	var url_searchActionChooseUser = "user/query.do";//查询URL  admin/FarmAuthorityUserQuery.do
	var title_windowChooseUser = "用户管理";//功能名称
	var gridChooseUser;//数据表格对象
	var searchChooseUser;//条件查询组件对象
	var toolBarChooseUser = [ {
		id : 'choose',
		text : '选中',
		iconCls : 'icon-ok',
		handler : chooseChooseUserFun
	} ];
	$(function() {
		//初始化数据表格
		gridChooseUser = $('#dataChooseUserGrid').datagrid( {
			url : url_searchActionChooseUser,
			fit : true,
			fitColumns : true,
			'toolbar' : toolBarChooseUser,
			pagination : true,
			closable : true,
			checkOnSelect : true,
			striped : true,
			border : false,
			rownumbers : true,
			ctrlSelect : true
		});
		//初始化条件查询
		searchChooseUser = $('#searchChooseUserForm').searchForm( {
			gridObj : gridChooseUser
		});
	});
	function chooseChooseUserFun() {
		var selectedArray = $(gridChooseUser).datagrid('getSelections');
		if (selectedArray.length > 0) {
			chooseWindowCallBackHandle(selectedArray);
		} else {
			$.messager.alert(MESSAGE_PLAT.PROMPT, MESSAGE_PLAT.CHOOSE_ONE,
					'info');
		}
	}
</script>
