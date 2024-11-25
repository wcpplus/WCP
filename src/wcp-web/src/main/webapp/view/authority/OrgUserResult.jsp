<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<table id="dataPostUserGrid">
	<thead>
		<tr>
			<th data-options="field:'ck',checkbox:true"></th>
			<th field="USERNAME" data-options="sortable:true" width="30">
				名称
			</th>
			<th field="USERSTATE" data-options="sortable:true" width="20">
				状态
			</th>
			<th field="LOGINTIME" data-options="sortable:true" width="30">
				登陆时间
			</th>
		</tr>
	</thead>
</table>
<script type="text/javascript">
	var url_delActionPostUser = "organization/removePostUser.do";//删除URL
	var url_searchActionPostUser = "user/orgUserQuery.do?ids="
			+ $('#domTabsId').val();//查询URL
	var title_windowPostUser = "用户管理";//功能名称
	var gridPostUser;//数据表格对象
	var searchPostUser;//条件查询组件对象
	var pageType = '${pageset.operateType}';
	var toolBarPostUser = [  {
		id : 'del',
		text : '移除',
		iconCls : 'icon-remove',
		handler : delDataPostUser
	} ];
	$(function() {
	/*  if (pageType == '0') {
			toolBarPostUser = [];
		}  */
		//初始化数据表格
		gridPostUser = $('#dataPostUserGrid').datagrid( {
			url : url_searchActionPostUser,
			fit : true,
			fitColumns : true,
			'toolbar' : toolBarPostUser,
			pagination : true,
			closable : true,
			checkOnSelect : true,
			striped : true,
			border : true,
			rownumbers : true,
			ctrlSelect : true
		});
		//初始化条件查询
		searchPostUser = $('#searchPostUserForm').searchForm( {
			gridObj : gridPostUser
		});
	});
	//删除
	function delDataPostUser() {
		var selectedArray = $(gridPostUser).datagrid('getSelections');
		if (selectedArray.length == 1) {
			// 有数据执行操作
			var str = "该操作将从组织机构中移除所选用户是否继续？";
			$.messager.confirm(MESSAGE_PLAT.PROMPT, str, function(flag) {
				if (flag) {
					$(gridPostUser).datagrid('loading');
					$.post(url_delActionPostUser + '?id='+selectedArray[0].POSTID+'&ids='
							+ selectedArray[0].USERID, {},
							function(flag) {
								$(gridPostUser).datagrid('loaded');
								var jsonObject = JSON.parse(flag,null);
								if (jsonObject.model.STATE == 0) {
									$(gridPostUser).datagrid('reload');
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
			$.messager.alert(MESSAGE_PLAT.PROMPT, MESSAGE_PLAT.CHOOSE_ONE_ONLY,
					'info');
		}
	}
</script>