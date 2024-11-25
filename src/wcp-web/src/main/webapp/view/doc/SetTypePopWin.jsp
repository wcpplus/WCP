<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!--设置阅读权限-->
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false">
		<form id="dom_chooseSearchSetreadpop">
			<table class="editTable">
				<tr>
					<td class="title">分类名称:</td>
					<td>${entity.name}</td>
				</tr>
				<tr>
					<td class="title">继承权限:</td>
					<td><c:if test="${extendmsg=='2'}">继承</c:if> <c:if
							test="${extendmsg!='2'}">无</c:if></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false">
		<table class="easyui-datagrid" id="dom_chooseGridSetreadpop">
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true"></th>
					<th field="POPTYPE" data-options="sortable:true" width="80">对象类型</th>
					<th field="ONAME" data-options="sortable:true" width="80">名称</th>
					<th field="FUNTYPE" data-options="sortable:true" width="80">来源</th>
				</tr>
			</thead>
		</table>
	</div>
</div>
<script type="text/javascript">
	var chooseGridSetreadpop;
	var chooseSearchfarmSetreadpop;
	var toolbar_chooseSetreadpop = [ {
		text : '添加用户',
		iconCls : 'icon-hire-me',
		handler : function() {
			$.farm.openWindow({
				id : 'chooseUserWin',
				width : 600,
				height : 400,
				modal : true,
				url : 'post/chooseUser.do',
				title : '选择用户'
			});
		}
	}, {
		text : '添加组织机构',
		iconCls : 'icon-customers',
		handler : function() {
			$.farm.openWindow({
				id : 'chooseOrgWin',
				width : 300,
				height : 400,
				modal : true,
				url : 'organization/chooseOrg.do',
				title : '选择组织机构'
			});
		}
	}, {
		text : '删除',
		iconCls : 'icon-remove',
		handler : delPop
	} ];
	function delPop() {
		var selectedArray = $(chooseGridSetreadpop).datagrid('getSelections');
		if (selectedArray.length > 0) {
			// 有数据执行操作
			var str = selectedArray.length + MESSAGE_PLAT.SUCCESS_DEL_NEXT_IS;
			$.messager.confirm(MESSAGE_PLAT.PROMPT, str, function(flag) {
				if (flag) {
					$(chooseGridSetreadpop).datagrid('loading');
					$.post('doctypeplus/delPop.do?ids='
							+ $.farm.getCheckedIds(chooseGridSetreadpop, 'ID'),
							{}, function(flag) {
								var jsonObject = JSON.parse(flag, null);
								$(chooseGridSetreadpop).datagrid('loaded');
								if (jsonObject.STATE == 0) {
									$(chooseGridSetreadpop).datagrid('reload');
									$(gridDoctype).datagrid('reload');
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

	function chooseOrgWindowCallBackHandle(node) {
		$.post('doctypeplus/addOrgToPop.do', {
			orgid : node.id,
			typeid : '${entity.id}',
			type : '${type}'
		}, function(flag) {
			if (flag.STATE == '0') {
				$.messager.alert(MESSAGE_PLAT.PROMPT, "组织机构添加成功!", 'info');
				$(chooseGridSetreadpop).datagrid('reload');
				$(gridDoctype).datagrid('reload');
			} else {
				$.messager.alert(MESSAGE_PLAT.PROMPT, flag.MESSAGE, 'error');
			}
		}, 'json');
	}

	//添加用户到权限中
	function chooseWindowCallBackHandle(selectedArray) {
		var userids;
		$(selectedArray).each(function(i, obj) {
			if (userids) {
				userids = userids + ',' + obj.ID;
			} else {
				userids = obj.ID;
			}
		});
		$.post('doctypeplus/addUserToPop.do', {
			userid : userids,
			typeid : '${entity.id}',
			type : '${type}'
		}, function(flag) {
			$("#chooseUserWin").window('close');
			if (flag.STATE == '0') {
				$(chooseGridSetreadpop).datagrid('reload');
				$(gridDoctype).datagrid('reload');
			} else {
				$.messager.alert(MESSAGE_PLAT.PROMPT, flag.MESSAGE, 'error');
			}
		}, 'json');
	}

	$(function() {
		chooseGridSetreadpop = $('#dom_chooseGridSetreadpop').datagrid({
			url : 'doctypeplus/SetTypePopQuery.do?id=${id}&type=${type}',
			fit : true,
			'toolbar' : toolbar_chooseSetreadpop,
			pagination : true,
			closable : true,
			checkOnSelect : true,
			striped : true,
			rownumbers : true,
			ctrlSelect : true,
			fitColumns : true
		});
		chooseSearchfarmSetreadpop = $('#dom_chooseSearchSetreadpop')
				.searchForm({
					gridObj : chooseGridSetreadpop
				});
	});
//-->
</script>